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
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ROUTE,"");

        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));

        Cursor routeCursor = db.getData(db.TRIP_HEADER, map, filter);
        if(routeCursor.getCount()>0){
            routeCursor.moveToFirst();
        }
        String route = routeCursor.getString(routeCursor.getColumnIndex(db.KEY_ROUTE));
       // int routeId = Integer.parseInt(route);
        int docTypeId = Integer.parseInt(getDocumentTypeNo(documentType));
        int numRange = 00000;

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
                numRange = numRange + 1;
            }
        }

        return route+String.valueOf(docTypeId)+String.valueOf(numRange);
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

        }
        return docTypeNo;
    }

    public static String getMaskedValue(String value, int length){
        return StringUtils.leftPad(value, length, "0");
    }

    public static void loadData(Context context){
        Log.e("Helper Load","Load Data");
        ArticleHeaders.loadData(context);
        CustomerHeaders.loadData(context);
    }

}
