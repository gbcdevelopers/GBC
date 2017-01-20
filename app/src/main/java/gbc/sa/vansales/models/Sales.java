package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by eheuristic on 24/12/2016.
 */

public class Sales implements Parcelable {
    private String item_code;
    private String item_category;
    private String material_no;
    private String material_description;
    private String uom;
    private String name;
    private String price;
    private String pic;
    private String cases;
    private String inv_cases;
    private String inv_piece;
    private String reasonCode;
    public String getReasonCode() {
        return reasonCode;
    }
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    public String getMaterial_description() {
        return material_description;
    }
    public void setMaterial_description(String material_description) {
        this.material_description = material_description;
    }
    public String getUom() {
        return uom;
    }
    public void setUom(String uom) {
        this.uom = uom;
    }
    public String getItem_category() {
        return item_category;
    }
    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }
    public String getItem_code() {
        return item_code;
    }
    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }
    public String getMaterial_no() {
        return material_no;
    }
    public void setMaterial_no(String material_no) {
        this.material_no = material_no;
    }


    public String getInv_cases() {
        return inv_cases;
    }
    public void setInv_cases(String inv_cases) {
        this.inv_cases = inv_cases;
    }
    public String getInv_piece() {
        return inv_piece;
    }
    public void setInv_piece(String inv_piece) {
        this.inv_piece = inv_piece;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(item_code);
        parcel.writeString(item_category);
        parcel.writeString(material_no);
        parcel.writeString(material_description);
        parcel.writeString(uom);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(pic);
        parcel.writeString(cases);
        parcel.writeString(inv_cases);
        parcel.writeString(inv_piece);
        parcel.writeString(reasonCode);
    }

    public static final Creator<Sales> CREATOR = new Creator<Sales>() {
        @Override
        public Sales createFromParcel(Parcel source) {
            Sales sale = new Sales();

            sale.item_code = source.readString();
            sale.item_category = source.readString();
            sale.material_no = source.readString();
            sale.material_description = source.readString();
            sale.uom = source.readString();
            sale.name = source.readString();
            sale.price = source.readString();
            sale.pic = source.readString();
            sale.cases = source.readString();
            sale.inv_cases = source.readString();
            sale.inv_piece = source.readString();
            sale.reasonCode = source.readString();
            return sale;
        }
        @Override
        public Sales[] newArray(int size) {
            return new Sales[size];
        }
    };
}
