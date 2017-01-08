package gbc.sa.vansales.sap;
import java.io.Serializable;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface AnnotationName extends Serializable {
    String getNamespace();

    String getName();
}
