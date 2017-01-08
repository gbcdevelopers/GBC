package gbc.sa.vansales.sap;
import java.io.Serializable;
import java.util.Set;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataMetaFunctionImport extends Serializable {
    String getName();

    String getReturnTypeName();

    boolean isReturnCollection();

    String getReturnEntitySetName();

    String getCallMethod();

    Set<String> getParameterNames();

    ODataMetaFunctionParameter getParameter(String var1);

    Set<AnnotationName> getAnnotationNames();

    String getAnnotation(AnnotationName var1);

    String getAnnotation(String var1, String var2);
}

