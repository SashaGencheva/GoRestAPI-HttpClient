package api;

import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PatchRequest {

    //create PATCH Method, which will call the URL and get the response in the form of JSON object
    public static CloseableHttpResponse patch(String url, String entityString, HashMap<String, String> headerMap) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(url); //HTTP PATCH Request
        httpPatch.setEntity(new StringEntity(entityString, ContentType.APPLICATION_JSON)); //for payload

        //Create a for loop, iterate the hashmap and store the header
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpPatch.addHeader(entry.getKey(), entry.getValue());
        }

        //Execute the HTTP PATCH Request
        CloseableHttpResponse response = httpClient.execute(httpPatch);
        return response;
    }
}