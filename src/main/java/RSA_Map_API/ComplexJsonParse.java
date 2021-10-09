package RSA_Map_API;

import org.testng.Assert;

import files.JsonData_RSA_Maps;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {

		/** We are using Mock Data to perform our tests. 
		 * Mock data - JsonData.MockData_APINotReady */

		JsonPath js = new JsonPath(JsonData_RSA_Maps.MockData_APINotReady());

		// 1. Print No of courses returned by API
		int noOfCourses = js.getInt("courses.size()");
		System.out.println("No of courses returned by API - " + noOfCourses);

		// 2.Print Purchase Amount
		int purchase_Amount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase Amount - " + purchase_Amount);

		// 3. Print Title of the first course
		String titleOfFirstCourse = js.getString("courses[0].title");
		System.out.println("Title of the first course - " + titleOfFirstCourse);

		// 4. Print All course titles and their respective Prices
		System.out.println();
		for(int i=0; i < noOfCourses; i++) {
			System.out.println("Course "+ (i+1) + " Title - " + js.get("courses["+i+"].title"));
			System.out.println("Course "+ (i+1) + " Price - " + js.get("courses["+i+"].price"));
		}
		
		// 5. Print no of copies sold by RPA Course
		System.out.println();
		for(int i=0; i < noOfCourses; i++) {
			if(js.getString("courses["+i+"].title").equalsIgnoreCase("RPA")) {
				System.out.println("Price of RPA Course- " + js.getInt("courses["+i+"].price"));
				break;
			}
		}
		
		// 6. Verify if Sum of all Course prices matches with Purchase Amount
		System.out.println();
		int total = 0, price = 0, copies = 0;
		for(int i=0; i < noOfCourses; i++) {
			price = js.getInt("courses["+i+"].price");
			copies = js.getInt("courses["+i+"].copies");
			total = total + (price * copies);
		}
		Assert.assertEquals(total, purchase_Amount);
	}

}
