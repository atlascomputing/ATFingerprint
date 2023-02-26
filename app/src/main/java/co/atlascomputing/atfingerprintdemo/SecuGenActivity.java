package co.atlascomputing.atfingerprintdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.nio.ByteBuffer;

import co.atlascomputing.atfingerprint.wrappers.SecuGenScannerWrapper;

public class SecuGenActivity extends AppCompatActivity {

    private SecuGenScannerWrapper dm = null;
    TextView statusTextView = null;
    ImageView fingerprintImageView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secu_gen);
        dm = new SecuGenScannerWrapper(getApplicationContext());

        statusTextView = (TextView) findViewById(R.id.textView_status);
        fingerprintImageView = (ImageView) findViewById(R.id.imageView_fingerprint);

        // init
        final Button initBtn = (Button) findViewById(R.id.btn_init);
        initBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int success = dm.init();

                        // update UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusTextView.setText(success == 0 ? "Init success" : "Init failed");
                            }
                        });
                    }
                }).start();


            }
        });


        // open
        final Button openBtn = (Button) findViewById(R.id.btn_open);
        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int success = dm.openDevice(0);

                        // update UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusTextView.setText(success == 0 ? "openDevice success" : "openDevice failed");
                            }
                        });
                    }
                }).start();


            }
        });


        // capture
        final Button captureBtn = (Button) findViewById(R.id.btn_capture);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] rawData = dm.captureImage();
//                byte[] rawData = dm.captureImageSimple();
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

    public static Bitmap toGrayscale(byte[] mImageBuffer, int width, int height) {
        byte[] Bits = new byte[mImageBuffer.length * 4];
        for (int i = 0; i < mImageBuffer.length; i++) {
            Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
            Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
        }

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
        return bmpGrayscale;
    }

    // TODO: find a way to share ACTION_USB_PERMISSION from library
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //Log.d(TAG,"Enter mUsbReceiver.onReceive()");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //DEBUG Log.d(TAG, "Vendor ID : " + device.getVendorId() + "\n");
                            //DEBUG Log.d(TAG, "Product ID: " + device.getProductId() + "\n");
                            Log.d("AT", "USB BroadcastReceiver VID : " + device.getVendorId() + "\n");
                            Log.d("AT", "USB BroadcastReceiver PID: " + device.getProductId() + "\n");
                        } else
                            Log.e("AT", "mUsbReceiver.onReceive() Device is null");
                    } else
                        Log.e("AT", "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };
}