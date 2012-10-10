package web.pagerank;

import java.util.*;

public class PageRankComputeUrl implements ComputeUrl{
	public boolean accept(String url,String pageContent){
		return false;
	}
	
	private double[] rank;
	Hashtable<String,Integer> hashedPages;
	String[] sortedRank;
	public PageRankComputeUrl(){
		
	}
	
	private void rankFilter(BigMatrix dataMatrix){
		String[] tempRank=new String[sortedRank.length];
		Boolean isEqual=true;
		for(int i=0;i<50;i++){
			rank=dataMatrix.mutiply(rank);
			for(int j=0;j<sortedRank.length;j++){
				tempRank[j]=sortedRank[j];
			}
			Arrays.sort(sortedRank, new compareByRank());
			for(int j=0;j<sortedRank.length;j++){
				if(sortedRank[j].compareTo(tempRank[j])!=0){
					isEqual=false;
					break;
				}
			}
			if(isEqual==true){
				break;
			}else{
				isEqual=true;
			}
		}
	}
	
	class compareByRank implements Comparator<String>{
		public int compare(String a,String b){
			int indexA=hashedPages.get(a);
			int indexB=hashedPages.get(b);
			if(rank[indexA]==rank[indexB]){
				return 0;
			}
			else if(rank[indexA]>rank[indexB]){
				return -1;
				
			}
			else{
				return 1;
			}
		}
	}
	
	public String[] pageRank(String[] s){
		int theSize=Math.max(4*s.length/3, 16);
		hashedPages=new Hashtable<String,Integer>(theSize);
		String[] pages=new String[s.length];
		int[] nLinks=new int[s.length];
		rank=new double[s.length];
		sortedRank=new String[s.length];
		String[] dataEntry=new String[s.length];
		
		for(int i=0;i<s.length;i++){
			String[] temp=s[i].split(" ");
			pages[i]=temp[0];
			nLinks[i]=temp.length-1;
			sortedRank[i]=temp[0];
			rank[i]=1;
			dataEntry[i]="";
			hashedPages.put(pages[i], i);
		}
		int tRow,tCol;
		for(int i=0;i<s.length;i++){
			String[] temp=s[i].split(" ");
			for(int j=1;j<s.length;j++){
				tCol=hashedPages.get(temp[0]);
				tRow=hashedPages.get(temp[j]);
				dataEntry[tRow]+="{"+tCol+","+(1/(double)nLinks[i])+"};";
			}
		}
		BigMatrix dataMatrix=new BigMatrix(dataEntry);
		rankFilter(dataMatrix);
		return sortedRank;
	}

}

class BigMatrix{
	public int nRows,nCols;
	EntryList[] theRows;
	public BigMatrix(String[] x){
		nRows=x.length;
		nCols=0;
		for(int i=0;i<nRows;i++){
		theRows[i]=new EntryList();
		if(x[i]!=null){
			String[] tempArr=x[i].split(";");
			if(tempArr[0]!=null){
				for(int j=0;j<tempArr.length;j++){
					Entry instance=new Entry(tempArr[j]);
					theRows[i].add(instance);
					if(nCols<=instance.col){
						nCols=instance.col+1;
					}
				}
			}
			
		}
		}
	}
	
	public double[] mutiply(double[] x){
		double[] result=new double[nRows];
		int count;
		for(int i=0;i<nRows;i++){
			EntryList temp=theRows[i];
			count=0;
			while((temp!=null)&&(temp.data!=null)){
				result[i]+=(temp.data.value*temp.data.col);
				temp=temp.next;
				count++;
			}
		}
		return result;
	}
}

class EntryList{
	Entry data;
	EntryList next,tail;
	public EntryList(){
		next=null;
		tail=null;
		data=null;
	}
	
	void add(Entry x){
		if(tail==null){
			data=x;
			tail=this;
		}else{
			tail.next=new EntryList();
			tail.next.data=x;
			tail=tail.next;
		}
	}
	
}

class Entry{
	int col;
	double value;
	public Entry(String x){
		String[] temp=x.split(",");
		if(temp[0].compareTo("")!=0){
			col=Integer.parseInt(temp[0].trim().substring(1));
			value=Double.parseDouble(temp[1].trim().substring(0,temp[1].trim().length()-1));
		}
		
	}
	
}

