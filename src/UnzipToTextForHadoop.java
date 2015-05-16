import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

public class UnzipToTextForHadoop {
	static IndexWriter writer;
	static Map<String, String> classMap = new HashMap<String, String>();
	File file;
	static BufferedWriter bw;
	static int fileNumber = 0;
	// private File unZippedFile;

	public static void main(String[] args) throws SAXException {

		String usage = "java org.apache.lucene.demo.IndexFiles [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\nThis indexes the documents in DOCS_PATH, creating a Lucene indexin INDEX_PATH that can be searched with SearchFiles";
		String indexPath = "IndexWithEverything";
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
					if (unZippedFile.getName().startsWith("ipg")) {
						Removeline.removeLineFromFile(unZippedFile
								.getAbsolutePath());
						// fis = new FileInputStream(unzippedFile);
						file = new File("C:/Users/sankam/Desktop/Files1/"
								+ fileNumber + ".txt");
						if (!file.exists()) {
							file.createNewFile();
						}
						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						bw = new BufferedWriter(fw);
						Digester digester = new Digester();
						digester.setValidating(false);
						digester.addObjectCreate("brrr",
								UnzipToTextForHadoop.class);
						digester.addObjectCreate("brrr/us-patent-grant",
								UsPatentGrant.class);
						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/parties/applicants/applicant/addressbook/last-name",
								"setLastName", 0);
						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/parties/applicants/applicant/addressbook/first-name",
								"setFirstName", 0);
						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/publication-reference/document-id/date",
								"setDatePublished", 0);
						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/publication-reference/document-id/doc-number",
								"setPatentId", 0);

						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/publication-reference/document-id/kind",
								"setKind", 0);

						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/classification-national/further-classification",
								"setClassificationList", 0);

						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/classification-national/main-classification",
								"setClassification", 0);
						digester.addSetProperties(
								"brrr/us-patent-grant/us-bibliographic-data-grant/application-reference",
								"appl-type", "type");

						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/invention-title",
								"setTitle", 0);

						digester.addCallMethod(
								"brrr/us-patent-grant/abstract/p",
								"setTextOfThePatent", 0);
						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/assignees/assignee/addressbook/orgname",
								"setAssigned", 0);
						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/assignees/assignee/addressbook/address/city",
								"setCity", 0);

						digester.addCallMethod(
								"brrr/us-patent-grant/us-claim-statement",
								"setUsClaim", 0);

						digester.addCallMethod(
								"brrr/us-patent-grant/description/p",
								"setPatentText", 0);
						digester.addCallMethod(
								"brrr/us-patent-grant/us-bibliographic-data-grant/assignees/assignee/addressbook/address/state",
								"setState", 0);

						digester.addObjectCreate("brrr/us-patent-grant/claims",
								Claims.class);
						digester.addObjectCreate(
								"brrr/us-patent-grant/claims/claim",
								Claim.class);
						digester.addCallMethod(
								"brrr/us-patent-grant/claims/claim/claim-text",
								"setClaim", 0);
						digester.addCallMethod(
								"brrr/us-patent-grant/claims/claim/claim-text/claim-text",
								"setClaimText", 0);
						digester.addSetNext(
								"brrr/us-patent-grant/claims/claim",
								"setClaimList");
						digester.addSetNext("brrr/us-patent-grant/claims",
								"setClaims");

						digester.addSetNext("brrr/us-patent-grant",
								"addPatentToIndex");

						UnzipToTextForHadoop abp = (UnzipToTextForHadoop) digester
								.parse(new File(unZippedFile.getAbsolutePath()));
						System.out.println("Indexing . . .");
						unZippedFile.delete();
						fileNumber++;
						bw.close();
					} else if (unZippedFile.getName().startsWith("pg")) {
						RemoveSGMLine.removeLineFromFile(unZippedFile
								.getAbsolutePath());
						file = new File("C:/Users/sankam/Desktop/Files1/"
								+ fileNumber + ".txt");
						if (!file.exists()) {
							file.createNewFile();
						}

						FileWriter fw = new FileWriter(file.getAbsoluteFile());
						bw = new BufferedWriter(fw);
						Digester digester = new Digester();
						digester.setValidating(false);//
						digester.addObjectCreate("brrr",
								UnzipToTextForHadoop.class);
						digester.addObjectCreate("brrr/PATDOC",
								UsPatentGrant.class);

						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B500/B540/STEXT/PDAT",
								"setTitle", 0);

						digester.addCallMethod(
								"brrr/PATDOC/SDOAB/BTEXT/PARA/PTEXT/PDAT",
								"setTextOfThePatent", 0);
						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B700/B730/B731/PARTY-US/NAM/ONM/STEXT/PDAT",
								"setAssigned", 0);
						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B500/B560/B561/PCIT/DOC/DNUM/PDAT",
								"setCitationList", 0);

						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B100/B140/DATE/PDAT",
								"setDatePublished", 0);
						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B100/B110/DNUM/PDAT",
								"setPatentId", 0);
						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B500/B520/B522/PDAT",
								"setClassificationList", 0);

						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B500/B520/B521/PDAT",
								"setClassification", 0);

						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B700/B730/B731/PARTY-US/ADR/CITY/PDAT",
								"setCity", 0);
						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B700/B730/B731/PARTY-US/ADR/STATE/PDAT",
								"setState", 0);

						digester.addCallMethod(
								"brrr/PATDOC/SDOBI/B100/B130/PDAT", "setKind",
								0);
						digester.addCallMethod(
								"brrr/PATDOC/SDOCL/H/STEXT/PDAT", "setUsClaim",
								0);
						digester.addObjectCreate("brrr/PATDOC/SDOCL/CL",
								Claims.class);
						digester.addObjectCreate("brrr/PATDOC/SDOCL/CL/CLM",
								Claim.class);
						digester.addCallMethod(
								"brrr/PATDOC/SDOCL/CL/CLM/PARA/PTEXT/PDAT",
								"setClaim", 0);
						digester.addCallMethod(
								"brrr/PATDOC/SDOCL/CL/CLM/CLMSTEP/PTEXT/PDAT",
								"setClaimText", 0);
						digester.addCallMethod(
								"brrr/PATDOC/SDODE/DETDESC/BTEXT/PARA/PTEXT/PDAT",
								"setPatentText", 0);

						digester.addSetNext("brrr/PATDOC/SDOCL/CL/CLM",
								"setClaimList");
						digester.addSetNext("brrr/PATDOC/SDOCL/CL", "setClaims");
						digester.addSetNext("brrr/PATDOC", "addPatentToIndex");

						UnzipToTextForHadoop abp = (UnzipToTextForHadoop) digester
								.parse(new File(unZippedFile.getAbsolutePath()));
						System.out.println("Indexing . . .");
						unZippedFile.delete();
						fileNumber++;
						bw.close();
					} else {
						String a = readFile(unZippedFile.getName());

						String[] tokens = a.split("PATN");
						// String[] reference = null;
						String patentID = "";
						String title = "";
						String name1 = "";
						String city = "";
						String state = "";
						String abstract1 = "";
						String stm = "";
						String background = "";
						LinkedList<String> par = new LinkedList<String>();
						LinkedList<String> num = new LinkedList<String>();
						LinkedList<String> mainClass = new LinkedList<String>();
						LinkedList<String> subClass = new LinkedList<String>();
						LinkedList<String> reference = new LinkedList<String>();

						// System.out.println(tokens[1]);
						for (int x = 1; x < tokens.length; x++) {
							Pattern p = Pattern.compile("WKU(.*)");
							Matcher m = p.matcher(tokens[x]);
							if (m.find()) {
								patentID = m.group(1).trim();
							}
							Pattern p2 = Pattern.compile("OCL(.*){1}");
							Matcher m2 = p2.matcher(tokens[x]);
							String Classification = "";
							if (m2.find()) {
								// mainClass1 = m2.group(1).trim();
								Classification = m2.group(1).trim();
							}

							Pattern p3 = Pattern.compile("XCL(.*)");
							Matcher m3 = p3.matcher(tokens[x]);
							String furtherClassification = "";
							while (m3.find()) {
								// subClass1 = m3.group(1).trim();
								furtherClassification += m3.group(1).trim()
										+ ",";
							}
							Pattern p4 = Pattern.compile("TTL(.*){1}");
							Matcher m4 = p4.matcher(tokens[x]);
							if (m4.find()) {
								title = m4.group(1).trim();
							}
							Pattern p5 = Pattern.compile("NAM(.*){1}");
							Matcher m5 = p5.matcher(tokens[x]);
							if (m5.find()) {
								name1 = m5.group(1).trim();
							}
							Pattern p6 = Pattern.compile("CTY(.*){1}");
							Matcher m6 = p6.matcher(tokens[x]);
							if (m6.find()) {
								city = m6.group(1).trim();
							}
							Pattern p7 = Pattern.compile("STA(.*){1}");
							Matcher m7 = p7.matcher(tokens[x]);
							if (m7.find()) {
								state = m7.group(1).trim();
							}

							Pattern p8 = Pattern.compile("(?<=UREF).*?(?=ISD)",
									Pattern.MULTILINE | Pattern.DOTALL);
							Matcher m8 = p8.matcher(tokens[x]);
							while (m8.find()) {
								String temp = m8.group().trim();
								Pattern z = Pattern.compile("PNO(.*)",
										Pattern.DOTALL);
								Matcher y = z.matcher(temp);
								while (y.find()) {
									String temp1 = y.group(1).trim();
									reference.add(temp1);
								}

							}

							Pattern p9 = Pattern.compile(
									"(?<=ABST).*.(?=BSUM)", Pattern.DOTALL);
							// Pattern p9 =
							// Pattern.compile("ABST(.*?\n )*?",Pattern.DOTALL);
							Matcher m9 = p9.matcher(tokens[x]);
							while (m9.find()) {
								// abstract1 = m9.group(0).trim();
								String temp = m9.group().trim();
								Pattern z = Pattern.compile("PAL(.*)",
										Pattern.DOTALL);
								Matcher y = z.matcher(temp);
								while (y.find()) {
									abstract1 = y.group(1).trim();
								}

							}
							abstract1 = abstract1.replaceAll("\\s+", " ");

							Pattern p10 = Pattern.compile(
									"<?BSUM.*?(CLMS|PATN)|BSUM(.*)",
									Pattern.DOTALL);
							// Pattern p10 =
							// Pattern.compile("PAC  BACKGROUND OF THE INVENTION(.*?)PAC",Pattern.DOTALL);
							Matcher m10 = p10.matcher(tokens[x]);
							if (m10.find()) {
								background = m10.group();
								// background = background.replaceAll("PAR",
								// "");
								background = background.replaceAll("\\s+", " ")
										.trim();
							} else {
								background = "";
							}

							Pattern p11 = Pattern.compile("CLMS(.*)",
									Pattern.DOTALL);
							Matcher m11 = p11.matcher(tokens[x]);
							String CLaims = "";
							if (m11.find()) {
								CLaims = m11.group();
								String temp = m11.group().trim();
								Pattern z = Pattern.compile("STM(.*)");
								Matcher y = z.matcher(temp);
								while (y.find()) {
									String temp1 = y.group(1).trim();
									stm = temp1;
								}

								Pattern z2 = Pattern.compile("NUM(.*)");
								Matcher y2 = z2.matcher(temp);
								while (y2.find()) {
									String temp1 = y2.group(1).trim();
									num.add(temp1);
								}

								// Pattern z3 =
								// Pattern.compile("(?<=PAR).*.(?=NUM)",Pattern.DOTALL|Pattern.MULTILINE);
								Pattern z3 = Pattern.compile("PAR(.*?\n )*?\n");
								// Pattern z3 = Pattern.compile("PAR(.*)");
								Matcher y3 = z3.matcher(temp);
								while (y3.find()) {
									String temp1 = y3.group(1).trim();
									par.add(temp1);
								}
							}

							Pattern p20 = Pattern.compile("ASSG(.*)",
									Pattern.DOTALL);
							Matcher m20 = p20.matcher(tokens[x]);
							String orgName = "";
							if (m20.find()) {
								// CLaims = m20.group();
								String temp = m20.group().trim();
								Pattern z = Pattern.compile("NAM(.*)");
								Matcher y = z.matcher(temp);
								if (y.find()) {
									String temp1 = y.group(1).trim();
									orgName = temp1;
								}

							}

							Pattern p15 = Pattern.compile(
									"(?<=CLMS).*.(?=PATN)", Pattern.DOTALL);

							// System.out.println("WKU = " + patentID);
							// System.out.println("OCL = " + mainClass);
							/*
							 * for (int j = 0; j < mainClass.size(); j++) {
							 * System.out.println("OCL" + (j + 1) + " = " +
							 * mainClass.get(j)); } for (int j = 0; j <
							 * subClass.size(); j++) { System.out.println("XCL"
							 * + (j + 1) + " = " + subClass.get(j)); }
							 */
							UsPatentGrant patentGrant = new UsPatentGrant();
							patentGrant.setPatentId(patentID);
							patentGrant.setTitle(title);
							patentGrant.setAssigned(orgName);
							patentGrant.setCity(city);
							patentGrant.setState(state);
							// System.out.println("TTL = " + title);
							/*
							 * System.out.println("NAM = " + name1);
							 * System.out.println("CTY = " + city);
							 * System.out.println("STA = " + state);
							 */
							String citationList = "";
							for (int j = 0; j < reference.size(); j++) {
								citationList += reference.get(j) + ",";
							}
							patentGrant.setPatentText(background);
							patentGrant.setCitationList(citationList);
							patentGrant.setUsClaim(stm);
							patentGrant.setClassification(Classification);
							if (!furtherClassification.equals(""))
								patentGrant
										.setClassificationList(furtherClassification);

							patentGrant.setTextOfThePatent(abstract1);
							patentGrant.setClaimCompleteText(CLaims);
							/*
							 * System.out.println("abstract = " + abstract1);
							 * System.out.println("background = " + background);
							 * System.out.println("orgName=" + orgName);
							 * System.out.println("STM = " + stm);
							 */
							/*
							 * for (int j = 0; j < num.size(); j++) {
							 * System.out.println("num" + (j + 1) + " = " +
							 * num.get(j)); } System.out.println("Claims" +
							 * CLaims);
							 */
							// System.out.println();
							file = new File("C:/Users/sankam/Desktop/Files1/"
									+ fileNumber + ".txt");
							if (!file.exists()) {
								file.createNewFile();
							}

							FileWriter fw = new FileWriter(
									file.getAbsoluteFile());
							bw = new BufferedWriter(fw);
							addPatentToIndex(patentGrant);
							mainClass.clear();
							subClass.clear();
							reference.clear();
							num.clear();
							par.clear();
						}
						unZippedFile.delete();
						fileNumber++;
						bw.close();
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
			if (zipentry.getName().contains("xml")
					|| zipentry.getName().contains("XML")
					|| zipentry.getName().contains("txt")) {
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
		if (null != grant.getUsClaim()) {
			doc.add(new TextField("usClaimStatement", grant.getUsClaim(),
					Field.Store.YES));
		}
		if (grant.getClaims() != null) {
			List<String> newClaimList = new ArrayList<String>();
			for (Claim claim : grant.getClaims().getClaimList()) {
				newClaimList.addAll(claim.getClaim());
				newClaimList.addAll(claim.getClaimText());
			}
			String claimText = modifyList(newClaimList);

			doc.add(new TextField("claimText", claimText, Field.Store.YES));
			bw.write(claimText);
		}
		if (grant.getPatentText().size() > 0) {
			doc.add(new TextField("Description", modifyList(grant
					.getPatentText()), Field.Store.YES));
			bw.write(modifyList(grant.getPatentText()));
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
		doc.add(new TextField("PatentId", grant.getPatentId(), Field.Store.YES));
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
					"C:/Users/sankam/workspace/LuceneSearch/List.xls"));

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

	private static String readFile(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		} finally {
			stream.close();
		}
	}

}