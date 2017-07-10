package com.axiros.axactservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.axiros.axact.AxirosService;

public class MainActivity extends Activity  {
    Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceIntent = new Intent(this, AxirosService.class);
        // About this key https://wiki.axiros.com/display/EFI/INTERNAL
        mServiceIntent.putExtra("key", "KEY HERE");
        startService(mServiceIntent);

        Toast.makeText(this, "AXACT Service started.", Toast.LENGTH_LONG).show();

        finish();
    }
}
