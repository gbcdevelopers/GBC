package gbc.sa.vansales;
import android.app.Application;

import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 14-Dec-16.
 */
public class App extends Application {

    public static final String ENVIRONMENT = "Development";
    public static final String HOST = "78.93.41.222";
    public static final int PORT = 8047;
    public static final String SAP_CLIENT = "SAP-CLIENT";
    public static final String SAP_CLIENT_ID = "350";
    public static final boolean IS_HTTPS = false;
    public static final String SERVER_DOMAIN = "default";
    public static final String URL = "/sap/opu/odata/sap/ZSFA_DOWNLOAD_SRV/";

    public static final String SERVICE_USER = "ecs";
    public static final String SERVICE_PASSWORD = "sap123";

    public static final String APP_DB_NAME = "gbc.db";
    public static final String APP_DB_PATH = "/data/data/gbc.sa.vansales/databases/" + APP_DB_NAME;
    public static final String APP_DB_BACKUP_PATH = "/mnt/sdcard/GBC/Backup/";

    public static final String TRIP_ID = "ITripId";

    public static final String DATE_TIME_FORMAT = "yyyy.MM.dd.HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy.MM.dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final String CASE_UOM = "CAR";
    public static final String BOTTLES_UOM = "ZSR";

    @Override
    public void onCreate() {
        super.onCreate();
        Settings.initialize(getApplicationContext());
    }
}
