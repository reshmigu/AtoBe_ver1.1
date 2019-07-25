package com.test.xrayapis;

import static com.test.xrayapis.XrayAPIs.TEST_EXECUTION_GET_URL;
import static com.test.xrayapis.XrayAPIs.TEST_RUN_GET_URL;
import static com.test.xrayapis.XrayAPIs.TEST_RUN_STATUS_PUT_URL;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.client.utils.URIBuilder;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class XrayAPIIntegration {
	private static final ResourceBundle rb = ResourceBundle.getBundle("application");
	private static final String BASE_URL = rb.getString("baseUrl");

	public List<TestExecution> getTestExecution() {
		String exc = TEST_EXECUTION_GET_URL;
		String api = exc.replace("execid", rb.getString("testexecutionkey"));
		RestAssured.baseURI = BASE_URL;
		Response response = RestAssured.given().auth().preemptive().basic("thinkpalm", "Think@123").get(api);
		if (response.getStatusCode() == 200) {
			return Arrays.asList(response.getBody().as(TestExecution[].class));
		}
		return Collections.emptyList();

	}

	public String updateTestCaseStatus(int testRunId, String status) throws URISyntaxException {
		String testRunPutUrl = TEST_RUN_STATUS_PUT_URL;
		String replacedUrl = testRunPutUrl.replace("id", "" + testRunId);
		URIBuilder b = new URIBuilder(BASE_URL + replacedUrl);
		URI u = b.addParameter("status", status).build();
		Response response = RestAssured.given().auth().preemptive().basic("thinkpalm", "Think@123").put(u);
		return response.getBody().prettyPrint();
	}

	public TestRun getTestRun(String testKey) throws URISyntaxException {
		String testRunGetUrl = TEST_RUN_GET_URL;
		URIBuilder b = new URIBuilder(BASE_URL + testRunGetUrl);
		b.addParameter("testIssueKey", testKey);
		b.addParameter("testExecIssueKey", rb.getString("testexecutionkey"));
		URI url = b.build();
		Response response = RestAssured.given().auth().preemptive().basic("thinkpalm", "Think@123").get(url);
		if (response.getStatusCode() == 200)
			return response.getBody().as(TestRun.class);
		return null;
	}
}
