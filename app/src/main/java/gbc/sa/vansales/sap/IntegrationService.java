package gbc.sa.vansales.sap;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import gbc.sa.vansales.App;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.UrlBuilder;
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

    private static String getUrl(){
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
        return builder.toString();
    }

    private static String postUrl(String collectionname){
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
        builder.append(collectionname);
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
            get.addHeader(ACCEPT, APPLICATION_JSON);
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

    public static JSONArray getService(Context context, String url){
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope(), getCredentials());
            HttpGet get = new HttpGet(getUrl(url));
            String authString = App.SERVICE_USER + ":" + App.SERVICE_PASSWORD;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            get.addHeader(App.SAP_CLIENT, App.SAP_CLIENT_ID);
            get.addHeader(ACCEPT, APPLICATION_JSON);
            HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == 201||response.getStatusLine().getStatusCode() == 200) {
                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                jsonObj = new JSONObject(jsonString);
                jsonObj = jsonObj.getJSONObject("d");
                Log.e("JSON", "" + jsonObj);
                jsonArray = jsonObj.getJSONArray("results");

                return jsonArray;
            }
            else{
                Log.e("Fail Again","Fail Again");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public static String loadData(String url){
        try{
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope(), getCredentials());
            HttpGet get = new HttpGet(getUrl(url));
            String authString = App.SERVICE_USER + ":" + App.SERVICE_PASSWORD;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            get.addHeader(App.SAP_CLIENT, App.SAP_CLIENT_ID);
            get.addHeader(ACCEPT, APPLICATION_JSON);
            HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == 201||response.getStatusLine().getStatusCode() == 200) {
                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                return jsonString;

            }
            else{
                Log.e("Fail Again","Fail Again");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void postData(Context context, String collection, HashMap<String, String> map,JSONArray deepEntity){
        try{
            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postUrl(collection));
            post.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            post.addHeader(ACCEPT,APPLICATION_JSON);
            post.addHeader(X_REQUESTED_WITH_KEY,X_REQUESTED_WITH_VAL);
         //   post.addHeader(X_CSRF_TOKEN_KEY,token);
            post.setEntity(getPayload(map, deepEntity));
            HttpResponse response = client.execute(post);

            if (response.getStatusLine().getStatusCode() == 201) {

                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
            }
            else{
                Log.e("fail", "Fail" + response.getStatusLine().getStatusCode());

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> RequestToken(Context context){
        ArrayList<String> mylist = new ArrayList<String>();
        try {
            DefaultHttpClient client = new DefaultHttpClient();
           // client.getCredentialsProvider().setCredentials(getAuthScope1(), getCredentials1(logonCore));
            HttpGet get = new HttpGet(getUrl());

            get.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            get.addHeader(X_CSRF_TOKEN_KEY, X_CSRF_TOKEN_FETCH);
            get.addHeader(App.SAP_CLIENT, App.SAP_CLIENT_ID);
            // get.addHeader(X_REQUESTED_WITH_KEY, X_REQUESTED_WITH_VAL);

            HttpResponse response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                Header[] headers = response.getAllHeaders();
                String tokenval = response.getFirstHeader(X_CSRF_TOKEN_KEY).getValue();
                Log.e("Token","" + tokenval);
                if(!tokenval.equals("")){
                    mylist.add(tokenval);
                }
                return mylist;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mylist;
    }

    private static HttpEntity getPayload(HashMap<String,String> map,JSONArray deepEntity) throws IOException {
        String data = "";
        try{

            StringBuilder body = new StringBuilder();
            body.append(bodyBuilder(map));

            if(deepEntity.length()>0){
                body.append(",");
                body.append(App.DEEP_ENTITY + ":[");
                for(int i=0;i<deepEntity.length();i++){
                    body.append("{");
                    body.append(bodyBuilder(convertToMap(deepEntity.getJSONObject(i))));
                    body.append("}");
                    if(deepEntity.length()>1&&i<deepEntity.length()-1){
                        body.append(",");
                    }
                }
                body.append("]");

            }
            Log.e("String Build","" + body.toString());
           // data = String.format("{" + body.toString() + "}");
            data = "{" + body.toString() + "}";
            Log.e("POST Data","" + data);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ByteArrayEntity(data.getBytes());
    }

    public static HashMap<String, String> convertToMap(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap();
        Iterator keys = object.keys();
        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = object.getString(key);
            map.put(key, value);
        }
        return map;
    }

    public static String bodyBuilder(HashMap<String, String> hashMap) {
        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();

            value = UrlBuilder.clean(value);

            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            list.add("\"" + entry.getKey() + "\""+ ":\"" + value + "\"");
        }

        return TextUtils.join(",", list);
    }
}
