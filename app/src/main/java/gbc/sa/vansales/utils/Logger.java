package gbc.sa.vansales.utils;
/**
 * Created by Rakshit on 16-Feb-17.
 */

import android.content.Context;
import android.os.Environment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private boolean isAppenlog;

    public Logger() {
        this.isAppenlog = true;
    }

    public void appendLog(Context context,String text) {
        if (this.isAppenlog) {
            File dir = new File(Environment.getExternalStorageDirectory() + "/sfa/");
            File mydir = context.getFilesDir();
            String documents = "documents/berain";
            File documentsFolder = new File(mydir, documents);
            if (!documentsFolder.exists()) {
                documentsFolder.mkdirs();
            }
           // File logFile = new File(Environment.getExternalStorageDirectory() + "/sfa/sfa_log_+" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt");
            File logFile = new File(documentsFolder + "/sfa/sfa_log_+" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".txt");
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(new StringBuilder(String.valueOf(new Date().toString())).append(StringUtilities.LF).toString());
                buf.append(text);
                buf.newLine();
                buf.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}
