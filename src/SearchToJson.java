import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchToJson {
	private String date;
	static String subClasses = "1,10,100,101,102,103,104,105,106,107,108,109,11.1,110,111,112,113,114.1,114.2,114.3,115.1,115.2,115.3,115.4,116,117,118,119,12.1,120,121,122,123,124,125,126,127.1,127.2,127.3,127.4,127.5,128,129,13.1,13.2,13.3,13.4,130,131,132,133,134,135,136,137,138,139,14,140,141,142,143,144,145,146,147,148,149,15,150.1,151.1,151.2,151.3,151.4,152.1,153.1,153.2,154.1,154.2,155.1,156.1,157.1,157.2,158.1,158.2,158.3,158.4,158.5,159.1,159.2,16,160.1,161.1,161.2,161.3,162.1,163.1,164.1,164.2,165.1,166.1,166.2,167.1,168.1,169.1,169.2,17,170.1,171.1,172.1,173.1,174.1,175.1,176.1,177.1,178.1,179.1,18,180.1,180.2,180.3,180.4,181.1,182.1,182.2,182.3,183.1,183.2,184.1,185.1,186.1,186.2,187.1,188.1,188.2,189.1,19,190.1,191.1,191.2,191.3,192.1,192.2,192.3,193.1,193.2,193.3,194.1,194.2,195.1,196.1,197.1,197.2,197.3,198.1,199.1,2.01,20,200.1,201,202,203,204,205,206,207,208,209,21,210,211,212,213,214,215,216,217,218,219,22,220,221,222,223,224,225,226.1,226.2,226.3,226.4,227,228,229,23,230,231,232.1,233.1,234.1,234.2,235.1,236.1,237.1,238.1,239.1,24,240.1,241.1,242.1,242.2,243.1,244.1,245.1,245.2,246.1,247.1,248.1,249.1,25,250.1,251.1,252.1,253.1,253.2,254,255,256,257,258,259,26.1,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276.1,277.1,277.2,278.1,279.1,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,3.01,3.02,3.03,3.04,3.05,3.06,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343.1,343.2,343.3,343.4,343.5,344,345,346,347,348,349,350,351,352,353,354,355,39,40,400,401,402,403,404.1,404.2,405,406,407,408,409,41.1,41.2,41.3,410,411,412.1,412.2,413,414.2,414.3,414.4,415,416,417,418,419,42,420,421,422.1,423,424,425,426.1,426.2,427,428,429,43,430,431,432.1,432.2,432.3,433,434,435.1,435.2,435.3,436,437,438,439,44,440,441,442,443,444,445,446,447,448,449,45,450,451,452.1,452.2,453,454,455,456.1,456.2,456.3,456.4,456.5,456.6,457,458,459,46,460,461,462,463,464,465,466,47,48,500,501,502,503,504,505,506,507,508,509,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,550.1,551,552.1,553.1,554.1,554.2,555,556.1,556.2,557,558,559,560,561,562.1,563,564,565,566,567,569.1,569.2,570,571,572,573,574,575.1,575.2,575.3,575.4,575.5,575.6,575.7,575.8,575.9,59,60,61,62,63.1,63.2,63.3,63.4,64,65,66.1,67.11,67.12,67.13,67.14,67.15,67.16,67.7,68,69,7,70,701,702,703,71,72,73,74,74.1,75,76,77,78,79,8,80,81,82,83,84,85,86,87,88,899,9,90.1,90.2,90.3,91,92,93,94,95,96,97,98,99";
	static QueryParser parser;

	public String[] getDate() {
		String[] myStringArray = new String[] { date };
		return myStringArray;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public static void main(String[] args) throws Exception {
		String usage = "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/java/4_0/demo.html for details.";

		if ((args.length > 0)
				&& (("-h".equals(args[0])) || ("-help".equals(args[0])))) {
			System.out.println(usage);
			System.exit(0);
		}

		String index = "IndexWithEverything";
		String field = "Class";
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		String queryString = null;
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(
				index)));
		IndexSearcher searcher = new IndexSearcher(reader);

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

		BufferedReader in = null;
		if (queries != null)
			in = new BufferedReader(new InputStreamReader(new FileInputStream(
					queries), "UTF-8"));
		else {
			in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		}
		parser = new QueryParser(Version.LUCENE_40, field, analyzer);

		if ((queries == null) && (queryString == null)) {
			System.out.println("Enter query: ");
		}

		String line = queryString != null ? queryString : in.readLine();

		Query query = parser.parse(line);
		System.out.println("Searching for: " + query.toString(field));

		if (repeat > 0) {
			Date start = new Date();
			for (int i = 0; i < repeat; i++) {
				searcher.search(query, null, 100);
			}
			Date end = new Date();
			System.out.println("Time: " + (end.getTime() - start.getTime())
					+ "ms");
		}
		SearchToJson files = new SearchToJson();
		files.doPagingSearch(in, searcher, query, hitsPerPage, raw,
				(queries == null) && (queryString == null));

		reader.close();
	}

	public void doPagingSearch(BufferedReader in, IndexSearcher searcher,
			Query query, int hitsPerPage, boolean raw, boolean interactive)
			throws IOException, JSONException, ParseException {
		// query.add(new fie)
		String array[] = subClasses.split(",");
		for (String x : array) {
			String query2 = "(455 AND SubClass:"+x+") OR FurtherClassification:\"455 "+x+"\"~2";  //"(455 AND SubClass:"+x+") OR FurtherClassification:/"455 "+x+"/"~2"
			Query query1 = parser.parse(query2);
			TopDocs results = searcher.search(query1, 40000000);
			ScoreDoc[] hits = results.scoreDocs;

			int numTotalHits = results.totalHits;
			System.out.println(numTotalHits + " total matching documents");

			int start = 0;
			int end = Math.min(numTotalHits, hitsPerPage);

			if (end > hits.length) {
				System.out
						.println("Only results 1 - " + hits.length + " of "
								+ numTotalHits
								+ " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = numTotalHits;
			List<String[]> patentList = new ArrayList<String[]>();
			boolean success = (new File("C:/Users/sankam/workspace/FilesAfterQueryModification/" + x)).mkdir();
			FileWriter file = new FileWriter(new File("C:/Users/sankam/workspace/FilesAfterQueryModification/" + x
					+ "/All1.json"));

			JSONObject main = new JSONObject();
			JSONArray list = new JSONArray();
			for (int i = start; i < end; i++) {
				if (i == start) {
					patentList.add(new String[] { "PatentId", "DatePublished",
							"Assignee", "Title", "City", "State",
							"AbstractText", "Description", "usClaimStatement",
							"claimText" });
				}
				Document doc = searcher.doc(hits[i].doc);
				// Remove the comments to write to csv
				/*
				 * String searchString[] = { doc.get("PatentId"),
				 * doc.get("DatePublished"), doc.get("Assignee"),
				 * doc.get("Title"), doc.get("City"), doc.get("State"),
				 * doc.get("AbstractText"), doc.get("Description"),
				 * doc.get("usClaimStatement"), doc.get("claimText") };
				 */
				JSONObject obj = new JSONObject();
				obj.put("PID", doc.get("PatentId"));
				if (doc.get("DatePublished") != null)
					obj.put("Year", doc.get("DatePublished").toString()
							.substring(0, 4));
				if (doc.get("claimText") != null)
					obj.put("Claims", doc.get("claimText"));
				if (doc.get("FurtherClassification") != null)
					obj.put("FC", doc.get("FurtherClassification"));

				file.write(obj.toString() + "\n");
				// patentList.add(searchString);
			}
			file.flush();
			file.close();
			/*
			 * String csv = "D:\\PatentsWithTextAnalysisInTitle.csv"; CSVWriter
			 * writer = new CSVWriter(new FileWriter(csv));
			 * 
			 * writer.writeAll(patentList); writer.close();
			 */
			System.out.println(patentList.size());
		}
	}
}
