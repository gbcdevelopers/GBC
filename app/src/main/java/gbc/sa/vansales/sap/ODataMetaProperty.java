package gbc.sa.vansales.sap;
import java.io.Serializable;
import java.util.Set;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public interface ODataMetaProperty extends Serializable {
    String getName();

    ODataMetaProperty.EDMType getType();

    String getTypeName();

    boolean isKey();

    Set<String> getFacetNames();

    String getFacet(String var1);

    Set<AnnotationName> getAnnotationNames();

    String getAnnotation(AnnotationName var1);

    String getAnnotation(String var1, String var2);

    public static enum EDMType {
        Null("Null"),
        Binary("Edm.Binary"),
        Boolean("Edm.Boolean"),
        SByte("Edm.SByte"),
        Byte("Edm.Byte"),
        Int16("Edm.Int16"),
        Int32("Edm.Int32"),
        Int64("Edm.Int64"),
        Single("Edm.Single"),
        Double("Edm.Double"),
        Decimal("Edm.Decimal"),
        String("Edm.String"),
        Guid("Edm.Guid"),
        DateTime("Edm.DateTime"),
        Time("Edm.Time"),
        DateTimeOffset("Edm.DateTimeOffset"),
        Complex("Complex");

        String text;

        private EDMType(String text) {
            if(text == null) {
                throw new IllegalArgumentException("text is null");
            } else {
                this.text = text;
            }
        }

        public String getText() {
            return this.text;
        }

        public static ODataMetaProperty.EDMType fromString(String text) {
            if(text != null) {
                ODataMetaProperty.EDMType[] arr$ = values();
                int len$ = arr$.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    ODataMetaProperty.EDMType value = arr$[i$];
                    if(text.equalsIgnoreCase(value.getText())) {
                        return value;
                    }
                }
            }

            return null;
        }
    }
}
