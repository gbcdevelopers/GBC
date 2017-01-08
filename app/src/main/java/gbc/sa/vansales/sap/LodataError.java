package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public class LodataError {
    private int code;
    private String msg;

    LodataError() {
        this.clear();
    }

    boolean isOK() {
        return this.code == 0;
    }

    int getCode() {
        return this.code;
    }

    String getReason() {
        return this.msg;
    }

    void setError(int var1, String var2) {
        this.code = var1;
        this.msg = var2;
    }

    void clear() {
        this.code = 0;
        this.msg = null;
    }

    MessageCode getMessageCode() {
        MessageCode var1 = MessageCode.getByCode(this.code);

        assert var1 != null : "Could not find message code";

        return var1;
    }
}
