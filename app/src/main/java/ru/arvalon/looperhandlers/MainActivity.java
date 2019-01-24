package ru.arvalon.looperhandlers;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * https://blog.nikitaog.me/2014/10/11/android-looper-handler-handlerthread-i/
 * https://blog.nikitaog.me/2014/10/18/android-looper-handler-handlerthread-ii/
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "vga";
    public static final String SEPARATOR = "=====================================================";
    static final String MIDDLE = "This is the middle of task";
    static final String END = "This is the end of task";

    MyThread myThread = new MyThread();
    public static MyMainHandler handler;

    MyHandlerThread handlerThread;

    Handler mainHandler;

    Runnable runnable1;
    Runnable runnable2;

    int run1_count, run2_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new MyMainHandler();

        mainHandler = new Handler();

        Message message = handler.obtainMessage();
        message.arg1 = 22;
        message.arg2 = 44;
        message.obj = new Object();

        handler.dispatchMessage(message);

        Messenger messenger = new Messenger(handler);

        Intent i = new Intent();
        //i.putExtra("MYHANDLER", handler);
        i.putExtra("MYMESSENGER", messenger);

        Message m = new Message();
        try {
            messenger.send(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        myThread.start();

        handlerThread = new MyHandlerThread("Secret name!");
        handlerThread.start();

        Message m2 = new Message();
        m2.arg1=444;

        //Looper looper = handlerThread.getLooper();

        /*final Handler handler = new Handler(looper){
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG,"HandlerThread Runnable, Thread.currentThread().getName(): "+Thread.currentThread().getName());
            }
        };*/

        //handler.obtainMessage();
        //handler.sendMessage(m2);
        //handlerThread.handler.dispatchMessage(m2);
        //

        //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent());

        Runnable run = () -> {
            for (int i1 = 0; i1 <5; i1++){

                SystemClock.sleep(200);

                if (i1 ==2){
                    handler.post(() -> {
                        Toast.makeText(getApplication(), MIDDLE, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, MIDDLE);
                    });
                }
            }

            handler.post(() -> {
                Toast.makeText(getApplication(), END, Toast.LENGTH_SHORT).show();
                Log.d(TAG, END);
            });
        };

        handlerThread.prepareHandler();
        handlerThread.postTask(run);
        handlerThread.postTask(run);

        //Handler stackTraceHandler = new StackTraceHandler(Looper.getMainLooper());
        //Message mes = stackTraceHandler.obtainMessage();
        //stackTraceHandler.sendMessage(mes);

        SimpleLooper simpleLooper = new SimpleLooper();
        simpleLooper.start();
        simpleLooper.waitForStart();

        Handler handler = new StackTraceHandler(simpleLooper.getLooper());

        Message mes2 = handler.obtainMessage();

        Message mes3 = Message.obtain(handler,1,2,3);

        Log.d(TAG,SEPARATOR);

        handler.sendMessage(mes2);
        handler.sendMessage(mes3);
    }

    public void postMessage(View view) {
        Message message = myThread.handler.obtainMessage();

        message.arg1=111;

        Log.d(TAG, "postMessage arg1 "+message.arg1 +" arg2 "+message.arg2);
        myThread.handler.dispatchMessage(message);

        message.arg2=222;
        myThread.handler2.dispatchMessage(message);

        message.arg2=333;
        myThread.handler2.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
    }

    public void postRunnable1(View view) {
        int number=run1_count++;

        runnable1=new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,getClass().getSimpleName()+" runnable1 count "+number);
            }
        };
        mainHandler.postDelayed(runnable1,3000);
    }

    public void postRunnable2(View view) {
        int number=run2_count++;

        runnable2=new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,getClass().getSimpleName()+" runnable2 count "+number);
            }
        };
        mainHandler.postDelayed(runnable2,3000);
    }

    public void removeRunnable1(View view) {
        mainHandler.removeCallbacks(runnable1);
    }

    public void removeRunnable2(View view) {
        mainHandler.removeCallbacks(runnable2);
    }

    static class MyMainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg2 == 55)
            {

            }
        }
    }

    static class StackTraceHandler extends Handler{

        StackTraceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, getClass().getSimpleName()
                    +" handleMessage dumpStack Thread id/name "+Thread.currentThread().getId()
                    +" "+Thread.currentThread().getName());
            Log.d(TAG,getClass().getSimpleName()
                    +" message what "+msg.what+" arg "+msg.arg1+" arg2 "+msg.arg2);
            Thread.dumpStack();
        }
    }
}



