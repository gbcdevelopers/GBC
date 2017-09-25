package gbc.sa.vansales.models;

/**
 * Created by eheuristic on 22/12/2016.
 */

public class TodaysOrder {
    String ID = "";
    String Customer = "";
    String OrderNo = "";
    String Price = "";

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        Customer = customer;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
