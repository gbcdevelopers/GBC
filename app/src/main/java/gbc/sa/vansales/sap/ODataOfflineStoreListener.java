package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataOfflineStoreListener {
    void offlineStoreStateChanged(ODataOfflineStore var1, ODataOfflineStoreState var2);

    void offlineStoreOpenFailed(ODataOfflineStore var1, ODataException var2);

    void offlineStoreOpenFinished(ODataOfflineStore var1);

    void offlineStoreNotification(ODataOfflineStore var1, ODataOfflineStoreNotification var2);
}
