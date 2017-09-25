package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
/**
 * Created by Rakshit on 02-Jan-17.
 */
public class Message implements Parcelable {
    private String id;
    private String structure;
    private String message;
    private String driver;

    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStructure() {
        return structure;
    }
    public void setStructure(String structure) {
        this.structure = structure;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(structure);
        parcel.writeString(message);
        parcel.writeString(driver);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        public Message createFromParcel(Parcel source) {
            Message message = new Message();

            message.id = source.readString();
            message.structure = source.readString();
            message.message = source.readString();
            message.driver = source.readString();

            return message;
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
