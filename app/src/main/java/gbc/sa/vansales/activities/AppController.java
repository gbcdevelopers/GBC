package gbc.sa.vansales.activities;
/**
 * Created by Muhammad Umair on 29/11/2016.
 */
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import gbc.sa.vansales.App;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.Settings;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import io.fabric.sdk.android.Fabric;
public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    String lang;
    private static AppController mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        Fabric.with(this, new Crashlytics());
        mInstance = this;
        System.out.println("On Create");
        Settings.initialize(getApplicationContext());
        lang = "";
        try {
            lang = Settings.getString(App.LANGUAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Helpers.logData(getApplicationContext(),"Start of Application");
        if (lang == null) {
            Settings.setString(App.LANGUAGE, "en");
            changeLanguage(getBaseContext(), "en");
        } else if (!(lang.isEmpty() || lang == null || lang.equalsIgnoreCase(""))) {
            changeLanguage(getBaseContext(), lang);
        } else {
            changeLanguage(getBaseContext(), "en");
        }
    }
    public static void restartApp(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                mInstance.getInstance().getBaseContext(), 0, intent, 0);
        //Following code will restart your application after 2 seconds
        AlarmManager mgr = (AlarmManager) mInstance.getInstance().getBaseContext()
                .getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                pendingIntent);
        //This will finish your activity manually
        //This will stop your application and take out from it.
        System.exit(2);
    }
    public static void changeLanguage(Context context, String lang) {
        //Log.e("Language", "" + lang);
        try {
            if (lang.equalsIgnoreCase(""))
                return;
            Locale myLocale = new Locale(lang);
            saveLocale(lang);
            Locale.setDefault(myLocale);
            android.content.res.Configuration config = new android.content.res.Configuration();
            config.locale = myLocale;
            Resources resources = context.getResources();
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }
    public static void saveLocale(String lang) {
        // Settings.setString(App.LANGUAGE, lang);
    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}