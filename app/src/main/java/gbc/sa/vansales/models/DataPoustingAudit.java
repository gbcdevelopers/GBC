package gbc.sa.vansales.models;

/**
 * Created by ehs on 22/12/16.
 */

public class DataPoustingAudit {

    int customer_id, customer_transection;
    String type;

    public DataPoustingAudit() {

    }

    public DataPoustingAudit(int customer_id, int customer_transection, String type) {
        this.customer_id = customer_id;
        this.customer_transection = customer_transection;
        this.type = type;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getCustomer_transection() {
        return customer_transection;
    }

    public void setCustomer_transection(int customer_transection) {
        this.customer_transection = customer_transection;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
