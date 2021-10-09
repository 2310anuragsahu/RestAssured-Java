package files;

public class JsonData_Library {

	public static String addBook(String isbn, String aisle) {
		String s = "{\n" + 
		  		"\n" + 
		  		"\"name\":\"Learn Aomat Java\",\n" + 
		  		"\"isbn\":\""+isbn+"\",\n" + 
		  		"\"aisle\":\""+aisle+"\",\n" + 
		  		"\"author\":\"Jn foe\"\n" + 
		  		"}\n" + 
		  		" ";
				//System.out.println(s);
				 return s;
	}

}
