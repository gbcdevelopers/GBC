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
        hashMap.put("Y002", "SKU Discount");
        hashMap.put("Y007", "SKU Surcharge");
        hashMap.put("Y005", "Promotion");
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
    public static final String DriverRouteEntity = "ZSFA_DOWNLOAD_SRV.RouteFlag";
    public static final String OrderReasonEntity = "ZSFA_DOWNLOAD_SRV.OrderReason";
    public static final String OrderRejReasonEntity = "ZSFA_DOWNLOAD_SRV.OrderRejReason";
    public static final String VisitReasonEntity = "ZSFA_DOWNLOAD_SRV.VisitReason";
    public static final String PricingEntity = "ZSFA_DOWNLOAD_SRV.Pricing";
    public static final String Promotion02Entity = "ZSFA_DOWNLOAD_SRV.Promotion02";
    public static final String Promotion05Entity = "ZSFA_DOWNLOAD_SRV.Promotion05";
    public static final String Promotion07Entity = "ZSFA_DOWNLOAD_SRV.Promotion07";
    public static final String BankEntity = "ZSFA_DOWNLOAD_SRV.Bank";
    public static final String DriverOpenItemsEntity = "ZSFA_DOWNLOAD_SRV.DriverOpenItems";
    public static final String FOCEntity = "ZSFA_DOWNLOAD_SRV.FOC";

    //Functions for Posting
    public static final String LoadRequestFunction = "ORDERREQ";
    public static final String CustomerOrderRequestFunction = "ORDERREQ";
    public static final String LoadConfirmationFunction = "LCON";
    public static final String InvoiceRequestFunction = "HHTIV";
    public static final String CustomerDeliveryRequestFunction = "CUSTDEL";
    public static final String CustomerDeliveryDeleteRequestFunction = "CUSTDELDELETE";
    public static final String BeginDayFunction = "BEGINDAY";
    public static final String EndDayFunction = "ENDDAY";
    public static final String ReturnsFunction = "RETURNS";
    //public static final String UnloadFunction = "ORDERREQ";
    public static final String LoadVarianceFunction = "UNLOAD";
    public static final String VisitListFunction = "VISITACTIVITY";
    public static final String CollectionFunction = "COLLECTION";
    public static final String PartialCollectionFunction = "PARTIAL";
    public static final String DriverCollectionFunction = "COLLECTION";
    public static final String AddCustomerFunction = "ADDCUSTOMER";
    public static final String ClearingFunction = "CASHPOST";
    public static final String UnloadFunction = "UNLOAD";

    //This is never used but only for offline posting
    public static final String OdometerFunction = "ODOMETER";
    public static final String LoadConfirmationItems = "LCONITEM";
    public static final String GoodReturn = "GOODRETURN";
    public static final String BadReturn = "BADRETURN";

    //Document Types
    public static final String DocumentType = "ZVDL";
    public static final String LoadRequestDocumentType = "ZVDL";
    public static final String CustomerOrderRequestDocumentType = "ZPRE";
    public static final String DeliveryDocumentType = "ZDLV";
    public static final String InvoiceDocumentType = "ZVAN";
    public static final String GoodReturnType = "ZGRN";
    public static final String BadReturnType = "ZEXP";
    public static final String OrderRequestType = "ZPRE";
    public static final String EndingInventory = "ZVLO";
    public static final String TheftorTruckDocumentType = "ZDRB";
    public static final String ExcessDocumentType = "ZDCR";
    public static final String FreshUnload = "ZVLO";
    public static final String LoadVarianceDebit = "ZDRB";
    public static final String LoadVarianceCredit = "ZDCR";
    public static final String LoadRequestCurrentDocumentType = "ZREL";
    public static final String LoadRequestFutureDocumentType = "ZVDL";

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
    public static final String GoodReturns_PR_Type = "GOODRETURN";
    public static final String GoodReturns_PR = "06";
    public static final String BadReturns_PR_Type = "BADRETURN";
    public static final String BadReturns_PR = "07";
    public static final String TheftorTruck_PR_Type = "THEFTORTRUCK";
    public static final String TheftorTruck_PR = "08";
    public static final String Excess_PR_Type = "EXCESS";
    public static final String Excess_PR = "09";
    public static final String EndingInventory_PR_Type = "ENDINGINVENTORY";
    public static final String EndingInventory_PR = "10";
    public static final String FreshUnload_PR_Type = "FRESHUNLOAD";
    public static final String FreshUnload_PR = "11";
    public static final String CustomerDeliveryDelete_PR_Type = "CUSTOMERDELIVERYDELETE";
    public static final String CustomerDeliveryDelete_PR = "12";
    public static final String LoadVarianceDebit_PR_Type = "LOADVARIANCEDEBIT";
    public static final String LoadVarianceDebit_PR = "13";
    public static final String LoadVarianceCredit_PR_Type = "LOADVARIANCECREDIT";
    public static final String LoadVarianceCredit_PR = "14";
    public static final String Collection_PR_Type = "COLLECTION";
    public static final String Collection_PR = "15";
    public static final String Unload_PR_Type = "UNLOAD";
    public static final String Unload_PR = "16";
    //Connectivity Messages
    public static final String WIFI_CONNECTED = "Wifi Enabled";
    public static final String MOBILE_DATA_CONNECTED = "Mobile Data Enabled";
    public static final String NO_CONNECTION = "Not connected to Internet";

    //Transaction Types

    public static final String LoadRequest_TR = "LR";
    public static final String OrderRequest_TR = "OR";
    public static final String SalesInvoice_TR = "SI";
    public static final String GoodReturns_TR = "GR";
    public static final String BadReturns_TR = "BR";
    public static final String InventoryRequest_TR = "INV";
    public static final String CollectionRequest_TR = "AR";
    public static final String DriverCollectionRequest_TR = "DR";
    public static final String DeliveryRequest_TR = "DLV";
    public static final String UnloadRequest_TR = "UL";
    public static final String BeginDayRequest_TR = "BD";
    public static final String BeginDayOdometerRequest_TR = "ODOBD";
    public static final String EndDayRequest_TR = "ED";
    public static final String EndDayOdometerRequest_TR = "ODOED";
    public static final String LoadConfirmation_TR = "LCON";

    static {
        init();
    }


}
