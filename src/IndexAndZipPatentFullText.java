import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.digester3.Digester;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.xml.sax.SAXException;

public class IndexAndZipPatentFullText {
	static IndexWriter writer;
	static Map<String, String> classMap = new HashMap<String, String>();

	// private File unZippedFile;

	public static void main(String[] args) throws SAXException {

		String usage = "java org.apache.lucene.demo.IndexFiles [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\nThis indexes the documents in DOCS_PATH, creating a Lucene indexin INDEX_PATH that can be searched with SearchFiles";
		String indexPath = "indexToTestClaims";
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
		//	iwc.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
			writer = new IndexWriter(dir, iwc);
			getClassHashMap();
			indexDocs(writer, docDir);
			writer.close();
			Date end = new Date();
			System.out.println(end.getTime() - start.getTime()
					+ " Completed in");
		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}
	}

	static void indexDocs(IndexWriter writer, File file) throws IOException,
			SAXException {

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
					File unZippedFile = UnzipFile(file.getAbsolutePath());
					
					Removeline.removeLineFromFile(unZippedFile
							.getAbsolutePath());
					// fis = new FileInputStream(unzippedFile);

					Digester digester = new Digester();
					digester.setValidating(false);
					digester.addObjectCreate("brrr",
							IndexAndZipPatentFullText.class);
					digester.addObjectCreate("brrr/us-patent-grant",
							UsPatentGrant.class);
					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/parties/applicants/applicant/addressbook/last-name",
									"setLastName", 0);
					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/parties/applicants/applicant/addressbook/first-name",
									"setFirstName", 0);
					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/publication-reference/document-id/date",
									"setDatePublished", 0);
					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/publication-reference/document-id/doc-number",
									"setPatentId", 0);

					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/publication-reference/document-id/kind",
									"setKind", 0);

					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/classification-national/further-classification",
									"setClassificationList", 0);

					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/classification-national/main-classification",
									"setClassification", 0);
					digester
							.addSetProperties(
									"brrr/us-patent-grant/us-bibliographic-data-grant/application-reference",
									"appl-type", "type");

					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/invention-title",
									"setTitle", 0);

					digester.addCallMethod("brrr/us-patent-grant/abstract/p",
							"setTextOfThePatent", 0);
					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/assignees/assignee/addressbook/orgname",
									"setAssigned", 0);
					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/assignees/assignee/addressbook/address/city",
									"setCity", 0);

					digester.addCallMethod(
							"brrr/us-patent-grant/us-claim-statement",
							"setUsClaim", 0);

					digester.addCallMethod(
							"brrr/us-patent-grant/description/p",
							"setPatentText", 0);
					digester
							.addCallMethod(
									"brrr/us-patent-grant/us-bibliographic-data-grant/assignees/assignee/addressbook/address/state",
									"setState", 0);

					digester.addObjectCreate("brrr/us-patent-grant/claims",
							Claims.class);
					digester.addObjectCreate(
							"brrr/us-patent-grant/claims/claim", Claim.class);
					digester.addCallMethod(
							"brrr/us-patent-grant/claims/claim/claim-text",
							"setClaim", 0);
					digester
							.addCallMethod(
									"brrr/us-patent-grant/claims/claim/claim-text/claim-text",
									"setClaimText", 0);
					digester.addSetNext("brrr/us-patent-grant/claims/claim",
							"setClaimList");
					digester.addSetNext("brrr/us-patent-grant/claims",
							"setClaims");

					digester.addSetNext("brrr/us-patent-grant",
							"addPatentToIndex");

					IndexAndZipPatentFullText abp = (IndexAndZipPatentFullText) digester
							.parse(new File(unZippedFile.getAbsolutePath()));
					System.out.println("Indexing . . .");
					unZippedFile.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}

				finally {
					// writer.close();
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
			/*
			 * if (zipentry.isDirectory()) {
			 * System.out.println("Name of Extract directory : " +
			 * zipentry.getName()); (new File(zipentry.getName())).mkdir();
			 * continue; }
			 */
			System.out
					.println("Name of Extracted file : " + zipentry.getName());
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

	public void addPatentToIndex(UsPatentGrant grant) throws IOException {
		Document doc = new Document();
		if (null != grant.getUsClaim()) {
			doc.add(new TextField("usClaimStatement", grant.getUsClaim(),
					Field.Store.YES));
		}
		if (grant.getClaims()!=null) {
			List<String> newClaimList = new ArrayList<String>();
			for (Claim claim : grant.getClaims().getClaimList()) {
				newClaimList.addAll(claim.getClaim());
				newClaimList.addAll(claim.getClaimText());
			}
			String claimText = modifyList(newClaimList);

			doc.add(new TextField("claimText", claimText, Field.Store.YES));
		}
		if (grant.getPatentText().size() > 0) {
			doc.add(new TextField("Description", modifyList(grant
					.getPatentText()), Field.Store.YES));
		}

		if (null != grant.getClassification()) {
			String type = classMap.get(grant.getClassification()
					.substring(0, 3).trim());
			if (type != null)
				doc.add(new TextField("MainClassification", type,
						Field.Store.YES));
			doc.add(new TextField("Class", grant.getClassification().substring(
					0, 3), Field.Store.YES));
			String subClass = grant.getClassification().substring(3,
					grant.getClassification().length());
			if (subClass.length() > 3) {
				subClass = subClass.substring(0, 3) + "."
						+ subClass.substring(3, subClass.length());
				doc.add(new TextField("SubClass", subClass, Field.Store.YES));
			} else
				doc.add(new TextField("SubClass", subClass, Field.Store.YES));

		}
		if (grant.getClassificationList().size() > 0) {
			String furtherClassification = modifyClassificationList(grant
					.getClassificationList());
			if (null != furtherClassification)
				doc.add(new TextField("FurtherClassification",
						furtherClassification, Field.Store.YES));
		}
		/*
		 * if (null != grant.getClassification()) doc.add(new
		 * TextField("MainClassification", grant .getClassification(),
		 * Field.Store.YES));
		 */
		if (null != grant.getType())
			doc.add(new TextField("Type", grant.getType(), Field.Store.YES));

		if (null != grant.getTextOfThePatent())
			doc.add(new TextField("AbstractText", grant.getTextOfThePatent(),
					Field.Store.YES));
		if (null != grant.getTitle())
			doc.add(new TextField("Title", grant.getTitle().trim(),
					Field.Store.YES));
		doc
				.add(new TextField("PatentId", grant.getPatentId(),
						Field.Store.YES));
		if (null != grant.getDatePublished())
			doc.add(new TextField("DatePublished", grant.getDatePublished(),
					Field.Store.YES));
		if (null != grant.getAssigned())
			doc.add(new TextField("Assignee", grant.getAssigned().trim(),
					Field.Store.YES));
		if (null != grant.getCity())
			doc.add(new TextField("City", grant.getCity().trim(),
					Field.Store.YES));
		if (null != grant.getState())
			doc.add(new TextField("State", grant.getState(), Field.Store.YES));
		if (null != grant.getKind())
			doc.add(new TextField("kind", grant.getKind(), Field.Store.YES));
		if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
			// System.out.println("adding file");
			writer.addDocument(doc);
			// unZippedFile.delete();
		} else {
			writer.updateDocument(new Term("path", ""), doc);
		}
	}

	String modifyClassificationList(List<String> list) {
		List<String> newList = new ArrayList<String>();
		for (String String : list) {
			if (String.length() > 6) {
				String = String.substring(0, 3) + "/" + String.substring(3, 6)
						+ "." + String.substring(6, String.length());
				newList.add(String);
			} else {
				String = String.substring(0, 3) + "/"
						+ String.substring(3, String.length());
				newList.add(String);
			}
		}
		String sum = "";
		for (String string : newList) {
			sum = sum + "," + string;
		}

		sum = sum.substring(1, sum.length());
		return sum;

	}

	String modifyList(List<String> list) {
		List<String> newList = new ArrayList<String>();
		String sum = "";
		for (String string : list) {
			sum = sum + " " + string;
		}
		// sum = sum.substring(1, sum.length());
		return sum;

	}

	static Map<String, String> getClassHashMap() {
		try {

			// Workbook workbook = WorkbookUtil.create(new File("file.xlsx"))
			FileInputStream file = new FileInputStream(new File(
					"C:/Users/Suraj/workspace/LuceneSearch/List.xls"));

			// Get the workbook instance for XLS file
			HSSFWorkbook workbook = new HSSFWorkbook(file);

			// Get first sheet from the workbook
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows from first sheet
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				Double value;
				String stringValue = null;
				while (cellIterator.hasNext()) {

					Cell cell = cellIterator.next();

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						// System.out.print(cell.getBooleanCellValue() +
						// "\t\t");
						break;
					case Cell.CELL_TYPE_NUMERIC:
						// System.out.print(cell.getNumericCellValue() +
						// "\t\t");
						value = cell.getNumericCellValue();
						stringValue = value.toString();
						stringValue = stringValue.substring(0, stringValue
								.length() - 2);
						break;
					case Cell.CELL_TYPE_STRING:
						classMap.put(stringValue, cell.getStringCellValue());
						// System.out.print(cell.getStringCellValue() + "\t\t");
						break;
					}
				}
				// System.out.println("");
			}
			file.close();
			/*
			 * FileOutputStream out = new FileOutputStream(new
			 * File("C:\\test.xls")); workbook.write(out); out.close();
			 */classMap.put("D01", "Edible products");
			classMap.put("D02", "Apparel and haberdashery");
			classMap.put("D03", "Travel goods and personal belongings");
			classMap.put("D04", "Brushware");
			classMap.put("D05", "Textile or paper yard goods; sheet material");
			classMap.put("D06", "Furnishings");
			classMap
					.put("D07",
							"Equipment for preparing or serving food or drink not elsewhere specified");
			classMap.put("D08", "Tools and hardware");
			classMap.put("D09", "Packages and containers for goods");
			classMap
					.put("D10", "Measuring, testing, or signalling instruments");
			classMap.put("D11", "Jewelry, symbolic insignia, and ornaments");
			classMap.put("D12", "Transportation");
			classMap
					.put("D13",
							"Equipment for production, distribution, or transformation of energy");
			classMap
					.put("D14",
							"Recording, communication, or information retrieval equipment");
			classMap.put("D15", "Machines not elsewhere specified");
			classMap.put("D16", "Photography and optical equipment");
			classMap.put("D17", "Musical instruments");
			classMap.put("D18", "Printing and office machinery");
			classMap.put("D19",
					"Office supplies; artists and teachers materials");
			classMap.put("D20", "Sales and advertising equipment");
			classMap.put("D21", "Games, toys, and sports goods");
			classMap.put("D22",
					"Arms, pyrotechnics, hunting and fishing equipment");
			classMap
					.put("D23",
							"Environmental heating and cooling; fluid handling and sanitary equipment");
			classMap.put("D24", "Medical and laboratory equipment");
			classMap.put("D25", "Building units and construction elements");
			classMap.put("D26", "Lighting");
			classMap.put("D27", "Tobacco and smokers' supplies");
			classMap.put("D28", "Cosmetic products and toilet articles");
			classMap.put("D29", "Equipment for safety, protection, and rescue");
			classMap.put("D30", "Animal husbandry");
			classMap.put("D32", "Washing, cleaning, or drying machine");
			classMap.put("D34", "Material or article handling equipment");
			classMap.put("D99", "Miscellaneous");
			classMap
					.put(
							"G9B",
							"INFORMATION STORAGE BASED ON RELATIVE MOVEMENT BETWEEN RECORD CARRIER AND TRANSDUCER");
			classMap.put("PLT", "Plants");
			System.out.println(classMap.get(89));
			for (Map.Entry<String, String> entry : classMap.entrySet()) {
				System.out.println("key=" + entry.getKey() + ", value="
						+ entry.getValue());
			}
			System.out.println("SUraj");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classMap;
	}

}