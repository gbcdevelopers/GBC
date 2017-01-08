package gbc.sa.vansales.sap;
import java.util.List;
import java.util.Set;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataEntitySet extends ODataPayload {
    int getCount();

    List<ODataEntity> getEntities();

    String getResourcePath();

    String getNextResourcePath();

    String getDeltaPath();

    Set<String> getDeletedEntities();
}
