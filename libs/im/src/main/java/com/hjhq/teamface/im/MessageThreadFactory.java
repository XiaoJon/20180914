package com.hjhq.teamface.im;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

/**
 * @author Administrator
 * @date 2017/11/22
 * Describe：
 */

public class MessageThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(@NonNull Runnable r) {
        return new Thread(r, "MessageThread");

    }
}
