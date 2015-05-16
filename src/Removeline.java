import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Removeline {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// File file=new File("C:/Users/Suraj/Desktop/ipgb20120110.xml");
		Removeline
				.removeLineFromFile("C:/Users/Suraj/Downloads/Downloads/ipg130129/ipg130129.xml");
	}

	public static void removeLineFromFile(String file) {

		try {

			File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			BufferedReader br = new BufferedReader(new FileReader(file));
			PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;
			int i = 0;//
			Date start = new Date();
			while ((line = br.readLine()) != null) {
				if (!line.trim().equals(
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
						&& !line.trim()
								.equals("<!DOCTYPE us-patent-grant SYSTEM \"us-patent-grant-v42-2006-08-23.dtd\" [ ]>")
						&& !line.equals("<!DOCTYPE us-patent-grant SYSTEM \"us-patent-grant-v40-2004-12-02.dtd\" [ ]>")
						&& !line.equals("<!DOCTYPE us-patent-grant SYSTEM \"us-patent-grant-v41-2005-08-25.dtd\" [ ]>")
						&& !line.equals("<!DOCTYPE us-patent-grant SYSTEM \"us-patent-grant-v43-2012-12-04.dtd\" [ ]>")
						&& !line.equals("<!DOCTYPE us-patent-grant SYSTEM \"us-patent-grant-v44-2013-05-16.dtd\" [ ]>")
						&& !line.equals("<!DOCTYPE sequence-cwu SYSTEM \"us-sequence-listing-2004-03-09.dtd\" [ ]>")
						&& !line.equals("<!DOCTYPE sequence-cwu SYSTEM \"us-sequence-listing.dtd\" [ ]>")) {
					if (i == 0) {
						pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
						pw.println("<brrr>");
						pw.println(line);
						pw.flush();
					} else {
						pw.println(line);
						pw.flush();
					}
					i++;
				}
			}
			// System.out.println(i);
			pw.println("</brrr>");

			pw.flush();
			pw.close();
			br.close();
			Date end = new Date();
			System.out.println(end.getTime() - start.getTime()
					+ " total milliseconds ");
			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
