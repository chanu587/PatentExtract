
package PatentLink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip {

	private static final String FILESEPARATOR = File.separator;

	public static void storeZipStream(InputStream inputStream, String dir)
			throws IOException {

		ZipInputStream zis = new ZipInputStream(inputStream);
		ZipEntry entry = null;
		int countEntry = 0;
		if (!dir.endsWith(FILESEPARATOR))
			dir += FILESEPARATOR;

		// check inputStream is ZIP or not
		if ((entry = zis.getNextEntry()) != null) {
			do {
				String entryName = entry.getName();
				// Directory Entry should end with FileSeparator
				if (!entry.isDirectory()) {
					// Directory will be created while creating file with in it.
					String fileName = dir + entryName;
					createFile(zis, fileName);
					countEntry++;
				}
			} while ((entry = zis.getNextEntry()) != null);
			System.out.println("No of files Extracted : " + countEntry);

		} else {
			throw new IOException("Given file is not a Compressed one");
		}
	}

	public static void createFile(InputStream is, String absoluteFileName)
			throws IOException {
		File f = new File(absoluteFileName);

		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		OutputStream out = new FileOutputStream(absoluteFileName);
		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		// Close the streams
		//out.close();
	}

	public static void main(String args[]) throws Exception {

		FileInputStream inputStream = null;
		Scanner sc = null;
		String line = null;
		long count = 0;

		try {
			inputStream = new FileInputStream("./patents2014/dummy.txt");
			sc = new Scanner(inputStream, "UTF-8");

			String outputdir = "./patents2014/xml/";
			String inputfile = "./patents2014/zips/";
			String path = "";
			FileInputStream zis;
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				if (line.length() != 0) {
					System.out.println(" **** Starting Zipping of "
							+ line.substring(59) + " file");
					path = inputfile + line.substring(59).trim();
					System.out.println("path :"+path);
					
					zis = new FileInputStream(new File(path));
					storeZipStream(zis, outputdir);
					System.out.println("**** Zipping finished ****");
				}
				count++;
			}
			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} finally {
			System.out.println(" Counting total no of lines : " + count);
			if (inputStream != null) {
				inputStream.close();
			}
			if (sc != null) {
				sc.close();
			}
		}

		/*
		 * 
		 * System.out.println("Extracted to "+outputdir); storeZipStream(zis,
		 * outputdir); System.out.println("***  Unzipping completed ***");
		 */

	}
}