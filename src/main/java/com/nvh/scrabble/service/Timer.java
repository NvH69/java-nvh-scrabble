package com.nvh.scrabble.service;

import com.nvh.scrabble.model.Dictionary;

import java.io.Serializable;
import java.util.Calendar;

public class Timer extends Thread implements Serializable {
    private static final long serialVersionUID = -6290616549583735992L;
    private boolean isOn;

    public boolean isOn() {
        return isOn;
    }

    private long dt;
    private final long dtReset;

    public static boolean isTicking = false;
    public String display;

    public static StringBuffer tempLcd = new StringBuffer("");
    public static Calendar calendar;

    public Timer(long dt) {
        this.dt = dt + 1000 + System.currentTimeMillis();
        this.dtReset = dt + 1000;
        this.isOn = false;
        setDaemon(true);
        setPriority(Thread.MIN_PRIORITY);
        calendar = Calendar.getInstance();

        start();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isOn) {
                tempLcd.delete(0, tempLcd.length());
                long nowTimer = System.currentTimeMillis();
                calendar.setTimeInMillis(this.dt - nowTimer);
                if (calendar.getTimeInMillis() < 20000 && !isTicking)
                    new SampledSound(Dictionary.path + "/sounds/timer_short.wav").play();
                if (calendar.getTimeInMillis() < 0) {
                    tempLcd.delete(0, 7);
                    //jouer son
                    isTicking = true;
                    new SampledSound(Dictionary.path + "/sounds/ding.wav").play();
                }
                if (calendar.get(Calendar.MINUTE) > 9) {
                    tempLcd.append(calendar.get(Calendar.MINUTE)).append(":").append(calendar.get(Calendar.SECOND));
                    tempLcd.insert(1, ' ');
                } else
                    tempLcd.append("0").append(calendar.get(Calendar.MINUTE)).append(":").append(calendar.get(Calendar.SECOND));
                if (calendar.get(Calendar.SECOND) < 10) tempLcd.insert(3, "0");

                this.display = tempLcd.toString();
                if (calendar.getTimeInMillis() < 0) {
                    isOn = false;
                    isTicking = false;
                }

            } else this.display = "  :  ";

        }
    }

    public String getDisplay() {
        return display;
    }

    public void arret() {
        if (this.isOn) this.isOn = false;
    }

    public void reset() {
        dt = dtReset + System.currentTimeMillis();
        isOn = true;
        isTicking = false;
    }

}
