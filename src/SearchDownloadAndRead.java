import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchDownloadAndRead {
	public static void main(String[] args) throws Exception {
		String index = "index";
		String field = "PatentId";
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
		while (true) {
			if ((queries == null) && (queryString == null)) {
				System.out.println("Enter query: ");
			}

			String line = queryString != null ? queryString : in.readLine();

			if ((line == null) || (line.length() == -1)) {
				break;
			}
			line = line.trim();
			if (line.length() == 0) {
				break;
			}
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
			SearchXMLFiles xmlFiles = new SearchXMLFiles();
			xmlFiles.doPagingSearch(in, searcher, query, hitsPerPage, raw,
					(queries == null) && (queryString == null));
			
			sa.main(xmlFiles.getDate());
			if (queryString != null) {
				break;
			}
		}
	}

	private static void downloadFile(String date) {
		String year = date.substring(0, 4);
		String searchString = "http://storage.googleapis.com/patents/grantbib";
		String finalString=searchString+"/"+year+"/ipgb"+date+".zip";
		UrlDownload download=new UrlDownload();
		download.fileDownload("", "C:/Users/Suraj/Downloads");
		
	}

}
