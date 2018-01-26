package com.axiros.axactservice;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.axiros.axact.AxirosService;

public class MainActivity  extends BaseActivity implements AxirosService.AxirosEventsListener {
    private static AxirosService mService;
    private static AxirosService.LocalBinder mServiceBinder;
    private static Intent mServiceIntent;

    MainActivity mActivity;

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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceIntent = new Intent(this, AxirosService.class);

        // abr
        mServiceIntent.putExtra("key", "zptkrc8uJaud1spndstrqhCwb/MGaAj72Oiv2WcU43EaawEHu1bGoqrbLdpqF/EQX1ChYOT7dUuKYssVivAHcQ==");
        mServiceIntent.putExtra("enableWakeLock", "Off");

        // only integrators with url need to use it
        mServiceIntent.putExtra("cert", "eaq.com.br.crt");

        bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);


        init();
    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceBinder = (AxirosService.LocalBinder)service;
            mService = mServiceBinder.getServiceInstance();
            mService.registerEventsListener(mActivity);
            mService.verifyServicePermission(mActivity);
            textViewConnection.setText(String.format("Connection type: %s", getNetworkType()));

            startService(mServiceIntent);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService.unregisterEventsListener(mActivity);
            mService = null;
            mServiceBinder = null;
        }
    };

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
    public void downloadConfigured() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mCountDownTimerUL != null) {
                    mCountDownTimerUL.cancel();
                }

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
                if (mCountDownTimerDL != null) {
                    mCountDownTimerDL.cancel();
                }
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
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                networkType = "Ethernet";
            }
        }

        return networkType;
    }

    private void init()
    {
        mActivity = this;

        textViewUDPStatus = (TextView) findViewById(R.id.textViewUDPStatus);
        textViewUploadStatus = (TextView) findViewById(R.id.textViewUploadStatus);
        textViewDownloadStatus = (TextView) findViewById(R.id.textViewDownloadStatus);
        uploadView = (TextView) findViewById(R.id.textViewUpload);
        downloadView = (TextView) findViewById(R.id.textViewDownload);
        udpEchoView = (TextView) findViewById(R.id.textViewUDP);

        textViewConnection = (TextView) findViewById(R.id.textViewConnection);

        mProgressBarUpload=(ProgressBar)findViewById(R.id.progressBarUpload);
        mProgressBarUpload.setProgress(0);

        mProgressBarDownload=(ProgressBar)findViewById(R.id.progressBarDownload);
        mProgressBarDownload.setProgress(0);
    }

}
