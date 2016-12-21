package gbc.sa.vansales.utils;
import android.content.ContentValues;
import android.content.Context;
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

    private static final String LOGIN_CREDENTIALS = "LOGIN_CREDENTIALS";
    private static final String VISIT_LIST = "VISIT_LIST";
    private static final String TRIP_HEADER = "TRIP_HEADER";
    private static final String TRIP_SALES_AREA = "TRIP_SALES_AREA";
    private static final String ARTICLE_HEADER = "ARTICLE_HEADER";
    private static final String ARTICLE_UOM = "ARTICLE_UOM";
    private static final String ARTICLE_SALES_AREA = "ARTICLE_SALES_AREA";

    //Properties for Table(Based on Entity Sets)

    //UserAuthenticationSet
    private static final String KEY_ID = "_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IV = "iv";
    private static final String KEY_SYM = "symKey";

    //VisitListSet
    private static final String KEY_TRIP_ID = "tripId";
    private static final String KEY_VISITLISTID = "visitListId";
    private static final String KEY_ITEMNO = "itemNo";
    private static final String KEY_CUSTOMER_NO = "customerNo";
    private static final String KEY_EXEC_DATE = "execDate";
    private static final String KEY_DRIVER = "driver1";
    private static final String KEY_VP_TYPE = "vpType";

    //TripHeaderSet
    private static final String KEY_ROUTE = "route";
    private static final String KEY_TRUCK = "truck";
    private static final String KEY_PS_DATE = "psDate";
    private static final String KEY_AS_DATE = "asDate";
    private static final String KEY_TOUR_TYPE = "tourType";
    private static final String KEY_CREATED_TIME = "createdTime";
    private static final String KEY_CREATED_BY = "createdBy";
    private static final String KEY_SETTLED_BY = "settledBy";
    private static final String KEY_DOWN_STATUS = "downloadStatus";
    private static final String KEY_UP_STATUS = "uploadStatus";
    private static final String KEY_LOADS = "loads";

    //Trip Sales Area Collection
    private static final String KEY_DATE = "date";
    private static final String KEY_START_DATE = "startDate";
    private static final String KEY_START_TIME = "startTime";
    private static final String KEY_DIST_CHANNEL = "distChannel";
    private static final String KEY_DIVISION = "division";

    //ArticleHeaderSet
    private static final String KEY_MATERIAL_GROUPA_DESC = "materialGroupADesc";
    private static final String KEY_MATERIAL_GROUPB_DESC = "materialGroupBDesc";
    private static final String KEY_MATERIAL_DESC2 = "materialDesc2";
    private static final String KEY_BATCH_MANAGEMENT = "batchManagement";
    private static final String KEY_VOLUME_UOM = "volumeUOM";
    private static final String KEY_VOLUME = "volume";
    private static final String KEY_WEIGHT_UOM = "weightUOM";
    private static final String KEY_NET_WEIGHT = "netWeight";
    private static final String KEY_GROSS_WEIGHT = "grossWeight";
    private static final String KEY_ARTICLE_CATEGORY = "articleCategory";
    private static final String KEY_ARTICLE_NO = "articleNo";
    private static final String KEY_BASE_UOM = "baseUOM";
    private static final String KEY_MATERIAL_GROUP = "materialGroup";
    private static final String KEY_MATERIAL_TYPE = "materialType";
    private static final String KEY_MATERIAL_DESC1 = "materialDesc1";
    private static final String KEY_MATERIAL_NO = "materialNo";

    //Article UOM Collection
    private static final String KEY_UOM = "uom";
    private static final String KEY_NUMERATOR = "numerator";
    private static final String KEY_DENOMINATOR = "denominator";

    //Article Sales Area Collection
    private static final String KEY_PRICE_REF_MAT = "priceRefMat";
    private static final String KEY_SALES_UOM = "salesUOM";
    private static final String KEY_MATERIAL_PURCHASE_GROUP = "materialPurGroup";
    private static final String KEY_PRODUCT_HIERARCHY = "prodHierarchy";
    private static final String KEY_MINIMUM_ORDER_QTY = "minOrderQty";
    private static final String KEY_SALES_STATUS = "salesStatus";
    private static final String KEY_EMPTY_R_BLOCK = "emptyRBlock";
    private static final String KEY_EMPTY_GROUP = "emptyGroup";
    private static final String KEY_SKT_OF = "sktOf";
    private static final String KEY_SALES_ORG = "salesOrg";

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
                + KEY_IV + " TEXT " + ")";

        String TABLE_VISIT_LIST = "CREATE TABLE " + VISIT_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TRIP_ID + " TEXT,"
                + KEY_VISITLISTID + " TEXT,"
                + KEY_ITEMNO + " TEXT,"
                + KEY_CUSTOMER_NO + " TEXT,"
                + KEY_EXEC_DATE + " TEXT,"
                + KEY_DRIVER + " TEXT,"
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


        //Execute to create tables
        db.execSQL(TABLE_LOGIN_CREDENTIALS);
        db.execSQL(TABLE_VISIT_LIST);
        db.execSQL(TABLE_TRIP_HEADER);
        db.execSQL(TABLE_TRIP_SALES_AREA);
        db.execSQL(TABLE_ARTICLE_HEADER);
        db.execSQL(TABLE_ARTICLE_UOM);
        db.execSQL(TABLE_ARTICLE_SALES_AREA);
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

        onCreate(db);
    }

    //Storing Secured Credentials
    public void addLoginCredentials(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        byte[] sym = SecureStore.validateKey(SecureStore.generateKey(32));
        byte[] iv = SecureStore.validateKey(SecureStore.generateKey(16));
        String encryptedPassword = SecureStore.encryptData(sym,iv,password);


        values.put(KEY_SYM,new String(sym));
        values.put(KEY_IV, new String(iv));
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, encryptedPassword);

    }

    //Storing Data inside Database Table
    public void addData(String tablename, HashMap<String, String> parameters, HashMap<String, String> filters, boolean isOr){
        SQLiteDatabase db = this.getWritableDatabase();


    }

    public static String paramsBuilder(HashMap<String, String> hashMap) {
        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            value = clean(value);
            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            list.add(entry.getKey() + "," + value + "");
        }

        return "(" + TextUtils.join(",", list) + ")";
    }

    public static String clean(String data) {
        if (data == null) return "";

        data = data.replaceAll("([^A-Za-z0-9&: \\-\\.,_\\?\\*]*)", "");

        data = data.replaceAll("([ ]+)", " ");

        return data;
    }

}
