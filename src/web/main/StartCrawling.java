package web.main;
import java.io.IOException;

import org.htmlparser.util.ParserException;

//import web.focus.*;
import web.content.*;

public class StartCrawling {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParserException 
	 */
	public static void main(String[] args) throws IOException, ParserException {
		// TODO Auto-generated method stub
		
		//GetIP.fetchIP();
		double sim=PageSim.getPageSim("http://www.hao123.com/", "http://www.2345.com/");
		System.out.println("THe similarity is: "+sim);
	}

}
