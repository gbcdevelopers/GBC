package gbc.sa.vansales.activities;

/**
 * Created by Muhammad Umair on 23/12/2016.
 */
public class ExpandedMenuModel {

    String iconName = "";
    int iconImg = -1; // menu icon resource id
    boolean isEnabled = false;
    public boolean isEnabled() {
        return isEnabled;
    }
    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    public String getIconName() {
        return iconName;
    }
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    public int getIconImg() {
        return iconImg;
    }
    public void setIconImg(int iconImg) {
        this.iconImg = iconImg;
    }
}
