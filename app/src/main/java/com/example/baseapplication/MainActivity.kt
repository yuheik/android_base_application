package com.example.baseapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.baseapplication.util.LogUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtil.set(BuildConfig.APPLICATION_ID, BuildConfig.DEBUG)
        LogUtil.traceFunc()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonMisc = findViewById<Button>(R.id.button_miscellaneous)
        buttonMisc.setOnClickListener {
            LogUtil.debug("Misc Button clicked")
        }
    }
}
