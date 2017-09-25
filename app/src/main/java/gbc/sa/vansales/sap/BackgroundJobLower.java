package gbc.sa.vansales.sap;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.NetworkUtil;
/**
 * Created by Rakshit on 08-Feb-17.
 */
public class BackgroundJobLower extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if(!status.equals(ConfigStore.NO_CONNECTION)){
            context.startService(new Intent(context, SyncData.class));
        }
    }
}
