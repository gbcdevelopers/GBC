package gbc.sa.vansales.sap;
import java.util.List;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataLinkList extends ODataPayload {
    List<ODataLink> getLinks();
}
