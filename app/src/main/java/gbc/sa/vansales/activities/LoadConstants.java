package gbc.sa.vansales.activities;

/**
 * Created by Muhammad Umair on 29/11/2016.
 */
public class LoadConstants {
    private String loadNumber = "";
    private String loadDate = "";
    private String availableLoad = "";
    private String status = "";



    public void setloadNumber(String loadNumber) {
        this.loadNumber = loadNumber;
    }

    public String getloadNumber() {
        return loadNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setloadDate(String loadDate) {
        this.loadDate = loadDate;
    }

    public String getloadDate() {
        return loadDate;
    }
    public void setavailableLoad(String availableLoad) {
        this.availableLoad = availableLoad;
    }

    public String getavailableLoad() {
        return availableLoad;
    }

}
