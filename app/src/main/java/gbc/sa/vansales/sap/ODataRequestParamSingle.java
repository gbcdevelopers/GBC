package gbc.sa.vansales.sap;
import java.util.Map;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataRequestParamSingle extends ODataRequestBatchItem, ODataRequestParam {
    String USE_CACHE_OPTION = "UseCache";

    String getResourcePath();

    void setResourcePath(String var1);

    ODataRequestParamSingle.Mode getMode();

    void setMode(ODataRequestParamSingle.Mode var1);

    Map<String, String> getOptions();

    void setOptions(Map<String, String> var1);

    ODataPayload getPayload();

    void setPayload(ODataPayload var1);

    String getEtag();

    void setEtag(String var1);

    String getContentID();

    void setContentID(String var1);

    public static enum Mode {
        Read,
        Create,
        Update,
        Patch,
        Delete;

        private Mode() {
        }

        public String getHttpMethod() {
            return this.equals(Delete)?"DELETE":(this.equals(Create)?"POST":(this.equals(Update)?"PUT":(this.equals(Patch)?"MERGE":"GET")));
        }

        public static ODataRequestParamSingle.Mode getMode(String httpMethod) {
            if(httpMethod.toLowerCase().equals("get")) {
                return Read;
            } else if(httpMethod.toLowerCase().equals("delete")) {
                return Delete;
            } else if(httpMethod.toLowerCase().equals("post")) {
                return Create;
            } else if(httpMethod.toLowerCase().equals("put")) {
                return Update;
            } else if(httpMethod.toLowerCase().equals("merge")) {
                return Patch;
            } else {
                throw new UnsupportedOperationException("This type of httpMethod is not supported: " + httpMethod);
            }
        }
    }
}
