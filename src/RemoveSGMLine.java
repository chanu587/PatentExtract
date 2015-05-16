import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class RemoveSGMLine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// File file=new File("C:/Users/Suraj/Desktop/ipgb20120110.xml");
		RemoveSGMLine
				.RemoveLinesSGM("D:/pg010814.sgm");
	}
public static void RemoveLinesSGM(String file){

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
			if (!line.trim().contains("<!DOCTYPE PATDOC")
					&& !line.contains(".TIF\" NDATA TIF")
					&& !line.equals("]>")
					&& !line.contains("!ENTITY")
					&& !line
							.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
					&& !line.contains("<EMI ID=")
					&& !line.contains("<COLSPEC COLNAME=")
					&& !line.equals("")) {
				if (i == 0) {
					pw
							.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
					pw.println("<brrr>");
					pw.println(line);
					pw.flush();
				} else {
					line=line.replace("&", "");
					line=line.replace("<CITED-BY-EXAMINER>", "");
					line=line.replace("<CITED-BY-OTHER>", "");
					line=line.replace("<B597US>", "");
					line=line.replace("<B473US>", "");
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
				if (!line.trim().contains("<!DOCTYPE PATDOC")
						&& !line.contains(".TIF\" NDATA TIF")
						&& !line.equals("]>")
						&& !line.contains("!ENTITY")
						&& !line
								.equals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
						&& !line.equals("")) {
					if (i == 0) {
						pw
								.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
						pw.println("<brrr>");
						pw.println(line);
						pw.flush();
					} else {
						line=line.replace("&", "");
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
