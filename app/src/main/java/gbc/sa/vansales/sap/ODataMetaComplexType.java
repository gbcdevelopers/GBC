package gbc.sa.vansales.sap;
import java.io.Serializable;
import java.util.Set;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataMetaComplexType extends Serializable {
    Set<String> getPropertyNames();

    ODataMetaProperty getProperty(String var1);
}

