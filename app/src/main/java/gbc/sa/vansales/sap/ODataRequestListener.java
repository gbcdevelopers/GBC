package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataRequestListener {
    void requestStarted(ODataRequestExecution var1);

    void requestCacheResponse(ODataRequestExecution var1);

    void requestServerResponse(ODataRequestExecution var1);

    void requestFailed(ODataRequestExecution var1, ODataException var2);

    void requestFinished(ODataRequestExecution var1);
}
