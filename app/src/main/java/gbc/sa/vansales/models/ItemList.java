package gbc.sa.vansales.models;

/**
 * Created by ehs on 22/12/16.
 */

public class ItemList
{
    int id,item_number,case_price,unit_price,upc;
    String item_des;

    public ItemList()
    {}

    public ItemList(int item_number, String item_des, int case_price, int unit_price, int upc)
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

    public int getItem_number()
    {
        return item_number;
    }
    public void setItem_number(int item_number)
    {
        this.item_number = item_number;
    }

    public int getCase_price()
    {
        return case_price;
    }
    public void setCase_price(int case_price)
    {
        this.case_price = case_price;
    }

    public int getUnit_price()
    {
        return unit_price;
    }
    public void setUnit_price(int unit_price)
    {
        this.unit_price = unit_price;
    }

    public int getUpc()
    {
        return upc;
    }
    public void setUpc(int upc)
    {
        this.upc = upc;
    }
}
