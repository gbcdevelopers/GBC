package gbc.sa.vansales.utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gbc.sa.vansales.App;
import gbc.sa.vansales.data.ArticleHeaders;
import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.models.LoadSummary;
/**
 * Created by Rakshit on 07-Feb-17.
 */
public class PrinterDataHelper {
    public static HashMap<String,String>header = new HashMap<>();
    public static HashMap<String,String>data = new HashMap<>();
    public static HashMap<String,String>footer = new HashMap<>();
    public JSONArray jArr = new JSONArray();
    public static String HEADERS = "HEADERS";
    public ArrayList<ArticleHeader> articles = new ArrayList<>();
    public static String DATA= "data";
    public static String MAINARR = "mainArr";

    void PrinterDataHelper(){
        articles = ArticleHeaders.get();
    }

    public JSONArray createJSONData(String request,HashMap<String,String>headerData,ArrayList<LoadSummary> data,HashMap<String,String>footerData){
        try{
            JSONArray jInter = new JSONArray();
            JSONObject jDict = new JSONObject();
            jDict.put(App.REQUEST,request);
            JSONObject mainArr = new JSONObject();
            //Creating Header Array
            for (Map.Entry entry : headerData.entrySet()) {
                String value = entry.getValue() == null ? null : entry.getValue().toString();
                String key = entry.getKey().toString();
                mainArr.put(key,value);
            }
            JSONArray headers = new JSONArray();
            headers = headerData(request);
            mainArr.put(HEADERS,headers);
            JSONArray footers = new JSONArray();

            JSONArray jData = new JSONArray();
            for(Object obj: data){
                JSONArray jDataInner = new JSONArray();
                if(obj instanceof LoadSummary){
                    ArticleHeader articleHeader = ArticleHeader.getArticle(articles,((LoadSummary) obj).getMaterialNo());
                    if(articleHeader!=null){
                        jDataInner.put(((LoadSummary) obj).getItemCode());  //Item Code
                        jDataInner.put(UrlBuilder.decodeString(articleHeader.getMaterialDesc1()));  //Item Description English
                        jDataInner.put(articleHeader.getMaterialDesc2()); //Item Description Arabic
                        jDataInner.put("1"); //UPC
                        jDataInner.put(((LoadSummary) obj).getQuantityCases()); //Cases
                        jDataInner.put(0); //Variance
                        jDataInner.put(((LoadSummary) obj).getQuantityCases()); //Net Load
                        jDataInner.put(((LoadSummary) obj).getPrice());
                        jDataInner.put(String.valueOf(Double.parseDouble(((LoadSummary) obj).getQuantityCases())*Double.parseDouble(((LoadSummary) obj).getPrice())));
                    }
                }
                jData.put(jDataInner);
            }
            mainArr.put(DATA,jData);
            jDict.put(MAINARR,mainArr);
            jInter.put(jDict);
            jArr.put(jInter);
            jArr.put(headers);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return jArr;
    }

    public JSONArray headerData(String request){
        JSONArray headers = new JSONArray();
        switch (request){
            case App.LOAD_SUMMARY_REQUEST:{
                /*These are the header tables for the table*/
                headers.put("ITEM#");
                headers.put("ENGLISH DESCRIPTION");
                headers.put("ARABIC DESCRIPTION");
                headers.put("UPC ");
                headers.put("BEGIN INV");
                headers.put("LOAD");
                headers.put("ADJUST");
                headers.put("NET LOAD");
                headers.put("VALUE");
                break;
            }
            default:{
                break;
            }
        }
        return headers;
    }

    public static String clean(String data) {
        if (data == null) return "";
        data = data.replaceAll("([^A-Za-z0-9&: \\-\\.,_\\?\\*]*)", "");
        data = data.replaceAll("([ ]+)", " ");
        return data;
    }
}
