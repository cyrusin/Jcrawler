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

