package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.data.Banks;
import gbc.sa.vansales.data.CustomerDelivery;
import gbc.sa.vansales.data.CustomerHeaders;
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
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
public class LoginActivity extends Activity {
    private LoadingSpinner loadingSpinner;

    private static final String COLLECTION_NAME = "UserAuthenticationSet";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String TRIP_ID = "ITripId";
    public String username = "";

    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        db = new DatabaseHandler(this);
        db.getWritableDatabase();

        if( Boolean.parseBoolean(Settings.getString(App.IS_LOGGED_ID))){
            Settings.setString(App.IS_LOGGED_ID, "false");
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivityForResult(intent, 0);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        Settings.setString(App.IS_LOGGED_ID, "false");
        Settings.setString(App.LOGIN_DATE, "");
        loadingSpinner = new LoadingSpinner(this);
}
    public void login(View view){

        String id = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

        if (id.isEmpty()) {
            Toast.makeText(this, R.string.enter_employee_id, Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
        } else {
            loadingSpinner.show();
            //Logic to Login the user
            //For development purpose hardcoding credentials
            //new LoginUser("E2000", "PASSWORD");
            if(Helpers.isNetworkAvailable(LoginActivity.this)){
                this.username = id;
                new LoginUser(id, password);
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

                                      //  downloadData("GBC012000000003");
                                if(!checkTripID("Y000010000000011")){
                                    Settings.setString(App.IS_DATA_SYNCING,"false");
                                    Settings.setString(TRIP_ID, "Y000010000000011");
                                    Settings.setString(App.IS_LOGGED_ID,"true");
                                    Settings.setString(App.LOGIN_DATE,Helpers.formatDate(new Date(),App.DATE_FORMAT));
                                    db.addLoginCredentials("E2000", "PASSWORD", Helpers.formatDate(new Date(),App.DATE_FORMAT));  //For development purpose
                                    downloadData("Y000010000000011");
                                }
                                else{
                                    Settings.setString(App.IS_DATA_SYNCING,"false");
                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                    startActivityForResult(intent, 0);
                                    finish();
                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                }
                                   //     downloadData("GBC012000000003");
                               /* Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivityForResult(intent, 0);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/
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

                        // Settings.getEditor().putString(TRIP_ID, this.returnList.get(2)).commit();

                        boolean checkTripID = checkTripID(this.returnList.get(2));

                        if(!checkTripID){
                            Settings.setString(App.IS_DATA_SYNCING,"false");
                            Settings.setString(TRIP_ID, this.returnList.get(2));
                            Settings.setString(App.IS_LOGGED_ID,"true");
                            Settings.setString(App.LOGIN_DATE,Helpers.formatDate(new Date(), App.DATE_FORMAT));
                            downloadData(this.returnList.get(2));
                            db.addLoginCredentials("E2000", "PASSWORD",Helpers.formatDate(new Date(),App.DATE_FORMAT));
                        }
                        else{
                            Settings.setString(App.IS_DATA_SYNCING,"false");
                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            startActivityForResult(intent, 0);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }



                      /* Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                       startActivityForResult(intent, 0);
                       finish();
                       overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/
                        //  db.addLoginCredentials("E2000","PASSWORD");

                    }

                }
                else{
                    Toast.makeText(LoginActivity.this,R.string.request_timeout,Toast.LENGTH_SHORT).show();
                    //For testing Purpose only
                    /*Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivityForResult(intent, 0);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/
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

        return returnVal;
    }
    public void downloadData(final String tripId){
        //Log.e("Inside chain", "" + tripId);

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
                    }
                });

                /*ArticleHeaders.loadData(getApplicationContext());
                CustomerHeaders.loadData(getApplicationContext());*/
            }
        });

        chain.add(new Chain.Link(){
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
}
