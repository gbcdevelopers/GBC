package gbc.sa.vansales.utils;
/**
 * Created by Rakshit on 14-Dec-16.
 */
import java.util.HashMap;

public class ConfigStore {
    private static HashMap<String, String> hashMap = new HashMap<>();

    static {
        init();
    }
    public static void init() {

    }

    public static String getCode(String param) {
        return hashMap.get(param);
    }

    public static final String CHARSET = "UTF-8";

    public static final String EMPLOYEE_ID_LENGTH = "00000000";

    //Entity Types
    public static final String TripHeaderEntity = "ZSFA_DOWNLOAD_SRV.TripHd";
    public static final String LoadDeliveryEntity = "ZSFA_DOWNLOAD_SRV.LoadDeliveryHd";
    public static final String ArticleHeaderEntity = "ZSFA_DOWNLOAD_SRV.ArticleHeader";
    public static final String VisitListEntity = "ZSFA_DOWNLOAD_SRV.Visitlist";
    public static final String CustomerHeaderEntity = "ZSFA_DOWNLOAD_SRV.CustomerHeader";
    public static final String CustomerSalesAreaEntity = "ZSFA_DOWNLOAD_SRV.CustomerSalesArea";
    public static final String CustomerCreditEntity = "ZSFA_DOWNLOAD_SRV.CustomerCredit";
    public static final String CustomerOpenItemEntity = "ZSFA_DOWNLOAD_SRV.CustomerHeader";
    public static final String CustomerDeliverHeaderEntity = "ZSFA_DOWNLOAD_SRV.CustomerDelHD";
    public static final String CustomerDeliveryItemEntity = "ZSFA_DOWNLOAD_SRV.CustomerDelItm";
    public static final String MessageEntity = "ZSFA_DOWNLOAD_SRV.Messages";

    //Functions for Posting
    public static final String LoadRequestFunction = "ORDERREQ";
    public static final String CustomerOrderRequestFunction = "ORDERREQ";
    public static final String LoadConfirmationFunction = "LCON";
    public static final String InvoiceRequestFunction = "HHTIV";
    public static final String CustomerDeliveryRequestFunction = "CUSTDEL";
    public static final String BeginDayFunction = "BEGINDAY";

    //Document Types
    public static final String DocumentType = "ZVDL";
    public static final String DeliveryDocumentType = "ZVAN";

    //DocumentTypes for PR Number Generation
    public static final String LoadRequest_PR_Type = "LOAD";
    public static final String LoadRequest_PR = "01";
    public static final String OrderRequest_PR_Type = "ORDER";
    public static final String OrderRequest_PR = "02";
    public static final String InvoiceRequest_PR_Type = "INVOICE";
    public static final String InvoiceRequest_PR = "03";
    public static final String CustomerDeliveryRequest_PR_Type = "DELIVERY";
    public static final String CustomerDeliveryRequest_PR = "04";
    public static final String CustomerNew_PR_Type = "CUSTOMER";
    public static final String CustomerNew_PR = "05";
    public static final String BeginDay_PR_Type = "BEGINDAY";
    public static final String BeginDay_PR = "98";
    public static final String EndDay_PR_Type = "ENDDAY";
    public static final String EndDay_PR = "99";
    public static final String Odometer_PR_Type = "ODOMETER";
    public static final String Odometer_PR = "97";


    //Connectivity Messages
    public static final String WIFI_CONNECTED = "Wifi Enabled";
    public static final String MOBILE_DATA_CONNECTED = "Mobile Data Enabled";
    public static final String NO_CONNECTION = "Not connected to Internet";

    //Transaction Types

    public static final String LoadRequest_TR = "LR";
    public static final String OrderRequest_TR = "OR";
    public static final String SalesInvoice_TR = "SI";
    public static final String InventoryRequest_TR = "INV";
    public static final String CollectionRequest_TR = "AR";
    public static final String DeliveryRequest_TR = "DLV";
    public static final String UnloadRequest_TR = "UL";

}
