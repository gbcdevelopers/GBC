package gbc.sa.vansales.sap;
import java.io.Serializable;
import java.util.Set;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataMetaFunctionParameter extends Serializable {
    String getName();

    String getTypeName();

    boolean isCollection();

    Set<String> getFacetNames();

    String getFacet(String var1);

    Set<AnnotationName> getAnnotationNames();

    String getAnnotation(AnnotationName var1);

    String getAnnotation(String var1, String var2);

    ODataMetaFunctionParameter.ParameterMode getMode();

    public static enum ParameterMode {
        IN,
        OUT,
        INOUT;

        private ParameterMode() {
        }
    }
}

