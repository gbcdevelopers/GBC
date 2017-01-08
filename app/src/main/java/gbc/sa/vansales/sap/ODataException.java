package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public abstract class ODataException extends Exception {
    private static final long serialVersionUID = -6324559480782687830L;
    public final ODataException.AbstractErrorCode errorCode;

    protected ODataException(ODataException.AbstractErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    protected ODataException(ODataException.AbstractErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return this.errorCode.getMessage();
    }

    public interface AbstractErrorCode {
        String getMessage();
    }
}
