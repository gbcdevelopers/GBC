package gbc.sa.vansales.sap;
import java.util.Set;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataMetadata extends ODataPayload {
    Set<String> getMetaNamespaces();

    Set<String> getMetaEntityNames();

    Set<String> getMetaEntityContainerNames();

    ODataMetaEntityType getMetaEntity(String var1);

    Set<String> getMetaComplexNames();

    ODataMetaComplexType getMetaComplex(String var1);

    ODataMetaEntityContainer getMetaEntityContainer(String var1);

    String getResourcePath();

    String getLatestResourcePath();

    String getXml();
}
