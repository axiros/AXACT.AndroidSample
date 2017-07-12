package com.axiros.axactservice;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.axiros.axact.AxirosService;

public class MainActivity extends AppCompatActivity  {
    Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceIntent = new Intent(this, AxirosService.class);
        // About this key https://wiki.axiros.com/display/EFI/INTERNAL
        mServiceIntent.putExtra("key", "YpZYIAXwSWP5zKpsVRp8eM5Y7dFl9RY+F07Q2OUdx7x1dvZMsiBBg6BqbGwGsWSS3VinmLFweXSepmIrCG1lgg==");
        startService(mServiceIntent);

        Toast.makeText(this, "AXACT Service started.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        Toast.makeText(this, "Stoping AXACT Service.", Toast.LENGTH_LONG).show();
        stopService(mServiceIntent);
        super.onDestroy();
    }
}
