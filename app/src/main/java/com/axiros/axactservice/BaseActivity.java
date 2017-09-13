package com.axiros.axactservice;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by bzero on 8/13/17.
 */

public abstract class BaseActivity extends Activity {

    private static BaseActivity curActivity;

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            if (this instanceof MainActivity) {
                Intent intent = new Intent(getCurrentActivity(), BlankActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            else {
                super.onBackPressed();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        setCurrentActivity(null);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static BaseActivity getCurrentActivity() {
        return curActivity;
    }

    public static void setCurrentActivity(BaseActivity activity) {
        curActivity = activity;
    }
}
