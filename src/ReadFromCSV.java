import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFromCSV {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/*CSVReader reader = new CSVReader(new FileReader("C:/Users/Suraj/Downloads/randomWikipedia25K/randomWikipedia25K.csv"));
		File file = new File("C:/Users/Suraj/Desktop/XML.txt");
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		

		

	    String [] nextLine;
	    while ((nextLine = reader.readNext()) != null) {
	        // nextLine[] is an array of values from the line
	    	if(nextLine.length>2)
	        System.out.println(nextLine[0] + nextLine[1]);
	    	else
	    		System.out.println(nextLine[0]);
	    	//System.out.println(nextLine[0]);
	    	bw.write(nextLine[0]+"\n");
	    }
	    bw.close();
	    System.out.println("Done");*/
		String s="500	Robin Stuart	zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz";
		Pattern p=Pattern.compile("(\t)(.*?)(\t)");
		Matcher m1=p.matcher(s);
		if (m1.find()){
		    System.out.println(m1.group(2));
		}
	}
}