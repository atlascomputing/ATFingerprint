package co.atlascomputing.atfingerprintdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.atlascomputing.atfingerprint.DummyDevice;

public class SecuGenActivity extends AppCompatActivity {

    private DummyDevice dm = null;
    TextView statusTextView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secu_gen);
        dm = new DummyDevice();

        statusTextView = (TextView) findViewById(R.id.textView_status);

        // init
        final Button initBtn = (Button) findViewById(R.id.btn_init);
        initBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int success = dm.init(SecuGenActivity.this, (UsbManager)getSystemService(Context.USB_SERVICE));
                statusTextView.setText(success == 0 ? "Init success" : "Init failed");
            }
        });


        // open
        final Button openBtn = (Button) findViewById(R.id.btn_open);
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int success = dm.openDevice(0);
                statusTextView.setText(success == 0 ? "openDevice success" : "openDevice failed");

            }
        });


        // capture
        final Button captureBtn = (Button) findViewById(R.id.btn_capture);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] rawData = dm.captureRaw();
            }
        });


        // close
        final Button closeBtn = (Button) findViewById(R.id.btn_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dm.close();
                int success = dm.closeDevice();
                statusTextView.setText(success == 0 ? "closeDevice success" : "closeDevice failed");

            }
        });

    }

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.d(TAG,"Enter mUsbReceiver.onReceive()");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            //DEBUG Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
                            //DEBUG Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
                            Log.d("AT","USB BroadcastReceiver VID : " + device.getVendorId() + "\n");
                            Log.d("AT","USB BroadcastReceiver PID: " + device.getProductId() + "\n");
                        }
                        else
                            Log.e("AT", "mUsbReceiver.onReceive() Device is null");
                    }
                    else
                        Log.e("AT", "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };
}