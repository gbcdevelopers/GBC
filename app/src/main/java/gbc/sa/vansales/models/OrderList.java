package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 03-Jan-17.
 */
public class OrderList implements Parcelable{
    private String orderId;
    private String orderDate;
    private String orderStatus;

    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(orderId);
        parcel.writeString(orderDate);
        parcel.writeString(orderStatus);
    }

    public static final Creator<OrderList> CREATOR = new Creator<OrderList>() {
        public OrderList createFromParcel(Parcel source) {
            OrderList order = new OrderList();

            order.orderId = source.readString();
            order.orderDate = source.readString();
            order.orderStatus = source.readString();
            return order;
        }

        public OrderList[] newArray(int size) {
            return new OrderList[size];
        }
    };
}
