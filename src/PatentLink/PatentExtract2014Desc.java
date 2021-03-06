
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

public class PatentExtract2014Desc {

	
	static File file;
	static BufferedWriter output;
	static String inpurDir = "./patents2013/xmls/";
	static String outputDir = "./patents2013/descdata/";
	static boolean claimFlag = false;
	static boolean desFalag =false;
	static boolean assigneeFlag = false;
	static boolean abstractFlag = false;
	static String abc;
	
	static boolean publicationFlag = false;
	
	static String separator = "!@#$%^&*(";
	
	public static void main(String[] args) throws IOException {
		System.out.println("**** Extracting Patent Data ***** ");
		String line;
		FileInputStream nameinputStream = new FileInputStream("./patents2013/test");
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
		String outputPathFile = outputDir+path.replace("XML", "txt").replace("xml", "txt");
		System.out.println(" input file :"+inputPathFile+" Output file : "+outputPathFile);
		PatentDoc patent=null;
		ArrayList<PatentDoc> patentsList;
		String abc = null;
		try {
		    inputStream = new FileInputStream(inputPathFile);
		    sc = new Scanner(inputStream, "UTF-8");
		    file = new File(outputPathFile);
            output = new BufferedWriter(new FileWriter(file));
            
            patentsList = new ArrayList<PatentDoc>();
            StringBuilder resultClaim = null,resultDesc = null,resultAssignee=null;
            
            
		    while (sc.hasNextLine()) {
		    	
		        line = sc.nextLine();
		        
		        if(line.length() !=0){
		        	
		        		if(line.contains("<us-patent-grant")){
			        		patent = new PatentDoc();
							resultClaim = new StringBuilder();
							resultDesc = new StringBuilder();
							resultAssignee = new StringBuilder();
			        	}
			        	
		        		
		        		// Patent Id 
		        		else if(line.contains("<publication-reference>")){
		        			publicationFlag = true;
		        		}else if(line.contains("</publication-reference>")){
		        			publicationFlag = false;
		        		}
			        	else if(publicationFlag && line.contains("<doc-number>")){
			        			//System.out.println("id :"+StringUtils.substringBetween(line, "<doc-number>","</doc-number>"));
				        		patent.setId(StringUtils.substringBetween(line, "<doc-number>","</doc-number>"));
				        		
			        	}
		        	
		        		/*
		        		
		        		
			        	// Title
			        	else if(line.contains("<invention-title")){
			        			
			        			String title = StringUtils.substringBetween(line, "<invention-title","</invention-title>");
			        			title = title.substring(title.indexOf(">")+1);
//			        			System.out.println(title);
			        			patent.setTitle(title);
						}

	        		
		        		
			        	// Abstract
			        	else if(line.contains("<abstract")){
			        		abstractFlag = true;
			        	}else if(line.contains("</abstract>")){
			        		abstractFlag = false;
			        	}
			        	else if(abstractFlag){
			        		String abs = StringUtils.substringBetween(line, "<p", "</p>");
			        		//abs = abs.substring(abs.indexOf('>'));
			        		if(abs!=null){
				        		abs = abs.substring(abs.indexOf(">")+1);
				//        		System.out.println(abs);
			        		}
			        		patent.setAbstc(abs);
						}
		        		
	        		
		        		
		        		// Claims
			        	
			        	else if(line.contains("<claims")){
							claimFlag = true;
							//System.out.println("start of claims");
						}else if(line.contains("</claims>")){
							claimFlag = false;
							//System.out.println("end of claims :"+resultClaim.toString());
							patent.setClaims(resultClaim.toString());
							resultClaim=null;
						}else if(claimFlag && line.contains("claim-text") ){
							
							if(line!= null){
								
								line = line.replace("<claim-text>", "").replace("</claim-text>", "").replace("</claim-ref>", "");
								int start = line.indexOf("<claim-ref");
								int end = line.indexOf(">");
								if(start != -1 && end != -1)
									line = line.substring(0, start)+line.substring(end+1, line.length());
								//System.out.println(line);
								resultClaim.append(line);
								}
						}
						
						*/
	
		       		// Description
		        		
						else if(line.contains("<description id=")){
							desFalag = true;
							//System.out.println("*** Start of desc ****");
						}else if(line.contains("</description>")){
							desFalag = false;
							patent.setDesc(resultDesc.toString());
							//System.out.println(resultDesc.toString());
							resultDesc = null;
							
						}else if(desFalag && (line.contains("<p")|| line.contains("<heading"))){
							
							line = line.replace("<b>", "").replace("</b>","").replace("</figref>","").replace("</p>", "").replace("</heading>","")
									.replace("<figref idref=\"DRAWINGS\">", "");
							
							if(line.contains("<p")){
								line = line.substring(line.indexOf(">")+1); 
							}else if (line.contains("<heading")){
								line = line.substring(line.indexOf(">")+1);
							}
							
						//	System.out.println(line);	
							
							
							resultDesc.append(line);
						}
		        		
		       		/*
		        	
		        		// company information
			        	
			        	else if(line.contains("<assignees>")){
			        		assigneeFlag = true;
			        	}else if(line.contains("</assignees>")){
			        		assigneeFlag = false;
			        		String assignee = resultAssignee.toString();
			        		
			        		patent.setAssign(StringUtils.substringBetween(assignee, "<orgname>", "</orgname>"));
			        		patent.setCity(StringUtils.substringBetween(assignee, "<city>", "</city>"));
			        		patent.setState(StringUtils.substringBetween(assignee, "<state>", "</state>"));
			        		
			        		resultAssignee = null;
			        	}else if(assigneeFlag){
			        		if(line != null){
				        		resultAssignee.append(line);
			        		}
			        	}
		        		
		       		*/
			        	else if(line.contains("</us-patent-grant>")){
			        		patentsList.add(patent);
			        		claimFlag = false;	
							desFalag = false;
							assigneeFlag = false;
							publicationFlag = false;
							abstractFlag = false;
							
						}
						
						
		        }
		        System.out.println(count);	
		        count++;
		    }
		    
		    
			System.out.println(" Counting total no of lines : "+count);
			System.out.println(" Patent list :"+patentsList.size());
			
			System.out.println(" ******** Writing the contents to the file ******* ");
			
			for(int i=0;i<patentsList.size();i++){
				PatentDoc sample = patentsList.get(i);
				
				//output.write(sample.getId()+separator+sample.getTitle()+separator+sample.getAssign()+separator+sample.getCity()+separator+sample.getState()+separator+sample.getAbstc()+separator+sample.getClaims()+separator+"");
				
				//output.write(sample.getId()+separator);
				
				
				// write description 
				output.write(sample.getId()+separator+sample.getDesc());
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