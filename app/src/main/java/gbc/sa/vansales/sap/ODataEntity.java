package gbc.sa.vansales.sap;
import java.net.URL;
import java.util.Set;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataEntity extends ODataPayload{
    String getEntityType();

    String getResourcePath();

    String getEditResourcePath();

    void setResourcePath(String var1, String var2);

    boolean isMediaEntry();

    String getMediaContentType();

    URL getMediaLink();

    URL getEditMediaLink();

    void setMediaLink(String var1, URL var2, URL var3);

    Set<String> getNavigationPropertyNames();

    ODataNavigationProperty getNavigationProperty(String var1);

    ODataNavigationProperty setNavigationProperty(String var1, ODataNavigationProperty var2);

    ODataPropMap getProperties();

    String getEtag();

    String getMediaEtag();

    void setMediaEtag(String var1);

    void setEtag(String var1);

    Set<AnnotationName> getAnnotationNames();

    String getAnnotation(AnnotationName var1);

    String getAnnotation(String var1, String var2);
}
