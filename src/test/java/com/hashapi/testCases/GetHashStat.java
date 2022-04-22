/*
 * Author: Lekha Mohan
 * 
 * summary: Password Encoding
 * Date: 21/04/2022
 */

package com.hashapi.testCases;

import java.util.UUID;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.*;
import com.hashapi.base.TestBase;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class GetHashStat extends TestBase {

	RequestSpecification httpRequest;
	Response response;
	String uniqueID = UUID.randomUUID().toString();
	String password = uniqueID;

	@SuppressWarnings("unchecked")
	@Test
	void getHashStat() throws InterruptedException, ParseException {
		logger.info("********* Started password hashing **********");

		RestAssured.baseURI = "http://127.0.0.1:8088";
		httpRequest = RestAssured.given();

		JSONObject requestParams = new JSONObject();

		requestParams.put("password", password);

		// Add a header stating the Request body is a JSON
		httpRequest.header("Content-Type", "application/json");

		// Add the Json to the body of the request
		httpRequest.body(requestParams.toJSONString());

		response = httpRequest.request(Method.POST, "/hash");

		Thread.sleep(1000);

		// call get api to get encoded password
		ResponseBody stats = httpRequest.request(Method.GET, "/stats").getBody();

		// First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = stats.jsonPath();

		// Then simply query the JsonPath object to get a value of the node
		Integer totalRequests = jsonPathEvaluator.get("TotalRequests");
		Integer averageTime = jsonPathEvaluator.get("AverageTime");

		// Let us print the city variable to see what we got
		System.out.println("Total Request " + totalRequests);
		System.out.println("AverageTime " + averageTime);
		// Validate the response
		Assert.assertTrue(totalRequests != 0);
		Assert.assertTrue(averageTime != 0);

	}

	@AfterClass
	void tearDown() {
		logger.info("*********  Finished Password Hashing **********");
	}

}
