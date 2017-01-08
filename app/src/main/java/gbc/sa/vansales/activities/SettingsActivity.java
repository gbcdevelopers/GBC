package gbc.sa.vansales.activities;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingSpinner = new LoadingSpinner(this,getString(R.string.changinglanguage));
        setContentView(R.layout.activity_settings);
        iv_back=(ImageView)findViewById(R.id.toolbar_iv_back);
        tv_top_header=(TextView)findViewById(R.id.tv_top_header);
        iv_refresh=(ImageView) findViewById(R.id.iv_refresh);


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
        try{
            lang = Settings.getString(App.LANGUAGE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        languageSwitch = (Switch)findViewById(R.id.languageButton);
        Log.e("Lang in Settings","" + lang);
        if(lang==null){
            languageSwitch.setChecked(false);
        }
        else if(lang.equals("en")){
            languageSwitch.setChecked(false);
        }
        else if(lang.equals("ar")){
            languageSwitch.setChecked(true);
        }
        languageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Settings.setString(App.LANGUAGE, "ar");
                    AppController.changeLanguage(getBaseContext(), "ar");
                    Handler handler = new Handler();
                    loadingSpinner.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(loadingSpinner.isShowing()){
                                loadingSpinner.hide();
                            }
                            AppController.restartApp(getBaseContext());
                        }
                    }, 2000);
                } else {
                    Settings.setString(App.LANGUAGE, "en");
                    AppController.changeLanguage(getBaseContext(), "en");
                    Handler handler = new Handler();
                    loadingSpinner.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(loadingSpinner.isShowing()){
                                loadingSpinner.hide();
                            }
                            AppController.restartApp(getBaseContext());
                        }
                    }, 2000);
                }
            }
        });

    }

    public void clearData(View view){

    }
}
