import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.surround.query.FieldsQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class ExtracttoscvMatthew {
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

		String index = "indexwithMainClassificationWithCitation";
		String field = "Type";
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
		ExtracttoscvMatthew files = new ExtracttoscvMatthew();
		files.doPagingSearch(in, searcher, query, hitsPerPage, raw,
				(queries == null) && (queryString == null));

		reader.close();
	}

	public void doPagingSearch(BufferedReader in, IndexSearcher searcher,
			Query query, int hitsPerPage, boolean raw, boolean interactive)
			throws IOException {
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
		for (int i = start; i < end; i++) {
			if (i == start) {
				patentList.add(new String[]{"PatentId","Title","Citation"});
			}
			Document doc = searcher.doc(hits[i].doc);
			String searchString[] = {doc.get("PatentId"), doc.get("Title"),doc.get("Citation") };
			patentList.add(searchString);
		}
		String csv = "D:\\AllCitationDesign.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(csv));
		
		writer.writeAll(patentList);
		writer.close();
		System.out.println(patentList.size());
	}
}
