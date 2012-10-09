package web.content;
import java.util.*;
import web.urllist.*;
import java.io.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class DownloadFile {
	
	public String getFileNameByUrl(String url,String contentType){
		url=url.substring(7);
		if(contentType.indexOf("html")!=-1){
			url=url.replaceAll("[\\?/:*|<>\"]", "_")+".html";
			return url;
		}
		else{
			return url.replaceAll("[\\?/:*|<>\"]", "_")+"."+contentType.substring(contentType.lastIndexOf("/")+1);
		}
	}
	
	private void saveToLocal(byte[] data,String filepath){
		try{
			DataOutputStream out=new DataOutputStream(new FileOutputStream(new File(filepath)));
			for(int i=0;i<data.length;i++){
				out.write(data[i]);
			}
			
			out.flush();
			out.close();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		
	}
	
	public String downloadFile(String url){
		String filePath=null;
		HttpClient httpClient = new HttpClient();
		
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		GetMethod getMethod=new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		
		try{
			int statusCode =httpClient.executeMethod(getMethod);
			if(statusCode!=HttpStatus.SC_OK){
				System.out.println("Method failed:"+getMethod.getStatusLine());
				filePath=null;
			}
			
			byte[] responseBody=getMethod.getResponseBody();
			filePath="./musicLib/"+getFileNameByUrl(url,getMethod.getResponseHeader("Content-Type").getValue());
			saveToLocal(responseBody,filePath);		
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		finally{
			getMethod.releaseConnection();
		}
		
		return filePath;
		
	}
	
}
