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
import android.support.v4.media.TransportMediator;
import android.support.v4.widget.ExploreByTouchHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

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
import gbc.sa.vansales.activities.DriverPaymentDetails;
import gbc.sa.vansales.activities.LoadRequestActivity;
import gbc.sa.vansales.activities.LoadVerifyActivity;
import gbc.sa.vansales.activities.PaymentDetails;
import gbc.sa.vansales.activities.PreSaleOrderProceedActivity;
import gbc.sa.vansales.activities.PromotioninfoActivity;
import gbc.sa.vansales.activities.UnloadActivity;
import gbc.sa.vansales.models.DevicesData;
import gbc.sa.vansales.models.LoadSummary;
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
    byte[] wakeUp;
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
        this.wakeUp = new byte[]{(byte) (Integer.parseInt("1B",16) & 0xff),(byte) (Integer.parseInt("40",16) & 0xff)};
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
            Crashlytics.logException(e);
        }
    }

    class C04641 extends BroadcastReceiver {
        C04641() {
        }

        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            try{
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
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
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
                Crashlytics.logException(e);
            }
        }
    }

    class C04663 extends BroadcastReceiver {
        C04663() {
        }

        public void onReceive1(Context context, Intent intent) {
            try{
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
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
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
                    Crashlytics.logException(e);
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
                Crashlytics.logException(e2);
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
                    Crashlytics.logException(e);
                }
                return;
            }
            try {
                PrinterHelper.this.doConnectionTest(PrinterHelper.this.sMacAddr);
            } catch (JSONException e) {
                e.printStackTrace();
                Crashlytics.logException(e);
            }
        }

        protected void onPreExecute() {
        }
    }

    @SuppressLint({"NewApi"})
    void printReports(String address) throws JSONException {
        Log.e("Print Report",""+ this.jArr.toString());
        Log.e("Array Length", "" + this.jArr.length());
 //       Log.d("Print Report", this.jArr.toString());
        //printVanStockReport();

        for(int j=0; j<(this.jArr.length()>1?1:this.jArr.length());j++){
            JSONArray jInner = this.jArr.getJSONArray(j);
            for (int i = 0; i < jInner.length(); i++) {
                JSONObject jDict = jInner.getJSONObject(i);
                String request = jDict.getString(App.REQUEST);
                JSONObject jsnData = jDict.getJSONObject("mainArr");
                if(request.equals(App.LOAD_SUMMARY_REQUEST)){
                    parseLoadSummaryResponse(jsnData, address);
                }
                if(request.equals(App.LOAD_REQUEST)){
                    printLoadRequestReport(jsnData, address);
                }
                if(request.equals(App.ORDER_REQUEST)){
                    printOrderRequestReport(jsnData, address);
                }
                if(request.equals(App.SALES_INVOICE)){
                    printSalesInvoice(jsnData, address);
                }
                if(request.equals(App.DELIVERY)){
                    printDelivery(jsnData, address);
                }
                if(request.equals(App.COLLECTION)){
                    parseCollectionResponse(jsnData, address);
                }
                if(request.equals(App.DRIVER_COLLECTION)){
                    parseDriverCollectionResponse(jsnData, address);
                }
                if(request.equals(App.UNLOAD)){
                    parseEndInventory(jsnData, address);
                }
                if(request.equals(App.ROUTE_SALES)){
                    parseRouteSales(jsnData, address);
                }
                if(request.equals(App.DEPOSIT_REPORT)){
                    parseDepositCashResponse(jsnData, address);
                }
                if(request.equals(App.SALES_SUMMARY)){
                    printSalesSummaryReport(jsnData, address);
                }
                if(request.equals(App.BAD_RETURN_REPORT)){
                    printBadReturnsReport(jsnData, address);
                }
                if(request.equals(App.VAN_STOCK)){
                    printVanStockReport(jsnData,address);
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            try{
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
            catch (Exception e){
                e.printStackTrace();
                Crashlytics.logException(e);
            }
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
            Crashlytics.logException(e2);
        }
    }

    private void showProgressDialog() {
        try{
            this.progressDialog = ProgressDialog.show(context, "Please Wait", "Connecting to printer..", false);
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }

    private void dismissProgress() {
        try{
            if (this.progressDialog != null && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Crashlytics.logException(e);
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
            Crashlytics.logException(e);
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
                Crashlytics.logException(e);
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
            printheaders(getAccurateText("ROUTE: " + object.getString("ROUTE"), 40, 0) + getAccurateText("DATE:" + object.getString("DOC DATE") + " (" + object.getString("TIME") + ")", 40, 2), false, 1);
            this.outStream.write(this.NewLine);
            //printheaders(getAccurateText(object.getString("TIME") + " " + object.getString("DOC DATE"), 40, 2), true, 1);
            //printheaders(getAccurateText(object.getString("ROUTE") + "@" + ArabicLabels.Route, 40, 0)+"!" + " ",true,1);
            this.outStream.write(this.NewLine);
            printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMAN")+"", 40, 0) + getAccurateText("SALESMAN NO: " + object.getString("SALESMANNO"), 40, 2), true, 1);
            this.outStream.write(this.NewLine);
           /* printheaders(getAccurateText("SUPERVISOR NAME:" + object.getString("supervisorname"), 40, 0) + getAccurateText("SUPERVISOR PHONE: " + object.getString("supervisorno"), 40, 2), false, 1);
            this.outStream.write(this.NewLine);*/
            printheaders(getAccurateText("TRIP ID:" + object.getString("TourID"), 80, 0), false, 2);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            this.count++;
            if (type == 4) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("VAN STOCK SUMMARY ", 40, 1), false, 1);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
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
            Crashlytics.logException(e);
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
            //String strTotal = "";
            //JSONArray jTotal = object.getJSONArray("TOTAL");

            int MAXLEngth = 137;
            for (i = 0; i < headers.length(); i++) {
                MAXLEngth -= ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue();
            }
            if (MAXLEngth > 0) {
                MAXLEngth /= headers.length();
            }
            //JSONObject jTOBject = jTotal.getJSONObject(0);
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

            /*JSONArray jTotal = object.getJSONArray("TOTAL");
            for (i = 0; i < jTotal.length(); i++) {
                JSONObject jTOBject = jTotal.getJSONObject(0);
                String strTotal = "";
                for (j = 0; j < headers.length(); j++) {
                    if (jTOBject.has(headers.getString(j))) {
                        strTotal = new StringBuilder(String.valueOf(strTotal)).append(getAccurateText(jTOBject.getString(headers.getString(j).toString()), ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(j).toString())).intValue())).toString();
                    } else {
                        stringBuilder = new StringBuilder(String.valueOf(strTotal));
                        if (headers.getString(j).equals("ARABIC DESCRIPTION")) {
                            string = "TOTAL";
                        } else {
                            string = "";
                        }
                        strTotal = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(j))).intValue() + MAXLEngth, 1)).toString();
                    }
                }
                printlines1(strTotal, 1, object, 1, args, 1);
            }*/


            //rintlines1(strTotal, 1, object, 1, args, 1);
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
            printlines1(getAccurateText("STORE KEEPER", 26, 1) + getAccurateText("DUTY SUPERVISOR", 26, 1) + getAccurateText("SALESMAN", 26, 1), 2, object, 1, args, 1);
            jSONObject = object;
           // printlines1(getAccurateText(object.getString("printstatus"), 80, 1), 2, jSONObject, 2, args, 1);
            //closeConnection();
            if(context instanceof LoadVerifyActivity){
                ((LoadVerifyActivity) context).callbackFunction();
            }
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
            this.outStream.write(this.wakeUp);
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
            printlines1(getAccurateText("_____________", 40, 1) + getAccurateText("____________", 40, 1), 2, object, 1, args, 5);
            printlines1(getAccurateText("STORE KEEPER", 40, 1) + getAccurateText("SALESMAN", 40, 1), 2, object, 1, args, 5);

            //closeConnection();
            if(context instanceof LoadRequestActivity){
                ((LoadRequestActivity)context).callback();
            }
            jSONObject = object;
            //printlines1(getAccurateText(object.getString("printstatus"), 80, 1), 2, jSONObject, 2, args, 5);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    //Load Request
    void printOrderRequestReport(JSONObject object,String args){
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
            headerprint(object, 1);

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
            printlines1(getAccurateText("_____________", 40, 1) + getAccurateText("____________", 40, 1), 2, object, 1, args, 5);
            printlines1(getAccurateText("CUSTOMER", 40, 1) + getAccurateText("SALESMAN", 40, 1), 2, object, 1, args, 5);
            jSONObject = object;
            if(context instanceof PreSaleOrderProceedActivity){
                ((PreSaleOrderProceedActivity)context).callback();
            }
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
            JSONArray jCheques;
            JSONObject jCash;
            JSONObject jChequeDetails;
            String copyStatus;
            JSONObject jSONObject;
            int i;
            StringBuilder stringBuilder;
            /*if (object.getString("printoutletitemcode").length() > 0) {
                printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
            } else {
                printoultlet = 0;
            }*/
            if (object.getString("displayupc").equals("1")) {

                this.hashValues = new HashMap();
                this.hashValues.put("ITEM NO", Integer.valueOf(10));
                this.hashValues.put("ENGLISH DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("ARABIC DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("UPC ", Integer.valueOf(7));
                this.hashValues.put("TOTAL UNITS", Integer.valueOf(12));
                this.hashValues.put("UNIT PRICE", Integer.valueOf(12));
                this.hashValues.put("AMOUNT", Integer.valueOf(12));
                this.hashValues.put("REASON CODE", Integer.valueOf(11));
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
                this.hashPositions.put("REASON CODE", Integer.valueOf(2));


            } else {
                this.hashValues = new HashMap();
                this.hashValues.put("ITEM NO", Integer.valueOf(10));
                this.hashValues.put("ENGLISH DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("ARABIC DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("UPC ", Integer.valueOf(7));
                this.hashValues.put("TOTAL UNITS", Integer.valueOf(12));
                this.hashValues.put("UNIT PRICE", Integer.valueOf(12));
                this.hashValues.put("AMOUNT", Integer.valueOf(12));
                this.hashValues.put("REASON CODE", Integer.valueOf(11));
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
                this.hashPositions.put("REASON CODE", Integer.valueOf(2));
            }
            this.outStream.write(this.wakeUp);
            line(this.startln);
            headerprint(object, 9);
            this.outStream.write(this.BoldOn);
            this.outStream.write(this.DoubleWideOn);
            //printheaders(object.getString("invheadermsg"), false, 3);
            printheaders(getAccurateText("SALES", 40, 1), false, 2);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.DoubleWideOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);

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

            this.outStream.write(this.BoldOn);
            this.outStream.write(this.DoubleWideOn);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            //printheaders(object.getString("invheadermsg"), false, 3);

            JSONArray focData = object.getJSONArray("foc");
            if(focData.length()>0){
                printheaders(getAccurateText("FREE GOODS", 40, 1), false, 2);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);

                this.outStream.write(this.CompressOn);
                printlines1(strheader, 1, object, 1, args, 1);
                // printlines1(strHeaderBottom, 1, object, 1, args, 5);
                printlines1(printSepratorcomp(), 1, object, 1, args, 1);
                this.outStream.write(this.CompressOff);

                for (i = 0; i < focData.length(); i++) {
                    JSONArray jArr = focData.getJSONArray(i);
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
            }

            JSONArray grData = object.getJSONArray("gr");
            if(grData.length()>0){
                printheaders(getAccurateText("GOOD RETURN", 40, 1), false, 2);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);

                this.outStream.write(this.CompressOn);
                printlines1(strheader, 1, object, 1, args, 1);
                // printlines1(strHeaderBottom, 1, object, 1, args, 5);
                printlines1(printSepratorcomp(), 1, object, 1, args, 1);
                this.outStream.write(this.CompressOff);

                for (i = 0; i < grData.length(); i++) {
                    JSONArray jArr = grData.getJSONArray(i);
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
            }


            this.outStream.write(this.BoldOn);
            this.outStream.write(this.DoubleWideOn);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            JSONArray brData = object.getJSONArray("br");
            if(brData.length()>0){
                //printheaders(object.getString("invheadermsg"), false, 3);
                printheaders(getAccurateText("BAD RETURN", 40, 1), false, 2);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.DoubleWideOff);
                // this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);

                this.outStream.write(this.CompressOn);
                printlines1(strheader, 1, object, 1, args, 1);
                // printlines1(strHeaderBottom, 1, object, 1, args, 5);
                printlines1(printSepratorcomp(), 1, object, 1, args, 1);
                this.outStream.write(this.CompressOff);

                for (i = 0; i < brData.length(); i++) {
                    JSONArray jArr = brData.getJSONArray(i);
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
            }


            //Gagdadfdsafsd
            this.outStream.write(this.CompressOn);
            printlines1(printSepratorcomp(), 1, object, 1, args, 1);
            printlines1(strTotal, 1, object, 1, args, 1);
            this.outStream.write(this.CompressOff);
            printlines1(getAccurateText("", 80, 1), 2, object, 1, args, 1);
            this.outStream.write(this.BoldOn);
            jSONObject = object;
            if (object.getString("invoicepriceprint").equals("1")) {
                jSONObject = object;
                printlines2(getAccurateText("SUB TOTAL", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("SUB TOTAL") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.SubTotal, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.BoldOn);
                if (object.has("INVOICE DISCOUNT")) {
                    Log.e("I have discount", "Discount");
                    if (object.getString("INVOICE DISCOUNT").toString().length() > 0) {
                        Log.e("I have discount","Discount1");
                        if (Double.parseDouble(object.getString("INVOICE DISCOUNT")) > 0.0d) {
                            jSONObject = object;
                            printlines2(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("INVOICE DISCOUNT") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                        }
                        else{
                            Log.e("I have discount","Discount Else");
                            jSONObject = object;
                            printlines2(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("INVOICE DISCOUNT") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                        }
                    }
                }
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.BoldOn);
                jSONObject = object;
                printlines2(getAccurateText("NET SALES", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("NET SALES") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.NetSales, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                this.outStream.write(this.BoldOff);
            }
            //printlines1(getAccurateText("Net Value : ", 50, 2) + getAccurateText(object.getString("netvalue"), 12, 2), 3, jSONObject, 1, args, 5);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);

            printlines1(getAccurateText("_____________", 40, 1) + getAccurateText("____________", 40, 1), 2, object, 1, args, 5);
            printlines1(getAccurateText("CUSTOMER", 40, 1) + getAccurateText("SALESMAN", 40, 1), 2, object, 1, args, 5);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);

            printlines1(getAccurateText(object.getString("invoicefooter"), 80, 1), 1, object, 1, args, 5);
           // printlines1(getAccurateText("For any complaints please contact " + object.getString("supervisorname") + "on" + object.getString("supervisorno"), 40, 0), false, 1);

            //jSONObject = object;

            if(context instanceof PromotioninfoActivity){
                closeConnection();
                ((PromotioninfoActivity)context).callback(App.SALES_INVOICE);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //Delivery
    void printDelivery(JSONObject object,String args){
        StringBuffer s1 = new StringBuffer();
        try{
            int printoultlet;
            JSONArray jCheques;
            JSONObject jCash;
            JSONObject jChequeDetails;
            String copyStatus;
            JSONObject jSONObject;
            int i;
            StringBuilder stringBuilder;
            /*if (object.getString("printoutletitemcode").length() > 0) {
                printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
            } else {
                printoultlet = 0;
            }*/
            if (object.getString("displayupc").equals("1")) {

                this.hashValues = new HashMap();
                this.hashValues.put("ITEM NO", Integer.valueOf(10));
                this.hashValues.put("ENGLISH DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("ARABIC DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("UPC ", Integer.valueOf(7));
                this.hashValues.put("TOTAL UNITS", Integer.valueOf(12));
                this.hashValues.put("UNIT PRICE", Integer.valueOf(12));
                this.hashValues.put("AMOUNT", Integer.valueOf(12));
                this.hashValues.put("REASON CODE", Integer.valueOf(11));
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
                this.hashPositions.put("REASON CODE", Integer.valueOf(2));


            } else {
                this.hashValues = new HashMap();
                this.hashValues.put("ITEM NO", Integer.valueOf(10));
                this.hashValues.put("ENGLISH DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("ARABIC DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("UPC ", Integer.valueOf(7));
                this.hashValues.put("TOTAL UNITS", Integer.valueOf(12));
                this.hashValues.put("UNIT PRICE", Integer.valueOf(12));
                this.hashValues.put("AMOUNT", Integer.valueOf(12));
                this.hashValues.put("REASON CODE", Integer.valueOf(11));
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
                this.hashPositions.put("REASON CODE", Integer.valueOf(2));
            }
            line(this.startln);
            headerprint(object, 10);
            this.outStream.write(this.BoldOn);
            this.outStream.write(this.DoubleWideOn);
            //printheaders(object.getString("invheadermsg"), false, 3);
            printheaders(getAccurateText("SALES", 40, 1), false, 2);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.DoubleWideOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);

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

            this.outStream.write(this.BoldOn);
            this.outStream.write(this.DoubleWideOn);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            //printheaders(object.getString("invheadermsg"), false, 3);
                        //Gagdadfdsafsd
            this.outStream.write(this.CompressOn);
            printlines1(printSepratorcomp(), 1, object, 1, args, 1);
            printlines1(strTotal, 1, object, 1, args, 1);
            this.outStream.write(this.CompressOff);
            printlines1(getAccurateText("", 80, 1), 2, object, 1, args, 1);
            this.outStream.write(this.BoldOn);
            jSONObject = object;
            if (object.getString("invoicepriceprint").equals("1")) {
                jSONObject = object;
                printlines2(getAccurateText("SUB TOTAL", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("SUB TOTAL") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.SubTotal, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.BoldOn);
                if (object.has("INVOICE DISCOUNT")) {
                    if (object.getString("INVOICE DISCOUNT").toString().length() > 0) {
                        if (Double.parseDouble(object.getString("INVOICE DISCOUNT")) > 0.0d) {
                            jSONObject = object;
                            printlines2(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("INVOICE DISCOUNT") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                        }
                    }
                }
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.BoldOn);
                jSONObject = object;
                printlines2(getAccurateText("NET SALES", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("NET SALES") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.NetSales, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                this.outStream.write(this.BoldOff);
            }
            //printlines1(getAccurateText("Net Value : ", 50, 2) + getAccurateText(object.getString("netvalue"), 12, 2), 3, jSONObject, 1, args, 5);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);

            printlines1(getAccurateText("_____________", 40, 1) + getAccurateText("____________", 40, 1), 2, object, 1, args, 5);
            printlines1(getAccurateText("CUSTOMER", 40, 1) + getAccurateText("SALESMAN", 40, 1), 2, object, 1, args, 5);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);

            printlines1(getAccurateText(object.getString("invoicefooter"), 80, 1), 1, object, 1, args, 5);
            //printlines1(getAccurateText("For any complaints please contact supervisor on " + object.getString("supervisorno"), 80, 1), 1, object, 1, args, 5);
            // printlines1(getAccurateText("For any complaints please contact " + object.getString("supervisorname") + "on" + object.getString("supervisorno"), 40, 0), false, 1);

            //jSONObject = object;
            if(context instanceof PromotioninfoActivity){
                ((PromotioninfoActivity)context).callback(App.DELIVERY);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //Invoice Receipts
    void printInvoiceReceipt(JSONObject object,String args){
        StringBuffer s1 = new StringBuffer();
        try{
            int printoultlet;
            JSONArray jCheques;
            JSONObject jCash;
            JSONObject jChequeDetails;
            String copyStatus;
            JSONObject jSONObject;
            int i;
            StringBuilder stringBuilder;
            /*if (object.getString("printoutletitemcode").length() > 0) {
                printoultlet = Integer.parseInt(object.getString("printoutletitemcode"));
            } else {
                printoultlet = 0;
            }*/
            if (object.getString("displayupc").equals("1")) {

                this.hashValues = new HashMap();
                this.hashValues.put("ITEM NO", Integer.valueOf(10));
                this.hashValues.put("ENGLISH DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("ARABIC DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("UPC ", Integer.valueOf(7));
                this.hashValues.put("TOTAL UNITS", Integer.valueOf(12));
                this.hashValues.put("UNIT PRICE", Integer.valueOf(12));
                this.hashValues.put("AMOUNT", Integer.valueOf(12));
                this.hashValues.put("REASON CODE", Integer.valueOf(11));
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
                this.hashPositions.put("REASON CODE", Integer.valueOf(2));


            } else {
                this.hashValues = new HashMap();
                this.hashValues.put("ITEM NO", Integer.valueOf(10));
                this.hashValues.put("ENGLISH DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("ARABIC DESCRIPTION", Integer.valueOf(40));
                this.hashValues.put("UPC ", Integer.valueOf(7));
                this.hashValues.put("TOTAL UNITS", Integer.valueOf(12));
                this.hashValues.put("UNIT PRICE", Integer.valueOf(12));
                this.hashValues.put("AMOUNT", Integer.valueOf(12));
                this.hashValues.put("REASON CODE", Integer.valueOf(11));
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
                this.hashPositions.put("REASON CODE", Integer.valueOf(2));
            }
            line(this.startln);
            headerprint(object, 10);
            this.outStream.write(this.BoldOn);
            this.outStream.write(this.DoubleWideOn);
            //printheaders(object.getString("invheadermsg"), false, 3);
            printheaders(getAccurateText("SALES", 40, 1), false, 2);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.DoubleWideOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);

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

            this.outStream.write(this.BoldOn);
            this.outStream.write(this.DoubleWideOn);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            //printheaders(object.getString("invheadermsg"), false, 3);
            //Gagdadfdsafsd
            this.outStream.write(this.CompressOn);
            printlines1(printSepratorcomp(), 1, object, 1, args, 1);
            printlines1(strTotal, 1, object, 1, args, 1);
            this.outStream.write(this.CompressOff);
            printlines1(getAccurateText("", 80, 1), 2, object, 1, args, 1);
            this.outStream.write(this.BoldOn);
            jSONObject = object;
            if (object.getString("invoicepriceprint").equals("1")) {
                jSONObject = object;
                printlines2(getAccurateText("SUB TOTAL", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("SUB TOTAL") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.SubTotal, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.BoldOn);
                if (object.has("INVOICE DISCOUNT")) {
                    if (object.getString("INVOICE DISCOUNT").toString().length() > 0) {
                        if (Double.parseDouble(object.getString("INVOICE DISCOUNT")) > 0.0d) {
                            jSONObject = object;
                            printlines2(getAccurateText("INVOICE DISCOUNT", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("INVOICE DISCOUNT") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.InvoiceDiscount, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                        }
                    }
                }
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.BoldOn);
                jSONObject = object;
                printlines2(getAccurateText("NET SALES", 20, 0) + getAccurateText(" : ", 3, 0) + getAccurateText(object.getString("NET SALES") + " SAR", 12, 0) + getAccurateText(" : ", 3, 0) + "@" + getAccurateText(ArabicTEXT.NetSales, 15, 2) + "!", 1, jSONObject, 1, args, 1, 1);
                this.outStream.write(this.BoldOff);
            }
            //printlines1(getAccurateText("Net Value : ", 50, 2) + getAccurateText(object.getString("netvalue"), 12, 2), 3, jSONObject, 1, args, 5);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);

            printlines1(getAccurateText("_____________", 40, 1) + getAccurateText("____________", 40, 1), 2, object, 1, args, 5);
            printlines1(getAccurateText("CUSTOMER", 40, 1) + getAccurateText("SALESMAN", 40, 1), 2, object, 1, args, 5);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);

            printlines1(getAccurateText("For any complaints please contact supervisor on " + object.getString("supervisorno"), 80, 1), 1, object, 1, args, 5);
            // printlines1(getAccurateText("For any complaints please contact " + object.getString("supervisorname") + "on" + object.getString("supervisorno"), 40, 0), false, 1);

            //jSONObject = object;
            if(context instanceof PromotioninfoActivity){
                ((PromotioninfoActivity)context).callback(App.DELIVERY);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void parseCollectionResponse(JSONObject object,String args){
        StringBuffer s1 = new StringBuffer();
        try {
            int i;
            int j;
            JSONArray jCheques;
            JSONObject jCash;
            JSONObject jSONObject;
            String exPayment;
            this.hashValues = new HashMap();
            this.hashValues.put("Invoice#", Integer.valueOf(18));
            this.hashValues.put("Due Date", Integer.valueOf(15));
            this.hashValues.put("Due Amount", Integer.valueOf(15));
            this.hashValues.put("Invoice Balance", Integer.valueOf(15));
            this.hashValues.put("Amount Paid", Integer.valueOf(15));
            this.hashPositions = new HashMap();
            this.hashPositions.put("Invoice#", Integer.valueOf(0));
            this.hashPositions.put("Due Date", Integer.valueOf(0));
            this.hashPositions.put("Due Amount", Integer.valueOf(2));
            this.hashPositions.put("Invoice Balance", Integer.valueOf(2));
            this.hashPositions.put("Amount Paid", Integer.valueOf(2));
            this.hashArbPositions = new HashMap();
            this.hashArbPositions.put("Invoice#", Integer.valueOf(2));
            this.hashArbPositions.put("Due Date", Integer.valueOf(2));
            this.hashArbPositions.put("Due Amount", Integer.valueOf(0));
            this.hashArbPositions.put("Invoice Balance", Integer.valueOf(0));
            this.hashArbPositions.put("Amount Paid", Integer.valueOf(0));
            this.hashArabVales = new HashMap();
            this.hashArabVales.put("Invoice#", ArabicTEXT.Invoice);
            this.hashArabVales.put("Due Date", ArabicTEXT.InvoiceDate);
            this.hashArabVales.put("Due Amount", ArabicTEXT.InvoiceAmount);
            this.hashArabVales.put("Invoice Balance", ArabicTEXT.InvoiceBalance);
            this.hashArabVales.put("Amount Paid", ArabicTEXT.AmountPaid);
            this.outStream.write(this.wakeUp);
            line(this.startln);
            headerprint(object, 2);
            JSONArray headers = object.getJSONArray("HEADERS");
            String strheader = "";
            String strTotal = "";
            String strHeaderBottom = "";
            int MAXLEngth = 80;
            for (i = 0; i < headers.length(); i++) {
                MAXLEngth -= ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue();
            }
            if (MAXLEngth > 0) {
                MAXLEngth /= headers.length();
            }
            JSONObject jTOBject = object.getJSONObject("TOTAL");
            for (i = 0; i < headers.length(); i++) {
                StringBuilder stringBuilder;
                String str;
                String HeaderVal = "";
                if (object.getString("LANG").equals("en")) {
                    HeaderVal = headers.getString(i);
                    stringBuilder = new StringBuilder(String.valueOf(strheader));
                    str = HeaderVal;
                    /*if (HeaderVal.indexOf(" ") == -1) {
                        str = HeaderVal;
                    } else {
                        str = HeaderVal.substring(0, HeaderVal.indexOf(" "));
                    }*/
                    strheader = stringBuilder.append(getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    //stringBuilder = new StringBuilder(String.valueOf(strHeaderBottom));
                    if (HeaderVal.indexOf(" ") == -1) {
                        str = "";
                    } else {
                        str = HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length());
                    }
                    //strHeaderBottom = stringBuilder.append(getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                } else {
                    HeaderVal = (String) this.hashArabVales.get(headers.getString(i));
                    if (HeaderVal.indexOf(" ") == -1) {
                        str = HeaderVal;
                    } else {
                        str = HeaderVal.substring(0, HeaderVal.indexOf(" "));
                    }
                    strheader = getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashArbPositions.get(headers.getString(i).toString())).intValue()) + strheader;
                    if (HeaderVal.indexOf(" ") == -1) {
                        str = "";
                    } else {
                        str = HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length());
                    }
                    strHeaderBottom = getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashArbPositions.get(headers.getString(i).toString())).intValue()) + strHeaderBottom;
                }
                if (jTOBject.has(headers.getString(i))) {
                    strTotal = new StringBuilder(String.valueOf(strTotal)).append(getAccurateText(jTOBject.getString(headers.getString(i).toString()), ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                } else {
                    stringBuilder = new StringBuilder(String.valueOf(strTotal));
                    if (headers.getString(i).equals("Due Date")) {
                        str = "TOTAL";
                    } else {
                        str = "";
                    }
                    strTotal = stringBuilder.append(getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i))).intValue() + MAXLEngth, 1)).toString();
                }
            }
            Log.e("Header", strheader);
            Log.e("Bottom", strHeaderBottom);
            if (object.getString("LANG").equals("en")) {
                printlines2(strheader, 1, object, 1, args, 2, 2);
                //printlines2(strHeaderBottom, 1, object, 1, args, 2, 2);
            } else {
                printlines2("@" + strHeaderBottom + "!", 1, object, 1, args, 2, 2);
                printlines2("@" + strheader + "!", 1, object, 1, args, 2, 2);
            }
            printlines2(printSeprator(), 1, object, 1, args, 2, 2);
            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            for (i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String strData = "";
                for (j = 0; j < jArr.length(); j++) {
                    Log.e("JARRR","" + jArr.getString(j));
                    strData = new StringBuilder(String.valueOf(strData)).append(getAccurateText(jArr.getString(j), ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(j).toString())).intValue())).toString();
                }
                printlines2(strData, 1, object, 1, args, 2, 2);
            }
            printlines2(printSeprator(), 1, object, 1, args, 2, 2);
            printlines2(strTotal, 2, object, 1, args, 2, 2);
            if (object.getString("LANG").equals("en")) {
                printlines2(getAccurateText("PAYMENT DETAILS", 80, 1), 2, object, 1, args, 2, 2);
            } else {
                printlines2(getAccurateText("@" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args, 2, 2);
            }
            if (object.has("Cheque")) {
                jCheques = object.getJSONArray("Cheque");
            } else {
                jCheques = null;
            }
            if (object.has("Cash")) {
                jCash = object.getJSONObject("Cash");
            } else {
                jCash = null;
            }
            JSONObject jChequeDetails;
            switch (Integer.parseInt(object.getString("PaymentType"))) {
                case 0 /*0*/:
                    this.outStream.write(this.BoldOn);
                    if (object.getString("LANG").equals("en")) {
                        jSONObject = object;
                        printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 1, jSONObject, 1, args, 2, 2);
                    } else {
                        jSONObject = object;
                        printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1), 1, jSONObject, 1, args, 2, 2);
                    }
                    this.outStream.write(this.BoldOff);
                    break;
                case 1 /*1*/:
                    this.outStream.write(this.BoldOn);
                    printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args, 2, 2);
                    this.outStream.write(this.BoldOff);
                    printlines2(getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0) + getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2), 1, object, 1, args, 2, 2);
                    printlines2(printSeprator(), 1, object, 1, args, 2, 2);
                    for (j = 0; j < jCheques.length(); j++) {
                        jChequeDetails = jCheques.getJSONObject(j);
                        jSONObject = object;
                        printlines2(getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0) + getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0) + getAccurateText(jChequeDetails.getString("Bank"), 20, 0) + getAccurateText(jChequeDetails.getString("Amount"), 20, 2), 1, jSONObject, 1, args, 2, 2);
                    }
                    printlines2(printSeprator(), 1, object, 1, args, 2, 2);
                    break;
                case 2 /*2*/:
                    this.outStream.write(this.BoldOn);
                    jSONObject = object;
                    printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, jSONObject, 1, args, 2, 2);
                    this.outStream.write(this.BoldOff);
                    this.outStream.write(this.BoldOn);
                    //printlines2(getAccurateText("CHEQUE", 80, 1), 1, jSONObject, 2, args, 2, 2);
                    printlines2(getAccurateText("CHEQUE", 80, 1), 2, jSONObject, 1, args, 2, 2);
                    this.outStream.write(this.BoldOff);
                    printlines2(getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0) + getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2), 1, jSONObject, 1, args, 2, 2);
                    printlines2(printSeprator(), 1, object, 1, args, 2, 2);
                    for (j = 0; j < jCheques.length(); j++) {
                        jChequeDetails = jCheques.getJSONObject(j);
                        jSONObject = object;
                        printlines2(getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0) + getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0) + getAccurateText(jChequeDetails.getString("Bank"), 20, 0) + getAccurateText(jChequeDetails.getString("Amount"), 20, 2), 1, jSONObject, 1, args, 2, 2);
                    }
                    printlines2(printSeprator(), 1, object, 1, args, 2, 2);
                    break;
            }
            if (object.has("expayment")) {
                exPayment = object.getString("expayment");
            } else {
                exPayment = "";
            }
            if (exPayment != null && exPayment.toString().trim().length() > 0) {
                printlines2(getAccurateText("Excess Payment : " + exPayment, 80, 0), 1, object, 1, args, 2, 2);
            }
            /*if (object.getString("comments").toString().length() > 0) {
                jSONObject = object;
                printlines2(getAccurateText("Comments: " + object.getString("comments"), 80, 0), 3, jSONObject, 1, args, 2, 2);
            } else {
                printlines2(" ", 2, object, 1, args, 2, 2);
            }*/
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            printlines2(getAccurateText("CUSTOMER_________________@" + ArabicTEXT.Customer + "!             SALESMAN_______________@" + ArabicTEXT.Salesman + "!", 80, 1), 2, object, 1, args, 2, 2);
            String copyStatus = "";
            if(context instanceof PaymentDetails){
                ((PaymentDetails)context).callback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void parseDriverCollectionResponse(JSONObject object,String args){
        StringBuffer s1 = new StringBuffer();
        try {
            int i;
            int j;
            JSONArray jCheques;
            JSONObject jCash;
            JSONObject jSONObject;
            String exPayment;
            this.hashValues = new HashMap();
            this.hashValues.put("Invoice#", Integer.valueOf(18));
            this.hashValues.put("Due Date", Integer.valueOf(15));
            this.hashValues.put("Due Amount", Integer.valueOf(15));
            this.hashValues.put("Invoice Balance", Integer.valueOf(15));
            this.hashValues.put("Amount Paid", Integer.valueOf(15));
            this.hashPositions = new HashMap();
            this.hashPositions.put("Invoice#", Integer.valueOf(0));
            this.hashPositions.put("Due Date", Integer.valueOf(0));
            this.hashPositions.put("Due Amount", Integer.valueOf(2));
            this.hashPositions.put("Invoice Balance", Integer.valueOf(2));
            this.hashPositions.put("Amount Paid", Integer.valueOf(2));
            this.hashArbPositions = new HashMap();
            this.hashArbPositions.put("Invoice#", Integer.valueOf(2));
            this.hashArbPositions.put("Due Date", Integer.valueOf(2));
            this.hashArbPositions.put("Due Amount", Integer.valueOf(0));
            this.hashArbPositions.put("Invoice Balance", Integer.valueOf(0));
            this.hashArbPositions.put("Amount Paid", Integer.valueOf(0));
            this.hashArabVales = new HashMap();
            this.hashArabVales.put("Invoice#", ArabicTEXT.Invoice);
            this.hashArabVales.put("Due Date", ArabicTEXT.InvoiceDate);
            this.hashArabVales.put("Due Amount", ArabicTEXT.InvoiceAmount);
            this.hashArabVales.put("Invoice Balance", ArabicTEXT.InvoiceBalance);
            this.hashArabVales.put("Amount Paid", ArabicTEXT.AmountPaid);
            line(this.startln);
            headerprint(object, 2);
            JSONArray headers = object.getJSONArray("HEADERS");
            String strheader = "";
            String strTotal = "";
            String strHeaderBottom = "";
            int MAXLEngth = 80;
            for (i = 0; i < headers.length(); i++) {
                MAXLEngth -= ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue();
            }
            if (MAXLEngth > 0) {
                MAXLEngth /= headers.length();
            }
            JSONObject jTOBject = object.getJSONObject("TOTAL");
            for (i = 0; i < headers.length(); i++) {
                StringBuilder stringBuilder;
                String str;
                String HeaderVal = "";
                if (object.getString("LANG").equals("en")) {
                    HeaderVal = headers.getString(i);
                    stringBuilder = new StringBuilder(String.valueOf(strheader));
                    str = HeaderVal;
                    /*if (HeaderVal.indexOf(" ") == -1) {
                        str = HeaderVal;
                    } else {
                        str = HeaderVal.substring(0, HeaderVal.indexOf(" "));
                    }*/
                    strheader = stringBuilder.append(getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    //stringBuilder = new StringBuilder(String.valueOf(strHeaderBottom));
                    if (HeaderVal.indexOf(" ") == -1) {
                        str = "";
                    } else {
                        str = HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length());
                    }
                    //strHeaderBottom = stringBuilder.append(getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                } else {
                    HeaderVal = (String) this.hashArabVales.get(headers.getString(i));
                    if (HeaderVal.indexOf(" ") == -1) {
                        str = HeaderVal;
                    } else {
                        str = HeaderVal.substring(0, HeaderVal.indexOf(" "));
                    }
                    strheader = getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashArbPositions.get(headers.getString(i).toString())).intValue()) + strheader;
                    if (HeaderVal.indexOf(" ") == -1) {
                        str = "";
                    } else {
                        str = HeaderVal.substring(HeaderVal.indexOf(" "), HeaderVal.length());
                    }
                    strHeaderBottom = getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashArbPositions.get(headers.getString(i).toString())).intValue()) + strHeaderBottom;
                }
                if (jTOBject.has(headers.getString(i))) {
                    strTotal = new StringBuilder(String.valueOf(strTotal)).append(getAccurateText(jTOBject.getString(headers.getString(i).toString()), ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                } else {
                    stringBuilder = new StringBuilder(String.valueOf(strTotal));
                    if (headers.getString(i).equals("Due Date")) {
                        str = "TOTAL";
                    } else {
                        str = "";
                    }
                    strTotal = stringBuilder.append(getAccurateText(str, ((Integer) this.hashValues.get(headers.getString(i))).intValue() + MAXLEngth, 1)).toString();
                }
            }
            Log.e("Header", strheader);
            Log.e("Bottom", strHeaderBottom);
            if (object.getString("LANG").equals("en")) {
                printlines2(strheader, 1, object, 1, args, 2, 2);
                //printlines2(strHeaderBottom, 1, object, 1, args, 2, 2);
            } else {
                printlines2("@" + strHeaderBottom + "!", 1, object, 1, args, 2, 2);
                printlines2("@" + strheader + "!", 1, object, 1, args, 2, 2);
            }
            printlines2(printSeprator(), 1, object, 1, args, 2, 2);
            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            for (i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String strData = "";
                for (j = 0; j < jArr.length(); j++) {
                    Log.e("JARRR","" + jArr.getString(j));
                    strData = new StringBuilder(String.valueOf(strData)).append(getAccurateText(jArr.getString(j), ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(j).toString())).intValue())).toString();
                }
                printlines2(strData, 1, object, 1, args, 2, 2);
            }
            printlines2(printSeprator(), 1, object, 1, args, 2, 2);
            printlines2(strTotal, 2, object, 1, args, 2, 2);
            if (object.getString("LANG").equals("en")) {
                printlines2(getAccurateText("PAYMENT DETAILS", 80, 1), 2, object, 1, args, 2, 2);
            } else {
                printlines2(getAccurateText("@" + ArabicTEXT.PaymentDetails + "!", 80, 1), 2, object, 1, args, 2, 2);
            }
            if (object.has("Cheque")) {
                jCheques = object.getJSONArray("Cheque");
            } else {
                jCheques = null;
            }
            if (object.has("Cash")) {
                jCash = object.getJSONObject("Cash");
            } else {
                jCash = null;
            }
            JSONObject jChequeDetails;
            switch (Integer.parseInt(object.getString("PaymentType"))) {
                case 0 /*0*/:
                    this.outStream.write(this.BoldOn);
                    if (object.getString("LANG").equals("en")) {
                        jSONObject = object;
                        printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 1, jSONObject, 1, args, 2, 2);
                    } else {
                        jSONObject = object;
                        printlines2(getAccurateText("*" + ArabicTEXT.Cash + "! :" + jCash.getString("Amount"), 80, 1), 1, jSONObject, 1, args, 2, 2);
                    }
                    this.outStream.write(this.BoldOff);
                    break;
                case 1 /*1*/:
                    this.outStream.write(this.BoldOn);
                    printlines2(getAccurateText("CHEQUE", 80, 1), 2, object, 1, args, 2, 2);
                    this.outStream.write(this.BoldOff);
                    printlines2(getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0) + getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2), 1, object, 1, args, 2, 2);
                    printlines2(printSeprator(), 1, object, 1, args, 2, 2);
                    for (j = 0; j < jCheques.length(); j++) {
                        jChequeDetails = jCheques.getJSONObject(j);
                        jSONObject = object;
                        printlines2(getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0) + getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0) + getAccurateText(jChequeDetails.getString("Bank"), 20, 0) + getAccurateText(jChequeDetails.getString("Amount"), 20, 2), 1, jSONObject, 1, args, 2, 2);
                    }
                    printlines2(printSeprator(), 1, object, 1, args, 2, 2);
                    break;
                case 2 /*2*/:
                    this.outStream.write(this.BoldOn);
                    jSONObject = object;
                    printlines2(getAccurateText("CASH:" + jCash.getString("Amount"), 80, 1), 2, jSONObject, 1, args, 2, 2);
                    this.outStream.write(this.BoldOff);
                    this.outStream.write(this.BoldOn);
                    //printlines2(getAccurateText("CHEQUE", 80, 1), 1, jSONObject, 2, args, 2, 2);
                    printlines2(getAccurateText("CHEQUE", 80, 1), 2, jSONObject, 1, args, 2, 2);
                    this.outStream.write(this.BoldOff);
                    printlines2(getAccurateText("Cheque Date:", 20, 0) + getAccurateText("Cheque No:", 20, 0) + getAccurateText("Bank:", 20, 0) + getAccurateText("Amount:", 20, 2), 1, jSONObject, 1, args, 2, 2);
                    printlines2(printSeprator(), 1, object, 1, args, 2, 2);
                    for (j = 0; j < jCheques.length(); j++) {
                        jChequeDetails = jCheques.getJSONObject(j);
                        jSONObject = object;
                        printlines2(getAccurateText(jChequeDetails.getString("Cheque Date"), 20, 0) + getAccurateText(jChequeDetails.getString("Cheque No"), 20, 0) + getAccurateText(jChequeDetails.getString("Bank"), 20, 0) + getAccurateText(jChequeDetails.getString("Amount"), 20, 2), 1, jSONObject, 1, args, 2, 2);
                    }
                    printlines2(printSeprator(), 1, object, 1, args, 2, 2);
                    break;
            }
            if (object.has("expayment")) {
                exPayment = object.getString("expayment");
            } else {
                exPayment = "";
            }
            if (exPayment != null && exPayment.toString().trim().length() > 0) {
                printlines2(getAccurateText("Excess Payment : " + exPayment, 80, 0), 1, object, 1, args, 2, 2);
            }
            /*if (object.getString("comments").toString().length() > 0) {
                jSONObject = object;
                printlines2(getAccurateText("Comments: " + object.getString("comments"), 80, 0), 3, jSONObject, 1, args, 2, 2);
            } else {
                printlines2(" ", 2, object, 1, args, 2, 2);
            }*/
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            printlines2(getAccurateText("CUSTOMER_________________@" + ArabicTEXT.Customer + "!             SALESMAN_______________@" + ArabicTEXT.Salesman + "!", 80, 1), 2, object, 1, args, 2, 2);
            String copyStatus = "";
            if(context instanceof DriverPaymentDetails){
                ((DriverPaymentDetails)context).callback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Printing Headers
    void parseEndInventory(JSONObject object, String args) {
        StringBuffer s1 = new StringBuffer();
        try {
            int i;
            StringBuilder stringBuilder;
            String string;
            this.hashValues = new HashMap();
            this.hashValues.put("Sl#", Integer.valueOf(0));
            this.hashValues.put("ITEMNO", Integer.valueOf(10));
            this.hashValues.put("DESCRIPTION", Integer.valueOf(36));
            this.hashValues.put("INVENTORY CALCULATED",Integer.valueOf(10));  //Fresh unload
            this.hashValues.put("RETURN TO STOCK",Integer.valueOf(10));  //Summation of all
            this.hashValues.put("TRUCK SPOILS",Integer.valueOf(10));  //Truck Damage
            this.hashValues.put("ACTUAL ON TRUCK",Integer.valueOf(10));  //Truck Damage
            this.hashValues.put("BAD RTRNS",Integer.valueOf(12));  //Bad Returns
            this.hashValues.put("----VARIANCE---- QTY        AMNT",Integer.valueOf(16));
            this.hashValues.put("ENDING INV.VALUE",Integer.valueOf(10));
            this.hashValues.put("TOTAL VALUE", Integer.valueOf(10));
            //this.hashValues.put("DESCRIPTION(AR)", Integer.valueOf(40));
            //this.hashValues.put("UPO", Integer.valueOf(0));
            this.hashValues.put("Truck Stock", Integer.valueOf(8));
            this.hashValues.put("Fresh Unload", Integer.valueOf(9));
            this.hashValues.put("Truck Damage", Integer.valueOf(9));
            this.hashValues.put("Theft", Integer.valueOf(5));
            this.hashValues.put("Closing Stock", Integer.valueOf(9));
            this.hashValues.put("Variance Qty", Integer.valueOf(9));

            this.hashValues.put("Description", Integer.valueOf(42));
            this.hashValues.put("TOTAL TRUCK STOCK", Integer.valueOf(8));
            this.hashValues.put("TOTAL FRESH UNLOAD", Integer.valueOf(9));
            this.hashValues.put("TOTAL TRUCK DAMAGE", Integer.valueOf(9));
            this.hashValues.put("TOTAL CLOSING STOCK", Integer.valueOf(9));
            this.hashValues.put("TOTAL VARIANCE QTY", Integer.valueOf(9));
            this.hashPositions = new HashMap();
            this.hashPositions.put("Sl#", Integer.valueOf(0));
            this.hashPositions.put("ITEMNO", Integer.valueOf(0));
            this.hashPositions.put("DESCRIPTION", Integer.valueOf(0));
            this.hashPositions.put("INVENTORY CALCULATED",Integer.valueOf(2));  //Fresh unload
            this.hashPositions.put("RETURN TO STOCK",Integer.valueOf(2));  //Summation of all
            this.hashPositions.put("TRUCK SPOILS",Integer.valueOf(2));  //Truck Damage
            this.hashPositions.put("ACTUAL ON TRUCK",Integer.valueOf(2));  //Truck Damage
            this.hashPositions.put("BAD RTRNS",Integer.valueOf(2));  //Bad Returns
            this.hashPositions.put("----VARIANCE---- QTY        AMNT",Integer.valueOf(2));
            this.hashPositions.put("ENDING INV.VALUE",Integer.valueOf(2));
            this.hashPositions.put("TOTAL VALUE", Integer.valueOf(2));
            //this.hashPositions.put("DESCRIPTION(AR)", Integer.valueOf(2));
            //this.hashPositions.put("DESCRIPTION(AR)", Integer.valueOf(0));
            this.hashPositions.put("UPO", Integer.valueOf(2));
            this.hashPositions.put("Truck Stock", Integer.valueOf(2));
            this.hashPositions.put("Fresh Unload", Integer.valueOf(2));
            this.hashPositions.put("Truck Damage", Integer.valueOf(2));
            this.hashPositions.put("Theft", Integer.valueOf(2));
            this.hashPositions.put("Closing Stock", Integer.valueOf(2));
            this.hashPositions.put("Variance Qty", Integer.valueOf(2));

            this.hashPositions.put("Description", Integer.valueOf(0));
            this.hashPositions.put("TOTAL TRUCK STOCK", Integer.valueOf(0));
            this.hashPositions.put("TOTAL FRESH UNLOAD", Integer.valueOf(0));
            this.hashPositions.put("TOTAL TRUCK DAMAGE", Integer.valueOf(0));
            this.hashPositions.put("TOTAL CLOSING STOCK", Integer.valueOf(0));
            this.hashPositions.put("TOTAL VARIANCE QTY", Integer.valueOf(0));
            line(this.startln);
            headerinvprint(object, 3);
            JSONArray headers = object.getJSONArray("HEADERS");
            String strheader = "";
            String strHeaderBottom = "";
            //int MAXLEngth = 80;
            int MAXLEngth = 137;
            for (i = 0; i < headers.length(); i++) {
                MAXLEngth -= ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue();
            }
            if (MAXLEngth > 0) {
                MAXLEngth /= headers.length();
            }
            String strTotal = "";
            JSONObject jTOBject = object.getJSONArray("TOTAL").getJSONObject(0);
            for (i = 0; i < headers.length(); i++) {
                try {
                    String string2;
                    stringBuilder = new StringBuilder(String.valueOf(strheader));
                    //string2 = headers.getString(i);
                    if (headers.getString(i).indexOf(" ") == -1) {
                        string2 = headers.getString(i);
                    } else {
                        string2 = headers.getString(i).substring(0, headers.getString(i).indexOf(" "));
                    }
                    strheader = stringBuilder.append(getAccurateText(string2, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    stringBuilder = new StringBuilder(String.valueOf(strHeaderBottom));
                    if (headers.getString(i).indexOf(" ") == -1) {
                        string2 = "";
                    } else {
                        string2 = headers.getString(i).substring(headers.getString(i).indexOf(" "), headers.getString(i).length());
                    }
                    strHeaderBottom = stringBuilder.append(getAccurateText(string2, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    if (jTOBject.has(headers.getString(i))) {
                        strTotal = new StringBuilder(String.valueOf(strTotal)).append(getAccurateText(jTOBject.getString(headers.getString(i).toString()), ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    } else {
                        stringBuilder = new StringBuilder(String.valueOf(strTotal));
                        if (headers.getString(i).equals("DESCRIPTION")) {
                            string2 = "TOTAL";
                        } else {
                            string2 = "";
                        }
                        strTotal = stringBuilder.append(getAccurateText(string2, ((Integer) this.hashValues.get(headers.getString(i))).intValue() + MAXLEngth, 1)).toString();
                    }
                } catch (Exception e) {
                }
            }
            this.outStream.write(this.CompressOn);
            printlines1(strheader, 1, object, 1, args, 3);
            printlines1(strHeaderBottom, 1, object, 1, args, 3);
            printlines1(printSepratorcomp(), 1, object, 1, args, 3);
            this.outStream.write(this.CompressOff);
            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            for (i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String strData = "";
                for (int j = 0; j < jArr.length(); j++) {
                    int i2;
                    Object obj;
                    String itemDescrion = jArr.getString(j);
                    /*if (j == 2) {
                        itemDescrion = "       @" + jArr.getString(j) + "!";
                    }*/
                    stringBuilder = new StringBuilder(String.valueOf(strData));
                    if (j == 2) {
                        //i2 = 60;
                        i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                    } else {
                        i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                    }
                    HashMap hashMap = this.hashPositions;
                    if (j == 13) {
                        obj = "Description";
                    } else {
                        obj = headers.getString(j).toString();
                    }
                    strData = stringBuilder.append(getAccurateText(itemDescrion, i2, ((Integer) hashMap.get(obj)).intValue())).toString();
                }
                this.outStream.write(this.CompressOn);
                this.count++;
                printlines1(strData, 1, object, 1, args, 3);
                this.outStream.write(this.CompressOff);
            }
            this.outStream.write(this.CompressOn);
            printlines1(printSepratorcomp(), 1, object, 1, args, 3);
            printlines1(strTotal, 1, object, 1, args, 3);
            this.outStream.write(this.CompressOff);
            this.outStream.write(this.BoldOn);
            this.outStream.write(this.NewLine);
            JSONObject jSONObject = object;
            printlines1(getAccurateText("END INVENTORY VALUE : ", 40, 2) + getAccurateText(object.getString("closevalue"), 30, 1), 1, jSONObject, 1, args, 2);
            this.outStream.write(this.NewLine);
            jSONObject = object;
            //printlines1(getAccurateText("AVAILABLE INVENTORY : ", 40, 2) + getAccurateText(object.getString("availvalue"), 30, 1), 1, jSONObject, 1, args, 2);
            //jSONObject = object;
            //printlines1(getAccurateText("UNLOAD INVENTORY : ", 40, 2) + getAccurateText(object.getString("unloadvalue"), 30, 1), 1, jSONObject, 1, args, 2);
            //printlines1(printSeprator(), 1, object, 1, args, 3);
            //jSONObject = object;
            //printlines1(getAccurateText("CALCULATED INVENTORY : ", 40, 2) + getAccurateText(object.getString("closevalue"), 30, 1), 1, jSONObject, 1, args, 2);
            //printlines1(printSeprator(), 1, object, 1, args, 3);
            this.outStream.write(this.BoldOff);
            printlines1(getAccurateText("_____________", 40, 1) + getAccurateText("____________", 40, 1), 2, object, 1, args, 1);
            this.outStream.write(this.NewLine);
           // printlines1(" ", 1, object, 1, args, 3);
            printlines1(getAccurateText("STORE KEEPER", 40, 1) + getAccurateText("SALESMAN", 40, 1), 2, object, 1, args, 3);
            /*if (object.has("printstatus")) {
                string = object.getString("printstatus");
            } else {
                string = "";
            }*/
            //printlines1(getAccurateText(string, 80, 1), 2, object, 2, args[0], 3);
            if(context instanceof UnloadActivity){
                ((UnloadActivity)context).callbackFunction();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    void parseRouteSales(JSONObject object, String args) {

    }

    void parseDepositCashResponse(JSONObject object, String args) {
        StringBuffer s1 = new StringBuffer();
        try {
            this.hashValues = new HashMap();
            this.hashValues.put("Transaction Number", Integer.valueOf(20));
            this.hashValues.put("Customer Code", Integer.valueOf(15));
            this.hashValues.put("Customer Name", Integer.valueOf(25));
            this.hashValues.put("Cheque No", Integer.valueOf(12));
            this.hashValues.put("Cheque Date", Integer.valueOf(15));
            this.hashValues.put("Bank Name", Integer.valueOf(15));
            this.hashValues.put("Cheque Amount", Integer.valueOf(15));
            this.hashValues.put("Cash Amount", Integer.valueOf(10));
            this.hashPositions = new HashMap();
            this.hashPositions.put("Transaction Number", Integer.valueOf(0));
            this.hashPositions.put("Customer Code", Integer.valueOf(0));
            this.hashPositions.put("Customer Name", Integer.valueOf(0));
            this.hashPositions.put("Cheque No", Integer.valueOf(0));
            this.hashPositions.put("Cheque Date", Integer.valueOf(0));
            this.hashPositions.put("Bank Name", Integer.valueOf(0));
            this.hashPositions.put("Cheque Amount", Integer.valueOf(2));
            this.hashPositions.put("Cash Amount", Integer.valueOf(2));
            line(this.startln);
            headerprint(object, 3);
            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            for (int i = 0; i < jData.length(); i++) {
                JSONObject mainJson = jData.getJSONObject(i);
                JSONArray jInnerData = mainJson.getJSONArray("DATA");
                JSONArray headers = mainJson.getJSONArray("HEADERS");
                JSONObject jTotal = mainJson.getJSONObject("TOTAL");
                /*if (jInnerData.length() > 0) {
                    switch (i) {
                        case 0 *//*0*//*:
                            this.outStream.write(this.BoldOn);
                            this.outStream.write("       ".getBytes());
                            //this.outStream.write(this.UnderlineOn);
                           // printlines2("CASH", 1, object, 1, args, 3, 3);
                           // this.outStream.write(this.UnderlineOff);
                            this.outStream.write(this.BoldOff);
                            break;
                        case 1 *//*1*//*:
                            this.outStream.write(this.BoldOn);
                            this.outStream.write("       ".getBytes());
                            this.outStream.write(this.UnderlineOn);
                            printlines2("CHEQUE", 1, object, 1, args, 3, 3);
                            this.outStream.write(this.UnderlineOff);
                            this.outStream.write(this.BoldOff);
                            break;
                    }
                }*/
                int MAXLEngth = 137;
                for (int k = 0; k < headers.length(); k++) {
                    MAXLEngth -= ((Integer) this.hashValues.get(headers.getString(k).toString())).intValue();
                }
                if (MAXLEngth > 0) {
                    MAXLEngth /= headers.length();
                }
                String strheader = "";
                String strHeaderBottom = "";
                String strTotal = "";
                for (int j = 0; j < headers.length(); j++) {
                    String string;
                    StringBuilder stringBuilder = new StringBuilder(String.valueOf(strheader));
                    string = headers.getString(j);
                    /*if (headers.getString(j).indexOf(" ") == -1) {
                        string = headers.getString(j);
                    } else {
                        string = headers.getString(j).substring(0, headers.getString(j).indexOf(" ")).trim();
                    }*/
                    strheader = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(j).toString())).intValue())).toString();
                    stringBuilder = new StringBuilder(String.valueOf(strHeaderBottom));
                    if (headers.getString(j).indexOf(" ") == -1) {
                        string = "";
                    } else {
                        string = headers.getString(j).substring(headers.getString(j).indexOf(" "), headers.getString(j).length()).trim();
                    }
                    //strHeaderBottom = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(j).toString())).intValue())).toString();
                    if (jTotal.has(headers.getString(j))) {
                        strTotal = new StringBuilder(String.valueOf(strTotal)).append(getAccurateText(jTotal.getString(headers.getString(j).toString()), ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(j).toString())).intValue())).toString();
                    } else {
                        Object obj;
                        stringBuilder = new StringBuilder(String.valueOf(strTotal));
                        //string = headers.getString(j);
                        if (i == 0) {
                            obj = "Cheque Amount";
                        } else {
                            obj = "Amount";
                        }
                        if (string.equals(obj)) {
                            string = "Total";
                        } else {
                            string = "";
                        }
                        strTotal = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(j))).intValue() + MAXLEngth, 1)).toString();
                    }
                }
                if (jInnerData.length() > 0) {
                    this.outStream.write(CompressOn);
                    printlines1(strheader, 1, object, 1, args, 1);
                    //printlines1(strHeaderBottom, 1, object, 1, args, 1);
                    printlines1(printSepratorcomp(), 1, object, 1, args,1);
                    this.outStream.write(CompressOff);
                }
                for (int l = 0; l < jInnerData.length(); l++) {
                    JSONArray jArr = jInnerData.getJSONArray(l);
                    String strData = "";
                    for (int m = 0; m < jArr.length(); m++) {
                        strData = new StringBuilder(String.valueOf(strData)).append(getAccurateText(jArr.getString(m), ((Integer) this.hashValues.get(headers.getString(m).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(m).toString())).intValue())).toString();
                    }
                    this.outStream.write(CompressOn);
                    printlines2(strData, 1, object, 1, args, 3, 3);
                    this.outStream.write(CompressOff);
                }
                if (jInnerData.length() > 0) {
                    this.outStream.write(CompressOn);
                    printlines2(printSepratorcomp(), 1, object, 1, args, 3, 3);
                    printlines2(strTotal, 2, object, 1, args, 3, 3);
                    this.outStream.write(CompressOff);
                }
            }
            this.outStream.write(this.BoldOn);
            String totalAmt = object.getString("TOTAL DEPOSIT AMOUNT");
            String totalCashAmt = object.getString("TOTAL CASH AMOUNT");
            String totalChequeAmt = object.getString("TOTAL CHEQUE AMOUNT");
            //String varAmt = object.getString("totalvaramount");
            printlines1(getAccurateText("TOTAL CASH AMOUNT : ", 40, 2) + getAccurateText(totalCashAmt, 16, 1), 1, object, 1, args, 2);
            //this.outStream.write(this.NewLine);
            printlines1(getAccurateText("TOTAL CHEQUE AMOUNT : ", 40, 2) + getAccurateText(totalChequeAmt, 16, 1), 1, object, 1, args, 2);
            //this.outStream.write(this.NewLine);
            printlines1(getAccurateText("TOTAL DEPOSIT AMOUNT : ", 40, 2) + getAccurateText(totalAmt, 16, 1), 1, object, 1, args, 2);
           // printlines2(getAccurateText("TOTAL DEPOSIT AMOUNT", 67, 2) + getAccurateText(totalAmt, 16, 1), 1, object, 1, args, 3, 3);
            //printlines2(getAccurateText("TOTAL VARIENCE AMOUNT", 67, 2) + getAccurateText(varAmt, 16, 1), 1, object, 1, args[0], 3, 3);
            /*if (totalAmt.length() > 0 && varAmt.length() > 0) {
                float totalCount = Float.parseFloat(totalAmt) + Float.parseFloat(varAmt);
                String str = totalAmt;
                int decimal_count = str.substring(totalAmt.indexOf(".") + 1, totalAmt.length()).length();
                JSONObject jSONObject = object;
                printlines2(getAccurateText("NET DUE AMOUNT", 67, 2) + getAccurateText(String.format("%." + decimal_count + "f", new Object[]{Float.valueOf(totalCount)}), 16, 1), 1, jSONObject, 1, args, 3, 3);
            }*/
            this.outStream.write(this.BoldOff);
            printlines2(" ", 2, object, 1, args, 3, 3);
            printlines1(getAccurateText("_____________", 26, 1) + getAccurateText("____________", 27, 1) + getAccurateText("____________", 27, 1), 2, object, 1, args, 1);
            this.outStream.write(this.NewLine);
            printlines1(getAccurateText("SALES REP", 26, 1) + getAccurateText("DUTY SUPERVISOR", 26, 1) + getAccurateText("ACCOUNTANT", 26, 1), 2, object, 1, args, 1);
           // printlines2(getAccurateText("______________", 26, 0) + getAccurateText("______________", 26, 0) + getAccurateText("______________", 26, 0), 1, object, 2, args, 3, 3);
            //this.outStream.write(this.NewLine);
           // printlines2(getAccurateText("SALES REP", 26, 0) + getAccurateText("SUPERVISOR", 26, 0) + getAccurateText("ACCOUNTANT", 26, 0), 1, object, 2, args, 3, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void printSalesSummaryReport(JSONObject object, String args) {
        StringBuffer s1 = new StringBuffer();
        int i;
        JSONObject jSONObject;
        StringBuilder stringBuilder;
        try {
            this.hashValues = new HashMap();
            this.hashValues.put("Transaction No.", Integer.valueOf(15));
            this.hashValues.put("Cust. No.", Integer.valueOf(15));
            this.hashValues.put("Customer", Integer.valueOf(25));
            this.hashValues.put("Type", Integer.valueOf(5));
            this.hashValues.put("Sales", Integer.valueOf(8));
            this.hashValues.put("Returns", Integer.valueOf(10));
            this.hashValues.put("Good Rtns", Integer.valueOf(8));
            this.hashValues.put("Net Sales", Integer.valueOf(8));
            this.hashValues.put("Discounts", Integer.valueOf(8));
            this.hashValues.put("Amount Paid", Integer.valueOf(12));
            this.hashValues.put("T.C", Integer.valueOf(8));
            this.hashPositions = new HashMap();
            this.hashPositions.put("Transaction No.", Integer.valueOf(0));
            this.hashPositions.put("Cust. No.", Integer.valueOf(0));
            this.hashPositions.put("Customer", Integer.valueOf(0));
            this.hashPositions.put("Type", Integer.valueOf(0));
            this.hashPositions.put("Sales", Integer.valueOf(2));
            this.hashPositions.put("Returns", Integer.valueOf(2));
            this.hashPositions.put("Good Rtns", Integer.valueOf(2));
            this.hashPositions.put("Net Sales", Integer.valueOf(2));
            this.hashPositions.put("Discounts", Integer.valueOf(2));
            this.hashPositions.put("Amount Paid", Integer.valueOf(2));
            this.hashPositions.put("T.C", Integer.valueOf(0));
            line(this.startln);
            headerprint(object, 4);
            this.outStream.write(this.BoldOn);
            this.outStream.write("       ".getBytes());
            this.outStream.write(this.UnderlineOn);
            printlines2("CASH INVOICE", 1, object, 1, args, 4, 4);
            this.outStream.write(this.UnderlineOff);
            this.outStream.write(this.BoldOff);

            this.outStream.write(this.NewLine);

            JSONArray headers = object.getJSONArray("HEADERS");
            String strheader = "";
            String strHeaderBottom = "";
            String strTotal = "";

            JSONArray jTotal = object.getJSONArray("TOTAL");
            JSONObject jTOBject = jTotal.getJSONObject(0);
            int MAXLEngth = 137;
            for (i = 0; i < headers.length(); i++) {
                MAXLEngth -= ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue();
            }
            if (MAXLEngth > 0) {
                MAXLEngth /= headers.length();
            }

            for (i = 0; i < headers.length(); i++) {
                try {
                    String string;
                    stringBuilder = new StringBuilder(String.valueOf(strheader));
                    string = headers.getString(i);
                    strheader = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    stringBuilder = new StringBuilder(String.valueOf(strHeaderBottom));
                    if (headers.getString(i).indexOf(" ") == -1) {
                        string = "";
                    } else {
                        string = headers.getString(i).substring(headers.getString(i).indexOf(" "), headers.getString(i).length());
                    }
                    //strHeaderBottom = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    /*if (jTOBject.has(headers.getString(i))) {
                        strTotal = new StringBuilder(String.valueOf(strTotal)).append(getAccurateText(jTOBject.getString(headers.getString(i).toString()), ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    } else {
                        stringBuilder = new StringBuilder(String.valueOf(strTotal));
                        if (headers.getString(i).equals("ARABIC DESCRIPTION")) {
                            string = "TOTAL";
                        } else {
                            string = "";
                        }
                        strTotal = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i))).intValue() + MAXLEngth, 1)).toString();
                    }*/
                } catch (Exception e) {
                }
            }



            this.outStream.write(this.CompressOn);
            printlines1(strheader, 1, object, 1, args, 1);
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
                    /*if (j == 0) {
                        //itemDescrion = new StringBuilder(String.valueOf(i + 1)).toString();
                    } else if (j == 2) {
                        itemDescrion = "           @" + jArr.getString(j) + "!";
                    }*/
                    stringBuilder = new StringBuilder(String.valueOf(strData));
                    if (j == 2) {
                        //i2 = 60;
                        i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                    } else {
                        i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                    }
                    HashMap hashMap = this.hashPositions;
                    if (j == 14) {
                        //obj = "Description";
                        obj = headers.getString(j).toString();
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

           // this.outStream.write(this.BoldOn);
           // this.outStream.write(this.DoubleWideOn);
            this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);

            JSONArray grData = object.getJSONArray("creditData");
            if(grData.length()>0){
                this.outStream.write(this.BoldOn);
                this.outStream.write("       ".getBytes());
                this.outStream.write(this.UnderlineOn);
                printlines2("CREDIT INVOICE", 1, object, 1, args, 4, 4);
                this.outStream.write(this.UnderlineOff);
                this.outStream.write(this.BoldOff);

                this.outStream.write(this.NewLine);

                this.outStream.write(this.CompressOn);
                printlines1(strheader, 1, object, 1, args, 1);
                printlines1(printSepratorcomp(), 1, object, 1, args, 1);
                this.outStream.write(this.CompressOff);

                for (i = 0; i < grData.length(); i++) {
                    JSONArray jArr = grData.getJSONArray(i);
                    String strData = "";
                    for (int j = 0; j < jArr.length(); j++) {
                        int i2;
                        Object obj;
                        String itemDescrion = jArr.getString(j);
                        /*if (j == 0) {
                            //itemDescrion = new StringBuilder(String.valueOf(i + 1)).toString();
                        } else if (j == 2) {
                            itemDescrion = "           @" + jArr.getString(j) + "!";
                        }*/
                        stringBuilder = new StringBuilder(String.valueOf(strData));
                        if (j == 2) {
                            //i2 = 60;
                            i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                        } else {
                            i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                        }
                        HashMap hashMap = this.hashPositions;
                        if (j == 7) {
                            //obj = "Description";
                            obj = headers.getString(j).toString();
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
            }

            this.outStream.write(this.NewLine);
            //this.outStream.write(this.NewLine);

            JSONArray brData = object.getJSONArray("tcData");
            if(brData.length()>0){
                this.outStream.write(this.BoldOn);
                this.outStream.write("       ".getBytes());
                this.outStream.write(this.UnderlineOn);
                printlines2("TC INVOICE", 1, object, 1, args, 4, 4);
                this.outStream.write(this.UnderlineOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.CompressOn);
                printlines1(strheader, 1, object, 1, args, 1);
                printlines1(printSepratorcomp(), 1, object, 1, args, 1);
                this.outStream.write(this.CompressOff);

                for (i = 0; i < brData.length(); i++) {
                    JSONArray jArr = brData.getJSONArray(i);
                    String strData = "";
                    for (int j = 0; j < jArr.length(); j++) {
                        int i2;
                        Object obj;
                        String itemDescrion = jArr.getString(j);
                        /*if (j == 0) {
                            //itemDescrion = new StringBuilder(String.valueOf(i + 1)).toString();
                        } else if (j == 2) {
                            itemDescrion = "           @" + jArr.getString(j) + "!";
                        }*/
                        stringBuilder = new StringBuilder(String.valueOf(strData));
                        if (j == 2) {
                            //i2 = 60;
                            i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                        } else {
                            i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                        }
                        HashMap hashMap = this.hashPositions;
                        if (j == 7) {
                            obj = headers.getString(j).toString();
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
            }

            printlines2(" ", 2, object, 1, args, 4, 4);
            //printlines2(getAccurateText("_______________ ", 80, 1), 2, object, 2, args, 4, 4);
            this.outStream.write(this.NewLine);
            // printlines2(getAccurateText("SALESMAN ", 80, 1), 2, object, 2, args, 4, 4);
            printlines1(getAccurateText("_____________", 40, 1) + getAccurateText("____________", 40, 1), 2, object, 1, args, 5);
            printlines1(getAccurateText("SUPERVISOR", 40, 1) + getAccurateText("SALESMAN", 40, 1), 2, object, 1, args, 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void printBadReturnsReport(JSONObject object, String args) {
        StringBuffer s1 = new StringBuffer();
        try {
            int i;
            StringBuilder stringBuilder;
            String string;
            int j;
            String string2;
            this.hashValues = new HashMap();
            this.hashValues.put("ITEM#", Integer.valueOf(15));
            this.hashValues.put("DESCRIPTION", Integer.valueOf(42));
            this.hashValues.put("INVOICE CREDIT", Integer.valueOf(15));
            this.hashValues.put("LOADED IN", Integer.valueOf(15));
            this.hashValues.put("PRICE",Integer.valueOf(15));
            this.hashValues.put("-----VARIANCE----- QTY         AMOUNT",Integer.valueOf(25));
            this.hashPositions = new HashMap();
            this.hashPositions.put("ITEM#", Integer.valueOf(0));
            this.hashPositions.put("DESCRIPTION", Integer.valueOf(0));
            this.hashPositions.put("INVOICE CREDIT", Integer.valueOf(2));
            this.hashPositions.put("LOADED IN", Integer.valueOf(2));
            this.hashPositions.put("PRICE", Integer.valueOf(2));
            this.hashPositions.put("-----VARIANCE----- QTY         AMOUNT", Integer.valueOf(2));
            line(this.startln);
            headerprint(object, 7);
            JSONArray headers = object.getJSONArray("HEADERS");
            String strheader = "";
            String strHeaderBottom = "";
            int MAXLEngth = 130;
            for (i = 0; i < headers.length(); i++) {
                MAXLEngth -= ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue();
            }
            if (MAXLEngth > 0) {
                MAXLEngth /= headers.length();
            }
            i = 0;
            while (i < headers.length()) {
                if (i != 3 || i != 4 || i != 5 || i != 7 || i != 8) {
                    stringBuilder = new StringBuilder(String.valueOf(strheader));
                    if (headers.getString(i).indexOf(" ") == -1) {
                        string = headers.getString(i);
                    } else {
                        string = headers.getString(i).substring(0, headers.getString(i).indexOf(" "));
                    }
                    strheader = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                    stringBuilder = new StringBuilder(String.valueOf(strHeaderBottom));
                    if (headers.getString(i).indexOf(" ") == -1) {
                        string = "";
                    } else {
                        string = headers.getString(i).substring(headers.getString(i).indexOf(" "), headers.getString(i).length());
                    }
                    strHeaderBottom = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(i).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(i).toString())).intValue())).toString();
                }
                i++;
            }
            this.outStream.write(this.CompressOn);
            //printlines2(printSepratorcomp(), 1, object, 1, args, 7, 7);
            printlines2(strheader, 1, object, 1, args, 7, 7);
            printlines2(strHeaderBottom, 1, object, 1, args, 7, 7);
            printlines2(printSepratorcomp(), 1, object, 1, args, 7, 7);
            this.outStream.write(this.CompressOff);
            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            i = 0;
            while (i < jData.length()) {
                JSONArray jArr = jData.getJSONArray(i);
                String strData = "";
                for (j = 0; j < jArr.length(); j++) {
                    if (i != 3 || i != 4 || i != 5 || i != 7 || i != 8) {
                        String itemDescrion = jArr.getString(j);
                        if (j == 1) {
                            if (object.getString("LANG").equals("en")) {
                                itemDescrion = jArr.getString(j);
                            } else {
                                itemDescrion = "@" + jArr.getString(j) + "!";
                            }
                        }
                        strData = new StringBuilder(String.valueOf(strData)).append(getAccurateText(itemDescrion, ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(j).toString())).intValue())).toString();
                    }
                }
                this.outStream.write(this.CompressOn);
                printlines2(strData, 1, object, 1, args, 7, 7);
                this.outStream.write(this.CompressOff);
                i++;
            }
            this.outStream.write(this.CompressOn);
            printlines2(printSepratorcomp(), 1, object, 1, args, 7, 7);
            JSONArray jTotal = object.getJSONArray("TOTAL");
            i = 0;
            while (i < jTotal.length()) {
                JSONObject jTOBject = jTotal.getJSONObject(0);
                String strTotal = "";
                for (j = 0; j < headers.length(); j++) {
                    if (i != 3 || i != 4 || i != 5 || i != 7 || i != 8) {
                        if (jTOBject.has(headers.getString(j))) {
                            strTotal = new StringBuilder(String.valueOf(strTotal)).append(getAccurateText(jTOBject.getString(headers.getString(j).toString()), ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth, ((Integer) this.hashPositions.get(headers.getString(j).toString())).intValue())).toString();
                        } else {
                            stringBuilder = new StringBuilder(String.valueOf(strTotal));
                            if (headers.getString(j).equals("Description")) {
                                string = "TOTAL";
                            } else {
                                string = "";
                            }
                            strTotal = stringBuilder.append(getAccurateText(string, ((Integer) this.hashValues.get(headers.getString(j))).intValue() + MAXLEngth, 1)).toString();
                        }
                    }
                }
                printlines2(strTotal, 1, object, 1, args, 7, 7);
                i++;
            }
            this.outStream.write(this.CompressOff);
            printlines2(" ", 2, object, 1, args, 7, 7);
            this.outStream.write(this.BoldOn);
            String totalAmt = "0";
            String varAmt = "0";
            StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(getAccurateText("TOTAL DAMAGE VALUE", 60, 2)));
            if (object.has("TOTAL_DAMAGE_VALUE")) {
                string2 = object.getString("TOTAL_DAMAGE_VALUE");
            } else {
                string2 = "0";
            }
            printlines1(getAccurateText("TOTAL INVOICE RETURNS : ", 40, 2) + getAccurateText(string2, 30, 1), 1, object, 1, args, 2);
            //printlines2(stringBuilder2.append(getAccurateText(string2, 16, 1)).toString(), 1, object, 1, args, 7, 7);
            stringBuilder2 = new StringBuilder(String.valueOf(getAccurateText("UNLOADED DAMAGE VARIANCE", 60, 2)));
            if (object.has("damagevariance")) {
                string2 = object.getString("damagevariance");
            } else {
                string2 = "0";
            }
            printlines1(getAccurateText("TOTAL LOADED IN RETURNS : ", 40, 2) + getAccurateText(string2, 30, 1), 1, object, 1, args, 2);
            //printlines2(stringBuilder2.append(getAccurateText(string2, 16, 1)).toString(), 1, object, 1, args, 7, 7);
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            //printlines2(" ", 2, object, 1, args, 7, 7);
            printlines1(getAccurateText("_____________", 40, 1) + getAccurateText("____________", 40, 1), 2, object, 1, args, 5);
            printlines1(getAccurateText("SUPERVISOR", 40, 1) + getAccurateText("SALESMAN", 40, 1), 2, object, 1, args, 5);
            //printlines2(getAccurateText("SALESMAN______________", 80, 1), 1, object, 2, args, 7, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Van Stock Report
    void printVanStockReport(JSONObject object, String args) {
        try {
            int i;
            StringBuilder stringBuilder;
            this.hashValues = new HashMap();
            this.hashValues.put("ITEM#", Integer.valueOf(7));
            this.hashValues.put("DESCRIPTION", Integer.valueOf(37));
            this.hashValues.put("LOADED QTY", Integer.valueOf(8));
            this.hashValues.put("SALE QTY", Integer.valueOf(6));
            this.hashValues.put("TRUCK STOCK", Integer.valueOf(7));
            this.hashValues.put("TOTAL", Integer.valueOf(9));
            this.hashPositions = new HashMap();
            this.hashPositions.put("ITEM#", Integer.valueOf(0));
            this.hashPositions.put("DESCRIPTION", Integer.valueOf(0));
            this.hashPositions.put("LOADED QTY", Integer.valueOf(2));
            this.hashPositions.put("SALE QTY", Integer.valueOf(2));
            this.hashPositions.put("TRUCK STOCK", Integer.valueOf(2));
            this.hashPositions.put("TOTAL", Integer.valueOf(1));
            line(this.startln);
            headervanstockprint(object, 4);
            JSONArray headers = object.getJSONArray("HEADERS");
            String strheader = "";
            String strHeaderBottom = "";
            String strTotal = "";
            JSONArray jTotal = object.getJSONArray("TOTAL");
            int MAXLEngth = 140;
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
                   /* if (headers.getString(i).indexOf(" ") == -1) {
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
                        if (headers.getString(i).equals("DESCRIPTION")) {
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
            printlines1(strheader, 1, object, 1, args, 4);
            //printlines1(strHeaderBottom, 1, object, 1, args, 4);
            printlines1(printSepratorcomp(), 1, object, 1, args, 4);
            this.outStream.write(this.CompressOff);
            JSONArray jData = object.getJSONArray(JsonRpcUtil.PARAM_DATA);
            for (i = 0; i < jData.length(); i++) {
                JSONArray jArr = jData.getJSONArray(i);
                String strData = "";
                for (int j = 0; j < jArr.length(); j++) {
                    if (j != 3) {
                        int i2;
                        Object obj;
                        String itemDescrion = jArr.getString(j);
                        if (j == 8) {
                            itemDescrion = "        @" + jArr.getString(j) + "!";
                        }
                        stringBuilder = new StringBuilder(String.valueOf(strData));
                        if (j == 8) {
                            i2 = 60;
                        } else {
                            i2 = ((Integer) this.hashValues.get(headers.getString(j).toString())).intValue() + MAXLEngth;
                        }
                        HashMap hashMap = this.hashPositions;
                        if (j == 8) {
                            obj = "Description";
                        } else {
                            obj = headers.getString(j).toString();
                        }
                        strData = stringBuilder.append(getAccurateText(itemDescrion, i2, ((Integer) hashMap.get(obj)).intValue())).toString();
                    }
                }
                this.outStream.write(this.CompressOn);
                this.count++;
                printlines1(strData, 1, object, 1, args, 4);
                this.outStream.write(this.CompressOff);
            }
            this.outStream.write(this.CompressOn);
            printlines1(printSepratorcomp(), 1, object, 1, args, 4);
            printlines1(strTotal, 1, object, 1, args, 4);
            printlines1(printSepratorcomp(), 1, object, 2, args, 4);
            this.outStream.write(this.CompressOff);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void printlines1(String data, int ln, JSONObject object, int sts, String adr, int tp) throws JSONException, IOException, LinePrinterException {
        int i;
        this.count += ln;
        boolean isEnd = false;
        if (sts == 2 && this.count != 0) {
            Log.e("Going for Arabic1", "Arabic" + data);
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
    private void printlines2(String data, int ln, JSONObject object, int sts, String adr, int tran, int tp) throws JSONException, IOException, LinePrinterException {
        int i;
        this.count += ln;
        boolean isEnd = false;
        if (sts == 2 && this.count != 0) {
            printArabic(data);
            //Log.e("salessummary count", this.count);
            //Log.e("salessummary pln", 48);
            isEnd = true;
            int lnno1 = (48 - this.count) + this.endln;
            for (i = 0; i < lnno1; i++) {
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
//            this.status.put("status", true);
//            this.status.put("isconnected", 0);
 //           sendUpdate(this.status, true);
        }
        if (!isEnd) {
            printArabic(data);
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
            Log.d("Count", "Count1" + this.count);
            if (this.count > 48) {
                this.count = 0;
                Log.d("Count", "Count1" + this.count);
                Log.d("Pln", "PLn" + 48);
                try {
                    line(this.cnln);
                    this.outStream.write(this.CompressOff);
                    if (tran == 2) {
                        headerprint(object, tp);
                    } else if (tran == 1) {
                        headerprint(object, tp);
                    } else if (tran == 5) {
                        headerprint(object, tp);
                    } else if (tran == 4) {
                        headerprint(object, tp);
                    } else if (tran == 3) {
                        headerprint(object, tp);
                    } else if (tran == 6) {
                        headerprint(object, tp);
                    } else if (tran == 7) {
                        headerprint(object, tp);
                    }
                    this.outStream.write(printSeprator().getBytes());
                    this.count++;
                    if (tran == 5 || tran == 4) {
                        this.outStream.write(this.CompressOn);
                    }
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
                if(!(invtype==3)){
                    printheaders(getAccurateText("DOCUMENT NO: " + object.getString("DOCUMENT NO"), 40, 0) + getAccurateText("TRIP START DATE:" + object.getString("TRIP START DATE"), 40, 2), false, 1);
                    this.outStream.write(this.NewLine);
                }

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
                printheaders(getAccurateText("ENDING INVENTORY SUMMARY", 40, 1), false, 2);
            } else if (invtype == 5) {
                printheaders(getAccurateText("LOAD REQUEST - " + object.getString("LOAD DATE"), 40, 1), false, 1);
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
            printheaders(getAccurateText("ROUTE#: " + object.getString("ROUTE"), 26, 0) + getAccurateText("SALESMAN#: " + object.getString("SALESMAN"), 26, 0) + getAccurateText("DATE:" + object.getString("DOC DATE") + " " + object.getString("TIME"), 26, 2), false, 1);
            this.outStream.write(this.NewLine);
            printheaders(getAccurateText(object.getString("TIME") + " " + object.getString("DOC DATE") + " " + "@" + ArabicLabels.Date, 26, 0) + "!" + getAccurateText(object.getString("SALESMAN") + " " + "@" + ArabicLabels.SalesMan, 26, 1) + "!", true, 1);
            printheaders(getAccurateText(object.getString("ROUTE") + "@" + ArabicLabels.Route, 26, 2)+"!" + " ",true,1);
            //printheaders(getAccurateText("SALESMAN: " + object.getString("SALESMAN"), 40, 0) + getAccurateText("SALESMAN NO: " + object.getString("CONTACTNO"), 40, 2), true, 1);
            this.outStream.write(this.NewLine);
            //printheaders(getAccurateText("SUPERVISOR NAME:" + object.getString("supervisorname"), 40, 0) + getAccurateText("SUPERVISOR PHONE: " + object.getString("supervisorno"), 40, 2), true, 1);
            if (type == 3 || type == 5 || type == 6 || type == 4 || type == 7) {
                printheaders(getAccurateText("TRIP START DATE:" + object.getString("TRIP START DATE"), 40, 0) + getAccurateText("TRIP ID:" + object.getString("TripID"), 40, 2), false, 1);
            } else {
                if(!(type==9||type==10)){
                    printheaders(getAccurateText("TRIP ID:" + object.getString("TripID"), 80, 0), false, 1);
                }
                /*else if(type == 9){
                    printheaders(getAccurateText("SUPERVISOR NAME: " + object.getString("supervisorname"), 40, 0) + getAccurateText("SUPERVISOR NO: " + object.getString("supervisorno"), 40, 2), false, 1);
                }*/
            }
            this.outStream.write(this.BoldOff);
            this.outStream.write(this.NewLine);
            this.outStream.write(this.NewLine);
            if (!(type == 3 && type == 6 && type == 4 && type == 5 && type == 7) && object.has("invheadermsg") && object.getString("invheadermsg").length() > 0) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                //printheaders(object.getString("invheadermsg"), false, 3);
                printheaders(getAccurateText(object.getString("invheadermsg"),40,1),false,2);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.DoubleWideOff);
            }
            if (type == 1) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                if (object.getString("LANG").equals("en")) {
                    printheaders(getAccurateText(object.getString("INVOICETYPE"), 40, 1), false, 2);
                    this.outStream.write(this.NewLine);
                    printheaders(getAccurateText("ORDER NO - " + object.getString("ORDERNO"),40,1),false,2);
                } else if (object.getString("invoicepaymentterms").contains("2")) {
                    printheaders(getAccurateText("@" + ArabicTEXT.Creditinvoice + "!:" + object.getString("invoicenumber"), 40, 1), true, 2);
                } else if (object.getString("invoicepaymentterms").contains("0") || object.getString("invoicepaymentterms").contains("1")) {
                    printheaders(getAccurateText("@" + ArabicTEXT.Cashinvoice + "!:" + object.getString("invoicenumber"), 40, 1), true, 2);
                } else {
                    //printheaders(getAccurateText(object.getString("INVOICETYPE"), 40, 1), false, 2);
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
                    Log.e("ARABIC CUSTOMER","" + parts[1]);
                    printheaders("          @" + parts[1] + "!", true, 1);
                    this.outStream.write(this.NewLine);
                    this.outStream.write(this.BoldOff);
                    printheaders("ADDRESS: " + object.getString("ADDRESS"), false, 1);
                    this.outStream.write(this.NewLine);
                    printheaders("          @" + object.getString("ARBADDRESS") + "!", true, 1);
                    this.outStream.write(this.NewLine);
                   // this.outStream.write(this.NewLine);
                  //  this.outStream.write(this.NewLine);
                } catch (Exception e) {
                }
                this.count++;
            }
            else if (type == 9) {   //Type for Sales Invoice
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                if (object.getString("LANG").equals("en")) {
                    String customerType = "";
                    if(object.getString("customertype").equals("1")){
                        customerType = "CASH";
                    }
                    else if(object.getString("customertype").equals("2")){
                        customerType = "CREDIT";
                    }
                    else if(object.getString("customertype").equals("3")){
                        customerType = "TC";
                    }
                    //printheaders(getAccurateText(object.getString("INVOICETYPE"), 40, 1), false, 2);
                    this.outStream.write(this.NewLine);
                    printheaders(getAccurateText(customerType + " " + "INVOICE NO - " + object.getString("ORDERNO"),40,1),false,2);
                } else if (object.getString("invoicepaymentterms").contains("2")) {
                    printheaders(getAccurateText("@" + ArabicTEXT.Creditinvoice + "!:" + object.getString("invoicenumber"), 40, 1), true, 2);
                } else if (object.getString("invoicepaymentterms").contains("0") || object.getString("invoicepaymentterms").contains("1")) {
                    printheaders(getAccurateText("@" + ArabicTEXT.Cashinvoice + "!:" + object.getString("invoicenumber"), 40, 1), true, 2);
                } else {
                    //printheaders(getAccurateText(object.getString("INVOICETYPE"), 40, 1), false, 2);
                }
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.BoldOn);
                try {
                    printheaders("CUSTOMER ID: " + object.getString("CUSTOMERID"), false, 2);
                    this.outStream.write(this.NewLine);
                    String[] parts = object.getString("CUSTOMER").split("\\-");
                    printheaders("CUSTOMER: " + parts[0], false, 2);
                    this.outStream.write(this.NewLine);
                    Log.e("ARABIC CUSTOMER","" + parts[1]);
                    printheaders("          @" + parts[1] + "!", true, 1);
                    this.outStream.write(this.NewLine);
                    this.outStream.write(this.BoldOff);
                    printheaders("ADDRESS: " + object.getString("ADDRESS"), false, 1);
                    this.outStream.write(this.NewLine);
                    printheaders("          @" + object.getString("ARBADDRESS") + "!", true, 1);
                    this.outStream.write(this.NewLine);
                    //this.outStream.write(this.NewLine);
                    //this.outStream.write(this.NewLine);
                } catch (Exception e) {
                }
                this.count++;
            }
            else if (type == 10) {   //Type for Delivery
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                if (object.getString("LANG").equals("en")) {
                    //printheaders(getAccurateText(object.getString("INVOICETYPE"), 40, 1), false, 2);
                    this.outStream.write(this.NewLine);
                    printheaders(getAccurateText("DELIVERY NO - " + object.getString("ORDERNO"), 40, 1), false, 2);
                    printheaders(getAccurateText("DELIVERY DATE - " + object.getString("DELIVERYDATE"),40,1),false,2);
                }
                if (object.getString("LANG").equals("en")) {
                    String customerType = "";
                    if(object.getString("customertype").equals("1")){
                        customerType = "CASH";
                    }
                    else if(object.getString("customertype").equals("2")){
                        customerType = "CREDIT";
                    }
                    else if(object.getString("customertype").equals("3")){
                        customerType = "TC";
                    }
                    //printheaders(getAccurateText(object.getString("INVOICETYPE"), 40, 1), false, 2);
                    this.outStream.write(this.NewLine);
                    printheaders(getAccurateText(customerType + " " + "INVOICE NO - " + object.getString("ORDERNO"),40,1),false,2);
                } else if (object.getString("invoicepaymentterms").contains("2")) {
                    printheaders(getAccurateText("@" + ArabicTEXT.Creditinvoice + "!:" + object.getString("invoicenumber"), 40, 1), true, 2);
                } else if (object.getString("invoicepaymentterms").contains("0") || object.getString("invoicepaymentterms").contains("1")) {
                    printheaders(getAccurateText("@" + ArabicTEXT.Cashinvoice + "!:" + object.getString("invoicenumber"), 40, 1), true, 2);
                } else {
                    //printheaders(getAccurateText(object.getString("INVOICETYPE"), 40, 1), false, 2);
                }
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.BoldOn);
                try {
                    printheaders("CUSTOMER ID: " + object.getString("CUSTOMERID"), false, 2);
                    this.outStream.write(this.NewLine);
                    String[] parts = object.getString("CUSTOMER").split("\\-");
                    printheaders("CUSTOMER: " + parts[0], false, 2);
                    this.outStream.write(this.NewLine);
                    Log.e("ARABIC CUSTOMER","" + parts[1]);
                    printheaders("          @" + parts[1] + "!", true, 1);
                    this.outStream.write(this.NewLine);
                    this.outStream.write(this.BoldOff);
                    printheaders("ADDRESS: " + object.getString("ADDRESS"), false, 1);
                    this.outStream.write(this.NewLine);
                    printheaders("          @" + object.getString("ARBADDRESS") + "!", true, 1);
                    this.outStream.write(this.NewLine);
                    printheaders("PAYEE: " + object.getString("payee"), false, 1);
                    this.outStream.write(this.NewLine);
                    //this.outStream.write(this.NewLine);
                    //this.outStream.write(this.NewLine);
                } catch (Exception e) {
                }
                this.count++;
            }

            else if (type == 2) {
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                if (object.getString("LANG").equals("en")) {
                    printheaders(getAccurateText("RECEIPT: " + object.getString("RECEIPT"), 40, 1), false, 2);
                } else {
                    printheaders(getAccurateText("@" + ArabicTEXT.Receipt + "!:" + object.getString("RECEIPT"), 40, 1), true, 2);
                }
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.BoldOn);
                printheaders("CUSTOMER ID: " + object.getString("CUSTOMERID"), false, 2);
                this.outStream.write(this.NewLine);
                String[] parts = object.getString("CUSTOMER").split("\\-");
                printheaders("CUSTOMER: " + parts[0], false, 2);
                this.outStream.write(this.NewLine);
                Log.e("ARABIC CUSTOMER", "" + parts[1]);
                printheaders("          @" + parts[1] + "!", true, 1);
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
                this.outStream.write(this.NewLine);
                this.outStream.write(this.BoldOn);
                this.outStream.write(this.DoubleWideOn);
                printheaders(getAccurateText("DAMAGE/EXPIRY REPORT", 40, 1), false, 3);
                this.outStream.write(this.DoubleWideOff);
                this.outStream.write(this.BoldOff);
                this.outStream.write(this.NewLine);
                this.outStream.write(this.NewLine);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    private void closeConnection(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
}
