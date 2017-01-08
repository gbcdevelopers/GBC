package gbc.sa.vansales.sap;
import java.io.Serializable;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataNavigationProperty extends Serializable{
    boolean hasNavigationInline();

    ODataNavigationProperty.Type getNavigationType();

    Object getNavigationContent();

    void setNavigationContent(Object var1);

    String getAssociationResourcePath();

    void setAssociationResourcePath(String var1);

    public static enum Type {
        Empty,
        ResourcePath,
        Entity,
        EntitySet;

        private Type() {
        }
    }
}
