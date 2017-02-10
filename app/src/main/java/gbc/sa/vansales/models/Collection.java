package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 23-Jan-17.
 */
public class Collection implements Parcelable {
    private String customerNo;
    private String invoiceNo;
    private String invoiceAmount;
    private String invoiceDueDate;
    private String invoiceDate;
    private String amountCleared;
    private String isInvoiceComplete;
    public String getIndicator() {
        return indicator;
    }
    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }
    private String indicator;


    public String getAmountCleared() {
        return amountCleared;
    }
    public void setAmountCleared(String amountCleared) {
        this.amountCleared = amountCleared;
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    public String getInvoiceAmount() {
        return invoiceAmount;
    }
    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }
    public String getInvoiceDate() {
        return invoiceDate;
    }
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    public String getInvoiceDueDate() {
        return invoiceDueDate;
    }
    public void setInvoiceDueDate(String invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    public String getIsInvoiceComplete() {
        return isInvoiceComplete;
    }
    public void setIsInvoiceComplete(String isInvoiceComplete) {
        this.isInvoiceComplete = isInvoiceComplete;
    }

    public static final Creator<Collection> CREATOR = new Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel source) {
            Collection collection = new Collection();
            collection.amountCleared = source.readString();
            collection.customerNo = source.readString();
            collection.invoiceAmount = source.readString();
            collection.invoiceDate = source.readString();
            collection.invoiceDueDate = source.readString();
            collection.invoiceNo = source.readString();
            collection.isInvoiceComplete = source.readString();
            collection.indicator = source.readString();
            return collection;
        }
        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(amountCleared);
        parcel.writeString(customerNo);
        parcel.writeString(invoiceAmount);
        parcel.writeString(invoiceDate);
        parcel.writeString(invoiceDueDate);
        parcel.writeString(invoiceNo);
        parcel.writeString(isInvoiceComplete);
        parcel.writeString(indicator);
    }
}
