package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.HashMap;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
public class ManageInventory extends AppCompatActivity {
    Button load, loadRequest, unload, vanStock;
    DatabaseHandler db = new DatabaseHandler(this);
    float lastValue = 0;
    LoadingSpinner loadingSpinner;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventery);
        setTitle(R.string.manage_inventory);
        loadingSpinner = new LoadingSpinner(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        load = (Button) findViewById(R.id.btnLoad);
        loadRequest = (Button) findViewById(R.id.btnLoadRequest);
        vanStock = (Button) findViewById(R.id.btnVanStock);
        unload = (Button) findViewById(R.id.btnUnLoad);
        setButtonVisibility();
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Load Popup", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ManageInventory.this, LoadActivity.class);
                startActivity(i);
            }
        });
        loadRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageInventory.this, LoadRequestActivity.class);
                startActivity(i);
            }
        });
        vanStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "VanStock Popup", Toast.LENGTH_LONG).show();
                Intent i = new Intent(ManageInventory.this, VanStockActivity.class);
                startActivity(i);
            }
        });
        unload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();


                /*final Dialog dialog = new Dialog(ManageInventory.this);
                View view = getLayoutInflater().inflate(R.layout.password_prompt, null);
                final EditText userInput = (EditText) view
                        .findViewById(R.id.password);
                Button btn_continue = (Button)view.findViewById(R.id.btn_ok);
                Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
                btn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = userInput.getText().toString();
                        if (input.equals("")) {
                            dialog.cancel();
                            Toast.makeText(ManageInventory.this, getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent(ManageInventory.this, UnloadActivity.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                dialog.setContentView(view);
                dialog.setCancelable(false);
                dialog.show();*/
                // Toast.makeText(getApplicationContext(), "Unload Popup", Toast.LENGTH_LONG).show();

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        setButtonVisibility();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               // onBackPressed();
                Intent intent = new Intent(ManageInventory.this,DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setButtonVisibility() {
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_IS_BEGIN_DAY, "");
        map.put(db.KEY_IS_LOAD_VERIFIED, "");
        map.put(db.KEY_IS_UNLOAD,"");
        map.put(db.KEY_IS_END_DAY, "");
        HashMap<String, String> filter = new HashMap<>();
        Cursor cursor = db.getData(db.LOCK_FLAGS, map, filter);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
        }
        boolean isBeginTripEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_BEGIN_DAY)));
        boolean isloadVerifiedEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_LOAD_VERIFIED)));
        boolean isEndDayEnabled = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(db.KEY_IS_END_DAY)));
        boolean isUnloadenabled = setUnloadVisibility();
       // boolean isUnloadenabled = true;  //This is only for testing purpose;
        if(isBeginTripEnabled){
            if (!isloadVerifiedEnabled) {
                loadRequest.setEnabled(false);
                loadRequest.setAlpha(.5f);
                vanStock.setEnabled(false);
                vanStock.setAlpha(.5f);
            }
            if(!isUnloadenabled){
                unload.setEnabled(false);
                unload.setAlpha(.5f);
            }
        }

    }
    private boolean setUnloadVisibility(){
        HashMap<String,String> map = new HashMap<>();
        map.put(db.KEY_ORDER_ID,"");
        map.put(db.KEY_PURCHASE_NUMBER,"");
        HashMap<String,String>filter = new HashMap<>();

        Cursor preSaleCount = db.getData(db.ORDER_REQUEST, map, filter);
        Cursor saleCount = db.getData(db.CAPTURE_SALES_INVOICE, map, filter);
        Cursor deliveryCount = db.getData(db.CUSTOMER_DELIVERY_ITEMS_POST, map, filter);

        boolean canActivate = preSaleCount.getCount()>0||saleCount.getCount()>0||deliveryCount.getCount()>0?true:false;
        return canActivate;
    }
    public void showDialog() {
        LayoutInflater li = LayoutInflater.from(ManageInventory.this);
        View promptsView = li.inflate(R.layout.activity_odometer_popup, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManageInventory.this);
        alertDialogBuilder.setView(promptsView);
        //Reading last save odometer
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_ODOMETER_VALUE, "");
        HashMap<String, String> filter = new HashMap<>();
        filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
        if (db.checkData(db.ODOMETER, filter)) {
            Cursor cursor = db.getData(db.ODOMETER, map, filter);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                lastValue = Float.parseFloat(cursor.getString(cursor.getColumnIndex(db.KEY_ODOMETER_VALUE)));
            }
        } else {
        }
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String input = userInput.getText().toString();
                                if (input.equals("")) {
                                    dialog.cancel();
                                    Toast.makeText(ManageInventory.this, getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                                } else {
                                    postOdometer(input);
                                }
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                hideKeyboard();
                                dialog.cancel();
//                                Intent i=new Intent(getActivity(),DashboardActivity.class);
//                                startActivity(i);
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void postOdometer(String value) {
        try{
            String purchaseNumber = Helpers.generateNumber(db, ConfigStore.Odometer_PR_Type);
            String timeStamp = Helpers.getCurrentTimeStamp();
            HashMap<String, String> map = new HashMap<>();
            map.put(db.KEY_ODOMETER_VALUE, value);
            map.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
            map.put(db.KEY_PURCHASE_NUMBER, purchaseNumber);
            map.put(db.KEY_TIME_STAMP, timeStamp);
            map.put(db.KEY_ODOMETER_TYPE,App.ODOMETER_END_DAY);
            map.put(db.KEY_IS_POSTED, App.DATA_NOT_POSTED);
            HashMap<String, String> filter = new HashMap<>();
            filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
            filter.put(db.KEY_ODOMETER_TYPE, App.ODOMETER_END_DAY);
            if (db.checkData(db.ODOMETER, filter)) {
                db.updateData(db.ODOMETER, map, filter);
            } else {
                db.addData(db.ODOMETER, map);
            }
            new postData(value, purchaseNumber);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public class postData extends AsyncTask<Void, Void, Void> {
        String flag = "";
        String value = "";
        String purchaseNumber = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingSpinner.show();
        }
        private postData(String value, String purchaseNumber) {
            this.value = value;
            this.purchaseNumber = purchaseNumber;
            execute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("TripID", Settings.getString(App.TRIP_ID));
            map.put("Value", this.value);
            JSONArray deepEntity = new JSONArray();
            this.flag = IntegrationService.postOdometer(ManageInventory.this, App.POST_ODOMETER_SET, map, deepEntity, purchaseNumber);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (loadingSpinner.isShowing()) {
                loadingSpinner.hide();
            }
            hideKeyboard();
            if (this.flag.equals(purchaseNumber)) {
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                filter.put(db.KEY_ODOMETER_TYPE,App.ODOMETER_END_DAY);
                db.updateData(db.ODOMETER, map, filter);

                final Dialog dialog = new Dialog(ManageInventory.this);
                View view = getLayoutInflater().inflate(R.layout.password_prompt, null);
                final EditText userInput = (EditText) view
                        .findViewById(R.id.password);
                Button btn_continue = (Button)view.findViewById(R.id.btn_ok);
                Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
                btn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = userInput.getText().toString();
                        if (input.equals("")) {
                            dialog.cancel();
                            Toast.makeText(ManageInventory.this, getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent(ManageInventory.this, UnloadActivity.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                dialog.setContentView(view);
                dialog.setCancelable(false);
                dialog.show();


            } else if (this.flag.equals("Y")) {
                HashMap<String, String> map = new HashMap<>();
                map.put(db.KEY_TIME_STAMP, Helpers.getCurrentTimeStamp());
                map.put(db.KEY_IS_POSTED, App.DATA_MARKED_FOR_POST);
                HashMap<String, String> filter = new HashMap<>();
                filter.put(db.KEY_TRIP_ID, Settings.getString(App.TRIP_ID));
                filter.put(db.KEY_ODOMETER_TYPE,App.ODOMETER_END_DAY);
                db.updateData(db.ODOMETER, map, filter);

                final Dialog dialog = new Dialog(ManageInventory.this);
                View view = getLayoutInflater().inflate(R.layout.password_prompt, null);
                final EditText userInput = (EditText) view
                        .findViewById(R.id.password);
                Button btn_continue = (Button)view.findViewById(R.id.btn_ok);
                Button btn_cancel = (Button)view.findViewById(R.id.btn_cancel);
                btn_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = userInput.getText().toString();
                        if (input.equals("")) {
                            dialog.cancel();
                            Toast.makeText(ManageInventory.this, getString(R.string.valid_value), Toast.LENGTH_SHORT).show();
                        } else {
                            Intent i = new Intent(ManageInventory.this, UnloadActivity.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);
                dialog.setContentView(view);
                dialog.setCancelable(false);
                dialog.show();
            } else if (this.flag.contains("Error")) {
                Toast.makeText(ManageInventory.this, this.flag.replaceAll("Error", "").trim(), Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManageInventory.this);
                alertDialogBuilder.setTitle(R.string.error_title)
                        .setMessage(R.string.error_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (loadingSpinner.isShowing()) {
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

    void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
