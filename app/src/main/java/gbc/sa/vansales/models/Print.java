package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 13-Jan-17.
 */
public class Print implements Parcelable{
    private String customer_id;
    private String customer_name;
    private String referenceNumber;
    private String transactionType;
    private boolean isPosted;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }
    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
    public boolean isPosted() {
        return isPosted;
    }
    public void setIsPosted(boolean isPosted) {
        this.isPosted = isPosted;
    }
    public String getCustomer_id() {
        return customer_id;
    }
    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }
    public String getCustomer_name() {
        return customer_name;
    }
    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }
    public String getReferenceNumber() {
        return referenceNumber;
    }
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(customer_id);
        parcel.writeString(customer_name);
        parcel.writeString(referenceNumber);
        parcel.writeString(transactionType);
        parcel.writeByte((byte)(isPosted?1:0));
        parcel.writeByte((byte)(isChecked?1:0));
    }

    public static final Creator<Print> CREATOR = new Creator<Print>() {
        @Override
        public Print createFromParcel(Parcel source) {
            Print print = new Print();

            print.customer_id = source.readString();
            print.customer_name = source.readString();
            print.referenceNumber = source.readString();
            print.transactionType = source.readString();
            print.isPosted = source.readByte()!=0;
            print.isChecked = source.readByte()!=0;
            return print;
        }
        @Override
        public Print[] newArray(int size) {
            return new Print[size];
        }
    };
}
