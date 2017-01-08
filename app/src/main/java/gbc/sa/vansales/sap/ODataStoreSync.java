package gbc.sa.vansales.sap;
import java.net.URL;
import java.util.Map;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataStoreSync extends ODataStore {
    ODataResponse executeRequest(ODataRequestParam var1) throws ODataException;

    ODataResponseSingle executeReadEntitySet(String var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeReadEntity(String var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeReadEntity(ODataEntity var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeCreateEntity(ODataEntity var1, String var2, Map<String, String> var3) throws ODataException;

    ODataResponseSingle executeUpdateEntity(ODataEntity var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executePatchEntity(ODataEntity var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeDeleteEntity(ODataEntity var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeDeleteEntity(String var1, String var2, Map<String, String> var3) throws ODataException;

    ODataResponseSingle executeReadPropertyPrimitive(String var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeReadPropertyRaw(String var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeReadPropertyComplex(String var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeReadLink(String var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeReadLinkSet(String var1, Map<String, String> var2) throws ODataException;

    ODataResponseSingle executeFunction(String var1, Map<String, String> var2) throws ODataException;

    //void executeMediaDownload(URL var1, ODataDownloadMediaSyncListener var2) throws ODataException;
}
