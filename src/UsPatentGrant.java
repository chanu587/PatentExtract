import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class UsPatentGrant {
	private String patentId;
	private String abstractOfThePatent;
	private String title;
	private String Assigned;
	private String textOfThePatent;
	private String lastName;
	private String firstName;
	private String fullName;
	private String city;
	private String state;
	private String classification;
	private String type;
	private String kind;
	private List<String> patentText = new ArrayList<String>();
	private List<String> citationList = new ArrayList<String>();
	private Claims claims;
	private String claimCompleteText;
	private String textForClaims;
	private IpcrClassifications ipcrClassifications;
	private CpcClassifications cpcClassifications;
	private String cpcString;
	private String ipcString;
	private String ClassForPatent;
	private String subClassForPatent;

	

	public String getClassForPatent() {
		return ClassForPatent;
	}

	public void setClassForPatent(String classForPatent) {
		ClassForPatent = classForPatent;
	}

	public String getSubClassForPatent() {
		return subClassForPatent;
	}

	public void setSubClassForPatent(String subClassForPatent) {
		this.subClassForPatent = subClassForPatent;
	}

	public String getCpcString() {
		return cpcString;
	}

	public void setCpcString(String cpcString) {
		this.cpcString = cpcString;
	}

	public String getIpcString() {
		return ipcString;
	}

	public void setIpcString(String ipcString) {
		this.ipcString = ipcString;
	}

	public String classificationLevel;
	public String classValue;
	public String subClass;
	public String section;
	public String subGroup;
	public String mainGroup;
	public String mainCpc;

	public String getCpc() {
		if (section != null)
			return section + classValue + subClass + mainGroup + "/" + subGroup;
		return null;
	}

	public void setCpc(String mainIpcr) {
		this.mainCpc = mainIpcr;
	}

	public String getMainGroup() {
		return mainGroup;
	}

	public void setMainGroup(String mainGroup) {
		this.mainGroup = mainGroup;
	}

	public String classificationValue;
	public String classificationStatus;
	public String classificationDataSource;
	public String symbolPosition;

	public String getClassificationLevel() {
		return classificationLevel;
	}

	public void setClassificationLevel(String classificationLevel) {
		this.classificationLevel = classificationLevel;
	}

	public String getClassValue() {
		return classValue;
	}

	public void setClassValue(String classValue) {
		this.classValue = classValue;
	}

	public String getSubClass() {
		return subClass;
	}

	public void setSubClass(String subClass) {
		this.subClass = subClass;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public String getClassificationValue() {
		return classificationValue;
	}

	public void setClassificationValue(String classificationValue) {
		this.classificationValue = classificationValue;
	}

	public String getClassificationStatus() {
		return classificationStatus;
	}

	public void setClassificationStatus(String classificationStatus) {
		this.classificationStatus = classificationStatus;
	}

	public String getClassificationDataSource() {
		return classificationDataSource;
	}

	public void setClassificationDataSource(String classificationDataSource) {
		this.classificationDataSource = classificationDataSource;
	}

	public String getSymbolPosition() {
		return symbolPosition;
	}

	public void setSymbolPosition(String symbolPosition) {
		this.symbolPosition = symbolPosition;
	}

	public CpcClassifications getCpcClassifications() {
		return cpcClassifications;
	}

	public void setCpcClassifications(CpcClassifications cpcClassifications) {
		this.cpcClassifications = cpcClassifications;
	}

	private String fileName;
	private String mainIpcr;

	public String getMainIpcr() {
		return mainIpcr;
	}

	public void setMainIpcr(String mainIpcr) {
		this.mainIpcr = mainIpcr;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public IpcrClassifications getIpcrClassifications() {
		return ipcrClassifications;
	}

	public void setIpcrClassifications(IpcrClassifications ipcrClassifications) {
		this.ipcrClassifications = ipcrClassifications;
	}

	public String getTextForClaims() {
		return textForClaims;
	}

	public void setTextForClaims(String textForClaims) {
		this.textForClaims = textForClaims;
	}

	public String getClaimCompleteText() {
		return claimCompleteText;
	}

	public void setClaimCompleteText(String claimCompleteText) {
		this.claimCompleteText = claimCompleteText;
	}

	public Claims getClaims() {
		return claims;
	}

	public void setClaims(Claims claims) {
		this.claims = claims;
	}

	public List<String> getCitationList() {
		return citationList;
	}

	public void setCitationList(String citation) {
		citationList.add(citation);
	}

	private List<String> claimText = new ArrayList<String>();

	private String usClaim;

	public String getUsClaim() {
		return usClaim;
	}

	public void setUsClaim(String usClaim) {
		this.usClaim = usClaim;
	}

	public List<String> getClaimText() {
		return claimText;
	}

	public void setClaimText(String claim) {
		claimText.add(claim);
	}

	public List<String> getPatentText() {
		return patentText;
	}

	public void setPatentText(String patent)
			throws UnsupportedEncodingException {

		byte[] b = patent.getBytes("UTF-8");
		String s = new String(b, "US-ASCII");
		patentText.add(s);
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public List<String> getClassificationList() {
		return classificationList;
	}

	public void setClassificationList(String arg0) {
		classificationList.add(arg0);
	}

	private List<String> classificationList = new ArrayList<String>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private String country;
	private String datePublished;

	public String getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(String datePublished) {
		this.datePublished = datePublished;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFullName() {
		return lastName + "," + firstName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPatentId() {
		return patentId;
	}

	public void setPatentId(String patentId) {
		this.patentId = patentId;
	}

	public String getAbstractOfThePatent() {
		return abstractOfThePatent;
	}

	public void setAbstractOfThePatent(String abstractOfThePatent) {
		this.abstractOfThePatent = abstractOfThePatent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAssigned() {
		return Assigned;
	}

	public void setAssigned(String assigned) {
		Assigned = assigned;
	}

	public String getTextOfThePatent() {
		return textOfThePatent;
	}

	public void setTextOfThePatent(String textOfThePatent) {
		this.textOfThePatent = textOfThePatent;
	}

}
