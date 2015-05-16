import java.util.ArrayList;
import java.util.List;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<String> list = new ArrayList<String>();
		List<String> newList = new ArrayList<String>();
		list.add("604396");
		list.add("60438524");
		list.add("60438525");

		for(String String:list){
			if(String.length()>6){
				String = String.substring(0, 3) + "/"
				+ String.substring(3, 6)+"."+String.substring(6, String.length());
				newList.add(String);
			}else{
			String = String.substring(0, 3) + "/"
			+ String.substring(3, String.length());
			newList.add(String);
			}
		}
		String sum = "";
		for(String string:newList){
			sum=sum+","+string;
		}
		
		System.out.println(sum.substring(1,sum.length()));
		
		
		
		
		
	}

}
