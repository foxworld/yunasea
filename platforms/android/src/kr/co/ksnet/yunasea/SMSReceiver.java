package kr.co.ksnet.yunasea;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
                    NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = new Notification(R.drawable.icon, context.getString(R.string.alert_sms_title), System.currentTimeMillis());
                    //notification.flags = Notification.FLAG_ONGOING_EVENT;
                    notification.flags = Notification.FLAG_AUTO_CANCEL;

                    Intent intent_nm = new Intent(context, NoticesActivity.class);
                    intent_nm.putExtra("SMS_TITLE", context.getString(R.string.alert_sms_title));
                    intent_nm.putExtra("SMS_BODY" , sms_body);
                    intent_nm.putExtra("SMS_TIME" , dateformat.format(new Date(msgs[i].getTimestampMillis())));
                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent_nm, 0);
                    notification.setLatestEventInfo(context, context.getString(R.string.alert_sms_title), sms_body, contentIntent);
                    nm.notify(context.getString(R.string.NOTIFICATION_ID), 0, notification);

                    Intent intent_sms = new Intent();
                    intent_sms.setAction(context.getString(R.string.BROADCASET_NOTICE));
                    intent_sms.putExtra("SMS_TITLE" , context.getString(R.string.alert_sms_title));
                    intent_sms.putExtra("SMS_BODY" , sms_body);
                    intent_sms.putExtra("SMS_TIME" , dateformat.format(new Date(msgs[i].getTimestampMillis())));
                    context.sendBroadcast(intent_sms);
                }
            }
        }
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
