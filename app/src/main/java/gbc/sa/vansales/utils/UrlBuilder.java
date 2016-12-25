package gbc.sa.vansales.utils;
/**
 * Created by Rakshit on 14-Dec-16.
 */

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UrlBuilder {
    public static String build(String collection) {
        return build(collection, null, null);
    }

    public static String build(String collection, HashMap<String, String> parameters) {
        return build(collection, parameters, null, false);
    }

    public static String build(String collection, HashMap<String, String> parameters, HashMap<String, String> filters) {
        return build(collection, parameters, filters, false);
    }

    public static String build(String collection, HashMap<String, String> parameters, HashMap<String, String> filters, boolean isOr) {
        String url = collection;

        if (parameters != null && parameters.size() > 0) {
            url += paramsBuilder(parameters);
        }

        if (filters != null && filters.size() > 0) {
            url += "?" + filtersBuilder(filters, isOr);
        }

        return url;
    }

    public static String buildExpansion(String collection, HashMap<String, String> parameters, HashMap<String, String> expansion) {
        String url = collection;

        if (parameters != null && parameters.size() > 0) {
            url += "?" + expansionBuilder(parameters, expansion);
        }

        return url;
    }



    public static String paramsBuilder(HashMap<String, String> hashMap) {
        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();

            value = UrlBuilder.clean(value);

            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            list.add(entry.getKey() + "='" + value + "'");
        }

        return "(" + TextUtils.join(",", list) + ")";
    }

    public static String filtersBuilder(HashMap<String, String> hashMap, boolean isOr) {
        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry entry : hashMap.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();

            value = UrlBuilder.clean(value);

            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            list.add(entry.getKey() + "%20eq%20'" + value + "'");
        }

        return "$filter=" + TextUtils.join("%20" + (isOr ? "or" : "and") + "%20", list);
    }

    public static String expansionBuilder(HashMap<String, String> parameters, HashMap<String,String>expansionSets){

        boolean isOr = false;
        ArrayList<String> paramList = new ArrayList<>();
        ArrayList<String> expansionList = new ArrayList<>();
        for (Map.Entry entry : parameters.entrySet()) {
            String value = entry.getValue() == null ? null : entry.getValue().toString();

            value = UrlBuilder.clean(value);

            try {
                value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            paramList.add(entry.getKey() + "%20eq%20'" + value + "'");
        }
        if(!expansionSets.isEmpty()){
            for (Map.Entry entry : expansionSets.entrySet()) {
                String value = entry.getValue() == null ? null : entry.getValue().toString();

                value = UrlBuilder.clean(value);

                try {
                    value = URLEncoder.encode(value, ConfigStore.CHARSET).replace("+", "%20").replace("%3A", ":");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                expansionList.add(value);
            }
        }

        if(!expansionList.isEmpty()){
            Log.e("ExpansionList Size","" + expansionList.size());
            boolean listSize = (expansionList.size()>1);
            Log.e("Boolean","" + listSize);
            String url = "$filter=" + TextUtils.join("%20" + (isOr ? "or" : "and") + "%20", paramList) + "&$expand=" + TextUtils.join((listSize ? "," : "" ),expansionList);
            Log.e("URL for Expansion","" + url);
            return url;
        }
        else{
            String url = "$filter=" + TextUtils.join("%20" + (isOr ? "or" : "and") + "%20", paramList);
            Log.e("URL W/o Expansion","" + url);
            return url;
        }

    }

    public static String clean(String data) {
        if (data == null) return "";

        data = data.replaceAll("([^A-Za-z0-9&: \\-\\.,_\\?\\*]*)", "");

        data = data.replaceAll("([ ]+)", " ");
        return data;
    }

    public static String decodeString(String data){
        if(data == null) return "";
        try {
            data = URLDecoder.decode(data,ConfigStore.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }
}
