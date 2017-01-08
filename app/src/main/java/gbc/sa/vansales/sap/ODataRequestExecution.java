package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
import gbc.sa.vansales.sap.ODataRequestParam;
import gbc.sa.vansales.sap.ODataRequestExecution;

public interface ODataRequestExecution {

    String getUniqueId();

    ODataRequestExecution.Status getStatus();

    ODataRequestParam getRequest();

    ODataResponse getResponse();

    void cancelExecution();

    boolean isUpdated();

    public static enum Status {
        Initialized,
        InProgress,
        Complete,
        Canceled,
        Error;

        private Status() {
        }
    }
}
