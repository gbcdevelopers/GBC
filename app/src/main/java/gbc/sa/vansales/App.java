package gbc.sa.vansales;
import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

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
    public static final String DATA_PUT_ON_HOLD = "H";

    public static final String LANGUAGE = "lang";
    public static final String IS_DATA_SYNCING = "isDataSync";

    public static final String IS_LOGGED_ID = "isLoggedIn";
    public static final String LOGIN_DATE = "loginDate";

    public static final String IS_COMPLETE = "true";
    public static final String IS_NOT_COMPLETE = "false";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    //Reason Types
    public static final String OrderReasons = "ORDER";
    public static final String VisitReasons = "VISIT";

    //Promotions
    public static final String Promotions02 = "Y002";
    public static final String Promotions05 = "Y005";
    public static final String Promotions07 = "Y007";

    public static final String SALES = "SALES";
    //Returns
    public static final String GOOD_RETURN = "GOOD";
    public static final String BAD_RETURN = "BAD";

    //Reasons
    public static final String REASON_REJECT = "REJECT";

    //Collection Type
    public static final String COLLECTION_INVOICE = "INVOICE";
    public static final String COLLECTION_INVOICE_CASH = "INVOICE_CASH";
    public static final String COLLECTION_DELIVERY = "DELIVERY";

    //Variance for Unload
    public static final String BAD_RETURN_VARIANCE = "BAD_RETURN_VARIANCE";
    public static final String ENDING_INVENTORY = "ENDING_INVENTORY";
    public static final String TRUCK_DAMAGE = "TRUCK_DAMAGE";
    public static final String INVENTORY_VARIANCE = "INVENTORY_VARIANCE";
    public static final String THEFT = "THEFT";
    public static final String EXCESS = "EXCESS";
    public static final String FRESHUNLOAD = "FRESHUNLOAD";

    //Invoice Flags
    public static final String INVOICE_COMPLETE = "C";
    public static final String INVOICE_INCOMPLETE = "I";
    public static final String INVOICE_PARTIAL = "P";

    //Posting Constants
    public static final String STORAGE_LOCATION = "VAN";
    public static final String PLANT = "1000";

    @Override
    public void onCreate() {
        super.onCreate();
        Settings.initialize(getApplicationContext());
    }

    public static class DriverRouteControl implements Parcelable {
        private static String tripID;
        private static String driver;
        private static String routeType;
        private static boolean promptOdometer;
        private static boolean eodSalesReports;
        private static boolean isDeleteInvoice;
        private static boolean isNoSale;
        private static boolean isAddCustomer;
        private static boolean isDelayPrint;
        private static String defaultDeliveryDays;
        private static String password1;
        private static String password2;
        private static String password3;
        private static String password4;
        private static String password5;
        private static String isViewVanStock;
        private static String isLoadSecurityGuard;
        private static String isStartOfDay;
        private static String isEndTrip;
        private static String isInformationButton;
        private static boolean isCallSequence;
        private static boolean isDisplayInvoiceSummary;
        private static boolean isAllowRadius;
        private static boolean isEnableGPS;

        public static boolean isAllowRadius() {
            return isAllowRadius;
        }
        public void setIsAllowRadius(boolean isAllowRadius) {
            this.isAllowRadius = isAllowRadius;
        }
        public static boolean isEnableGPS() {
            return isEnableGPS;
        }
        public void setIsEnableGPS(boolean isEnableGPS) {
            this.isEnableGPS = isEnableGPS;
        }
        public static final Creator<DriverRouteControl> CREATOR = new Creator<DriverRouteControl>() {
            @Override
            public DriverRouteControl createFromParcel(Parcel source) {
                DriverRouteControl driverFlag = new DriverRouteControl();
                driverFlag.tripID = source.readString();
                driverFlag.driver = source.readString();
                driverFlag.routeType = source.readString();
                driverFlag.promptOdometer = source.readByte() != 0;
                driverFlag.eodSalesReports = source.readByte() != 0;
                driverFlag.isDeleteInvoice = source.readByte() != 0;
                driverFlag.isNoSale = source.readByte() != 0;
                driverFlag.isAddCustomer = source.readByte() != 0;
                driverFlag.isDelayPrint = source.readByte() != 0;
                driverFlag.defaultDeliveryDays = source.readString();
                driverFlag.password1 = source.readString();
                driverFlag.password2 = source.readString();
                driverFlag.password3 = source.readString();
                driverFlag.password4 = source.readString();
                driverFlag.password5 = source.readString();
                driverFlag.isViewVanStock = source.readString();
                driverFlag.isLoadSecurityGuard = source.readString();
                driverFlag.isStartOfDay = source.readString();
                driverFlag.isEndTrip = source.readString();
                driverFlag.isInformationButton = source.readString();
                driverFlag.isCallSequence = source.readByte() != 0;
                driverFlag.isDisplayInvoiceSummary = source.readByte() != 0;
                driverFlag.isAllowRadius = source.readByte() != 0;
                driverFlag.isEnableGPS = source.readByte() != 0;
                return driverFlag;
            }
            @Override
            public DriverRouteControl[] newArray(int size) {
                return new DriverRouteControl[size];
            }
        };
        public static String getDefaultDeliveryDays() {
            return defaultDeliveryDays;
        }
        public void setDefaultDeliveryDays(String defaultDeliveryDays) {
            this.defaultDeliveryDays = defaultDeliveryDays;
        }
        public static String getDriver() {
            return driver;
        }
        public void setDriver(String driver) {
            this.driver = driver;
        }
        public static  boolean isEodSalesReports() {
            return eodSalesReports;
        }
        public void setEodSalesReports(boolean eodSalesReports) {
            this.eodSalesReports = eodSalesReports;
        }
        public static boolean isAddCustomer() {
            return isAddCustomer;
        }
        public void setIsAddCustomer(boolean isAddCustomer) {
            this.isAddCustomer = isAddCustomer;
        }
        public static boolean isCallSequence() {
            return isCallSequence;
        }
        public void setIsCallSequence(boolean isCallSequence) {
            this.isCallSequence = isCallSequence;
        }
        public static  boolean isDelayPrint() {
            return isDelayPrint;
        }
        public void setIsDelayPrint(boolean isDelayPrint) {
            this.isDelayPrint = isDelayPrint;
        }
        public static boolean isDeleteInvoice() {
            return isDeleteInvoice;
        }
        public void setIsDeleteInvoice(boolean isDeleteInvoice) {
            this.isDeleteInvoice = isDeleteInvoice;
        }
        public static  boolean isDisplayInvoiceSummary() {
            return isDisplayInvoiceSummary;
        }
        public void setIsDisplayInvoiceSummary(boolean isDisplayInvoiceSummary) {
            this.isDisplayInvoiceSummary = isDisplayInvoiceSummary;
        }
        public static String getIsEndTrip() {
            return isEndTrip;
        }
        public void setIsEndTrip(String isEndTrip) {
            this.isEndTrip = isEndTrip;
        }
        public static String getIsInformationButton() {
            return isInformationButton;
        }
        public void setIsInformationButton(String isInformationButton) {
            this.isInformationButton = isInformationButton;
        }
        public static String getIsLoadSecurityGuard() {
            return isLoadSecurityGuard;
        }
        public void setIsLoadSecurityGuard(String isLoadSecurityGuard) {
            this.isLoadSecurityGuard = isLoadSecurityGuard;
        }
        public static  boolean isNoSale() {
            return isNoSale;
        }
        public void setIsNoSale(boolean isNoSale) {
            this.isNoSale = isNoSale;
        }
        public static String getIsStartOfDay() {
            return isStartOfDay;
        }
        public void setIsStartOfDay(String isStartOfDay) {
            this.isStartOfDay = isStartOfDay;
        }
        public static String getIsViewVanStock() {
            return isViewVanStock;
        }
        public void setIsViewVanStock(String isViewVanStock) {
            this.isViewVanStock = isViewVanStock;
        }
        public static String getPassword1() {
            return password1;
        }
        public void setPassword1(String password1) {
            this.password1 = password1;
        }
        public static String getPassword2() {
            return password2;
        }
        public void setPassword2(String password2) {
            this.password2 = password2;
        }
        public static String getPassword3() {
            return password3;
        }
        public void setPassword3(String password3) {
            this.password3 = password3;
        }
        public static String getPassword4() {
            return password4;
        }
        public void setPassword4(String password4) {
            this.password4 = password4;
        }
        public static String getPassword5() {
            return password5;
        }
        public void setPassword5(String password5) {
            this.password5 = password5;
        }
        public static boolean isPromptOdometer() {
            return promptOdometer;
        }
        public void setPromptOdometer(boolean promptOdometer) {
            this.promptOdometer = promptOdometer;
        }
        public static String getRouteType() {
            return routeType;
        }
        public void setRouteType(String routeType) {
            this.routeType = routeType;
        }
        public static String getTripID() {
            return tripID;
        }
        public void setTripID(String tripID) {
            this.tripID = tripID;
        }
        @Override
        public int describeContents() {
            return 0;
        }
        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeString(tripID);
            parcel.writeString(driver);
            parcel.writeString(routeType);
            parcel.writeByte((byte) (promptOdometer ? 1 : 0));
            parcel.writeByte((byte) (eodSalesReports ? 1 : 0));
            parcel.writeByte((byte) (isDeleteInvoice ? 1 : 0));
            parcel.writeByte((byte) (isNoSale ? 1 : 0));
            parcel.writeByte((byte) (isAddCustomer ? 1 : 0));
            parcel.writeByte((byte) (isDelayPrint ? 1 : 0));
            parcel.writeString(defaultDeliveryDays);
            parcel.writeString(password1);
            parcel.writeString(password2);
            parcel.writeString(password3);
            parcel.writeString(password4);
            parcel.writeString(password5);
            parcel.writeString(isViewVanStock);
            parcel.writeString(isLoadSecurityGuard);
            parcel.writeString(isStartOfDay);
            parcel.writeString(isEndTrip);
            parcel.writeString(isInformationButton);
            parcel.writeByte((byte) (isCallSequence ? 1 : 0));
            parcel.writeByte((byte) (isDisplayInvoiceSummary ? 1 : 0));
            parcel.writeByte((byte) (isAllowRadius ? 1 : 0));
            parcel.writeByte((byte) (isEnableGPS ? 1 : 0));
        }

    }
}
