package gbc.sa.vansales.models;
/**
 * Created by Rakshit on 16-Nov-16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
public class Customer implements Parcelable{
    private String customer_id;
    private String customer_name;
    private String payment_method;
    private String customer_address;
    private String credit_limit;
    private String credit_days;
    private String credit_available;
    private String customerItemNo;
    private String latitude;
    private String longitude;
    private boolean order;
    private boolean sale;
    private boolean delivery;
    private boolean collection;
    private boolean merchandize;
    private boolean newcustomer;
    private String customer_name_ar;
    private boolean openDelivery;

    public boolean isOpenDelivery() {
        return openDelivery;
    }
    public void setOpenDelivery(boolean openDelivery) {
        this.openDelivery = openDelivery;
    }


    public String getCustomer_name_ar() {
        return customer_name_ar;
    }
    public void setCustomer_name_ar(String customer_name_ar) {
        this.customer_name_ar = customer_name_ar;
    }
    public String getVisitListID() {
        return visitListID;
    }
    public void setVisitListID(String visitListID) {
        this.visitListID = visitListID;
    }
    private String visitListID;
    private boolean isCredit;

    //Get Instance of the Customer
    public Customer(){
    }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getCustomerItemNo() {
        return customerItemNo;
    }
    public void setCustomerItemNo(String customerItemNo) {
        this.customerItemNo = customerItemNo;
    }
    //Getter and Setter methods
    public String getCustomerID(){
        return customer_id;
    }
    public void setCustomerID(String id){
        this.customer_id = id;
    }

    public String getCustomerName(){
        return customer_name;
    }
    public void setCustomerName(String customer_name){
        this.customer_name = customer_name;
    }

    public String getPaymentMethod(){
        return payment_method;
    }
    public void setPaymentMethod(String payment_method){
        this.payment_method = payment_method;
    }

    public String getCustomerAddress(){
        return customer_address;
    }
    public void setCustomerAddress(String address){
        this.customer_address = address;
    }

    public String getCreditLimit(){
        return credit_limit;
    }
    public void setCreditLimit(String credit_limit){
        this.credit_limit = credit_limit;
    }

    public String getCreditDays(){
        return credit_days;
    }
    public void setCreditDays(String credit_days){
        this.credit_days = credit_days;
    }

    public String getCreditAvailable(){
        return credit_available;
    }
    public void setCreditAvailable(String credit_available){
        this.credit_available = credit_available;
    }

    public boolean isSale(){
        return sale;
    }
    public void setSale(boolean sale){
        this.sale = sale;
    }

    public boolean isDelivery(){
        return delivery;
    }
    public void setDelivery(boolean delivery){
        this.delivery = delivery;
    }

    public boolean isCollection(){
        return collection;
    }
    public void setCollection(boolean collection){
        this.collection = collection;
    }

    public boolean isMerchandize(){
        return merchandize;
    }
    public void setMerchandize(boolean merchandize){
        this.merchandize = merchandize;
    }

    public boolean isOrder(){
        return order;
    }
    public void setOrder(boolean order){
        this.order = order;
    }

    public boolean isNewCustomer(){
        return newcustomer;
    }
    public void setNewCustomer(boolean newcustomer){
        this.newcustomer = newcustomer;
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel source) {
            Customer customer = new Customer();

            customer.customer_id = source.readString();
            customer.customer_name = source.readString();
            customer.customer_address = source.readString();
            customer.payment_method = source.readString();
            customer.credit_limit = source.readString();
            customer.credit_available = source.readString();
            customer.credit_days = source.readString();
            customer.sale = source.readByte() !=0;
            customer.delivery = source.readByte()!=0;
            customer.collection = source.readByte()!=0;
            customer.merchandize = source.readByte()!=0;
            customer.order = source.readByte()!=0;
            customer.newcustomer = source.readByte()!=0;
            customer.openDelivery = source.readByte()!=0;
            customer.customerItemNo = source.readString();
            customer.latitude = source.readString();
            customer.longitude = source.readString();
            customer.visitListID = source.readString();
            customer.customer_name_ar = source.readString();
            return customer;
        }
        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(customer_id);
        parcel.writeString(customer_name);
        parcel.writeString(customer_address);
        parcel.writeString(payment_method);
        parcel.writeString(credit_limit);
        parcel.writeString(credit_days);
        parcel.writeString(credit_available);
        parcel.writeByte((byte) (sale ? 1 : 0));
        parcel.writeByte((byte)(delivery ? 1:0));
        parcel.writeByte((byte)(collection ? 1:0));
        parcel.writeByte((byte)(merchandize ? 1:0));
        parcel.writeByte((byte)(order ? 1:0));
        parcel.writeByte((byte)(newcustomer ? 1:0));
        parcel.writeByte((byte)(openDelivery ? 1:0));
        parcel.writeString(customerItemNo);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
        parcel.writeString(visitListID);
        parcel.writeString(customer_name_ar);
    }

    public static Customer getCustomer(ArrayList<Customer> list, String searchString) {
        //Log.e("Material No","" + searchString);
        for (Customer customer : list) {

            if (customer.getCustomerID().equals(searchString)){
                // Log.e("Inside if","Condition");
                return customer;
            }
        }

        return null;
    }
}
