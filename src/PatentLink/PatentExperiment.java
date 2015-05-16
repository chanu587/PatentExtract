package PatentLink;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class PatentExperiment {

	
	static File file;
	static BufferedWriter output;
	static String inpurDir = "./patents2000/txt/";
	static String outputDir = "./patents2000/parsed/";
	
	static boolean claimFlag = false;
	static boolean desFalag =false;
	static boolean assigneeFlag = false;
	static String separator = "!@#$%^&*(";
	
	public static void main(String[] args) throws IOException {
		System.out.println("**** Extracting Patent Data ***** ");
		String line;
		
		FileInputStream nameinputStream = new FileInputStream("./patents2000/test1");
		Scanner namesc = new Scanner(nameinputStream,"UTF-8");
		try{
			
		 
	    while (namesc.hasNextLine()) {
	        line = namesc.nextLine();
	        if(line.length()!=0){
	        	String fileName = line;
	        	System.out.println(fileName);
	        	readFile(fileName);
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

	}

	public  static void readFile(String path) throws IOException {
		
		
		
		FileInputStream inputStream = null;
		Scanner sc = null;
		String line=null;
		long count =0;
		
		String inputPathFile = inpurDir+path;
		String outputPathFile = outputDir+path.replace("XML", "txt").replace("xml", "txt").replace("SGML", "txt").replace("sgm", "txt");
		System.out.println(" input file :"+inputPathFile+" Output file : "+outputPathFile);
		PatentDoc patent=null;
		ArrayList<PatentDoc> patentsList;
		
		try {
		    inputStream = new FileInputStream(inputPathFile);
		    sc = new Scanner(inputStream, "UTF-8");
		    file = new File(outputPathFile);
            output = new BufferedWriter(new FileWriter(file));
            
            patentsList = new ArrayList<PatentDoc>();
            StringBuilder resultClaim = null,resultDesc = null;
            boolean firstPatent = true;
            
		    while (sc.hasNextLine()) {
		    	
		        line = sc.nextLine();
		        
		        if(line.length() !=0){
		        	
		        		if(line.contains("PATN")){
		        			
		        			if(firstPatent == false){
		        				patentsList.add(patent);
		        				assigneeFlag = false;
		        			}
		        			
			        		patent = new PatentDoc();
			        		firstPatent = false;
			        	}
		        		else if(line.contains("WKU")){
		        			//System.out.println(line);
		        			String[] id = line.split("  ");
		        			patent.setId(id[1]);
		        			
		        			id[1] = id[1].substring(0,id[1].length()-1);
		        			
		        			if(id[1].substring(0,1).equalsIgnoreCase("0")){
		        				id[1] = id[1].substring(1);
		        			}
		        			
		        			if(id[1].substring(0,2).equalsIgnoreCase("D0"))
		        				id[1] = "D"+id[1].substring(2);
		        			
		        			else if(id[1].substring(0,2).equalsIgnoreCase("H0"))
		        				id[1] = "H"+id[1].substring(2);
		        						        			
		        			else if(id[1].substring(0,2).equalsIgnoreCase("RE"))
		        				id[1] = "RE"+id[1].substring(3);
		        			
		        			else if(id[1].substring(0,2).equalsIgnoreCase("PP"))
		        				id[1] = "PP"+id[1].substring(3);
		        			
		        			
		        			patent.setSearchid(id[1]);
		        			
		        		}
		        		else if(line.contains("TTL")){
		        			
		        			String title[] = line.split("  ");
		        			//System.out.println(title[1]);
		        			patent.setTitle(title[1].trim());
		        		}
		        		
		        		else if (line.contains("ASSG")){
		        			assigneeFlag = true;
		        		}
		        		else if(assigneeFlag && line.contains("NAM")){
		        			String name[] = line.split("  ");
		        			patent.setAssign(name[1].trim());
		        		}
		        		else if(assigneeFlag && line.contains("CTY")){
		        			String city[] = line.split("  ");
		        			patent.setCity(city[1].trim());
		        		}
		        		else if(assigneeFlag && line.contains("CNT")){
		        			String cnty[] = line.split("  ");
		        			patent.setState(cnty[1].trim());
		        			assigneeFlag = false;
		        		}
		        		
		        		
		        		
		        		
	
		        }
		        	
     		   count++;
		    }
		    
		    
			System.out.println(" Counting total no of lines : "+count);
			System.out.println(" Patent list :"+patentsList.size());
			
			System.out.println(" ******** Writing the contents to the file ******* ");
		
			
			for(int i=0;i<patentsList.size();i++){
				PatentDoc sample = patentsList.get(i);
				//System.out.println(sample.getId()+separator+sample.getSearchid());
				output.write(sample.getId()+separator+sample.getSearchid()+separator+sample.getTitle()+separator+sample.getAssign()+separator+sample.getCity()+separator+sample.getState());
				output.newLine();
			}
			
			
			
			
			
			
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} catch(Exception e){
			System.out.println("Exception :"+e);
		}
	
		output.close();
	}
	
}
