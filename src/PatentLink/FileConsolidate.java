package PatentLink;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class FileConsolidate {

	
	static File file;
	static BufferedWriter output;
	static String inpurDir = "./patents2014/testnew/";
	static String outputDir = "./patents2014/testnew/year_consolidate.txt";
	static boolean assigneeFlag = false;
	
	public static void main(String[] args) throws IOException {
		System.out.println("**** Extracting Patent Data ***** ");
		String line;
		FileInputStream nameinputStream = new FileInputStream("./patents2014/links.txt");
		Scanner namesc = new Scanner(nameinputStream,"UTF-8");
		try{
			
			String outputPathFile = outputDir;
		    file = new File(outputPathFile);
            output = new BufferedWriter(new FileWriter(file));
    

	    while (namesc.hasNextLine()) {
	        line = namesc.nextLine();
	        if(line.length()!=0){
	        	
	        	System.out.println(line);
	        	
	        	readFile(line);
	        	
	        }
	    }
		}
		finally{
			
			if (nameinputStream != null) {
		        nameinputStream.close();
		    }
		    if (namesc != null) {
		        namesc.close();
		    }
			
		}
		System.out.println("****  Patent Data  Extracted ***** ");
		output.close();

	}

	public static String readFile(String path) throws IOException {
		
		FileInputStream inputStream = null;
		Scanner sc = null;
		String line=null;
		long count =0;
		FileConsolidate pe = new FileConsolidate();
		
		String inputPathFile = inpurDir+path;
		System.out.println(" input file :"+inputPathFile+" Output file : "+outputDir);
		
		try {
		    inputStream = new FileInputStream(inputPathFile);
		    sc = new Scanner(inputStream, "UTF-8");
	        
		    while (sc.hasNextLine()) {
		        line = sc.nextLine();
		        output.write(line);
		        output.newLine();
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
		return line;	
	
	}
	

}
