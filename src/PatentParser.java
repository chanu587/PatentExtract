import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.LinkedList;

public class PatentParser {

	public static void main(String[] args) throws IOException {
		String name = "/home/chan/NLPData/pg030107.XML";
		String a = readFile(name);

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
		for (int x = 1; x < 3; x++) {
			Pattern p = Pattern.compile("WKU(.*)");
			Matcher m = p.matcher(tokens[x]);
			if (m.find()) {
				patentID = m.group(1).trim();
			}
			Pattern p2 = Pattern.compile("OCL(.*){1}");
			Matcher m2 = p2.matcher(tokens[x]);
			if (m2.find()) {
				// mainClass1 = m2.group(1).trim();
				mainClass.add(m2.group(1).trim());
			}

			Pattern p3 = Pattern.compile("XCL(.*)");
			Matcher m3 = p3.matcher(tokens[x]);
			while (m3.find()) {
				// subClass1 = m3.group(1).trim();
				subClass.add(m3.group(1).trim());
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
				Pattern z = Pattern.compile("PNO(.*)", Pattern.DOTALL);
				Matcher y = z.matcher(temp);
				while (y.find()) {
					String temp1 = y.group(1).trim();
					reference.add(temp1);
				}

			}

			Pattern p9 = Pattern
					.compile("(?<=ABST).*.(?=BSUM)", Pattern.DOTALL);
			// Pattern p9 = Pattern.compile("ABST(.*?\n )*?",Pattern.DOTALL);
			Matcher m9 = p9.matcher(tokens[x]);
			while (m9.find()) {
				// abstract1 = m9.group(0).trim();
				String temp = m9.group().trim();
				Pattern z = Pattern.compile("PAL(.*)", Pattern.DOTALL);
				Matcher y = z.matcher(temp);
				while (y.find()) {
					abstract1 = y.group(1).trim();
				}
				Pattern z2 = Pattern.compile("(.*)PARN", Pattern.DOTALL);
				Matcher y2 = z2.matcher(abstract1);
				while (y2.find()) {
					abstract1 = y2.group(1).trim();

				}
			}
			abstract1 = abstract1.replaceAll("\\s+", " ");

			Pattern p10 = Pattern.compile("<?BSUM.*?(CLMS|PATN)|BSUM(.*)",
					Pattern.DOTALL);
			// Pattern p10 =
			// Pattern.compile("PAC  BACKGROUND OF THE INVENTION(.*?)PAC",Pattern.DOTALL);
			Matcher m10 = p10.matcher(tokens[x]);
			if (m10.find()) {
				background = m10.group();
				// background = background.replaceAll("PAR", "");
				background = background.replaceAll("\\s+", " ").trim();
			} else {
				background = "";
			}

			Pattern p11 = Pattern.compile("CLMS(.*)", Pattern.DOTALL);
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

			
			Pattern p20 = Pattern.compile("ASSG(.*)", Pattern.DOTALL);
			Matcher m20 = p20.matcher(tokens[x]);
			String orgName = "";
			if (m20.find()) {
				//CLaims = m20.group();
				String temp = m20.group().trim();
				Pattern z = Pattern.compile("NAM(.*)");
				Matcher y = z.matcher(temp);
				if (y.find()) {
					String temp1 = y.group(1).trim();
					orgName = temp1;
				}

			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			Pattern p15 = Pattern.compile("(?<=CLMS).*.(?=PATN)",
					Pattern.DOTALL);

			/*
			 * Pattern p10 = Pattern.compile(
			 * "(?<=PAC  BACKGROUND OF THE INVENTION).*.(?=PAC)",
			 * Pattern.DOTALL);
			 */

			// Pattern p10 =
			/*
			 * String CLaims; //
			 * Pattern.compile("PAC  BACKGROUND OF THE INVENTION(.*?)PAC"
			 * ,Pattern.DOTALL); Matcher m15 = p15.matcher(tokens[x]); if
			 * (m15.find()) { CLaims = m15.group(); // background =
			 * background.replaceAll("PAR", ""); CLaims =
			 * background.replaceAll("\\s+", " ").trim(); } else { CLaims = "";
			 * }
			 */
			System.out.println("WKU = " + patentID);
			// System.out.println("OCL = " + mainClass);
			for (int j = 0; j < mainClass.size(); j++) {
				System.out.println("OCL" + (j + 1) + " = " + mainClass.get(j));
			}
			for (int j = 0; j < subClass.size(); j++) {
				System.out.println("XCL" + (j + 1) + " = " + subClass.get(j));
			}
			System.out.println("TTL = " + title);
			System.out.println("NAM = " + name1);
			System.out.println("CTY = " + city);
			System.out.println("STA = " + state);
			for (int j = 0; j < reference.size(); j++) {
				System.out.println("reference" + (j + 1) + " = "
						+ reference.get(j));
			}
			System.out.println("abstract = " + abstract1);
			//System.out.println("background = " + background);
			System.out.println("orgName="+orgName);
			System.out.println("STM = " + stm);
			for (int j = 0; j < num.size(); j++) {
				System.out.println("num" + (j + 1) + " = " + num.get(j));
			}
			for (int j = 0; j < par.size(); j++) {
				System.out.println("par" + (j + 1) + " = " + par.get(j));
			}
			System.out.println("Claims" + CLaims);
			System.out.println();
			mainClass.clear();
			subClass.clear();
			reference.clear();
			num.clear();
			par.clear();
		}
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

// import java.io.File;
// import java.io.FileNotFoundException;
// import java.util.Scanner;
//
//
// public class Test2 {
//
// /**
// * @param args
// */
// public static void main(String[] args) {
// Scanner sc = null;
// try {
// sc = new Scanner(new
// File("C:/Users/Terry/workspace/LuceneSearch/test2a.txt"));
// } catch (FileNotFoundException e) {
// // TODO Auto-generated catch block
// e.printStackTrace();
// }
// StringBuilder sb = new StringBuilder();
//
//
//
// while (sc.hasNext()) {
// String line = sc.nextLine();
// if (line.startsWith(" ")) {
// // this is not a label
// }
// if (!line.matches("NUM  \\d+."))
// sb.append(line);
// }
// sc.close();
//
// String claims = sb.toString();
// // System.out.println(claims);
// String[] parSplit = claims.split("PAR");
// for (int i = 1; i < parSplit.length; i++) {
// System.out.println(parSplit[i]);
// }
//
// }
//
// }
//
//

