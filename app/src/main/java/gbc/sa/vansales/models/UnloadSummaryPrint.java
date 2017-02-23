package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 22-Feb-17.
 */
public class UnloadSummaryPrint implements Parcelable{
    private String itemNo;
    private String itemDescription;
    private String vanStock;
    private String endingInventory;
    private String freshUnload;
    private String truckDamage;
    private String badReturns;
    private String varianceQty;
    private String varianceAmount;
    private String itemPrice;

    public String getBadReturns() {
        return badReturns;
    }
    public void setBadReturns(String badReturns) {
        this.badReturns = badReturns;
    }
    public String getEndingInventory() {
        return endingInventory;
    }
    public void setEndingInventory(String endingInventory) {
        this.endingInventory = endingInventory;
    }
    public String getFreshUnload() {
        return freshUnload;
    }
    public void setFreshUnload(String freshUnload) {
        this.freshUnload = freshUnload;
    }
    public String getItemDescription() {
        return itemDescription;
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public String getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
    public String getTruckDamage() {
        return truckDamage;
    }
    public void setTruckDamage(String truckDamage) {
        this.truckDamage = truckDamage;
    }
    public String getVanStock() {
        return vanStock;
    }
    public void setVanStock(String vanStock) {
        this.vanStock = vanStock;
    }
    public String getVarianceAmount() {
        return varianceAmount;
    }
    public void setVarianceAmount(String varianceAmount) {
        this.varianceAmount = varianceAmount;
    }
    public String getVarianceQty() {
        return varianceQty;
    }
    public void setVarianceQty(String varianceQty) {
        this.varianceQty = varianceQty;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(itemNo);
        parcel.writeString(itemDescription);
        parcel.writeString(vanStock);
        parcel.writeString(endingInventory);
        parcel.writeString(freshUnload);
        parcel.writeString(truckDamage);
        parcel.writeString(badReturns);
        parcel.writeString(varianceQty);
        parcel.writeString(varianceAmount);
        parcel.writeString(itemPrice);
    }

    public static final Parcelable.Creator<UnloadSummaryPrint> CREATOR = new Parcelable.Creator<UnloadSummaryPrint>() {
        @Override
        public UnloadSummaryPrint createFromParcel(Parcel source) {
            UnloadSummaryPrint unload = new UnloadSummaryPrint();

            unload.itemNo = source.readString();
            unload.itemDescription = source.readString();
            unload.vanStock = source.readString();
            unload.endingInventory = source.readString();
            unload.freshUnload = source.readString();
            unload.truckDamage = source.readString();
            unload.badReturns = source.readString();
            unload.varianceQty = source.readString();
            unload.varianceAmount = source.readString();
            unload.itemPrice = source.readString();
            return unload;
        }
        @Override
        public UnloadSummaryPrint[] newArray(int size) {
            return new UnloadSummaryPrint[size];
        }
    };
}
