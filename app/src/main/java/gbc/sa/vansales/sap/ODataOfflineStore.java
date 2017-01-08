package gbc.sa.vansales.sap;
import android.content.Context;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public class ODataOfflineStore implements ODataStoreSync, ODataStoreAsync {
    private static final int STORE_OPENING = 0;
    private static final int STORE_INITIALIZING = 1;
    private static final int STORE_POPULATING = 2;
    private static final int STORE_DOWNLOADING = 3;
    private static final int STORE_OPEN = 4;
    private static final int STORE_ERROR = 5;
    private static final int PENDING_REFRESH = 1;
    private static final int PENDING_FLUSH = 2;
    private static final String DEFAULT_STORE_NAME = "localstore";
    private Context context;
    private Store store;
    private ODataMetadata metadata;
    private AtomicBoolean open = new AtomicBoolean();
    private AtomicBoolean closed = new AtomicBoolean();
    private ODataOfflineStoreListener offlineStoreListener;
    private ODataOfflineStoreRequestErrorListener offlineStoreRequestErrorListener;
    private int notifications = 0;
   // private HttpConversationManager conversationManager;
   // private ODataOfflineHttpConversation conversation;
    private String authStreamParms;
    private String authURL;
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    @Override
    public ODataRequestExecution scheduleRequest(ODataRequestParam var1, ODataRequestListener var2) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleReadEntitySet(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleReadEntity(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleReadEntity(ODataEntity var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleCreateEntity(ODataEntity var1, String var2, ODataRequestListener var3, Map<String, String> var4) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleUpdateEntity(ODataEntity var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution schedulePatchEntity(ODataEntity var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleDeleteEntity(ODataEntity var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleDeleteEntity(String var1, String var2, ODataRequestListener var3, Map<String, String> var4) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleReadPropertyPrimitive(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleReadPropertyComplex(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleReadPropertyRaw(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleReadLink(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleReadLinkSet(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataRequestExecution scheduleFunction(String var1, ODataRequestListener var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataResponse executeRequest(ODataRequestParam var1) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeReadEntitySet(String var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeReadEntity(String var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeReadEntity(ODataEntity var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeCreateEntity(ODataEntity var1, String var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeUpdateEntity(ODataEntity var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executePatchEntity(ODataEntity var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeDeleteEntity(ODataEntity var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeDeleteEntity(String var1, String var2, Map<String, String> var3) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeReadPropertyPrimitive(String var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeReadPropertyRaw(String var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeReadPropertyComplex(String var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeReadLink(String var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeReadLinkSet(String var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataResponseSingle executeFunction(String var1, Map<String, String> var2) throws ODataException {
        return null;
    }
    @Override
    public ODataEntity allocateProperties(ODataEntity var1, PropMode var2) throws ODataException {
        return null;
    }
    @Override
    public ODataMetadata getMetadata() {
        return null;
    }
    @Override
    public ODataEntity allocateNavigationProperties(ODataEntity var1) throws ODataException {
        return null;
    }
    @Override
    public ODataPayload.Type determineODataType(String var1, ODataRequestParamSingle.Mode var2) throws ODataException {
        return null;
    }
    @Override
    public String determineEntityType(String var1) throws ODataException {
        return null;
    }
    @Override
    public String determineEntitySet(String var1) throws ODataException {
        return null;
    }
}
