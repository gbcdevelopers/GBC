package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
/**
 * Created by Rakshit on 22-Dec-16.
 */

public class TripSalesArea implements Parcelable {

    private String tripId;
    private String visitListId;
    private Date date;
    private Date startDate;
    private String startTime;
    private String salesOrg;
    private String distChannel;
    private String division;

    public String getDistChannel() {
        return distChannel;
    }
    public void setDistChannel(String distChannel) {
        this.distChannel = distChannel;
    }
    public String getDivision() {
        return division;
    }
    public void setDivision(String division) {
        this.division = division;
    }
    public String getSalesOrg() {
        return salesOrg;
    }
    public void setSalesOrg(String salesOrg) {
        this.salesOrg = salesOrg;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
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
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(tripId);
        parcel.writeString(visitListId);
        parcel.writeSerializable(date);
        parcel.writeSerializable(startDate);
        parcel.writeString(startTime);
        parcel.writeString(salesOrg);
        parcel.writeString(distChannel);
        parcel.writeString(division);
    }

    public static final Creator<TripSalesArea> CREATOR = new Creator<TripSalesArea>() {
        public TripSalesArea createFromParcel(Parcel source) {
            TripSalesArea tripSalesArea = new TripSalesArea();

            tripSalesArea.tripId = source.readString();
            tripSalesArea.visitListId = source.readString();
            tripSalesArea.date = (Date)source.readSerializable();
            tripSalesArea.startDate = (Date)source.readSerializable();
            tripSalesArea.startTime = source.readString();
            tripSalesArea.salesOrg = source.readString();
            tripSalesArea.distChannel = source.readString();
            tripSalesArea.division = source.readString();

            return tripSalesArea;
        }

        public TripSalesArea[] newArray(int size) {
            return new TripSalesArea[size];
        }
    };
}
