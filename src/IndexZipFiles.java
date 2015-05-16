import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LiveIndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexZipFiles {
	public static void main(String[] args) {
		String usage = "java org.apache.lucene.demo.IndexFiles [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\nThis indexes the documents in DOCS_PATH, creating a Lucene indexin INDEX_PATH that can be searched with SearchFiles";
		String indexPath = "index1";
		String docsPath = null;
		boolean create = true;
		
		for (int i = 0; i < args.length; i++) {
			if ("-index".equals(args[i])) {
				indexPath = args[(i + 1)];
				i++;
			} else if ("-docs".equals(args[i])) {
				docsPath = args[(i + 1)];
				i++;
			} else if ("-update".equals(args[i])) {
				create = false;
			}
		}

		if (docsPath == null) {
			System.err.println("Usage: " + usage);
			System.exit(1);
		}

		File docDir = new File(docsPath);
		if ((!docDir.exists()) || (!docDir.canRead())) {
			System.out
					.println("Document directory '"
							+ docDir.getAbsolutePath()
							+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		Date start = new Date();
		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");

			Directory dir = FSDirectory.open(new File(indexPath));
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40,
					analyzer);

			if (create) {
				iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			} else {
				iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			}
			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);
			writer.close();
			Date end = new Date();
			System.out.println(end.getTime() - start.getTime()
					+ " total milliseconds");
		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}
	}

	static void indexDocs(IndexWriter writer, File file) throws IOException {

		if (file.canRead())
			if (file.isDirectory()) {

				String[] files = file.list();

				if (files != null)
					for (int i = 0; i < files.length; i++)
						indexDocs(writer, new File(file, files[i]));
			} else {
				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException fnfe) {
					return;
				}

				try {
					Document doc = new Document();
					File unzippedFile = UnzipFile(file.getAbsolutePath());
					fis = new FileInputStream(unzippedFile);
					Field pathField = new StringField("path", unzippedFile
							.getAbsolutePath(), Field.Store.YES);
					doc.add(pathField);
					doc.add(new LongField("modified", file.lastModified(),
							Field.Store.NO));
					doc.add(new TextField("contents", new BufferedReader(
							new InputStreamReader(fis, "UTF-8"))));

					if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
						System.out.println("adding " + unzippedFile);
						writer.addDocument(doc);
						//unzippedFile.delete();
					} else {
						System.out.println("updating " + file);
						writer.updateDocument(new Term("path", file.getPath()),
								doc);
					}
				} finally {
					fis.close();
				}
			}
	}

	private static File UnzipFile(String file) throws IOException {
		Enumeration enumEntries;
		ZipFile zip;
		zip = new ZipFile(file);
		enumEntries = zip.entries();
		while (enumEntries.hasMoreElements()) {
			ZipEntry zipentry = (ZipEntry) enumEntries.nextElement();
			if (zipentry.isDirectory()) {
				System.out.println("Name of Extract directory : "
						+ zipentry.getName());
				(new File(zipentry.getName())).mkdir();
				continue;
			}
			System.out.println("Name of Extract fille : " + zipentry.getName());
			extractFile(zip.getInputStream(zipentry), new FileOutputStream(
					zipentry.getName()));
			if (zipentry.getName().contains("xml")) {
				zip.close();
				return new File(zipentry.getName());
			}
		}
		zip.close();

		return null;
	}

	private static void extractFile(InputStream inStream,
			FileOutputStream outStream) throws IOException {
		byte[] buf = new byte[1024];
		int l;
		while ((l = inStream.read(buf)) >= 0) {
			outStream.write(buf, 0, l);
		}
		inStream.close();
		outStream.close();

	}
}