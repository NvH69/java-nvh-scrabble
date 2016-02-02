package com.nvh.controller;

public class Chrono

{
    private long dx;
    private boolean on;

    public Chrono(long dx, boolean on, boolean ring) {
        this.dx = dx;
        this.on = on;
    }

    public void begin() {
        on = true;
        dx = System.currentTimeMillis();
    }

    public void set(long x) {
        dx = x;
        on = false;
    }

    public void stop() {
        dx = 0;
        on = false;
    }

    public long get() {
        if (!on) return dx;
        long now = System.currentTimeMillis();

        return now - dx;
    }

    public String getLCD() {
        // � faire
        return " SET ";
    }
}