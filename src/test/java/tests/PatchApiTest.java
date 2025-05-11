package tests;

import api.BaseRequest;
import api.PatchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import data.Users;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class PatchApiTest extends BaseRequest {
    BaseRequest baseRequest;
    String baseUrl;
    String apiUrl;
    String url;
    String accessToken;
    String id;

    HashMap<String, String> headerMap;
    File jsonData;
    ObjectMapper mapper;
    Users user;
    CloseableHttpResponse response;

    int actualStatusCode;
    String actualReasonPhrase;
    String actualHeaderValue;
    int actualId;
    String actualName;
    String actualEmail;
    String actualGender;
    String actualStatus;
    String actualMessage;
    int expectedId;
    String expectedName;
    String expectedEmail;
    String expectedGender;
    String expectedStatus;

    @BeforeTest
    public void SetUp() {
        baseRequest = new BaseRequest();
        baseUrl = prop.getProperty("baseUrl");
        apiUrl = prop.getProperty("apiUrl");
        accessToken = prop.getProperty("accessToken");
        url = baseUrl + apiUrl;

        //Pass the request header
        headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + accessToken);
    }

    @Test
    public void partiallyUpdateUser() throws IOException, ProtocolException {
        String sysPath = System.getProperty("user.dir");
        jsonData = new File(sysPath + "/src/test/java/data/users.json");
        //jsonData = new File("C:\\Users\\digger\\IdeaProjects\\GoRestAPIwithHTTPClient\\src\\test\\java\\data\\users.json");

        //Jackson API
        mapper = new ObjectMapper();
        user = mapper.readValue(jsonData, Users.class); //expected Users object
        id = String.valueOf(user.getId());

        if ((user.getName() != null) && (user.getEmail() != null) && (user.getGender() != null) && (user.getStatus() != null)) {
            System.out.println("User details before PATCH request: " + user.getId() + ", " + user.getName() + ", " + user.getEmail() + ", " + user.getGender() + ", " + user.getStatus());
            user.setStatus("active");
            mapper.writeValue(jsonData, user);
        }

        //Java object to json in String (serialization)
        String userJsonString = mapper.writeValueAsString(user); //request body

        //Call the API
        response = PatchRequest.patch(url + "/" + id, userJsonString, headerMap);

        //JsonString
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

        //JsonString to JSONObject
        JSONObject responseJson = new JSONObject(responseString);
        System.out.println("============== Partially Update User ==============");
        System.out.println("PATCH Request: " + url + "/" + id);
        System.out.println(response);
        System.out.println("Response JSON from API: " + responseJson);

        actualStatusCode = baseRequest.getResponseCode(response);
        actualReasonPhrase = baseRequest.getResponseReasonPhrase(response);
        actualHeaderValue = baseRequest.getResponseHeaderValue(response, "Content-Type");

        if ((user.getName() != null) && (user.getEmail() != null) && (user.getGender() != null) && (user.getStatus() != null)) {
            actualId = (int) responseJson.get("id");
            actualName = String.valueOf(responseJson.get("name"));
            actualEmail = String.valueOf(responseJson.get("email"));
            actualGender = String.valueOf(responseJson.get("gender"));
            actualStatus = String.valueOf(responseJson.get("status"));
            expectedId = user.getId();
            expectedName = user.getName();
            expectedEmail = user.getEmail();
            expectedGender = user.getGender();
            expectedStatus = user.getStatus();

            Assert.assertEquals(actualStatusCode, baseRequest.RESPONSE_STATUS_CODE_200, "Response status code is not 200");
            Assert.assertEquals(actualReasonPhrase, "OK", "Response reason phrase is not 'OK'");

            Assert.assertEquals(actualId, expectedId, "Expected and actual partially updated user id do not match");
            Assert.assertEquals(actualName, expectedName, "Expected and actual partially updated user name do not match");
            Assert.assertEquals(actualEmail, expectedEmail, "Expected and actual partially updated user email do not match");
            Assert.assertEquals(actualGender, expectedGender, "Expected and actual partially updated user gender do not match");
            Assert.assertEquals(actualStatus, expectedStatus, "Expected and actual partially updated user status do not match");
        } else {
            actualMessage = String.valueOf(responseJson.get("message"));
            System.out.println("Requested user with ID " + user.getId() + " is already deleted");

            Assert.assertEquals(actualStatusCode, baseRequest.RESPONSE_STATUS_CODE_404, "Response status code is not 404");
            Assert.assertEquals(actualReasonPhrase, "Not Found", "Response reason phrase is not 'Not Found'");
            Assert.assertTrue(actualMessage.contains("not found"), "Expected and actual 'Not Found' message do not match");
        }
        Assert.assertTrue(baseRequest.isHeaderPresent(response, "Content-Type"), "Response does not contains 'Content-Type' header");
        Assert.assertTrue((actualHeaderValue.contains("application/json")), "Response 'Content-Type' header is not application/json");

        System.out.println("===================================================\n");
    }
}