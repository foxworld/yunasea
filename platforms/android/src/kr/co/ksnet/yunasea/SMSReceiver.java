package kr.co.ksnet.yunasea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {
    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String sms_body;
        //String action = intent.getAction();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if(intent.getAction().equals(context.getString(R.string.SMS_RECEIVED))) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage msgs[] = new SmsMessage[pdus.length];
            for (int i = 0; i <msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            for (int i = 0; i <msgs.length; i++) {
                Log.d("foxworld", "getMessageBody=" + msgs[i].getMessageBody());
                Log.d("foxworld", "getOriginatingAddress="+msgs[i].getOriginatingAddress());
                Log.d("foxworld", "getDisplayOriginatingAddress="+msgs[i].getDisplayOriginatingAddress());
                Log.d("foxworld", "getTimestampMillis="+dateformat.format(new Date(msgs[i].getTimestampMillis())));

                if(msgs[i].getOriginatingAddress().equals("0234205838") || msgs[i].getOriginatingAddress().equals("0234200000") || msgs[i].getOriginatingAddress().equals("01027577127")) {
                    sms_body = msgs[i].getMessageBody();
                    if(sms_body.substring(0,7).equals("[Web발신]")) {
                        sms_body = sms_body.substring(7, sms_body.length());
                    }
                    Toast.makeText(context, "**"+context.getString(R.string.alert_sms_title)+"**\n"+sms_body, Toast.LENGTH_LONG).show();
                    //abortBroadcast();
                }
            }
        }
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
