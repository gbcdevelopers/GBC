package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.util.HashMap;
/**
 * Created by Rakshit on 09-Jan-17.
 */
public class OfflinePost implements Parcelable{
    String collectionName;
    HashMap<String,String> map;
    JSONArray deepEntity;

    public String getCollectionName() {
        return collectionName;
    }
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
    public JSONArray getDeepEntity() {
        return deepEntity;
    }
    public void setDeepEntity(JSONArray deepEntity) {
        this.deepEntity = deepEntity;
    }
    public HashMap<String, String> getMap() {
        return map;
    }
    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
    public static final Creator<OfflinePost> CREATOR = new Creator<OfflinePost>() {
        public OfflinePost createFromParcel(Parcel source) {
            OfflinePost offlinePost = new OfflinePost();

            return offlinePost;
        }

        public OfflinePost[] newArray(int size) {
            return new OfflinePost[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
       // parcel.writeMap(this.map);

    }
}
