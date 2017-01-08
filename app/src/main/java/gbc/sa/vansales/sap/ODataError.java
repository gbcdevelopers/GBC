package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataError extends ODataPayload {
    String getCode();

    String getMessage();
}
