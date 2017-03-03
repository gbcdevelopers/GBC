package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Banks;
import gbc.sa.vansales.data.CustomerDelivery;
import gbc.sa.vansales.data.CustomerHeaders;
import gbc.sa.vansales.data.DriverOpenItems;
import gbc.sa.vansales.data.DriverRouteFlags;
import gbc.sa.vansales.data.FOCData;
import gbc.sa.vansales.data.LoadDelivery;
import gbc.sa.vansales.data.Messages;
import gbc.sa.vansales.data.OrderReasons;
import gbc.sa.vansales.data.Pricing;
import gbc.sa.vansales.data.Promotions02;
import gbc.sa.vansales.data.Promotions05;
import gbc.sa.vansales.data.Promotions07;
import gbc.sa.vansales.data.TripHeader;
import gbc.sa.vansales.data.VisitList;
import gbc.sa.vansales.data.VisitReasons;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.Chain;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Logger;
import gbc.sa.vansales.utils.SecureStore;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
public class LoginActivity extends Activity {
    private LoadingSpinner loadingSpinner;

    private static final String COLLECTION_NAME = "UserAuthenticationSet";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String TRIP_ID = "ITripId";
    public String username = "";
    public String password = "";

    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        db.getWritableDatabase();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        db = new DatabaseHandler(this);
        setAppInfo();
        if( Boolean.parseBoolean(Settings.getString(App.IS_LOGGED_ID))){
            Settings.setString(App.IS_LOGGED_ID, "false");
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivityForResult(intent, 0);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        Settings.setString(App.IS_LOGGED_ID, "false");
        Settings.setString(App.LOGIN_DATE, "");
        if(Settings.getString(App.LANGUAGE)==null){
           Settings.setString(App.LANGUAGE,"en");
        }
        Helpers.logData(LoginActivity.this, "On Login Screen");
        loadingSpinner = new LoadingSpinner(this);
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


    public void login(View view){

        String id = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
       // id = "E102964";
       // password = "E102964";
        Helpers.logData(LoginActivity.this,"Login Credentials for user:" + id + "/" + password);
        if (id.isEmpty()) {
            Toast.makeText(this, R.string.enter_employee_id, Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
        } else {
            loadingSpinner.show();
            if(Helpers.isNetworkAvailable(LoginActivity.this)){
                Helpers.logData(LoginActivity.this,"Network Available. Logging in user");
                this.username = id;
                this.password = password;
                new LoginUser(id, password);
            }
            else{
                //Fetch Credentials from db for offline authentication
                Helpers.logData(LoginActivity.this,"Network is not available. And user is already logged in. Doing relogin");
                this.username = id;
                this.password = password;
                HashMap<String,String>map = new HashMap<>();
                map.put(db.KEY_USERNAME,"");
                map.put(db.KEY_PASSWORD,"");
                map.put(db.KEY_SYM, "");
                map.put(db.KEY_IV,"");
                HashMap<String,String>filter = new HashMap<>();
                filter.put(db.KEY_DATE,Helpers.formatDate(new Date(),App.DATE_FORMAT));
                Cursor c = db.getData(db.LOGIN_CREDENTIALS,map,filter);
                if(c.getCount()>0){
                    byte[]sym = c.getString(c.getColumnIndex(db.KEY_SYM)).getBytes();
                    byte[]iv = c.getString(c.getColumnIndex(db.KEY_IV)).getBytes();
                    String passwd = SecureStore.decryptData(sym,iv,c.getString(c.getColumnIndex(db.KEY_PASSWORD)));
                    if(this.username.equals(c.getString(c.getColumnIndex(db.KEY_USERNAME)))&&this.password.equals(passwd)){
                        Helpers.logData(LoginActivity.this,"User Authentication offline success");
                        Settings.setString(App.IS_DATA_SYNCING,"false");
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivityForResult(intent, 0);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                    else{
                        Helpers.logData(LoginActivity.this,"Wrong Credentials");
                        if(loadingSpinner.isShowing()){
                            loadingSpinner.hide();
                        }
                        Toast.makeText(getApplicationContext(),getString(R.string.credentials_mismatch),Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                    alertDialogBuilder.setTitle(R.string.internet_available_title)
                            .setMessage(R.string.internet_available_msg)
                            .setCancelable(false)
                            .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(loadingSpinner.isShowing()){
                                        loadingSpinner.hide();
                                    }
                                    dialog.dismiss();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
            }
        }
    }

    private class LoginUser extends AsyncTask<Void,Void,Void>{
        private String username;
        private String password;
        private String url;
        private ArrayList<String>returnList;

        private LoginUser(String username, String password) {
            this.username = username;
            this.password = password;
            this.returnList = new ArrayList<>();
            HashMap<String, String> map = new HashMap<>();
            map.put(USERNAME, username);
            map.put(PASSWORD, password);
            this.url = UrlBuilder.build(COLLECTION_NAME, null, map);
            execute();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                //Login the user
                this.returnList =  IntegrationService.loginUser(LoginActivity.this,this.username,this.password,this.url);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if(loadingSpinner.isShowing()){
                loadingSpinner.hide();
                //Log.e("Return List", "" + this.returnList.size());
                if(this.returnList.size()>0){
                    if(this.returnList.get(2).contains("Trip")){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        alertDialogBuilder.setTitle(getString(R.string.message))
                                .setMessage(this.returnList.get(2))
                                .setCancelable(false)
                                .setPositiveButton(getString(R.string.Continue_text), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        loadingSpinner.show();
                                 //For development purpose only
                                // Below code for development only..If there is no trip id driver should not proceed. Comment when building the final version.
                                /*if(!checkTripID("Y000030000000014")){
                                    Settings.setString(App.IS_DATA_SYNCING,"false");
                                    Settings.setString(TRIP_ID, "Y000030000000014");
                                    //Settings.setString(App.IS_LOGGED_ID,"true");
                                    Settings.setString(App.LOGIN_DATE,Helpers.formatDate(new Date(),App.DATE_FORMAT));
                                    db.addLoginCredentials(username, password, Helpers.formatDate(new Date(),App.DATE_FORMAT));  //For development purpose
                                    downloadData("Y000030000000014");
                                }
                                else{
                                    Settings.setString(App.IS_DATA_SYNCING,"false");
                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                    startActivityForResult(intent, 0);
                                    finish();
                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                }*/
                                        dialog.dismiss();
                                        if(loadingSpinner.isShowing()){
                                            loadingSpinner.hide();
                                        }
                                    }
                                })
                                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    }
                    else if(this.returnList.get(2).contains("Incorrect")){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        alertDialogBuilder.setTitle("Message")
                                .setMessage(this.returnList.get(2))
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    }
                    else{
                        boolean checkTripID = checkTripID(this.returnList.get(2));
                        if(!checkTripID){
                            clearDatabase();
                            Settings.setString(App.IS_DATA_SYNCING,"false");
                            Settings.setString(TRIP_ID, this.returnList.get(2));
                            //Settings.setString(App.IS_LOGGED_ID,"true");
                            Settings.setString(App.LOGIN_DATE,Helpers.formatDate(new Date(), App.DATE_FORMAT));
                            downloadData(this.returnList.get(2));
                            db.addLoginCredentials(username, password,Helpers.formatDate(new Date(),App.DATE_FORMAT));
                        }
                        else{
                            Settings.setString(App.IS_DATA_SYNCING,"false");
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivityForResult(intent, 0);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                    }

                }
                else{
                    Toast.makeText(LoginActivity.this,R.string.request_timeout,Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    public boolean checkTripID(String trip_Id){

        String tripID = "";
        tripID = Settings.getString(TRIP_ID);
        boolean returnVal = false;
        if(tripID == null){
            returnVal = false;
        }
        else if(tripID.isEmpty()){
            returnVal =  false;
        }
        else if(tripID.equals(trip_Id)){
            returnVal =  true;
        }
        else if(!tripID.equals(trip_Id)){
            returnVal = false;
        }

        return returnVal;
    }
    public void downloadData(final String tripId){
        Helpers.logData(LoginActivity.this,"Downloading user data");
        db = new DatabaseHandler(LoginActivity.this);
        db.getWritableDatabase();
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_IS_BEGIN_DAY, App.FALSE);
        map.put(db.KEY_IS_LOAD_VERIFIED, App.FALSE);
        map.put(db.KEY_IS_END_DAY,App.FALSE);
        map.put(db.KEY_IS_UNLOAD,App.FALSE);
        db.addData(db.LOCK_FLAGS, map);

        Chain chain = new Chain(new Chain.Link(){
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TripHeader.load(LoginActivity.this,tripId, db);
                        LoadDelivery.load(LoginActivity.this,tripId, db);
                        ArticleHeaders.load(LoginActivity.this, tripId, db);
                        CustomerHeaders.load(LoginActivity.this, tripId, db);
                        VisitList.load(LoginActivity.this, tripId, db);
                        Messages.load(LoginActivity.this, username, db);
                        CustomerDelivery.load(LoginActivity.this,tripId,db);
                        DriverRouteFlags.load(LoginActivity.this,tripId,db);
                        OrderReasons.load(LoginActivity.this,"",db);
                        OrderReasons.load(LoginActivity.this,App.REASON_REJECT,db);
                        VisitReasons.load(LoginActivity.this, "", db);
                        Promotions02.load(LoginActivity.this,username,db);
                        Promotions05.load(LoginActivity.this, username, db);
                        Promotions07.load(LoginActivity.this, username, db);
                        Pricing.load(LoginActivity.this, username, db);
                        Banks.load(LoginActivity.this,"",db);
                        FOCData.load(LoginActivity.this,"",db);
                        DriverOpenItems.load(LoginActivity.this,username,db);
                    }
                });
            }
        });

        chain.add(new Chain.Link(){
            @Override
            public void run() {
                ArticleHeaders.loadData(getApplicationContext());
                CustomerHeaders.loadData(getApplicationContext());
                OrderReasons.loadData(getApplicationContext());
            }
        });
        chain.start();

    }

    private void go() {
        loadingSpinner.hide();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void fail() {
        if(loadingSpinner.isShowing()){
            loadingSpinner.hide();
            finish();
        }
    }
    private void clearDatabase(){
        Settings.clearPreferenceStore();
        LoginActivity.this.deleteDatabase("gbc.db");
    }
}