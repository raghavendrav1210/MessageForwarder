package com.ragav.messageforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        snedMessageToRecipient(context, "Message From: " + msg_from + "--" + msgBody);
                    }
                } catch (Exception e) {
                    Log.e("Exception caught", e.getMessage());
                }
            }
        }
    }

    private void snedMessageToRecipient(Context context, String msgBody) {

        String config = readDataFromSharedPrefs(context);

        if (null != config && config.contains("~")) {

            String[] recipientDetails = config.split("~");

            if (null != recipientDetails && recipientDetails.length > 0) {

                String phoneNo = recipientDetails[0];
                String filter = recipientDetails[1];

                if (null != filter) {
                    if (msgBody.toLowerCase().contains(filter.toLowerCase())) {
                        forwardMessage(context, phoneNo, msgBody);

                        Toast.makeText(context, "Message Forwarded to " + phoneNo, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Message didn't match the config. Not forwarding", Toast.LENGTH_SHORT).show();

                    }
                }
            } else {
                Toast.makeText(context, "No configuration found. Couldn't send sms", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String readDataFromSharedPrefs(Context context) {

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        return sharedPref.getString(context.getString(R.string.recipient_info), null);
    }

    private void forwardMessage(Context context, String phoneNo, String message) {

        try {
            SmsManager sms = SmsManager.getDefault(); // using android SmsManager
            sms.sendTextMessage(phoneNo, null, message, null, null); // adding number and text
        } catch (Exception e) {
            Toast.makeText(context, "Couldn't send sms", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
