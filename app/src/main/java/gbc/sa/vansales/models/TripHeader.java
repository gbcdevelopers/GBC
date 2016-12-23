package gbc.sa.vansales.models;
/**
 * Created by Rakshit on 21-Dec-16.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TripHeader implements Parcelable {

    private String tripId;
    private String visitListId;
    private String route;
    private String driver;
    private String truck;
    private Date plannedStartDate;
    private Date actualStartDate;
    private String tourType;
    private String creationTime;
    private String createdBy;
    private String settledBy;
    private boolean downloadStatus;
    private boolean uploadStatus;
    private String loads;


    public String getTripId(){
        return this.tripId;
    }
    public void setTripId(String tripId){
        this.tripId = tripId;
    }

    public String getVisitListId(){
        return this.visitListId;
    }
    public void setVisitListId(String visitListId){
        this.visitListId = visitListId;
    }

    public String getRoute(){
        return this.route;
    }
    public void setRoute(String route){
        this.route = route;
    }

    public String getDriver(){
        return this.driver;
    }
    public void setDriver(String driver){
        this.driver = driver;
    }

    public String getTruck(){
        return this.truck;
    }
    public void setTruck(String truck){
        this.truck = truck;
    }

    public Date getPlannedStartDate(){
        return this.plannedStartDate;
    }
    public void setPlannedStartDate(Date plannedStartDate){
        this.plannedStartDate = plannedStartDate;
    }

    public Date getActualStartDate(){
        return this.actualStartDate;
    }
    public void setActualStartDate(Date actualStartDate){
        this.actualStartDate = actualStartDate;
    }

    public String getTourType(){
        return this.tourType;
    }
    public void setTourType(String tourType){
        this.tourType = tourType;
    }

    public String getCreationTime(){
       return this.creationTime;
    }
    public void setCreationTime(String creationTime){
        this.creationTime = creationTime;
    }

    public String getCreatedBy(){
        return this.createdBy;
    }
    public void setCreatedBy(String createdBy){
        this.createdBy = createdBy;
    }

    public String getSettledBy(){
        return this.settledBy;
    }
    public void setSettledBy(String settledBy){
        this.settledBy = settledBy;
    }

    public boolean getDownloadStatus(){
        return this.downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus){
        this.downloadStatus = downloadStatus.equals("X") ? true : false;
    }

    public boolean getUploadStatus(){
        return this.uploadStatus;
    }
    public void setUploadStatus(String uploadStatus){
        this.uploadStatus = uploadStatus.equals("X") ? true : false;
    }

    public String getLoads(){
        return this.loads;
    }
    public void setLoads(String loads){
        this.loads = loads;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(tripId);
        parcel.writeString(visitListId);
        parcel.writeString(route);
        parcel.writeString(driver);
        parcel.writeString(truck);
        parcel.writeSerializable(plannedStartDate);
        parcel.writeSerializable(actualStartDate);
        parcel.writeString(tourType);
        parcel.writeString(creationTime);
        parcel.writeString(createdBy);
        parcel.writeString(settledBy);
        parcel.writeByte((byte) (downloadStatus ? 1:0));
        parcel.writeByte((byte) (uploadStatus ? 1:0));
        parcel.writeString(loads);
    }

    public static final Creator<TripHeader> CREATOR = new Creator<TripHeader>() {
        public TripHeader createFromParcel(Parcel source) {
            TripHeader tripHeader = new TripHeader();

            tripHeader.tripId = source.readString();
            tripHeader.visitListId = source.readString();
            tripHeader.route = source.readString();
            tripHeader.driver = source.readString();
            tripHeader.truck  = source.readString();
            tripHeader.plannedStartDate = (Date) source.readSerializable();
            tripHeader.actualStartDate = (Date) source.readSerializable();
            tripHeader.tourType = source.readString();
            tripHeader.creationTime = source.readString();
            tripHeader.createdBy = source.readString();
            tripHeader.settledBy = source.readString();
            tripHeader.downloadStatus = source.readByte()!=0;
            tripHeader.uploadStatus = source.readByte()!=0;
            tripHeader.loads = source.readString();
            return tripHeader;
        }

        public TripHeader[] newArray(int size) {
            return new TripHeader[size];
        }
    };

}
