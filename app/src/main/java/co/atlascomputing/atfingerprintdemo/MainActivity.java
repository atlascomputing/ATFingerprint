package co.atlascomputing.atfingerprintdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

        final Button mantraMorphinBtn = (Button) findViewById(R.id.btn_mantra_morfin);
        mantraMorphinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MantraMorfinAuthActivity.class));
            }
        });

        final Button mantraMidBtn = (Button) findViewById(R.id.btn_mantra_mid);
        mantraMidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MantraMIDAuthActivity.class));
            }
        });



        final Button mantraMfs100Btn = (Button) findViewById(R.id.btn_mantra_mfs100);
        mantraMfs100Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MantraMFS100Activity.class));
            }
        });



        final Button morphoBtn = (Button) findViewById(R.id.btn_morpho);
        morphoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MorphoSmartActivity.class));
            }
        });

        final Button fpBtn = (Button) findViewById(R.id.btn_fp);
        fpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChineseFPActivity.class));
            }
        });


        final Button autoDetectBtn = (Button) findViewById(R.id.btn_auto_detect);
        autoDetectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AutoDetectActivity.class));
            }
        });

    }
}