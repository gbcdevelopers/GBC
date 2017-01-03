package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 03-Jan-17.
 */
public class VanStock implements Parcelable{
    private String item_code;
    private String item_description;
    private String item_case;
    private String item_units;

    public String getItem_case() {
        return item_case;
    }
    public void setItem_case(String item_case) {
        this.item_case = item_case;
    }
    public String getItem_code() {
        return item_code;
    }
    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }
    public String getItem_description() {
        return item_description;
    }
    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }
    public String getItem_units() {
        return item_units;
    }
    public void setItem_units(String item_units) {
        this.item_units = item_units;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(item_code);
        parcel.writeString(item_description);
        parcel.writeString(item_case);
        parcel.writeString(item_units);
    }

    public static final Creator<VanStock> CREATOR = new Creator<VanStock>() {
        public VanStock createFromParcel(Parcel source) {
            VanStock item = new VanStock();
            item.item_code = source.readString();
            item.item_description = source.readString();
            item.item_case = source.readString();
            item.item_units = source.readString();
            return item;
        }

        public VanStock[] newArray(int size) {
            return new VanStock[size];
        }
    };
}
