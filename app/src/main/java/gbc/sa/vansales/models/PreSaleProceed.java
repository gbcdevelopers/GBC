package gbc.sa.vansales.models;

/**
 * Created by eheuristic on 10/12/2016.
 */

public class PreSaleProceed {


    String PRODUCT_NAME = "";
    String CATEGORY_NAME="";
    String CTN = "";
    String BTL = "";
    String PRICE="";
    String DATE="";


    public String getCATEGORY_NAME() {
        return CATEGORY_NAME;
    }

    public void setCATEGORY_NAME(String CATEGORY_NAME) {
        this.CATEGORY_NAME = CATEGORY_NAME;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    int img;

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }



    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
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
