package tests;

import api.BaseRequest;
import api.DeleteRequest;
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

public class DeleteApiTest extends BaseRequest {
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
    String actualMessage;

    @BeforeTest
    public void setUp() {
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
    public void deleteUserTest() throws IOException, ProtocolException {
        jsonData = new File("C:\\Users\\digger\\IdeaProjects\\GoRestAPIwithHTTPClient\\src\\test\\java\\data\\users.json");

        //Jackson API
        mapper = new ObjectMapper();
        user = mapper.readValue(jsonData, Users.class);
        id = String.valueOf(user.getId());

        //Call the API
        response = DeleteRequest.delete(url + "/" + id, headerMap);

        System.out.println("=================== Delete User ===================");
        System.out.println("DELETE Request: " + url + "/" + id);
        System.out.println(response);

        actualStatusCode = baseRequest.getResponseCode(response);
        actualReasonPhrase = baseRequest.getResponseReasonPhrase(response);

        if ((user.getName() != null) && (user.getEmail() != null) && (user.getGender() != null) && (user.getStatus() != null)) {
            System.out.println("User with ID " + user.getId() + " is successfully deleted");

            Assert.assertEquals(actualStatusCode, baseRequest.RESPONSE_STATUS_CODE_204, "Response status code is not 204");
            Assert.assertEquals(actualReasonPhrase, "No Content", "Response reason phrase is not 'No Content'");
        } else {
            //JsonString
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

            //JsonString to JSONObject
            JSONObject responseJson = new JSONObject(responseString);
            actualMessage = String.valueOf(responseJson.get("message"));
            actualHeaderValue = baseRequest.getResponseHeaderValue(response, "Content-Type");
            System.out.println("Requested user with ID " + user.getId() + " is already deleted");

            Assert.assertEquals(actualStatusCode, baseRequest.RESPONSE_STATUS_CODE_404, "Response status code is not 404");
            Assert.assertEquals(actualReasonPhrase, "Not Found", "Response reason phrase is not 'Not Found'");
            Assert.assertTrue(actualMessage.contains("not found"), "Expected and actual 'Not Found' message do not match");
            Assert.assertTrue(baseRequest.isHeaderPresent(response, "Content-Type"), "Response does not contains 'Content-Type' header");
            Assert.assertTrue((actualHeaderValue.contains("application/json")), "Response 'Content-Type' header is not application/json");
        }
        user.setName(null);
        user.setEmail(null);
        user.setGender(null);
        user.setStatus(null);
        mapper.writeValue(jsonData, user);

        System.out.println("===================================================\n");
    }
}