package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 10-Jan-17.
 */
public class OfflineResponse implements Parcelable{
    private String response_code;
    private String response_message;
    private String orderID;
    private String purchaseNumber;
    private String function;
    private String customerID;

    public String getCustomerID() {
        return customerID;
    }
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
    public String getResponse_message() {
        return response_message;
    }
    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }
    public String getFunction() {
        return function;
    }
    public void setFunction(String function) {
        this.function = function;
    }
    public String getOrderID() {
        return orderID;
    }
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }
    public String getPurchaseNumber() {
        return purchaseNumber;
    }
    public void setPurchaseNumber(String purchaseNumber) {
        this.purchaseNumber = purchaseNumber;
    }
    public String getResponse_code() {
        return response_code;
    }
    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public static final Creator<OfflineResponse> CREATOR = new Creator<OfflineResponse>() {
        public OfflineResponse createFromParcel(Parcel source) {
            OfflineResponse response = new OfflineResponse();

            response.response_code = source.readString();
            response.response_message = source.readString();
            response.orderID = source.readString();
            response.purchaseNumber = source.readString();
            response.function = source.readString();
            response.customerID = source.readString();
            return response;
        }

        public OfflineResponse[] newArray(int size) {
            return new OfflineResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(response_code);
        parcel.writeString(response_message);
        parcel.writeString(orderID);
        parcel.writeString(purchaseNumber);
        parcel.writeString(function);
        parcel.writeString(customerID);
    }
}
