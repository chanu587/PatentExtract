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

public class PatentExtractDesc {

	
	static File file;
	static BufferedWriter output;
	static String inpurDir = "./patents2001/xmls/";
	static String outputDir = "./patents2001/descdata/";
	static boolean claimFlag = false;
	static boolean desFalag =false;
	static boolean assigneeFlag = false;
	static String separator = "!@#$%^&*(";
	
	public static void main(String[] args) throws IOException {
		System.out.println("**** Extracting Patent Data ***** ");
		String line;
		FileInputStream nameinputStream = new FileInputStream("./patents2001/test");
		Scanner namesc = new Scanner(nameinputStream,"UTF-8");
		try{
			
		 
	    while (namesc.hasNextLine()) {
	        line = namesc.nextLine();
	        if(line.length()!=0){
	        	String fileName = line.substring(line.indexOf("pg"));
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
            
            
		    while (sc.hasNextLine()) {
		    	
		        line = sc.nextLine();
		        
		        if(line.length() !=0){
		        	
		        		if(line.contains("<PATDOC")){
			        		patent = new PatentDoc();
							resultClaim = new StringBuilder();
							resultDesc = new StringBuilder();
			        	}
			        	
		        		// Patent Id 
			        	else if(line.contains("<B110>")){
//				        		patent.setId(line.substring(line.indexOf("<PDAT>")+6, line.indexOf("</PDAT>")));
				        		
			        			patent.setId(StringUtils.substringBetween(line, "<PDAT>","</PDAT>"));
			        			
				        		
			        	}
		        		
		      /*  		
			        	// Title
			        	else if(line.contains("<B540>")){
//			        			patent.setTitle(line.substring(line.indexOf("<PDAT>")+6, line.indexOf("</PDAT>")));
			        			patent.setTitle(StringUtils.substringBetween(line, "<PDAT>","</PDAT>"));
			        			
			        	

						}
			        	
			        	// Abstract
			        	else if(line.contains("<PARA") && line.contains("P-00001") && line.contains("ID") ){
			        		
			        		//patent.setAbstc(StringUtils.substringBetween(line, "<PDAT>", "</PDAT>"));
			        		
			        		
			        		String[] abstractText = StringUtils.substringsBetween(line, "<PDAT>", "</PDAT>");
			        		String abc=null;
							if(abstractText!= null){
								for(int i=0;i<abstractText.length;i++){
									abc+=abstractText[i];
								}
								patent.setAbstc(abc);
							}
			        		
						}
		        		
		        		
		        		
		        		// Claims
			        	
			        	else if(line.contains("<CL>")){
							claimFlag = true;
						}else if(line.contains("</CL>")){
							claimFlag = false;
							resultClaim=null;
						}else if(claimFlag && line.contains("<PDAT>")){

								String[] claimText = StringUtils.substringsBetween(line, "<PDAT>", "</PDAT>");
								if(claimText!= null){
									for(int i=0;i<claimText.length;i++){
										resultClaim.append(claimText[i]);
									}
									patent.setClaims(resultClaim.toString());
								}
						}
		       */ 		
		        		// Description
		        		
						else if(line.contains("<SDODE>")){
							desFalag = true;
						}else if(line.contains("</SDODE>")){
							desFalag = false;
							resultDesc = null;
						}else if(desFalag){
							
							String[] claimText = StringUtils.substringsBetween(line, "<PDAT>", "</PDAT>");
							
							if(claimText!= null){
								for(int i=0;i<claimText.length;i++){
									resultDesc.append(claimText[i]);
								}
								patent.setDesc(resultDesc.toString());
							}
							
						}
		        		
		    /*    		
		        	
		        		// company information
			        	
			        	else if(line.contains("<B731>")){
			        		assigneeFlag = true;
			        	}else if(line.contains("</B731>")){
			        		assigneeFlag = false;
			        	}else if(assigneeFlag && line.contains("<NAM>")){
	        				patent.setAssign(StringUtils.substringBetween(line, "<PDAT>", "</PDAT"));
//			        		output.write(StringUtils.substringBetween(line, "<PDAT>", "</PDAT")+separator);
	        				
			        	}
			        	else if(assigneeFlag && line.contains("<ADR>")){
			        			String[] citystate = StringUtils.substringsBetween(line, "<PDAT>", "</PDAT>");
			        			if(citystate.length == 2){
			        				patent.setCity(citystate[0]);
			        				patent.setState(citystate[1]);
//			        				output.write(citystate[0]+separator+citystate[1]+separator);
			        			}
			        				
			        			else if(citystate.length == 1){
			        				patent.setState(citystate[0]);
			        				//output.write("null"+separator+citystate[0]+separator);
			        			}
			        				
			        			
			        	}
		        		*/
		        		
			        	else if(line.contains("</PATDOC>")){
			        		patentsList.add(patent);
			        		claimFlag = false;	
							desFalag = false;
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
				
			//	output.write(sample.getId()+separator+sample.getTitle()+separator+sample.getAssign()+separator+sample.getCity()+separator+sample.getState()+separator+sample.getAbstc()+separator+sample.getClaims()+separator+"");
				
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
