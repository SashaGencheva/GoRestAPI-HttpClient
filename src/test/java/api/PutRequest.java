package api;

import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PutRequest {

    //create PUT Method, which will call the URL and get the response in the form of JSON object
    public static CloseableHttpResponse put(String url, String entityString, HashMap<String, String> headerMap) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url); //HTTP PUT Request
        httpPut.setEntity(new StringEntity(entityString, ContentType.APPLICATION_JSON)); //for payload

        //Create a for loop, iterate the hashmap and store the header
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpPut.addHeader(entry.getKey(), entry.getValue());
        }

        //Execute the HTTP PUT Request
        CloseableHttpResponse response = httpClient.execute(httpPut);
        return response;
    }
}