package tests;

import api.BaseRequest;
import api.PostRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
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

public class CreateUserTest extends BaseRequest {
    BaseRequest baseRequest;
    String baseUrl;
    String apiUrl;
    String url;
    String accessToken;

    File jsonData;
    ObjectMapper mapper;
    Users user;
    Users userResponseObject;
    Faker faker;
    HashMap<String, String> headerMap;
    CloseableHttpResponse response;

    int actualStatusCode;
    String actualReasonPhrase;
    String actualHeaderValue;
    int actualId;
    String actualName;
    String actualEmail;
    String actualGender;
    String actualStatus;
    int expectedId;
    String expectedName;
    String expectedEmail;
    String expectedGender;
    String expectedStatus;

    @BeforeTest
    public void setUp() {
        baseRequest = new BaseRequest();
        baseUrl = prop.getProperty("baseUrl");
        apiUrl = prop.getProperty("apiUrl");
        accessToken = prop.getProperty("accessToken");
        url = baseUrl + apiUrl;
    }

    @Test
    public void createUserTest() throws IOException, ProtocolException {
        //Pass the request header
        headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + accessToken);

        faker = new Faker();
        String name = faker.name().fullName();
        String email = name.replace(" ", "_") + "@gmail.com";

        //Jackson API
        mapper = new ObjectMapper();
        user = new Users(name, email, "male", "active"); //expected Users object

        //Object to json file
        String sysPath = System.getProperty("user.dir");
        jsonData = new File(sysPath + "/src/test/java/data/users.json");
        mapper.writeValue(jsonData, user);

        //Java object to json in String (request body)
        String userJsonString = mapper.writeValueAsString(user);

        //Call the API
        response = PostRequest.post(url, userJsonString, headerMap);

        //JsonString
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

        //JsonString to JSONObject
        JSONObject responseJson = new JSONObject(responseString);
        System.out.println("=================== Create User ===================");
        System.out.println("POST Request: " + url);
        System.out.println(response);
        System.out.println("Response JSON from API: " + responseJson);

        //Json to java object
        userResponseObject = mapper.readValue(responseString, Users.class); //actual Users object

        actualId = userResponseObject.getId();
        user.setId(actualId);
        mapper.writeValue(jsonData, user);

        actualStatusCode = baseRequest.getResponseCode(response);
        actualReasonPhrase = baseRequest.getResponseReasonPhrase(response);
        actualHeaderValue = baseRequest.getResponseHeaderValue(response, "Content-Type");

        actualName = userResponseObject.getName();
        actualEmail = userResponseObject.getEmail();
        actualGender = userResponseObject.getGender();
        actualStatus = userResponseObject.getStatus();

        expectedId = user.getId();
        expectedName = user.getName();
        expectedEmail = user.getEmail();
        expectedGender = user.getGender();
        expectedStatus = user.getStatus();

        Assert.assertEquals(actualStatusCode, baseRequest.RESPONSE_STATUS_CODE_201, "Response status code is not 201");
        Assert.assertEquals(actualReasonPhrase, "Created", "Response reason phrase is not 'Created'");
        Assert.assertTrue(baseRequest.isHeaderPresent(response, "Content-Type"), "Response does not contains 'Content-Type' header");
        Assert.assertTrue((actualHeaderValue.contains("application/json")), "Response 'Content-Type' header is not application/json");

        Assert.assertEquals(actualId, expectedId, "Expected and actual created user id do not match");
        Assert.assertEquals(actualName, expectedName, "Expected and actual created user name do not match");
        Assert.assertEquals(actualEmail, expectedEmail, "Expected and actual created user email do not match");
        Assert.assertEquals(actualGender, expectedGender, "Expected and actual created user gender do not match");
        Assert.assertEquals(actualStatus, expectedStatus, "Expected and actual created user status do not match");

        System.out.println("===================================================\n");
    }
}