import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

public class DigesterExample {
	static int t;

	public static void addContact(UsPatentGrant grant) {
		// System.out.println(grant.getPatentId());
		List<String> newClaimList = new ArrayList<String>();
		if (null != grant.getCpc())
			System.out.println(grant.getCpc());
		if (null != grant.getCpcClassifications()) {
			for (CpcClassification claim : grant.getCpcClassifications()
					.getCList()) {

			}
		}
		t++;
	}

	public static void main(String[] args) throws IOException, SAXException {
		Digester digester = new Digester();
		digester.setValidating(false);
		digester.addObjectCreate("brrr", DigesterExample.class);
		digester.addObjectCreate("brrr/us-patent-grant", UsPatentGrant.class);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/main-cpc/classification-cpc/section",
				"setSection", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/main-cpc/classification-cpc/class",
				"setClassValue", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/main-cpc/classification-cpc/main-group",
				"setMainGroup", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/main-cpc/classification-cpc/subgroup",
				"setSubGroup", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/main-cpc/classification-cpc/subclass",
				"setSubClass", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc/classification-cpc/subclass",
				"setCpc", 0);

		digester.addObjectCreate(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc",
				CpcClassifications.class);
		digester.addObjectCreate(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc/classification-cpc",
				CpcClassification.class);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc/classification-cpc/section",
				"setSection", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc/classification-cpc/class",
				"setClassValue", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc/classification-cpc/main-group",
				"setMainGroup", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc/classification-cpc/subgroup",
				"setSubGroup", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc/classification-cpc/subclass",
				"setSubClass", 0);
		digester.addCallMethod(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc/classification-cpc/subclass",
				"setCpc", 0);
		digester.addSetNext(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc/classification-cpc",
				"setCList");
		digester.addSetNext(
				"brrr/us-patent-grant/us-bibliographic-data-grant/classifications-cpc/further-cpc",
				"setCpcClassifications");

		digester.addSetNext("brrr/us-patent-grant", "addContact");
		Removeline
				.removeLineFromFile("C:/Users/sankam/Desktop/ipg140107/ipg140107.xml");
		DigesterExample abp = (DigesterExample) digester.parse(new File(
				"C:/Users/sankam/Desktop/ipg140107/ipg140107.xml"));
	}
}
