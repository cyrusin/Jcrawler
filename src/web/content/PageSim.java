package web.content;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.htmlparser.*;
import org.htmlparser.lexer.*;
import org.htmlparser.util.ParserException;
//import org.htmlparser.nodes.*;
public class PageSim {
	
	public static  double longestCommonSubsequence(ArrayList<Node> pageNodes1,ArrayList<Node> pageNodes2){
		int[][] num=new int[pageNodes1.size()+1][pageNodes2.size()+1];
		//Iterator it1=pageNodes1.iterator();
		//Iterator it2=pageNodes2.iterator();
		for(int i=1;i<=pageNodes1.size();i++){
			for(int j=1;j<=pageNodes2.size();j++){
				if(pageNodes1.get(i-1).toPlainTextString().equals(pageNodes2.get(j-1).toPlainTextString())){
					num[i][j]=1+num[i-1][j-1];
				}
				else{
					num[i][j]=Math.max(num[i-1][j], num[i][j-1]);
				}
			}
		}
		
		System.out.println("length of LCS="+num[pageNodes1.size()][pageNodes2.size()]);
		
		int s1position=pageNodes1.size(),s2position=pageNodes2.size();
		
		List<Node> result=new LinkedList<Node>();
		
		while(s1position!=0 && s2position !=0){
			if(pageNodes1.get(s1position-1).equals(pageNodes2.get(s2position-1))){
				result.add(pageNodes1.get(s1position-1));
				s1position--;
				s2position--;
			}
			else{
				if(num[s1position][s2position-1]>num[s1position-1][s2position]){
					s2position--;
				}
				else{
					s1position--;
				}
			}
		}
		
		Collections.reverse(result);
		return num[pageNodes1.size()][pageNodes2.size()];
		
	}
	
	public static double getPageSim(String urlStr1,String urlStr2) throws ParserException, IOException{
		ArrayList<Node> pageNodes1=new ArrayList<Node>();
		URL url1=new URL(urlStr1);
		
		Node node;
		Lexer lexer=new Lexer(url1.openConnection());
		
		lexer.setNodeFactory(new PrototypicalNodeFactory());
		while(null!=(node=lexer.nextNode())){
			//System.out.println("Url1 Not Empty!");
			pageNodes1.add(node);
		}
		ArrayList<Node> pageNodes2=new ArrayList<Node>();
		URL url2=new URL(urlStr2);
		
		lexer=new Lexer(url2.openConnection());
		lexer.setNodeFactory(new PrototypicalNodeFactory());
		while(null!=(node=lexer.nextNode())){
			//System.out.println("Url2 Not Empty!");
			pageNodes2.add(node);
		}
		
		//ArrayList<Node> pageNodes=new ArrayList<Node>();
		double distance=longestCommonSubsequence(pageNodes1,pageNodes2);
		//System.out.println("Distance:"+distance);
		return (2.0*distance)/((double)pageNodes1.size()+(double)pageNodes2.size());
		
		
		
	}
	
	

}
