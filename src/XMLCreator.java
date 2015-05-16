/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Terry
 */



import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

public class XMLCreator
{

/**
* @param args the command line arguments
*/

//the input to this converter is a .txt file with each chunk of information (comment, sentence, paragraph) seperated by a newline
//the output is a .XML document with one element for each aforementioned chunk of info

	public static void main(String[] args) throws FileNotFoundException, ParserConfigurationException, TransformerConfigurationException, TransformerException
	{
	
	    int elementCount = 0;
	
	    File openFile = new File ("C:/Users/Suraj/Downloads/randomWikipedia25K/randomWikipedia25K.txt");						
	    Scanner inFile = new Scanner (new FileReader(openFile));
	
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
	
	    // root elements
	    Document doc = docBuilder.newDocument();
	    Element rootElement = doc.createElement("file");
	    doc.appendChild(rootElement);
	
	
	    while (inFile.hasNextInt())						
	    {   
	        elementCount++;
	        String text = inFile.nextLine();
	
	

	        String text2="";
	        Matcher matcher = Pattern.compile("\\d+").matcher(text);
	        if (matcher.find())
			 {
	         		String someNumberStr = matcher.group();
	        		 // if you need this to be an int:
	        		 Integer someNumberInt = Integer.parseInt(someNumberStr);
	        		 text2 = someNumberInt.toString();
	        		//System.out.println(someNumberInt);
	    	}
			
			String text3="";
	       Pattern p=Pattern.compile("(\t)(.*?)(\t)");
			Matcher m1=p.matcher(text);
			if (m1.find()){
   			//System.out.println(m1.group(2));
				text3 = m1.group(2);
			}
			
			String text4="";
	       Pattern p2=Pattern.compile("(\t)(.*?)(\t)");
			Matcher m2=p2.matcher(text);
			if (m2.find()){
   			String[] temp = m2.group(2).split(" ");
				int length = temp.length;
				//System.out.println(length);
				if (length == 2 || length ==3)
				{
					if (text.contains("born"))
					{
						text4 = "Person";
					}
					else
					{
						text4 = "Else";
					}		
				}
				else
				{
					text4 = "Else";
				}		
			}
	
	    Element document = doc.createElement("DOC");
	    rootElement.appendChild(document);
	
	    Element docNum = doc.createElement("DOCNO");
	    docNum.appendChild(doc.createTextNode(text2));
	    document.appendChild(docNum);      		
	
	    Element title = doc.createElement("TITLE");
	    title.appendChild(doc.createTextNode(text3));
	    document.appendChild(title);  	
	
	    // label elements
	    Element label = doc.createElement("CLASS");
	    label.appendChild(doc.createTextNode(text4));
	    document.appendChild(label);	
	
	    Element comment = doc.createElement("TEXT");
	    comment.appendChild(doc.createTextNode(text));
	    document.appendChild(comment);
	}
	
	System.out.println(elementCount);
	
	// write the content into xml file
	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	
	
	//    		transformer.transform(xmlInput, xmlOutput);
	
	
	DOMSource source = new DOMSource(doc);
	StreamResult result = new StreamResult(new File("WIKI.xml"));
	
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	
	
	transformer.transform(source, result);
	System.out.println("File saved!");
	
	}
}

