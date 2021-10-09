package RSA_Map_API;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

import files.JsonData_RSA_Maps;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class RSA_Maps_Advance {

	public static void main( String[] args ) {

		// given() - All input details required to be send
		// when() - Submit the APIs; Includes resourse and Http Method
		// then() - validate the response

		//Add place-> Update Place with New Address -> Get Place to validate if New address is present in response

		RestAssured.baseURI = "https://rahulshettyacademy.com";

		// 1. Add place code
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(JsonData_RSA_Maps.addPlaceData())
				.when().post("/maps/api/place/add/json")
				.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
				.extract().response().asString();

		System.out.println(response);

		// To get values inside json, you need to parse this json using JsonPath
		JsonPath js = new JsonPath(response); 
		String placeId = js.getString("place_id");

		System.out.println("Place Id - " +placeId);

		// 2. Update Place code
		String newAddress = "Summer Walk, Africa";

		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
		.body("{\r\n" + 
				"\"place_id\":\""+placeId+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));

		// 3. Get Place
		String responseAfterUpdate = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
				.when().get("/maps/api/place/get/json")
				.then().log().all().assertThat().statusCode(200).extract().response().asString();

		/*
		 * Making the below code reusable in framework 
		 * 
		 * JsonPath js1 = new JsonPath(responseAfterUpdate); 
		 * Assert.assertEquals(js1.get("address"), newAddress);
		 */
		
		JsonPath js1 = ReusableMethods.rawToJson(responseAfterUpdate);
		Assert.assertEquals(js1.get("address"), newAddress);
	}

}
