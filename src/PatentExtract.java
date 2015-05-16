import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedList;
import java.util.Scanner;

public class PatentExtract {

	
	static File file;
	static BufferedWriter output;
	
	public static void main(String[] args) throws IOException {
		System.out.println("**** Extracting Patent Data ***** ");
		String name = "/home/chan/NLPData/pg030107.XML";
		readFile(name);
		System.out.println("****  Patent Data  Extracted ***** ");

	}

	public static String readFile(String path) throws IOException {
		
		FileInputStream inputStream = null;
		Scanner sc = null;
		String line=null;
		long count =0;
		PatentExtract pe = new PatentExtract();
		
		try {
		    inputStream = new FileInputStream(path);
		    sc = new Scanner(inputStream, "UTF-8");
		    file = new File("/home/chan/NLPData/pg03107.txt");
            output = new BufferedWriter(new FileWriter(file));
            
		    while (sc.hasNextLine()) {
		        line = sc.nextLine();

		        pe.extractPatent(line);
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
		        output.close();
		    }
		}
		return line;	
	
	}
	
	public void extractPatent(String line){
//		System.out.println(line);
		
		        try {
					
		        	if(line.contains("<B110>")){
			        	output.write(line.substring(line.indexOf("<PDAT>")+6, line.indexOf("</PDAT>"))+"   ");
			        	
					}
		        	else if(line.contains("<B540>")){
						output.write(line.substring(line.indexOf("<PDAT>")+6, line.indexOf("</PDAT>"))+"   ");
						
					}else if(line.contains("<PARA") && line.contains("P-00001") && line.contains("ID") ){
					
						output.write(line.substring(line.indexOf("<PDAT>")+6, line.indexOf("</PDAT>"))+"   ");
						
					}else if(line.contains("</PATDOC>")){
						output.newLine();
					}
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    
	}
	
	

	public void extractDoc(String line){
		boolean writeLine = false;
		
		        try {
					
		        	if(line.contains("<PATDOC")){
		        		writeLine = true;
		        	}else if(line.contains("</PATDOC>")){
		        		writeLine = false;
		        	}
		        		
		        	if(writeLine){
			        	output.write(line);
						output.newLine();
		        	}
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    
	}
	

	
	

}
