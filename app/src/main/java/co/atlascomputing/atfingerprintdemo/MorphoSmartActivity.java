package co.atlascomputing.atfingerprintdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import co.atlascomputing.atfingerprint.wrappers.MorphoSmartDeviceWrapper;

public class MorphoSmartActivity extends AppCompatActivity implements Observer {
    private MorphoSmartDeviceWrapper dm = null;
    TextView statusTextView = null;
    ImageView fingerprintImageView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morpho_smart);


        dm = new MorphoSmartDeviceWrapper(getApplicationContext());

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
                        int success = dm.init(null);

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
                        int timeout = 100000000; // in milliseconds
                        int quality = 40;
                        byte[] rawData = dm.captureImage(timeout, quality,MorphoSmartActivity.this);

                        // update UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (rawData == null) {
                                    statusTextView.setText("capture failed");
                                    return;
                                }

//                                // TODO: get width and height from GetDeviceInfo, 296,354
//                                fingerprintImageView.setImageBitmap(MorphoSmartActivity.toGrayscale(rawData, 296, 354));
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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int success = dm.close();

                        // update UI
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusTextView.setText(success == 0 ? "Close success" : "Close failed");
                            }
                        });
                    }
                }).start();


            }
        });


    }

    @Override
    public void update(Observable o, Object arg) {

    }
}