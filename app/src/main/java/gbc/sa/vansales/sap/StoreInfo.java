package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public class StoreInfo {
    private long handle = this.jniStoreInfo();

    private native long jniStoreInfo();

    private native void jniClose();

    private native void jniSetServiceRoot(String var1);

    private native void jniSetStoreName(String var1);

    private native void jniSetStorePath(String var1);

    private native void jniSetStoreEncryptionKey(String var1);

    private native void jniSetHost(String var1);

    private native void jniSetPort(int var1);

    private native void jniSetUrlSuffix(String var1);

    private native void jniSetExtraStreamParms(String var1);

    private native void jniSetEnableHTTPS(boolean var1);

    private native void jniSetPageSize(int var1);

    private native void jniSetEnableRepeatableRequests(boolean var1);

    private native void jniAddDefiningRequest(String var1, String var2, boolean var3, LodataError var4);

    private native void jniAddCustomHeader(String var1, String var2, LodataError var3);

    private native void jniAddCustomCookie(String var1, String var2, LodataError var3);

    private native void jniSetAppcid(String var1);

    private native void jniSetUsername(String var1);

    private native void jniSetPassword(String var1);

    private native void jniSetSso2Token(String var1);

    private native void jniSetTrustedCertificate(String var1);

    private native void jniSetIdentityFile(String var1);

    private native void jniSetIdentityPassword(String var1);

    StoreInfo() {
    }

    void close() {
        if(this.handle != 0L) {
            this.jniClose();
            this.handle = 0L;
        }

    }

    void setServiceRoot(String var1) {
        this.jniSetServiceRoot(var1);
    }

    void setStoreName(String var1) {
        this.jniSetStoreName(var1);
    }

    void setStorePath(String var1) {
        this.jniSetStorePath(var1);
    }

    void setStoreEncryptionKey(String var1) {
        this.jniSetStoreEncryptionKey(var1);
    }

    void setHost(String var1) {
        this.jniSetHost(var1);
    }

    void setPort(int var1) {
        this.jniSetPort(var1);
    }

    void setUrlSuffix(String var1) {
        this.jniSetUrlSuffix(var1);
    }

    void setExtraStreamParms(String var1) {
        this.jniSetExtraStreamParms(var1);
    }

    void setAppcid(String var1) {
        this.jniSetAppcid(var1);
    }

    void setUsername(String var1) {
        this.jniSetUsername(var1);
    }

    void setPassword(String var1) {
        this.jniSetPassword(var1);
    }

    void setSso2Token(String var1) {
        this.jniSetSso2Token(var1);
    }

    void setEnableHTTPS(boolean var1) {
        this.jniSetEnableHTTPS(var1);
    }

    void setTrustedCertificate(String var1) {
        this.jniSetTrustedCertificate(var1);
    }

    void setIdentityFile(String var1) {
        this.jniSetIdentityFile(var1);
    }

    void setIdentityPassword(String var1) {
        this.jniSetIdentityPassword(var1);
    }

    void setPageSize(int var1) {
        this.jniSetPageSize(var1);
    }

    void setEnableRepeatableRequests(boolean var1) {
        this.jniSetEnableRepeatableRequests(var1);
    }

    void addDefiningRequest(String var1, String var2, boolean var3, LodataError var4) {
        this.jniAddDefiningRequest(var1, var2, var3, var4);
    }

    void addCustomHeader(String var1, String var2, LodataError var3) {
        this.jniAddCustomHeader(var1, var2, var3);
    }

    void addCustomCookie(String var1, String var2, LodataError var3) {
        this.jniAddCustomCookie(var1, var2, var3);
    }
}
