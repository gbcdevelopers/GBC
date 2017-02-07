package gbc.sa.vansales.utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gbc.sa.vansales.App;
/**
 * Created by Rakshit on 16-Dec-16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = App.APP_DB_NAME;
    //Table Names
    public static final String LOGIN_CREDENTIALS = "LOGIN_CREDENTIALS";
    public static final String VISIT_LIST = "VISIT_LIST";
    public static final String TRIP_HEADER = "TRIP_HEADER";
    public static final String TRIP_SALES_AREA = "TRIP_SALES_AREA";
    public static final String ARTICLE_HEADER = "ARTICLE_HEADER";
    public static final String ARTICLE_UOM = "ARTICLE_UOM";
    public static final String ARTICLE_SALES_AREA = "ARTICLE_SALES_AREA";
    public static final String CUSTOMER_HEADER = "CUSTOMER_HEADER";
    public static final String CUSTOMER_SALES_AREAS = "CUSTOMER_SALES_AREAS";
    public static final String CUSTOMER_OPEN_ITEMS = "CUSTOMER_OPEN_ITEMS";
    public static final String CUSTOMER_CREDIT = "CUSTOMER_CREDIT";
    public static final String CUSTOMER_FLAGS = "CUSTOMER_FLAGS";
    public static final String DRIVER_FLAGS = "DRIVER_FLAGS";
    public static final String LOAD_DELIVERY_HEADER = "LOAD_DELIVERY";
    public static final String LOAD_DELIVERY_ITEMS = "LOAD_DELIVERY_ITEMS";
    public static final String LOAD_DELIVERY_ITEMS_POST = "LOAD_DELIVERY_ITEMS_POST";
    public static final String BEGIN_DAY = "BEGIN_DAY";
    public static final String CAPTURE_CUSTOMER_STOCK = "CUSTOMER_STOCK";
    public static final String CAPTURE_SALES_INVOICE = "SALES_INVOICE";
    public static final String LOAD_REQUEST = "LOAD_REQUEST";
    public static final String PURCHASE_NUMBER_GENERATION = "PURCHASE_NUMBER_GENERATION";
    public static final String LOCK_FLAGS = "FLAGS_FOR_USER_OPERATIONS";
    public static final String VAN_STOCK_ITEMS = "VAN_STOCK_ITEMS";
    public static final String CUSTOMER_DELIVERY_HEADER = "CUSTOMER_DELIVERY_HEADER";
    public static final String CUSTOMER_DELIVERY_ITEMS = "CUSTOMER_DELIVERY_ITEMS";
    public static final String CUSTOMER_DELIVERY_ITEMS_POST = "CUSTOMER_DELIVERY_ITEMS_POST";
    public static final String CUSTOMER_DELIVERY_ITEMS_DELETE_POST = "CUSTOMER_DELIVERY_ITEMS_DELETE_POST";
    public static final String ORDER_REQUEST = "ORDER_REQUEST";
    public static final String MESSAGES = "MESSAGES";
    public static final String ODOMETER = "ODOMETER";
    public static final String REASONS = "REASONS";
    public static final String PROMOTIONS = "PROMOTIONS";
    public static final String PRICING = "PRICING";
    public static final String RETURNS = "RETURNS";
    public static final String UNLOAD_VARIANCE = "UNLOAD_VARIANCE";
    public static final String COLLECTION = "COLLECTION";
    public static final String BANKS = "BANKS";
    public static final String DAYACTIVITY = "DAYACTIVITY";
    public static final String LOAD_VARIANCE_ITEMS_POST = "LOAD_VARIANCE_ITEMS_POST";
    public static final String FOC_RULES = "FOC_RULES";
    public static final String FOC_INVOICE = "FOC_SALE";
    public static final String LOAD_CONFIRMATION_HEADER = "LOAD_CONFIRMATION_HEADER";
    public static final String VISIT_LIST_POST = "VISIT_LIST_POST";
    public static final String VISIT_LIST_ID_GENERATE = "VISIT_LIST_ID_GENERATE";
    //Properties for Table(Based on Entity Sets)
    //UserAuthenticationSet
    public static final String KEY_ID = "_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_IV = "iv";
    public static final String KEY_SYM = "symKey";
    //VisitListSet
    public static final String KEY_TRIP_ID = "tripId";
    public static final String KEY_VISITLISTID = "visitListId";
    public static final String KEY_ITEMNO = "itemNo";
    public static final String KEY_CUSTOMER_NO = "customerNo";
    public static final String KEY_EXEC_DATE = "execDate";
    public static final String KEY_DRIVER = "driver1";
    public static final String KEY_VP_TYPE = "vpType";
    //TripHeaderSet
    public static final String KEY_ROUTE = "route";
    public static final String KEY_TRUCK = "truck";
    public static final String KEY_PS_DATE = "psDate";
    public static final String KEY_AS_DATE = "asDate";
    public static final String KEY_TOUR_TYPE = "tourType";
    public static final String KEY_CREATED_TIME = "createdTime";
    public static final String KEY_CREATED_BY = "createdBy";
    public static final String KEY_SETTLED_BY = "settledBy";
    public static final String KEY_DOWN_STATUS = "downloadStatus";
    public static final String KEY_UP_STATUS = "uploadStatus";
    public static final String KEY_LOADS = "loads";
    //Trip Sales Area Collection
    public static final String KEY_DATE = "date";
    public static final String KEY_START_DATE = "startDate";
    public static final String KEY_START_TIME = "startTime";
    public static final String KEY_DIST_CHANNEL = "distChannel";
    public static final String KEY_DIVISION = "division";
    //ArticleHeaderSet
    public static final String KEY_MATERIAL_GROUPA_DESC = "materialGroupADesc";
    public static final String KEY_MATERIAL_GROUPB_DESC = "materialGroupBDesc";
    public static final String KEY_MATERIAL_DESC2 = "materialDesc2";
    public static final String KEY_BATCH_MANAGEMENT = "batchManagement";
    public static final String KEY_VOLUME_UOM = "volumeUOM";
    public static final String KEY_VOLUME = "volume";
    public static final String KEY_WEIGHT_UOM = "weightUOM";
    public static final String KEY_NET_WEIGHT = "netWeight";
    public static final String KEY_GROSS_WEIGHT = "grossWeight";
    public static final String KEY_ARTICLE_CATEGORY = "articleCategory";
    public static final String KEY_ARTICLE_NO = "articleNo";
    public static final String KEY_BASE_UOM = "baseUOM";
    public static final String KEY_MATERIAL_GROUP = "materialGroup";
    public static final String KEY_MATERIAL_TYPE = "materialType";
    public static final String KEY_MATERIAL_DESC1 = "materialDesc1";
    public static final String KEY_MATERIAL_NO = "materialNo";
    //Article UOM Collection
    public static final String KEY_UOM = "uom";
    public static final String KEY_NUMERATOR = "numerator";
    public static final String KEY_DENOMINATOR = "denominator";
    //Article Sales Area Collection
    public static final String KEY_PRICE_REF_MAT = "priceRefMat";
    public static final String KEY_SALES_UOM = "salesUOM";
    public static final String KEY_MATERIAL_PURCHASE_GROUP = "materialPurGroup";
    public static final String KEY_PRODUCT_HIERARCHY = "prodHierarchy";
    public static final String KEY_MINIMUM_ORDER_QTY = "minOrderQty";
    public static final String KEY_SALES_STATUS = "salesStatus";
    public static final String KEY_EMPTY_R_BLOCK = "emptyRBlock";
    public static final String KEY_EMPTY_GROUP = "emptyGroup";
    public static final String KEY_SKT_OF = "sktOf";
    public static final String KEY_SALES_ORG = "salesOrg";
    //Customer Header Set
    public static final String KEY_ORDER_BLOCK = "orderBlock";
    public static final String KEY_INVOICE_BLOCK = "invoiceBlock";
    public static final String KEY_DELIVERY_BLOCK = "deliveryBlock";
    public static final String KEY_ROOM_NO = "roomNo";
    public static final String KEY_FLOOR = "floor";
    public static final String KEY_BUILDING = "building";
    public static final String KEY_HOME_CITY = "homeCity";
    public static final String KEY_STREET5 = "street5";
    public static final String KEY_STREET4 = "street4";
    public static final String KEY_STREET3 = "street3";
    public static final String KEY_STREET2 = "street2";
    public static final String KEY_NAME4 = "name4";
    public static final String KEY_COUNTRY_CODE = "countryCode";
    public static final String KEY_NAME3 = "name3";
    public static final String KEY_NAME1 = "name1";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_STREET = "street";
    public static final String KEY_NAME2 = "name2";
    public static final String KEY_CITY = "city";
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_REGION = "region";
    public static final String KEY_SITE_CODE = "siteCode";
    public static final String KEY_POST_CODE = "postCode";
    public static final String KEY_PHONE_NO = "phoneNo";
    public static final String KEY_COMPANY_CODE = "companyCode";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_TERMS = "terms";
    public static final String KEY_TERMS_DESCRIPTION = "termsDescription";
    //Customer Sales Area
    public static final String KEY_SOLD_TO_NO = "soldTo";
    public static final String KEY_BILL_TO_NO = "billTo";
    public static final String KEY_SHIP_TO_NO = "shipTo";
    public static final String KEY_PAYER_NO = "payer";
    public static final String KEY_SALES_NO = "salesNo";
    public static final String KEY_CUSTOMER_GROUP1 = "customerGroup1";
    public static final String KEY_PAYCODE = "payCode";
    public static final String KEY_CUSTOMER_GROUP2 = "customerGroup2";
    public static final String KEY_CUSTOMER_GROUP3 = "customerGroup3";
    public static final String KEY_CUSTOMER_GROUP4 = "customerGroup4";
    public static final String KEY_CUSTOMER_GROUP5 = "customerGroup5";
    //Customer Credit Collection
    public static final String KEY_CREDIT_CONTROL_AREA = "creditControlArea";
    public static final String KEY_CREDIT_LIMIT = "creditLimit";
    public static final String KEY_AVAILABLE_LIMIT = "availableLimit";
    public static final String KEY_SPECIAL_LIABILITIES = "specialLiability";
    public static final String KEY_RECEIVABLES = "receivables";
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_RISK_CAT = "riskCategory";
    //Load Delivery Header
    public static final String KEY_DELIVERY_NO = "deliveryNo";
    public static final String KEY_ENTRY_TIME = "entryTime";
    public static final String KEY_SALES_DIST = "salesDist";
    public static final String KEY_SHIPPING_PT = "shippingPoint";
    public static final String KEY_DELIVERY_TYPE = "deliveryType";
    public static final String KEY_DELIVERY_DEFN = "deliveryDefinition";
    public static final String KEY_ORDER_COMB = "orderComb";
    public static final String KEY_GOODS_MOVEMENT_DATE = "goodMovementDate";
    public static final String KEY_LOADING_DATE = "loadingDate";
    public static final String KEY_TRANSPLANT_DATE = "transplantDate";
    public static final String KEY_DELIVERY_DATE = "deliveryDate";
    public static final String KEY_PICKING_DATE = "pickingDate";
    public static final String KEY_UNLOAD_POINT = "unloadPoint";
    //Load Items
    public static final String KEY_ITEM_NO = "itemNo";
    public static final String KEY_ITEM_CATEGORY = "itemCategory";
    public static final String KEY_MATERIAL_ENTERED = "materialEntered";
    public static final String KEY_PLANT = "plant";
    public static final String KEY_STORAGE_LOCATION = "storage";
    public static final String KEY_BATCH = "batch";
    public static final String KEY_ACTUAL_QTY = "actualQty";
    public static final String KEY_REMAINING_QTY = "remainingQty";
    //Capture Customer Stock
    public static final String KEY_AMOUNT = "amount";
    //For Posting
    public static final String KEY_ORG_CASE = "orgCase";
    public static final String KEY_ORG_UNITS = "orgUnits";
    public static final String KEY_VAR_CASE = "varCase";
    public static final String KEY_VAR_UNITS = "varUnits";
    public static final String KEY_IS_VERIFIED = "isVerified";
    public static final String KEY_IS_DELIVERED = "isDelivered";
    public static final String KEY_IS_SELECTED = "isSelected";
    public static final String KEY_TIME_STAMP = "timeStamp";
    //Load Request
    public static final String KEY_CASE = "cases";
    public static final String KEY_UNIT = "units";
    public static final String KEY_PRICE = "price";
    public static final String KEY_IS_POSTED = "isPosted";
    public static final String KEY_IS_PRINTED = "isPrinted";
    //Order Request
    public static final String KEY_ORDER_ID = "order_id";
    //Generating Sequential Number for Purchase Number
    public static final String KEY_DOC_TYPE = "documentType";
    public static final String KEY_PURCHASE_NUMBER = "purchaseNumber";
    //User Flags
    public static final String KEY_IS_BEGIN_DAY = "isBeginDay";
    public static final String KEY_IS_LOAD_VERIFIED = "isLoadVerified";
    public static final String KEY_IS_UNLOAD = "isUnload";
    public static final String KEY_IS_END_DAY = "isEndDay";
    //Messages
    public static final String KEY_STRUCTURE = "structure";
    public static final String KEY_MESSAGE = "message";
    //Vanstock Table
    public static final String KEY_RESERVED_QTY = "reservedQty";
    public static final String KEY_ACTUAL_QTY_CASE = "actualQtyCase";
    public static final String KEY_ACTUAL_QTY_UNIT = "actualQtyUnit";
    public static final String KEY_RESERVED_QTY_CASE = "reservedQtyCase";
    public static final String KEY_RESERVED_QTY_UNIT = "reservedQtyUnit";
    public static final String KEY_REMAINING_QTY_CASE = "remainingQtyCase";
    public static final String KEY_REMAINING_QTY_UNIT = "remainingQtyUnit";
    public static final String KEY_UOM_CASE = "uomCase";
    public static final String KEY_UOM_UNIT = "uomUnit";
    //Odometer
    public static final String KEY_ODOMETER_VALUE = "odometerValue";

    //Customer Activities
    public static final String KEY_IS_ORDER_CAPTURED = "isOrderCaptured";
    public static final String KEY_IS_DELIVERY_CAPTURED = "isDeliveryCaptured";
    public static final String KEY_IS_SALES_CAPTURED = "isSalesCaptured";
    public static final String KEY_IS_COLLECTION_CAPTURED = "isCollectionCaptured";
    public static final String KEY_IS_MERCHANDIZE_CAPTURED = "isMerchandizeCaptured";
    public static final String KEY_IS_ORDER_POSTED = "isOrderPosted";
    public static final String KEY_IS_DELIVERY_POSTED = "isDeliveryPosted";
    public static final String KEY_IS_SALES_POSTED = "isSalesPosted";
    public static final String KEY_IS_COLLECTION_POSTED = "isCollectionPosted";
    public static final String KEY_IS_MERCHANDIZE_POSTED = "isMerchandizePosted";
    public static final String KEY_VISIT_SERVICED_REASON = "visitServicedReason";
    public static final String KEY_VISIT_UNSERVICED_REASON = "visitUnServicedReason";
    public static final String KEY_CUSTOMER_IN_TIMESTAMP = "customerInTimestamp";
    public static final String KEY_CUSTOMER_OUT_TIMESTAMP = "customerOutTimestamp";
    public static final String KEY_IS_VISITED = "isVisited";
    public static final String KEY_IS_NEW_CUSTOMER = "isNewCustomer";
    public static final String KEY_FUNCTION = "function";

    //Customer Flags
    public static final String KEY_THRESHOLD_LIMIT = "thresholdLimit";
    public static final String KEY_VERIFYGPS = "verifyGPS";
    public static final String KEY_GPS_SAVE = "gpsSave";
    public static final String KEY_ENABLE_INVOICE = "enableInvoice";
    public static final String KEY_ENABLE_DELAY_PRINT = "enableDelayPrint";
    public static final String KEY_ENABLE_EDIT_ORDERS = "enableEditOrders";
    public static final String KEY_ENABLE_EDIT_INVOICE = "enableEditInvoice";
    public static final String KEY_ENABLE_RETURNS = "enableReturns";
    public static final String KEY_ENABLE_DAMAGED = "enableDamaged";
    public static final String KEY_ENABLE_SIGN_CAPTURE = "enableSignCapture";
    public static final String KEY_ENABLE_RETURN = "enableReturn";
    public static final String KEY_ENABLE_AR_COLLECTION = "enableARCollection";
    public static final String KEY_ENABLE_POS_EQUI = "enablePOSEqui";
    public static final String KEY_ENABLE_SUR_AUDIT = "enableAudit";

    //Driver Flags
    public static final String KEY_ROUTE_TYPE = "routeType";
    public static final String KEY_PROMPT_ODOMETER = "promptOdometer";
    public static final String KEY_EOD_SALES_REPORT = "eodSalesReport";
    public static final String KEY_ENABLE_PVOID = "enablePVoid";
    public static final String KEY_ENABLE_NO_SALE = "enableSale";
    public static final String KEY_ENABLE_ADD_CUSTOMER = "enableAddCustomer";
    public static final String KEY_DEFAULT_DELIVERY_DAYS = "defaultDeliveryDays";
    public static final String KEY_PASSWORD1 = "password1";
    public static final String KEY_PASSWORD2 = "password2";
    public static final String KEY_PASSWORD3 = "password3";
    public static final String KEY_PASSWORD4 = "password4";
    public static final String KEY_PASSWORD5 = "password5";
    public static final String KEY_DATE_TIME_CHANGE = "dateTimeChange";
    public static final String KEY_PRICE_CHANGE = "priceChange";
    public static final String KEY_PROMO_OVERRIDE = "promoOverride";
    public static final String KEY_ROUTE_SETUP = "routeSetup";
    public static final String KEY_VIEW_STOCK = "viewStock";
    public static final String KEY_LOAD_SECURITY_GUARD = "loadSecurityGuard";
    public static final String KEY_START_OF_DAY = "startOfDay";
    public static final String KEY_SETTLEMENT = "settlement";
    public static final String KEY_PRINT_EOD = "printEOD";
    public static final String KEY_LOAD_ADJUST = "loadAdjust";
    public static final String KEY_ENFORCE_CALL_SEQ = "enforceCallSequence";
    public static final String KEY_DISPLAY_IV_SUMMARY = "displayInvoiceSummary";
    public static final String KEY_ALLOW_RADIUS = "allowRadius";
    public static final String KEY_ENABLE_GPS = "enableGPS";

    //Reasons
    public static final String KEY_REASON_TYPE = "reasonType";
    public static final String KEY_REASON_CODE = "reasonCode";
    public static final String KEY_REASON_DESCRIPTION = "reasonDescription";
    public static final String KEY_REASON_DESCRIPTION_AR = "reasonDescriptionAr";

    //Banks
    public static final String KEY_BANK_CODE = "bankCode";
    public static final String KEY_BANK_NAME = "bankName";

    //Promotions
    public static final String KEY_PROMOTION_TYPE = "promotionType";

    //Pricing
    public static final String KEY_PRIORITY = "priority";

    //Returns
    public static final String KEY_RETURN_TYPE = "returnType";

    //Unload
    public static final String KEY_VARIANCE_TYPE = "varianceType";

    //Collection
    public static final String KEY_INVOICE_NO = "invoiceNo";
    public static final String KEY_DUE_DATE = "dueDate";
    public static final String KEY_INVOICE_DATE = "invoiceDate";
    public static final String KEY_INVOICE_AMOUNT = "invoiceAmount";
    public static final String KEY_AMOUNT_CLEARED = "amountCleared";
    public static final String KEY_CASH_AMOUNT = "cashAmount";
    public static final String KEY_CHEQUE_AMOUNT = "chequeAmount";
    public static final String KEY_CHEQUE_NUMBER = "chequeNumber";
    public static final String KEY_CHEQUE_DATE = "chequeDate";
    public static final String KEY_CHEQUE_BANK_NAME = "bankName";
    public static final String KEY_CHEQUE_BANK_CODE = "bankCode";
    public static final String KEY_COLLECTION_TYPE = "collectionType";
    public static final String KEY_IS_INVOICE_COMPLETE = "isInvoiceComplete";
    public static final String KEY_SAP_INVOICE_NO = "sapInvoiceNo";
    public static final String KEY_INVOICE_DAYS = "invoiceDays";
    public static final String KEY_CUSTOMER_TYPE = "customerType";

    //Day Activity
    public static final String KEY_ACTIVITY_TYPE = "activityType";

    //Load Variance
    public static final String KEY_DOCUMENT_TYPE = "documentType";
    public static final String KEY_CASE_DIFF = "caseDiff";
    public static final String KEY_UNIT_DIFF = "unitDiff";

    //FOC Rules
    public static final String KEY_FOC_QUALIFYING_ITEM = "qualifyingItem";
    public static final String KEY_FOC_ASSIGNING_ITEM = "assigningItem";
    public static final String KEY_FOC_QUALIFYING_QUANTITY = "qualifyingQuantity";
    public static final String KEY_FOC_ASSIGNING_QUANTITY = "assigningQuantity";
    public static final String KEY_FOC_DATE_FROM = "dateFrom";
    public static final String KEY_FOC_DATE_TO = "dateTo";

    //Visit List
    public static final String KEY_START_TIMESTAMP = "startTimestamp";
    public static final String KEY_END_TIMESTAMP = "endTimestamp";
    public static final String KEY_ACTIVITY_ID = "activityId";

    private static DatabaseHandler sInstance;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL Queries to create tables
        String TABLE_LOGIN_CREDENTIALS = "CREATE TABLE " + LOGIN_CREDENTIALS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_SYM + " TEXT,"
                + KEY_IV + " TEXT,"
                + KEY_DATE + " TEXT " + ")";
        String TABLE_VISIT_LIST = "CREATE TABLE " + VISIT_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_IN_TIMESTAMP + " TEXT,"
                + KEY_CUSTOMER_OUT_TIMESTAMP + " TEXT,"
                + KEY_VISITLISTID + " TEXT,"
                + KEY_ITEMNO + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_EXEC_DATE + " TEXT,"
                + KEY_DRIVER + " TEXT,"
                + KEY_IS_ORDER_CAPTURED + " TEXT,"
                + KEY_IS_DELIVERY_CAPTURED + " TEXT,"
                + KEY_IS_SALES_CAPTURED + " TEXT,"
                + KEY_IS_COLLECTION_CAPTURED + " TEXT,"
                + KEY_IS_MERCHANDIZE_CAPTURED + " TEXT,"
                + KEY_IS_ORDER_POSTED + " TEXT,"
                + KEY_IS_DELIVERY_POSTED + " TEXT,"
                + KEY_IS_SALES_POSTED + " TEXT,"
                + KEY_IS_COLLECTION_POSTED + " TEXT,"
                + KEY_IS_MERCHANDIZE_POSTED + " TEXT,"
                + KEY_VISIT_SERVICED_REASON + " TEXT,"
                + KEY_VISIT_UNSERVICED_REASON + " TEXT,"
                + KEY_IS_VISITED + " TEXT,"
                + KEY_IS_NEW_CUSTOMER + " TEXT,"
                + KEY_VP_TYPE + " TEXT " + ")";
        String TABLE_TRIP_HEADER = "CREATE TABLE " + TRIP_HEADER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_VISITLISTID + " TEXT,"
                + KEY_ROUTE + " TEXT,"
                + KEY_DRIVER + " TEXT,"
                + KEY_TRUCK + " TEXT,"
                + KEY_PS_DATE + " TEXT,"
                + KEY_AS_DATE + " TEXT,"
                + KEY_TOUR_TYPE + " TEXT,"
                + KEY_CREATED_TIME + " TEXT,"
                + KEY_CREATED_BY + " TEXT,"
                + KEY_SETTLED_BY + " TEXT,"
                + KEY_DOWN_STATUS + " TEXT,"
                + KEY_UP_STATUS + " TEXT,"
                + KEY_LOADS + " TEXT " + ")";
        String TABLE_TRIP_SALES_AREA = "CREATE TABLE " + TRIP_SALES_AREA + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_VISITLISTID + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_START_DATE + " TEXT,"
                + KEY_START_TIME + " TEXT,"
                + KEY_SALES_ORG + " TEXT,"
                + KEY_DIST_CHANNEL + " TEXT,"
                + KEY_DIVISION + " TEXT " + ")";
        String TABLE_ARTICLE_HEADER = "CREATE TABLE " + ARTICLE_HEADER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_MATERIAL_GROUPA_DESC + " TEXT,"
                + KEY_MATERIAL_GROUPB_DESC + " TEXT,"
                + KEY_MATERIAL_DESC2 + " TEXT,"
                + KEY_BATCH_MANAGEMENT + " TEXT,"
                + KEY_PRODUCT_HIERARCHY + " TEXT,"
                + KEY_VOLUME_UOM + " TEXT,"
                + KEY_VOLUME + " TEXT,"
                + KEY_WEIGHT_UOM + " TEXT,"
                + KEY_NET_WEIGHT + " TEXT,"
                + KEY_GROSS_WEIGHT + " TEXT,"
                + KEY_ARTICLE_CATEGORY + " TEXT,"
                + KEY_ARTICLE_NO + " TEXT,"
                + KEY_BASE_UOM + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_MATERIAL_TYPE + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT " + ")";
        String TABLE_ARTICLE_UOM = "CREATE TABLE " + ARTICLE_UOM + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_NUMERATOR + " TEXT,"
                + KEY_DENOMINATOR + " TEXT,"
                + KEY_ARTICLE_NO + " TEXT,"
                + KEY_ARTICLE_CATEGORY + " TEXT " + ")";
        String TABLE_ARTICLE_SALES_AREA = "CREATE TABLE " + ARTICLE_SALES_AREA + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_PRICE_REF_MAT + " TEXT,"
                + KEY_SALES_UOM + " TEXT,"
                + KEY_MATERIAL_PURCHASE_GROUP + " TEXT,"
                + KEY_PRODUCT_HIERARCHY + " TEXT,"
                + KEY_MINIMUM_ORDER_QTY + " TEXT,"
                + KEY_SALES_STATUS + " TEXT,"
                + KEY_EMPTY_R_BLOCK + " TEXT,"
                + KEY_EMPTY_GROUP + " TEXT,"
                + KEY_SKT_OF + " TEXT,"
                + KEY_DIST_CHANNEL + " TEXT,"
                + KEY_SALES_ORG + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT " + ")";
        String TABLE_CUSTOMER_HEADER = "CREATE TABLE " + CUSTOMER_HEADER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_ORDER_BLOCK + " TEXT,"
                + KEY_INVOICE_BLOCK + " TEXT,"
                + KEY_DELIVERY_BLOCK + " TEXT,"
                + KEY_ROOM_NO + " TEXT,"
                + KEY_FLOOR + " TEXT,"
                + KEY_BUILDING + " TEXT,"
                + KEY_HOME_CITY + " TEXT,"
                + KEY_STREET5 + " TEXT,"
                + KEY_STREET4 + " TEXT,"
                + KEY_STREET3 + " TEXT,"
                + KEY_STREET2 + " TEXT,"
                + KEY_NAME4 + " TEXT,"
                + KEY_DRIVER + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_COUNTRY_CODE + " TEXT,"
                + KEY_NAME3 + " TEXT,"
                + KEY_NAME1 + " TEXT,"
                + KEY_ADDRESS + " TEXT,"
                + KEY_STREET + " TEXT,"
                + KEY_NAME2 + " TEXT,"
                + KEY_CITY + " TEXT,"
                + KEY_DISTRICT + " TEXT,"
                + KEY_REGION + " TEXT,"
                + KEY_SITE_CODE + " TEXT,"
                + KEY_POST_CODE + " TEXT,"
                + KEY_PHONE_NO + " TEXT,"
                + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT,"
                + KEY_TERMS + " TEXT,"
                + KEY_TERMS_DESCRIPTION + " TEXT,"
                + KEY_COMPANY_CODE + " TEXT " + ")";
        String TABLE_CUSTOMER_SALES_AREAS = "CREATE TABLE " + CUSTOMER_SALES_AREAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_DIVISION + " TEXT,"
                + KEY_DIST_CHANNEL + " TEXT,"
                + KEY_SALES_ORG + " TEXT,"
                + KEY_DRIVER + " TEXT,"
                + KEY_SOLD_TO_NO + " TEXT,"
                + KEY_BILL_TO_NO + " TEXT,"
                + KEY_SHIP_TO_NO + " TEXT,"
                + KEY_PAYER_NO + " TEXT,"
                + KEY_SALES_NO + " TEXT,"
                + KEY_CUSTOMER_GROUP1 + " TEXT,"
                + KEY_PAYCODE + " TEXT,"
                + KEY_CUSTOMER_GROUP2 + " TEXT,"
                + KEY_CUSTOMER_GROUP3 + " TEXT,"
                + KEY_CUSTOMER_GROUP4 + " TEXT,"
                + KEY_CUSTOMER_GROUP5 + " TEXT " + ")";
        String TABLE_CUSTOMER_CREDIT = "CREATE TABLE " + CUSTOMER_CREDIT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_CREDIT_CONTROL_AREA + " TEXT,"
                + KEY_CREDIT_LIMIT + " TEXT,"
                + KEY_AVAILABLE_LIMIT + " TEXT,"
                + KEY_SPECIAL_LIABILITIES + " TEXT,"
                + KEY_RECEIVABLES + " TEXT,"
                + KEY_CURRENCY + " TEXT,"
                + KEY_RISK_CAT + " TEXT " + ")";
        String TABLE_LOAD_DELIVERY_HEADER = "CREATE TABLE " + LOAD_DELIVERY_HEADER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_DELIVERY_NO + " TEXT,"
                + KEY_CREATED_BY + " TEXT,"
                + KEY_CREATED_TIME + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_SALES_DIST + " TEXT,"
                + KEY_SHIPPING_PT + " TEXT,"
                + KEY_SALES_ORG + " TEXT,"
                + KEY_DELIVERY_TYPE + " TEXT,"
                + KEY_DELIVERY_DEFN + " TEXT,"
                + KEY_ORDER_COMB + " TEXT,"
                + KEY_GOODS_MOVEMENT_DATE + " TEXT,"
                + KEY_LOADING_DATE + " TEXT,"
                + KEY_TRANSPLANT_DATE + " TEXT,"
                + KEY_DELIVERY_DATE + " TEXT,"
                + KEY_PICKING_DATE + " TEXT,"
                + KEY_UNLOAD_POINT + " TEXT,"
                + KEY_IS_VERIFIED + " TEXT " + ")";
        String TABLE_LOAD_DELIVERY_ITEMS = "CREATE TABLE " + LOAD_DELIVERY_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DELIVERY_NO + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_ITEM_CATEGORY + " TEXT,"
                + KEY_CREATED_BY + " TEXT,"
                + KEY_ENTRY_TIME + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_ENTERED + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_PLANT + " TEXT,"
                + KEY_STORAGE_LOCATION + " TEXT,"
                + KEY_BATCH + " TEXT,"
                + KEY_ACTUAL_QTY + " TEXT,"
                + KEY_REMAINING_QTY + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_DIST_CHANNEL + " TEXT,"
                + KEY_DIVISION + " TEXT,"
                + KEY_IS_VERIFIED + " TEXT " + ")";
        String TABLE_CUSTOMER_DELIVERY_HEADER = "CREATE TABLE " + CUSTOMER_DELIVERY_HEADER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_DELIVERY_NO + " TEXT,"
                + KEY_CREATED_BY + " TEXT,"
                + KEY_CREATED_TIME + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_SALES_DIST + " TEXT,"
                + KEY_SHIPPING_PT + " TEXT,"
                + KEY_SALES_ORG + " TEXT,"
                + KEY_DELIVERY_TYPE + " TEXT,"
                + KEY_DELIVERY_DEFN + " TEXT,"
                + KEY_ORDER_COMB + " TEXT,"
                + KEY_GOODS_MOVEMENT_DATE + " TEXT,"
                + KEY_LOADING_DATE + " TEXT,"
                + KEY_TRANSPLANT_DATE + " TEXT,"
                + KEY_DELIVERY_DATE + " TEXT,"
                + KEY_PICKING_DATE + " TEXT,"
                + KEY_UNLOAD_POINT + " TEXT,"
                + KEY_IS_DELIVERED + " TEXT " + ")";
        String TABLE_CUSTOMER_DELIVERY_ITEMS = "CREATE TABLE " + CUSTOMER_DELIVERY_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DELIVERY_NO + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_ITEM_CATEGORY + " TEXT,"
                + KEY_CREATED_BY + " TEXT,"
                + KEY_ENTRY_TIME + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_ENTERED + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_PLANT + " TEXT,"
                + KEY_STORAGE_LOCATION + " TEXT,"
                + KEY_BATCH + " TEXT,"
                + KEY_ACTUAL_QTY + " TEXT,"
                + KEY_REMAINING_QTY + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_DIST_CHANNEL + " TEXT,"
                + KEY_DIVISION + " TEXT,"
                + KEY_IS_DELIVERED + " TEXT " + ")";
        String TABLE_VAN_STOCK_ITEMS = "CREATE TABLE " + VAN_STOCK_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DELIVERY_NO + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_ITEM_CATEGORY + " TEXT,"
                + KEY_CREATED_BY + " TEXT,"
                + KEY_ENTRY_TIME + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_MATERIAL_ENTERED + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_PLANT + " TEXT,"
                + KEY_STORAGE_LOCATION + " TEXT,"
                + KEY_BATCH + " TEXT,"
                + KEY_ACTUAL_QTY_CASE + " TEXT,"
                + KEY_ACTUAL_QTY_UNIT + " TEXT,"
                + KEY_RESERVED_QTY_CASE + " TEXT,"
                + KEY_RESERVED_QTY_UNIT + " TEXT,"
                + KEY_REMAINING_QTY_CASE + " TEXT,"
                + KEY_REMAINING_QTY_UNIT + " TEXT,"
                + KEY_UOM_CASE + " TEXT,"
                + KEY_UOM_UNIT + " TEXT,"
                + KEY_DIST_CHANNEL + " TEXT,"
                + KEY_DIVISION + " TEXT,"
                + KEY_IS_VERIFIED + " TEXT " + ")";
        String TABLE_LOAD_DELIVERY_ITEMS_POST = "CREATE TABLE " + LOAD_DELIVERY_ITEMS_POST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ENTRY_TIME + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_DELIVERY_NO + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_ORG_CASE + " TEXT,"
                + KEY_ORG_UNITS + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_VAR_CASE + " TEXT,"
                + KEY_VAR_UNITS + " TEXT " + ")";
        String TABLE_CUSTOMER_DELIVERY_ITEMS_POST = "CREATE TABLE " + CUSTOMER_DELIVERY_ITEMS_POST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_DELIVERY_NO + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_CASE + " TEXT,"
                + KEY_UNIT + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";
        String TABLE_CUSTOMER_DELIVERY_ITEMS_DELETE_POST = "CREATE TABLE " + CUSTOMER_DELIVERY_ITEMS_DELETE_POST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_DELIVERY_NO + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_CASE + " TEXT,"
                + KEY_UNIT + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_REASON_CODE + " TEXT,"
                + KEY_REASON_DESCRIPTION + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";
        String TABLE_BEGIN_DAY = "CREATE TABLE " + BEGIN_DAY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_FUNCTION + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_SELECTED + " TEXT " + ")";
        String TABLE_CAPTURE_CUSTOMER_STOCK = "CREATE TABLE " + CAPTURE_CUSTOMER_STOCK + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ENTRY_TIME + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_ITEM_CATEGORY + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_ORG_CASE + " TEXT,"
                + KEY_ORG_UNITS + " TEXT,"
                + KEY_AMOUNT + " TEXT " + ")";
        String TABLE_CAPTURE_SALES_INVOICE = "CREATE TABLE " + CAPTURE_SALES_INVOICE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_ITEM_CATEGORY + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_ORG_CASE + " TEXT,"
                + KEY_ORG_UNITS + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";
        String TABLE_LOAD_REQUEST = "CREATE TABLE " + LOAD_REQUEST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_CASE + " TEXT,"
                + KEY_UNIT + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";
        String TABLE_ORDER_REQUEST = "CREATE TABLE " + ORDER_REQUEST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_CASE + " TEXT,"
                + KEY_UNIT + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";
        String TABLE_GENERATE_PR_NUMBER = "CREATE TABLE " + PURCHASE_NUMBER_GENERATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_ROUTE + " TEXT,"
                + KEY_DOC_TYPE + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT " + ")";
        String TABLE_USER_FLAGS = "CREATE TABLE " + LOCK_FLAGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_IS_BEGIN_DAY + " TEXT,"
                + KEY_IS_LOAD_VERIFIED + " TEXT,"
                + KEY_IS_UNLOAD + " TEXT,"
                + KEY_IS_END_DAY + " TEXT " + ")";
        String TABLE_MESSAGES = "CREATE TABLE " + MESSAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_STRUCTURE + " TEXT,"
                + KEY_MESSAGE + " TEXT,"
                + KEY_DRIVER + " TEXT " + ")";
        String TABLE_ODOMETER_VALUE = "CREATE TABLE " + ODOMETER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_ODOMETER_VALUE + " TEXT " + ")";

        String TABLE_CUSTOMER_FLAGS = "CREATE TABLE " + CUSTOMER_FLAGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_THRESHOLD_LIMIT + " TEXT,"
                + KEY_VERIFYGPS + " TEXT,"
                + KEY_GPS_SAVE + " TEXT,"
                + KEY_ENABLE_INVOICE + " TEXT,"
                + KEY_ENABLE_DELAY_PRINT + " TEXT,"
                + KEY_ENABLE_EDIT_ORDERS + " TEXT,"
                + KEY_ENABLE_EDIT_INVOICE + " TEXT,"
                + KEY_ENABLE_RETURNS + " TEXT,"
                + KEY_ENABLE_DAMAGED + " TEXT,"
                + KEY_ENABLE_SIGN_CAPTURE + " TEXT,"
                + KEY_ENABLE_RETURN + " TEXT,"
                + KEY_ENABLE_AR_COLLECTION + " TEXT,"
                + KEY_ENABLE_POS_EQUI + " TEXT,"
                + KEY_ENABLE_SUR_AUDIT + " TEXT " + ")";

        String TABLE_DRIVER_FLAGS = "CREATE TABLE " + DRIVER_FLAGS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_DRIVER + " TEXT,"
                + KEY_ROUTE_TYPE + " TEXT,"
                + KEY_PROMPT_ODOMETER + " TEXT,"
                + KEY_EOD_SALES_REPORT + " TEXT,"
                + KEY_ENABLE_PVOID + " TEXT,"
                + KEY_ENABLE_NO_SALE + " TEXT,"
                + KEY_ENABLE_ADD_CUSTOMER + " TEXT,"
                + KEY_DEFAULT_DELIVERY_DAYS + " TEXT,"
                + KEY_PASSWORD1 + " TEXT,"
                + KEY_PASSWORD2 + " TEXT,"
                + KEY_PASSWORD3 + " TEXT,"
                + KEY_PASSWORD4 + " TEXT,"
                + KEY_PASSWORD5 + " TEXT,"
                + KEY_DATE_TIME_CHANGE + " TEXT,"
                + KEY_PRICE_CHANGE + " TEXT,"
                + KEY_PROMO_OVERRIDE + " TEXT,"
                + KEY_ROUTE_SETUP + " TEXT,"
                + KEY_VIEW_STOCK + " TEXT,"
                + KEY_LOAD_SECURITY_GUARD + " TEXT,"
                + KEY_START_OF_DAY + " TEXT,"
                + KEY_SETTLEMENT + " TEXT,"
                + KEY_LOAD_ADJUST + " TEXT,"
                + KEY_ENFORCE_CALL_SEQ + " TEXT,"
                + KEY_DISPLAY_IV_SUMMARY + " TEXT,"
                + KEY_ALLOW_RADIUS + " TEXT,"
                + KEY_ENABLE_GPS + " TEXT " + ")";

        String TABLE_REASONS = "CREATE TABLE " + REASONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_REASON_TYPE + " TEXT,"
                + KEY_REASON_CODE + " TEXT,"
                + KEY_REASON_DESCRIPTION + " TEXT,"
                + KEY_REASON_DESCRIPTION_AR + " TEXT " + ")";

        String TABLE_PROMOTIONS = "CREATE TABLE " + PROMOTIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PROMOTION_TYPE + " TEXT,"
                + KEY_SALES_ORG + " TEXT,"
                + KEY_DIST_CHANNEL + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_CURRENCY + " TEXT,"
                + KEY_DRIVER + " TEXT " + ")";

        String TABLE_PRICING = "CREATE TABLE " + PRICING + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_CURRENCY + " TEXT,"
                + KEY_PRIORITY + " TEXT,"
                + KEY_DRIVER + " TEXT " + ")";

        String TABLE_RETURNS = "CREATE TABLE " + RETURNS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_REASON_TYPE + " TEXT,"
                + KEY_REASON_CODE + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_CASE + " TEXT,"
                + KEY_UNIT + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";

        String TABLE_UNLOAD_VARIANCE = "CREATE TABLE " + UNLOAD_VARIANCE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_REASON_CODE + " TEXT,"
                + KEY_VARIANCE_TYPE + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_CASE + " TEXT,"
                + KEY_UNIT + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";

        String TABLE_COLLECTION = "CREATE TABLE " + COLLECTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_COLLECTION_TYPE + " TEXT,"
                + KEY_CUSTOMER_TYPE + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_INVOICE_NO + " TEXT,"
                + KEY_INVOICE_AMOUNT + " TEXT,"
                + KEY_DUE_DATE + " TEXT,"
                + KEY_INVOICE_DATE + " TEXT,"
                + KEY_AMOUNT_CLEARED + " TEXT,"
                + KEY_CASH_AMOUNT + " TEXT,"
                + KEY_CHEQUE_AMOUNT + " TEXT,"
                + KEY_CHEQUE_NUMBER + " TEXT,"
                + KEY_CHEQUE_DATE + " TEXT,"
                + KEY_CHEQUE_BANK_CODE + " TEXT,"
                + KEY_CHEQUE_BANK_NAME + " TEXT,"
                + KEY_SAP_INVOICE_NO + " TEXT,"
                + KEY_INVOICE_DAYS + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT,"
                + KEY_IS_INVOICE_COMPLETE + " TEXT " + ")";

        String TABLE_BANKS = "CREATE TABLE " + BANKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_BANK_CODE + " TEXT,"
                + KEY_BANK_NAME + " TEXT " + ")";

        String TABLE_ACTIVITY_DAY = "CREATE TABLE " + DAYACTIVITY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_ACTIVITY_TYPE + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_PRICE + " TEXT " + ")";

        String TABLE_LOAD_VARIANCE_POST = "CREATE TABLE " + LOAD_VARIANCE_ITEMS_POST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_CASE + " TEXT,"
               // + KEY_CASE_DIFF + " TEXT,"
                + KEY_UNIT + " TEXT,"
              //  + KEY_UNIT_DIFF + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_PRICE + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_REASON_CODE + " TEXT,"
                + KEY_REASON_DESCRIPTION + " TEXT,"
                + KEY_DOCUMENT_TYPE + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";

        String TABLE_FOC_RULES = "CREATE TABLE " + FOC_RULES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_FOC_QUALIFYING_ITEM + " TEXT,"
                + KEY_FOC_ASSIGNING_ITEM + " TEXT,"
                + KEY_FOC_QUALIFYING_QUANTITY + " TEXT,"
                + KEY_FOC_ASSIGNING_QUANTITY + " TEXT,"
                + KEY_FOC_DATE_FROM + " TEXT,"
                + KEY_FOC_DATE_TO + " TEXT " + ")";

        String TABLE_CAPTURE_FOC_SALES = "CREATE TABLE " + FOC_INVOICE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_PURCHASE_NUMBER + " TEXT,"
                + KEY_ITEM_NO + " TEXT,"
                + KEY_ITEM_CATEGORY + " TEXT,"
                + KEY_MATERIAL_NO + " TEXT,"
                + KEY_MATERIAL_DESC1 + " TEXT,"
                + KEY_MATERIAL_GROUP + " TEXT,"
                + KEY_ORG_CASE + " TEXT,"
                + KEY_ORG_UNITS + " TEXT,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_UOM + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";

        String TABLE_LOAD_CONFIRMATION_HEADER = "CREATE TABLE " + LOAD_CONFIRMATION_HEADER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_FUNCTION + " TEXT,"
                + KEY_ORDER_ID + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";

        String TABLE_VISIT_LIST_POST = "CREATE TABLE " + VISIT_LIST_POST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TIME_STAMP + " TEXT,"
                + KEY_START_TIMESTAMP + " TEXT,"
                + KEY_END_TIMESTAMP + " TEXT,"
                + KEY_VISITLISTID + " TEXT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_ACTIVITY_ID + " TEXT,"
                + KEY_VISIT_SERVICED_REASON + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_IS_POSTED + " TEXT,"
                + KEY_IS_PRINTED + " TEXT " + ")";

        String TABLE_VISIT_LIST_ID_GENERATE = "CREATE TABLE " + VISIT_LIST_ID_GENERATE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DOCUMENT_TYPE + " TEXT,"
                + KEY_VISITLISTID + " TEXT " + ")";

        //Execute to create tables
        db.execSQL(TABLE_LOGIN_CREDENTIALS);
        db.execSQL(TABLE_VISIT_LIST);
        db.execSQL(TABLE_TRIP_HEADER);
        db.execSQL(TABLE_TRIP_SALES_AREA);
        db.execSQL(TABLE_ARTICLE_HEADER);
        db.execSQL(TABLE_ARTICLE_UOM);
        db.execSQL(TABLE_ARTICLE_SALES_AREA);
        db.execSQL(TABLE_CUSTOMER_HEADER);
        db.execSQL(TABLE_CUSTOMER_SALES_AREAS);
        db.execSQL(TABLE_CUSTOMER_CREDIT);
        db.execSQL(TABLE_LOAD_DELIVERY_HEADER);
        db.execSQL(TABLE_LOAD_DELIVERY_ITEMS);
        db.execSQL(TABLE_LOAD_DELIVERY_ITEMS_POST);
        db.execSQL(TABLE_CUSTOMER_DELIVERY_HEADER);
        db.execSQL(TABLE_CUSTOMER_DELIVERY_ITEMS);
        db.execSQL(TABLE_CUSTOMER_DELIVERY_ITEMS_POST);
        db.execSQL(TABLE_CUSTOMER_DELIVERY_ITEMS_DELETE_POST);
        db.execSQL(TABLE_BEGIN_DAY);
        db.execSQL(TABLE_CAPTURE_CUSTOMER_STOCK);
        db.execSQL(TABLE_CAPTURE_SALES_INVOICE);
        db.execSQL(TABLE_LOAD_REQUEST);
        db.execSQL(TABLE_ORDER_REQUEST);
        db.execSQL(TABLE_GENERATE_PR_NUMBER);
        db.execSQL(TABLE_USER_FLAGS);
        db.execSQL(TABLE_VAN_STOCK_ITEMS);
        db.execSQL(TABLE_MESSAGES);
        db.execSQL(TABLE_ODOMETER_VALUE);
        db.execSQL(TABLE_CUSTOMER_FLAGS);
        db.execSQL(TABLE_DRIVER_FLAGS);
        db.execSQL(TABLE_REASONS);
        db.execSQL(TABLE_PROMOTIONS);
        db.execSQL(TABLE_PRICING);
        db.execSQL(TABLE_RETURNS);
        db.execSQL(TABLE_UNLOAD_VARIANCE);
        db.execSQL(TABLE_COLLECTION);
        db.execSQL(TABLE_BANKS);
        db.execSQL(TABLE_ACTIVITY_DAY);
        db.execSQL(TABLE_LOAD_VARIANCE_POST);
        db.execSQL(TABLE_FOC_RULES);
        db.execSQL(TABLE_CAPTURE_FOC_SALES);
        db.execSQL(TABLE_LOAD_CONFIRMATION_HEADER);
        db.execSQL(TABLE_VISIT_LIST_POST);
        db.execSQL(TABLE_VISIT_LIST_ID_GENERATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_CREDENTIALS);
        db.execSQL("DROP TABLE IF EXISTS " + VISIT_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TRIP_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + TRIP_SALES_AREA);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_UOM);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_SALES_AREA);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_SALES_AREAS);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_CREDIT);
        db.execSQL("DROP TABLE IF EXISTS " + LOAD_DELIVERY_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + LOAD_DELIVERY_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + LOAD_DELIVERY_ITEMS_POST);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_DELIVERY_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_DELIVERY_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_DELIVERY_ITEMS_POST);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_DELIVERY_ITEMS_DELETE_POST);
        db.execSQL("DROP TABLE IF EXISTS " + BEGIN_DAY);
        db.execSQL("DROP TABLE IF EXISTS " + CAPTURE_CUSTOMER_STOCK);
        db.execSQL("DROP TABLE IF EXISTS " + CAPTURE_SALES_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + LOAD_REQUEST);
        db.execSQL("DROP TABLE IF EXISTS " + PURCHASE_NUMBER_GENERATION);
        db.execSQL("DROP TABLE IF EXISTS " + LOCK_FLAGS);
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_REQUEST);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + ODOMETER);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_FLAGS);
        db.execSQL("DROP TABLE IF EXISTS " + DRIVER_FLAGS);
        db.execSQL("DROP TABLE IF EXISTS " + REASONS);
        db.execSQL("DROP TABLE IF EXISTS " + PROMOTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + PRICING);
        db.execSQL("DROP TABLE IF EXISTS " + RETURNS);
        db.execSQL("DROP TABLE IF EXISTS " + UNLOAD_VARIANCE);
        db.execSQL("DROP TABLE IF EXISTS " + COLLECTION);
        db.execSQL("DROP TABLE IF EXISTS " + BANKS);
        db.execSQL("DROP TABLE IF EXISTS " + DAYACTIVITY);
        db.execSQL("DROP TABLE IF EXISTS " + LOAD_VARIANCE_ITEMS_POST);
        db.execSQL("DROP TABLE IF EXISTS " + FOC_RULES);
        db.execSQL("DROP TABLE IF EXISTS " + FOC_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + LOAD_CONFIRMATION_HEADER);
        db.execSQL("DROP TABLE IF EXISTS " + VISIT_LIST_POST);
        db.execSQL("DROP TABLE IF EXISTS " + VISIT_LIST_ID_GENERATE);
        onCreate(db);
    }
    //Storing Secured Credentials
    public void addLoginCredentials(String username, String password, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        byte[] sym = SecureStore.validateKey(SecureStore.generateKey(32));
        byte[] iv = SecureStore.validateKey(SecureStore.generateKey(16));
        String encryptedPassword = SecureStore.encryptData(sym, iv, password);
        values.put(KEY_SYM, new String(sym));
        values.put(KEY_IV, new String(iv));
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, encryptedPassword);
        values.put(KEY_DATE, date);
        db.insert(LOGIN_CREDENTIALS, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    //Storing Data inside Database Table
    public void addData(String tablename, HashMap<String, String> keyMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        for (Map.Entry entry : keyMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            value = clean(value);
            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            values.put(entry.getKey().toString(), value);
        }
        db.insert(tablename, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public void updateData(String tablename, HashMap<String, String> hashMap, HashMap<String, String> filters) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String[] filterArray = null;
        String filterKeys = null;
        String filterValues = null;
        ContentValues values = new ContentValues();
        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            value = clean(value);
            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            values.put(entry.getKey().toString(), value);
        }
        if (!filters.isEmpty()) {
            filterKeys = filterBuilder(filters, false);
            filterValues = paramsBuilder(filters, true);
            filterArray = paramsBuilder(filters, true).split(",");
        }
        int records = db.update(tablename, values, filterKeys, filterArray);
        Log.e("Records updated", "" + records);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public Cursor getData(String tablename, HashMap<String, String> params, HashMap<String, String> filters) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String parameters = paramsBuilder(params, false);
        String[] paramArray = paramsBuilder(params, false).split(",");
        String filterKeys = null;
        String filterValues = null;
        String[] filterArray = null;
        if (!filters.isEmpty()) {
            filterKeys = filterBuilder(filters, false);
            filterValues = paramsBuilder(filters, true);
            filterArray = paramsBuilder(filters, true).split(",");
        }
     //   Log.e("Parameter", "" + parameters);
    //    Log.e("Filters in DB", "" + filterKeys);
        Cursor cursor = db.query(tablename, paramArray, filterKeys, filterArray, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return cursor;
    }
    public boolean checkData(String tablename, HashMap<String, String> filters) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String filterKeys = null;
        String filterValues = null;
        String[] filterArray = null;
        if (!filters.isEmpty()) {
            filterKeys = filterBuilder(filters, false);
            filterValues = paramsBuilder(filters, true);
            filterArray = paramsBuilder(filters, true).split(",");
        }
        Cursor cursor = db.rawQuery("Select * from " + tablename + " where " + filterKeys, filterArray);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return exists;
    }
    public void deleteData(String tablename, HashMap<String, String> filters) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String filterKeys = null;
        String filterValues = null;
        String[] filterArray = null;
        if (!filters.isEmpty()) {
            filterKeys = filterBuilder(filters, false);
            filterValues = paramsBuilder(filters, true);
            filterArray = paramsBuilder(filters, true).split(",");
        }
        db.delete(tablename, filterKeys, filterArray);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public static String paramsBuilder(HashMap<String, String> hashMap, boolean isValue) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            value = clean(value);
            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!isValue) {
                list.add(entry.getKey().toString());
            } else {
                list.add(value);
            }
        }
        return TextUtils.join(",", list);
    }
    public static String filterBuilder(HashMap<String, String> hashMap, boolean isValue) {
        ArrayList<String> list = new ArrayList<>();
        boolean isOr = false;
        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            value = clean(value);
            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!isValue) {
                list.add(entry.getKey().toString() + "=?");
            } else {
                list.add(value);
            }
        }
        return TextUtils.join((isOr ? "or" : " and "), list);
    }
    public static String clean(String data) {
        if (data == null) return "";
        data = data.replaceAll("([^A-Za-z0-9&: \\-\\.,_\\?\\*]*)", "");
        data = data.replaceAll("([ ]+)", " ");
        return data;
    }
}
