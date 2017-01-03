package gbc.sa.vansales.models;

/**
 * Created by eheuristic on 24/12/2016.
 */

public class Sales {
    String item_code;
    String item_category;
    String material_no;
    String material_description;
    String uom;
    String name;
    String price;
    String pic;
    String cases;
    String inv_cases;
    String inv_piece;

    public String getMaterial_description() {
        return material_description;
    }
    public void setMaterial_description(String material_description) {
        this.material_description = material_description;
    }
    public String getUom() {
        return uom;
    }
    public void setUom(String uom) {
        this.uom = uom;
    }
    public String getItem_category() {
        return item_category;
    }
    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }
    public String getItem_code() {
        return item_code;
    }
    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }
    public String getMaterial_no() {
        return material_no;
    }
    public void setMaterial_no(String material_no) {
        this.material_no = material_no;
    }


    public String getInv_cases() {
        return inv_cases;
    }
    public void setInv_cases(String inv_cases) {
        this.inv_cases = inv_cases;
    }
    public String getInv_piece() {
        return inv_piece;
    }
    public void setInv_piece(String inv_piece) {
        this.inv_piece = inv_piece;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }
}
