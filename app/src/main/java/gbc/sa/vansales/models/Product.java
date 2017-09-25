package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 18-Nov-16.
 */
public class Product implements Parcelable {
    private String sku;
    private String product_id;
    private String product_description;
    private String quantity_cs;
    private String quantity_bt;

    //Get instance of Products
    public Product(){
    }

    //Getter and Setter Methods
    public String getSKU(){
        return sku;
    }
    public void setSKU(String sku){
        this.sku = sku;
    }

    public String getProductID(){
        return product_id;
    }
    public void setProductID(String product_id){
        this.product_id = product_id;
    }

    public String getProductDescription(){
        return product_description;
    }
    public void setProductDescription(String product_description){
        this.product_description = product_description;
    }

    public String getQuantityCS(){
        return quantity_cs;
    }
    public void setQuantityCS(String quantity_cs){
        this.quantity_cs = quantity_cs;
    }

    public String getQuantityBT(){
        return quantity_bt;
    }
    public void setQuantityBT(String quantity_bt){
        this.quantity_bt = quantity_bt;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            Product product = new Product();

            product.sku = source.readString();
            product.product_id = source.readString();
            product.product_description = source.readString();
            product.quantity_cs = source.readString();
            product.quantity_bt = source.readString();

            return product;
        }
        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(sku);
        parcel.writeString(product_id);
        parcel.writeString(product_description);
        parcel.writeString(quantity_cs);
        parcel.writeString(quantity_bt);
    }
}
