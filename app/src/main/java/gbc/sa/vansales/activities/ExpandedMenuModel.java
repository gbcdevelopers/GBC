package gbc.sa.vansales.activities;

/**
 * Created by Muhammad Umair on 23/12/2016.
 */
public class ExpandedMenuModel {

    String iconName = "";
    int iconImg = -1; // menu icon resource id
    String iconCount = "";
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
    public String getNotificationCount(){
        return iconCount;
    }
    public void setNotificationCount(String iconCount){
        this.iconCount = iconCount;
    }
}
