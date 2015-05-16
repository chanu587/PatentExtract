import java.awt.EventQueue;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.*;

/** @see http://stackoverflow.com/questions/4637215 */
public class sa extends JFrame {

	private static final String s = "0.000000000000000";
	private JProgressBar progressBar = new JProgressBar(0, 100);
	private JLabel label = new JLabel(s, JLabel.CENTER);
	private String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public sa() {
		this.setLayout(new GridLayout(0, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(progressBar);
		this.add(label);
		this.setSize(161, 100);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void runCalc() {
		progressBar.setIndeterminate(true);
		TwoWorker task = new TwoWorker();
		task.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if ("progress".equals(e.getPropertyName())) {
					progressBar.setIndeterminate(false);
					progressBar.setValue((Integer) e.getNewValue());
				}
			}
		});
		task.execute();
	}

	private class TwoWorker extends SwingWorker<Double, Double> {

		private static final int N = 5;
		private final DecimalFormat df = new DecimalFormat(s);
		double x = 1;

		@Override
		protected Double doInBackground() throws Exception {
			/*
			 * for (int i = 1; i <= N; i++) { x = x - (((x * x - 2) / (2 * x)));
			 * setProgress(i * (100 / N)); publish(Double.valueOf(x));
			 * Thread.sleep(1000); // simulate latency }
			 */
			// String date1 = "20130101";
			String datename = date.substring(2);
			String year = date.substring(0, 4);
			String searchString = "http://storage.googleapis.com/patents/grant_full_text";
			String fulltext = "http://storage.googleapis.com/patents/grant_full_text/2013/ipg130101.zip";
			String finalString = searchString + "/" + year + "/ipg" + datename
					+ ".zip";
			System.out.println(finalString);
			 publish(Double.valueOf(x));
			UrlDownload download = new UrlDownload();
			download.fileDownload(finalString, "C:/Users/Suraj/Downloads");
			progressBar.setVisible(false);
			dispose();
			return Double.valueOf(x);
		}

		@Override
		protected void process(List<Double> chunks) {
			for (double d : chunks) {
				label.setText("Loading . . ");
			}
		}
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				sa t = new sa();
				t.setDate(args[0]);
				t.runCalc();
			}
		});
	}
}