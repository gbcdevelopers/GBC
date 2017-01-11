package gbc.sa.vansales;
import android.app.Application;

import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 14-Dec-16.
 */
public class App extends Application {

    public static final String AUTHORITY = "gbc.sa.vansales.provider";
    public static final String ACCOUNT_TYPE = "gbc.sa.vansales";
    public static final String ACCOUNT = "GBC";

    public static final String ENVIRONMENT = "Development";
    public static final String HOST = "78.93.41.222";
    public static final int PORT = 8047;
    public static final String SAP_CLIENT = "SAP-CLIENT";
    public static final String SAP_CLIENT_ID = "350";
    public static final boolean IS_HTTPS = false;
    public static final String SERVER_DOMAIN = "default";
    public static final String URL = "/sap/opu/odata/sap/ZSFA_DOWNLOAD_SRV/";
    public static final String POST_URL = "/sap/opu/odata/sap/ZSFA_CUSTOMER_ORDER_SRV/";
    public static final String POST_ODOMETER_URL = "/sap/opu/odata/sap/ZSFA_UPLOAD_SRV/";

    public static final String SERVICE_USER = "ecs";
    public static final String SERVICE_PASSWORD = "sap123";

    public static final String APP_DB_NAME = "gbc.db";
    public static final String APP_DB_PATH = "/data/data/gbc.sa.vansales/databases/" + APP_DB_NAME;
    public static final String APP_DB_BACKUP_PATH = "/mnt/sdcard/GBC/Backup/";

    //Static keys to store in shared preference due to global usage during posting
    public static final String TRIP_ID = "ITripId";
    public static final String ROUTE = "Route";
    public static final String DRIVER = "Driver";
    public static final String SALES_ORG = "SalesOrg";
    public static final String DIVISION = "Division";
    public static final String DIST_CHANNEL = "DistChannel";


    public static final String DATE_TIME_FORMAT = "yyyy.MM.dd.HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy.MM.dd";
    public static final String DATE_PICKER_FORMAT = "dd-MM-yyyy";
    public static final String DATE_FORMAT_WO_SPACE = "yyyyMMdd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String CASE_UOM = "CAR";
    public static final String CASE_UOM_NEW = "KAR";
    public static final String BOTTLES_UOM = "ZSR";

    public static final String CURRENCY = "SAR";

    public static final String POST_COLLECTION = "SOHeaders";
    public static final String POST_BATCH = "$batch";
    public static final String DEEP_ENTITY = "SOItems";
    public static final String POST_ODOMETER_SET = "OdometerSet";

    //Message Reading KEYS
    public static final String DRIVER_MESSAGE_KEY = "DRIVER MESSAGE KEY";
    public static final String INVOICE_HEADER_KEY = "INVOICE HEADER MESSAGE KEY";
    public static final String INVOICE_FOOTER_KEY = "INVOICE TRAILER MESSAGE KEY";
    public static final String PRINT_MESSAGE_KEY1 = "PRINT MESSAGE KEY1";
    public static final String PRINT_MESSAGE_KEY2 = "PRINT MESSAGE KEY2";
    public static final String DISPLAY_MESSAGE_KEY1 = "DISPLAY MESSAGE KEY1";
    public static final String DISPLAY_MESSAGE_KEY2 = "DISPLAY MESSAGE KEY2";

    //Post Flags
    public static final String DATA_IS_POSTED = "Y";
    public static final String DATA_MARKED_FOR_POST = "M";
    public static final String DATA_NOT_POSTED = "N";

    public static final String LANGUAGE = "lang";
    public static final String IS_DATA_SYNCING = "isDataSync";

    @Override
    public void onCreate() {
        super.onCreate();
        Settings.initialize(getApplicationContext());
    }
}
