package gbc.sa.vansales.models;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
/**
 * Created by Rakshit on 21-Dec-16.
 */
public class CustomerHeader implements Parcelable {
    private String tripId;
    private String orderBlock;
    private String invoiceBlock;
    private String deliveryBlock;
    private String roomNo;
    private String floor;
    private String building;
    private String homeCity;
    private String street5;
    private String street4;
    private String street3;
    private String street2;
    private String name4;
    private String driver1;
    private String customerNo;
    private String countryCode;
    private String name3;
    private String name1;
    private String name2;
    private String address;
    private String street;
    private String city;
    private String district;
    private String region;
    private String siteCode;
    private String postCode;
    private String phone;

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getBuilding() {
        return building;
    }
    public void setBuilding(String building) {
        this.building = building;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }
    public String getDeliveryBlock() {
        return deliveryBlock;
    }
    public void setDeliveryBlock(String deliveryBlock) {
        this.deliveryBlock = deliveryBlock;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getDriver1() {
        return driver1;
    }
    public void setDriver1(String driver1) {
        this.driver1 = driver1;
    }
    public String getFloor() {
        return floor;
    }
    public void setFloor(String floor) {
        this.floor = floor;
    }
    public String getHomeCity() {
        return homeCity;
    }
    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }
    public String getInvoiceBlock() {
        return invoiceBlock;
    }
    public void setInvoiceBlock(String invoiceBlock) {
        this.invoiceBlock = invoiceBlock;
    }
    public String getName1() {
        return name1;
    }
    public void setName1(String name1) {
        this.name1 = name1;
    }
    public String getName2() {
        return name2;
    }
    public void setName2(String name2) {
        this.name2 = name2;
    }
    public String getName3() {
        return name3;
    }
    public void setName3(String name3) {
        this.name3 = name3;
    }
    public String getName4() {
        return name4;
    }
    public void setName4(String name4) {
        this.name4 = name4;
    }
    public String getOrderBlock() {
        return orderBlock;
    }
    public void setOrderBlock(String orderBlock) {
        this.orderBlock = orderBlock;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPostCode() {
        return postCode;
    }
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getRoomNo() {
        return roomNo;
    }
    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
    public String getSiteCode() {
        return siteCode;
    }
    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
    public String getStreet2() {
        return street2;
    }
    public void setStreet2(String street2) {
        this.street2 = street2;
    }
    public String getStreet3() {
        return street3;
    }
    public void setStreet3(String street3) {
        this.street3 = street3;
    }
    public String getStreet4() {
        return street4;
    }
    public void setStreet4(String street4) {
        this.street4 = street4;
    }
    public String getStreet5() {
        return street5;
    }
    public void setStreet5(String street5) {
        this.street5 = street5;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getTripId() {
        return tripId;
    }
    public void setTripId(String tripId) {
        this.tripId = tripId;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(tripId);
        parcel.writeString(orderBlock);
        parcel.writeString(invoiceBlock);
        parcel.writeString(deliveryBlock);
        parcel.writeString(roomNo);
        parcel.writeString(floor);
        parcel.writeString(building);
        parcel.writeString(homeCity);
        parcel.writeString(street5);
        parcel.writeString(street4);
        parcel.writeString(street3);
        parcel.writeString(street2);
        parcel.writeString(name4);
        parcel.writeString(driver1);
        parcel.writeString(customerNo);
        parcel.writeString(countryCode);
        parcel.writeString(name3);
        parcel.writeString(name1);
        parcel.writeString(name2);
        parcel.writeString(address);
        parcel.writeString(street);
        parcel.writeString(city);
        parcel.writeString(district);
        parcel.writeString(region);
        parcel.writeString(siteCode);
        parcel.writeString(postCode);
        parcel.writeString(phone);
    }

    public static final Creator<CustomerHeader> CREATOR = new Creator<CustomerHeader>() {
        public CustomerHeader createFromParcel(Parcel source){
            CustomerHeader customerHeader = new CustomerHeader();

            customerHeader.tripId= source.readString();
            customerHeader.orderBlock= source.readString();
            customerHeader.invoiceBlock= source.readString();
            customerHeader.deliveryBlock= source.readString();
            customerHeader.roomNo= source.readString();
            customerHeader.floor= source.readString();
            customerHeader.building= source.readString();
            customerHeader.homeCity= source.readString();
            customerHeader.street5= source.readString();
            customerHeader.street4= source.readString();
            customerHeader.street3= source.readString();
            customerHeader.street2= source.readString();
            customerHeader.name4= source.readString();
            customerHeader.driver1= source.readString();
            customerHeader.customerNo= source.readString();
            customerHeader.countryCode= source.readString();
            customerHeader.name3= source.readString();
            customerHeader.name1= source.readString();
            customerHeader.name2= source.readString();
            customerHeader.address= source.readString();
            customerHeader.street= source.readString();
            customerHeader.city= source.readString();
            customerHeader.district= source.readString();
            customerHeader.region= source.readString();
            customerHeader.siteCode= source.readString();
            customerHeader.postCode= source.readString();
            customerHeader.phone= source.readString();

            return customerHeader;
        }

        public CustomerHeader[] newArray(int size) {
            return new CustomerHeader[size];
        }
    };

    public static CustomerHeader getCustomer(ArrayList<CustomerHeader> list, String searchString) {

        for (CustomerHeader customer : list) {
            if (customer.getCustomerNo().equals(searchString)){
                return customer;
            }
        }
        return null;
    }

}
