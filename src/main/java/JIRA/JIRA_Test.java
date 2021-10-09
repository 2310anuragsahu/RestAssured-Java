package JIRA;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.annotations.Test;

public class JIRA_Test {

	public static SessionFilter get_Session_Id() {

		RestAssured.baseURI = "http://localhost:8081";

		// Getting sessionID Using Session Filter
		SessionFilter session = new SessionFilter();

		given().log().all().header("Content-Type", "application/json")
		.body("{ \"username\": \"2310anurag\", \"password\": \"Welcome1!\" }")
		
		.filter(session)
		
		// For bypassing HTTP Certificate Validations 
		.relaxedHTTPSValidation()
		
		.when().post("/rest/auth/1/session")
		.then().log().all();

		return session;

		/*	Old Method
		 * String response = given().log().all().header("Content-Type",
		 * "application/json")
		 * .body("{ \"username\": \"2310anurag\", \"password\": \"Welcome1!\" }")
		 * .when().post("/rest/auth/1/session")
		 * .then().log().all().extract().response().asString();
		 * 
		 * JsonPath js = new JsonPath(response); 
		 * return js.get("session.value");
		 */
	}

	public static void add_JIRA_Issue(SessionFilter sessionId, String projectKey, String summary, String issueType) {

		RestAssured.baseURI = "http://localhost:8081";

		given().log().all().header("Content-Type", "application/json")
		.header("Cookie", "JSESSIONID="+sessionId+"")
		.body("{\r\n" + 
				"    \"fields\": {\r\n" + 
				"        \"project\": {\r\n" + 
				"            \"key\": \""+projectKey+"\"\r\n" + 
				"        },\r\n" + 
				"        \"summary\": \""+summary+"\",\r\n" + 
				"        \"issuetype\": {\r\n" + 
				"            \"name\": \""+issueType+"\"\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}")
		.when().post("/rest/api/2/issue")
		.then().log().all();
	}

	public static void add_Comment_In_Issue(SessionFilter sessionId, String summary) {

		RestAssured.baseURI = "http://localhost:8081";

		given().log().all().header("Content-Type", "application/json")
		.filter(sessionId)
		.pathParam("key", "10100")
		.body("{\r\n" + 
				"    \"body\": \""+summary+"\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}")
		.when().post("/rest/api/2/issue/{key}/comment")
		.then().log().all();
	}

	public static void add_Attachments_In_Issue(SessionFilter sessionId) {

		RestAssured.baseURI = "http://localhost:8081";

		given().log().all()
		
		// IMP lines of code
		.header("X-Atlassian-Token", "no-check")
		.header("Content-Type", "multipart/form-data") 
		.filter(sessionId)
		.multiPart("file", new File("C:\\Users\\inasahu\\demows\\RestAssured_RahulShetty\\src\\main\\java\\files\\JIRA_Attachment.txt"))
		
		.pathParam("key", "10100")
		.when().post("/rest/api/2/issue/{key}/attachments")
		.then().log().all().assertThat().statusCode(200);
	}

	public static String get_JIRA_Issue_Details(SessionFilter sessionId) {
		
		RestAssured.baseURI = "http://localhost:8081";

		String issueDetails = given().log().all().filter(sessionId).pathParam("key", "10100")
				.queryParam("fields", "comment")
		.when().get("/rest/api/2/issue/{key}")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		return issueDetails;
	}
	
	@Test
	public void jiraTest(){

		// Get Session details using Session filter
		SessionFilter session = get_Session_Id();
		System.out.println(session);

		// Create JIRA Ticket
		// add_JIRA_Issue(session, "RES", "1st Ticket", "Bug");

		// Add Comment in JIRA Ticket
		// add_Comment_In_Issue(session, "3rd Comment on this ticket");
		
		// Add an Attachment in JIRA Ticket
		// add_Attachments_In_Issue(session);
		
		// Get Issue Details 
		String issueDetails = get_JIRA_Issue_Details(session);
		

		JsonPath js = new JsonPath(issueDetails);
		int length = js.getInt("fields.comment.comments.size()");
		
		for(int i=0; i<length;i++) {
			String id = js.get("fields.comment.comments["+i+"].id").toString();
			if(id.equalsIgnoreCase("10103")) {
				System.out.println("Comment is successfully added");
				break;
			} else {
				System.out.println("Comment not Added");
			}
		}
	}

}
