package gbc.sa.vansales.sap;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataProperty extends ODataPayload {
    String getName();

    boolean isComplex();

    Object getValue();

    void setValue(Object var1);
}
