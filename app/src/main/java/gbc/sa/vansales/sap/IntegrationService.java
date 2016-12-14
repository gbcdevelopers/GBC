package gbc.sa.vansales.sap;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import gbc.sa.vansales.App;
/**
 * Created by Rakshit on 14-Dec-16.
 */
public class IntegrationService extends IntentService {

    private static final String TAG = "IntegrationService";
    private static final String ACCEPT = "Accept";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    // Subscription KEY/VALUES
    private static final String X_CSRF_TOKEN_KEY = "X-CSRF-Token";
    private static final String X_CSRF_TOKEN_FETCH = "Fetch";
    private static final String X_REQUESTED_WITH_KEY = "X-Requested-With";
    private static final String X_REQUESTED_WITH_VAL = "XMLHttpRequest";

    private DefaultHttpClient client;
    private static String username = "";
    private static String password = "";

    public IntegrationService() {
        super(TAG);

    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);

            client = new DefaultHttpClient();
            //    DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope(), getCredentials());
            username = "ecs";
            password = "sap123";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AuthScope getAuthScope() {
        return new AuthScope(App.HOST, App.PORT);
    }
    private static UsernamePasswordCredentials getCredentials() {
        return new UsernamePasswordCredentials(username, password);
    }

    private static String getUrl(String url){
        StringBuilder builder = new StringBuilder();

        if (App.IS_HTTPS) {
            builder.append("https://");
        } else {
            builder.append("http://");
        }

        builder.append(App.HOST);
        builder.append(":");
        builder.append(App.PORT);
        builder.append(App.URL);
        builder.append(url);
        return builder.toString();
    }

    public static String getJSONString(HttpEntity r_entity) throws IOException {
        String jsonStr = EntityUtils.toString(r_entity);
        return jsonStr;
    }

    public static ArrayList<String> loginUser(Context context,String username, String password, String url) {
        ArrayList<String> mylist = new ArrayList<String>();
        try {
            Log.e("URL is",""+url);
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope(), getCredentials());
            HttpGet get = new HttpGet(getUrl(url));
            String authString = App.SERVICE_USER + ":" + App.SERVICE_PASSWORD;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            get.addHeader(App.SAP_CLIENT, App.SAP_CLIENT_ID);
            get.addHeader(ACCEPT,APPLICATION_JSON);
            HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == 201||response.getStatusLine().getStatusCode() == 200) {
                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                JSONObject jsonObj = new JSONObject(jsonString);
                jsonObj = jsonObj.getJSONObject("d");
                Log.e("JSON", "" + jsonObj);
                JSONArray jsonArray = jsonObj.getJSONArray("results");
                jsonObj = jsonArray.getJSONObject(0);
                //jsonObj = jsonObj.getJSONObject("__metadata");
                String message = jsonObj.getString("Message");
                Log.e("Message",""+message);
                String user = jsonObj.getString("Username");
                String passCode = jsonObj.getString("Password");
                String returnMessage = jsonObj.getString("Message");

                mylist.add(user);
                mylist.add(passCode);
                mylist.add(returnMessage);

                return mylist;
            }
            else{
                Log.e("Fail Again","Fail Again");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mylist;
    }
}
