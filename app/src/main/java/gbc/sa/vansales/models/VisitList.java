package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
/**
 * Created by Rakshit on 21-Dec-16.
 */

/*
private static final String KEY_TRIP_ID = "tripId";
    private static final String KEY_VISITLISTID = "visitListId";
    private static final String KEY_ITEMNO = "itemNo";
    private static final String KEY_CUSTOMER_NO = "customerNo";
    private static final String KEY_EXEC_DATE = "execDate";
    private static final String KEY_DRIVER = "driver1";
    private static final String KEY_VP_TYPE = "vpType";

 */

public class VisitList implements Parcelable{
    private String tripId;
    private String visitListId;
    private String itemNo;
    private String customerNo;
    private Date execDate;
    private String driver1;
    private String vpType;

    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    public String getDriver1() {
        return driver1;
    }
    public void setDriver1(String driver1) {
        this.driver1 = driver1;
    }
    public Date getExecDate() {
        return execDate;
    }
    public void setExecDate(Date execDate) {
        this.execDate = execDate;
    }
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public String getTripId() {
        return tripId;
    }
    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
    public String getVisitListId() {
        return visitListId;
    }
    public void setVisitListId(String visitListId) {
        this.visitListId = visitListId;
    }
    public String getVpType() {
        return vpType;
    }
    public void setVpType(String vpType) {
        this.vpType = vpType;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(tripId);
        parcel.writeString(visitListId);
        parcel.writeSerializable(execDate);
        parcel.writeString(itemNo);
        parcel.writeString(customerNo);
        parcel.writeString(driver1);
        parcel.writeString(vpType);
    }

    public static final Creator<VisitList> CREATOR = new Creator<VisitList>() {
        public VisitList createFromParcel(Parcel source) {
            VisitList visitList = new VisitList();

            visitList.tripId = source.readString();
            visitList.visitListId = source.readString();
            visitList.execDate = (Date)source.readSerializable();
            visitList.itemNo = source.readString();
            visitList.customerNo = source.readString();
            visitList.driver1 = source.readString();
            visitList.vpType = source.readString();

            return visitList;
        }

        public VisitList[] newArray(int size) {
            return new VisitList[size];
        }
    };
}
