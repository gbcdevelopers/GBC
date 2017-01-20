package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 17-Jan-17.
 */
public class Promotions implements Parcelable {
    private String promotionCode;
    private String promotionType;
    private boolean isMandatory;
    private String promotionDescription;

    public boolean isMandatory() {
        return isMandatory;
    }
    public void setIsMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }
    public String getPromotionCode() {
        return promotionCode;
    }
    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }
    public String getPromotionDescription() {
        return promotionDescription;
    }
    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }
    public String getPromotionType() {
        return promotionType;
    }
    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(promotionCode);
        parcel.writeString(promotionDescription);
        parcel.writeString(promotionType);
        parcel.writeByte((byte) (isMandatory ? 1 : 0));
    }

    public static final Creator<Promotions> CREATOR = new Creator<Promotions>() {
        @Override
        public Promotions createFromParcel(Parcel source) {
            Promotions promotions = new Promotions();

            promotions.promotionCode = source.readString();
            promotions.promotionDescription = source.readString();
            promotions.promotionType = source.readString();
            promotions.isMandatory = source.readByte() !=0;

            return promotions;
        }
        @Override
        public Promotions[] newArray(int size) {
            return new Promotions[size];
        }
    };
}
