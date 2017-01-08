package gbc.sa.vansales.utils;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Rakshit on 14-Dec-16.
 */
public class Settings {
    private static final String PREFS_NAME = "GBCPrefs";
    private static SharedPreferences sharedPreferences;

    public static void initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(Settings.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getInstance() {
        return sharedPreferences;
    }

    public static String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public static int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public static void setString(String key, String value){
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key,value);
        editor.apply();
    }

    public static void clearPreferenceStore(){
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.commit();
    }
}
