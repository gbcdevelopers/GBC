package gbc.sa.vansales.models;
/**
 * Created by Rakshit on 21-Dec-16.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class TripHeader {

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

}
