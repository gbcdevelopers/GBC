package gbc.sa.vansales.models;

/**
 * Created by eheuristic on 12/23/2016.
 */

public class ShelfProduct {

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getPro_case() {
        return pro_case;
    }

    public void setPro_case(int pro_case) {
        this.pro_case = pro_case;
    }

    public int getPro_pcs() {
        return pro_pcs;
    }

    public void setPro_pcs(int pro_pcs) {
        this.pro_pcs = pro_pcs;
    }

    public int getPro_id() {
        return pro_id;
    }

    public void setPro_id(int pro_id) {
        this.pro_id = pro_id;
    }

    String productname;
    int pro_case;
    int pro_pcs;
    int pro_id;


}
