package gbc.sa.vansales.sap;
import java.net.URL;
import java.util.Map;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataStoreAsync extends ODataStore {
    ODataRequestExecution scheduleRequest(ODataRequestParam var1, ODataRequestListener var2) throws ODataException;

    ODataRequestExecution scheduleReadEntitySet(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleReadEntity(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleReadEntity(ODataEntity var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleCreateEntity(ODataEntity var1, String var2, ODataRequestListener var3, Map<String, String> var4) throws ODataException;

    ODataRequestExecution scheduleUpdateEntity(ODataEntity var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution schedulePatchEntity(ODataEntity var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleDeleteEntity(ODataEntity var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleDeleteEntity(String var1, String var2, ODataRequestListener var3, Map<String, String> var4) throws ODataException;

    ODataRequestExecution scheduleReadPropertyPrimitive(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleReadPropertyComplex(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleReadPropertyRaw(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleReadLink(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleReadLinkSet(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

    ODataRequestExecution scheduleFunction(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException;

}
