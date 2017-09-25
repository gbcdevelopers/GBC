package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 11-Feb-17.
 */
public class ChequeCollection implements Parcelable{
    private String chequeNo;
    private String chequeAmount;
    private String customerNo;
    private String customerName;
    private String invoiceNo;
    private String bankCode;
    private String bankName;

    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getChequeAmount() {
        return chequeAmount;
    }
    public void setChequeAmount(String chequeAmount) {
        this.chequeAmount = chequeAmount;
    }
    public String getChequeNo() {
        return chequeNo;
    }
    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(chequeNo);
        parcel.writeString(chequeAmount);
        parcel.writeString(bankCode);
        parcel.writeString(bankName);
        parcel.writeString(customerNo);
        parcel.writeString(customerName);
    }

    public static final Creator<ChequeCollection> CREATOR = new Creator<ChequeCollection>() {
        @Override
        public ChequeCollection createFromParcel(Parcel source) {
            ChequeCollection chequeCollection = new ChequeCollection();
            chequeCollection.chequeNo = source.readString();
            chequeCollection.chequeAmount = source.readString();
            chequeCollection.bankCode = source.readString();
            chequeCollection.bankName = source.readString();
            chequeCollection.customerNo = source.readString();
            chequeCollection.customerName = source.readString();

            return chequeCollection;
        }
        @Override
        public ChequeCollection[] newArray(int size) {
            return new ChequeCollection[size];
        }
    };
}
