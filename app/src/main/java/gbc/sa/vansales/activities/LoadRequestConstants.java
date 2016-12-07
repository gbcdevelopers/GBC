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
    private int categoryImage;

    public LoadRequestConstants(String itemName, String category, String cases,String units,int categoryImage) {
        this.itemName = itemName;
        this.category = category;
        this.cases = cases;
        this.units=units;
        this.categoryImage = categoryImage;
    }

        public String getItemName() {
            return this.itemName;
        }

        public String getCategory() {
            return this.category;
        }

        public String getCases() {
            return this.cases;
        }
        public String getUnits() {
        return this.cases;
        }

        public int getCategoryImage() {
            return this.categoryImage;
        }
}
