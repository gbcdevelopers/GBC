package gbc.sa.vansales.models;

/**
 * Created by eheuristic on 10/12/2016.
 */

public class PreSaleProceed {
    String SKU = "";
    String CTN = "";
    String BTL = "";
    String PRICE="";

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }



    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getCTN() {
        return CTN;
    }

    public void setCTN(String CTN) {
        this.CTN = CTN;
    }

    public String getBTL() {
        return BTL;
    }

    public void setBTL(String BTL) {
        this.BTL = BTL;
    }
}
