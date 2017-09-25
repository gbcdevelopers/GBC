package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Rakshit on 24-Jan-17.
 */
public class DriverFlag implements Parcelable{
    private String tripID;
    private String driver;
    private String routeType;
    private boolean promptOdometer;
    private boolean eodSalesReports;
    private boolean isDeleteInvoice;
    private boolean isNoSale;
    private boolean isAddCustomer;
    private boolean isDelayPrint;
    private String defaultDeliveryDays;
    private String password1;
    private String password2;
    private String password3;
    private String password4;
    private String password5;
    private String isViewVanStock;
    private String isLoadSecurityGuard;
    private String isStartOfDay;
    private String isEndTrip;
    private String isInformationButton;
    private boolean isCallSequence;
    private boolean isDisplayInvoiceSummary;
    private boolean isAllowRadius;
    private boolean isEnableGPS;

    public boolean isAllowRadius() {
        return isAllowRadius;
    }
    public void setIsAllowRadius(boolean isAllowRadius) {
        this.isAllowRadius = isAllowRadius;
    }
    public boolean isEnableGPS() {
        return isEnableGPS;
    }
    public void setIsEnableGPS(boolean isEnableGPS) {
        this.isEnableGPS = isEnableGPS;
    }
    public static final Creator<DriverFlag> CREATOR = new Creator<DriverFlag>() {
        @Override
        public DriverFlag createFromParcel(Parcel source) {
            DriverFlag driverFlag = new DriverFlag();
            driverFlag.tripID = source.readString();
            driverFlag.driver = source.readString();
            driverFlag.routeType = source.readString();
            driverFlag.promptOdometer = source.readByte() != 0;
            driverFlag.eodSalesReports = source.readByte() != 0;
            driverFlag.isDeleteInvoice = source.readByte() != 0;
            driverFlag.isNoSale = source.readByte() != 0;
            driverFlag.isAddCustomer = source.readByte() != 0;
            driverFlag.isDelayPrint = source.readByte() != 0;
            driverFlag.defaultDeliveryDays = source.readString();
            driverFlag.password1 = source.readString();
            driverFlag.password2 = source.readString();
            driverFlag.password3 = source.readString();
            driverFlag.password4 = source.readString();
            driverFlag.password5 = source.readString();
            driverFlag.isViewVanStock = source.readString();
            driverFlag.isLoadSecurityGuard = source.readString();
            driverFlag.isStartOfDay = source.readString();
            driverFlag.isEndTrip = source.readString();
            driverFlag.isInformationButton = source.readString();
            driverFlag.isCallSequence = source.readByte() != 0;
            driverFlag.isDisplayInvoiceSummary = source.readByte() != 0;
            driverFlag.isAllowRadius = source.readByte() != 0;
            driverFlag.isEnableGPS = source.readByte() != 0;
            return driverFlag;
        }
        @Override
        public DriverFlag[] newArray(int size) {
            return new DriverFlag[size];
        }
    };
    public String getDefaultDeliveryDays() {
        return defaultDeliveryDays;
    }
    public void setDefaultDeliveryDays(String defaultDeliveryDays) {
        this.defaultDeliveryDays = defaultDeliveryDays;
    }
    public String getDriver() {
        return driver;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }
    public boolean isEodSalesReports() {
        return eodSalesReports;
    }
    public void setEodSalesReports(boolean eodSalesReports) {
        this.eodSalesReports = eodSalesReports;
    }
    public boolean isAddCustomer() {
        return isAddCustomer;
    }
    public void setIsAddCustomer(boolean isAddCustomer) {
        this.isAddCustomer = isAddCustomer;
    }
    public boolean isCallSequence() {
        return isCallSequence;
    }
    public void setIsCallSequence(boolean isCallSequence) {
        this.isCallSequence = isCallSequence;
    }
    public boolean isDelayPrint() {
        return isDelayPrint;
    }
    public void setIsDelayPrint(boolean isDelayPrint) {
        this.isDelayPrint = isDelayPrint;
    }
    public boolean isDeleteInvoice() {
        return isDeleteInvoice;
    }
    public void setIsDeleteInvoice(boolean isDeleteInvoice) {
        this.isDeleteInvoice = isDeleteInvoice;
    }
    public boolean isDisplayInvoiceSummary() {
        return isDisplayInvoiceSummary;
    }
    public void setIsDisplayInvoiceSummary(boolean isDisplayInvoiceSummary) {
        this.isDisplayInvoiceSummary = isDisplayInvoiceSummary;
    }
    public String getIsEndTrip() {
        return isEndTrip;
    }
    public void setIsEndTrip(String isEndTrip) {
        this.isEndTrip = isEndTrip;
    }
    public String getIsInformationButton() {
        return isInformationButton;
    }
    public void setIsInformationButton(String isInformationButton) {
        this.isInformationButton = isInformationButton;
    }
    public String getIsLoadSecurityGuard() {
        return isLoadSecurityGuard;
    }
    public void setIsLoadSecurityGuard(String isLoadSecurityGuard) {
        this.isLoadSecurityGuard = isLoadSecurityGuard;
    }
    public boolean isNoSale() {
        return isNoSale;
    }
    public void setIsNoSale(boolean isNoSale) {
        this.isNoSale = isNoSale;
    }
    public String getIsStartOfDay() {
        return isStartOfDay;
    }
    public void setIsStartOfDay(String isStartOfDay) {
        this.isStartOfDay = isStartOfDay;
    }
    public String getIsViewVanStock() {
        return isViewVanStock;
    }
    public void setIsViewVanStock(String isViewVanStock) {
        this.isViewVanStock = isViewVanStock;
    }
    public String getPassword1() {
        return password1;
    }
    public void setPassword1(String password1) {
        this.password1 = password1;
    }
    public String getPassword2() {
        return password2;
    }
    public void setPassword2(String password2) {
        this.password2 = password2;
    }
    public String getPassword3() {
        return password3;
    }
    public void setPassword3(String password3) {
        this.password3 = password3;
    }
    public String getPassword4() {
        return password4;
    }
    public void setPassword4(String password4) {
        this.password4 = password4;
    }
    public String getPassword5() {
        return password5;
    }
    public void setPassword5(String password5) {
        this.password5 = password5;
    }
    public boolean isPromptOdometer() {
        return promptOdometer;
    }
    public void setPromptOdometer(boolean promptOdometer) {
        this.promptOdometer = promptOdometer;
    }
    public String getRouteType() {
        return routeType;
    }
    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }
    public String getTripID() {
        return tripID;
    }
    public void setTripID(String tripID) {
        this.tripID = tripID;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(tripID);
        parcel.writeString(driver);
        parcel.writeString(routeType);
        parcel.writeByte((byte) (promptOdometer ? 1 : 0));
        parcel.writeByte((byte) (eodSalesReports ? 1 : 0));
        parcel.writeByte((byte) (isDeleteInvoice ? 1 : 0));
        parcel.writeByte((byte) (isNoSale ? 1 : 0));
        parcel.writeByte((byte) (isAddCustomer ? 1 : 0));
        parcel.writeByte((byte) (isDelayPrint ? 1 : 0));
        parcel.writeString(defaultDeliveryDays);
        parcel.writeString(password1);
        parcel.writeString(password2);
        parcel.writeString(password3);
        parcel.writeString(password4);
        parcel.writeString(password5);
        parcel.writeString(isViewVanStock);
        parcel.writeString(isLoadSecurityGuard);
        parcel.writeString(isStartOfDay);
        parcel.writeString(isEndTrip);
        parcel.writeString(isInformationButton);
        parcel.writeByte((byte) (isCallSequence ? 1 : 0));
        parcel.writeByte((byte) (isDisplayInvoiceSummary ? 1 : 0));
        parcel.writeByte((byte) (isAllowRadius ? 1 : 0));
        parcel.writeByte((byte) (isEnableGPS ? 1 : 0));
    }

}
