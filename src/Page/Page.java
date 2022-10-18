package Page;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Page {
	public String getPageHtml(String path) {
		StringBuilder contentBuilder = new StringBuilder();
		
		try {
		    BufferedReader in = new BufferedReader(new FileReader(path));
		    String str;
		    
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    
		    in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		
		return contentBuilder.toString();
	}
}
