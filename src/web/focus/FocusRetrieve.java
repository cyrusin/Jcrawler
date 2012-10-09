package web.focus;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.htmlparser.*;
import org.htmlparser.visitors.NodeVisitor;
//import org.htmlparser.Tag;

import java.io.*;
public class FocusRetrieve {

	/**
	 * @param args
	 */
	
	private static HttpClient client=new HttpClient();
	
	static{
		client.getHostConfiguration().setProxy("172.17.18.84", 8080);
	}
	
	public static boolean downloadPages(String path) throws HttpException,IOException,Exception{
		InputStream input=null;
		OutputStream output=null;
		GetMethod getMethod=new GetMethod(path);
		
		NameValuePair[] postData=new NameValuePair[8];
		
		postData[0]=new NameValuePair("DCity1","BJS");
		postData[1]=new NameValuePair("ACity1","SHA");
		postData[2]=new NameValuePair("DDate1","2012-9-30");
		postData[3]=new NameValuePair("ClassType","");
		postData[4]=new NameValuePair("PassengerQuantity","1");
		postData[5]=new NameValuePair("SendTicketCity","%u5317%u4EAC");
		postData[6]=new NameValuePair("Airline","");
		postData[7]=new NameValuePair("PassengerType","ADU");
		
		getMethod.setQueryString(postData);
		
		int statusCode=client.executeMethod(getMethod);
		
		if(statusCode==HttpStatus.SC_OK){
			System.out.println(getMethod.getStatusLine());
			input=getMethod.getResponseBodyAsStream();
			String charset=getMethod.getRequestCharSet();
			
			String fileName=path.substring(path.lastIndexOf("/")+1);
			
			fileName+=System.currentTimeMillis();
			File tempFile=new File(fileName);
			
			if(!tempFile.exists()){
				tempFile.createNewFile();
			}
			
			output=new FileOutputStream(tempFile);
			
			int tempByte=-1;
			while((tempByte=input.read())>0){
				output.write(tempByte);
			}
			
			if(input!=null){
				input.close();
			}
			if(output!=null){
				output.close();
			}
			
			Parser parser=new Parser(fileName);
			
			parser.setEncoding(charset);
			NodeVisitor nodeVisitor=new NodeVisitor(){
				private boolean flag = false;
				private int index=-1;
				public void visitTag(Tag tag){
					if(tag.getTagName().equals("TBODY")){
						System.out.println("begin............");
						flag=true;
						index=0;
					}
					if(tag.getTagName().equals("TD")&&flag){
						switch(index){
						case 0:
							System.out.println("from_to:"+tag.toPlainTextString().trim());
							break;
						case 1:
							System.out.println("carrier:"+tag.toPlainTextString().trim());
							break;
						case 2:
							System.out.println("type:"+tag.toPlainTextString().trim());
							break;
						case 3:
							System.out.println("number:"+tag.toPlainTextString().trim());
							break;
						case 4:
							System.out.println("dicount:"+tag.toPlainTextString().trim());
							break;
						case 5:
							System.out.print("price:"+tag.toPlainTextString().trim());
							break;
						}
						index++;
					}
					
				}
				
				public void visitEndTag(Tag tag){
					if(tag.getTagName().equals("TBODY")){
						System.out.println("end............");
						flag=false;
					}
				}
			};
			
			parser.visitAllNodesWith(nodeVisitor);
			
			return true;
		}
		return false;
		
	}
	
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
			FocusRetrieve.downloadPages("http://flights.ctrip.com/Domestic/ShowFareFirst.aspx");
		}
		catch (HttpException ex){
			ex.printStackTrace();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		catch (Exception ex){
			ex.printStackTrace();
		}

	}*/

}
