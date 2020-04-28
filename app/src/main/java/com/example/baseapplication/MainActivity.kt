package com.example.baseapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.baseapplication.util.LogUtil
import com.example.baseapplication.util.NetworkUtil
import okhttp3.ResponseBody

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtil.set(BuildConfig.APPLICATION_ID, BuildConfig.DEBUG)
        LogUtil.traceFunc()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonMisc = findViewById<Button>(R.id.button_miscellaneous)
        buttonMisc.setOnClickListener {
            LogUtil.debug("Misc Button clicked")

            sampleCall()
        }

        val buttonIntent = findViewById<Button>(R.id.button_intent)
        buttonIntent.setOnClickListener {
            LogUtil.debug("Intent Button clicked")
            val intent = Intent(this, SecondScreenActivity::class.java)
            startActivity(intent)
        }
    }

    class SampleCallListener : NetworkUtil.ResultListener() {
        override fun onSuccess(responseBody: ResponseBody?) {
            super.onSuccess(responseBody)
            LogUtil.debug(responseBody?.string())            
        }
    }

    private fun sampleCall() {
        LogUtil.traceFunc()

        val id = "<mail address>"
        val password = "<access token>"
        val url = "https://yuheik.atlassian.net/rest/api/2/issue/MYP-62"

        val storyPoint = 12
        val param = "{ \"fields\" : { \"customfield_10024\" : ${storyPoint.toFloat()} } }"

        NetworkUtil.callPut(url, id, password, param, SampleCallListener())
        NetworkUtil.callGet(url, id, password, SampleCallListener()) // Note that this call will be executed before first call is completed.
    }
}
