package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 22-Feb-17.
 */
public class DepositReport implements Parcelable {
    private String invoiceNo;
    private String customerId;
    private String customerNo;
    private String customerName;
    private String chequeNo;
    private String chequeDate;
    private String bankCode;
    private String bankName;
    private String chequeAmount;
    private String cashAmount;

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
    public String getCashAmount() {
        return cashAmount;
    }
    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }
    public String getChequeAmount() {
        return chequeAmount;
    }
    public void setChequeAmount(String chequeAmount) {
        this.chequeAmount = chequeAmount;
    }
    public String getChequeDate() {
        return chequeDate;
    }
    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }
    public String getChequeNo() {
        return chequeNo;
    }
    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
        parcel.writeString(invoiceNo);
        parcel.writeString(customerId);
        parcel.writeString(customerName);
        parcel.writeString(chequeNo);
        parcel.writeString(chequeDate);
        parcel.writeString(chequeAmount);
        parcel.writeString(bankCode);
        parcel.writeString(bankName);
        parcel.writeString(cashAmount);
    }

    public static final Creator<DepositReport> CREATOR = new Creator<DepositReport>() {
        @Override
        public DepositReport createFromParcel(Parcel source) {
            DepositReport depositReport = new DepositReport();
            depositReport.invoiceNo = source.readString();
            depositReport.customerId = source.readString();
            depositReport.customerName = source.readString();
            depositReport.chequeNo = source.readString();
            depositReport.chequeDate = source.readString();
            depositReport.bankCode = source.readString();
            depositReport.bankName = source.readString();
            depositReport.chequeAmount = source.readString();
            depositReport.cashAmount = source.readString();

            return depositReport;
        }
        @Override
        public DepositReport[] newArray(int size) {
            return new DepositReport[size];
        }
    };
}
