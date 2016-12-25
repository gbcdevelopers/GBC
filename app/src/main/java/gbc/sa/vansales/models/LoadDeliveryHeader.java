package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
/**
 * Created by Rakshit on 25-Dec-16.
 */
public class LoadDeliveryHeader implements Parcelable{
    private String deliveryNo;
    private boolean loadVerified;
    private String loadingDate;
    private String availableLoad;
    private String deliveryType;

    public String getDeliveryType() {
        return deliveryType;
    }
    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
    public String getAvailableLoad() {
        return availableLoad;
    }
    public void setAvailableLoad(String availableLoad) {
        this.availableLoad = availableLoad;
    }
    public String getDeliveryNo() {
        return deliveryNo;
    }
    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }
    public String getLoadingDate() {
        return loadingDate;
    }
    public void setLoadingDate(String loadingDate) {
        this.loadingDate = loadingDate;
    }
    public boolean isLoadVerified() {
        return loadVerified;
    }
    public void setLoadVerified(boolean loadVerified) {
        this.loadVerified = loadVerified;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(deliveryNo);
        parcel.writeString(loadingDate);
        parcel.writeByte((byte) (loadVerified ? 1 : 0));
        parcel.writeString(availableLoad);
        parcel.writeString(deliveryType);
    }

    public static final Creator<LoadDeliveryHeader> CREATOR = new Creator<LoadDeliveryHeader>() {
        public LoadDeliveryHeader createFromParcel(Parcel source) {
            LoadDeliveryHeader loadHeader = new LoadDeliveryHeader();

            loadHeader.deliveryNo = source.readString();
            loadHeader.loadingDate = source.readString();
            loadHeader.loadVerified = source.readByte() !=0;
            loadHeader.availableLoad = source.readString();
            loadHeader.deliveryType = source.readString();

            return loadHeader;
        }

        public LoadDeliveryHeader[] newArray(int size) {
            return new LoadDeliveryHeader[size];
        }
    };
}
