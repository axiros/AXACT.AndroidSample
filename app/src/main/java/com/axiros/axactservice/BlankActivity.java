package com.axiros.axactservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by bzero on 8/13/17.
 */

public class BlankActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(BlankActivity.this,
                MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
            finish();
        }
    }



}
