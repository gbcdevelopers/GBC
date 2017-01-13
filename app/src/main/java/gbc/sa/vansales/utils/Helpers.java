package gbc.sa.vansales.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gbc.sa.vansales.App;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.CustomerHeaders;

import android.net.NetworkInfo;
import org.apache.commons.lang3.StringUtils;
/**
 * Created by Rakshit on 17-Dec-16.
 */
public class Helpers {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");

    public static String formatDate(Date date, String format) {
        if (date == null) return null;

        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormat.format(date);
    }

    public static String getCurrentTimeStamp(){
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return timeStamp;
    }

    public void backupDatabase(){
        File dbFile = new File(App.APP_DB_PATH);
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try{
            inputStream = new FileInputStream(dbFile);
            outputStream = new FileOutputStream(App.APP_DB_BACKUP_PATH);
            while (true){
                int i=inputStream.read();
                if(i!=-1){
                    outputStream.write(i);
                }
                else{
                    break;
                }
            }
            outputStream.flush();
            Log.e("Backup ok","Backup ok");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                inputStream.close();
                outputStream.close();
            }
            catch (IOException  e){
                e.printStackTrace();
            }

        }
    }

    public void restoreDatabase(){
        File file = new File(App.APP_DB_BACKUP_PATH);
        Date lastModDate = new Date(file.lastModified());
        Log.e("Last modified date","" + lastModDate);
    }

    public static Date formatDate(String date){
        Date formatDate = null;
        String pattern1 = "/Date(";
        String pattern2 = ")/";
        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(date);
        while(m.find()){
            long milli = Long.parseLong(m.group(1));
            formatDate = new Date(milli);
        }
        return formatDate;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String generateNumber(DatabaseHandler db, String documentType){

        String route = Settings.getString(App.ROUTE);
       // int routeId = Integer.parseInt(route);
        int docTypeId = Integer.parseInt(getDocumentTypeNo(documentType));
        int numRange = 0;
        int length =5;
        HashMap<String, String> search = new HashMap<>();
        search.put(db.KEY_DOC_TYPE,documentType);
        boolean checkPRNo = db.checkData(db.PURCHASE_NUMBER_GENERATION, search);
        if(checkPRNo){
            HashMap<String, String> prData = new HashMap<>();
            prData.put(db.KEY_PURCHASE_NUMBER,"");

            Cursor cursor = db.getData(db.PURCHASE_NUMBER_GENERATION,prData,search);
            if(cursor.getCount()>0){
                cursor.moveToFirst();

                numRange = Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                Log.e("Num Range From DB","" + numRange);
                numRange = numRange + 1;

                HashMap<String, String> valueMap = new HashMap<>();
                valueMap.put(db.KEY_PURCHASE_NUMBER, String.valueOf(numRange));
                db.updateData(db.PURCHASE_NUMBER_GENERATION, valueMap, search);
            }
        }
        else{
            numRange = numRange<=0?numRange+1:numRange;
            HashMap<String, String> valueMap = new HashMap<>();
            valueMap.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
            valueMap.put(db.KEY_ROUTE,Settings.getString(App.ROUTE));
            valueMap.put(db.KEY_DOC_TYPE, documentType);
            valueMap.put(db.KEY_PURCHASE_NUMBER, String.valueOf(numRange));
            Log.e("Adding Data Num Range","" + valueMap);
            db.addData(db.PURCHASE_NUMBER_GENERATION,valueMap);
        }
        return route+String.valueOf(docTypeId)+StringUtils.leftPad(String.valueOf(numRange), length, "0");
    }

    public static String generateCustomer(DatabaseHandler db, String documentType){

        String route = Settings.getString(App.ROUTE);
        String customer = "CUS";
        // int routeId = Integer.parseInt(route);
        int docTypeId = Integer.parseInt(getDocumentTypeNo(documentType));
        int numRange = 0;
        int length =4;
        HashMap<String, String> search = new HashMap<>();
        search.put(db.KEY_DOC_TYPE,documentType);
        boolean checkPRNo = db.checkData(db.PURCHASE_NUMBER_GENERATION,search);
        if(checkPRNo){
            HashMap<String, String> prData = new HashMap<>();
            prData.put(db.KEY_PURCHASE_NUMBER,"");

            Cursor cursor = db.getData(db.PURCHASE_NUMBER_GENERATION,prData,search);
            if(cursor.getCount()>0){
                cursor.moveToFirst();

                numRange = Integer.parseInt(cursor.getString(cursor.getColumnIndex(db.KEY_PURCHASE_NUMBER)));
                Log.e("Num Range From DB","" + numRange);
                numRange = numRange + 1;

                HashMap<String, String> valueMap = new HashMap<>();
                valueMap.put(db.KEY_PURCHASE_NUMBER, String.valueOf(numRange));
                db.updateData(db.PURCHASE_NUMBER_GENERATION, valueMap, search);
            }
        }
        else{
            numRange = numRange<=0?numRange+1:numRange;
            HashMap<String, String> valueMap = new HashMap<>();
            valueMap.put(db.KEY_TIME_STAMP,Helpers.getCurrentTimeStamp());
            valueMap.put(db.KEY_ROUTE,Settings.getString(App.ROUTE));
            valueMap.put(db.KEY_DOC_TYPE, documentType);
            valueMap.put(db.KEY_PURCHASE_NUMBER, String.valueOf(numRange));
            Log.e("Adding Data Num Range","" + valueMap);
            db.addData(db.PURCHASE_NUMBER_GENERATION,valueMap);
        }
        return customer + route +StringUtils.leftPad(String.valueOf(numRange), length, "0");
    }

    public static String getDocumentTypeNo(String documentType){
        String docTypeNo = "";
        switch (documentType){
            case ConfigStore.LoadRequest_PR_Type:{
                docTypeNo = ConfigStore.LoadRequest_PR;
                break;
            }
            case ConfigStore.OrderRequest_PR_Type:{
                docTypeNo = ConfigStore.OrderRequest_PR;
                break;
            }
            case ConfigStore.InvoiceRequest_PR_Type:{
                docTypeNo = ConfigStore.InvoiceRequest_PR;
                break;
            }
            case ConfigStore.CustomerDeliveryRequest_PR_Type:{
                docTypeNo = ConfigStore.CustomerDeliveryRequest_PR;
                break;
            }
            case ConfigStore.CustomerNew_PR_Type:{
                docTypeNo = ConfigStore.CustomerNew_PR;
                break;
            }

        }
        return docTypeNo;
    }

    public static  String getDayofWeek(String dateString){
        int dayOfWeek=0;
        String day="";
        try{
            Calendar c = Calendar.getInstance();
            c.setTime(new SimpleDateFormat("yyyy.MM.dd").parse(dateString));
            dayOfWeek =c.get(Calendar.DAY_OF_WEEK);
            Log.e("Day of Week","" + dayOfWeek);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        switch (dayOfWeek){
            case 1:
                day = "Sunday";
            break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
    }

    public static String getMaskedValue(String value, int length){
        return StringUtils.leftPad(value, length, "0");
    }

    public static void loadData(Context context){
        Log.e("Helper Load","Load Data");
        ArticleHeaders.loadData(context);
        CustomerHeaders.loadData(context);
    }

    public static HashMap<String,String> buildHeaderMap(String function, String orderId, String documentType,String customerId,
                                                        String orderValue,String purchaseNumber){
        HashMap<String,String> map = new HashMap<>();
        map.put("Function", function);
        map.put("OrderId", orderId.equals("")?"":orderId);
        map.put("DocumentType", documentType);
        // map.put("DocumentDate", Helpers.formatDate(new Date(),App.DATE_FORMAT_WO_SPACE));
        // map.put("DocumentDate", null);
           /* map.put("PurchaseNum", Helpers.generateNumber(db,ConfigStore.LoadRequest_PR_Type));
            purchaseNumber = map.get("PurchaseNum");*/
        map.put("CustomerId", customerId);
        map.put("SalesOrg", Settings.getString(App.SALES_ORG));
        map.put("DistChannel", Settings.getString(App.DIST_CHANNEL));
        map.put("Division", Settings.getString(App.DIVISION));
        map.put("OrderValue", orderValue.equals("")?"2000":orderValue);
        map.put("Currency", App.CURRENCY);
        map.put("PurchaseNum", purchaseNumber);
        return map;
    }

}
