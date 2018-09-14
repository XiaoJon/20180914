package com.hjhq.teamface.basis.util.log;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 有边框样式的Log日志
 * Created by Administrator on 2017/7/30.
 */

public class LogRedirector {
    private static volatile Logger sLogger;

    /**
     * Override Android's default {@link Log} API with a custom logger interface.  This affects
     * all subsequent calls to {@link LogRedirector} APIs.
     */
    public static void setLogger(Logger logger) {
        if (logger == null) {
            throw new NullPointerException();
        }
        if (sLogger != null) {
            throw new IllegalStateException();
        }
        sLogger = logger;
    }

    public static void e(String tag, String message, Throwable t) {
        e(tag, message + "\n" + formatThrowable(t));
    }

    public static void e(String tag, String message) {
        log(Log.ERROR, tag, message);
    }

    public static void w(String tag, String message, Throwable t) {
        w(tag, message + "\n" + formatThrowable(t));
    }

    public static void w(String tag, String message) {
        log(Log.WARN, tag, message);
    }

    public static void i(String tag, String message, Throwable t) {
        i(tag, message + "\n" + formatThrowable(t));
    }

    public static void i(String tag, String message) {
        log(Log.INFO, tag, message);
    }

    public static void d(String tag, String message, Throwable t) {
        d(tag, message + "\n" + formatThrowable(t));
    }

    public static void d(String tag, String message) {
        log(Log.DEBUG, tag, message);
    }

    public static void v(String tag, String message, Throwable t) {
        v(tag, message + "\n" + formatThrowable(t));
    }

    public static void v(String tag, String message) {
        log(Log.VERBOSE, tag, message);
    }

    private static String formatThrowable(Throwable t) {
        StringWriter buf = new StringWriter();
        PrintWriter writer = new PrintWriter(buf);
        t.printStackTrace();
        writer.flush();
        return buf.toString();
    }

    private static void log(int priority, String tag, String message) {
        Logger logger = sLogger;
        if (logger != null) {
            logger.log(priority, tag, message);
        } else {
            Log.println(priority, tag, message + "<");
        }
    }

    public static boolean isLoggable(String tag, int priority) {
        Logger logger = sLogger;
        if (logger != null) {
            return logger.isLoggable(tag, priority);
        } else {
            return Log.isLoggable(tag, priority);
        }
    }

    /**
     * Custom logger implementation that can forward logs to something other than Android's
     * {@link Log} API.
     */
    public interface Logger {
        /**
         * Allows the caller to query if a particular tag and priority will be logged.  Note that just
         * like with {@link Log#isLoggable}, callers are required to consult this manually if the
         * result is to be considered.
         */
        boolean isLoggable(String tag, int priority);

        /**
         * Submit a log message.
         */
        void log(int priority, String tag, String message);
    }
}
