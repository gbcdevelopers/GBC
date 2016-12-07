package gbc.sa.vansales.activities;

import java.io.Serializable;

/**
 * Created by Muhammad Umair on 06/12/2016.
 */

public class UnloadSwipe implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String cases;

    private String pcs;

    public UnloadSwipe() {

    }
    public UnloadSwipe(String name, String cases,String pcs) {
        this.name = name;
        this.cases = cases;
        this.pcs = pcs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getPcs() {
        return pcs;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs;
    }


}

