package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
class Store {
    private long handle;

    private native void jniOpen(StoreInfo var1, String var2, ODataOfflineStore var3, LodataError var4);

    private native boolean jniClose(LodataError var1);

    private native boolean jniFlushQueuedRequests(String var1, LodataError var2);

    private native void jniRefresh(String var1, String var2, LodataError var3);

    private native void jniRegisterStreamRequest(String var1, String var2, LodataError var3);

    private native void jniUnregisterStreamRequest(String var1, LodataError var2);

    private native void jniDestroy();

    Store(long var1) {
        this.handle = var1;
    }

    void Refresh(LodataError var1) {
        this.jniRefresh((String)null, (String)null, var1);
    }

    void Refresh(String var1, String var2, LodataError var3) {
        this.jniRefresh(var1, var2, var3);
    }

    void RegisterStreamRequest(String var1, String var2, LodataError var3) {
        this.jniRegisterStreamRequest(var1, var2, var3);
    }

    void UnregisterStreamRequest(String var1, LodataError var2) {
        this.jniUnregisterStreamRequest(var1, var2);
    }

    void Open(StoreInfo var1, String var2, ODataOfflineStore var3, LodataError var4) {
        this.jniOpen(var1, var2, var3, var4);
    }

    void Close(LodataError var1) {
        this.jniClose(var1);
    }

    void Destroy() {
        this.jniDestroy();
    }

    void FlushQueuedRequests(String var1, LodataError var2) {
        this.jniFlushQueuedRequests(var1, var2);
    }

    long getHandle() {
        return this.handle;
    }
}
