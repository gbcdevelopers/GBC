package gbc.sa.vansales.sap;
import java.io.Serializable;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataMetaNavigationProperty extends Serializable {
    String getName();

    String getTargetEntityType();

    boolean isEntitySet();
}