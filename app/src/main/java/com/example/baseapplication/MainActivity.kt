package com.example.baseapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseapplication.util.ListUtil
import com.example.baseapplication.util.LogUtil
import com.example.baseapplication.util.NetworkUtil
import okhttp3.ResponseBody

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: SampleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        LogUtil.set(BuildConfig.APPLICATION_ID, BuildConfig.DEBUG)
        LogUtil.traceFunc()

        adapter = SampleAdapter(this)
//        adapter = SampleAdapter(
//            this,
//            arrayListOf(
//                JiraIssue("dummy"),
//                JiraIssue("dumy"),
//                JiraIssue("dummy")
//            )
//        )

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

        findViewById<RecyclerView>(R.id.recycler_view).let {
            it.adapter = adapter

            // Normal List Layout. Use GridLayout Manager instead.
            it.layoutManager = LinearLayoutManager(this)

            // item decorlation
            it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            it.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        }
    }

    class SampleAdapter : ListUtil.Adapter<JiraIssue> {
        companion object {
            const val LAYOUT_ID = R.layout.sample_list_item
        }

        private val context: Context

        // TODO constructor looks messy
        constructor(context: Context) : super(LAYOUT_ID) {
            this.context = context
        }
        constructor(context: Context, initialData: ArrayList<JiraIssue>) : super(LAYOUT_ID, initialData) {
            this.context = context
        }

        override fun onBind(itemView: View, data: JiraIssue) {
            itemView.findViewById<TextView>(R.id.item_text1).also {
                it.text = data.key
            }

            itemView.findViewById<TextView>(R.id.item_text2).also{
                it.text = data.summary
            }

            if (getItemViewType(data) == VIEW_TYPE_TITLE) {
                itemView.setBackgroundColor(getColor(context, R.color.colorAccent))
            }
        }

        override fun getItemViewType(data: JiraIssue): Int {
            return when (data.issueType) {
                "Story" -> {
                    VIEW_TYPE_NORMAL
                }
                else -> {
                    VIEW_TYPE_TITLE
                }
            }
        }
    }

    class SampleCallListener(private val adapter: SampleAdapter) : NetworkUtil.ResultListener() {
        override fun onSuccess(responseBody: ResponseBody?) {
            super.onSuccess(responseBody)
            responseBody?.string()?.takeIf {
                it.isNotEmpty()
            }?.let {
                JiraIssue(it!!)
            }?.also {
                doInMainThread {
                    LogUtil.debug(it)
                    adapter.addData(it)
                }
            }
        }
    }

    private fun sampleCall() {
        LogUtil.traceFunc()

        val id = "<mail address>"
        val password = "<access token>"
        val url = "https://yuheik.atlassian.net/rest/api/2/issue/MYP-62"

        val storyPoint = 12
        val param = "{ \"fields\" : { \"customfield_10024\" : ${storyPoint.toFloat()} } }"

        NetworkUtil.callPut(url, id, password, param, SampleCallListener(adapter))
        NetworkUtil.callGet(url, id, password, SampleCallListener(adapter)) // Note that this call will be executed before first call is completed.
    }
}
