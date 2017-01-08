package gbc.sa.vansales.sap;
import java.io.Serializable;
import java.util.Set;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataMetaEntityType extends Serializable {
    String getName();

    boolean isMediaEntity();

    Set<String> getKeyPropertyNames();

    Set<String> getPropertyNames();

    ODataMetaProperty getProperty(String var1);

    Set<String> getNavigationPropertyNames();

    ODataMetaNavigationProperty getNavigationProperty(String var1);

    Set<AnnotationName> getAnnotationNames();

    String getAnnotation(AnnotationName var1);

    String getAnnotation(String var1, String var2);
}
