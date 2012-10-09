package web.urllist;
import java.util.*;
public class UrlLists {
	
	private static Set visitedUrl=new HashSet();
	
	private static UrlQueue unVisitedUrl=new UrlQueue();
	
	public static UrlQueue getUnVisitedUrl(){
		return unVisitedUrl;
	}
	
	public static void addVisitedUrl(String url){
		visitedUrl.add(url);
	}
	
	public static void removeVisitedUrl(String url){
		
		visitedUrl.remove(url);
	}
	
	public static Object unVisitedUrlDeQueue(){
		return unVisitedUrl.deQueue();
	}
	
	public static void addUnVisitedUrl(String url){
		if( url != null && !url.trim().equals("") && !visitedUrl.contains(url)&&!unVisitedUrl.contains(url)){
			
			unVisitedUrl.enQueue(url);
		}
	}
	
	public static int getVisitedUrlNum(){
		return visitedUrl.size();
	}
	
	public static boolean unVisitedUrlIsEmpty(){
		return unVisitedUrl.isUrlQueueEmpty();
	}
}
