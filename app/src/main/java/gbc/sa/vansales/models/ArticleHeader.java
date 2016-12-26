package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class ArticleHeader implements Parcelable {
    private String tripId;
    private String materialGroupADesc;
    private String materialGroupBDesc;
    private String materialDesc2;
    private String batchManagement;
    private String productHierarchy;
    private String volumeUOM;
    private String volume;
    private String weightUOM;
    private String netWeight;
    private String grossWeight;
    private String articleCategory;
    private String articleNo;
    private String baseUOM;
    private String materialGroup;
    private String materialType;
    private String materialDesc1;
    private String materialNo;

    public String getArticleCategory() {
        return articleCategory;
    }
    public void setArticleCategory(String articleCategory) {
        this.articleCategory = articleCategory;
    }
    public String getArticleNo() {
        return articleNo;
    }
    public void setArticleNo(String articleNo) {
        this.articleNo = articleNo;
    }
    public String getBaseUOM() {
        return baseUOM;
    }
    public void setBaseUOM(String baseUOM) {
        this.baseUOM = baseUOM;
    }
    public String getBatchManagement() {
        return batchManagement;
    }
    public void setBatchManagement(String batchManagement) {
        this.batchManagement = batchManagement;
    }
    public String getGrossWeight() {
        return grossWeight;
    }
    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }
    public String getMaterialDesc1() {
        return materialDesc1;
    }
    public void setMaterialDesc1(String materialDesc1) {
        this.materialDesc1 = materialDesc1;
    }
    public String getMaterialDesc2() {
        return materialDesc2;
    }
    public void setMaterialDesc2(String materialDesc2) {
        this.materialDesc2 = materialDesc2;
    }
    public String getMaterialGroup() {
        return materialGroup;
    }
    public void setMaterialGroup(String materialGroup) {
        this.materialGroup = materialGroup;
    }
    public String getMaterialGroupADesc() {
        return materialGroupADesc;
    }
    public void setMaterialGroupADesc(String materialGroupADesc) {
        this.materialGroupADesc = materialGroupADesc;
    }
    public String getMaterialGroupBDesc() {
        return materialGroupBDesc;
    }
    public void setMaterialGroupBDesc(String materialGroupBDesc) {
        this.materialGroupBDesc = materialGroupBDesc;
    }
    public String getMaterialNo() {
        return materialNo;
    }
    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }
    public String getMaterialType() {
        return materialType;
    }
    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }
    public String getNetWeight() {
        return netWeight;
    }
    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }
    public String getProductHierarchy() {
        return productHierarchy;
    }
    public void setProductHierarchy(String productHierarchy) {
        this.productHierarchy = productHierarchy;
    }
    public String getTripId() {
        return tripId;
    }
    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
    public String getVolume() {
        return volume;
    }
    public void setVolume(String volume) {
        this.volume = volume;
    }
    public String getVolumeUOM() {
        return volumeUOM;
    }
    public void setVolumeUOM(String volumeUOM) {
        this.volumeUOM = volumeUOM;
    }
    public String getWeightUOM() {
        return weightUOM;
    }
    public void setWeightUOM(String weightUOM) {
        this.weightUOM = weightUOM;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(tripId);
        parcel.writeString(materialGroupADesc);
        parcel.writeString(materialGroupBDesc);
        parcel.writeString(materialDesc2);
        parcel.writeString(batchManagement);
        parcel.writeString(productHierarchy);
        parcel.writeString(volumeUOM);
        parcel.writeString(volume);
        parcel.writeString(weightUOM);
        parcel.writeString(netWeight);
        parcel.writeString(grossWeight);
        parcel.writeString(articleCategory);
        parcel.writeString(articleNo);
        parcel.writeString(baseUOM);
        parcel.writeString(materialGroup);
        parcel.writeString(materialType);
        parcel.writeString(materialDesc1);
        parcel.writeString(materialNo);
    }

    public static final Creator<ArticleHeader> CREATOR = new Creator<ArticleHeader>() {
        public ArticleHeader createFromParcel(Parcel source) {
            ArticleHeader article = new ArticleHeader();

            article.tripId = source.readString();
            article.materialGroupADesc = source.readString();
            article.materialGroupBDesc = source.readString();
            article.materialDesc2 = source.readString();
            article.batchManagement = source.readString();
            article.productHierarchy = source.readString();
            article.volumeUOM = source.readString();
            article.volume = source.readString();
            article.weightUOM = source.readString();
            article.netWeight = source.readString();
            article.grossWeight = source.readString();
            article.articleCategory = source.readString();
            article.articleNo = source.readString();
            article.baseUOM = source.readString();
            article.materialGroup = source.readString();
            article.materialType = source.readString();
            article.materialDesc1 = source.readString();
            article.materialNo = source.readString();

            return article;
        }

        public ArticleHeader[] newArray(int size) {
            return new ArticleHeader[size];
        }
    };

    public static ArticleHeader getArticle(ArrayList<ArticleHeader> list, String searchString) {
        Log.e("Material No","" + searchString);
        for (ArticleHeader article : list) {
            if (article.getMaterialNo().equals(searchString)){
                Log.e("Inside if","Condition");
                return article;
            }
        }

        return null;
    }
}
