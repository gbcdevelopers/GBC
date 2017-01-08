package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public enum ODataOfflineStoreState {
    ODataOfflineStoreOpening,
    ODataOfflineStoreInitializing,
    ODataOfflineStorePopulating,
    ODataOfflineStoreDownloading,
    ODataOfflineStoreOpen,
    ODataOfflineStoreClosed;

    private ODataOfflineStoreState() {
    }
}
