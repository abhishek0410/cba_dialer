package com.dialer.replicate.AppUtils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.TelephonyManager;

import java.util.Date;


public class PhoneStateService extends Service {
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    public static boolean isIncoming;
    private static String savedNumber, phoneNumber;
    BroadcastReceiver callExplicitReceiver;
    //    public static MediaPlayer mediaPlayer;
    private static String data = "false";
    public static Context mContext;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;
        if (intent.getExtras() != null) {
            data = (String) intent.getExtras().get("showPopUp");
            phoneNumber = (String) intent.getExtras().get("dialNumber");
        }
        if (data.equalsIgnoreCase("true")) {
            callExplicitReceiver = new PhoneStateService.CallReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PHONE_STATE");
            this.registerReceiver(callExplicitReceiver, intentFilter);
        }
        return START_NOT_STICKY;
    }


    public static void onIncomingCallReceived(Context ctx, String number, Date start) {
    }

    public static void onIncomingCallAnswered(Context ctx, String number, Date start) {
    }

    public static void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DeleteCallLogByNumber(number);
            }
        }, 2000);

    }

    public static void onOutgoingCallStarted(Context ctx, String number, Date start) {
//        mAppPreferences.setPrefrenceString("busy", "yes");
//        if (data.equalsIgnoreCase("true")) {
        if (data.equalsIgnoreCase("true")) {
//            mediaPlayer = MediaPlayer.create(ctx, R.raw.speech_audio);
//        } else {
//            mediaPlayer = MediaPlayer.create(ctx, R.raw.speech_audio);
//        }

//            mediaPlayer.start();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                }
            }
        }, 12000);
    }


    public static void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DeleteCallLogByNumber(phoneNumber);
            }
        }, 2000);
    }

    public static void onMissedCall(Context ctx, String number, Date start) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DeleteCallLogByNumber(phoneNumber);
            }
        }, 2000);
    }

    @SuppressLint("MissingPermission")
    public static void DeleteCallLogByNumber(String number) {
        try {
            Uri CALLLOG_URI = Uri.parse("content://call_log/calls");
            mContext.getContentResolver().delete(CALLLOG_URI, CallLog.Calls.NUMBER + "=?", new String[]{number});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public static void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallReceived(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                } else {
                    isIncoming = true;
                    callStartTime = new Date();
                    onIncomingCallAnswered(context, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    onMissedCall(context, savedNumber, callStartTime);
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }

    @Override
    public void onDestroy() {
        if (callExplicitReceiver != null) {
            this.unregisterReceiver(callExplicitReceiver);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    public static class CallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
                    if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
                        savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
                    } else {
                        String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
                        phoneNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        int state = 0;
                        if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                            state = TelephonyManager.CALL_STATE_IDLE;
                        } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                            state = TelephonyManager.CALL_STATE_OFFHOOK;
                        } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                            state = TelephonyManager.CALL_STATE_RINGING;
                        }
                        onCallStateChanged(context, state, phoneNumber);
                    }
                }
            }
        }
    }

}
