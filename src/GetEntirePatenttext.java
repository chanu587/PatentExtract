import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

public class GetEntirePatenttext {
	static List<String> patentText = new ArrayList<String>();
	private String patentId;

	public String getPatentId() {
		return patentId;
	}

	public void setPatentId(String patentId) {
		this.patentId = patentId;
	}

	public void addContact1(UsPatentGrant grant) {

		if ("08361320".equals(grant.getPatentId()))
			setPatentId(grant.getPatentId());

	}

	public void addContact(UsPatentGrant grant) {
		// System.out.println("patentId" + grant.getPatentId());
		// List<String> patentText = new ArrayList<String>();
		if (getPatentId() != null){
			if(grant.getTextOfThePatent()!=null)
			patentText.add(grant.getTextOfThePatent().trim());
		}
		// System.out.println("    Text:    " + grant.getTextOfThePatent());
	}

	public GetEntirePatenttext() throws IOException, SAXException {
		super();
		parse();
	}

	private void parse() throws IOException, SAXException {

	}

	public static void main(String[] args) throws IOException, SAXException {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("brrr/us-patent-grant", GetEntirePatenttext.class);
		digester.addObjectCreate("brrr/us-patent-grant", UsPatentGrant.class);
		digester.addCallMethod("brrr/us-patent-grant/description/p",
				"setTextOfThePatent", 0);
		digester
				.addCallMethod(
						"brrr/us-patent-grant/us-bibliographic-data-grant/publication-reference/document-id/doc-number",
						"setPatentId", 0);
		digester.addSetProperties("address-book/contact",         "type", "type" );
		digester.addSetNext("brrr/us-patent-grant/description/p", "addContact");
		digester.addSetNext("brrr/us-patent-grant/us-bibliographic-data-grant",
				"addContact1");
		//  C:/Users/Suraj/Downloads/Downloads/ipg130101/ipg130101.xml
		GetEntirePatenttext abp = (GetEntirePatenttext) digester
				.parse(new File("C:/Users/Suraj/Desktop/assignee.xml.txt"));
		for (String text : patentText)
			System.out.println(text);
		System.out.println(patentText.size() + "Size of the list");
	}
}
