package gbc.sa.vansales.models;
/**
 * Created by Rakshit on 16-Nov-16.
 */
import android.os.Parcel;
import android.os.Parcelable;
public class Customer implements Parcelable{
    private String customer_id;
    private String customer_name;
    private String payment_method;
    private String customer_address;
    private String credit_limit;
    private String credit_days;
    private String credit_available;
    private boolean order;
    private boolean sale;
    private boolean delivery;
    private boolean collection;
    private boolean merchandize;
    private boolean newcustomer;

    private boolean isCredit;

    //Get Instance of the Customer
    public Customer(){
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
    }
}
