import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class WebServicesTestingExam {

	@BeforeTest
	public void setUp() {
		RestAssured.baseURI = "http://localhost:8080";
		RestAssured.basePath = "/landlords";
	}

	@Test(enabled = true)
	public void Test1() {

		// request-response GET
		Response response = RestAssured.given().contentType(ContentType.JSON).get("");
		String jsonResponseBody = response.asString();
		JsonPath jp = new JsonPath(jsonResponseBody);
		System.out.println("json Response Body: \t" + jsonResponseBody);

		// assertions
		assertEquals(200, response.getStatusCode());
		assertEquals(0, jp.getList("").size());

	}

	@Test(enabled = true)
	public void Test2() {

		// create landlord pojo
		LandLord landLord = new LandLord();
		landLord.setFirstName("aana");
		landLord.setLastName("sa");

		// request-response POST
		String jsonRequestBody = "{  \"firstName\": \"" + landLord.getFirstName() + "\",  \"lastName\": \""
				+ landLord.getLastName() + "\",  \"trusted\": true }";
		Response response = RestAssured.given().contentType(ContentType.JSON).body(jsonRequestBody).when().post("");
		String jsonResponseBody = response.asString();
		JsonPath jp = new JsonPath(jsonResponseBody);
		System.out.println("json Request Body: \t" + jsonRequestBody);
		System.out.println("json Response Body: \t" + jsonResponseBody);

		// update landlord pojo with id created by server
		landLord.setId(jp.getString("id"));

		// assertions
		assertEquals(201, response.getStatusCode());
		assertEquals(landLord.getFirstName(), jp.get("firstName"));
		assertEquals(landLord.getLastName(), jp.get("lastName"));

	}

	@Test(enabled = true)
	public void Test3_and_5() {
		// landlord pojo
		LandLord landLord = new LandLord();
		landLord.setFirstName("john");
		landLord.setLastName("KL");

		// request-response POST
		// create land lord
		String jsonRequestBody = "{  \"firstName\": \"" + landLord.getFirstName() + "\",  \"lastName\": \""
				+ landLord.getLastName() + "\",  \"trusted\": true }";
		Response response = RestAssured.given().contentType(ContentType.JSON).body(jsonRequestBody).when().post("");
		String jsonResponseBody = response.asString();
		JsonPath jp = new JsonPath(jsonResponseBody);
		System.out.println("json Request Body: \t" + jsonRequestBody);
		System.out.println("json Response Body: \t" + jsonResponseBody);

		// update landlord pojo with id created by server
		landLord.setId(jp.getString("id"));

		// assertions
		assertEquals(201, response.getStatusCode());
		assertEquals(0, jp.getList("apartments").size());

		// modify pojo
		landLord.setFirstName("antony");

		// request-response PUT
		// modify landlord
		jsonRequestBody = "{  \"firstName\": \"" + landLord.getFirstName() + "\",  \"lastName\": \""
				+ landLord.getLastName() + "\",  \"trusted\": true }";
		response = RestAssured.given().contentType(ContentType.JSON).body(jsonRequestBody).when()
				.put("/" + landLord.getId());
		jsonResponseBody = response.asString();
		jp = new JsonPath(jsonResponseBody);
		System.out.println("json Request Body: \t" + jsonRequestBody);
		System.out.println("json Response Body: \t" + jsonResponseBody);

		// assertions
		assertEquals(200, response.getStatusCode());
		String message = jp.getString("message");
		String messageExpected = "LandLord with id: " + landLord.getId() + " successfully updated";
		assertEquals(message, messageExpected);

		// request-response GET
		// get landlord with id
		response = RestAssured.given().contentType(ContentType.JSON).get("/" + landLord.getId());
		jsonResponseBody = response.asString();
		jp = new JsonPath(jsonResponseBody);
		System.out.println("json Response Body: \t" + jsonResponseBody);

		// assertions
		assertEquals(200, response.getStatusCode());
		assertEquals(landLord.getId(), jp.getString("id"));
		assertEquals(landLord.getFirstName(), jp.getString("firstName"));
		assertEquals(landLord.getLastName(), jp.getString("lastName"));

	}

	@Test
	public void test4() {
		LandLord landLord = new LandLord();
		landLord.setId("4DKIS34");
		landLord.setFirstName("saara");
		landLord.setLastName("KL");
		String jsonRequestBody = "{  \"firstName\": \"" + landLord.getFirstName() + "\",  \"lastName\": \""
				+ landLord.getLastName() + "\",  \"trusted\": true }";
		Response response = RestAssured.given().contentType(ContentType.JSON).body(jsonRequestBody).when()
				.put("/" + landLord.getId());
		String jsonResponseBody = response.asString();
		JsonPath jp = new JsonPath(jsonResponseBody);
		System.out.println("json Request Body: \t" + jsonRequestBody);
		System.out.println("json Response Body: \t" + jsonResponseBody);
		assertEquals(404, response.getStatusCode());
		String message = jp.getString("message");
		String messageExpected = "There is no LandLord with id: " + landLord.getId();
		assertEquals(message, messageExpected);

	}

}
