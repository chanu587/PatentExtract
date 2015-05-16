import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.WorkbookUtil;

public class ReadPatentClassesFromExcel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Workbook workbook = WorkbookUtil.create(new File("file.xlsx"))
			Map<String, String> mMap = new HashMap<String, String>();
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
						System.out.print(cell.getBooleanCellValue() + "\t\t");
						break;
					case Cell.CELL_TYPE_NUMERIC:
						value = cell.getNumericCellValue();
						stringValue = value.toString();
						stringValue = stringValue.substring(0, stringValue
								.length() - 2);
						// System.out.print(stringValue + "\t\t");
						break;
					case Cell.CELL_TYPE_STRING:

						mMap.put(stringValue, cell.getStringCellValue());

						// System.out.print(cell.getStringCellValue() + "\t\t");

						break;
					}
				}
				for (Map.Entry<String, String> entry : mMap.entrySet()) {
					System.out.println("key=" + entry.getKey() + ", value="
							+ entry.getValue());
				}

			}
			file.close();
			System.out.println(mMap.size());
			/*
			 * FileOutputStream out = new FileOutputStream(new
			 * File("C:\\test.xls")); workbook.write(out); out.close();
			 */

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
