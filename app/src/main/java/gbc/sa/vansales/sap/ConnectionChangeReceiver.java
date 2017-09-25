package gbc.sa.vansales.sap;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import gbc.sa.vansales.utils.ConfigStore;
import gbc.sa.vansales.utils.NetworkUtil;
/**
 * Created by Rakshit on 11-Jan-17.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        Toast.makeText(context,status,Toast.LENGTH_LONG).show();
        if(!status.equals(ConfigStore.NO_CONNECTION)){
            context.startService(new Intent(context, SyncData.class));
        }
    }
}
