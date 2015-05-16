import java.util.ArrayList;
import java.util.List;

public class Claim {

	List<String> claimText = new ArrayList<String>();
	private List<String> claim = new ArrayList<String>();

	public List<String> getClaim() {
		return claim;
	}

	public void setClaim(String claim1) {
		claim.add(claim1);
	}

	public List<String> getClaimText() {
		return claimText;
	}

	public void setClaimText(String claim1) {
		claimText.add(claim1);
	}
}
