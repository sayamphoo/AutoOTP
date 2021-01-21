package com.test.autootp

import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
//SMS Example
//Local Secure OTP: 237378 (Ref. DVOL) for your secured online payment. 07:36

        permission()
        val service = Service()
        service.smsServerListener = object : Service.SmsServerListener{
            override fun otpMessage(sms: String) {

                if (otp.text.isEmpty()){
                    otp.text.append(sms)
                }else{
                    otp.text.clear()
                    otp.text.append(sms)
                }

            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED")
        registerReceiver(service,intentFilter)
    }

    private fun permission(){
        val dd = Manifest.permission.RECEIVE_SMS
        val geund = ContextCompat.checkSelfPermission(this, dd)
        if (geund != PackageManager.PERMISSION_GRANTED) {
            val str = arrayOfNulls<String>(1)
            str[0] = dd
            ActivityCompat.requestPermissions(this, str, 1)
        }
    }
}
