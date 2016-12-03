package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 21-Nov-16.
 */
public class GoodReturn implements Parcelable{
    private String item_sku;
    private String item_description;
    private String quantitycs;
    private String quantitybt;
    private String return_type;
    private String reason;

    public GoodReturn(){

    }

    public String getItemSKU(){
        return item_sku;
    }
    public void setItemSKU(String item_sku){
        this.item_sku = item_sku;
    }

    public String getItemDescription(){
        return item_description;
    }
    public void setItemDescription(String item_description){
        this.item_description = item_description;
    }

    public String getQuantityCS(){
        return quantitycs;
    }
    public void setQuantityCS(String quantitycs){
        this.quantitycs = quantitycs;
    }

    public String getQuantityBT(){
        return quantitybt;
    }
    public void setQuantityBT(String quantitybt){
        this.quantitybt = quantitybt;
    }

    public String getReturnType(){
        return return_type;
    }
    public void setReturnType(String return_type){
        this.return_type = return_type;
    }

    public String getReason(){
        return reason;
    }
    public void setReason(String reason){
        this.reason = reason;
    }

    public static final Parcelable.Creator<GoodReturn> CREATOR = new Parcelable.Creator<GoodReturn>() {
        @Override
        public GoodReturn createFromParcel(Parcel source) {
            GoodReturn item = new GoodReturn();

            item.item_sku = source.readString();
            item.item_description = source.readString();
            item.quantitycs = source.readString();
            item.quantitybt = source.readString();
            item.return_type = source.readString();
            item.reason = source.readString();

            return item;
        }
        @Override
        public GoodReturn[] newArray(int size) {
            return new GoodReturn[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(item_sku);
        parcel.writeString(item_description);
        parcel.writeString(quantitycs);
        parcel.writeString(quantitybt);
        parcel.writeString(return_type);
        parcel.writeString(reason);

    }
}
