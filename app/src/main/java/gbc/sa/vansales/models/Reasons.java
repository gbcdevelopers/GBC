package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 17-Jan-17.
 */
public class Reasons implements Parcelable {
    private String reasonID;
    private String reasonType;
    private String reasonDescription;

    public String getReasonDescription() {
        return reasonDescription;
    }
    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }
    public String getReasonID() {
        return reasonID;
    }
    public void setReasonID(String reasonID) {
        this.reasonID = reasonID;
    }
    public String getReasonType() {
        return reasonType;
    }
    public void setReasonType(String reasonType) {
        this.reasonType = reasonType;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(reasonID);
        parcel.writeString(reasonDescription);
        parcel.writeString(reasonType);
    }

    public static final Creator<Reasons> CREATOR = new Creator<Reasons>() {
        @Override
        public Reasons createFromParcel(Parcel source) {
            Reasons reasons = new Reasons();

            reasons.reasonID = source.readString();
            reasons.reasonDescription = source.readString();
            reasons.reasonType = source.readString();

            return reasons;
        }
        @Override
        public Reasons[] newArray(int size) {
            return new Reasons[size];
        }
    };
}
