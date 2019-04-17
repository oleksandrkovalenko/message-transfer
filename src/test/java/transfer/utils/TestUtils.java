package transfer.utils;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import org.apache.http.entity.ContentType;
import org.springframework.http.HttpHeaders;

public class TestUtils {

    public static Headers createHeaders(String authToken) {
        Header contentType = new Header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        Header authorization = new Header(HttpHeaders.AUTHORIZATION, authToken);
        return new Headers(contentType, authorization);
    }

}
