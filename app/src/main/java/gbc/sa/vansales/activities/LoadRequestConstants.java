package gbc.sa.vansales.activities;

/**
 * Created by Muhammad Umair on 02/12/2016.
 */

public class LoadRequestConstants
{
    private String itemName;
    private String category;
    private String cases;
    private String units;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private  String id;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private int categoryImage;
    public String date;



    public LoadRequestConstants(String id,String itemName, String category, String cases,String units,int categoryImage) {
        this.itemName = itemName;
        this.category = category;
        this.cases = cases;
        this.units=units;
        this.categoryImage = categoryImage;
        this.id=id;

    }
    public LoadRequestConstants()
    {

    }



}
