package gbc.sa.vansales.sap;
import java.io.Serializable;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataPayload extends Serializable {
    public static enum Type {
        Raw,
        Property,
        Entity,
        EntitySet,
        Link,
        LinkList,
        Metadata,
        Error,
        MediaResource,
        None;

        private Type() {
        }

        public static ODataPayload.Type getTypeForPayload(ODataPayload payload) {
            ODataPayload.Type type;
            if(payload == null) {
                type = None;
            } else if(payload instanceof ODataEntity) {
                type = Entity;
            } else if(payload instanceof ODataEntitySet) {
                type = EntitySet;
            } else if(payload instanceof ODataLink) {
                type = Link;
            } else if(payload instanceof ODataLinkList) {
                type = LinkList;
            } else if(payload instanceof ODataMetadata) {
                type = Metadata;
            } else if(payload instanceof ODataProperty) {
                type = Property;
            } else if(payload instanceof ODataRawValue) {
                type = Raw;
            } else if(payload instanceof ODataError) {
                type = Error;
            }  else {
                type = Raw;
            }

            return type;
        }
    }
}
