package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 23-Feb-17.
 */
public class DamageReport implements Parcelable{
    private String itemNo;
    private String itemDescription;
    private String itemQuantity;
    private String itemVariance;
    private String itemPrice;

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
    public String getItemQuantity() {
        return itemQuantity;
    }
    public void setItemQuantity(String itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
    public String getItemVariance() {
        return itemVariance;
    }
    public void setItemVariance(String itemVariance) {
        this.itemVariance = itemVariance;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(itemNo);
        parcel.writeString(itemDescription);
        parcel.writeString(itemQuantity);
        parcel.writeString(itemVariance);
        parcel.writeString(itemPrice);
    }

    public static final Parcelable.Creator<DamageReport> CREATOR = new Parcelable.Creator<DamageReport>() {
        @Override
        public DamageReport createFromParcel(Parcel source) {
            DamageReport item = new DamageReport();

            item.itemNo = source.readString();
            item.itemDescription = source.readString();
            item.itemQuantity = source.readString();
            item.itemPrice = source.readString();
            item.itemVariance = source.readString();

            return item;
        }
        @Override
        public DamageReport[] newArray(int size) {
            return new DamageReport[size];
        }
    };
}
