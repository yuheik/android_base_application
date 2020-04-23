package com.example.baseapplication.util;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.baseapplication.BuildConfig;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LogUtil {
    private static String APP_TAG = "<application name not set>";

    private static boolean ENABLE_DEBUG_LOG = false;
    private static boolean ENABLE_ERROR_LOG = false;

    /**
     * Setup LogUtil behaviour
     *
     * @param appTag Application Name for logcat. Set BuildConfig.APPLICATION_ID value as default.
     * @param isDebug Switch enable/disable log output. Set BuildConfig.DEBUG value as default.
     */
    public static void set(String appTag, boolean isDebug) {
        APP_TAG = appTag;
        ENABLE_DEBUG_LOG = isDebug;
        ENABLE_ERROR_LOG = isDebug;
    }

    public static void debug(final Object... objs) {
        for (Object obj : objs) {
            logD(getTag(), obj.toString());
        }
    }

    public static void debug(final String... msgs) {
        for (String msg : msgs) {
            logD(getTag(), msg);
        }
    }

    public static void error(final String... msgs) {
        for (String msg : msgs) {
            logE(getTag(), msg);
        }
    }

    public static void error(final Exception e) {
        logE(getTag(), getFuncName() + "()");
        dumpException(e);
    }

    public static void error(final String msg, final Exception e) {
        logE(getTag(), getFuncName() + "()");
        logE(getTag(), msg);
        dumpException(e);
    }

    private static void dumpException(final Exception e) {
        logE(APP_TAG, "Error Message : " + e.getMessage());
        errorStackTrace(e);
    }

    private static void errorStackTrace(final Exception e) {
        logE(APP_TAG, "Stacktrace:");

        int index = 0;
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            logE(APP_TAG, "[" + index + "] " + stackTraceElement.toString());
            index++;
        }
    }

    public static void traceFunc() {
        logD(getTag(), getFuncName() + "()");
    }

    public static void traceFunc(final String... params) {
        logD(getTag(), getFuncName() + "() ");

        for (String param : params) {
            logD(getTag(), "  " + param);
        }
    }

    public static void traceCallerFunc() {
        logD(getTag(), getCallerFuncName() + "()");
    }

    public static void traceCallerFunc(final String... params) {
        logD(getTag(), getCallerFuncName() + "() ");

        for (String param : params) {
            logD(getTag(), "  " + param);
        }
    }

    public static void dumpIntent(final Intent intent) {
        if (intent == null) {
            logD(getTag(), "intent is null");
        } else {
            logD(getTag(),
                 "dump intent"                            + "\n" +
                 "  action   : " + intent.getAction()     + "\n" +
                 "  category : " + intent.getCategories() + "\n" +
                 "  package  : " + intent.getPackage()    + "\n" +
                 "  data     : " + intent.getData()       + "\n" +
                 "  extra    : " + intent.getExtras()     + "\n");
        }
    }

    public static void dumpBundle(final Bundle bundle) {
        String data_str = "";

        for (String key : bundle.keySet()) {
            data_str += "  " + key + " : " + bundle.get(key).toString() + "\n";
        }

        logD(getTag(), "dump bundle\n" + data_str);
    }

    public static void dumpCursor(final Cursor cursor) {
        for (String column_name : cursor.getColumnNames()) {
            int column_index = cursor.getColumnIndex(column_name);

            switch (cursor.getType(column_index)) {
            case Cursor.FIELD_TYPE_NULL:
                logD(getTag(), column_name + " : " + "Null");
                break;
            case Cursor.FIELD_TYPE_INTEGER:
                logD(getTag(), column_name + " : " + cursor.getInt(column_index));
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                logD(getTag(), column_name + " : " + cursor.getFloat(column_index));
                break;
            case Cursor.FIELD_TYPE_STRING:
                logD(getTag(), column_name + " : " + cursor.getString(column_index));
                break;
            case Cursor.FIELD_TYPE_BLOB:
                logD(getTag(), column_name + " : " + "BLOB");
                break;
            default:
                logD(getTag(), column_name + " : " + "unknown type");
                break;
            }
        }
    }

    public static void dumpObject(String title, Object object) {
        logD(getTag(), "dump object : " + title);
        try {
            logD(getTag(), new JSONObject(new Gson().toJson(object)).toString(4));
        } catch (JSONException e) {
            logE(getTag(), "dumpObjectAsJson() failed.");
        }
    }

    private static void logE(final String tag, final String msg) {
        if (ENABLE_ERROR_LOG) {
            Log.e(tag, msg);
        }
    }

    private static void logD(final String tag, final String msg) {
        if (ENABLE_DEBUG_LOG) {
            Log.d(tag, msg);
        }
    }

    private static String getTag() {
        return APP_TAG + "::" + getCallerClassName();
    }

    private static String getFuncName() {
        return getCaller(3).getMethodName();
    }

    private static String getCallerFuncName() {
        return getCaller(4).getMethodName();
    }

    private static String trimPackageName(String className) {
        return className.substring(className.lastIndexOf(".")+1);
    }

    private static String getCallerClassName() {
        return trimPackageName(getCaller(4).getClassName());
    }

    /**
     * @param depth : depends on the call hierarchy how StackTraceElement is used.
     */
    private static StackTraceElement getCaller(int depth) {
        StackTraceElement[] ste = (new Throwable()).getStackTrace();

        if (ste.length >= (depth + 1)) {
            return ste[depth];
        } else {
            return ste[ste.length - 1];
        }
    }
}
