package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 03-Jan-17.
 */
public class DeliveryOrderList implements Parcelable{
    private String orderId;
    private String orderDate;
    private String orderStatus;
    private String orderReferenceNo;

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
    public String getOrderReferenceNo() {
        return orderReferenceNo;
    }
    public void setOrderReferenceNo(String orderReferenceNo) {
        this.orderReferenceNo = orderReferenceNo;
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
        parcel.writeString(orderReferenceNo);
    }

    public static final Creator<DeliveryOrderList> CREATOR = new Creator<DeliveryOrderList>() {
        public DeliveryOrderList createFromParcel(Parcel source) {
            DeliveryOrderList order = new DeliveryOrderList();

            order.orderId = source.readString();
            order.orderDate = source.readString();
            order.orderStatus = source.readString();
            order.orderReferenceNo = source.readString();
            return order;
        }

        public DeliveryOrderList[] newArray(int size) {
            return new DeliveryOrderList[size];
        }
    };
}
