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
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import gbc.sa.vansales.App;
import gbc.sa.vansales.R;
import gbc.sa.vansales.models.DevicesData;
import gbc.sa.vansales.printer.Arabic6822;
import gbc.sa.vansales.printer.JsonRpcUtil;
import gbc.sa.vansales.printer.LinePrinterException;
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

    public void execute(String request,JSONArray jsonArray){
        try{
            this.jArr = jsonArray;
            this.arrData = new ArrayList();
            print();
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
                 //   PrinterHelper.this.status.put("status", false);
                 //   PrinterHelper.this.status.put("isconnected", -1);
                } catch (Exception e) {
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
 //       Log.d("Print Report", this.jArr.toString());
        //printVanStockReport();
        for(int j=0; j<this.jArr.length();j++){
            JSONArray jInner = this.jArr.getJSONArray(j);
            for (int i = 0; i < jInner.length(); i++) {
                JSONObject jDict = jInner.getJSONObject(i);
                String request = jDict.getString(App.REQUEST);
                JSONObject jsnData = jDict.getJSONObject("mainArr");
                if(request.equals(App.LOAD_SUMMARY_REQUEST)){
                    parseLoadSummaryResponse(jsnData, address);
                }
                if(request.equals(App.LOAD_REQUEST)){
                    printLoadRequestReport(jsnData,address);
                }
                if(request.equals(App.SALES_INVOICE)){
                    printSalesInvoice(jsnData,address);
                }
            }
        }
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

    void printVanStockReport(){
        try{
            int i;
            StringBuilder stringBuilder;
            this.hashValues = new HashMap();
            this.hashValues.put("Item#", Integer.valueOf(7));
            this.hashValues.put("Description", Integer.valueOf(37));
            this.hashValues.put("Loaded Qty", Integer.valueOf(8));
            this.hashValues.put("Transfer Qty", Integer.valueOf(0));
            this.hashValues.put("Sale Qty", Integer.valueOf(6));
            this.hashValues.put("Return Qty", Integer.valueOf(6));
            this.hashValues.put("Truck Stock", Integer.valueOf(7));
            this.hashValues.put("Total", Integer.valueOf(9));
            this.hashValues.put("Description", Integer.valueOf(37));
            this.hashPositions = new HashMap();
            this.hashPositions.put("Item#", Integer.valueOf(0));
            this.hashPositions.put("Description", Integer.valueOf(0));
            this.hashPositions.put("Loaded Qty", Integer.valueOf(2));
            this.hashPositions.put("Transfer Qty", Integer.valueOf(2));
            this.hashPositions.put("Sale Qty", Integer.valueOf(2));
            this.hashPositions.put("Return Qty", Integer.valueOf(2));
            this.hashPositions.put("Truck Stock", Integer.valueOf(2));
            this.hashPositions.put("Total", Integer.valueOf(1));
            this.hashPositions.put("Description", Integer.valueOf(0));
            line(this.startln);
            JSONObject object = new JSONObject();
            object.put("ROUTE","1234");
            object.put("DOC DATE","06-02-2017");
            object.put("TIME","12:12:12");
            object.put("SALESMAN","هذا هو الاختبار العربية");
            object.put("CONTACTNO","01234567");
            object.put("supervisorname","No Supervisor");
            object.put("supervisorno","12389930");
            object.put("TourID","1235");
            headervanstockprint(object, 4);
        }
        catch (Exception e){
            e.printStackTrace();
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

    private void printArabic(String data) {
        try {
            if (data.indexOf("@") == -1 || data.indexOf("!") == -1) {
                this.outStream.write(data.getBytes());
                return;
            }
            String start = data.substring(0, data.indexOf("@"));
            String middle = data.substring(data.indexOf("@") + 1, data.indexOf("!"));
            String end = data.substring(data.indexOf("!") + 1, data.length());
            Log.e("start", start);
            Log.e("middle", middle);
            Log.e("end", end);
            Arabic6822 Arabic = new Arabic6822();
            byte[] printbyte = Arabic.Convert(middle, true);
            this.outStream.write(start.getBytes());
            this.outStream.write(printbyte);
            this.outStream.write("  ".getBytes());
            if (end.indexOf("@") == -1 || end.indexOf("!") == -1) {
                this.outStream.write(end.getBytes());
                return;
            }
            String startbet = end.substring(0, end.indexOf("@"));
            String middlebet = end.substring(end.indexOf("@") + 1, end.indexOf("!"));
            String endbet = end.substring(end.indexOf("!") + 1, end.length());
            byte[] printmidbyte = Arabic.Convert(middlebet, true);
            this.outStream.write(startbet.getBytes());
            this.outStream.write(printmidbyte);
            this.outStream.write("  ".getBytes());
            this.outStream.write(endbet.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printheaders(String line, boolean isArabic, int pluscount) {
        if (isArabic) {
            printArabic(line);
        } else {
            try {
                this.outStream.write(line.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.count += pluscount;
    }

    private void line(int ln) {
        for (int i = 0; i < ln; i++) {
            try {
                this.outStream.write(this.NewLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getAccurateText(String text, int width, int position) {
        String finalText = "";
        if (text.length() == width) {
            Log.d("Matched String", text);
            return text;
        } else if (text.length() > width) {
            return text.substring(0, width);
        } else {
            finalText = text;
            Log.d("String", finalText);
            int i;
            switch (position) {
                case 0:
                    for (i = 0; i < width - text.length(); i++) {
                        finalText = finalText.concat(" ");
                    }
                    return finalText;
                case 1 :
                    for (i = 0; i < width - text.length(); i++) {
                        if (i < (width - text.length()) / 2) {
                            finalText = " " + finalText;
                        } else {
                            finalText = new StringBuilder(String.valueOf(finalText)).append(" ").toString();
                        }
                    }
                    return finalText;
                case 2 :
                    for (i = 0; i < width - text.length(); i++) {
                        finalText = " " + finalText;
                    }
                    return finalText;
                default:
                    return finalText;
            }
        }
    }

    private void headervanstockprint(JSONObject object, int type) throws JSONException {
        try {
            this.outStream.write(this.BoldOn);
           /* printheaders(getAccurateText("ROUTE: " + object.getString("ROUTE"), 40, 0) + getAccurateText("DATE:" + object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 40, 2), false, 1);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);*/
            printheaders(getAccurateText("SALESMAN: *" + object.getString("SALESMAN")+"!", 40, 0) + getAccurateText("SALESMAN NO: " + object.getString("CONTACTNO"), 40, 2), true, 1);
            this.outStream.write(this.NewLine);
           /* printheaders(getAccurateText("SUPERVISOR NAME:" + object.getString("supervisorname"), 40, 0) + getAccurateText("SUPERVISOR PHONE: " + object.getString("supervisorno"), 40, 2), false, 1);
            this.outStream.write(this.NewLine);
            printheaders(getAccurateText("TOUR ID:" + object.getString("TourID"), 80, 0), false, 2);*/
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            this.count++;
            if (type == 4) {
                /*this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("VAN STOCK SUMMARY ", 40, 1), false, 1);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);*/
            } else if (type == 10) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("ITEM SALES SUMMARY ", 40, 1), false, 1);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
            } else if (type == 6) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("COMPANY CREDIT SUMMARY", 40, 1), false, 1);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
            } else if (type == 25) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("TEMPORARY CREDIT SUMMARY", 40, 1), false, 1);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
            }
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Parsing Data
    //Load Summary
    void parseLoadSummaryResponse(JSONObject object,String args){
        StringBuffer s1 = new StringBuffer();
        try{
            int i;
            StringBuilder stringBuilder;
            String string;
            int j;
            this.hashValues = new HashMap();
            //this.hashValues.put("ITEM#", Integer.valueOf(4));
            this.hashValues.put("ITEM#", Integer.valueOf(10));
            this.hashValues.put("ENGLISH DESCRIPTION", Integer.valueOf(35));
            this.hashValues.put("ARABIC DESCRIPTION", Integer.valueOf(35));
            this.hashValues.put("UPC ", Integer.valueOf(7));
            this.hashValues.put("BEGIN INV", Integer.valueOf(8));
            this.hashValues.put("LOAD", Integer.valueOf(8));
            this.hashValues.put("ADJUST", Integer.valueOf(8));
            this.hashValues.put("NET LOAD", Integer.valueOf(8));
            this.hashValues.put("VALUE", Integer.valueOf(8));
            //this.hashValues.put("Description", Integer.valueOf(40));
            this.hashPositions = new HashMap();
            this.hashPositions.put("ITEM#", Integer.valueOf(0));
           // this.hashPositions.put("Item#", Integer.valueOf(0));
            this.hashPositions.put("ENGLISH DESCRIPTION", Integer.valueOf(0));
            this.hashPositions.put("ARABIC DESCRIPTION", Integer.valueOf(0));
            this.hashPositions.put("UPC ", Integer.valueOf(2));
            this.hashPositions.put("BEGIN INV", Integer.valueOf(2));
            this.hashPositions.put("LOAD", Integer.valueOf(2));
            this.hashPositions.put("ADJUST", Integer.valueOf(2));
            this.hashPositions.put("NET LOAD", Integer.valueOf(2));
            this.hashPositions.put("VALUE", Integer.valueOf(2));
            this.hashPositions.put("Description", Integer.valueOf(0));
            line(this.startln);
            headerinvprint(object, 1);  //Uncomment to print header
            JSONArray headers = object.getJSONArray("HEADERS");
            String strheader = "";
            String strHeaderBottom = "";
            //int MAXLEngth = 137;
            int MAXLEngth = 137;
            for (i = 0; i < headers.length(); i++) {
                MAXLEngth -= ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue();
            }
            if (MAXLEngth > 0) {
                MAXLEngth /= headers.length();
            }
            for (i = 0; i < headers.length(); i++) {
                stringBuilder = new StringBuilder(String.valueOf(strheader));
                string = headers.getString(i);
                /*if (headers.getString(i).indexOf(" ") == -1) {
                    string = headers.getString(i);
                } else {
                    string = headers.getString(i).substring(0, headers.getString(i).indexOf(" "));
                }*/
                strheader = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                stringBuilder = new StringBuilder(String.valueOf(strHeaderBottom));
                if (headers.getString(i).indexOf(" ") == -1) {
                    string = "";
                } else {
                    string = headers.getString(i).substring(headers.getString(i).indexOf(" "), headers.getString(i).length()).trim();
                }
               // strHeaderBottom = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
            }
            this.outStream.write(this.CompressOn);
            printlines1(strheader, 1, object, 1, args, 1);
           // printlines1(strHeaderBottom, 1, object, 1, args, 1);
            printlines1(printSepratorcomp(), 1, object, 1, args, 1);
            this.outStream.write(this.CompressOff);
            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            for(i =0;i<jData.length();i++){
                JSONArray jArr = jData.getJSONArray(i);
                String strData = "";
                for (j = 0; j < jArr.length(); j++) {
                    int i2;
                    Object obj;
                    String itemDescrion = jArr.getString(j);
                    if (j == 0) {
                        //itemDescrion = new StringBuilder(String.valueOf(i + 1)).toString();
                    } else if (j == 2) {
                        //itemDescrion = "                 *" + jArr.getString(j) + "!";
                        itemDescrion = "          @" + jArr.getString(j) + "!";
                    }
                    stringBuilder = new StringBuilder(String.valueOf(strData));
                    if (j == 2) {
                        //i2 = 60;
                        i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                    } else {
                        i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                    }
                    HashMap hashMap = this.hashPositions;
                    if (j == 9) {
                        obj = "Description";
                    } else {
                        obj = headers.getString(j).toString();
                    }
                    strData = stringBuilder.append(getAccurateText(itemDescrion, i2, ((Integer) hashMap.get(obj)).intValue())).toString();
                }
                this.outStream.write(this.CompressOn);
                this.count++;
                //printlines1(strData, 1, object, 1, args, 1);
                printlines1(strData, 1, object, 1, args, 1);
                this.outStream.write(this.CompressOff);
            }
            this.outStream.write(this.CompressOn);
            printlines1(printSepratorcomp(), 1, object, 1, args, 1);

            //Logic for Total

            //Logic Ends Here
            this.outStream.write(this.CompressOff);
            printlines1(" ", 1, object, 1, args, 1);
            this.outStream.write(this.BoldOn);
            JSONObject jSONObject = object;
            //printlines1(getAccurateText("Load Value : ", 50, 2) + getAccurateText(object.getString("LoadValue"), 12, 2), 1, jSONObject, 1, args, 2);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.BoldOff);
            printlines1(getAccurateText("_____________", 26, 1) + getAccurateText("____________", 27, 1) + getAccurateText("____________", 27, 1), 2, object, 1, args, 1);
            this.outStream.write(this.NewLine);
            printlines1(getAccurateText("STORE KEEPER", 26, 1) + getAccurateText("SUPERVISOR", 26, 1) + getAccurateText("SALESMAN", 26, 1), 2, object, 1, args, 1);
            jSONObject = object;
            printlines1(getAccurateText(object.getString("printstatus"), 80, 1), 2, jSONObject, 2, args, 1);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Load Request
    void printLoadRequestReport(JSONObject object,String args){
        try{
            int i;
            StringBuilder stringBuilder;
           // String string;
            //int j;

            this.hashValues = new HashMap();
            this.hashValues.put("ITEM NO", Integer.valueOf(10));
            this.hashValues.put("ENGLISH DESCRIPTION", Integer.valueOf(40));
            this.hashValues.put("ARABIC DESCRIPTION", Integer.valueOf(40));
            this.hashValues.put("UPC ", Integer.valueOf(7));
            this.hashValues.put("TOTAL UNITS", Integer.valueOf(12));
            this.hashValues.put("UNIT PRICE", Integer.valueOf(12));
            this.hashValues.put("AMOUNT", Integer.valueOf(12));
            //this.hashValues.put("NET LOAD", Integer.valueOf(8));
            //this.hashValues.put("VALUE", Integer.valueOf(8));
            //this.hashValues.put("Description", Integer.valueOf(40));
            this.hashPositions = new HashMap();
            this.hashPositions.put("ITEM NO", Integer.valueOf(0));
            // this.hashPositions.put("Item#", Integer.valueOf(0));
            this.hashPositions.put("ENGLISH DESCRIPTION", Integer.valueOf(0));
            this.hashPositions.put("ARABIC DESCRIPTION", Integer.valueOf(0));
            this.hashPositions.put("UPC ", Integer.valueOf(2));
            this.hashPositions.put("TOTAL UNITS", Integer.valueOf(2));
            this.hashPositions.put("UNIT PRICE", Integer.valueOf(2));
            this.hashPositions.put("AMOUNT", Integer.valueOf(2));
            // this.hashPositions.put("NET LOAD", Integer.valueOf(2));
            // this.hashPositions.put("VALUE", Integer.valueOf(2));
            // this.hashPositions.put("Description", Integer.valueOf(0));
            line(this.startln);
            headerinvprint(object, 5);

            JSONArray headers = object.getJSONArray("HEADERS");
            String strheader = "";
            String strHeaderBottom = "";
            String strTotal = "";
            JSONArray jTotal = object.getJSONArray("TOTAL");
            int MAXLEngth = 137;
            for (i = 0; i < headers.length(); i++) {
                MAXLEngth -= ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue();
            }
            if (MAXLEngth > 0) {
                MAXLEngth /= headers.length();
            }
            JSONObject jTOBject = jTotal.getJSONObject(0);
            for (i = 0; i < headers.length(); i++) {
                try {
                    String string;
                    stringBuilder = new StringBuilder(String.valueOf(strheader));
                    string = headers.getString(i);
                    /*if (headers.getString(i).indexOf(" ") == -1) {
                        string = headers.getString(i);
                    } else {
                        string = headers.getString(i).substring(0, headers.getString(i).indexOf(" "));
                    }*/
                    strheader = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    stringBuilder = new StringBuilder(String.valueOf(strHeaderBottom));
                    if (headers.getString(i).indexOf(" ") == -1) {
                        string = "";
                    } else {
                        string = headers.getString(i).substring(headers.getString(i).indexOf(" "), headers.getString(i).length());
                    }
                    //strHeaderBottom = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    if (jTOBject.has(headers.getString(i))) {
                        strTotal = new StringBuilder(String.valueOf(strTotal)).append(getAccurateText(jTOBject.getString(headers.getString(i).toString()), ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    } else {
                        stringBuilder = new StringBuilder(String.valueOf(strTotal));
                        if (headers.getString(i).equals("ARABIC DESCRIPTION")) {
                            string = "TOTAL";
                        } else {
                            string = "";
                        }
                        strTotal = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i))).intValue() + MAXLEngth, 1)).toString();
                    }
                } catch (Exception e) {
                }
            }
            this.outStream.write(this.CompressOn);
            printlines1(strheader, 1, object, 1, args, 1);
           // printlines1(strHeaderBottom, 1, object, 1, args, 5);
            printlines1(printSepratorcomp(), 1, object, 1, args, 1);
            this.outStream.write(this.CompressOff);
            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            for (i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String strData = "";
                for (int j = 0; j < jArr.length(); j++) {
                    int i2;
                    Object obj;
                    String itemDescrion = jArr.getString(j);
                    if (j == 0) {
                        //itemDescrion = new StringBuilder(String.valueOf(i + 1)).toString();
                    } else if (j == 2) {
                        itemDescrion = "           @" + jArr.getString(j) + "!";
                    }
                    stringBuilder = new StringBuilder(String.valueOf(strData));
                    if (j == 2) {
                        //i2 = 60;
                        i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                    } else {
                        i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                    }
                    HashMap hashMap = this.hashPositions;
                    if (j == 7) {
                        obj = "Description";
                    } else {
                        obj = headers.getString(j).toString();
                    }
                    strData = stringBuilder.append(getAccurateText(itemDescrion, i2, ((Integer) hashMap.get(obj)).intValue())).toString();
                }
                this.outStream.write(this.CompressOn);
                this.count++;
                printlines1(strData, 1, object, 1, args, 1);
                this.outStream.write(this.CompressOff);
            }
            this.outStream.write(this.CompressOn);
            printlines1(printSepratorcomp(), 1, object, 1, args, 1);
            printlines1(strTotal, 1, object, 1, args, 1);
            this.outStream.write(this.CompressOff);
            printlines1(getAccurateText("", 80, 1), 2, object, 1, args, 1);
            this.outStream.write(this.BoldOn);
            JSONObject jSONObject = object;
            //printlines1(getAccurateText("Net Value : ", 50, 2) + getAccurateText(object.getString("netvalue"), 12, 2), 3, jSONObject, 1, args, 5);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.BoldOff);
            printlines1(getAccurateText("_____________", 40, 1)+ getAccurateText("____________", 40, 1), 2, object, 1, args, 5);
            printlines1(getAccurateText("STORE KEEPER", 40, 1) + getAccurateText("SALESMAN", 40, 1), 2, object, 1, args, 5);
            jSONObject = object;
            //printlines1(getAccurateText(object.getString("printstatus"), 80, 1), 2, jSONObject, 2, args, 5);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    //Sales Invoice
    void printSalesInvoice(JSONObject object,String args){
        StringBuffer s1 = new StringBuffer();
        try{
            int printoultlet;
            int j;
            JSONArray jCheques;
            JSONObject jCash;
            JSONObject jChequeDetails;
            String copyStatus;
            /*if (object.getString("printoutletitemcode").length() > 0) {
                printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
            } else {
                printoultlet = 0;
            }*/
            if (object.getString("displayupc").equals("1")) {
                this.hashValues = new HashMap();
                this.hashValues.put("SL#", Integer.valueOf(4));
                this.hashValues.put("ITEM#", Integer.valueOf(11));
                this.hashValues.put("OUTLET CODE", Integer.valueOf(10));
                this.hashValues.put("DESCRIPTION", Integer.valueOf(43));
                this.hashValues.put("UPO", Integer.valueOf(5));
                this.hashValues.put("QTY CAS/PCS", Integer.valueOf(11));
                this.hashValues.put("QTY OUT/PCS", Integer.valueOf(11));
                this.hashValues.put("TOTAL PCS", Integer.valueOf(0));
                this.hashValues.put("CASE PRICE", Integer.valueOf(11));
                this.hashValues.put("UNIT PRICE", Integer.valueOf(11));
                this.hashValues.put("DISCOUNT", Integer.valueOf(13));
                this.hashValues.put("AMOUNT", Integer.valueOf(11));
                this.hashValues.put("DESCRIPTION", Integer.valueOf(38));
                this.hashValues.put("OUTER PRICE", Integer.valueOf(11));
                this.hashValues.put("PCS PRICE", Integer.valueOf(11));
                this.hashValues.put("REASON CODE", Integer.valueOf(11));
                this.hashPositions = new HashMap();
                this.hashPositions.put("SL#", Integer.valueOf(0));
                this.hashPositions.put("ITEM#", Integer.valueOf(0));
                this.hashPositions.put("OUTLET CODE", Integer.valueOf(0));
                this.hashPositions.put("DESCRIPTION", Integer.valueOf(0));
                this.hashPositions.put("UPO", Integer.valueOf(1));
                this.hashPositions.put("QTY CAS/PCS", Integer.valueOf(1));
                this.hashPositions.put("QTY OUT/PCS", Integer.valueOf(1));
                this.hashPositions.put("TOTAL PCS", Integer.valueOf(1));
                this.hashPositions.put("CASE PRICE", Integer.valueOf(2));
                this.hashPositions.put("UNIT PRICE", Integer.valueOf(2));
                this.hashPositions.put("DISCOUNT", Integer.valueOf(2));
                this.hashPositions.put("AMOUNT", Integer.valueOf(2));
                this.hashPositions.put("DESCRIPTION", Integer.valueOf(0));
                this.hashPositions.put("OUTER PRICE", Integer.valueOf(2));
                this.hashPositions.put("PCS PRICE", Integer.valueOf(2));
                this.hashPositions.put("REASON CODE", Integer.valueOf(2));
            } else {
                this.hashValues = new HashMap();
                this.hashValues.put("SL#", Integer.valueOf(4));
                this.hashValues.put("ITEM", Integer.valueOf(8));
                this.hashValues.put("DESCRIPTION", Integer.valueOf(32));
                this.hashValues.put("DESCRIPTION(AR)", Integer.valueOf(32));
                this.hashValues.put("QTY CAS/PCS", Integer.valueOf(3));
                this.hashValues.put("CASE PRICE", Integer.valueOf(7));
                this.hashValues.put("UNIT PRICE", Integer.valueOf(7));
                this.hashValues.put("AMOUNT", Integer.valueOf(8));
                this.hashPositions = new HashMap();
                this.hashPositions.put("SL#", Integer.valueOf(0));
                this.hashPositions.put("ITEM", Integer.valueOf(0));
                this.hashPositions.put("DESCRIPTION", Integer.valueOf(0));
                this.hashPositions.put("DESCRIPTION(AR)", Integer.valueOf(0));
                this.hashPositions.put("QTY CAS/PCS", Integer.valueOf(2));
                this.hashPositions.put("CASE PRICE", Integer.valueOf(2));
                this.hashPositions.put("UNIT PRICE", Integer.valueOf(2));
                this.hashPositions.put("DISCOUNT", Integer.valueOf(2));
                this.hashPositions.put("AMOUNT", Integer.valueOf(2));
            }
            line(this.startln);
            headerprint(object, 1);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //Printing Headers

    private void printlines1(String data, int ln, JSONObject object, int sts, String adr, int tp) throws JSONException, IOException, LinePrinterException {
        int i;
        this.count += ln;
        boolean isEnd = false;
        if (sts == 2 && this.count != 0) {
            Log.e("Going for Arabic1","Arabic" + data);
            printArabic(data);
            isEnd = true;
            int lnno = (48 - this.count) + this.endln;
            for (i = 0; i < lnno; i++) {
                try {
                    if (i % 10 == 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    this.outStream.write(this.NewLine);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            this.outStream.write(this.CarriageReturn);
            this.count = 0;
            try {
                Thread.sleep(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
            this.status.put("status", true);
            this.status.put("isconnected", 0);
            sendUpdate(this.status, true);
        }
        if (!isEnd) {
            printArabic(data);
            Log.e("Going for Arabic", "Arabic" + data);
            for (i = 0; i < ln; i++) {
                try {
                    this.outStream.write(this.NewLine);
                } catch (IOException e22) {
                    e22.printStackTrace();
                }
            }
            if (this.count % 10 == 0) {
                try {
                    Thread.sleep(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
                } catch (InterruptedException e32) {
                    e32.printStackTrace();
                }
            }
            if (this.count > 48) {
                Log.e("Count 1 time", "Count " + this.count);
                this.outStream.write(this.CompressOff);
                this.count = 0;
                try {
                    line(this.cnln);
                    if (tp == 4) {
                        headervanstockprint(object, tp);
                    } else if (tp == 6 || tp == 25) {
                        headervanstockprint(object, tp);
                    } else {
                        headerinvprint(object, tp);
                    }
                    this.outStream.write(printSeprator().getBytes());
                    this.count++;
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    private void headerinvprint(JSONObject object, int invtype) throws JSONException {
        try {
            this.outStream.write(this.BoldOn);
            printheaders(getAccurateText("ROUTE#: " + object.getString("ROUTE"), 26, 0) + getAccurateText("SMAN#: " + object.getString("SALESMAN"), 26, 0) + getAccurateText("DATE:" + object.getString("DOC DATE") + " " + object.getString("TIME"), 26, 2), false, 1);
            this.outStream.write(this.NewLine);
            printheaders(getAccurateText(object.getString("TIME") + " " + object.getString("DOC DATE") + " " + "@" + ArabicLabels.Date, 26, 0) + "!" + getAccurateText(object.getString("SALESMAN") + " " + "@" + ArabicLabels.SalesMan, 26, 1) + "!", true, 1);
            printheaders(getAccurateText(object.getString("ROUTE") + "@" + ArabicLabels.Route, 26, 2)+"!" + " ",true,1);
            this.outStream.write(this.NewLine);
           // printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMAN"), 40, 0) + getAccurateText("SALESMAN NO: " + object.getString("CONTACTNO"), 40, 2), false, 1);
           // this.outStream.write(this.NewLine);
            try {
                printheaders(getAccurateText("DOCUMENT NO: " + object.getString("DOCUMENT NO"), 40, 0) + getAccurateText("TRIP START DATE:" + object.getString("TRIP START DATE"), 40, 2), false, 1);
                this.outStream.write(this.NewLine);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //printheaders(getAccurateText("SUPERVISOR NAME:" + object.getString("supervisorname"), 40, 0) + getAccurateText("SUPERVISOR PHONE: " + object.getString("supervisorno"), 40, 2), true, 1);
            this.outStream.write(this.NewLine);
            printheaders(getAccurateText("TRIP ID:" + object.getString("TripID"), 80, 0), false, 2);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.BoldOn);
            this.outStream.write(this.DoubleWideOn);
            if (invtype == 1) {
                printheaders(getAccurateText("LOAD SUMMARY ", 40, 1), false, 1);
                printheaders(getAccurateText("LOAD NUMBER: " + object.getString("Load Number"), 40, 1), false, 1);
            } else if (invtype == 2) {
                printheaders(getAccurateText("LOAD TRANSFER SUMMARY", 40, 1), false, 1);
            } else if (invtype == 3) {
                printheaders(getAccurateText("END INVENTORY SUMMARY", 40, 1), false, 2);
            } else if (invtype == 5) {
                printheaders(getAccurateText("LOAD REQUEST - " + Helpers.formatDate(new Date(),App.PRINT_DATE_FORMAT), 40, 1), false, 1);
            } else if (invtype == 6) {
                printheaders(getAccurateText("COMPANY CREDIT SUMMARY", 40, 1), false, 1);
            }
            this.outStream.write(this.DoubleWideOff);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            /*if (invtype == 2) {
                JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
                if (jData.getJSONObject(0).getJSONArray("DATA").length() > 0 && (jData.getJSONObject(1).getJSONArray("DATA").length() > 0 || jData.getJSONObject(2).getJSONArray("DATA").length() > 0)) {
                    printheaders(getAccurateText("FROM & TO ROUTE: " + object.getString("TO ROUTE"), 80, 0), false, 1);
                    this.outStream.write(this.NewLine);
                } else if (jData.getJSONObject(0).getJSONArray("DATA").length() > 0) {
                    printheaders(getAccurateText("FROM ROUTE: " + object.getString("TO ROUTE"), 80, 0), false, 1);
                    this.outStream.write(this.NewLine);
                } else if (jData.getJSONObject(1).getJSONArray("DATA").length() > 0) {
                    printheaders(getAccurateText("TO ROUTE: " + object.getString("TO ROUTE"), 80, 0), false, 1);
                    this.outStream.write(this.NewLine);
                } else if (jData.getJSONObject(2).getJSONArray("DATA").length() > 0) {
                    printheaders(getAccurateText("TO ROUTE: " + object.getString("TO ROUTE"), 80, 0), false, 1);
                    this.outStream.write(this.NewLine);
                }
            }
            if (invtype == 5) {
                printheaders(getAccurateText("Requested Delivery Date : " + object.getString("Requestdate"), 80, 0), false, 1);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
            }*/
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    private void headerprint(JSONObject object, int type) throws JSONException {
        try {
            this.outStream.write(this.BoldOn);
            printheaders(getAccurateText("ROUTE: " + object.getString("ROUTE"), 40, 0) + getAccurateText("DATE:" + object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 40, 2), true, 1);
            this.outStream.write(this.NewLine);
            printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMAN"), 40, 0) + getAccurateText("SALESMAN NO: " + object.getString("CONTACTNO"), 40, 2), true, 1);
            this.outStream.write(this.NewLine);
            //printheaders(getAccurateText("SUPERVISOR NAME:" + object.getString("supervisorname"), 40, 0) + getAccurateText("SUPERVISOR PHONE: " + object.getString("supervisorno"), 40, 2), true, 1);
            if (type == 3 || type == 5 || type == 6 || type == 4 || type == 7) {
                printheaders(getAccurateText("TRIP START DATE:" + object.getString("TRIP START DATE"), 40, 0) + getAccurateText("TOUR ID:" + object.getString("TourID"), 40, 2), false, 1);
            } else {
                printheaders(getAccurateText("TRIP ID:" + object.getString("TourID"), 80, 0), false, 1);
            }
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            if (!(type == 3 && type == 6 && type == 4 && type == 5 && type == 7) && object.has("invheadermsg") && object.getString("invheadermsg").length() > 0) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(object.getString("invheadermsg"), false, 3);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.DoubleWideOff);
            }
            if (type == 1) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                if (object.getString("LANG").equals("en")) {
                    printheaders(getAccurateText(object.getString("INVOICETYPE"), 40, 1), false, 2);
                } else if (object.getString("invoicepaymentterms").contains("2")) {
                    printheaders(getAccurateText("@" + ArabicTEXT.Creditinvoice + "!:" + object.getString("invoicenumber"), 40, 1), true, 2);
                } else if (object.getString("invoicepaymentterms").contains("0") || object.getString("invoicepaymentterms").contains("1")) {
                    printheaders(getAccurateText("@" + ArabicTEXT.Cashinvoice + "!:" + object.getString("invoicenumber"), 40, 1), true, 2);
                } else {
                    printheaders(getAccurateText(object.getString("INVOICETYPE"), 40, 1), false, 2);
                }
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.BoldOn);
                try {
                    String[] parts = object.getString("CUSTOMER").split("\\-");
                    printheaders("CUSTOMER: " + parts[0], false, 2);
                    this.outStream.write(this.NewLine);
                    printheaders("          *" + parts[1] + "!", true, 1);
                    this.outStream.write(this.NewLine);
                    this.outStream.write(this.BoldOff);
                    printheaders("ADDRESS: " + object.getString("ADDRESS"), false, 1);
                    this.outStream.write(this.NewLine);
                    printheaders("          @" + object.getString("ARBADDRESS") + "!", true, 1);
                    this.outStream.write(this.NewLine);
                } catch (Exception e) {
                }
                this.count++;
            } else if (type == 2) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                if (object.getString("LANG").equals("en")) {
                    printheaders(getAccurateText("RECEIPT: " + object.getString("RECEIPT"), 40, 1), false, 2);
                } else {
                    printheaders(getAccurateText("*" + ArabicTEXT.Receipt + "!:" + object.getString("RECEIPT"), 40, 1), true, 2);
                }
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.BoldOn);
                printheaders("CUSTOMER: " + object.getString("CUSTOMER"), true, 1);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.BoldOff);
                printheaders("ADDRESS :" + object.getString("ADDRESS"), true, 1);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
            } else if (type == 3) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("DEPOSIT SUMMARY", 40, 1), false, 3);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
                this.count++;
            } else if (type == 4) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("SALES SUMMARY", 40, 1), false, 1);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
            } else if (type == 5) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("ROUTE ACTIVITY LOG", 40, 1), false, 1);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
            } else if (type == 6) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("ROUTE SUMMARY", 40, 1), false, 1);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
            } else if (type == 7) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("STALES/DAMAGE SUMMARY", 40, 1), false, 3);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
