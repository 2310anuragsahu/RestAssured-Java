package Library_API;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import RSA_Map_API.ReusableMethods;
import files.JsonData_Library;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;


public class DynamicJson {

	@Test(dataProvider = "3Books")
	public void addBook(String isbn, String aisle) throws InterruptedException {

		RestAssured.baseURI = "https://rahulshettyacademy.com";

		// Add 3 Book
		String resp = given().header("Content-Type", "application/json")
				.body(JsonData_Library.addBook(isbn, aisle))
				.when().post("/Library/Addbook.php")
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();

		JsonPath js1 = ReusableMethods.rawToJson(resp);
		String id = js1.get("ID");

		// Delete the Books added
		given().header("Content-Type", "application/json")
		.body("{\r\n" + 
				"    \"ID\": \""+id+"\"\r\n" + 
				"}")
		.when().delete("/Library/DeleteBook.php")
		.then().log().all().assertThat().statusCode(200);

	}

	@DataProvider(name="3Books")
	public Object[][] getData() {
		return new Object[][] {{"weart", "221134"},{"earty", "232451"}, {"aasdf","452176"}};
	}

}
