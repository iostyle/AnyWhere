package dream.anywhere.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by SKYMAC on 16/8/31.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bd = intent.getExtras();
        Object[] os = (Object[]) bd.get("pdus");
        byte[] bytes = (byte[]) os[0];
        SmsMessage sms = SmsMessage.createFromPdu(bytes);
        String message = sms.getDisplayMessageBody();
        Log.i("Receiver", message.substring(1,5));
        Log.i("Receiver", message.substring(12,18));
        String text = message.substring(1, 5);
        if(text.equals("美团外卖")){
            message = message.substring(12, 18);
            Log.i("Log", message);
            if (listener != null) {
                listener.toSms(message);
            }
        }
    }
    //外部调用此方法监听短信
    public static void setSmsListener(SmsListener listener) {
        //回调监听，短信内容会自动传入这个传入的listener的toSms方法中
        SmsReceiver.listener = listener;
    }

    public interface SmsListener {
        void toSms(String text);
    }
}
