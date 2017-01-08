package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataStore {
    ODataEntity allocateProperties(ODataEntity var1, ODataStore.PropMode var2) throws ODataException;

    ODataMetadata getMetadata();

    ODataEntity allocateNavigationProperties(ODataEntity var1) throws ODataException;

    ODataPayload.Type determineODataType(String var1, ODataRequestParamSingle.Mode var2) throws ODataException;

    String determineEntityType(String var1) throws ODataException;

    String determineEntitySet(String var1) throws ODataException;

    public static enum PropMode {
        Keys,
        Mandatory,
        Optional,
        All;

        private PropMode() {
        }
    }
}
