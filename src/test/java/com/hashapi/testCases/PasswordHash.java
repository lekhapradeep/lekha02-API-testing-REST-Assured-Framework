/*
 * Author: Lekha Mohan
 * 
 * summary: Password Hashing
 * Date: 21/04/2022
 */

package com.hashapi.testCases;

import java.util.UUID;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;

import com.hashapi.base.TestBase;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PasswordHash extends TestBase {

	RequestSpecification httpRequest;
	Response response;
	String uniqueID = UUID.randomUUID().toString();
	String password = uniqueID;

	@SuppressWarnings("unchecked")
	@Test
	void passwordHashing() throws InterruptedException {
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
		String responseBody = response.getBody().asString();
		System.out.println("responseBody is :" + responseBody);
		Assert.assertTrue(responseBody.toString() != null);
		int statusCode = response.getStatusCode(); // Gettng status code
		Assert.assertEquals(statusCode, 200);

	}
	
	@Test
	void passwordHashing_nullInput() throws InterruptedException {
		logger.info("********* Started password hashing **********");

		RestAssured.baseURI = "http://127.0.0.1:8088";
		httpRequest = RestAssured.given();

		JSONObject requestParams = new JSONObject();

		requestParams.put("password", "");

		// Add a header stating the Request body is a JSON
		httpRequest.header("Content-Type", "application/json");

		// Add the Json to the body of the request
		httpRequest.body(requestParams.toJSONString());

		response = httpRequest.request(Method.POST, "/hash");

		Thread.sleep(1000);
		String responseBody = response.getBody().asString();
		System.out.println("responseBody is :" + responseBody);
		int statusCode = response.getStatusCode(); // Gettng status code
		Assert.assertEquals(statusCode, 400);

	}

	@AfterClass
	void tearDown() {
		logger.info("*********  Finished Password Hashing **********");
	}

}
