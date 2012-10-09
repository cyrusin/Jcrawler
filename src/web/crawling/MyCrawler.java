package web.crawling;
import java.util.*;

import web.content.*;
//import web.md5.*;
import web.urllist.*;
public class MyCrawler {

	/**
	 * @param args
	 */
	
	private void initCrawlerWithSeeds(String[] seeds){
		for(int i=0;i<seeds.length;i++){
			UrlLists.addUnVisitedUrl(seeds[i]);
		}
	}
	
	public void crawl(String[] seeds){
		LinkFilter filter=new LinkFilter(){
			public boolean accept(String url){
				if(url.startsWith("http://www.1ting.com")){
					return true;
				}
				else{
					return false;
				}
			}
		};
		
		initCrawlerWithSeeds(seeds);
		
		while(!UrlLists.unVisitedUrlIsEmpty()&&UrlLists.getVisitedUrlNum()<=100){
			String visitUrl=(String)UrlLists.unVisitedUrlDeQueue();
			if(visitUrl==null){
				continue;
			}
			
			DownloadFile downloader=new DownloadFile();
			downloader.downloadFile(visitUrl);
			UrlLists.addVisitedUrl(visitUrl);
			Set<String > links=HtmlParserTool.extractLinks(visitUrl, filter);
			for(String link:links){
				UrlLists.addUnVisitedUrl(link);
			}
		}
		
		
	}
	/**public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MyCrawler crawler=new MyCrawler();
		crawler.crawl(new String[] {"http://www.1ting.com"});
		
	}*/

}
