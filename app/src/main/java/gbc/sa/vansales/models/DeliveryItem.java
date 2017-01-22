package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 04-Jan-17.
 */
public class DeliveryItem implements Parcelable{
    private String itemCode;
    private String itemDescription;
    private String materialNo;
    private String itemCase;
    private String itemUnits;
    private String itemUom;
    private String amount;
    private String reasonCode;
    private boolean isAltUOM;

    public String getItemUom() {
        return itemUom;
    }
    public void setItemUom(String itemUom) {
        this.itemUom = itemUom;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getItemCase() {
        return itemCase;
    }
    public void setItemCase(String itemCase) {
        this.itemCase = itemCase;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemDescription() {
        return itemDescription;
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    public String getItemUnits() {
        return itemUnits;
    }
    public void setItemUnits(String itemUnits) {
        this.itemUnits = itemUnits;
    }
    public String getMaterialNo() {
        return materialNo;
    }
    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }
    public boolean isAltUOM() {
        return isAltUOM;
    }
    public void setIsAltUOM(boolean isAltUOM) {
        this.isAltUOM = isAltUOM;
    }
    public String getReasonCode() {
        return reasonCode;
    }
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(itemCode);
        parcel.writeString(itemDescription);
        parcel.writeString(itemCase);
        parcel.writeString(itemUnits);
        parcel.writeString(materialNo);
        parcel.writeString(amount);
        parcel.writeString(itemUom);
        parcel.writeString(reasonCode);
        parcel.writeByte((byte) (isAltUOM ? 1 : 0));
    }

    public static final Parcelable.Creator<DeliveryItem> CREATOR = new Parcelable.Creator<DeliveryItem>() {
        @Override
        public DeliveryItem createFromParcel(Parcel source) {
            DeliveryItem item = new DeliveryItem();

            item.itemCode = source.readString();
            item.itemDescription = source.readString();
            item.itemCase = source.readString();
            item.itemUnits = source.readString();
            item.materialNo = source.readString();
            item.amount = source.readString();
            item.itemUom = source.readString();
            item.reasonCode = source.readString();
            item.isAltUOM = source.readByte()!=0;
            return item;
        }
        @Override
        public DeliveryItem[] newArray(int size) {
            return new DeliveryItem[size];
        }
    };
}
