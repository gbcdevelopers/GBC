package gbc.sa.vansales.activities;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.CustomerDelivery;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.LoadDelivery;
import gbc.sa.vansales.data.Messages;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.data.TripHeader;
import gbc.sa.vansales.data.VisitList;
import gbc.sa.vansales.models.OfflinePost;
import gbc.sa.vansales.models.OfflineResponse;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.sap.SyncData;
import gbc.sa.vansales.utils.Chain;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public class SettingsActivity extends AppCompatActivity {
    String lang;
    Switch languageSwitch;
    LoadingSpinner loadingSpinner;
    ImageView iv_back;
    TextView tv_top_header;
    ImageView iv_refresh;
    Button btn_sync_data;
    DatabaseHandler db = new DatabaseHandler(this);
    ArrayList<OfflinePost> arrayList = new ArrayList<>();
    LoadingSpinner loadingSpinnerPost;
    LoginActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingSpinner = new LoadingSpinner(this, getString(R.string.changinglanguage));
        loadingSpinnerPost = new LoadingSpinner(this, getString(R.string.posting));
        setContentView(R.layout.activity_settings);
        iv_back = (ImageView) findViewById(R.id.toolbar_iv_back);
        tv_top_header = (TextView) findViewById(R.id.tv_top_header);
        iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
        btn_sync_data = (Button) findViewById(R.id.btn_synchronize);
        setSyncCount();
        iv_back.setVisibility(View.VISIBLE);
        tv_top_header.setVisibility(View.VISIBLE);
        tv_top_header.setText(getString(R.string.settings));
        iv_refresh.setVisibility(View.INVISIBLE);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lang = "";
        try {
            lang = Settings.getString(App.LANGUAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        languageSwitch = (Switch) findViewById(R.id.languageButton);
        //Log.e("Lang in Settings","" + lang);
        if (lang == null) {
            languageSwitch.setChecked(false);
        } else if (lang.equals("en")) {
            languageSwitch.setChecked(false);
        } else if (lang.equals("ar")) {
            languageSwitch.setChecked(true);
        }
        setAppInfo();
        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Settings.setString(App.IS_LOGGED_ID, "true");
                    Settings.setString(App.LANGUAGE, "ar");
                    AppController.changeLanguage(getBaseContext(), "ar");
                    Handler handler = new Handler();
                    loadingSpinner.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingSpinner.isShowing()) {
                                loadingSpinner.hide();
                            }
                            AppController.restartApp(getBaseContext());
                        }
                    }, 2000);
                } else {
                    Settings.setString(App.IS_LOGGED_ID, "true");
                    Settings.setString(App.LANGUAGE, "en");
                    AppController.changeLanguage(getBaseContext(), "en");
                    Handler handler = new Handler();
                    loadingSpinner.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingSpinner.isShowing()) {
                                loadingSpinner.hide();
                            }
                            AppController.restartApp(getBaseContext());
                        }
                    }, 2000);
                }
            }
        });
    }
    public void setSyncCount() {
        int syncCount = 0;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(db.KEY_TIME_STAMP, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
        Cursor loadRequest = db.getData(db.LOAD_REQUEST, map, filter);
        Cursor orderRequest = db.getData(db.ORDER_REQUEST, map, filter);
        if (loadRequest.getCount() > 0) {
            syncCount += loadRequest.getCount();
        }
        if (orderRequest.getCount() > 0) {
            syncCount += orderRequest.getCount();
        }
        btn_sync_data.setText(getString(R.string.synchronize) + "(" + String.valueOf(syncCount) + ")");
    }
    public int getSyncCount() {
        int syncCount = 0;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(db.KEY_TIME_STAMP, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
        Cursor loadRequest = db.getData(db.LOAD_REQUEST, map, filter);
        Cursor orderRequest = db.getData(db.ORDER_REQUEST, map, filter);
        if (loadRequest.getCount() > 0) {
            syncCount += loadRequest.getCount();
        }
        if (orderRequest.getCount() > 0) {
            syncCount += orderRequest.getCount();
        }
        return syncCount;
    }
    public void syncData(View view) {
        new syncData().execute();
    }
    public void clearData(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.data_loss_msg))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.proceed), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Settings.clearPreferenceStore();
                        SettingsActivity.this.deleteDatabase("gbc.db");
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    public void reloadData(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
        alertDialogBuilder.setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.data_loss_msg))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.proceed), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tripID = Settings.getString(App.TRIP_ID);
                        String username = Settings.getString(App.DRIVER);
                        Settings.clearPreferenceStore();
                        SettingsActivity.this.deleteDatabase("gbc.db");
                        Settings.initialize(getApplicationContext());
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }
    public void downloadData(final String tripId, final String username) {
        //Log.e("Inside chain", "" + tripId);
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_IS_BEGIN_DAY, "false");
        map.put(db.KEY_IS_LOAD_VERIFIED, "false");
        map.put(db.KEY_IS_END_DAY, "false");
        db.addData(db.LOCK_FLAGS, map);
        Chain chain = new Chain(new Chain.Link() {
            @Override
            public void run() {
                go();
            }
        });
        chain.setFail(new Chain.Link() {
            @Override
            public void run() throws Exception {
                fail();
            }
        });
        chain.add(new Chain.Link() {
            @Override
            public void run() {
                TripHeader.load(SettingsActivity.this, tripId, db);
                LoadDelivery.load(SettingsActivity.this, tripId, db);
                ArticleHeaders.load(SettingsActivity.this, tripId, db);
                CustomerHeaders.load(SettingsActivity.this, tripId, db);
                VisitList.load(SettingsActivity.this, tripId, db);
                Messages.load(SettingsActivity.this, username, db);
                CustomerDelivery.load(SettingsActivity.this, tripId, db);
                /*ArticleHeaders.loadData(getApplicationContext());
                CustomerHeaders.loadData(getApplicationContext());*/
            }
        });
        chain.add(new Chain.Link() {
            @Override
            public void run() {
                /*TripHeader.load(LoginActivity.this,tripId, db);
                LoadDelivery.load(LoginActivity.this,tripId, db);
                ArticleHeaders.load(LoginActivity.this, tripId, db);
                CustomerHeaders.load(LoginActivity.this, tripId, db);
                VisitList.load(LoginActivity.this,tripId, db);
                Messages.load(LoginActivity.this,username,db);*/
                ArticleHeaders.loadData(getApplicationContext());
                CustomerHeaders.loadData(getApplicationContext());
                OrderReasons.loadData(getApplicationContext());
            }
        });
        chain.start();
    }
    private void go() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadingSpinner.isShowing()) {
                    loadingSpinner.hide();
                    Intent intent = new Intent(SettingsActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 5000);
    }
    private void fail() {
        if (loadingSpinner.isShowing()) {
            loadingSpinner.hide();
            finish();
        }
    }
    public class syncData extends AsyncTask<Void, Void, Void> {
        ArrayList<OfflineResponse> data = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            loadingSpinnerPost.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            SettingsActivity.this.startService(new Intent(SettingsActivity.this, SyncData.class));
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinnerPost.isShowing()) {
              //  setSyncCount();
                loadingSpinnerPost.hide();
            }
        }
    }
    private void createBatch(String collectionName, HashMap<String, String> map, JSONArray deepEntity) {
        //Log.e("Map", "" + map);
        //Log.e("Deep Entity", "" + deepEntity);
        OfflinePost object = new OfflinePost();
        object.setCollectionName(collectionName);
        object.setMap(map);
        object.setDeepEntity(deepEntity);
        arrayList.add(object);
        for (OfflinePost offlinePost : arrayList) {
            //Log.e("Payload Batch","" + offlinePost.getMap());
        }
        for (int i = 0; i < arrayList.size(); i++) {
            OfflinePost obj = arrayList.get(i);
            //Log.e("Payload 2","" + obj.getMap());
        }
    }
    public void setAppInfo(){
        TextView appinfo = (TextView)findViewById(R.id.tv_appinfo);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)appinfo.getLayoutParams();
        params.setMargins(0, 110, 0, -300);
        appinfo.setLayoutParams(params);*/
        StringBuilder sb = new StringBuilder();
        sb.append("App Ver:");
        sb.append(pInfo.versionName + "." + pInfo.versionCode);
        sb.append("\t \t");
        sb.append("Build:");
        sb.append(App.ENVIRONMENT);
        /*sb.append("\n");
        sb.append("Copyright" + "\u00A9" + "; Engineering Office");*/
        // sb.append("\u00A9");
        appinfo.setTextSize(13);
        appinfo.setTypeface(null, Typeface.ITALIC);
        appinfo.setText(sb.toString());
    }
}
