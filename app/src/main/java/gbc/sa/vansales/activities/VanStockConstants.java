package gbc.sa.vansales.activities;

/**
 * Created by Muhammad Umair on 02/12/2016.
 */

public class VanStockConstants {
    private String vanStockItem;

    private String vanStockCase;
    private String vanStockDescription;
    private String vanStockUnits;


    public VanStockConstants(String vanStockItem, String vanStockCase, String vanStockDescription,String vanStockUnits) {
        this.vanStockItem = vanStockItem;
        this.vanStockCase = vanStockCase;
        this.vanStockDescription = vanStockDescription;
        this.vanStockUnits = vanStockUnits;
    }

    public String getvanStockItem() {
        return this.vanStockItem;
    }

    public String getvanStockCase() {
        return this.vanStockCase;
    }

    public String getvanStockDescription() {
        return this.vanStockDescription;
    }
    public String getVanStockUnits() {
        return this.vanStockUnits;
    }

}
