package gbc.sa.vansales.sap;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import gbc.sa.vansales.utils.DatabaseHandler;
/**
 * Created by Rakshit on 08-Jan-17.
 */
public class OfflineSync extends AbstractThreadedSyncAdapter {
    private static final String TAG = "OfflineSyncAdapter";
    DatabaseHandler db;
    public OfflineSync(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.db = new DatabaseHandler(context);
    }
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {


    }
}
