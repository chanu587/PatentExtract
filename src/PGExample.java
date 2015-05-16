import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.xml.sax.SAXException;

public class PGExample {
	
	public void addContact(UsPatentGrant grant) throws IOException {
		// System.out.println("TYPE: " + contact.getType());

		System.out.println("PATENT Id: " + grant.getPatentId());
		System.out.println("Title: " + grant.getTitle());
		// System.out.println("    Assignee:    " + grant.getAssigned());
		System.out.println("    Abstract:    " + grant.getTextOfThePatent());
		System.out.println("Date" + grant.getDatePublished());
	System.out.println("    Kind:    " + grant.getKind());
		System.out.println("Claim Statement :" + grant.getUsClaim());
		System.out.println("Assignee:" + grant.getAssigned());
		System.out.println("Citations:" + grant.getCitationList());
		System.out.println("Description:" + grant.getPatentText());
		System.out.println("Citations Size:" + grant.getCitationList().size());
		System.out.println("Classification:" + grant.getClassification());
		System.out.println("Further Classification"
				+ grant.getClassificationList());

		/*
		 * brrr/PATDOC[2953]/SDOBI/B700/B730/B731/PARTY-US/NAM/ONM/STEXT/PDAT 1
		 * System.out.println(grant.getClaims().getClaimList().get(0).getClaim(
		 * ). size());
		 * System.out.println(grant.getClaims().getClaimList().get(0)
		 * .getClaimText ().size());
		 */

		List<String> newClaimList = new ArrayList<String>();
		for (Claim claim : grant.getClaims().getClaimList()) {
			newClaimList.addAll(claim.getClaim());
			newClaimList.addAll(claim.getClaimText()); // System.out.println(n);
		}
		for (String string : newClaimList) {
			// System.out.println(string);
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
		"C:/Users/Suraj/Desktop/XMLanalysis/pg040106/yo.txt"),
		true));
		for(String strLine:grant.getPatentText() ){
		bw.write(strLine);
		bw.newLine();
		}
		bw.close();
		/*
		 * for(int i=0;i<=grant.getClaims().getClaimList().size();i++){
		 * newClaimList
		 * .add(grant.getClaims().getClaimList().get(i).getClaim().get(i));
		 * newClaimList
		 * .addAll(grant.getClaims().getClaimList().get(i).getClaimText()); }
		 */

		// System.out.println(newClaimList);

	}

	public static void main(String[] args) throws IOException, SAXException {
		Digester digester = new Digester();
		digester.setValidating(false);//
		digester.addObjectCreate("brrr", PGExample.class);
		digester.addObjectCreate("brrr/PATDOC", UsPatentGrant.class);
		
		digester.addCallMethod("brrr/PATDOC/SDOBI/B500/B540/STEXT/PDAT",
				"setTitle", 0);

		digester.addCallMethod("brrr/PATDOC/SDOAB/BTEXT/PARA/PTEXT/PDAT",
				"setTextOfThePatent", 0);
		digester.addCallMethod(
				"brrr/PATDOC/SDOBI/B700/B730/B731/PARTY-US/NAM/ONM/STEXT/PDAT",
				"setAssigned", 0);
		digester.addCallMethod(
				"brrr/PATDOC/SDOBI/B500/B560/B561/PCIT/DOC/DNUM/PDAT",
				"setCitationList", 0);

		digester.addCallMethod("brrr/PATDOC/SDOBI/B100/B140/DATE/PDAT",
				"setDatePublished", 0);
		digester.addCallMethod("brrr/PATDOC/SDOBI/B100/B110/DNUM/PDAT",
				"setPatentId", 0);
		digester.addCallMethod("brrr/PATDOC/SDOBI/B500/B520/B522/PDAT",
				"setClassificationList", 0);

		digester.addCallMethod("brrr/PATDOC/SDOBI/B500/B520/B521/PDAT",
				"setClassification", 0);

		digester.addCallMethod(
				"brrr/PATDOC/SDOBI/B700/B730/B731/PARTY-US/ADR/CITY/PDAT",
				"setCity", 0);
		digester.addCallMethod(
				"brrr/PATDOC/SDOBI/B700/B730/B731/PARTY-US/ADR/STATE/PDAT",
				"setState", 0);

		digester
				.addCallMethod("brrr/PATDOC/SDOBI/B100/B130/PDAT", "setKind", 0);
		digester.addCallMethod("brrr/PATDOC/SDOCL/H/STEXT/PDAT", "setUsClaim",
				0);
		digester.addObjectCreate("brrr/PATDOC/SDOCL/CL", Claims.class);
		digester.addObjectCreate("brrr/PATDOC/SDOCL/CL/CLM", Claim.class);
		digester.addCallMethod("brrr/PATDOC/SDOCL/CL/CLM/PARA/PTEXT/PDAT",
				"setClaim", 0);
		digester.addCallMethod("brrr/PATDOC/SDOCL/CL/CLM/CLMSTEP/PTEXT/PDAT",
				"setClaimText", 0);
		digester.addCallMethod(
				"brrr/PATDOC/SDODE/DETDESC/BTEXT/PARA/PTEXT/PDAT",
				"setPatentText", 0);

		digester.addSetNext("brrr/PATDOC/SDOCL/CL/CLM", "setClaimList");
		digester.addSetNext("brrr/PATDOC/SDOCL/CL", "setClaims");
		digester.addSetNext("brrr/PATDOC", "addContact");

		PGExample abp = (PGExample) digester.parse(new File(
				"D:/pg010814.sgm"));
	}
}
