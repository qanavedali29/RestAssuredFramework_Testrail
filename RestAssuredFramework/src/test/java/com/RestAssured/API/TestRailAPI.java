package com.RestAssured.API;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.RestAssured.CommonMethod.CommonMethod;
import com.RestAssured.CommonMethod.TestRailUrlInitialization;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class TestRailAPI extends TestRailUrlInitialization
{
	int project_id=0;
	int suite_id=0;
	CommonMethod commonMethod = null;
	
	public TestRailAPI()
	{
		commonMethod = new CommonMethod();
	}
	
	@Test(priority=1)
	public void addProject()
	{

		Response response = loginInToApplication().
				body("{\r\n" + 
						"	\"name\": \"This is a new project\",\r\n" + 
						"	\"announcement\": \"This is the description for the project\",\r\n" + 
						"	\"show_announcement\": true\r\n" + 
						"}").
				when().
				contentType(ContentType.JSON).
				post(baseUrl+"/index.php?/api/v2/add_project");
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority=2)
	public void getAllProject()
	{

		Response response = loginInToApplication().
				when().
				contentType(ContentType.JSON).
				get(baseUrl+"/index.php?/api/v2/get_projects");
		Assert.assertEquals(response.getStatusCode(), 200);
		project_id = response.then().extract().path("[0].id");
	}
	
	@Test(priority=3)
	public void addSuite()
	{

		Response response = loginInToApplication().
				body("{\r\n" + 
						"	\"name\": \"This is a new test suite\",\r\n" + 
						"	\"description\": \"Use the description to add additional context details\"\r\n" + 
						"}").
				when().
				contentType(ContentType.JSON).
				post(baseUrl+"/index.php?/api/v2/add_suite/"+project_id);
		Assert.assertEquals(response.getStatusCode(), 200);
		suite_id = response.then().extract().path("id");
				System.out.println("suite_id : "+suite_id);
	}
	
	@Test(priority=4)
	public void deleteProject()
	{

		Response response = loginInToApplication().
				when().
				contentType(ContentType.JSON).
				post(baseUrl+"/index.php?/api/v2/delete_project/"+project_id);
		Assert.assertEquals(response.getStatusCode(), 200);
	}
	
	@Test(priority=5)
	public void getUnknownProject() throws Exception
	{

		Response response = loginInToApplication().
				when().
				contentType(ContentType.JSON).
				get(baseUrl+"/index.php?/api/v2/get_project/"+project_id);
		Assert.assertEquals(response.getStatusCode(), 400);
		commonMethod.verifyBodyContains(response, "error");
	}

	
}
