package gbc.sa.vansales.data;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import gbc.sa.vansales.models.ArticleHeader;
import gbc.sa.vansales.utils.DatabaseHandler;
import gbc.sa.vansales.utils.DownloadData;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class ArticleHeaders {
    private static final String COLLECTION_NAME = "ArticleHeaderSet";
    private static final String ARTICLE_ALT_UOM = "ArticleAltuom";
    private static final String ARTICLE_SALES_AREAS = "ArticleSalesareas";
    private static final String TRIP_ID = "ITripId";
    private static ArrayList<ArticleHeader> data = new ArrayList<>();


    public static void load(Context context,String tripId, DatabaseHandler db){

        HashMap<String, String> params = new HashMap<>();
        params.put(TRIP_ID,tripId);

        HashMap<String,String>expansion = new HashMap<>();
        expansion.put(ARTICLE_ALT_UOM,ARTICLE_ALT_UOM);
        expansion.put(ARTICLE_SALES_AREAS,ARTICLE_SALES_AREAS);

        new DownloadData(context,COLLECTION_NAME,params,expansion,db);
    }

    public static void loadData(Context context){
        data.clear();
        DatabaseHandler db = new DatabaseHandler(context);
        HashMap<String, String> map = new HashMap<>();
        map.put(db.KEY_TRIP_ID,"");
        map.put(db.KEY_MATERIAL_GROUPA_DESC,"");
        map.put(db.KEY_MATERIAL_GROUPB_DESC,"");
        map.put(db.KEY_MATERIAL_DESC2,"");
        map.put(db.KEY_BATCH_MANAGEMENT,"");
        map.put(db.KEY_PRODUCT_HIERARCHY,"");
        map.put(db.KEY_VOLUME_UOM,"");
        map.put(db.KEY_VOLUME,"");
        map.put(db.KEY_WEIGHT_UOM,"");
        map.put(db.KEY_NET_WEIGHT,"");
        map.put(db.KEY_GROSS_WEIGHT,"");
        map.put(db.KEY_ARTICLE_CATEGORY,"");
        map.put(db.KEY_ARTICLE_NO,"");
        map.put(db.KEY_BASE_UOM,"");
        map.put(db.KEY_MATERIAL_GROUP,"");
        map.put(db.KEY_MATERIAL_TYPE,"");
        map.put(db.KEY_MATERIAL_DESC1,"");
        map.put(db.KEY_MATERIAL_NO,"");

        HashMap<String, String> filters = new HashMap<>();
        Cursor cursor = db.getData(db.ARTICLE_HEADER,map,filters);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
              ArticleHeader article = new ArticleHeader();

                article.setTripId(cursor.getString(cursor.getColumnIndex(db.KEY_TRIP_ID)));
                article.setMaterialGroupADesc(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_GROUPA_DESC)));
                article.setMaterialGroupBDesc(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_GROUPB_DESC)));
                article.setMaterialDesc1(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC1)));
                article.setMaterialDesc2(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_DESC2)));
                article.setBatchManagement(cursor.getString(cursor.getColumnIndex(db.KEY_BATCH_MANAGEMENT)));
                article.setProductHierarchy(cursor.getString(cursor.getColumnIndex(db.KEY_PRODUCT_HIERARCHY)));
                article.setVolumeUOM(cursor.getString(cursor.getColumnIndex(db.KEY_VOLUME_UOM)));
                article.setVolume(cursor.getString(cursor.getColumnIndex(db.KEY_VOLUME)));
                article.setWeightUOM(cursor.getString(cursor.getColumnIndex(db.KEY_WEIGHT_UOM)));
                article.setNetWeight(cursor.getString(cursor.getColumnIndex(db.KEY_NET_WEIGHT)));
                article.setGrossWeight(cursor.getString(cursor.getColumnIndex(db.KEY_GROSS_WEIGHT)));
                article.setArticleCategory(cursor.getString(cursor.getColumnIndex(db.KEY_ARTICLE_CATEGORY)));
                article.setArticleNo(cursor.getString(cursor.getColumnIndex(db.KEY_ARTICLE_NO)));
                article.setBaseUOM(cursor.getString(cursor.getColumnIndex(db.KEY_BASE_UOM)));
                article.setMaterialGroup(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_GROUP)));
                article.setMaterialType(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_TYPE)));
                article.setMaterialNo(cursor.getString(cursor.getColumnIndex(db.KEY_MATERIAL_NO)));

                data.add(article);

            }
            while (cursor.moveToNext());
            Log.e("Load Data","" + data.size());
        }

    }

    public static ArrayList<ArticleHeader> get() {
        return data;
    }


}
