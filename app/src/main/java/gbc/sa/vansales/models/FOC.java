package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 03-Feb-17.
 */
public class FOC implements Parcelable{
    private String customerNo;
    private String qualifyingItem;
    private String assigningItem;
    private String qualifyingQuantity;
    private String assigningQuantity;
    private String dateFrom;
    private String dateTo;

    public String getAssigningItem() {
        return assigningItem;
    }
    public void setAssigningItem(String assigningItem) {
        this.assigningItem = assigningItem;
    }
    public String getAssigningQuantity() {
        return assigningQuantity;
    }
    public void setAssigningQuantity(String assigningQuantity) {
        this.assigningQuantity = assigningQuantity;
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    public String getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }
    public String getDateTo() {
        return dateTo;
    }
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }
    public String getQualifyingItem() {
        return qualifyingItem;
    }
    public void setQualifyingItem(String qualifyingItem) {
        this.qualifyingItem = qualifyingItem;
    }
    public String getQualifyingQuantity() {
        return qualifyingQuantity;
    }
    public void setQualifyingQuantity(String qualifyingQuantity) {
        this.qualifyingQuantity = qualifyingQuantity;
    }

    public static final Creator<FOC> CREATOR = new Creator<FOC>() {
        @Override
        public FOC createFromParcel(Parcel source) {
            FOC foc = new FOC();
            foc.customerNo = source.readString();
            foc.qualifyingItem = source.readString();
            foc.assigningItem = source.readString();
            foc.qualifyingQuantity = source.readString();
            foc.assigningQuantity = source.readString();
            foc.dateFrom = source.readString();
            foc.dateTo = source.readString();
            return foc;
        }
        @Override
        public FOC[] newArray(int size) {
            return new FOC[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(customerNo);
        parcel.writeString(qualifyingItem);
        parcel.writeString(assigningItem);
        parcel.writeString(qualifyingQuantity);
        parcel.writeString(assigningQuantity);
        parcel.writeString(dateFrom);
        parcel.writeString(dateTo);
    }
}
