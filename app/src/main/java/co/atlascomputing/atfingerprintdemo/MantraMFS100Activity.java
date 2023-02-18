package co.atlascomputing.atfingerprintdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import co.atlascomputing.atfingerprint.wrappers.MantraMFS100DeviceWrapper;

public class MantraMFS100Activity extends AppCompatActivity {

    private MantraMFS100DeviceWrapper dm = null;
    TextView statusTextView = null;
    ImageView fingerprintImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantra_mfs100);


        dm = new MantraMFS100DeviceWrapper();

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
                        int success = dm.init(MantraMFS100Activity.this, null);

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

        // capture
        final Button captureBtn = (Button) findViewById(R.id.btn_capture);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int timeout = 100000000; // in milliseconds, 0 means infinite, no timeout
                        byte[] rawData = dm.captureImage(timeout, false);

                        // update UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (rawData == null) {
                                    statusTextView.setText("capture failed");
                                    return;
                                }

                                // TODO: get width and height from GetDeviceInfo, 316,354
                                fingerprintImageView.setImageBitmap(SecuGenActivity.toGrayscale(rawData, 316,354));
                            }
                        });
                    }
                }).start();


            }
        });
    }
}