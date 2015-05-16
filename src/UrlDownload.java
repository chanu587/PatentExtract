import java.io.*;
import java.net.*;

public class UrlDownload {
	final static int size = 1024;
	final static String outputDirect ="/home/chan/NLPData";
	

	public static void fileUrl(String fAddress, String localFileName,
			String destinationDir) {
		OutputStream outStream = null;
		URLConnection uCon = null;

		InputStream is = null;
		try {
			URL Url;
			byte[] buf;
			int ByteRead, ByteWritten = 0;
			Url = new URL(fAddress);
			outStream = new BufferedOutputStream(new FileOutputStream(
					destinationDir + "/" + localFileName));

			uCon = Url.openConnection();
			is = uCon.getInputStream();
			buf = new byte[size];
			while ((ByteRead = is.read(buf)) != -1) {
				outStream.write(buf, 0, ByteRead);
				ByteWritten += ByteRead;
				System.out.println(ByteWritten);
			}
			System.out.println("Downloaded Successfully.");
			System.out.println("File name:\"" + localFileName
					+ "\"\nNo ofbytes :" + ByteWritten);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void fileDownload(String fAddress, String destinationDir) {
		int slashIndex = fAddress.lastIndexOf('/');
		int periodIndex = fAddress.lastIndexOf('.');

		String fileName = fAddress.substring(slashIndex + 1);

		if (periodIndex >= 1 && slashIndex >= 0
				&& slashIndex < fAddress.length() - 1) {
			fileUrl(fAddress, fileName, destinationDir);
		} else {
			System.err.println("path or file name.");
		}
	}

	public static void main(String[] args) {
		
		System.out.println("**** Starting Download File *******");
		UrlDownload download = new UrlDownload();
		String path = "http://storage.googleapis.com/patents/grant_full_text/2003/pg030114.zip";
		
		download
		.fileDownload(path,outputDirect);
		
		System.out.println("***** Completed Download *********");

	}
}