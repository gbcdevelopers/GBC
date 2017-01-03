package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 29-Dec-16.
 */
public class OrderRequest implements Parcelable {
    private String itemCode;
    private String itemCategory;
    private String itemName;
    private String materialNo;
    private String cases;
    private String units;
    private String price;
    private String uom;


    public static final Creator<OrderRequest> CREATOR = new Creator<OrderRequest>() {
        @Override
        public OrderRequest createFromParcel(Parcel source) {
            OrderRequest loadRequest = new OrderRequest();
            loadRequest.itemCode = source.readString();
            loadRequest.itemCategory = source.readString();
            loadRequest.materialNo = source.readString();
            loadRequest.cases = source.readString();
            loadRequest.units = source.readString();
            loadRequest.price = source.readString();
            loadRequest.uom = source.readString();
            loadRequest.itemName = source.readString();
            return loadRequest;
        }
        @Override
        public OrderRequest[] newArray(int size) {
            return new OrderRequest[size];
        }
    };
    public String getCases() {
        return cases;
    }
    public void setCases(String cases) {
        this.cases = cases;
    }
    public String getItemCategory() {
        return itemCategory;
    }
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getMaterialNo() {
        return materialNo;
    }
    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getUnits() {
        return units;
    }
    public void setUnits(String units) {
        this.units = units;
    }
    public String getUom() {
        return uom;
    }
    public void setUom(String uom) {
        this.uom = uom;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(itemCode);
        parcel.writeString(itemCategory);
        parcel.writeString(materialNo);
        parcel.writeString(cases);
        parcel.writeString(units);
        parcel.writeString(price);
        parcel.writeString(uom);
        parcel.writeString(itemName);
    }
}
