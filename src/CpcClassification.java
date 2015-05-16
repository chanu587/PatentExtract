public class CpcClassification {

	public String classificationLevel;
	public String classValue;
	public String subClass;
	public String section;
	public String subGroup;
	public String mainGroup;
	public String mainCpc;

	public String getCpc() {if (section != null)
		return section + classValue + subClass + mainGroup + "/" + subGroup;
	return null;}

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

}
