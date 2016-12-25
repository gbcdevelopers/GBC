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
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gbc.sa.vansales.App;
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
}
