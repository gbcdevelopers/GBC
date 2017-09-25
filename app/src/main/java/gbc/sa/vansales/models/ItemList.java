package gbc.sa.vansales.models;

/**
 * Created by ehs on 22/12/16.
 */

public class ItemList
{
    int id;
    String item_des;
    String item_number;
    String case_price;
    String unit_price;
    String upc;

    public ItemList()
    {}

    public ItemList(String item_number, String item_des, String case_price, String unit_price, String upc)
    {

        this.item_number = item_number;
        this.item_des = item_des;
        this.case_price = case_price;
        this.unit_price = unit_price;
        this.upc = upc;
    }

    public String getItem_des()
    {
        return item_des;
    }
    public void setItem_des(String item_des)
    {
        this.item_des = item_des;
    }

    public String getItem_number()
    {
        return item_number;
    }
    public void setItem_number(String item_number)
    {
        this.item_number = item_number;
    }

    public String getCase_price()
    {
        return case_price;
    }
    public void setCase_price(String case_price)
    {
        this.case_price = case_price;
    }

    public String getUnit_price()
    {
        return unit_price;
    }
    public void setUnit_price(String unit_price)
    {
        this.unit_price = unit_price;
    }

    public String getUpc()
    {
        return upc;
    }
    public void setUpc(String upc)
    {
        this.upc = upc;
    }
}
