package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 18-Nov-16.
 */
public class OrderSummary implements Parcelable {
    private String product_id;
    private String product_description;
    private String quantity_cs;
    private String quantity_bt;
    private String tradeprice;
    private String discount;
    private String totalprice;

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

    public String getTradeprice(){
        return tradeprice;
    }
    public void setTradeprice(String tradeprice){
        this.tradeprice = tradeprice;
    }

    public String getTotalprice(){
        return totalprice;
    }
    public void setTotalprice(String totalprice){
        this.totalprice = totalprice;
    }

    public String getDiscount(){
        return discount;
    }
    public void setDiscount(String discount){
        this.discount = discount;
    }

    public static final Parcelable.Creator<OrderSummary> CREATOR = new Parcelable.Creator<OrderSummary>() {
        @Override
        public OrderSummary createFromParcel(Parcel source) {
            OrderSummary order = new OrderSummary();

            order.product_id = source.readString();
            order.product_description = source.readString();
            order.quantity_cs = source.readString();
            order.quantity_bt = source.readString();
            order.tradeprice = source.readString();
            order.totalprice = source.readString();
            order.discount = source.readString();

            return order;
        }
        @Override
        public OrderSummary[] newArray(int size) {
            return new OrderSummary[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(product_id);
        parcel.writeString(product_description);
        parcel.writeString(quantity_cs);
        parcel.writeString(quantity_bt);
        parcel.writeString(tradeprice);
        parcel.writeString(totalprice);
        parcel.writeString(discount);
    }
}
