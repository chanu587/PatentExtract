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

public class SearchToJsonForSustain {
	private String date;

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

		String index = "C:/Users/sankam/workspace/LuceneSearch/IndexWithIpc/Cpc";
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
		QueryParser parser = new QueryParser(Version.LUCENE_40, field, analyzer);

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
		SearchToJsonForSustain files = new SearchToJsonForSustain();
		files.doPagingSearch(in, searcher, query, hitsPerPage, raw,
				(queries == null) && (queryString == null));

		reader.close();
	}

	public void doPagingSearch(BufferedReader in, IndexSearcher searcher,
			Query query, int hitsPerPage, boolean raw, boolean interactive)
			throws IOException, JSONException {
		// query.add(new fie)

		TopDocs results = searcher.search(query, 40000000);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		if (end > hits.length) {
			System.out.println("Only results 1 - " + hits.length + " of "
					+ numTotalHits + " total matching documents collected.");
			System.out.println("Collect more (y/n) ?");
			String line = in.readLine();
			hits = searcher.search(query, numTotalHits).scoreDocs;
		}

		end = numTotalHits;
		List<String[]> patentList = new ArrayList<String[]>();
		boolean success = (new File("C:/FilesAfterQueryModification/")).mkdir();
		FileWriter file = new FileWriter(new File(
				"C:/FilesAfterQueryModification/" + "/Test2.json"));

		JSONObject main = new JSONObject();
		JSONArray list = new JSONArray();
		for (int i = start; i < end; i++) {
			if (i == start) {
				patentList.add(new String[] { "PatentId", "DatePublished",
						"Assignee", "Title", "City", "State", "AbstractText",
						"Description", "usClaimStatement", "claimText" });
			}
			Document doc = searcher.doc(hits[i].doc);
			// Remove the comments to write to csv
			/*
			 * String searchString[] = { doc.get("PatentId"),
			 * doc.get("DatePublished"), doc.get("Assignee"), doc.get("Title"),
			 * doc.get("City"), doc.get("State"), doc.get("AbstractText"),
			 * doc.get("Description"), doc.get("usClaimStatement"),
			 * doc.get("claimText") };
			 */
			JSONObject obj = new JSONObject();
			obj.put("PID", doc.get("PatentId"));
			if (doc.get("DatePublished") != null)
				obj.put("Year",
						doc.get("DatePublished").toString().substring(0, 4));
			else
				obj.put("Year", "Null");
			if (doc.get("Assignee") != null)
				obj.put("Assignee", doc.get("Assignee"));
			else
				obj.put("Assignee", "Null");
			if (doc.get("AbstractText") != null)
				obj.put("AbstractText", doc.get("AbstractText"));
			else
				obj.put("AbstractText", "Null");
			if (doc.get("Title") != null)
				obj.put("Title", doc.get("Title"));
			else
				obj.put("Title", "Null");
			if (doc.get("City") != null)
				obj.put("City", doc.get("City"));
			else
				obj.put("City", "Null");
			if (doc.get("State") != null)
				obj.put("State", doc.get("State"));
			else
				obj.put("State", "Null");
			if (doc.get("usClaimStatement") != null)
				obj.put("usClaimStatement", doc.get("usClaimStatement"));
			else
				obj.put("usClaimStatement", "Null");
			if (doc.get("claimText") != null)
				obj.put("Claims", doc.get("claimText"));
			else
				obj.put("Claims", "Null");
			if (doc.get("Description") != null)
				obj.put("Description", doc.get("Description"));
			else
				obj.put("Description", "Null");
			if (doc.get("IpcrClassification") != null)
				obj.put("IpcrClassification", doc.get("IpcrClassification"));
			else
				obj.put("IpcrClassification", "Null");
			if (doc.get("CpcClassification") != null)
				obj.put("CpcClassification", doc.get("CpcClassification"));
			else
				obj.put("CpcClassification", "Null");
			if (doc.get("fileName") != null)
				obj.put("fileName", doc.get("fileName"));
			else
				obj.put("fileName", "Null");
			if (doc.get("Class") != null)
				obj.put("Class", doc.get("Class"));
			else
				obj.put("Class", "Null");
			if (doc.get("SubClass") != null)
				obj.put("SubClass", doc.get("SubClass"));
			else
				obj.put("SubClass", "Null");
			file.write(obj.toString() + "\n");
			// patentList.add(searchString);
		}
		file.flush();
		file.close();
		System.out.println(patentList.size());
	}
}
