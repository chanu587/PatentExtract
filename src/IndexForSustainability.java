import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.FieldInfo.IndexOptions;
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
import org.json.JSONObject;
import org.xml.sax.SAXException;

public class IndexForSustainability {
	static IndexWriter writer;
	static Map<String, String> classMap = new HashMap<String, String>();

	// private File unZippedFile;

	public static void main(String[] args) throws SAXException {

		String usage = "java org.apache.lucene.demo.IndexFiles [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\nThis indexes the documents in DOCS_PATH, creating a Lucene indexin INDEX_PATH that can be searched with SearchFiles";
		String indexPath = "indexForReselience";
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
			// iwc.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
			writer = new IndexWriter(dir, iwc);
			// getClassHashMap();
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
				Reader fis;
				try {
					fis = new FileReader(file);
				} catch (FileNotFoundException fnfe) {
					return;
				}
				try {
					Scanner scanner = new Scanner(fis);
					scanner.useDelimiter("\n");
					BufferedReader in = new BufferedReader(fis);
					String sCurrentLine;
					int i = 0;
					while ((sCurrentLine = in.readLine()) != null) {
						// System.out.println(i++);
						UsPatentGrant patentGrant = new UsPatentGrant();
						JSONObject jsonObject = new JSONObject(sCurrentLine);
						if (jsonObject.getString("Year") != null)
							patentGrant.setDatePublished(jsonObject
									.getString("Year"));
						if (jsonObject.getString("PID") != null)
							patentGrant
									.setPatentId(jsonObject.getString("PID"));
						if (jsonObject.getString("AbstractText") != null)
							patentGrant.setAbstractOfThePatent(jsonObject
									.getString("AbstractText"));
						if (jsonObject.has("Assignee"))
							if (jsonObject.getString("Assignee") != null)
								patentGrant.setAssigned(jsonObject
										.getString("Assignee"));
						if (jsonObject.getString("Title") != null)
							patentGrant.setTitle(jsonObject.getString("Title"));
						if (jsonObject.getString("City") != null)
							patentGrant.setCity(jsonObject.getString("City"));
						/*
						 * if (jsonObject.getString("State") != null)
						 * patentGrant.setState(jsonObject.getString("State"));
						 */
						if (jsonObject.getString("Description") != null)
							patentGrant.setPatentText(jsonObject
									.getString("Description"));
						if (jsonObject.getString("Claims") != null)
							patentGrant.setTextForClaims(jsonObject
									.getString("Claims"));
						if (jsonObject.getString("usClaimStatement") != null)
							patentGrant.setUsClaim(jsonObject
									.getString("usClaimStatement"));
						if (jsonObject.getString("IpcrClassification") != null)
							patentGrant.setIpcString(jsonObject
									.getString("IpcrClassification"));
						if (jsonObject.getString("CpcClassification") != null)
							patentGrant.setCpcString(jsonObject
									.getString("CpcClassification"));
						if (jsonObject.getString("fileName") != null)
							patentGrant.setFileName(jsonObject
									.getString("fileName"));
						if (jsonObject.getString("Class") != null)
							patentGrant.setClassForPatent(jsonObject
									.getString("Class"));
						if (jsonObject.getString("SubClass") != null)
							patentGrant.setSubClassForPatent(jsonObject
									.getString("SubClass"));
						addPatentToIndex(patentGrant);
					}

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

	public static void addPatentToIndex(UsPatentGrant grant) throws IOException {
		Document doc = new Document();
		FieldType type=new FieldType();
		type.setStored(true);
		type.setIndexed(true);
		type.setTokenized(true);
		//type.setIndexOptions(IndexOptions.);
		if (null != grant.getUsClaim()) {
			doc.add(new Field("usClaimStatement", grant.getUsClaim(),
					type));
		}
		if (grant.getTextForClaims() != null) {

			doc.add(new Field("claimText", grant.getTextForClaims(),
					type));
		}
		if (grant.getPatentText().size() > 0) {
			doc.add(new Field("Description", modifyList(grant.getPatentText()),
					type));
		}

		if (null != grant.getClassification()) {
			String type1 = classMap.get(grant.getClassification()
					.substring(0, 3).trim());
			if (type != null)
				doc.add(new Field("MainClassification", type1, type));
			doc.add(new Field("Class", grant.getClassification().substring(0, 3), type));
			String subClass = grant.getClassification().substring(3,
					grant.getClassification().length());
			if (subClass.length() > 3) {
				subClass = subClass.substring(0, 3) + "."
						+ subClass.substring(3, subClass.length());
				doc.add(new Field("SubClass", subClass,type));
			} else
				doc.add(new Field("SubClass", subClass,type));
		}
		if (null != grant.getClassForPatent()) {
			doc.add(new Field("Class", grant.getClassForPatent(),
					type));
		}
		if (null != grant.getSubClassForPatent()) {
			doc.add(new Field("SubClass", grant.getSubClassForPatent(),
					type));
		}
		if (grant.getClassificationList().size() > 0) {
			String furtherClassification = modifyClassificationList(grant
					.getClassificationList());
			if (null != furtherClassification)
				doc.add(new Field("FurtherClassification",
						furtherClassification,type));
		}
		/*
		 * if (null != grant.getClassification()) doc.add(new
		 * Field("MainClassification", grant .getClassification(),
		 * Field.Store.YES));
		 */
		if (null != grant.getType())
			doc.add(new Field("Type", grant.getType(),type));

		if (null != grant.getAbstractOfThePatent())
			doc.add(new Field("AbstractText", grant.getAbstractOfThePatent(),
					type));
		if (null != grant.getTitle())
			doc.add(new Field("Title", grant.getTitle().trim(),
					type));
		doc.add(new Field("id", grant.getPatentId(), type));
		if (null != grant.getDatePublished())
			doc.add(new Field("DatePublished", grant.getDatePublished(),
					type));
		if (null != grant.getAssigned())
			doc.add(new Field("Assignee", grant.getAssigned().trim(),
					type));
	
		if (null != grant.getCity())
			doc.add(new Field("City", grant.getCity().trim(),type));
		if (null != grant.getState())
			doc.add(new Field("State", grant.getState(), type));
		if (null != grant.getKind())
			doc.add(new Field("kind", grant.getKind(), type));
		if (null != grant.getCpcString())
			doc.add(new Field("CPC Classification", grant.getCpcString(),
					type));
		if (null != grant.getIpcString())
			doc.add(new Field("IPCR Classification", grant.getIpcString(),
					type));
		if (null != grant.getFileName())
			doc.add(new Field("Filename", grant.getFileName(),type));
		if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
			// System.out.println("adding file");
			writer.addDocument(doc);
			// unZippedFile.delete();
		} else {
			writer.updateDocument(new Term("path", ""), doc);
		}
	}

	static String modifyClassificationList(List<String> list) {
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

	static String modifyList(List<String> list) {
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
						stringValue = stringValue.substring(0,
								stringValue.length() - 2);
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
			classMap.put("D07",
					"Equipment for preparing or serving food or drink not elsewhere specified");
			classMap.put("D08", "Tools and hardware");
			classMap.put("D09", "Packages and containers for goods");
			classMap.put("D10", "Measuring, testing, or signalling instruments");
			classMap.put("D11", "Jewelry, symbolic insignia, and ornaments");
			classMap.put("D12", "Transportation");
			classMap.put("D13",
					"Equipment for production, distribution, or transformation of energy");
			classMap.put("D14",
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
			classMap.put("D23",
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
			classMap.put(
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