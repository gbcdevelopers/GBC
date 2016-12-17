package gbc.sa.vansales.activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import gbc.sa.vansales.R;
import gbc.sa.vansales.sap.IntegrationService;
import gbc.sa.vansales.utils.LoadingSpinner;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
public class LoginActivity extends Activity {
    private LoadingSpinner loadingSpinner;

    private static final String COLLECTION_NAME = "UserAuthenticationSet";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String TRIP_ID = "ITripId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
            new LoginUser("E2000", "PASSWORD");
            //new LoginUser(id, password);
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
               Log.e("Return List", "" + this.returnList.size());
               if(this.returnList.size()>0){
                   if(this.returnList.get(2).contains("Trip")){
                       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                       alertDialogBuilder.setTitle("Message")
                               .setMessage(this.returnList.get(2))
                               .setCancelable(false)
                               .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                       startActivityForResult(intent, 0);
                                       finish();
                                       overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
                       Settings.setString(TRIP_ID,this.returnList.get(2));
                   }

               }

           }
        }
    }

}
