package co.atlascomputing.atfingerprintdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import co.atlascomputing.atfingerprint.DummyDevice;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button secugenBtn = (Button) findViewById(R.id.btn_secugen);
        secugenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecuGenActivity.class));
            }
        });

//        DummyDevice dm = new DummyDevice();
//        dm.init(this, (UsbManager)getSystemService(Context.USB_SERVICE));
//        dm.openDevice(0);
//        dm.closeDevice();

    }
}