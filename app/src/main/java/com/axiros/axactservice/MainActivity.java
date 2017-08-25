package com.axiros.axactservice;

import android.app.Activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.axiros.axact.AxirosService;

public class MainActivity  extends BaseActivity implements View.OnClickListener, AxirosService.AxirosEventsListener {
    private static AxirosService mService;
    private static AxirosService.LocalBinder mServiceBinder;
    private static Intent mServiceIntent;

    private static boolean mBound;

    MainActivity mActivity;
    Button buttonStart, buttonStop;

    TextView textViewUploadStatus;
    TextView uploadView;

    TextView textViewDownloadStatus;
    TextView downloadView;

    TextView textViewUDPStatus;
    TextView textViewConnection;

    TextView udpEchoView;

    ProgressBar mProgressBarDownload;
    ProgressBar mProgressBarUpload;
    CountDownTimer mCountDownTimerDL;
    CountDownTimer mCountDownTimerUL;

    int countUp = 0;
    int countDown = 0;

    private String getNetworkType() {
        String networkType = "n/a";
        ConnectivityManager cm = (ConnectivityManager) mService.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (networkInfo.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        networkType = "1xRTT";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        networkType = "CDMA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        networkType = "EDGE";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        networkType = "EVDO_0";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        networkType = "EVDO_A";
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        networkType = "GPRS";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        networkType = "HSDPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        networkType = "HSPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        networkType = "HSUPA";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        networkType = "UMTS";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        networkType = "EHRPD";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        networkType = "EVDO_B";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        networkType = "HSPAP";
                        break;
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        networkType = "IDEN";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        networkType = "LTE";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        networkType = "UNKNOWN";
                        break;
                }
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                networkType = "WiFi";
            }
        }

        return networkType;
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.axiros.axact.AxirosService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceBinder = (AxirosService.LocalBinder)service;
            mService = mServiceBinder.getServiceInstance();
            mService.registerEventsListener(mActivity);
            textViewConnection.setText(String.format("Connection type: %s", getNetworkType()));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService.unregisterEventsListener(mActivity);
            mService = null;
            mServiceBinder = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        Intent intent = new Intent(this, AxirosService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    private void init()
    {
        mActivity = this;

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        textViewUDPStatus = (TextView) findViewById(R.id.textViewUDPStatus);
        textViewUploadStatus = (TextView) findViewById(R.id.textViewUploadStatus);
        textViewDownloadStatus = (TextView) findViewById(R.id.textViewDownloadStatus);
        uploadView = (TextView) findViewById(R.id.textViewUpload);
        downloadView = (TextView) findViewById(R.id.textViewDownload);
        udpEchoView = (TextView) findViewById(R.id.textViewUDP);

        textViewConnection = (TextView) findViewById(R.id.textViewConnection);

        if(isServiceRunning())
        {
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);
        }
        else
        {
            buttonStart.setEnabled(true);
            buttonStop.setEnabled(false);
        }


        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

        mProgressBarUpload=(ProgressBar)findViewById(R.id.progressBarUpload);
        mProgressBarUpload.setProgress(0);

        mProgressBarDownload=(ProgressBar)findViewById(R.id.progressBarDownload);
        mProgressBarDownload.setProgress(0);
    }

    private void clearView() {
        mProgressBarDownload.setProgress(0);
        mProgressBarUpload.setProgress(0);
        textViewUDPStatus.setText(" ");
        textViewUploadStatus.setText(" ");
        textViewDownloadStatus.setText(" ");
        uploadView.setText(" ");
        downloadView.setText(" ");
        udpEchoView.setText(" ");

        countUp = 0;
        countDown = 0;
    }

    @Override
    public void onClick(View view) {


        if (mServiceIntent == null)
        {
            mServiceIntent = new Intent(this, AxirosService.class);
            // About this key https://wiki.axiros.com/display/EFI/INTERNAL
            mServiceIntent.putExtra("key", "zptkrc8uJaud1spndstrqhCwb/MGaAj72Oiv2WcU43EaawEHu1bGoqrbLdpqF/EQX1ChYOT7dUuKYssVivAHcQ==");

            // only integrators with url need to use it
            mServiceIntent.putExtra("cert", "eaq.com.br.crt");
        }

        switch (view.getId()) {

            case R.id.buttonStart:
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);

                startService(mServiceIntent);

                break;
            case R.id.buttonStop:
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);

                stopService(mServiceIntent);

                clearView();
                break;
        }
    }

    @Override
    public void downloadConfigured() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewDownloadStatus.setText("Running download test");
                mCountDownTimerDL =new CountDownTimer(30000,1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        countDown++;
                        mProgressBarDownload.setProgress((int) countDown *100/(30000/1000));

                    }

                    @Override
                    public void onFinish() {
                        //Do what you want
                        countDown++;
                        mProgressBarDownload.setProgress(100);
                    }
                };
                mCountDownTimerDL.start();
            }
        });
    }

    @Override
    public void uploadConfigured() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewUploadStatus.setText("Running upload test");
                mCountDownTimerUL =new CountDownTimer(30000,1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        countUp++;
                        mProgressBarUpload.setProgress((int) countUp *100/(30000/1000));

                    }

                    @Override
                    public void onFinish() {
                        //Do what you want
                        countUp++;
                        mProgressBarUpload.setProgress(0);
                    }
                };
                mCountDownTimerUL.start();
            }
        });
    }

    @Override
    public void udpEchoConfigured() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearView();;
                textViewUDPStatus.setText("Running UDP echo test");
            }
        });
    }

    @Override
    public void downloadDiagnosticsResult(long bps) {
        final long result = bps;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadView.setText( String.format("Download Result: %d", result));
                mCountDownTimerDL.cancel();
            }
        });
    }

    @Override
    public void uploadDiagnosticsResult(long bps) {
        final long result = bps;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uploadView.setText( String.format("Upload Result: %d", result));
                if (mCountDownTimerUL != null) {
                    mCountDownTimerUL.cancel();
                }
            }
        });
    }

    @Override
    public void udpEchoDiagnosticsResult(long bps) {
        final long result = bps;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                udpEchoView.setText( String.format("UDP ECHO Result: %d", result));
            }
        });
    }


}
