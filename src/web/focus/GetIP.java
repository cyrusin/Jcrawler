package web.focus;

import java.io.*;
//import java.net.InetAddress;
//import java.net.UnknownHostException;
import java.net.*;

public class GetIP {
	public static void fetchIP() throws IOException{
		String hostName;
		BufferedReader input=new BufferedReader(new InputStreamReader(System.in));
		System.out.print("\n");
		System.out.print("Host name: ");
		hostName=input.readLine();
		try{
			InetAddress ipaddress=InetAddress.getByName(hostName);
			System.out.println("IP address: "+ipaddress.getHostAddress());
			
		}
		catch (UnknownHostException ex){
			System.out.println("Could not fine IP address for: "+hostName);
		}
		
	}

}
