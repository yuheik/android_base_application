package com.example.baseapplication.util

import android.os.Handler
import android.os.Looper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class NetworkUtil {
    open class ResultListener() {
        open fun onSuccess(responseBody : ResponseBody?) {
            LogUtil.debug("Success!!")
        }

        open fun onFailure() {
            LogUtil.debug("Fail!!")
        }

        /**
         * Note that callback is called on worker thread.
         * Some operations such as UI update must run on main thread otherwise app will crash.
         */
        internal fun doInMainThread(method: () -> Unit) {
            Handler(Looper.getMainLooper()).post {
                method()
            }
        }
    }

    companion object {
        fun callPut(url : String, id : String, password : String, param : String, listener : ResultListener?) {
            callApi(createPutRequest(url, id, password, param), listener)
        }

        fun callGet(url : String, id : String, password : String, listener: ResultListener?) {
            callApi(createGetRequest(url, id, password), listener)
        }

        private fun createPutRequest(url : String, id : String, password : String, paramJsonString : String): Request {
            LogUtil.traceFunc()

            val body =
                paramJsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            return Request.Builder()
                .url(url)
                .header("Authorization", Credentials.basic(id, password))
                .put(body)
                .build()
        }

        private fun createGetRequest(url : String, id : String, password : String): Request {
            LogUtil.traceFunc()
            return Request.Builder()
                .url(url)
                .header("Authorization", Credentials.basic(id, password))
                .build()
        }

        private fun callApi(request: Request, listener: ResultListener?) {
            LogUtil.traceFunc()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    LogUtil.traceFunc(call.request().toString())
                    LogUtil.error(e)
                    listener?.onFailure()
                }

                override fun onResponse(call: Call, response: Response) {
                    LogUtil.traceFunc(call.request().toString())
                    //dumpSuccess(call, response)

                    if (response.isSuccessful) {
                        listener?.onSuccess(response.body)
                    } else {
                        LogUtil.error("status code: ${response.code}")
                        LogUtil.error(response.body?.string())
                        listener?.onFailure()
                    }
                }
            })
        }

        private fun dumpSuccess(call: Call, response: Response) {
            LogUtil.debug(call.request().body?.contentType().toString())
            LogUtil.debug(response.toString())
            LogUtil.debug("header:", response.headers.toString())
            LogUtil.debug("body:", response.body!!.string())
        }
    }
}
