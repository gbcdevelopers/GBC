package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 20-Jan-17.
 */
public class CustomerStatus implements Parcelable{
    private String customerId;
    private String reasonCode;
    private String reasonDescription;


    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getReasonCode() {
        return reasonCode;
    }
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    public String getReasonDescription() {
        return reasonDescription;
    }
    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }

    public static final Parcelable.Creator<CustomerStatus> CREATOR = new Parcelable.Creator<CustomerStatus>() {
        @Override
        public CustomerStatus createFromParcel(Parcel source) {
            CustomerStatus item = new CustomerStatus();

            item.customerId = source.readString();
            item.reasonCode = source.readString();
            item.reasonDescription = source.readString();
            return item;
        }
        @Override
        public CustomerStatus[] newArray(int size) {
            return new CustomerStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(customerId);
        parcel.writeString(reasonCode);
        parcel.writeString(reasonDescription);
    }
}
