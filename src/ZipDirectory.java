import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ZipDirectory {
	public static void extractFile(InputStream inStream, OutputStream outStream)
			throws IOException {
		byte[] buf = new byte[1024];
		int l;
		while ((l = inStream.read(buf)) >= 0) {
			outStream.write(buf, 0, l);
		}
		inStream.close();
		outStream.close();
	}

	public static void main(String[] args) {
		Enumeration enumEntries;
		ZipFile zip;

		if (args.length != 1) {
			System.out.println("There is no zip file :");
			return;
		}
		try {
			zip = new ZipFile(args[0]);
			enumEntries = zip.entries();
			while (enumEntries.hasMoreElements()) {
				ZipEntry zipentry = (ZipEntry) enumEntries.nextElement();
				if (zipentry.isDirectory()) {
					System.out.println("Name of Extract directory : "
							+ zipentry.getName());
					(new File(zipentry.getName())).mkdir();
					continue;
				}
				System.out.println("Name of Extract fille : "
						+ zipentry.getName());
				extractFile(zip.getInputStream(zipentry), new FileOutputStream(
						zipentry.getName()));
			}
			zip.close();
		} catch (IOException ioe) {
			System.out.println("There is an IoException Occured :" + ioe);
			ioe.printStackTrace();
			return;
		}
	}

}