/*
 * Author: Lekha Mohan
 * 
 * summary: Password Encoding
 * Date: 21/04/2022
 */

package com.hashapi.testCases;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;

import com.hashapi.base.TestBase;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class GetEncodedPassword extends TestBase {

	RequestSpecification httpRequest;
	Response response;
	String uniqueID = UUID.randomUUID().toString();
	String password = uniqueID;

	@SuppressWarnings("unchecked")
	@Test
	void getEncodedpassword() throws InterruptedException {
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
		String jobId = response.getBody().asString();
		// call get api to get encoded password
		ResponseBody hashcode = httpRequest.request(Method.GET, "/hash/" + jobId).getBody();
		Assert.assertTrue(hashcode != null);
		System.out.println("Encoded password" + hashcode.asString());

	}

	@SuppressWarnings("unchecked")
	@Test
	void getEncodedpassword_matchesBase64() throws InterruptedException {
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
		String jobId = response.getBody().asString();
		// call get api to get encoded password
		ResponseBody hashcode = httpRequest.request(Method.GET, "/hash/" + jobId).getBody();
		Assert.assertTrue(hashcode != null);
		System.out.println("Encoded password" + hashcode.asString());
		//calling hashing algorithm SHA-512 and base64 encoding of hash 
		String encodedBase64Value=base64Encoding(password);
		Assert.assertEquals(hashcode.asString(), encodedBase64Value);
	}

	//method for base64 encoding
	public String base64Encoding(String password) {
		try {
			// getInstance() method is called with algorithm SHA-512
			MessageDigest md = MessageDigest.getInstance("SHA-512");

			// digest() method is called
			// to calculate message digest of the input string
			// returned as array of byte
			byte[] messageDigest = md.digest(password.getBytes(StandardCharsets.UTF_8));

			// base64 encoding the hash value
			String encodedString = Base64.getEncoder().encodeToString(messageDigest);

			System.out.println("base64 string: " + encodedString);
			return encodedString;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterClass
	void tearDown() {
		logger.info("*********  Finished Password Hashing **********");
	}

}
