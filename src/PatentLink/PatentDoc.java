package PatentLink;

public class PatentDoc {

	String id,title,abstc,assign,city,state,claims,desc,searchid;

	@Override
	public String toString() {
		return "PatentDoc [id=" + id + ", title=" + title + ", abstc=" + abstc
				+ ", assign=" + assign + ", city=" + city + ", state=" + state
				+ ", claims=" + claims + ", desc=" + desc + ", patentid="
				+ searchid + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbstc() {
		return abstc;
	}

	public void setAbstc(String abstc) {
		this.abstc = abstc;
	}

	public String getAssign() {
		return assign;
	}

	public void setAssign(String assign) {
		this.assign = assign;
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

	public String getClaims() {
		return claims;
	}

	public void setClaims(String claims) {
		this.claims = claims;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSearchid() {
		return searchid;
	}

	public void setSearchid(String searchid) {
		this.searchid = searchid;
	}
	
}
