package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */

import gbc.sa.vansales.sap.ODataRequestExecution;
import gbc.sa.vansales.sap.ODataRequestParam;
import gbc.sa.vansales.sap.ODataResponse;
import gbc.sa.vansales.sap.ODataRequestExecution.Status;
import java.util.UUID;

public class ODataOfflineRequestExecution {
    private Object lock = new Object();
    private String uniqueId;
    private Status status;
    private ODataRequestParam request;
    private ODataResponse response;

    public String getUniqueId() {
        return this.uniqueId;
    }

    public ODataRequestParam getRequest() {
        return this.request;
    }

    public void cancelExecution() {
    }

    public ODataOfflineRequestExecution(ODataRequestParam var1) {
        this.request = var1;
        this.response = null;
        this.status = Status.Initialized;
        this.uniqueId = UUID.randomUUID().toString();
    }

    void setResponse(ODataResponse var1) {
        Object var2 = this.lock;
        synchronized(this.lock) {
            this.response = var1;
        }
    }

    public ODataResponse getResponse() {
        ODataResponse var1 = null;
        Object var2 = this.lock;
        synchronized(this.lock) {
            var1 = this.response;
            return var1;
        }
    }

    void setStatus(Status var1) {
        Object var2 = this.lock;
        synchronized(this.lock) {
            this.status = var1;
        }
    }

    public Status getStatus() {
        Status var1 = Status.Error;
        Object var2 = this.lock;
        synchronized(this.lock) {
            var1 = this.status;
            return var1;
        }
    }

    public boolean isUpdated() {
        return true;
    }
}
