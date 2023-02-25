package co.atlascomputing.atfingerprintdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import co.atlascomputing.atfingerprint.FingerprintLib;
import co.atlascomputing.atfingerprint.dto.DeviceInfo;
import co.atlascomputing.atfingerprint.dto.DeviceModel;

public class AutoDetectActivity extends AppCompatActivity {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    PendingIntent mPermissionIntent;
    private BroadcastReceiver mUsbReceiver;

    TextView connectedDeviceTextView = null;
    TextView statusTextView = null;
    ImageView fingerprintImageView = null;

    FingerprintLib fingerprintLib = null;
    DeviceInfo deviceInfo;
    DeviceModel deviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_detect);

        // bind UI
        connectedDeviceTextView = (TextView) findViewById(R.id.textView_connected);
        statusTextView = (TextView) findViewById(R.id.textView_status);
        fingerprintImageView = (ImageView) findViewById(R.id.imageView_fingerprint);

        // capture
        final Button captureBtn = (Button) findViewById(R.id.btn_capture);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        byte[] rawData = fingerprintLib.startCapture(deviceModel);
                        byte[] rawData = null;
//                        int timeout = 0;
//                        int minQuality = 50;
                        int timeout = 100000000; // in milliseconds
                        int minQuality = 40;
                        fingerprintLib.startCapture(deviceModel, timeout, minQuality);

                        // update UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (rawData == null) {
                                    statusTextView.setText("capture failed");
                                    return;
                                }
                                statusTextView.setText("capture success");

                                // TODO: get width and height from GetDeviceInfo
                                fingerprintImageView.setImageBitmap(SecuGenActivity.toGrayscale(rawData, 300, 400));
                            }
                        });
                    }
                }).start();


            }
        });


        // permisssion intent
        int flags = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE;
        } else {
            flags = PendingIntent.FLAG_UPDATE_CURRENT;
        }

        Intent intent = new Intent(ACTION_USB_PERMISSION);
        mPermissionIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, flags);


        //broad cast receiver
        this.mUsbReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
                Log.d("AT", "BroadcastReceiver.onReceive action: " + action);
                Log.d("AT", "BroadcastReceiver.onReceive device: " + device.getProductName());


                // attached
                if (Objects.equals(action, UsbManager.ACTION_USB_DEVICE_ATTACHED)) {

                    connectedDeviceTextView.setText("ATTACHED: " + device.getProductName());

                    // check supported device then request permission
                    if (!fingerprintLib.isSupportedDevice(device.getVendorId(), device.getProductId(), device.getProductName(), device.getManufacturerName())) {
                        connectedDeviceTextView.setText("DEVICE NOT SUPPORTED: " + device.getProductName());
                        Log.d("AT", "DEVICE NOT SUPPORTED: " + device.getProductName());
                        return;
                    }
                    Log.d("AT", "SUPPORTED DEVICE: " + device.getProductName());

                    UsbManager usbManager = (UsbManager) AutoDetectActivity.this.getSystemService(USB_SERVICE);

                    // request permission
                    if (!usbManager.hasPermission(device)) {
                        usbManager.requestPermission(device, mPermissionIntent);
                        return;
                    }

                    //if has permission
                    initializeConnectedDevice(device);

                }

                // detached
                if (Objects.equals(action, UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                    connectedDeviceTextView.setText("DETACHED: " + device.getProductName());
                    closeConnectedDevice(device);
                }

                // permission
                if (Objects.equals(action, ACTION_USB_PERMISSION)) {

                    //check if permission was granted
                    boolean hasPermission = intent.getBooleanExtra("permission", false);
                    Log.d("AT", "BroadcastReceiver.onReceive hasPermission: " + hasPermission);

                    if (hasPermission) {
                        initializeConnectedDevice(device);
                    } else {
                        connectedDeviceTextView.setText("USB PERMISSION NOT GRANTED: " + device.getProductName());
                    }

                }

            }
        };

        this.registerUsbHost();

        // initialize fingerprint lib
        fingerprintLib = new FingerprintLib(getApplicationContext());
    }


    public void registerUsbHost() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_USB_PERMISSION);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            this.getApplicationContext().registerReceiver(this.mUsbReceiver, filter);
            this.findDeviceAndRequestPermission();
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    public void unRegisterUsbHost() {
        try {
            if (this.mUsbReceiver != null) {
                this.getApplicationContext().unregisterReceiver(this.mUsbReceiver);
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    private void findDeviceAndRequestPermission() {
        try {
            UsbManager usbManager = (UsbManager) this.getSystemService(USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            Iterator var3 = deviceList.values().iterator();

            while (var3.hasNext()) {
                UsbDevice device = (UsbDevice) var3.next();

                if (fingerprintLib.isSupportedDevice(device.getVendorId(), device.getProductId(), device.getProductName(), device.getManufacturerName())) {

                    // request permission
                    if (!usbManager.hasPermission(device)) {
                        usbManager.requestPermission(device, mPermissionIntent);
                    }
                }

            }

        } catch (Exception var7) {
            Log.e("", "FindDeviceAndRequestPermission.Exception", var7);
        }

    }

    @Override
    protected void onDestroy() {

        fingerprintLib.close(deviceModel);

        this.unRegisterUsbHost();
        super.onDestroy();
    }


    public void initializeConnectedDevice(UsbDevice device) {
        Log.d("AT", "Initializing connected device:  " + device.getProductName());

        deviceInfo = new DeviceInfo();
        deviceModel = new DeviceModel(device.getVendorId(), device.getProductId(), device.getProductName(), device.getManufacturerName());
        fingerprintLib.init(deviceModel, null, deviceInfo);
    }

    public void closeConnectedDevice(UsbDevice device) {
//        DeviceModel dModel = new DeviceModel(device.getVendorId(), device.getProductId(), device.getProductName(), device.getManufacturerName());
//        fingerprintLib.closeDevice(closeDevice);
    }

}