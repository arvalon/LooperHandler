package ru.arvalon.looperhandlers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import static ru.arvalon.looperhandlers.MainActivity.TAG;

public class MyThread extends Thread {

    public MyHandler handler;
    public MyHandler2 handler2;

    @Override
    public void run() {
        Looper.prepare();
        handler = new MyHandler();
        handler2 = new MyHandler2();
        Log.d(TAG, getClass().getSimpleName()
                +" run Thread id/name "+Thread.currentThread().getId()
                +" "+Thread.currentThread().getName());
        Looper.loop();
    }

    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, getClass().getSimpleName()
                    +" handleMessage Thread id/name "+Thread.currentThread().getId()
                    +" "+Thread.currentThread().getName()+" arg1 "+msg.arg1 +" arg2 "+msg.arg2);
        }
    }

    static class MyHandler2 extends Handler {
        @Override
        public void handleMessage(Message msg) {

            Log.d(TAG, getClass().getSimpleName()
                    +" handleMessage Thread id/name "+Thread.currentThread().getId()
                    +" "+Thread.currentThread().getName()+" arg1 "+msg.arg1 +" arg2 "+msg.arg2);
        }
    }
}