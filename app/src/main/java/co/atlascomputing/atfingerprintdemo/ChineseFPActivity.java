package co.atlascomputing.atfingerprintdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import co.atlascomputing.atfingerprint.wrappers.ChineseFPScannerWrapper;
import co.atlascomputing.atfingerprint.wrappers.SecuGenScannerWrapper;

public class ChineseFPActivity extends AppCompatActivity {

    private ChineseFPScannerWrapper dm = null;
    TextView statusTextView = null;
    ImageView fingerprintImageView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_fpactivity);

        dm = new ChineseFPScannerWrapper(getApplicationContext());

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
                        int success = dm.openDevice();

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
                        int timeout = 100000000; // in milliseconds
                        int quality = 40;
                        byte[] rawData = dm.captureImage(timeout, quality);
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
}