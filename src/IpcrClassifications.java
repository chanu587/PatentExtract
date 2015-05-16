import java.util.ArrayList;
import java.util.List;


public class IpcrClassifications {

	List<IpcrClassification> cList = new ArrayList<IpcrClassification>();

	public List<IpcrClassification> getCList() {
		return cList;
	}

	public void setCList(IpcrClassification ipc) {
		cList.add(ipc);
	}
	
}
