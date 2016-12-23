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



}
