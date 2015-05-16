package PatentLink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ReadFile {
	
	public static File file;
	public String outputDir;
	static UrlDownload url;
	
	
		public void readPatentFile() throws IOException{

			FileInputStream inputStream = null;
			Scanner sc = null;
			String line=null;
			long count =0;
			
			url = new UrlDownload();
			System.out.println("Reading URL Download :"+UrlDownload.outputDirect);
			outputDir = url.outputDirect;
			System.out.println(" output director :"+outputDir);
			
			try {
			    inputStream = new FileInputStream("/home/chan/NLPData/abc");
			    sc = new Scanner(inputStream, "UTF-8");
		        
			    while (sc.hasNextLine()) {
			        line = sc.nextLine();
			        if(line.length()!=0){	
			        	System.out.println(line);
			        	url.fileDownload(line.trim(),outputDir);
			        	
			        }
			        count++;
			    }
			    // note that Scanner suppresses exceptions
			    if (sc.ioException() != null) {
			        throw sc.ioException();
			    }
			} finally {
				System.out.println(" Counting total no of lines : "+count);
			    if (inputStream != null) {
			        inputStream.close();
			    }
			    if (sc != null) {
			        sc.close();
			    }
			}
			
	
	
}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Reading 2003 Patents links ");
		
		
		try {
			new ReadFile().readPatentFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("End of Reading File");
		
	}

}
