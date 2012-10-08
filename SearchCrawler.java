package web.multi;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.*;

public class SearchCrawler implements Runnable{

	/**
	 * @param args
	 */
	private HashMap<String,ArrayList<String>> disallowListCache=new HashMap<String,ArrayList<String>>();
	ArrayList<String> errorList=new ArrayList<String>();
	ArrayList<String> result=new ArrayList<String>();
	String startUrl;
	int maxUrl;
	String searchString;
	boolean caseSensitive=false;
	boolean limitHost=false;
	
	public SearchCrawler(String startUrl,int maxUrl,String searchString){
		this.startUrl=startUrl;
		this.maxUrl=maxUrl;
		this.searchString=searchString;
	}
	
	public ArrayList<String> getResult(){
		return result;
	}
	
	public void run(){
		System.out.println("Thread Run!");
		crawl(startUrl,maxUrl,searchString,limitHost,caseSensitive);
	}
	private ArrayList<String> crawl(String startUrl, int maxUrl, String searchString,
			boolean limitHost, boolean caseSensitive) {
		// TODO Auto-generated method stub
		HashSet<String> crawledList=new HashSet<String>();
		LinkedHashSet<String> toCrawlList=new LinkedHashSet<String>();
		
		if(maxUrl<1){
			errorList.add("Invalid Max URLs value.");
			System.out.println("Invalid Max URLs value.");
		}
		if(searchString.length()<1){
			errorList.add("Missing Search String.");
			System.out.println("Missing Search string.");
		}
		if(errorList.size()>0){
			System.out.println("Err!!!");
			return errorList;
		}
		
		startUrl=removeWwwFromUrl(startUrl);
		while(toCrawlList.size()>0){
			if(maxUrl!=-1){
				if(crawledList.size()==maxUrl){
					break;
				}
			}
		
		
		String url=toCrawlList.iterator().next();
		toCrawlList.remove(url);
		URL verifiedUrl=verifyUrl(url);
		if(!isRobotAllowed(verifiedUrl)){
			continue;
		}
		crawledList.add(url);
		String pageContents=downloadPage(verifiedUrl);
		if(pageContents!=null&&pageContents.length()>0){
			ArrayList<String> links=retrieveLinks(verifiedUrl,pageContents,crawledList,limitHost);
			toCrawlList.addAll(links);
			if(searchStringMatches(pageContents,searchString,caseSensitive)){
				result.add(url);
				System.out.println(url);
			}
		}
		}
		return result;
	}

	private boolean searchStringMatches(String pageContents,
			String searchString, boolean caseSensitive) {
		// TODO Auto-generated method stub
		String searchContents=pageContents;
	//	String seatchContents=pageContents;
		if(!caseSensitive){
			searchContents=pageContents.toLowerCase();
		}
		
		Pattern p=Pattern.compile("[\\s]+");
		String[] terms=p.split(searchString);
		for(int i=0;i<terms.length;i++){
			if(caseSensitive){
				if(searchContents.indexOf(terms[i])==-1){
					return false;
				}
			}
			else{
				if(searchContents.indexOf(terms[i].toLowerCase())==-1){
					return false;
				}
			}
		}
		return true;
	}

	private ArrayList<String> retrieveLinks(URL pageUrl,
			String pageContents, HashSet<String> crawledList, boolean limitHost) {
		// TODO Auto-generated method stub
		Pattern p=Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]", Pattern.CASE_INSENSITIVE);
		Matcher m=p.matcher(pageContents);
		ArrayList<String> linkList=new ArrayList<String>();
		while(m.find()){
			String link=m.group(1).trim();
			if(link.length()<1){
				continue;
			}
			if(link.charAt(0)=='#'){
				continue;
			}
			if(link.indexOf("mailto:")!=-1){
				continue;
			}
			if(link.toLowerCase().indexOf("javaScript")!=-1){
				continue;
			}
			if(link.indexOf("://")==-1){
				if(link.charAt(0)=='/'){
					link="http://"+pageUrl.getHost()+":"+pageUrl.getPort()+link;
				}
				else{
					String file=pageUrl.getFile();
					if(file.indexOf('/')==-1){
						link="http://"+pageUrl.getHost()+":"+pageUrl.getPort()+"/"+link;
					}
					else{
						String path=file.substring(0,file.lastIndexOf('/')+1);
						link="http://"+pageUrl.getHost()+":"+pageUrl.getPort()+path+link;
					}
				}
			}
			
			int index=link.indexOf('#');
			if(index!=-1){
				link=link.substring(0,index);
			}
			
			link=removeWwwFromUrl(link);
			URL verifiedLink=verifyUrl(link);
			
			if(verifiedLink==null){
				continue;
			}
			
			if(limitHost&&!pageUrl.getHost().toLowerCase().equals(
					verifiedLink.getHost().toLowerCase())){
				continue;
			}
			if(crawledList.contains(link)){
				continue;
			}
			
			System.out.println(link);
			linkList.add(link);
			
		}
		return linkList;
	}

	private String downloadPage(URL pageUrl) {
		// TODO Auto-generated method stub
		try{
			BufferedReader reader=new BufferedReader(new InputStreamReader(pageUrl.openStream()));
			String line;
			StringBuffer pageBuffer=new StringBuffer();
			while((line=reader.readLine())!=null){
				pageBuffer.append(line);
			}
			return pageBuffer.toString();
		}
		catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
		
	}

	private boolean isRobotAllowed(URL urlToCheck) {
		// TODO Auto-generated method stub
		String host=urlToCheck.getHost().toLowerCase();
		System.out.println("Host="+host);
		ArrayList<String> disallowList=disallowListCache.get(host);
		if(disallowList==null){
			disallowList=new ArrayList<String>();
			try{
				URL robotsFileUrl=new URL("http://"+host+"robots.txt");
				BufferedReader reader=new BufferedReader(new InputStreamReader(robotsFileUrl.openStream()));
				String line;
				while((line=reader.readLine()) != null){
					if(line.indexOf("Disallow:")==0){
						String disallowPath=line.substring("Disabllow:".length());
						int commentIndex=disallowPath.indexOf("#");
						if(commentIndex!=-1){
							disallowPath=disallowPath.substring(0,commentIndex);
						}
						disallowPath=disallowPath.trim();
						disallowList.add(disallowPath);
					}
					
				}
				disallowListCache.put(host, disallowList);
			}
			catch(Exception ex){
				ex.printStackTrace();
				return false;
				
			}
		}
		String file=urlToCheck.getFile();
		for(int i=0;i<disallowList.size();i++){
			String disallow=disallowList.get(i);
			if(file.startsWith(disallow)){
				return false;
			}
		}
		return true;
	}

	private URL verifyUrl(String url) {
		// TODO Auto-generated method stub
		
		if(!url.toLowerCase().startsWith("http://"))
		return null;
		URL verifiedUrl=null;
		try{
			verifiedUrl=new URL(url);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		return verifiedUrl;
	}

	private String removeWwwFromUrl(String url) {
		// TODO Auto-generated method stub
		int index=url.indexOf("://www.");
		if(index!=-1){
			return url.substring(0,index+3)+url.substring(index+7);
		}
		return url;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		SearchCrawler crawler=new SearchCrawler("http://www.hao123.com",20,"day");
		URL tempUrl=new URL("http://www.douban.com");
		BufferedReader reader=new BufferedReader(new InputStreamReader(tempUrl.openStream()));
		String tempLine;
		String filePath;
		DataOutputStream out=new DataOutputStream(new OutputStream(new FileOutputStream("./temp/"+filePath)));
		//StringBuffer tempStringBuffer=new StringBuffer();
		while((tempLine=reader.readLine())!=null){
			//tempStringBuffer.append(tempLine);
			System.out.println(tempLine);
			
		}
	//	System.out.println(tempStringBuffer.toString());
		Thread search=new Thread(crawler);
		System.out.println("Start searching...");
		System.out.println("result:");
		search.start();
		
	}

}
