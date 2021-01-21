package com.test.autootp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import java.util.regex.Pattern

class Service : BroadcastReceiver() {

    lateinit var smsServerListener: SmsServerListener

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent?.extras
        if (intent!!.action.equals("android.provider.Telephony.SMS_RECEIVED")){
            Log.d("00182",intent!!.action!!)
            Log.d("00182",intent.data.toString())
            val pdu = bundle!!["pdus"] as Array<*>
            val sms = arrayOfNulls<SmsMessage>(pdu.size)
            val format = bundle.getString("format")

            for (i in pdu.indices){
                sms[i] = SmsMessage.createFromPdu(pdu[i] as ByteArray,format)
            }

            var smsBody = ""

            for (i in sms.indices){
                smsBody += sms[i]!!.messageBody
            }

            smsServerListener.otpMessage(getOtp(smsBody))
        }


    }

    private fun getOtp(s:String) : String{
        val pattern = "(.+)([0-9]{6})"
        val compiler = Pattern.compile(pattern)
        val matcher = compiler.matcher(s)
        return if (matcher.find()) {
            matcher.group(2)!!
        } else {
            ""
        }

    }

    interface SmsServerListener{
        fun otpMessage(sms:String)
    }
}