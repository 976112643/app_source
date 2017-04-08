package com.wq.businessdirectory.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by WQ on 2017/2/10.
 */

public class SMSUtil {

    public static PhoneMessage generateMessage(Intent intent) {
        Bundle bundle = intent.getExtras();
        PhoneMessage phoneMessage = null;
        if (null != bundle) {
            SmsMessage smsMessage = null;
            Object[] smsObj = (Object[]) bundle.get("pdus");
            phoneMessage = new PhoneMessage();
            StringBuilder stringBuilder = new StringBuilder();
            for (Object object : smsObj) {
                smsMessage = SmsMessage.createFromPdu((byte[]) object);
                String sender = smsMessage.getDisplayOriginatingAddress();
                //短信内容
                String content = smsMessage.getDisplayMessageBody();
                long date = smsMessage.getTimestampMillis();
                Date tiemDate = new Date(date);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = simpleDateFormat.format(tiemDate);
                phoneMessage.setPhone(sender);
                phoneMessage.setAddtime(time);
                stringBuilder.append(content);
            }
            phoneMessage.setContent(stringBuilder.toString());
            Log.e("weiquan", String.valueOf(phoneMessage));
        }
        return phoneMessage;
    }

    public static void createFakeSms(Context context, String sender, String body) {
        //Source: http://stackoverflow.com/a/12338541
        //Source: http://blog.dev001.net/post/14085892020/android-generate-incoming-sms-from-within-your-app
        byte[] pdu = null;
        byte[] scBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD("0000000000");
        byte[] senderBytes = PhoneNumberUtils
                .networkPortionToCalledPartyBCD(sender);
        int lsmcs = scBytes.length;
        byte[] dateBytes = new byte[7];
        Calendar calendar = new GregorianCalendar();
        dateBytes[0] = reverseByte((byte) (calendar.get(Calendar.YEAR)));
        dateBytes[1] = reverseByte((byte) (calendar.get(Calendar.MONTH) + 1));
        dateBytes[2] = reverseByte((byte) (calendar.get(Calendar.DAY_OF_MONTH)));
        dateBytes[3] = reverseByte((byte) (calendar.get(Calendar.HOUR_OF_DAY)));
        dateBytes[4] = reverseByte((byte) (calendar.get(Calendar.MINUTE)));
        dateBytes[5] = reverseByte((byte) (calendar.get(Calendar.SECOND)));
        dateBytes[6] = reverseByte((byte) ((calendar.get(Calendar.ZONE_OFFSET) + calendar
                .get(Calendar.DST_OFFSET)) / (60 * 1000 * 15)));
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            bo.write(lsmcs);
            bo.write(scBytes);
            bo.write(0x04);
            bo.write((byte) sender.length());
            bo.write(senderBytes);
            bo.write(0x00);
            try {
                String sReflectedClassName = "com.android.internal.telephony.GsmAlphabet";
                Class cReflectedNFCExtras = Class.forName(sReflectedClassName);
                Method stringToGsm7BitPacked = cReflectedNFCExtras.getMethod(
                        "stringToGsm7BitPacked", new Class[]{String.class});
                stringToGsm7BitPacked.setAccessible(true);
                byte[] bodybytes = (byte[]) stringToGsm7BitPacked.invoke(null, body);

                bo.write(0x00); // encoding: 0 for default 7bit
                bo.write(dateBytes);
                bo.write(bodybytes);
            } catch (Exception e) {
                try {
                    // try UCS-2
                    byte[] bodybytes = encodeUCS2(body, null);
                    bo.write(0x08); // encoding: 0x08 (GSM_UCS2) for UCS-2
                    bo.write(dateBytes);
                    bo.write(bodybytes);
                } catch (UnsupportedEncodingException uex) {
                    Log.e("_DEBUG_", String.format("String '%s' encode unknow", body));
                }
            }
//            Log.d("_DEBUG_", String.format("PDU: ", bytesToHexString(bo.toByteArray())));

            pdu = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Intent intent = new Intent();
            intent.setClassName("com.android.mms",
                    "com.android.mms.transaction.SmsReceiverService");
            intent.setAction("android.provider.Telephony.SMS_RECEIVED");
            intent.putExtra("pdus", new Object[]{pdu});
            intent.putExtra("format", "3gpp");
            context.startService(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static byte reverseByte(byte b) {
        return (byte) ((b & 0xF0) >> 4 | (b & 0x0F) << 4);
    }

    /**
     * Packs header and UCS-2 encoded message. Includes TP-UDL & TP-UDHL if
     * necessary
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] encodeUCS2(String message, byte[] header)
            throws UnsupportedEncodingException {
        byte[] userData, textPart;
        textPart = message.getBytes("utf-16be");

        if (header != null) {
            // Need 1 byte for UDHL
            userData = new byte[header.length + textPart.length + 1];

            userData[0] = (byte) header.length;
            System.arraycopy(header, 0, userData, 1, header.length);
            System.arraycopy(textPart, 0, userData, header.length + 1,
                    textPart.length);
        } else {
            userData = textPart;
        }
        byte[] ret = new byte[userData.length + 1];
        ret[0] = (byte) (userData.length & 0xff);
        System.arraycopy(userData, 0, ret, 1, userData.length);
        return ret;
    }

}
