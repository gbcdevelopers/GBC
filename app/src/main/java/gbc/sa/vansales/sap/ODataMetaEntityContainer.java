package gbc.sa.vansales.sap;
import java.io.Serializable;
import java.util.Set;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataMetaEntityContainer extends Serializable {
    Set<String> getFunctionImportNames();

    ODataMetaFunctionImport getFunctionImport(String var1);

    Set<String> getEntitySetNames();

    ODataMetaEntitySet getMetaEntitySet(String var1);

    Set<AnnotationName> getAnnotationNames();

    String getAnnotation(AnnotationName var1);

    String getAnnotation(String var1, String var2);
}
