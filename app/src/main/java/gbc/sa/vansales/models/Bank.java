package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 23-Jan-17.
 */
public class Bank implements Parcelable{
    private String bankCode;
    private String bankName;

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel source) {
            Bank bank = new Bank();

            bank.bankCode = source.readString();
            bank.bankName = source.readString();

            return bank;
        }
        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };
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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(bankCode);
        parcel.writeString(bankName);
    }




}
