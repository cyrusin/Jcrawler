package web.md5;
import java.security.*;

public class MD5 {

	/**
	 * @param args
	 */
	
	public static String getMD5(byte[] source){
		String s=null;
		char hexDigits[]={'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
                'a', 'b', 'c', 'd', 'e', 'f' }; // 用来将字节转换成十六进制表示的字符 
		
		try{
			MessageDigest md=MessageDigest.getInstance("MD5");
			md.update(source);
			
			byte tmp[]=md.digest();
			char str[]=new char[16*2];
			int k=0;
			for(int i=0;i<16;i++){
				byte byte0=tmp[i];
				str[k++]=hexDigits[byte0 >>> 4 & 0xf];
				str[k++]=hexDigits[byte0 & 0xf];
				
			}
			
			s=new String(str);
			                    
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		
		return s;
		
	}
}
	
