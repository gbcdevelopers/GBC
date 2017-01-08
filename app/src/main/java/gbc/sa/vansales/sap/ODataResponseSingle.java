package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataResponseSingle extends ODataResponse, ODataResponseBatchItem {
    ODataPayload.Type getPayloadType();

    ODataPayload getPayload();
}
