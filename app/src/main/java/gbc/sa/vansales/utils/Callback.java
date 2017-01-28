package gbc.sa.vansales.utils;
import java.util.ArrayList;
/**
 * Created by Rakshit on 28-Jan-17.
 */
public class Callback {
    public void callback() {
    }

    public void callbackSuccess() {
    }

    public void callbackSuccess(String data) {
    }

    public void callbackSuccess(Object object) {
    }

    public void callbackSuccess(ArrayList data) {
    }

    public void callbackSuccess(android.location.Location location) {
    }

    public void callbackFailure() {
    }

    public void callbackFailure(Exception exception) {
        exception.printStackTrace();
    }
}
