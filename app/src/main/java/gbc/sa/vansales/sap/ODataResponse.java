package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
import java.util.Map;

public interface ODataResponse {
    String getCustomTag();
    Map<ODataResponse.Headers, String> getHeaders();
    boolean isBatch();

    public static enum Headers {
        Code("code"),
        Location("location"),
        ETag("etag"),
        LastModified("last-modified"),
        CacheControl("cache-control"),
        ContentType("content-type"),
        ContentLength("content-length");

        public String value;

        private Headers(String value) {
            this.value = value;
        }
    }
}
