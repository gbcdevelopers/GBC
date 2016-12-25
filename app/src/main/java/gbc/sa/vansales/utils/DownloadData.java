package gbc.sa.vansales.utils;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
        String url = UrlBuilder.buildExpansion(this.collectionName, this.params, this.expansions);
        JSONArray jsonArray = IntegrationService.getService(this.context,url);
        Log.e("Exp Response", ""+ jsonArray);

        try {
            Log.e("Metadata","" + jsonArray.getJSONObject(0).getJSONObject("__metadata").getString("type"));
            String metadata = jsonArray.getJSONObject(0).getJSONObject("__metadata").getString("type");
            parseJSON(metadata,jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
                    headerParams.put(db.KEY_DRIVER,headerObj.get("Driver1").toString());
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
                        params.put(db.KEY_DIST_CHANNEL  ,object.get("DistChannel").toString());
                        params.put(db.KEY_DIVISION  ,object.get("Division").toString());

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
                    db.addData(db.LOAD_DELIVERY_HEADER, headerParams);

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
                        params.put(db.KEY_UOM ,object.get("Uom").toString());
                        params.put(db.KEY_DIST_CHANNEL ,object.get("DistCha").toString());
                        params.put(db.KEY_DIVISION ,object.get("Division").toString());

                        db.addData(db.LOAD_DELIVERY_ITEMS,params);
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
                    headerParams.put(db.KEY_MATERIAL_DESC2,headerObj.get("MatDesc2").toString());
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
                        Log.e("Object ", "" + object.get("CustNo"));

                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_TRIP_ID,Settings.getString(App.TRIP_ID));
                        params.put(db.KEY_VISITLISTID,object.get("Vlid").toString());
                        params.put(db.KEY_ITEMNO, object.get("ItemNo").toString());
                        params.put(db.KEY_CUSTOMER_NO, object.get("CustNo").toString());
                        params.put(db.KEY_EXEC_DATE, Helpers.formatDate(Helpers.formatDate(object.get("Execdate").toString()), App.DATE_FORMAT));
                       // params.put(db.KEY_EXEC_DATE, object.get("Execdate").toString().substring(0,10));
                        params.put(db.KEY_DRIVER,object.get("Driver1").toString());
                        params.put(db.KEY_VP_TYPE,object.get("Vptype").toString());

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

                    db.addData(db.CUSTOMER_HEADER, headerParams);

                    for(int j=0;j<customerCreditArray.length();j++){
                        JSONObject customerCreditObj = customerCreditArray.getJSONObject(j);
                        Log.e("Credit Obj","" + customerCreditObj.get("CreditLimit"));
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_CUSTOMER_NO  ,customerCreditObj.get("CustNo").toString());
                        params.put(db.KEY_CREDIT_CONTROL_AREA ,customerCreditObj.get("CreditCtrlArea").toString());
                        params.put(db.KEY_CREDIT_LIMIT ,customerCreditObj.get("CreditLimit").toString());
                        params.put(db.KEY_SPECIAL_LIABILITIES ,customerCreditObj.get("SpecialLiabilities").toString());
                        params.put(db.KEY_RECEIVABLES ,customerCreditObj.get("Recievables").toString());
                        params.put(db.KEY_CURRENCY ,customerCreditObj.get("currency").toString());
                        params.put(db.KEY_RISK_CAT, customerCreditObj.get("RiskCat").toString());

                        db.addData(db.CUSTOMER_CREDIT,params);
                    }

                    for(int k=0;k<customerSalesAreas.length();k++){
                        JSONObject customerSalesAreasObject = customerSalesAreas.getJSONObject(k);
                        Log.e("Sales Are Obj", "" + customerSalesAreasObject.get("SoldToNo"));
                        HashMap<String, String> params = new HashMap<>();
                        params.put(db.KEY_CUSTOMER_NO  ,customerSalesAreasObject.get("CustNo").toString());
                        params.put(db.KEY_DIVISION ,customerSalesAreasObject.get("Division").toString());
                        params.put(db.KEY_DIST_CHANNEL,customerSalesAreasObject.get("DistChannel").toString());
                        params.put(db.KEY_SALES_ORG,customerSalesAreasObject.get("SalesOrg").toString());
                        params.put(db.KEY_DRIVER,customerSalesAreasObject.get("DriverNo").toString());
                        params.put(db.KEY_SOLD_TO_NO ,customerSalesAreasObject.get("SoldToNo").toString());
                        params.put(db.KEY_BILL_TO_NO  ,customerSalesAreasObject.get("BillToNo").toString());
                        params.put(db.KEY_SHIP_TO_NO ,customerSalesAreasObject.get("ShipToNo").toString());
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
                }
                break;
        }
    }
}
