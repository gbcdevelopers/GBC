package gbc.sa.vansales.activities;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import gbc.sa.vansales.R;
import gbc.sa.vansales.utils.LoadingSpinner;

public class LoginActivity extends Activity {
    private LoadingSpinner loadingSpinner;

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
            new LoginUser(id, password);
        }
    }

    private class LoginUser extends AsyncTask<Void,Void,Void>{
        private String username;
        private String password;

        private LoginUser(String username, String password) {
            this.username = username;
            this.password = password;

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
               Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
               startActivityForResult(intent, 0);
               finish();
               overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
           }
        }
    }

}
