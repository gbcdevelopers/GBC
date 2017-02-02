package gbc.sa.vansales.sap;
import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
/**
 * Created by Rakshit on 29-Jan-17.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BackgroundJob extends JobService {
    private static final String TAG = "BackgroundJob";
    Context context;

    public BackgroundJob(Context context) {
        this.context = context;
    }

    public BackgroundJob() {

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        getApplicationContext().startService(new Intent(getApplicationContext(), SyncData.class));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("Stop Job","Stop Job");
        return false;
    }
}
