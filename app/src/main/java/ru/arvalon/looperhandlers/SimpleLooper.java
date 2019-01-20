package ru.arvalon.looperhandlers;

import android.os.Looper;

public class SimpleLooper extends Thread {

    boolean started=false;
    Object startMonitor = new Object();
    Looper threadLooper = null;

    @Override
    public void run() {
        Looper.prepare();

        threadLooper=Looper.myLooper();

        synchronized (startMonitor){
            started=true;
            startMonitor.notifyAll();
        }

        Looper.loop();
    }

    public Looper getLooper() {
        return threadLooper;
    }

    void waitForStart(){
        synchronized (startMonitor){
            while (!started){
                try {
                    startMonitor.wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
