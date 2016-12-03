package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 16-Nov-16.
 */
public class ShopStatus implements Parcelable {
    private String status_id;
    private String status_text;
    private boolean selected;

    //Get Instance of the Customer
    public ShopStatus(){
    }

    public String getStatusID(){
        return status_id;
    }

    public void setStatusID(String statusid){
        this.status_id = statusid;
    }

    public String getStatusText(){
        return status_text;
    }
    public void setStatusText(String status_text){
        this.status_text = status_text;
    }

    public boolean isSelected(){
        return selected;
    }
    public void setSelected(boolean selected){
        this.selected = selected;
    }
    public static final Creator<ShopStatus> CREATOR = new Creator<ShopStatus>() {
        @Override
        public ShopStatus createFromParcel(Parcel source) {
            ShopStatus status = new ShopStatus();

            status.status_id = source.readString();
            status.status_text = source.readString();
            status.selected = source.readByte()!=0;
            return status;
        }
        @Override
        public ShopStatus[] newArray(int size) {
            return new ShopStatus[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(status_id);
        parcel.writeString(status_text);
        parcel.writeByte((byte) (selected ? 1 : 0));
    }
}
