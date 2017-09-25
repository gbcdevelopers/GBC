package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 06-Mar-17.
 */
public class Survey implements Parcelable{
    private String srNo;
    private String questionID;
    private String questionText;
    private String responseType;
    private String responseOptions;

    public String getResponseOptions() {
        return responseOptions;
    }
    public void setResponseOptions(String responseOptions) {
        this.responseOptions = responseOptions;
    }
    private String answers;

    public String getResponseType() {
        return responseType;
    }
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
    public String getAnswers() {
        return answers;
    }
    public void setAnswers(String answers) {
        this.answers = answers;
    }
    public String getQuestionID() {
        return questionID;
    }
    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }
    public String getQuestionText() {
        return questionText;
    }
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    public String getSrNo() {
        return srNo;
    }
    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(srNo);
        parcel.writeString(questionID);
        parcel.writeString(questionText);
        parcel.writeString(answers);
        parcel.writeString(responseType);
        parcel.writeString(responseOptions);
    }

    public static final Creator<Survey> CREATOR = new Creator<Survey>() {
        @Override
        public Survey createFromParcel(Parcel source) {
            Survey survey = new Survey();
            survey.srNo = source.readString();
            survey.questionID = source.readString();
            survey.questionText = source.readString();
            survey.answers = source.readString();
            survey.responseType = source.readString();
            survey.responseOptions = source.readString();
            return survey;
        }
        @Override
        public Survey[] newArray(int size) {
            return new Survey[size];
        }
    };
}
