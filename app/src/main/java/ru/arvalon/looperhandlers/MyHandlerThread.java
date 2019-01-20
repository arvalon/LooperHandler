package ru.arvalon.looperhandlers;

import android.os.Handler;
import android.os.HandlerThread;

public class MyHandlerThread extends HandlerThread {

    Handler handler;

    public MyHandlerThread(String name) {
        super(name);
    }

    public void prepareHandler(){
        handler=new Handler(getLooper());
    }

    public void postTask(Runnable task){
        handler.post(task);
    }
}
