package com.hypertian.library.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.hypertian.library.HTConfig;

import java.io.File;
import java.util.Locale;

/**
 * Created by hypertian on 2016/8/12.
 * Desc Log日志工具类
 */

public class HTLog {

    public static String customTag = "hypertian";//自定义tag前缀

    public static String logFileName = "hypertian";//保存Log的文件路径名

    private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath()
            + File.separator + logFileName + File.separator + "log" + File.separator;//获取SD卡的根目录


    /*私有化构造函数*/
    private HTLog() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void d(String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.d(generateTag(), msg);
        }
    }

    public static void d(String Tag, String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.d(Tag, msg);
        }
    }

    public static void i(String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.i(generateTag(), msg);
        }
    }

    public static void i(String Tag, String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.i(Tag, msg);
        }
    }

    public static void e(String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.e(generateTag(), msg);
        }
    }

    public static void e(String Tag, String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.e(Tag, msg);
        }
    }

    public static void w(String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.w(generateTag(), msg);
        }
    }

    public static void w(String Tag, String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.w(Tag, msg);
        }
    }

    public static void v(String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.v(generateTag(), msg);
        }
    }

    public static void v(String Tag, String msg) {
        if (HTConfig.__IS_OPEN_LOG) {
            Log.v(Tag, msg);
        }
    }

    /*==================        以下为自动生成TAG        =================*/
    private static String generateTag() {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = "%s.%s(Line:%d)";//返回tag格式的占位符,类名.方法名(Line:行数)
        //获取类名
        String callerClassName = caller.getClassName();
        //获取方法名
        String callerMethodName = caller.getMethodName();
        //格式化tag
        tag = String.format(Locale.US, tag, callerClassName, callerMethodName, caller.getLineNumber());
        tag = TextUtils.isEmpty(customTag) ? tag : customTag + ":" + tag;
        return tag;
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[5];
    }
}
