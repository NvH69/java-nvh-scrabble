package com.nvh.controller;

import com.nvh.model.Dictionnaire;
import com.nvh.model.SampledSon;

import java.io.Serializable;
import java.util.Calendar;

public class Timer extends Thread implements Serializable {
    private static final long serialVersionUID = -6290616549583735992L;
    private boolean keepCounting;

    public boolean isKeepCounting() {
        return keepCounting;
    }

    public void setKeepCounting(boolean keepCounting) {
        this.keepCounting = keepCounting;
    }

    private long dt;
    private final long dtreset;

    private static long nowTimer;
    public static boolean flagtictac = false;
    public String lcd;

    public static StringBuffer tempLcd = new StringBuffer("");
    public static Calendar c;

    public Timer(long dt) {
        this.dt = dt + 1000 + System.currentTimeMillis();
        this.dtreset = dt + 1000;
        this.keepCounting = false;
        setDaemon(true);
        setPriority(Thread.MIN_PRIORITY);
        c = Calendar.getInstance();

        start();
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (keepCounting) {
                tempLcd.delete(0, tempLcd.length());
                nowTimer = System.currentTimeMillis();
                c.setTimeInMillis(this.dt - nowTimer);
                if (c.getTimeInMillis() < 20000 && !flagtictac)
                    new SampledSon(Dictionnaire.chemin + "/sons/timer_short.wav").play();
                if (c.getTimeInMillis() < 0) {
                    tempLcd.delete(0, 7);
                    //jouer son
                    flagtictac = true;
                    new SampledSon(Dictionnaire.chemin + "/sons/ding.wav").play();
                }
                if (c.get(Calendar.MINUTE) > 9) {
                    tempLcd.append(c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
                    tempLcd.insert(1, ' ');
                } else tempLcd.append("0" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
                if (c.get(Calendar.SECOND) < 10) tempLcd.insert(3, "0");

                this.lcd = tempLcd.toString();
                if (c.getTimeInMillis() < 0) {
                    keepCounting = false;
                    flagtictac = false;
                }

            } else this.lcd = "  :  ";

        }
    }

    public String getLcd() {
        return lcd;
    }


    public void setLcd(String lcd) {
        this.lcd = lcd;
    }

    public void arret() {
        if (this.keepCounting) this.keepCounting = false;
    }

    public void reset() {
        dt = dtreset + System.currentTimeMillis();
        keepCounting = true;
        flagtictac = false;
    }

}
