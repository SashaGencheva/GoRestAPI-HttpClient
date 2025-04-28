package api;

import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeleteRequest {

    //create DELETE Method, which will call the URL and get the response in the form of JSON object
    public static CloseableHttpResponse delete(String url, HashMap<String, String> headerMap) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url); //HTTP DELETE Request

        //Create a for loop, iterate the hashmap and store the header
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpDelete.addHeader(entry.getKey(), entry.getValue());
        }

        //Execute the HTTP DELETE Request
        CloseableHttpResponse response = httpClient.execute(httpDelete);
        return response;
    }
}