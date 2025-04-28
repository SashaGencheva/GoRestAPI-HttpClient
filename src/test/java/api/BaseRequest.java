package api;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ProtocolException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseRequest {
    public final int RESPONSE_STATUS_CODE_200 = 200;
    public final int RESPONSE_STATUS_CODE_201 = 201;
    public final int RESPONSE_STATUS_CODE_204 = 204;
    public final int RESPONSE_STATUS_CODE_404 = 404;

    public Properties prop;

    //Code to read the properties file
    public BaseRequest() {

        try {
            prop = new Properties();
            String sysPath = System.getProperty("user.dir");

            //Calling the config.properties file
            FileInputStream ip = new FileInputStream(sysPath + "/src/test/java/config/config.properties");

            //Loading the properties file
            prop.load(ip);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public int getResponseCode(CloseableHttpResponse response) {
        return response.getCode();
    }

    public String getResponseReasonPhrase(CloseableHttpResponse response) {
        return response.getReasonPhrase();
    }

    public String getResponseHeaderValue(CloseableHttpResponse response, String header) throws ProtocolException {
        return response.getHeader(header).getValue();
    }

    public boolean isHeaderPresent(CloseableHttpResponse response, String header) {
        return response.containsHeader(header);
    }
}