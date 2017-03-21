package gbc.sa.vansales.utils;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gbc.sa.vansales.App;
import gbc.sa.vansales.activities.LoginActivity;
import gbc.sa.vansales.sap.IntegrationService;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class DownloadData extends AsyncTask<Void, Void, Void>{

    private HashMap<String,String> params;
    private String collectionName;
    private HashMap<String,String>expansions;
    private DatabaseHandler db;
    private Context context;

    public DownloadData(Context context,String collectionName,HashMap<String, String> parameters, HashMap<String, String> expansion, DatabaseHandler db) {
        this.params = parameters;
        this.collectionName = collectionName;
        this.expansions = expansion;
        this.db = db;
        this.context = context;
        execute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        if(this.collectionName.equals("FOCSet")){
            generateData(db);
        }
        else{
            String url = UrlBuilder.buildExpansion(this.collectionName, this.params, this.expansions);
            JSONArray jsonArray = IntegrationService.getService(this.context,url);
            Log.e("Exp Response", "" + jsonArray);

            try {
                Log.e("Metadata", "" + jsonArray.getJSONObject(0).getJSONObject("__metadata").getString("type"));
                Helpers.logData(this.context, jsonArray.getJSONObject(0).getJSONObject("__metadata").getString("type"));
                String metadata = jsonArray.getJSONObject(0).getJSONObject("__metadata").getString("type");
                parseJSON(metadata,jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {

    }
    void parseJSON(String metadata,JSONArray jsonArray) throws JSONException {
        switch (metadata){
            case ConfigStore.TripHeaderEntity:
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject headerObj = jsonArray.getJSONObject(i);
                    JSONArray tripSalesArea = headerObj.getJSONObject("TripSalesArea").getJSONArray("results");

                    HashMap<String, String> headerParams = new HashMap<>();
                    headerParams.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                    headerParams.put(db.KEY_VISITLISTID  ,headerObj.get("Vlid").toString());
                    headerParams.put(db.KEY_ROUTE ,headerObj.get("Route").toString());
                    Settings.setString(App.ROUTE, headerObj.get("Route").toString());
                    headerParams.put(db.KEY_DRIVER, headerObj.get("Driver1").toString());
                    Settings.setString(App.DRIVER, headerObj.get("Driver1").toString());
                    headerParams.put(db.KEY_TRUCK, headerObj.get("Truck").toString());
                    headerParams.put(db.KEY_PS_DATE, Helpers.formatDate(Helpers.formatDate(headerObj.get("Psdate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_AS_DATE, Helpers.formatDate(Helpers.formatDate(headerObj.get("Asdate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_TOUR_TYPE ,headerObj.get("TourType").toString());
                    headerParams.put(db.KEY_CREATED_TIME ,headerObj.get("Ctime").toString());
                    headerParams.put(db.KEY_CREATED_BY ,headerObj.get("CreatedBy").toString());
                    headerParams.put(db.KEY_SETTLED_BY ,headerObj.get("SettledBy").toString());
                    headerParams.put(db.KEY_DOWN_STATUS ,headerObj.get("DownStatus").toString());
                    headerParams.put(db.KEY_UP_STATUS ,headerObj.get("UpStatus").toString());
                    headerParams.put(db.KEY_LOADS  ,headerObj.get("Loads").toString());
                    Settings.setString(App.DRIVER_NAME_AR, headerObj.get("Name_AR").toString());
                    Settings.setString(App.DRIVER_NAME_EN,headerObj.get("Name").toString());

                    Settings.setString(App.LANGUAGE,"en");

                    db.addData(db.TRIP_HEADER, headerParams);

                    for(int j=0;j<tripSalesArea.length();j++){
                        JSONObject object = tripSalesArea.getJSONObject(j);
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_TRIP_ID,object.get("TripId").toString());
                        params.put(db.KEY_VISITLISTID,headerObj.get("Vlid").toString());
                        params.put(db.KEY_DATE, Helpers.formatDate(Helpers.formatDate(object.get("IDate").toString()), App.DATE_FORMAT));
                        params.put(db.KEY_START_DATE, Helpers.formatDate(Helpers.formatDate(object.get("PstartDate").toString()), App.DATE_FORMAT));
                        params.put(db.KEY_START_TIME  ,object.get("PstartTime").toString());
                        params.put(db.KEY_SALES_ORG  ,object.get("SalesOrg").toString());
                        Settings.setString(App.SALES_ORG, object.getString("SalesOrg").toString());
                        params.put(db.KEY_DIST_CHANNEL, object.get("DistChannel").toString());
                        Settings.setString(App.DIST_CHANNEL, object.getString("DistChannel").toString());
                        params.put(db.KEY_DIVISION, object.get("Division").toString());
                        Settings.setString(App.DIVISION, object.getString("Division").toString());

                        db.addData(db.TRIP_SALES_AREA,params);
                    }
                }

                break;
            case ConfigStore.LoadDeliveryEntity:
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject headerObj = jsonArray.getJSONObject(i);
                    JSONArray loadItems = headerObj.getJSONObject("LoadDelItems").getJSONArray("results");

                    HashMap<String, String> headerParams = new HashMap<>();
                    headerParams.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                    headerParams.put(db.KEY_DELIVERY_NO  ,headerObj.get("DeliveryNo").toString());
                    headerParams.put(db.KEY_CREATED_BY ,headerObj.get("CreatedBy").toString());
                    headerParams.put(db.KEY_CREATED_TIME,headerObj.get("EntryTime").toString());
                    headerParams.put(db.KEY_SALES_DIST, headerObj.get("SalesDist").toString());
                    headerParams.put(db.KEY_DATE, Helpers.formatDate(Helpers.formatDate(headerObj.get("Creationdate").toString()), App.DATE_FORMAT));
                    //headerParams.put(db.KEY_AS_DATE, Helpers.formatDate(Helpers.formatDate(headerObj.get("Asdate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_SHIPPING_PT  ,headerObj.get("ShippingPt").toString());
                    headerParams.put(db.KEY_SALES_ORG  ,headerObj.get("SalesOrg").toString());
                    headerParams.put(db.KEY_DELIVERY_TYPE  ,headerObj.get("Delvtype").toString());
                    headerParams.put(db.KEY_DELIVERY_DEFN  ,headerObj.get("DelvDefin").toString());
                    headerParams.put(db.KEY_ORDER_COMB  ,headerObj.get("OrderComb").toString());
                    headerParams.put(db.KEY_GOODS_MOVEMENT_DATE  ,Helpers.formatDate(Helpers.formatDate(headerObj.get("GoodsMvtdate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_LOADING_DATE   ,Helpers.formatDate(Helpers.formatDate(headerObj.get("LoadingDat").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_TRANSPLANT_DATE  ,Helpers.formatDate(Helpers.formatDate(headerObj.get("TransplDate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_DELIVERY_DATE  ,Helpers.formatDate(Helpers.formatDate(headerObj.get("DelvDate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_PICKING_DATE   ,Helpers.formatDate(Helpers.formatDate(headerObj.get("PickingDae").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_UNLOAD_POINT   ,headerObj.get("UnloadPt").toString());
                    headerParams.put(db.KEY_IS_VERIFIED, "false");
                    db.addData(db.LOAD_DELIVERY_HEADER, headerParams);

                    for(int j=0;j<loadItems.length();j++){
                        JSONObject object = loadItems.getJSONObject(j);
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_DELIVERY_NO,object.get("DeliveryNo").toString());
                        params.put(db.KEY_ORDER_ID,object.get("OrderId").toString());
                        params.put(db.KEY_ITEM_NO,object.get("Itemno").toString());
                        params.put(db.KEY_ITEM_CATEGORY, object.get("DelvItmCat").toString());
                        params.put(db.KEY_CREATED_BY, object.get("CreatedBy").toString());
                        params.put(db.KEY_ENTRY_TIME  ,object.get("EntryTime").toString());
                        params.put(db.KEY_DATE   ,Helpers.formatDate(Helpers.formatDate(object.get("CreationDat").toString()), App.DATE_FORMAT));
                        params.put(db.KEY_MATERIAL_NO   ,object.get("MaterialNo").toString());
                        params.put(db.KEY_MATERIAL_ENTERED   ,object.get("MaterialEntered").toString());
                        params.put(db.KEY_MATERIAL_GROUP ,object.get("MatGroup").toString());
                        params.put(db.KEY_PLANT ,object.get("Plant").toString());
                        params.put(db.KEY_STORAGE_LOCATION , object.get("StorLocation").toString());
                        params.put(db.KEY_BATCH , object.get("Batch").toString());
                        params.put(db.KEY_ACTUAL_QTY,object.get("ActQtyDel").toString());
                        params.put(db.KEY_REMAINING_QTY,object.get("ActQtyDel").toString());
                        params.put(db.KEY_UOM ,object.get("Uom").toString());
                        params.put(db.KEY_DIST_CHANNEL ,object.get("DistCha").toString());
                        params.put(db.KEY_DIVISION ,object.get("Division").toString());
                        params.put(db.KEY_IS_VERIFIED,"false");
                        db.addData(db.LOAD_DELIVERY_ITEMS,params);
                    }

                }
                break;

            case ConfigStore.CustomerDeliverHeaderEntity:
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject headerObj = jsonArray.getJSONObject(i);
                    JSONArray loadItems = headerObj.getJSONObject("CustDelItems").getJSONArray("results");

                    HashMap<String, String> headerParams = new HashMap<>();
                    headerParams.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                    headerParams.put(db.KEY_DELIVERY_NO  ,headerObj.get("DeliveryNo").toString());
                    headerParams.put(db.KEY_CREATED_BY ,headerObj.get("CreatedBy").toString());
                    headerParams.put(db.KEY_CREATED_TIME,headerObj.get("EntryTime").toString());
                    headerParams.put(db.KEY_SALES_DIST, headerObj.get("SalesDist").toString());
                    headerParams.put(db.KEY_DATE, Helpers.formatDate(Helpers.formatDate(headerObj.get("Creationdate").toString()), App.DATE_FORMAT));
                    //headerParams.put(db.KEY_AS_DATE, Helpers.formatDate(Helpers.formatDate(headerObj.get("Asdate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_SHIPPING_PT  ,headerObj.get("ShippingPt").toString());
                    headerParams.put(db.KEY_SALES_ORG  ,headerObj.get("SalesOrg").toString());
                    headerParams.put(db.KEY_DELIVERY_TYPE  ,headerObj.get("Delvtype").toString());
                    headerParams.put(db.KEY_DELIVERY_DEFN  ,headerObj.get("DelvDefin").toString());
                    headerParams.put(db.KEY_ORDER_COMB  ,headerObj.get("OrderComb").toString());
                    headerParams.put(db.KEY_GOODS_MOVEMENT_DATE  ,Helpers.formatDate(Helpers.formatDate(headerObj.get("GoodsMvtdate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_LOADING_DATE   ,Helpers.formatDate(Helpers.formatDate(headerObj.get("LoadingDat").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_TRANSPLANT_DATE  ,Helpers.formatDate(Helpers.formatDate(headerObj.get("TransplDate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_DELIVERY_DATE  ,Helpers.formatDate(Helpers.formatDate(headerObj.get("DelvDate").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_PICKING_DATE   ,Helpers.formatDate(Helpers.formatDate(headerObj.get("PickingDae").toString()), App.DATE_FORMAT));
                    headerParams.put(db.KEY_UNLOAD_POINT   ,headerObj.get("UnloadPt").toString());
                    headerParams.put(db.KEY_CUSTOMER_NO, headerObj.get("CustNo").toString());
                    headerParams.put(db.KEY_IS_DELIVERED, App.FALSE);
                    db.addData(db.CUSTOMER_DELIVERY_HEADER, headerParams);

                    for(int j=0;j<loadItems.length();j++){
                        JSONObject object = loadItems.getJSONObject(j);
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_DELIVERY_NO,object.get("DeliveryNo").toString());
                        params.put(db.KEY_ITEM_NO,object.get("Itemno").toString());
                        params.put(db.KEY_ITEM_CATEGORY, object.get("DelvItmCat").toString());
                        params.put(db.KEY_CREATED_BY, object.get("CreatedBy").toString());
                        params.put(db.KEY_ENTRY_TIME  ,object.get("EntryTime").toString());
                        params.put(db.KEY_DATE   ,Helpers.formatDate(Helpers.formatDate(object.get("CreationDat").toString()), App.DATE_FORMAT));
                        params.put(db.KEY_MATERIAL_NO   ,object.get("MaterialNo").toString());
                        params.put(db.KEY_MATERIAL_ENTERED   ,object.get("MaterialEntered").toString());
                        params.put(db.KEY_MATERIAL_GROUP ,object.get("MatGroup").toString());
                        params.put(db.KEY_PLANT ,object.get("Plant").toString());
                        params.put(db.KEY_STORAGE_LOCATION , object.get("StorLocation").toString());
                        params.put(db.KEY_BATCH , object.get("Batch").toString());
                        params.put(db.KEY_ACTUAL_QTY,object.get("ActQtyDel").toString());
                        params.put(db.KEY_REMAINING_QTY,object.get("ActQtyDel").toString());
                        params.put(db.KEY_UOM ,object.get("Uom").toString());
                        params.put(db.KEY_DIST_CHANNEL ,object.get("DistCha").toString());
                        params.put(db.KEY_DIVISION ,object.get("Division").toString());
                        params.put(db.KEY_IS_DELIVERED,App.FALSE);
                        db.addData(db.CUSTOMER_DELIVERY_ITEMS,params);
                    }

                }
                break;
            case ConfigStore.ArticleHeaderEntity:

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject headerObj = jsonArray.getJSONObject(i);
                    JSONArray articleUOM = headerObj.getJSONObject("ArticleAltuom").getJSONArray("results");
                    JSONArray articleSalesAreas = headerObj.getJSONObject("ArticleSalesareas").getJSONArray("results");

                    HashMap<String, String> headerParams = new HashMap<>();
                    headerParams.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                    headerParams.put(db.KEY_MATERIAL_GROUPA_DESC  ,headerObj.get("MatGroupADesc").toString());
                    headerParams.put(db.KEY_MATERIAL_GROUPB_DESC ,headerObj.get("MatGroupDesc").toString());
                   // headerParams.put(db.KEY_MATERIAL_DESC2,headerObj.get("MatDesc2").toString());
                    headerParams.put(db.KEY_MATERIAL_DESC2,headerObj.get("Mat_Desc1_AR").toString());
                    headerParams.put(db.KEY_BATCH_MANAGEMENT,headerObj.get("Batchmgmt").toString());
                    headerParams.put(db.KEY_PRODUCT_HIERARCHY,headerObj.get("ProdHier").toString());
                    headerParams.put(db.KEY_VOLUME_UOM ,headerObj.get("VolumeUom").toString());
                    headerParams.put(db.KEY_VOLUME ,headerObj.get("Volume").toString());
                    headerParams.put(db.KEY_WEIGHT_UOM ,headerObj.get("WeightUom").toString());
                    headerParams.put(db.KEY_NET_WEIGHT ,headerObj.get("NetWeight").toString());
                    headerParams.put(db.KEY_GROSS_WEIGHT ,headerObj.get("GrossWeight").toString());
                    headerParams.put(db.KEY_ARTICLE_CATEGORY ,headerObj.get("IntArtCat").toString());
                    headerParams.put(db.KEY_ARTICLE_NO ,headerObj.get("IntArticleNo").toString());
                    headerParams.put(db.KEY_BASE_UOM ,headerObj.get("BaseUom").toString());
                    headerParams.put(db.KEY_MATERIAL_GROUP,headerObj.get("MatGroup").toString());
                    headerParams.put(db.KEY_MATERIAL_TYPE,headerObj.get("MatType").toString());
                    headerParams.put(db.KEY_MATERIAL_DESC1 ,headerObj.get("MatDesc1").toString());
                    headerParams.put(db.KEY_MATERIAL_NO  ,headerObj.get("MatNo").toString());

                    db.addData(db.ARTICLE_HEADER, headerParams);

                    for(int j=0;j<articleUOM.length();j++){
                        JSONObject object = articleUOM.getJSONObject(j);
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        params.put(db.KEY_MATERIAL_NO   ,object.get("MatNo").toString());
                        params.put(db.KEY_UOM  ,object.get("Uom").toString());
                        params.put(db.KEY_NUMERATOR  ,object.get("Numerator").toString());
                        params.put(db.KEY_DENOMINATOR  ,object.get("Denominator").toString());
                        params.put(db.KEY_ARTICLE_NO  ,object.get("IntArticleNo").toString());
                        params.put(db.KEY_ARTICLE_CATEGORY  ,object.get("IntArtCategory").toString());

                        db.addData(db.ARTICLE_UOM,params);
                    }

                    for(int k=0;k<articleSalesAreas.length();k++){
                        JSONObject object = articleSalesAreas.getJSONObject(k);
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        params.put(db.KEY_PRICE_REF_MAT  ,object.get("PriceRefMat").toString());
                        params.put(db.KEY_SALES_UOM ,object.get("SalesUom").toString());
                        params.put(db.KEY_MATERIAL_PURCHASE_GROUP,object.get("MatPrcGrp").toString());
                        params.put(db.KEY_PRODUCT_HIERARCHY,object.get("ProdHier").toString());
                        params.put(db.KEY_MINIMUM_ORDER_QTY,object.get("MinOrdQty").toString());
                        params.put(db.KEY_SALES_STATUS ,object.get("SalesStat").toString());
                        params.put(db.KEY_EMPTY_R_BLOCK  ,object.get("EmptRblock").toString());
                        params.put(db.KEY_EMPTY_GROUP ,object.get("EmptGrp").toString());
                        params.put(db.KEY_SKT_OF  ,object.get("Sktof").toString());
                        params.put(db.KEY_DIST_CHANNEL  ,object.get("DistChannel").toString());
                        params.put(db.KEY_SALES_ORG  ,object.get("SalesOrg").toString());
                        params.put(db.KEY_MATERIAL_NO   ,object.get("MatNo").toString());

                        db.addData(db.ARTICLE_SALES_AREA,params);
                    }
                }
                break;
            case ConfigStore.VisitListEntity:
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);
                      //  Log.e("Object ", "" + object.get("CustNo"));

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        params.put(db.KEY_VISITLISTID,object.get("Vlid").toString());
                        params.put(db.KEY_ITEMNO, object.get("ItemNo").toString());
                        params.put(db.KEY_CUSTOMER_NO, object.get("CustNo").toString());
                        params.put(db.KEY_EXEC_DATE, Helpers.formatDate(Helpers.formatDate(object.get("Execdate").toString()), App.DATE_FORMAT));
                       // params.put(db.KEY_EXEC_DATE, object.get("Execdate").toString().substring(0,10));
                        params.put(db.KEY_DRIVER,object.get("Driver1").toString());
                        params.put(db.KEY_VP_TYPE,object.get("Vptype").toString());

                        params.put(db.KEY_IS_DELIVERY_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_ORDER_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_SALES_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_COLLECTION_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_MERCHANDIZE_CAPTURED,App.IS_NOT_COMPLETE);
                        params.put(db.KEY_IS_VISITED,App.IS_NOT_COMPLETE);

                        params.put(db.KEY_IS_DELIVERY_POSTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_ORDER_POSTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_SALES_POSTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_COLLECTION_POSTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_MERCHANDIZE_POSTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_NEW_CUSTOMER,App.FALSE);
                        db.addData(db.VISIT_LIST, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;
            case ConfigStore.CustomerHeaderEntity:

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject headerObj = jsonArray.getJSONObject(i);
                    JSONArray customerCreditArray = headerObj.getJSONObject("CustomerCredit").getJSONArray("results");
                    JSONArray customerSalesAreas = headerObj.getJSONObject("CustomerSalesAreas").getJSONArray("results");
                    JSONArray customerOpenItems = headerObj.getJSONObject("CustomerOpenItems").getJSONArray("results");
                    JSONArray customerFlags = headerObj.getJSONObject("CustomerFlags").getJSONArray("results");
                    HashMap<String, String> headerParams = new HashMap<>();
                    headerParams.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                    headerParams.put(db.KEY_ORDER_BLOCK  ,headerObj.get("OrderBlock").toString());
                    headerParams.put(db.KEY_INVOICE_BLOCK ,headerObj.get("InvBlock").toString());
                    headerParams.put(db.KEY_DELIVERY_BLOCK,headerObj.get("DelvBlock").toString());
                    headerParams.put(db.KEY_ROOM_NO,headerObj.get("Roomnumber").toString());
                    headerParams.put(db.KEY_FLOOR,headerObj.get("Floor").toString());
                    headerParams.put(db.KEY_BUILDING ,headerObj.get("Building").toString());
                    headerParams.put(db.KEY_HOME_CITY ,headerObj.get("HomeCity").toString());
                    headerParams.put(db.KEY_STREET5 ,headerObj.get("Street5").toString());
                    headerParams.put(db.KEY_STREET4 ,headerObj.get("Street4").toString());
                    headerParams.put(db.KEY_STREET3 ,headerObj.get("Street3").toString());
                    headerParams.put(db.KEY_STREET2 ,headerObj.get("Street2").toString());
                    headerParams.put(db.KEY_NAME4 ,headerObj.get("Name4").toString());
                    headerParams.put(db.KEY_DRIVER,headerObj.get("DriverNo").toString());
                    headerParams.put(db.KEY_CUSTOMER_NO,headerObj.get("CustNo").toString());
                    headerParams.put(db.KEY_COUNTRY_CODE ,headerObj.get("CountryCode").toString());
                    headerParams.put(db.KEY_NAME3 ,headerObj.get("Name3").toString());
                    headerParams.put(db.KEY_NAME1 ,headerObj.get("Name1").toString());
                    headerParams.put(db.KEY_ADDRESS ,headerObj.get("Adressnr").toString());
                    headerParams.put(db.KEY_STREET ,headerObj.get("Street").toString());
                    headerParams.put(db.KEY_NAME2 ,headerObj.get("Name2").toString());
                    headerParams.put(db.KEY_CITY ,headerObj.get("City").toString());
                    headerParams.put(db.KEY_DISTRICT ,headerObj.get("District").toString());
                    headerParams.put(db.KEY_REGION ,headerObj.get("Regio").toString());
                    headerParams.put(db.KEY_SITE_CODE ,headerObj.get("SiteCode").toString());
                    headerParams.put(db.KEY_POST_CODE ,headerObj.get("PostCode").toString());
                    headerParams.put(db.KEY_PHONE_NO ,headerObj.get("PhoneNumber").toString());
                    headerParams.put(db.KEY_COMPANY_CODE, headerObj.get("CompanyCode").toString());
                    headerParams.put(db.KEY_LATITUDE ,headerObj.get("Latitude").toString());
                    headerParams.put(db.KEY_LONGITUDE ,headerObj.get("Longitude").toString());
                    headerParams.put(db.KEY_TERMS ,headerObj.get("Terms").toString());
                    headerParams.put(db.KEY_TERMS_DESCRIPTION ,headerObj.get("TermDesc").toString());
                    db.addData(db.CUSTOMER_HEADER, headerParams);

                    for(int j=0;j<customerCreditArray.length();j++){
                        JSONObject customerCreditObj = customerCreditArray.getJSONObject(j);
                        //Log.e("Credit Obj","" + customerCreditObj.get("CreditLimit"));
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_CUSTOMER_NO  ,customerCreditObj.get("CustNo").toString());
                        params.put(db.KEY_CREDIT_CONTROL_AREA ,customerCreditObj.get("CreditCtrlArea").toString());
                        params.put(db.KEY_CREDIT_LIMIT ,customerCreditObj.get("CreditLimit").toString());
                        params.put(db.KEY_AVAILABLE_LIMIT ,customerCreditObj.get("CreditLimit").toString());
                        params.put(db.KEY_SPECIAL_LIABILITIES ,customerCreditObj.get("SpecialLiabilities").toString());
                        params.put(db.KEY_RECEIVABLES ,customerCreditObj.get("Recievables").toString());
                        params.put(db.KEY_CURRENCY ,customerCreditObj.get("currency").toString());
                        params.put(db.KEY_CREDIT_DAYS ,customerCreditObj.get("Days").toString());
                        params.put(db.KEY_RISK_CAT, customerCreditObj.get("RiskCat").toString());

                        db.addData(db.CUSTOMER_CREDIT,params);
                    }

                    for(int k=0;k<customerSalesAreas.length();k++){
                        JSONObject customerSalesAreasObject = customerSalesAreas.getJSONObject(k);
                        //Log.e("Sales Are Obj", "" + customerSalesAreasObject.get("SoldToNo"));
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_CUSTOMER_NO  ,customerSalesAreasObject.get("CustNo").toString());
                        params.put(db.KEY_DIVISION ,customerSalesAreasObject.get("Division").toString());
                        params.put(db.KEY_DIST_CHANNEL,customerSalesAreasObject.get("DistChannel").toString());
                        params.put(db.KEY_SALES_ORG,customerSalesAreasObject.get("SalesOrg").toString());
                        params.put(db.KEY_DRIVER,customerSalesAreasObject.get("DriverNo").toString());
                        params.put(db.KEY_SOLD_TO_NO ,customerSalesAreasObject.get("SoldToNo").toString());
                        params.put(db.KEY_BILL_TO_NO  ,customerSalesAreasObject.get("BillToNo").toString());
                        params.put(db.KEY_SHIP_TO_NO, customerSalesAreasObject.get("ShipToNo").toString());
                        params.put(db.KEY_PAYER_NO  ,customerSalesAreasObject.get("PayerNo").toString());
                        params.put(db.KEY_SALES_NO  ,customerSalesAreasObject.get("SalesNo").toString());
                        params.put(db.KEY_CUSTOMER_GROUP1  ,customerSalesAreasObject.get("CustomerGroup1").toString());
                        params.put(db.KEY_PAYCODE  ,customerSalesAreasObject.get("PayCode").toString());
                        params.put(db.KEY_CUSTOMER_GROUP2,customerSalesAreasObject.get("CustomerGroup2").toString());
                        params.put(db.KEY_CUSTOMER_GROUP3,customerSalesAreasObject.get("CustomerGroup3").toString());
                        params.put(db.KEY_CUSTOMER_GROUP4  ,customerSalesAreasObject.get("CustomerGroup4").toString());
                        params.put(db.KEY_CUSTOMER_GROUP5 ,customerSalesAreasObject.get("CustomerGroup5").toString());

                        db.addData(db.CUSTOMER_SALES_AREAS,params);
                    }

                    for(int m=0;m<customerOpenItems.length();m++){
                        JSONObject customerOpenItemsObj = customerOpenItems.getJSONObject(m);
                        HashMap<String,String>params = new HashMap<>();
                        params.put(db.KEY_COLLECTION_TYPE,customerOpenItemsObj.get("DocType").toString());
                        params.put(db.KEY_CUSTOMER_NO,customerOpenItemsObj.get("CustNo").toString());
                        params.put(db.KEY_SAP_INVOICE_NO,customerOpenItemsObj.get("DocNum").toString());
                        params.put(db.KEY_INVOICE_NO, customerOpenItemsObj.get("DocNo").toString());
                        params.put(db.KEY_INVOICE_AMOUNT,customerOpenItemsObj.get("Amount").toString());
                        params.put(db.KEY_INVOICE_DATE,customerOpenItemsObj.get("DocDate").toString());
                        params.put(db.KEY_INVOICE_DAYS,customerOpenItemsObj.get("Days").toString());
                        params.put(db.KEY_DUE_DATE,customerOpenItemsObj.get("DueDate").toString());
                        params.put(db.KEY_INDICATOR,customerOpenItemsObj.get("DebitCreditInd").toString());
                        params.put(db.KEY_AMOUNT_CLEARED,"0");
                        params.put(db.KEY_CHEQUE_AMOUNT,"0");
                        params.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"0");
                        params.put(db.KEY_CHEQUE_NUMBER,"0000");
                        params.put(db.KEY_CHEQUE_DATE,"0000");
                        params.put(db.KEY_CHEQUE_BANK_CODE,"0000");
                        params.put(db.KEY_CHEQUE_BANK_NAME,"0000");
                        params.put(db.KEY_CASH_AMOUNT,"0");
                        params.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                        params.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_INCOMPLETE);
                        db.addData(db.COLLECTION,params);
                    }

                    for(int l=0;l<customerFlags.length();l++){
                        JSONObject customerFlagObject = customerFlags.getJSONObject(l);
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        params.put(db.KEY_CUSTOMER_NO,customerFlagObject.get("CustNo").toString());
                        params.put(db.KEY_THRESHOLD_LIMIT,customerFlagObject.get("Thresholdlimit").toString());
                        params.put(db.KEY_VERIFYGPS,customerFlagObject.get("Verifygpsdata").toString());
                        params.put(db.KEY_GPS_SAVE,customerFlagObject.get("Gpssavecount").toString());
                        params.put(db.KEY_ENABLE_INVOICE,customerFlagObject.get("Enableivcopy").toString());
                        params.put(db.KEY_ENABLE_DELAY_PRINT,customerFlagObject.get("Enabledelayprint").toString());
                        params.put(db.KEY_ENABLE_EDIT_ORDERS,customerFlagObject.get("Enableeditorders").toString());
                        params.put(db.KEY_ENABLE_EDIT_INVOICE,customerFlagObject.get("Enableeditiv").toString());
                        params.put(db.KEY_ENABLE_RETURNS,customerFlagObject.get("Enablereturns").toString());
                        params.put(db.KEY_ENABLE_DAMAGED,customerFlagObject.get("Enabledamaged").toString());
                        params.put(db.KEY_ENABLE_SIGN_CAPTURE,customerFlagObject.get("Enablsigncapture").toString());
                        params.put(db.KEY_ENABLE_RETURN,customerFlagObject.get("Enablereturn").toString());
                        params.put(db.KEY_ENABLE_AR_COLLECTION,customerFlagObject.get("Enablearcoll").toString());
                        params.put(db.KEY_ENABLE_POS_EQUI,customerFlagObject.get("Enableposequi").toString());
                        params.put(db.KEY_ENABLE_SUR_AUDIT,customerFlagObject.get("Enablesuraudit").toString());
                        db.addData(db.CUSTOMER_FLAGS,params);
                    }
                }
                break;

            case ConfigStore.MessageEntity:{
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_USERNAME,object.get("Tdname").toString());
                        params.put(db.KEY_STRUCTURE, object.get("Tdkeystruc").toString());
                        params.put(db.KEY_MESSAGE, object.get("Message").toString());
                        params.put(db.KEY_DRIVER,object.get("Driver").toString());

                        db.addData(db.MESSAGES, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                //Load Master data into model on all load data complete
                Helpers.loadData(this.context);
                break;
            }

            case ConfigStore.DriverRouteEntity:{
                App.DriverRouteControl driverFlag = new App.DriverRouteControl();
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        params.put(db.KEY_DRIVER,object.get("DriverNo").toString());
                        params.put(db.KEY_ROUTE_TYPE,object.get("Routetype").toString());
                        driverFlag.setRouteType(object.get("Routetype").toString());
                        params.put(db.KEY_PROMPT_ODOMETER, object.get("Promptodometer").toString());
                        driverFlag.setPromptOdometer(object.get("Promptodometer").toString().equals("0") ? false : true);
                        params.put(db.KEY_EOD_SALES_REPORT, object.get("Eodsalesrep").toString());
                        driverFlag.setEodSalesReports(object.get("Eodsalesrep").toString().equals("0") ? false : true);
                        params.put(db.KEY_ENABLE_PVOID, object.get("Enablepvoid").toString());
                        driverFlag.setIsDeleteInvoice(object.get("Enablepvoid").toString().equals("0") ? false : true);
                        params.put(db.KEY_ENABLE_NO_SALE, object.get("Enablenosale").toString());
                        driverFlag.setIsNoSale(object.get("Enablenosale").toString().equals("0") ? false : true);
                        params.put(db.KEY_ENABLE_ADD_CUSTOMER, object.get("Enableaddcustomer").toString());
                        driverFlag.setIsAddCustomer(object.get("Enableaddcustomer").toString().equals("0") ? false : true);
                        params.put(db.KEY_DEFAULT_DELIVERY_DAYS, object.get("Defaultdeldays").toString());
                        driverFlag.setDefaultDeliveryDays(object.get("Defaultdeldays").toString());
                        params.put(db.KEY_PASSWORD1, object.get("Password1").toString());
                        driverFlag.setPassword1(object.get("Password1").toString());
                        params.put(db.KEY_PASSWORD2, object.get("Password2").toString());
                        driverFlag.setPassword2(object.get("Password2").toString());
                        params.put(db.KEY_PASSWORD3, object.get("Password3").toString());
                        driverFlag.setPassword3(object.get("Password3").toString());
                        params.put(db.KEY_PASSWORD4, object.get("Password4").toString());
                        driverFlag.setPassword4(object.get("Password4").toString());
                        params.put(db.KEY_PASSWORD5,object.get("Password5").toString());
                        driverFlag.setPassword5(object.get("Password5").toString());
                        params.put(db.KEY_DATE_TIME_CHANGE,object.get("Datetimechange").toString());
                        params.put(db.KEY_PRICE_CHANGE,object.get("Pricechange").toString());
                        params.put(db.KEY_PROMO_OVERRIDE,object.get("Promooverride").toString());
                        params.put(db.KEY_ROUTE_SETUP,object.get("Routesetup").toString());
                        params.put(db.KEY_VIEW_STOCK,object.get("Viewstock").toString());
                        driverFlag.setIsViewVanStock(object.get("Viewstock").toString());
                        params.put(db.KEY_LOAD_SECURITY_GUARD,object.get("Loadsecurityguard").toString());
                        driverFlag.setIsLoadSecurityGuard(object.get("Loadsecurityguard").toString());
                        params.put(db.KEY_START_OF_DAY,object.get("Startofday").toString());
                        driverFlag.setIsStartOfDay(object.get("Startofday").toString());
                        params.put(db.KEY_SETTLEMENT,object.get("Settlement").toString());
                        driverFlag.setIsEndTrip(object.get("Settlement").toString());
                        params.put(db.KEY_LOAD_ADJUST,object.get("Loadadjust").toString());
                        params.put(db.KEY_ENFORCE_CALL_SEQ,object.get("Enforcecallseq").toString());
                        driverFlag.setIsCallSequence(object.get("Enforcecallseq").equals("0")?false:true);
                        params.put(db.KEY_DISPLAY_IV_SUMMARY,object.get("Displayivsummary").toString());
                        driverFlag.setIsDisplayInvoiceSummary(object.get("Displayivsummary").toString().equals("0")?true:false);
                        params.put(db.KEY_ALLOW_RADIUS, object.get("Allowradious").toString());
                        driverFlag.setIsAllowRadius(object.get("Allowradious").toString().equals("0")?true:false);
                        params.put(db.KEY_ENABLE_GPS, object.get("Enablegps").toString());
                        driverFlag.setIsEnableGPS(object.get("Enablegps").toString().equals("0")?false:true);

                        db.addData(db.DRIVER_FLAGS, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;
            }

            case ConfigStore.OrderReasonEntity:
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_REASON_TYPE,App.OrderReasons);
                        params.put(db.KEY_REASON_CODE,object.get("Reason").toString());
                        params.put(db.KEY_REASON_DESCRIPTION, object.get("Description").toString());
                        params.put(db.KEY_REASON_DESCRIPTION_AR,object.get("DescriptionAr").toString());
                        db.addData(db.REASONS, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;
            case ConfigStore.OrderRejReasonEntity:
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_REASON_TYPE,App.REASON_REJECT);
                        params.put(db.KEY_REASON_CODE,object.get("Reason").toString());
                        params.put(db.KEY_REASON_DESCRIPTION, object.get("Description").toString());
                        params.put(db.KEY_REASON_DESCRIPTION_AR,object.get("DescriptionAr").toString());
                        db.addData(db.REASONS, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;
            case ConfigStore.VisitReasonEntity:
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_REASON_TYPE,App.VisitReasons);
                        params.put(db.KEY_REASON_CODE,object.get("VisitActivity").toString());
                        params.put(db.KEY_REASON_DESCRIPTION, object.get("Description").toString());
                        params.put(db.KEY_REASON_DESCRIPTION_AR, object.get("DescriptionAr").toString());
                        db.addData(db.REASONS, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;
            case ConfigStore.PricingEntity:
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_CUSTOMER_NO,object.get("CustNo").toString());
                        params.put(db.KEY_MATERIAL_NO,object.get("Material").toString());
                        params.put(db.KEY_AMOUNT,object.get("Amount").toString());
                        params.put(db.KEY_CURRENCY,object.get("Currency").toString());
                        params.put(db.KEY_PRIORITY, object.get("Priority").toString());
                        params.put(db.KEY_DRIVER, object.get("Driver").toString());
                        db.addData(db.PRICING, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;

            case ConfigStore.Promotion02Entity:
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_PROMOTION_TYPE,App.Promotions02);
                        params.put(db.KEY_SALES_ORG,object.get("SalesOrg").toString());
                        params.put(db.KEY_DIST_CHANNEL,object.get("DistChannel").toString());
                        params.put(db.KEY_CUSTOMER_NO,object.get("CustNo").toString());
                        params.put(db.KEY_MATERIAL_NO, object.get("Material").toString());
                        params.put(db.KEY_AMOUNT, object.get("Amount").toString());
                        params.put(db.KEY_CURRENCY, object.get("Currency").toString());
                        params.put(db.KEY_DRIVER, object.get("Driver").toString());
                        db.addData(db.PROMOTIONS, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;

            case ConfigStore.Promotion05Entity:
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_PROMOTION_TYPE,App.Promotions05);
                        params.put(db.KEY_SALES_ORG,object.get("SalesOrg").toString());
                        params.put(db.KEY_DIST_CHANNEL,object.get("DistChannel").toString());
                        params.put(db.KEY_CUSTOMER_NO,object.get("CustNo").toString());
                        params.put(db.KEY_MATERIAL_NO, object.get("Material").toString());
                        params.put(db.KEY_AMOUNT, object.get("Amount").toString());
                        params.put(db.KEY_CURRENCY, object.get("Currency").toString());
                        params.put(db.KEY_DRIVER, object.get("Driver").toString());
                        db.addData(db.PROMOTIONS, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                break;
            case ConfigStore.Promotion07Entity:
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_PROMOTION_TYPE,App.Promotions07);
                        params.put(db.KEY_SALES_ORG,object.get("SalesOrg").toString());
                        params.put(db.KEY_DIST_CHANNEL,object.get("DistChannel").toString());
                        params.put(db.KEY_CUSTOMER_NO,object.get("CustNo").toString());
                        params.put(db.KEY_MATERIAL_NO, object.get("Material").toString());
                        params.put(db.KEY_AMOUNT, object.get("Amount").toString());
                        params.put(db.KEY_CURRENCY, object.get("Currency").toString());
                        params.put(db.KEY_DRIVER, object.get("Driver").toString());
                        db.addData(db.PROMOTIONS, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;

            case ConfigStore.BankEntity:
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject object = jsonArray.getJSONObject(i);
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_BANK_CODE,object.get("Bankkey").toString());
                        params.put(db.KEY_BANK_NAME, object.get("Name").toString());
                        db.addData(db.BANKS, params);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;

            case ConfigStore.DriverOpenItemsEntity:
                for(int i=0;i<jsonArray.length();i++){

                    JSONObject customerOpenItemsObj = jsonArray.getJSONObject(i);
                    HashMap<String,String>params = new HashMap<>();
                    params.put(db.KEY_COLLECTION_TYPE,customerOpenItemsObj.get("DocType").toString());
                    params.put(db.KEY_CUSTOMER_NO,Settings.getString(App.DRIVER));
                    params.put(db.KEY_SAP_INVOICE_NO,customerOpenItemsObj.get("DocNum").toString());
                    params.put(db.KEY_INVOICE_NO, customerOpenItemsObj.get("DocNum").toString());
                    params.put(db.KEY_INVOICE_AMOUNT,customerOpenItemsObj.get("Amount").toString());
                    params.put(db.KEY_INVOICE_DATE,customerOpenItemsObj.get("DocDate").toString());
                    params.put(db.KEY_INVOICE_DAYS,customerOpenItemsObj.get("Days").toString());
                    params.put(db.KEY_DUE_DATE,customerOpenItemsObj.get("DueDate").toString());
                    params.put(db.KEY_INDICATOR,customerOpenItemsObj.get("DebitCreditInd").toString());
                    params.put(db.KEY_AMOUNT_CLEARED,"0");
                    params.put(db.KEY_CHEQUE_AMOUNT,"0");
                    params.put(db.KEY_CHEQUE_AMOUNT_INDIVIDUAL,"0");
                    params.put(db.KEY_CHEQUE_NUMBER,"0000");
                    params.put(db.KEY_CHEQUE_DATE,"0000");
                    params.put(db.KEY_CHEQUE_BANK_CODE,"0000");
                    params.put(db.KEY_CHEQUE_BANK_NAME,"0000");
                    params.put(db.KEY_CASH_AMOUNT,"0");
                    params.put(db.KEY_IS_POSTED,App.DATA_NOT_POSTED);
                    params.put(db.KEY_IS_PRINTED,App.DATA_NOT_POSTED);
                    params.put(db.KEY_IS_INVOICE_COMPLETE,App.INVOICE_INCOMPLETE);
                    db.addData(db.DRIVER_COLLECTION,params);
                }
                break;

            case ConfigStore.FOCEntity:


                break;
        }
    }
    public static void generateData(DatabaseHandler db){
        /*HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO,"");
        map.put(db.KEY_DIST_CHANNEL,"");
        map.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map);

        HashMap<String,String>map1 = new HashMap<>();
        map1.put(db.KEY_CUSTOMER_NO,"");
        map1.put(db.KEY_DIST_CHANNEL,"");
        map1.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map1.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map1.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map1.put(db.KEY_FOC_ASSIGNING_QUANTITY,"1");
        map1.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map1.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map1);

        HashMap<String,String>map2 = new HashMap<>();
        map2.put(db.KEY_CUSTOMER_NO,"");
        map2.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map2.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map2.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map2.put(db.KEY_FOC_ASSIGNING_QUANTITY,"1");
        map2.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map2.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map2);

        HashMap<String,String>map3 = new HashMap<>();
        map3.put(db.KEY_CUSTOMER_NO,"");
        map3.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map3.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map3.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map3.put(db.KEY_FOC_ASSIGNING_QUANTITY,"1");
        map3.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map3.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map3);*/

        HashMap<String,String>map = new HashMap<>();
        map.put(db.KEY_CUSTOMER_NO,"");
        map.put(db.KEY_DIST_CHANNEL,"20");
        map.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map);

        HashMap<String,String>map1 = new HashMap<>();
        map1.put(db.KEY_CUSTOMER_NO,"");
        map1.put(db.KEY_DIST_CHANNEL,"20");
        map1.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map1.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map1.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map1.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map1.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map1.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map1);

        HashMap<String,String>map2 = new HashMap<>();
        map2.put(db.KEY_CUSTOMER_NO,"");
        map2.put(db.KEY_DIST_CHANNEL,"20");
        map2.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map2.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map2.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map2.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map2.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map2.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map2);

        HashMap<String,String>map3 = new HashMap<>();
        map3.put(db.KEY_CUSTOMER_NO,"");
        map3.put(db.KEY_DIST_CHANNEL,"20");
        map3.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map3.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map3.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map3.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map3.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map3.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map3);

        HashMap<String,String>map4 = new HashMap<>();
        map4.put(db.KEY_CUSTOMER_NO,"");
        map4.put(db.KEY_DIST_CHANNEL,"20");
        map4.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map4.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map4.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map4.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map4.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map4.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map4);

        HashMap<String,String>map5 = new HashMap<>();
        map5.put(db.KEY_CUSTOMER_NO,"");
        map5.put(db.KEY_DIST_CHANNEL,"20");
        map5.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map5.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map5.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map5.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map5.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map5.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map5);

        HashMap<String,String>map6 = new HashMap<>();
        map6.put(db.KEY_CUSTOMER_NO,"");
        map6.put(db.KEY_DIST_CHANNEL,"20");
        map6.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map6.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map6.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map6.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map6.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map6.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map6);

        HashMap<String,String>map7 = new HashMap<>();
        map7.put(db.KEY_CUSTOMER_NO,"");
        map7.put(db.KEY_DIST_CHANNEL,"20");
        map7.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map7.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map7.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map7.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map7.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map7.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map7);

        HashMap<String,String>map8 = new HashMap<>();
        map8.put(db.KEY_CUSTOMER_NO,"");
        map8.put(db.KEY_DIST_CHANNEL,"20");
        map8.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map8.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map8.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map8.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map8.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map8.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map8);

        HashMap<String,String>map9 = new HashMap<>();
        map9.put(db.KEY_CUSTOMER_NO,"");
        map9.put(db.KEY_DIST_CHANNEL,"20");
        map9.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map9.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map9.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map9.put(db.KEY_FOC_ASSIGNING_QUANTITY, "3");
        map9.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map9.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map9);

        HashMap<String,String>map10 = new HashMap<>();
        map10.put(db.KEY_CUSTOMER_NO,"");
        map10.put(db.KEY_DIST_CHANNEL,"20");
        map10.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map10.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map10.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map10.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map10.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map10.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map10);

        HashMap<String,String>map11 = new HashMap<>();
        map11.put(db.KEY_CUSTOMER_NO,"");
        map11.put(db.KEY_DIST_CHANNEL,"20");
        map11.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map11.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map11.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map11.put(db.KEY_FOC_ASSIGNING_QUANTITY, "3");
        map11.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map11.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map11);

        HashMap<String,String>map12 = new HashMap<>();
        map12.put(db.KEY_CUSTOMER_NO,"");
        map12.put(db.KEY_DIST_CHANNEL,"20");
        map12.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map12.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map12.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map12.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map12.put(db.KEY_FOC_DATE_FROM, "2017.03.01");
        map12.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map12);

        HashMap<String,String>map13 = new HashMap<>();
        map13.put(db.KEY_CUSTOMER_NO,"");
        map13.put(db.KEY_DIST_CHANNEL,"20");
        map13.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map13.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map13.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map13.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map13.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map13.put(db.KEY_FOC_DATE_TO, "2017.03.31");
        db.addData(db.FOC_RULES, map13);


        /*HashMap<String,String>map14 = new HashMap<>();
        map14.put(db.KEY_CUSTOMER_NO,"");
        map14.put(db.KEY_DIST_CHANNEL,"30");
        map14.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map14.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map14.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map14.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map14.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map14.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map14);

        HashMap<String,String>map15 = new HashMap<>();
        map15.put(db.KEY_CUSTOMER_NO,"");
        map15.put(db.KEY_DIST_CHANNEL,"30");
        map15.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map15.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map15.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map15.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map15.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map15.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map15);

        HashMap<String,String>map16 = new HashMap<>();
        map16.put(db.KEY_CUSTOMER_NO,"");
        map16.put(db.KEY_DIST_CHANNEL,"30");
        map16.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map16.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map16.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map16.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map16.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map16.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map16);

        HashMap<String,String>map17 = new HashMap<>();
        map17.put(db.KEY_CUSTOMER_NO,"");
        map17.put(db.KEY_DIST_CHANNEL,"30");
        map17.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map17.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map17.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map17.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map17.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map17.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map17);

        HashMap<String,String>map18 = new HashMap<>();
        map18.put(db.KEY_CUSTOMER_NO,"");
        map18.put(db.KEY_DIST_CHANNEL,"30");
        map18.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map18.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map18.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map18.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map18.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map18.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map18);

        HashMap<String,String>map19 = new HashMap<>();
        map19.put(db.KEY_CUSTOMER_NO,"");
        map19.put(db.KEY_DIST_CHANNEL,"30");
        map19.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map19.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map19.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map19.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map19.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map19.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map19);

        HashMap<String,String>map20 = new HashMap<>();
        map20.put(db.KEY_CUSTOMER_NO,"");
        map20.put(db.KEY_DIST_CHANNEL,"30");
        map20.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map20.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map20.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map20.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map20.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map20.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map20);

        HashMap<String,String>map21 = new HashMap<>();
        map21.put(db.KEY_CUSTOMER_NO,"");
        map21.put(db.KEY_DIST_CHANNEL,"30");
        map21.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map21.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map21.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map21.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map21.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map21.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map21);

        HashMap<String,String>map22 = new HashMap<>();
        map22.put(db.KEY_CUSTOMER_NO,"");
        map22.put(db.KEY_DIST_CHANNEL,"30");
        map22.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map22.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map22.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map22.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map22.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map22.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map22);

        HashMap<String,String>map23 = new HashMap<>();
        map23.put(db.KEY_CUSTOMER_NO,"");
        map23.put(db.KEY_DIST_CHANNEL,"30");
        map23.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map23.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map23.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map23.put(db.KEY_FOC_ASSIGNING_QUANTITY, "3");
        map23.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map23.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map23);

        HashMap<String,String>map24 = new HashMap<>();
        map24.put(db.KEY_CUSTOMER_NO,"");
        map24.put(db.KEY_DIST_CHANNEL,"30");
        map24.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map24.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map24.put(db.KEY_FOC_QUALIFYING_QUANTITY,"10");
        map24.put(db.KEY_FOC_ASSIGNING_QUANTITY, "1");
        map24.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map24.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map24);

        HashMap<String,String>map25 = new HashMap<>();
        map25.put(db.KEY_CUSTOMER_NO,"");
        map25.put(db.KEY_DIST_CHANNEL,"30");
        map25.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map25.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map25.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map25.put(db.KEY_FOC_ASSIGNING_QUANTITY, "3");
        map25.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map25.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map25);

        HashMap<String,String>map26 = new HashMap<>();
        map26.put(db.KEY_CUSTOMER_NO,"");
        map26.put(db.KEY_DIST_CHANNEL,"30");
        map26.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map26.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map26.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map26.put(db.KEY_FOC_ASSIGNING_QUANTITY, "2");
        map26.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map26.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map26);

        HashMap<String,String>map27 = new HashMap<>();
        map27.put(db.KEY_CUSTOMER_NO,"");
        map27.put(db.KEY_DIST_CHANNEL,"30");
        map27.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map27.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map27.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map27.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map27.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map27.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map27);

        //Rules for Dist Channel 50
        HashMap<String,String>map28 = new HashMap<>();
        map28.put(db.KEY_CUSTOMER_NO,"");
        map28.put(db.KEY_DIST_CHANNEL,"50");
        map28.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map28.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map28.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map28.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map28.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map28.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map28);

        HashMap<String,String>map29 = new HashMap<>();
        map29.put(db.KEY_CUSTOMER_NO,"");
        map29.put(db.KEY_DIST_CHANNEL,"50");
        map29.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map29.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000000");
        map29.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map29.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map29.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map29.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map29);

        HashMap<String,String>map30 = new HashMap<>();
        map30.put(db.KEY_CUSTOMER_NO,"");
        map30.put(db.KEY_DIST_CHANNEL,"50");
        map30.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map30.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map30.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map30.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map30.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map30.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map30);

        HashMap<String,String>map31 = new HashMap<>();
        map31.put(db.KEY_CUSTOMER_NO,"");
        map31.put(db.KEY_DIST_CHANNEL,"50");
        map31.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000002");
        map31.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000002");
        map31.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map31.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map31.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map31.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map31);

        HashMap<String,String>map32 = new HashMap<>();
        map32.put(db.KEY_CUSTOMER_NO,"");
        map32.put(db.KEY_DIST_CHANNEL,"50");
        map32.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map32.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map32.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map32.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map32.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map32.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map32);

        HashMap<String,String>map33 = new HashMap<>();
        map33.put(db.KEY_CUSTOMER_NO,"");
        map33.put(db.KEY_DIST_CHANNEL,"50");
        map33.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000003");
        map33.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map33.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map33.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map33.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map33.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map33);

        HashMap<String,String>map34 = new HashMap<>();
        map34.put(db.KEY_CUSTOMER_NO,"");
        map34.put(db.KEY_DIST_CHANNEL,"50");
        map34.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map34.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map34.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map34.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map34.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map34.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map34);

        HashMap<String,String>map35 = new HashMap<>();
        map35.put(db.KEY_CUSTOMER_NO,"");
        map35.put(db.KEY_DIST_CHANNEL,"50");
        map35.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000004");
        map35.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000004");
        map35.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map35.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map35.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map35.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map35);

        HashMap<String,String>map36 = new HashMap<>();
        map36.put(db.KEY_CUSTOMER_NO,"");
        map36.put(db.KEY_DIST_CHANNEL,"50");
        map36.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map36.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map36.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map36.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map36.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map36.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map36);

        HashMap<String,String>map37 = new HashMap<>();
        map37.put(db.KEY_CUSTOMER_NO,"");
        map37.put(db.KEY_DIST_CHANNEL,"50");
        map37.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000006");
        map37.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000006");
        map37.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map37.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map37.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map37.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map37);

        HashMap<String,String>map38 = new HashMap<>();
        map38.put(db.KEY_CUSTOMER_NO,"");
        map38.put(db.KEY_DIST_CHANNEL,"50");
        map38.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map38.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map38.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map38.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map38.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map38.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map38);

        HashMap<String,String>map39 = new HashMap<>();
        map39.put(db.KEY_CUSTOMER_NO,"");
        map39.put(db.KEY_DIST_CHANNEL,"50");
        map39.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000008");
        map39.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000008");
        map39.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map39.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map39.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map39.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map39);

        HashMap<String,String>map40 = new HashMap<>();
        map40.put(db.KEY_CUSTOMER_NO,"");
        map40.put(db.KEY_DIST_CHANNEL,"50");
        map40.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map40.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map40.put(db.KEY_FOC_QUALIFYING_QUANTITY,"50");
        map40.put(db.KEY_FOC_ASSIGNING_QUANTITY, "6");
        map40.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map40.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map40);

        HashMap<String,String>map41 = new HashMap<>();
        map41.put(db.KEY_CUSTOMER_NO,"");
        map41.put(db.KEY_DIST_CHANNEL,"50");
        map41.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000009");
        map41.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000009");
        map41.put(db.KEY_FOC_QUALIFYING_QUANTITY,"100");
        map41.put(db.KEY_FOC_ASSIGNING_QUANTITY, "13");
        map41.put(db.KEY_FOC_DATE_FROM,"2017.03.01");
        map41.put(db.KEY_FOC_DATE_TO,"2017.03.31");
        db.addData(db.FOC_RULES, map41);*/

        /*HashMap<String,String>map2 = new HashMap<>();
        map2.put(db.KEY_CUSTOMER_NO,"0000200513");
        map2.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000020");
        map2.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000021");
        map2.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map2.put(db.KEY_FOC_ASSIGNING_QUANTITY,"2");
        map2.put(db.KEY_FOC_DATE_FROM,"2017.03.03");
        map2.put(db.KEY_FOC_DATE_TO,"2017.03.12");
        db.addData(db.FOC_RULES, map2);

        HashMap<String,String>map3 = new HashMap<>();
        map3.put(db.KEY_CUSTOMER_NO,"0000200513");
        map3.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000020");
        map3.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000021");
        map3.put(db.KEY_FOC_QUALIFYING_QUANTITY,"20");
        map3.put(db.KEY_FOC_ASSIGNING_QUANTITY,"2");
        map3.put(db.KEY_FOC_DATE_FROM,"2017.03.03");
        map3.put(db.KEY_FOC_DATE_TO,"2017.03.12");
        db.addData(db.FOC_RULES, map3);

        HashMap<String,String>map4 = new HashMap<>();
        map4.put(db.KEY_CUSTOMER_NO,"0000200513");
        map4.put(db.KEY_FOC_QUALIFYING_ITEM,"000000000014000000");
        map4.put(db.KEY_FOC_ASSIGNING_ITEM,"000000000014000003");
        map4.put(db.KEY_FOC_QUALIFYING_QUANTITY,"5");
        map4.put(db.KEY_FOC_ASSIGNING_QUANTITY,"");
        map4.put(db.KEY_FOC_DATE_FROM,"2017.03.03");
        map4.put(db.KEY_FOC_DATE_TO,"2017.03.12");
        db.addData(db.FOC_RULES, map4);*/
    }
}

