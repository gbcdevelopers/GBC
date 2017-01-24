package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 24-Jan-17.
 */
public class Expense implements Parcelable{
    private String expenseCode;
    private String expenseDescription;
    private String expenseAmount;
    private String additionalReason;



    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel source) {
            Expense expense = new Expense();
            expense.expenseCode = source.readString();
            expense.expenseDescription = source.readString();
            expense.expenseAmount = source.readString();
            expense.additionalReason = source.readString();
            return expense;
        }
        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
    public String getAdditionalReason() {
        return additionalReason;
    }
    public void setAdditionalReason(String additionalReason) {
        this.additionalReason = additionalReason;
    }
    public String getExpenseAmount() {
        return expenseAmount;
    }
    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }
    public String getExpenseCode() {
        return expenseCode;
    }
    public void setExpenseCode(String expenseCode) {
        this.expenseCode = expenseCode;
    }
    public String getExpenseDescription() {
        return expenseDescription;
    }
    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(expenseCode);
        parcel.writeString(expenseDescription);
        parcel.writeString(expenseAmount);
        parcel.writeString(additionalReason);
    }
}
