package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 23-Feb-17.
 */
public class SalesSummary implements Parcelable{
    private String transactionNo;
    private String customerNo;
    private String customerName;
    private String transactionType;
    private String totalSales;
    private String totalReturns;
    private String totalgoodReturns;
    private String discounts;
    private String netSales;
    private String amountPaid;
    private String amountDue;

    public String getAmountDue() {
        return amountDue;
    }
    public void setAmountDue(String amountDue) {
        this.amountDue = amountDue;
    }
    public String getAmountPaid() {
        return amountPaid;
    }
    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
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
    public String getDiscounts() {
        return discounts;
    }
    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }
    public String getNetSales() {
        return netSales;
    }
    public void setNetSales(String netSales) {
        this.netSales = netSales;
    }
    public String getTotalgoodReturns() {
        return totalgoodReturns;
    }
    public void setTotalgoodReturns(String totalgoodReturns) {
        this.totalgoodReturns = totalgoodReturns;
    }
    public String getTotalReturns() {
        return totalReturns;
    }
    public void setTotalReturns(String totalReturns) {
        this.totalReturns = totalReturns;
    }
    public String getTotalSales() {
        return totalSales;
    }
    public void setTotalSales(String totalSales) {
        this.totalSales = totalSales;
    }
    public String getTransactionNo() {
        return transactionNo;
    }
    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
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
        parcel.writeString(transactionNo);
        parcel.writeString(customerNo);
        parcel.writeString(customerName);
        parcel.writeString(transactionType);
        parcel.writeString(totalSales);
        parcel.writeString(totalReturns);
        parcel.writeString(totalgoodReturns);
        parcel.writeString(netSales);
        parcel.writeString(discounts);
        parcel.writeString(amountPaid);
        parcel.writeString(amountDue);
    }

    public static final Creator<SalesSummary> CREATOR = new Creator<SalesSummary>() {
        @Override
        public SalesSummary createFromParcel(Parcel source) {
            SalesSummary salesSummary = new SalesSummary();
            salesSummary.transactionNo = source.readString();
            salesSummary.customerNo = source.readString();
            salesSummary.customerName = source.readString();
            salesSummary.transactionType = source.readString();
            salesSummary.totalSales = source.readString();
            salesSummary.totalReturns = source.readString();
            salesSummary.totalgoodReturns = source.readString();
            salesSummary.netSales = source.readString();
            salesSummary.discounts = source.readString();
            salesSummary.amountPaid = source.readString();
            salesSummary.amountDue = source.readString();

            return salesSummary;
        }
        @Override
        public SalesSummary[] newArray(int size) {
            return new SalesSummary[size];
        }
    };
}
