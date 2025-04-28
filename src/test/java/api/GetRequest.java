package api;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class GetRequest {

    //create GET Method, which will call the URL and get the response in the form of JSON object
    public static CloseableHttpResponse get(String url, HashMap<String, String> headerMap) throws IOException {

        /*
        Call HTTPClients class from HTTPClient library added in POM.xml.
        Call createDefault() method present in HTTPClients class, which will create a client connection.
        And this createDefault() method returns CloseableHttpClient object, which is an abstract class.
        And we are creating a reference to that abstract class.
         */
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //Create an object for HttpGet class, which is used for HTTP GET Request and pass the URL which is to be loaded
        HttpGet httpGet = new HttpGet(url); //HTTP GET Request

        //Create a for loop, iterate the hashmap and store the header
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }

        /*
        Execute the HTTP GET Request, means it will hit the GET API call as we click SEND button from POSTMAN client.
        httpClient.execute() method returns the response CloseableHttpResponse interface and store it in reference variable.
        So the complete response is stored in CloseableHttpResponse.
        Fetch all the details in test case/test method.
         */
        CloseableHttpResponse response = httpClient.execute(httpGet); //Execute the HTTP GET Request
        return response;
    }

    //create GET Method with Query Parameters, which will call the URL and get the response in the form of JSON object
    public static CloseableHttpResponse getWithParams(String url, String accessToken) throws IOException, ProtocolException {
        HttpGet getParams = new HttpGet(url); //HTTP GET Request

        List<NameValuePair> params = new ArrayList<>();
        //GET Query Parameters
        params.add(new BasicNameValuePair("access-token", accessToken));
        //Add to the request URL
        try {
            URI uri = new URIBuilder(new URI(url))
                    .addParameters(params)
                    .build();
            getParams.setUri(uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(getParams); //Execute the HTTP GET Request
        return response;
    }
}