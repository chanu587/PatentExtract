
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenerateCsv {
	public static void main(String[] args) throws IOException {
		String csv = "D:\\output2.csv";
		CSVWriter writer = new CSVWriter(new FileWriter(csv));

		List<String[]> data = new ArrayList<String[]>();
		data.add(new String[] { "India", "New Delhi" });
		data.add(new String[] { "United States", "Washington D.C" });
		data.add(new String[] { "Germany", "Berlin" });

		writer.writeAll(data);

		writer.close();
	}

}