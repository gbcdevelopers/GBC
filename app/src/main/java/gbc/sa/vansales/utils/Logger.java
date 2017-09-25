package gbc.sa.vansales.utils;
/**
 * Created by Rakshit on 16-Feb-17.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import gbc.sa.vansales.App;
public class Logger {
    private boolean isAppenlog;
    private Context context;
    private Activity activity;
    private String text;
    public Logger() {
        this.isAppenlog = true;
    }

    public void appendLog(Activity activity,String text) {
        this.activity = activity;
        this.context = activity.getBaseContext();
        this.text = text;
        if (this.isAppenlog) {
            if(checkPermission()){
                File dir = new File(Environment.getExternalStorageDirectory() + "/sfa/");
                String storagestate = Environment.getExternalStorageState();
                File documentsFolder = null;
                if (storagestate.equals(Environment.MEDIA_MOUNTED)){
                    documentsFolder = new File(Environment.getExternalStorageDirectory(),
                            "Download" + File.separator + "SFA");
                    if (!documentsFolder.exists()) {
                        documentsFolder.mkdirs();
                    }
                    //File logFile = new File(documentsFolder + "/sfa/sfa_log_+" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt");
                    deletePreviousLogFiles(this.context);
                    File logFile = new File(documentsFolder.getPath() + File.separator + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt");
                    if (!logFile.exists()) {
                        try {
                            logFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                        buf.append(new StringBuilder(String.valueOf(new Date().toString())).append(StringUtilities.LF).toString());
                        buf.append(text);
                        buf.newLine();
                        buf.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            else{
                requestPermission();
            }
        }
    }
    public void appendLog(Context context,String text) {
        this.context = context;
        this.text = text;
        if (this.isAppenlog) {
            if(checkPermission()){
                File dir = new File(Environment.getExternalStorageDirectory() + "/sfa/");
                String storagestate = Environment.getExternalStorageState();
                File documentsFolder = null;
                if (storagestate.equals(Environment.MEDIA_MOUNTED)){
                    documentsFolder = new File(Environment.getExternalStorageDirectory(),
                            "Download" + File.separator + "SFA");
                    if (!documentsFolder.exists()) {
                        documentsFolder.mkdirs();
                    }
                    //File logFile = new File(documentsFolder + "/sfa/sfa_log_+" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt");
                    File logFile = new File(documentsFolder.getPath() + File.separator + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt");
                    if (!logFile.exists()) {
                        try {
                            logFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                        buf.append(new StringBuilder(String.valueOf(new Date().toString())).append(StringUtilities.LF).toString());
                        buf.append(text);
                        buf.newLine();
                        buf.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            else{
                requestPermission();
            }
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    protected void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(context, "Write External Storage permission allows us to do write files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }
    protected void deletePreviousLogFiles(Context context){
        String storagestate = Environment.getExternalStorageState();
        File documentsFolder = null;
        if (storagestate.equals(Environment.MEDIA_MOUNTED)){
            documentsFolder = new File(Environment.getExternalStorageDirectory(),
                    "Download" + File.separator + "SFA");
            if (documentsFolder.exists()) {
                File logFile = new File(documentsFolder.getPath() + File.separator + new SimpleDateFormat("yyyy-MM-dd").format(giveLowerDate(new Date())) + ".txt");
                if (logFile.exists()) {
                    try {
                        if(logFile.delete()){
                            logFile.getCanonicalFile().delete();
                            if(logFile.exists()){
                                context.deleteFile(logFile.getName());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //File logFile = new File(documentsFolder + "/sfa/sfa_log_+" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt");

        }
    }
    public Date giveLowerDate(Date date){
        Date date1 = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); // convert your date to Calendar object
        int daysToDecrement = -5;
        cal.add(Calendar.DATE, daysToDecrement);
        date1 = cal.getTime(); // again get back your date object
        return date1;
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    appendLog(activity,text);
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}
