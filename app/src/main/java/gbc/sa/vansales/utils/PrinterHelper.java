package gbc.sa.vansales.utils;
/**
 * Created by Rakshit on 03-Feb-17.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Message;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import gbc.sa.vansales.models.DevicesData;
import gbc.sa.vansales.utils.StringUtilities;
public class PrinterHelper {
    private static final UUID MY_UUID;
    byte[] BoldOff;
    byte[] BoldOn;
    byte[] CarriageReturn;
    byte[] CompressOff;
    byte[] CompressOn;
    byte[] DoubleHighOff;
    byte[] DoubleHighOn;
    byte[] DoubleWideOff;
    byte[] DoubleWideOn;
    byte[] NewLine;
    private ProgressDialog ProgressDialog;
    byte[] UnderlineOff;
    byte[] UnderlineOn;
    ArrayAdapter<String> adapter;
    private ArrayList<DevicesData> arrData = new ArrayList<>();
    ArrayList<BluetoothDevice> arrayListBluetoothDevices;
    BluetoothSocket btSocket;
    private String callbackId;
    int cnln;
    private int count;
    ArrayAdapter<String> detectedAdapter;
    String devicename;
    int endln;
    private HashMap<String, String> hashArabVales;
    private HashMap<String, Integer> hashArbPositions;
    private HashMap<String, Integer> hashPositions;
    private HashMap<String, Integer> hashValues;
    private boolean isEnglish;
    private boolean isExceptionThrown;
    boolean isTwice;
    private JSONArray jArr;
    private BluetoothAdapter mBtAdapter;
    private final BroadcastReceiver mPairReceiver;
    private final BroadcastReceiver mReceiverRequiresPin;
    private BroadcastReceiver myReceiver;
    private NotificationManager notificationManager;
    OutputStream outStream;
    private ProgressDialog progressDialog;
    byte[] resetprinter;
    String resolution;
    private int retryCount;
    private String sMacAddr;
    int startln;
    private JSONObject status;
    String strFormat;
    String strFormatBold;
    String strFormatHeader;
    String strFormatTitle;
    String strPrintLeftBold;
    String strUnderLine;
    Context context;
    private Activity activity;

    public PrinterHelper(Context context,Activity activity){
        this.isTwice = false;
        this.resolution = "";
        this.callbackId = "";
        this.arrayListBluetoothDevices = null;
        this.isExceptionThrown = false;
        this.btSocket = null;
        this.outStream = null;
        this.devicename = "";
        this.startln = 4;
        this.endln = 12;
        this.cnln = 16;
        this.isEnglish = true;
        this.sMacAddr = "";
        this.BoldOn = new byte[]{(byte) 27, (byte) 71};
        this.BoldOff = new byte[]{(byte) 27, (byte) 72};
        this.UnderlineOn = new byte[]{(byte) 27, (byte) 45, (byte) 1};
        byte[] bArr = new byte[3];
        bArr[0] = (byte) 27;
        bArr[1] = (byte) 45;
        this.UnderlineOff = bArr;
        this.CompressOn = new byte[]{(byte) 27, (byte) 33, (byte) 4};
        bArr = new byte[3];
        bArr[0] = (byte) 27;
        bArr[1] = (byte) 33;
        this.CompressOff = bArr;
        this.NewLine = new byte[]{(byte) 13, (byte) 10};
        this.DoubleHighOn = new byte[]{(byte) 27, (byte) 33, (byte) 16};
        this.DoubleHighOff = new byte[]{(byte) 27, (byte) 33, (byte) 16};
        this.DoubleWideOn = new byte[]{(byte) 27, (byte) 33, (byte) 32};
        bArr = new byte[3];
        bArr[0] = (byte) 27;
        bArr[1] = (byte) 33;
        this.DoubleWideOff = bArr;
        this.resetprinter = new byte[]{(byte) 27, (byte) 64};
        this.CarriageReturn = new byte[]{(byte) 13, (byte) 10};
        this.retryCount = 0;
        this.count = 0;
        this.myReceiver = new C04641();
        this.mReceiverRequiresPin = new C04652();
        this.notificationManager = null;
        this.mPairReceiver = new C04663();
        this.context = context;
        this.activity = activity;
    }

    class C04641 extends BroadcastReceiver {
        C04641() {
        }

        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            if ("android.bluetooth.device.action.FOUND".equals(intent.getAction())) {
                Toast.makeText(context, "ACTION_FOUND", 0).show();
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (PrinterHelper.this.arrayListBluetoothDevices.size() < 1) {
                    PrinterHelper.this.detectedAdapter.add(device.getName() + StringUtilities.LF + device.getAddress());
                    PrinterHelper.this.arrayListBluetoothDevices.add(device);
                    PrinterHelper.this.detectedAdapter.notifyDataSetChanged();
                    return;
                }
                boolean flag = true;
                for (int i = 0; i < PrinterHelper.this.arrayListBluetoothDevices.size(); i++) {
                    if (device.getAddress().equals(((BluetoothDevice) PrinterHelper.this.arrayListBluetoothDevices.get(i)).getAddress())) {
                        flag = false;
                    }
                }
                if (flag) {
                    PrinterHelper.this.detectedAdapter.add(device.getName() + StringUtilities.LF + device.getAddress());
                    PrinterHelper.this.arrayListBluetoothDevices.add(device);
                    PrinterHelper.this.detectedAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    class C04652 extends BroadcastReceiver {
        C04652() {
        }

        public void onReceive(Context context, Intent intent) {
            try {
                BluetoothDevice newDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                Class<?> btDeviceInstance = Class.forName(BluetoothDevice.class.getCanonicalName());
                byte[] pin = (byte[]) btDeviceInstance.getMethod("convertPinToBytes", new Class[]{String.class}).invoke(newDevice, new Object[]{"1234"});
                Log.e("Success", "success" + ((Boolean) btDeviceInstance.getMethod("setPin", new Class[]{byte[].class}).invoke(newDevice, new Object[]{pin})).booleanValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C04663 extends BroadcastReceiver {
        C04663() {
        }

        public void onReceive1(Context context, Intent intent) {
            if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(intent.getAction())) {
                int state = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", ExploreByTouchHelper.INVALID_ID);
                int prevState = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", ExploreByTouchHelper.INVALID_ID);
                if (state == 12 && prevState == 11) {
                    Toast.makeText(context, "Paired", Toast.LENGTH_SHORT).show();
                } else if (state == 10 && prevState == 12) {
                    Toast.makeText(context, "Unpaired", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void onReceive(Context arg0, Intent arg1) {
        }
    }

    class C04694 implements Runnable {
        private final /* synthetic */ ArrayList val$arrData;
        private final /* synthetic */ String[] val$arrDevices;

        /* renamed from: com.phonegap.sfa.DotmatHelper.4.1 */
        class C04671 implements OnClickListener {
            private final /* synthetic */ ArrayList val$arrData;

            C04671(ArrayList arrayList) {
                this.val$arrData = arrayList;
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    PrinterHelper.this.retryCount = 0;
                    PrinterHelper.this.sMacAddr = ((DevicesData) this.val$arrData.get(which)).getAddress();
                    if (!PrinterHelper.this.sMacAddr.contains(":") && PrinterHelper.this.sMacAddr.length() == 12) {
                        char[] cAddr = new char[17];
                        int j = 0;
                        for (int i = 0; i < 12; i += 2) {
                            PrinterHelper.this.sMacAddr.getChars(i, i + 2, cAddr, j);
                            j += 2;
                            if (j < 17) {
                                int j2 = j + 1;
                                cAddr[j] = ':';
                                j = j2;
                            }
                        }
                        PrinterHelper.this.sMacAddr = new String(cAddr);
                    }
                    PrinterHelper.this.showProgressDialog();
                    PrinterHelper.this.doConnectionTest(PrinterHelper.this.sMacAddr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(context, " you have selected " + ((DevicesData) this.val$arrData.get(which)).getName(), 1).show();
            }
        }

        /* renamed from: com.phonegap.sfa.DotmatHelper.4.2 */
        class C04682 implements OnClickListener {
            C04682() {
            }

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    PrinterHelper.this.status.put("status", false);
                    PrinterHelper.this.status.put("isconnected", -1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                PrinterHelper.this.sendUpdate(PrinterHelper.this.status, true);
            }
        }

        C04694(String[] strArr, ArrayList arrayList) {
            this.val$arrDevices = strArr;
            this.val$arrData = arrayList;
        }

        public void run() {
            Builder dialog = new Builder(context);
            dialog.setTitle("Choose Device To Pair");
            dialog.setItems(this.val$arrDevices, new C04671(this.val$arrData));
            dialog.setPositiveButton("Cancel", new C04682());
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    class C04705 implements Runnable {
        private final /* synthetic */ String val$address;

        C04705(String str) {
            this.val$address = str;
        }

        public void run() {
            new ConnectTo().execute(new String[]{this.val$address});
        }
    }

    public class ConnectTo extends AsyncTask<String, Void, Boolean> {
        @SuppressLint({"NewApi"})
        protected Boolean doInBackground(String... address) {
            try {
                BluetoothDevice device = PrinterHelper.this.mBtAdapter.getRemoteDevice(address[0]);
                if (PrinterHelper.this.btSocket != null && PrinterHelper.this.btSocket.isConnected()) {
                    PrinterHelper.this.btSocket.close();
                    PrinterHelper.this.btSocket = null;
                }
                if (VERSION.SDK_INT < 19) {
                    Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{Integer.TYPE});
                    PrinterHelper.this.btSocket = (BluetoothSocket) m.invoke(device, new Object[]{Integer.valueOf(1)});
                } else {
                    PrinterHelper.this.btSocket = device.createInsecureRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
                }
                PrinterHelper.this.devicename = device.getName();
                try {
                    PrinterHelper.this.btSocket.connect();
                    PrinterHelper.this.outStream = PrinterHelper.this.btSocket.getOutputStream();
                    return Boolean.valueOf(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    return Boolean.valueOf(false);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                return Boolean.valueOf(false);
            }
        }

        protected void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                PrinterHelper.this.dismissProgress();
                Log.e("Connected", "true");
                try {
                    PrinterHelper.this.printReports(PrinterHelper.this.sMacAddr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            try {
                PrinterHelper.this.doConnectionTest(PrinterHelper.this.sMacAddr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void onPreExecute() {
        }
    }

    @SuppressLint({"NewApi"})
    void printReports(String address) throws JSONException {
        Log.e("Print Report",""+ this.jArr.toString());
        Log.d("Print Report", this.jArr.toString());
        if (this.outStream != null) {
            try {
                this.outStream.flush();
                this.outStream.close();
            } catch (IOException e2222) {
                e2222.printStackTrace();
            }
        }
        if (this.btSocket != null && this.btSocket.isConnected()) {
            try {
                this.btSocket.close();
            } catch (IOException e22222) {
                e22222.printStackTrace();
            }
        }
    }

    private class asyncgetDevices extends AsyncTask<Void, Set<BluetoothDevice>, Set<BluetoothDevice>> {
        private asyncgetDevices() {
        }

        protected Set<BluetoothDevice> doInBackground(Void... params) {
            PrinterHelper.this.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            if (PrinterHelper.this.mBtAdapter.isEnabled()) {
                return PrinterHelper.this.mBtAdapter.getBondedDevices();
            }
            PrinterHelper.this.mBtAdapter.enable();
            cancel(true);
            new asyncgetDevices().execute(new Void[0]);
            return null;
        }

        protected void onPostExecute(Set<BluetoothDevice> pairedDevices) {
            super.onPostExecute(pairedDevices);
            if (pairedDevices == null) {
                return;
            }
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    Log.e("devices", device.getName() + StringUtilities.LF + device.getAddress());
                    System.out.println("devices" + device.getName() + StringUtilities.LF + device.getAddress());
                    DevicesData d1 = new DevicesData();
                    d1.setAddress(device.getAddress());
                    d1.setName(device.getName());
                    PrinterHelper.this.arrData.add(d1);
                }
                PrinterHelper.this.showDialog(PrinterHelper.this.arrData);
                return;
            }
            Toast.makeText(context, "No Devices Found!", 0).show();
            System.out.println("No devices");
            Log.e("devices", "No devices");
            try {
                PrinterHelper.this.status.put("status", false);
                PrinterHelper.this.status.put("isconnected", -7);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PrinterHelper.this.sendUpdate(PrinterHelper.this.status, true);
        }
    }

    static {
        MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    }

    public void print() {
        new asyncgetDevices().execute(new Void[0]);
    }

    public void showDialog(ArrayList<DevicesData> arrData) {
        String[] arrDevices = new String[arrData.size()];
        for (int i = 0; i < arrData.size(); i++) {
            arrDevices[i] = new StringBuilder(String.valueOf(((DevicesData) arrData.get(i)).getName())).append(StringUtilities.LF).append(((DevicesData) arrData.get(i)).getAddress()).toString();
        }
        activity.runOnUiThread(new C04694(arrDevices, arrData));
    }

    private void sendUpdate(JSONObject obj, boolean keepCallback) {
        if (this.callbackId != null) {
            Log.e("End of plugin", "true");
           // success(new PluginResult(Status.OK, obj), this.callbackId);
        }
    }

    public void onDestroy() {
        onDestroy();
        try {
            if (this.btSocket != null) {
                this.btSocket.close();
            }
            if (this.outStream != null) {
                this.outStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doConnectionTest(String address) throws JSONException {
        try {
            this.retryCount++;
            if (this.retryCount < 3) {
                Thread.sleep(200);
                activity.runOnUiThread(new C04705(address));
                return;
            }
            dismissProgress();
            try {
                this.status.put("status", false);
                sendUpdate(this.status, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void showProgressDialog() {
        this.progressDialog = ProgressDialog.show(context, "Please Wait", "Connecting to printer..", false);
    }

    private void dismissProgress() {
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }
    }

    private String printSepratorcomp() {
        String seprator = "";
        for (int i = 0; i < 137; i++) {
            seprator = new StringBuilder(String.valueOf(seprator)).append("-").toString();
        }
        return seprator;
    }

    private String printSeprator() {
        String seprator = "";
        for (int i = 0; i < 80; i++) {
            seprator = new StringBuilder(String.valueOf(seprator)).append("-").toString();
        }
        return seprator;
    }

    private String printSepratorCompress() {
        String seprator = "";
        for (int i = 0; i < 137; i++) {
            seprator = new StringBuilder(String.valueOf(seprator)).append("-").toString();
        }
        return seprator;
    }


}
