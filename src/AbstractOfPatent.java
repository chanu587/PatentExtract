import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.joda.time.Weeks;

public class AbstractOfPatent {

	static String getURL(String date1) {
		String datename = date1.substring(2);
		String year = date1.substring(0, 4);
		String month = date1.substring(5, 6);
		String date = date1.substring(7, 8);
		String searchString = "http://storage.googleapis.com/patents/grant_full_text";
		String fulltext = "http://storage.googleapis.com/patents/grant_full_text/2013/ipg130101.zip";
		String finalString = searchString + "/" + year + "/ipg" + datename
				+ ".zip";
		return finalString;

	}

	public static void main(String[] args) throws Exception {
		String date1 = "20130101";
		String datename = date1.substring(2);
		String year = date1.substring(0, 4);
		String month = date1.substring(5, 6);
		String date = date1.substring(7, 8);
		String searchString = "http://storage.googleapis.com/patents/grant_full_text";
		String fulltext = "http://storage.googleapis.com/patents/grant_full_text/2013/ipg130101.zip";

		/*
		 * Calendar a = new
		 * GregorianCalendar(Integer.parseInt(year),Integer.parseInt
		 * (month)-1,Integer.parseInt(date));
		 * System.out.println(a.get(Calendar.WEEK_OF_YEAR)); String
		 * finalString=searchString+"/"+year+"/ipgb"+date1+"wk.zip";
		 */
		String finalString = searchString + "/" + year + "/ipg" + datename
				+ ".zip";
		System.out.println(finalString);
		UrlDownload download = new UrlDownload();
		download.fileDownload(finalString, "C:/Users/Suraj/Downloads");
	}
}
