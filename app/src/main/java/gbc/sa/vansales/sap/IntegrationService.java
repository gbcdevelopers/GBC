package gbc.sa.vansales.sap;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gbc.sa.vansales.App;
import gbc.sa.vansales.models.OfflinePost;
import gbc.sa.vansales.models.OfflineResponse;
import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.Helpers;
import gbc.sa.vansales.utils.Settings;
import gbc.sa.vansales.utils.UrlBuilder;
/**
 * Created by Rakshit on 14-Dec-16.
 */
public class IntegrationService extends IntentService {
    private static final String TAG = "IntegrationService";
    private static final String ACCEPT = "Accept";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_BATCH = "multipart/mixed; boundary=batch";
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
    private static String getUrl(String url) {
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
    private static String getUrl() {
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
    private static String postUrl(String collectionname) {
        StringBuilder builder = new StringBuilder();
        if (App.IS_HTTPS) {
            builder.append("https://");
        } else {
            builder.append("http://");
        }
        builder.append(App.HOST);
        builder.append(":");
        builder.append(App.PORT);
        builder.append(App.POST_URL);
        builder.append(collectionname);
        Log.e("Builder is", "" + builder.toString());
        return builder.toString();
    }
    private static String postUrlBatch(){
        StringBuilder builder = new StringBuilder();
        if (App.IS_HTTPS) {
            builder.append("https://");
        } else {
            builder.append("http://");
        }
        builder.append(App.HOST);
        builder.append(":");
        builder.append(App.PORT);
        builder.append(App.POST_URL);
        builder.append(App.POST_BATCH);
        Log.e("Builder is", "" + builder.toString());
        return builder.toString();
    }
    private static String postUrlBeginDayBatch(){
        StringBuilder builder = new StringBuilder();
        if (App.IS_HTTPS) {
            builder.append("https://");
        } else {
            builder.append("http://");
        }
        builder.append(App.HOST);
        builder.append(":");
        builder.append(App.PORT);
        builder.append(App.POST_URL);
        builder.append(App.POST_BATCH);
        Log.e("Builder is", "" + builder.toString());
        return builder.toString();
    }
    private static String postUrlBatchOdometer(){
        StringBuilder builder = new StringBuilder();
        if (App.IS_HTTPS) {
            builder.append("https://");
        } else {
            builder.append("http://");
        }
        builder.append(App.HOST);
        builder.append(":");
        builder.append(App.PORT);
        builder.append(App.POST_ODOMETER_URL);
        builder.append(App.POST_BATCH);
        Log.e("Builder is", "" + builder.toString());
        return builder.toString();
    }


    private static String postUrlOdometer(String collectionname) {
        StringBuilder builder = new StringBuilder();
        if (App.IS_HTTPS) {
            builder.append("https://");
        } else {
            builder.append("http://");
        }
        builder.append(App.HOST);
        builder.append(":");
        builder.append(App.PORT);
        builder.append(App.POST_ODOMETER_URL);
        builder.append(collectionname);
        Log.e("Builder is", "" + builder.toString());
        return builder.toString();
    }
    public static String getJSONString(HttpEntity r_entity) throws IOException {
        String jsonStr = EntityUtils.toString(r_entity);
        return jsonStr;
    }
    public static ArrayList<String> loginUser(Context context, String username, String password, String url) {
        ArrayList<String> mylist = new ArrayList<String>();
        try {
            Log.e("URL is", "" + url);
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope(), getCredentials());
            HttpGet get = new HttpGet(getUrl(url));
            String authString = App.SERVICE_USER + ":" + App.SERVICE_PASSWORD;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            get.addHeader(App.SAP_CLIENT, App.SAP_CLIENT_ID);
            get.addHeader(ACCEPT, APPLICATION_JSON);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 201 || response.getStatusLine().getStatusCode() == 200) {
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
                Log.e("Message", "" + message);
                String user = jsonObj.getString("Username");
                String passCode = jsonObj.getString("Password");
                String returnMessage = jsonObj.getString("Message");
                mylist.add(user);
                mylist.add(passCode);
                mylist.add(returnMessage);
                return mylist;
            } else {
                Log.e("Fail Again", "Fail Again");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mylist;
    }
    public static JSONArray getService(Context context, String url) {
        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope(), getCredentials());
            HttpGet get = new HttpGet(getUrl(url));
            String authString = App.SERVICE_USER + ":" + App.SERVICE_PASSWORD;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            get.addHeader(App.SAP_CLIENT, App.SAP_CLIENT_ID);
            get.addHeader(ACCEPT, APPLICATION_JSON);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 201 || response.getStatusLine().getStatusCode() == 200) {
                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                jsonObj = new JSONObject(jsonString);
                jsonObj = jsonObj.getJSONObject("d");
                Log.e("JSON", "" + jsonObj);
                jsonArray = jsonObj.getJSONArray("results");
                return jsonArray;
            } else {
                Log.e("Fail Again", "Fail Again");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
    public static String loadData(String url) {
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope(), getCredentials());
            HttpGet get = new HttpGet(getUrl(url));
            String authString = App.SERVICE_USER + ":" + App.SERVICE_PASSWORD;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            get.addHeader(App.SAP_CLIENT, App.SAP_CLIENT_ID);
            get.addHeader(ACCEPT, APPLICATION_JSON);
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 201 || response.getStatusLine().getStatusCode() == 200) {
                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                return jsonString;
            } else {
                Log.e("Fail Again", "Fail Again");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static AuthScope getAuthScope(String var) {
        return new AuthScope(App.HOST, App.PORT);
    }
    private static UsernamePasswordCredentials getCredentials(String username, String password) {
        return new UsernamePasswordCredentials(username, password);
    }
    public static String batch(Context context) {
        return null;
    }
    public static String postData(Context context, String collection, HashMap<String, String> map, JSONArray deepEntity) {
        String orderId = "";
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope("hello"), getCredentials("ecs", "sap123"));
            HttpPost post = new HttpPost(postUrl(collection));
            String authString = "ecs" + ":" + "sap123";
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
          //  post.addHeader("Authorization", "Basic " + new String(authEncBytes));
            post.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            post.addHeader(ACCEPT, APPLICATION_JSON);
            post.addHeader(X_REQUESTED_WITH_KEY, X_REQUESTED_WITH_VAL);
            //   post.addHeader(X_CSRF_TOKEN_KEY,token);
            post.setEntity(getPayload(map, deepEntity));
            if (!Helpers.isNetworkAvailable(context)) {
                if(map.containsKey("PurchaseNum")){
                    orderId = map.get("PurchaseNum").toString();
                }
                else if(map.containsKey("OrderId")){
                    orderId = map.get("OrderId").toString();
                }
                //orderId = map.get("PurchaseNum").toString();
            } else {
                if(map.containsKey("PurchaseNum")){
                    orderId = map.get("PurchaseNum").toString();
                }
                else if(map.containsKey("OrderId")){
                    orderId = map.get("OrderId").toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderId;
    }
    public static String postDataBackup(Context context, String collection, HashMap<String, String> map, JSONArray deepEntity) {
        String orderId = "";
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope("hello"), getCredentials("ecs", "sap123"));
            HttpPost post = new HttpPost(postUrl(collection));
            String authString = "ecs" + ":" + "sap123";
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
           // post.addHeader("Authorization", "Basic " + new String(authEncBytes));
            post.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            post.addHeader(ACCEPT, APPLICATION_JSON);
            post.addHeader(X_REQUESTED_WITH_KEY, X_REQUESTED_WITH_VAL);
            //   post.addHeader(X_CSRF_TOKEN_KEY,token);
            post.setEntity(getPayload(map, deepEntity));
            if (!Helpers.isNetworkAvailable(context)) {
                if(map.containsKey("PurchaseNum")){
                    orderId = map.get("PurchaseNum").toString();
                }
                else if(map.containsKey("OrderId")){
                    orderId = map.get("OrderId").toString();
                }
                //orderId = map.get("PurchaseNum").toString();
            } else {
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 201) {
                    Header[] headers = response.getAllHeaders();
                    HttpEntity r_entity = response.getEntity();
                    String jsonString = getJSONString(r_entity);
                    JSONObject jsonObj = new JSONObject(jsonString);
                    jsonObj = jsonObj.getJSONObject("d");
                    orderId = jsonObj.getString("OrderId");
                    Log.e("Posting", "" + jsonObj);
                    return orderId;
                } else {
                    Log.e("fail", "Fail" + response.getStatusLine().getStatusCode());
                    Log.e("Message", "Message" + response);
                    HttpEntity r_entity = response.getEntity();
                    String jsonString = getJSONString(r_entity);
                    JSONObject jsonObj = new JSONObject(jsonString);
                    jsonObj = jsonObj.getJSONObject("error").getJSONObject("message");
                    Log.e("Entity", "" + jsonObj);
                    orderId = "Error" + jsonObj.getString("value");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderId;
    }
    public static String postTrip(Context context, String collection, HashMap<String, String> map, JSONArray deepEntity, String purchaseNum) {
        String orderId = "";
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope("hello"), getCredentials("ecs", "sap123"));
            HttpPost post = new HttpPost(postUrl(collection));
            String authString = "ecs" + ":" + "sap123";
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            //post.addHeader("Authorization", "Basic " + new String(authEncBytes));
            post.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            post.addHeader(ACCEPT, APPLICATION_JSON);
            post.addHeader(X_REQUESTED_WITH_KEY, X_REQUESTED_WITH_VAL);
            //   post.addHeader(X_CSRF_TOKEN_KEY,token);
            post.setEntity(getPayload(map, deepEntity));
            if (!Helpers.isNetworkAvailable(context)) {
                orderId = purchaseNum;
            } else {
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 201) {
                    Header[] headers = response.getAllHeaders();
                    HttpEntity r_entity = response.getEntity();
                    String jsonString = getJSONString(r_entity);
                    JSONObject jsonObj = new JSONObject(jsonString);
                    jsonObj = jsonObj.getJSONObject("d");
                    orderId = jsonObj.getString("OrderId");
                    Log.e("Posting", "" + jsonObj);
                    return orderId;
                } else {
                    Log.e("fail", "Fail" + response.getStatusLine().getStatusCode());
                    Log.e("Message", "Message" + response);
                    HttpEntity r_entity = response.getEntity();
                    String jsonString = getJSONString(r_entity);
                    JSONObject jsonObj = new JSONObject(jsonString);
                    jsonObj = jsonObj.getJSONObject("error").getJSONObject("message");
                    Log.e("Entity", "" + jsonObj);
                    orderId = "Error" + jsonObj.getString("value");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderId;
    }
    public static String postOdometer(Context context, String collection, HashMap<String, String> map, JSONArray deepEntity, String purchaseNumber) {
        String flag = "";
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope("hello"), getCredentials("ecs", "sap123"));
            HttpPost post = new HttpPost(postUrlOdometer(collection));
            String authString = "ecs" + ":" + "sap123";
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
           // post.addHeader("Authorization", "Basic " + new String(authEncBytes));
            post.addHeader(CONTENT_TYPE, APPLICATION_JSON);
            post.addHeader(ACCEPT, APPLICATION_JSON);
            post.addHeader(X_REQUESTED_WITH_KEY, X_REQUESTED_WITH_VAL);
            //   post.addHeader(X_CSRF_TOKEN_KEY,token);
            post.setEntity(getPayload(map, deepEntity));
            if (!Helpers.isNetworkAvailable(context)) {
                flag = purchaseNumber;
            }
            else{
                HttpResponse response = client.execute(post);
                if (response.getStatusLine().getStatusCode() == 201) {
                    Header[] headers = response.getAllHeaders();
                    HttpEntity r_entity = response.getEntity();
                    String jsonString = getJSONString(r_entity);
                    JSONObject jsonObj = new JSONObject(jsonString);
                    jsonObj = jsonObj.getJSONObject("d");
                    flag = jsonObj.getString("Flag");
                    if (flag.equals("N")) {
                        flag = "Error" + jsonObj.getString("Message");
                    }
                    Log.e("Posting", "" + jsonObj);
                    return flag;
                } else {
                    Log.e("fail", "Fail" + response.getStatusLine().getStatusCode());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    public static ArrayList<String> RequestToken(Context context) {
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
                Log.e("Token", "" + tokenval);
                if (!tokenval.equals("")) {
                    mylist.add(tokenval);
                }
                return mylist;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mylist;
    }
    private static HttpEntity getPayload(HashMap<String, String> map, JSONArray deepEntity) throws IOException {
        String data = "";
        try {
            StringBuilder body = new StringBuilder();
            body.append(bodyBuilder(map));
            if (deepEntity.length() > 0) {
                body.append(",");
                body.append("\"" + App.DEEP_ENTITY + "\":[");
                for (int i = 0; i < deepEntity.length(); i++) {
                    body.append("{");
                    body.append(bodyBuilder(convertToMap(deepEntity.getJSONObject(i))));
                    body.append("}");
                    if (deepEntity.length() > 1 && i < deepEntity.length() - 1) {
                        body.append(",");
                    }
                }
                body.append("]");
            }
            Log.e("String Build", "" + body.toString());
            // data = String.format("{" + body.toString() + "}");
            data = "{" + body.toString() + "}";
            Log.e("POST Data", "" + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayEntity(data.getBytes());
    }
    private static HttpEntity getPayloadBatch(ArrayList<OfflinePost>arrayList) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        Log.e("ArrayList Length","" + arrayList.size());
        String data = "";
        try {
            StringBuilder body = new StringBuilder();
            int index=1;
            for (OfflinePost offlinePost:arrayList){
                Log.e("Map","" + offlinePost.getMap());
                Log.e("Deep","" + offlinePost.getDeepEntity());
                body.append("--batch");
                body.append("\n");
                body.append("Content-Type: multipart/mixed; boundary=changeset" + index);
                body.append("\n");
                body.append("\n");
                body.append("--changeset" + index);
                body.append("\n");
                body.append("Content-Type: application/http");
                body.append("\n");
                body.append("Content-Transfer-Encoding: binary");
                body.append("\n");
                body.append("\n");
                body.append("POST" + " " + offlinePost.getCollectionName() + " HTTP/1.1");
                body.append("\n");
                body.append("Accept: application/json");
                body.append("\n");
                body.append(CONTENT_TYPE + ":" + APPLICATION_JSON);
                body.append("\n");
                body.append("\n");
                Log.e("BodyBuild", "" + bodyBuilder(offlinePost.getMap()));
                body.append("{\"d\":{" + bodyBuilder(offlinePost.getMap()));
                if(offlinePost.getDeepEntity().length()>0){
                    JSONArray deepEntity = new JSONArray();
                    deepEntity = offlinePost.getDeepEntity();
                    body.append(",");
                    body.append("\"" + App.DEEP_ENTITY + "\":[");
                    for (int i = 0; i < deepEntity.length(); i++) {
                        body.append("{");
                        body.append(bodyBuilder(convertToMap(deepEntity.getJSONObject(i))));
                        body.append("}");
                        if (deepEntity.length() > 1 && i < deepEntity.length() - 1) {
                            body.append(",");
                        }
                    }
                    body.append("]");
                }
                body.append("}}");
                body.append("\n");
                body.append("--changeset"+index+"--");
                body.append("\n");
                body.append("\n");
                index++;
            }
            body.append("--batch--");
            Log.e("String Build", "" + body.toString());
            // data = String.format("{" + body.toString() + "}");
            data = body.toString();
           // data = "{" + body.toString() + "}";
            Log.e("POST Data", "" + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayEntity(data.getBytes());
    }
    private static HttpEntity getPayloadBatchOdometer(ArrayList<OfflinePost>arrayList) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        Log.e("ArrayList Length","" + arrayList.size());
        String data = "";
        try {
            StringBuilder body = new StringBuilder();
            int index=1;
            for (OfflinePost offlinePost:arrayList){
                Log.e("Map","" + offlinePost.getMap());
                Log.e("Deep","" + offlinePost.getDeepEntity());
                body.append("--batch");
                body.append("\n");
                body.append("Content-Type: multipart/mixed; boundary=changeset" + index);
                body.append("\n");
                body.append("\n");
                body.append("--changeset" + index);
                body.append("\n");
                body.append("Content-Type: application/http");
                body.append("\n");
                body.append("Content-Transfer-Encoding: binary");
                body.append("\n");
                body.append("\n");
                body.append("POST" + " " + offlinePost.getCollectionName() + " HTTP/1.1");
                body.append("\n");
                body.append("Accept: application/json");
                body.append("\n");
                body.append(CONTENT_TYPE + ":" + APPLICATION_JSON);
                body.append("\n");
                body.append("\n");
                Log.e("BodyBuild", "" + bodyBuilder(offlinePost.getMap()));
                body.append("{\"d\":{" + bodyBuilder(offlinePost.getMap()));
                body.append("}}");
                body.append("\n");
                body.append("--changeset"+index+"--");
                body.append("\n");
                body.append("\n");
                index++;
            }
            body.append("--batch--");
            Log.e("String Build", "" + body.toString());
            // data = String.format("{" + body.toString() + "}");
            data = body.toString();
            // data = "{" + body.toString() + "}";
            Log.e("POST Data", "" + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayEntity(data.getBytes());
    }
    private static HttpEntity getPayloadBatchCustomer(ArrayList<OfflinePost>arrayList) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        Log.e("ArrayList Length","" + arrayList.size());
        String data = "";
        try {
            StringBuilder body = new StringBuilder();
            int index=1;
            for (OfflinePost offlinePost:arrayList){
                Log.e("Map","" + offlinePost.getMap());
                Log.e("Deep","" + offlinePost.getDeepEntity());
                body.append("--batch");
                body.append("\n");
                body.append("Content-Type: multipart/mixed; boundary=changeset" + index);
                body.append("\n");
                body.append("\n");
                body.append("--changeset" + index);
                body.append("\n");
                body.append("Content-Type: application/http");
                body.append("\n");
                body.append("Content-Transfer-Encoding: binary");
                body.append("\n");
                body.append("\n");
                body.append("POST" + " " + offlinePost.getCollectionName() + " HTTP/1.1");
                body.append("\n");
                body.append("Accept: application/json");
                body.append("\n");
                body.append(CONTENT_TYPE + ":" + APPLICATION_JSON);
                body.append("\n");
                body.append("\n");
                Log.e("BodyBuild", "" + bodyBuilderBatch(offlinePost.getMap()));
                body.append("{\"d\":{" + bodyBuilderBatch(offlinePost.getMap()));
                body.append("}}");
                body.append("\n");
                body.append("--changeset"+index+"--");
                body.append("\n");
                body.append("\n");
                index++;
            }
            body.append("--batch--");
            Log.e("String Build", "" + body.toString());
            // data = String.format("{" + body.toString() + "}");
            data = body.toString();
            // data = "{" + body.toString() + "}";
            Log.e("POST Data", "" + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayEntity(data.getBytes());
    }
    public static HashMap<String, String> convertToMap(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = UrlBuilder.decodeString(object.getString(key));
            map.put(key, value);
        }
        return map;
    }
    public static String bodyBuilder(HashMap<String, String> hashMap) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            /*value = UrlBuilder.clean(value);
            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            list.add("\"" + entry.getKey() + "\"" + ":\"" + value + "\"");
        }
        return TextUtils.join(",", list);
    }
    public static String bodyBuilderBatch(HashMap<String, String> hashMap) {
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();
            /*//value = UrlBuilder.clean(value);
            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            list.add("\"" + entry.getKey() + "\"" + ":\"" + UrlBuilder.decodeString(value) + "\"");
        }
        return TextUtils.join(",", list);
    }
    public static ArrayList<OfflineResponse> batchRequest(Context context,String collectionName,ArrayList<OfflinePost>arrayList){
        ArrayList<OfflineResponse> data = new ArrayList<>();
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope("hello"), getCredentials("ecs", "sap123"));
            HttpPost post = new HttpPost(postUrlBatch());
            String authString = "ecs" + ":" + "sap123";
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
           // post.addHeader("Authorization", "Basic " + new String(authEncBytes));
            post.addHeader(CONTENT_TYPE, APPLICATION_BATCH);
            post.addHeader(ACCEPT, APPLICATION_JSON);
            post.addHeader(X_REQUESTED_WITH_KEY, X_REQUESTED_WITH_VAL);
            //   post.addHeader(X_CSRF_TOKEN_KEY,token);
            post.setEntity(getPayloadBatch(arrayList));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 201||response.getStatusLine().getStatusCode() == 202) {
                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                Log.e("Response JSON", jsonString);
                data = unpack(jsonString,response);
               // JSONObject jsonObj = new JSONObject(jsonString);
            } else {
                Log.e("fail", "Fail" + response.getStatusLine().getStatusCode());
                Log.e("Message", "Message" + response);
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                Log.e("Error JSON",jsonString);
                JSONObject jsonObj = new JSONObject(jsonString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public static ArrayList<OfflineResponse> batchRequestOdometer(Context context,String collectionName,ArrayList<OfflinePost>arrayList){
        ArrayList<OfflineResponse> data = new ArrayList<>();
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope("hello"), getCredentials("ecs", "sap123"));
            HttpPost post = new HttpPost(postUrlBatchOdometer());
            String authString = "ecs" + ":" + "sap123";
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
           // post.addHeader("Authorization", "Basic " + new String(authEncBytes));
            post.addHeader(CONTENT_TYPE, APPLICATION_BATCH);
            post.addHeader(ACCEPT, APPLICATION_JSON);
            post.addHeader(X_REQUESTED_WITH_KEY, X_REQUESTED_WITH_VAL);
            //   post.addHeader(X_CSRF_TOKEN_KEY,token);
            post.setEntity(getPayloadBatchOdometer(arrayList));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 201||response.getStatusLine().getStatusCode() == 202) {
                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                data = unpackOdometer(jsonString, response);
                // JSONObject jsonObj = new JSONObject(jsonString);
            } else {
                Log.e("fail", "Fail" + response.getStatusLine().getStatusCode());
                Log.e("Message", "Message" + response);
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                JSONObject jsonObj = new JSONObject(jsonString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public static ArrayList<OfflineResponse> batchRequestCustomer(Context context,String collectionName,ArrayList<OfflinePost>arrayList){
        ArrayList<OfflineResponse> data = new ArrayList<>();
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope("hello"), getCredentials("ecs", "sap123"));
            HttpPost post = new HttpPost(postUrlBatchOdometer());
            String authString = "ecs" + ":" + "sap123";
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            // post.addHeader("Authorization", "Basic " + new String(authEncBytes));
            post.addHeader(CONTENT_TYPE, APPLICATION_BATCH);
            post.addHeader(ACCEPT, APPLICATION_JSON);
            post.addHeader(X_REQUESTED_WITH_KEY, X_REQUESTED_WITH_VAL);
            //   post.addHeader(X_CSRF_TOKEN_KEY,token);
            post.setEntity(getPayloadBatchCustomer(arrayList));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 201||response.getStatusLine().getStatusCode() == 202) {
                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                data = unpackCustomer(jsonString, response);
                // JSONObject jsonObj = new JSONObject(jsonString);
            } else {
                Log.e("fail", "Fail" + response.getStatusLine().getStatusCode());
                Log.e("Message", "Message" + response);
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                JSONObject jsonObj = new JSONObject(jsonString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ArrayList<OfflineResponse> batchRequestBeginDay(Context context,String collectionName,ArrayList<OfflinePost>arrayList){
        ArrayList<OfflineResponse> data = new ArrayList<>();
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            client.getCredentialsProvider().setCredentials(getAuthScope("hello"), getCredentials("ecs", "sap123"));
            HttpPost post = new HttpPost(postUrlBeginDayBatch());
            String authString = "ecs" + ":" + "sap123";
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            // post.addHeader("Authorization", "Basic " + new String(authEncBytes));
            post.addHeader(CONTENT_TYPE, APPLICATION_BATCH);
            post.addHeader(ACCEPT, APPLICATION_JSON);
            post.addHeader(X_REQUESTED_WITH_KEY, X_REQUESTED_WITH_VAL);
            //   post.addHeader(X_CSRF_TOKEN_KEY,token);
            post.setEntity(getPayloadBatch(arrayList));
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 201||response.getStatusLine().getStatusCode() == 202) {
                Header[] headers = response.getAllHeaders();
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                data = unpack(jsonString, response);
                // JSONObject jsonObj = new JSONObject(jsonString);
            } else {
                Log.e("fail", "Fail" + response.getStatusLine().getStatusCode());
                Log.e("Message", "Message" + response);
                HttpEntity r_entity = response.getEntity();
                String jsonString = getJSONString(r_entity);
                JSONObject jsonObj = new JSONObject(jsonString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static ArrayList<OfflineResponse> unpack(String response,HttpResponse httpResponse){

        String lines[] = response.split("\\r?\\n");
        String boundary = lines[0];

        JSONArray data = new JSONArray();
        JSONObject d = new JSONObject();
        ArrayList<OfflineResponse> arrayList = new ArrayList<>();
        OfflineResponse offlineResponse = new OfflineResponse();
        try{
            for(int i=0;i<lines.length;i++){
                if(lines[i].contains(boundary)){
                    String dataStr = lines[i];
                    try {
                        Object json = new JSONTokener(dataStr).nextValue();
                        if(json instanceof JSONObject){
                            data.put(json);
                        }
                        else if(json instanceof JSONArray){

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else{

                    String dataStr = lines[i];
                    if(dataStr.startsWith("HTTP")){
                        dataStr = dataStr.replaceAll("HTTP/1.1","").trim();
                        dataStr = dataStr.substring(0, 3);
                        offlineResponse.setResponse_code(dataStr);
                    }
                    else if(dataStr.startsWith("{")){
                        Object json = new JSONTokener(dataStr).nextValue();
                        if(json instanceof JSONObject){
                            //JSONObject jsonObject = ((JSONObject) json).getJSONObject("d");
                            JSONObject jsonObject = null;
                            boolean checkData = ((JSONObject) json).has("d");
                            if(checkData){
                                jsonObject = ((JSONObject) json).getJSONObject("d");
                            }
                            boolean checkError = ((JSONObject) json).has("error");
                            JSONObject jsonError = null;
                            if(checkError){
                                jsonError = ((JSONObject)json).getJSONObject("error");
                            }
                            if(!(jsonObject==null)){
                                if(jsonObject.getString("Function").equals(ConfigStore.LoadRequestFunction)){
                                    if(jsonObject.getString("CustomerId").equals(Settings.getString(App.DRIVER))){
                                        offlineResponse.setFunction(jsonObject.getString("Function"));
                                        offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                        offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                        Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                        offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                        arrayList.add(offlineResponse);
                                    }
                                    else{
                                        offlineResponse.setFunction(jsonObject.getString("Function")+"O");
                                        offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                        offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                        Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                        offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                        arrayList.add(offlineResponse);
                                    }
                                }
                                if(jsonObject.getString("Function").equals(ConfigStore.BeginDayFunction)){
                                    if(jsonObject.getString("CreatedBy").equals(Settings.getString(App.DRIVER))){
                                        offlineResponse.setFunction(jsonObject.getString("Function"));
                                        offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                        offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                        Log.e("Response Order","" + jsonObject.getString("OrderId") + jsonObject.getString("PurchaseNum"));
                                        offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                        arrayList.add(offlineResponse);
                                    }
                                }
                                if(jsonObject.getString("Function").equals(ConfigStore.LoadConfirmationFunction)){
                                    if(jsonObject.getString("CustomerId").equals(Settings.getString(App.DRIVER))){
                                        offlineResponse.setFunction(jsonObject.getString("Function"));
                                        offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                        offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                        Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                        offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                        arrayList.add(offlineResponse);
                                    }
                                }
                                if(jsonObject.getString("Function").equals(ConfigStore.LoadVarianceFunction)){
                                    if(offlineResponse.getResponse_code().equals("201")){
                                        if(jsonObject.getString("CustomerId").equals(Settings.getString(App.DRIVER))){
                                            if(jsonObject.getString("DocumentType").equals(ConfigStore.LoadVarianceDebit)){
                                                if(jsonObject.getString("PurchaseNum").equals("")){
                                                    offlineResponse.setFunction(jsonObject.getString("Function")+"U");
                                                }
                                                else{
                                                    offlineResponse.setFunction(jsonObject.getString("Function")+"D");
                                                }
                                                offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                                offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                                Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                                offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                                arrayList.add(offlineResponse);
                                            }
                                            if(jsonObject.getString("DocumentType").equals(ConfigStore.LoadVarianceCredit)){
                                                if(jsonObject.getString("PurchaseNum").equals("")){
                                                    offlineResponse.setFunction(jsonObject.getString("Function")+"U");
                                                }
                                                else{
                                                    offlineResponse.setFunction(jsonObject.getString("Function")+"D");
                                                }
                                                offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                                offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                                Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                                offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                                arrayList.add(offlineResponse);
                                            }

                                            if(jsonObject.getString("DocumentType").equals(ConfigStore.EndingInventory)){
                                                if(jsonObject.getString("PurchaseNum").equals("")){
                                                    offlineResponse.setFunction(jsonObject.getString("Function")+"U");
                                                }
                                                else{
                                                    offlineResponse.setFunction(jsonObject.getString("Function")+"D");
                                                }
                                                offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                                offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                                Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                                offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                                arrayList.add(offlineResponse);
                                            }
                                        }
                                    }
                                    /*if(jsonObject.getString("CustomerId").equals(Settings.getString(App.DRIVER))){
                                        offlineResponse.setFunction(jsonObject.getString("Function"));
                                        offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                        offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                        Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                        offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                        arrayList.add(offlineResponse);
                                    }*/
                                }
                                if(jsonObject.getString("Function").equals(ConfigStore.CustomerDeliveryRequestFunction)){
                                    if(offlineResponse.getResponse_code().equals("201")){
                                        offlineResponse.setFunction(jsonObject.getString("Function"));
                                        offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                        offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                        Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                        offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                        arrayList.add(offlineResponse);
                                    }
                                }
                                if(jsonObject.getString("Function").equals(ConfigStore.CustomerDeliveryDeleteRequestFunction)){
                                    if(offlineResponse.getResponse_code().equals("201")){
                                        offlineResponse.setFunction(jsonObject.getString("Function"));
                                        offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                        offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                        Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                        offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                        arrayList.add(offlineResponse);
                                    }
                                }
                                if(jsonObject.getString("Function").equals(ConfigStore.ReturnsFunction)){
                                    if(offlineResponse.getResponse_code().equals("201")){
                                        if(jsonObject.getString("DocumentType").equals(ConfigStore.GoodReturn)){
                                            offlineResponse.setFunction(jsonObject.getString("Function")+"G");
                                            offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                            offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                            Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                            offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                            arrayList.add(offlineResponse);
                                        }
                                        if(jsonObject.getString("DocumentType").equals(ConfigStore.BadReturn)){
                                            offlineResponse.setFunction(jsonObject.getString("Function")+"B");
                                            offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                            offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                            Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                            offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                            arrayList.add(offlineResponse);
                                        }

                                    }
                                }
                                if(jsonObject.getString("Function").equals(ConfigStore.VisitListFunction)){
                                    offlineResponse.setFunction(jsonObject.getString("Function"));
                                    offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                    offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                    Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                    offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                    arrayList.add(offlineResponse);
                                }
                                if(jsonObject.getString("Function").equals(ConfigStore.InvoiceRequestFunction)){
                                    offlineResponse.setFunction(jsonObject.getString("Function"));
                                    offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                    offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                    Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                    offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                    arrayList.add(offlineResponse);
                                }
                                if(jsonObject.getString("Function").equals(ConfigStore.CollectionFunction)){
                                    offlineResponse.setFunction(jsonObject.getString("Function"));
                                    offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                                    offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                                    Log.e("Response Order","" + jsonObject.getString("OrderId"));
                                    offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                                    arrayList.add(offlineResponse);
                                }

                            /*offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                            offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                            Log.e("Response Order","" + jsonObject.getString("OrderId"));
                            offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                            arrayList.add(offlineResponse);*/
                                offlineResponse = new OfflineResponse();
                                data.put(json);
                            }
                            else if(!(jsonError==null)){
                                offlineResponse.setCustomerID("0000000");
                                offlineResponse.setOrderID("00000000");
                                offlineResponse.setPurchaseNumber("00000000");
                                arrayList.add(offlineResponse);
                                offlineResponse = new OfflineResponse();
                                data.put(json);
                            }

                        }
                        else if(json instanceof JSONArray){

                        }
                    }

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.e("JSON Array","" + data);
        return arrayList;
    }
    public static ArrayList<OfflineResponse> unpackOdometer(String response,HttpResponse httpResponse){

        String lines[] = response.split("\\r?\\n");
        String boundary = lines[0];

        JSONArray data = new JSONArray();
        JSONObject d = new JSONObject();
        ArrayList<OfflineResponse> arrayList = new ArrayList<>();
        OfflineResponse offlineResponse = new OfflineResponse();
        try{
            for(int i=0;i<lines.length;i++){
                if(lines[i].contains(boundary)){
                    String dataStr = lines[i];
                    try {
                        Object json = new JSONTokener(dataStr).nextValue();
                        if(json instanceof JSONObject){
                            data.put(json);
                        }
                        else if(json instanceof JSONArray){

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else{

                    String dataStr = lines[i];
                    if(dataStr.startsWith("HTTP")){
                        dataStr = dataStr.replaceAll("HTTP/1.1","").trim();
                        dataStr = dataStr.substring(0, 3);
                        offlineResponse.setResponse_code(dataStr);
                    }
                    else if(dataStr.startsWith("{")){
                        Object json = new JSONTokener(dataStr).nextValue();
                        if(json instanceof JSONObject){
                            JSONObject jsonObject = ((JSONObject) json).getJSONObject("d");
                            offlineResponse.setFunction(ConfigStore.OdometerFunction);
                            offlineResponse.setCustomerID(jsonObject.getString("Flag"));
                            offlineResponse.setPurchaseNumber(jsonObject.getString("TripID"));
                            offlineResponse.setOrderID(jsonObject.getString("Message"));
                            arrayList.add(offlineResponse);
                            /*offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                            offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                            Log.e("Response Order","" + jsonObject.getString("OrderId"));
                            offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                            arrayList.add(offlineResponse);*/
                            offlineResponse = new OfflineResponse();
                            data.put(json);
                        }
                        else if(json instanceof JSONArray){

                        }
                    }

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.e("JSON Array","" + data);
        return arrayList;
    }
    public static ArrayList<OfflineResponse> unpackCustomer(String response,HttpResponse httpResponse){

        String lines[] = response.split("\\r?\\n");
        String boundary = lines[0];

        JSONArray data = new JSONArray();
        JSONObject d = new JSONObject();
        ArrayList<OfflineResponse> arrayList = new ArrayList<>();
        OfflineResponse offlineResponse = new OfflineResponse();
        try{
            for(int i=0;i<lines.length;i++){
                if(lines[i].contains(boundary)){
                    String dataStr = lines[i];
                    try {
                        Object json = new JSONTokener(dataStr).nextValue();
                        if(json instanceof JSONObject){
                            data.put(json);
                        }
                        else if(json instanceof JSONArray){

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else{

                    String dataStr = lines[i];
                    if(dataStr.startsWith("HTTP")){
                        dataStr = dataStr.replaceAll("HTTP/1.1","").trim();
                        dataStr = dataStr.substring(0, 3);
                        offlineResponse.setResponse_code(dataStr);
                    }
                    else if(dataStr.startsWith("{")){
                        Object json = new JSONTokener(dataStr).nextValue();
                        if(json instanceof JSONObject){
                            JSONObject jsonObject = ((JSONObject) json).getJSONObject("d");
                            offlineResponse.setFunction(ConfigStore.AddCustomerFunction);
                            offlineResponse.setCustomerID(jsonObject.getString("Kunnr"));
                            offlineResponse.setPurchaseNumber(jsonObject.getString("Message"));
                            offlineResponse.setOrderID(jsonObject.getString("Comments"));
                            arrayList.add(offlineResponse);
                            /*offlineResponse.setCustomerID(jsonObject.getString("CustomerId"));
                            offlineResponse.setOrderID(jsonObject.getString("OrderId"));
                            Log.e("Response Order","" + jsonObject.getString("OrderId"));
                            offlineResponse.setPurchaseNumber(jsonObject.getString("PurchaseNum"));
                            arrayList.add(offlineResponse);*/
                            offlineResponse = new OfflineResponse();
                            data.put(json);
                        }
                        else if(json instanceof JSONArray){

                        }
                    }

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Log.e("JSON Array","" + data);
        return arrayList;
    }
}
