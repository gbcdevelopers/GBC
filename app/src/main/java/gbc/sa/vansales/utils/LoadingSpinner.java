package gbc.sa.vansales.utils;
/**
 * Created by Rakshit on 15-Nov-16.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import gbc.sa.vansales.R;
public class LoadingSpinner {

    private Context context;

    private ProgressDialog progressDialog;

    public LoadingSpinner(Context context) {
        this.context = context;

        progressDialog = new ProgressDialog(context);

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.setMessage(context.getString(R.string.loading));
    }

    public boolean isShowing(){
        return progressDialog.isShowing();
    }

    public void show() {
        if (((Activity) context).isDestroyed()) return;

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
    }

    public void hide() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }
}
