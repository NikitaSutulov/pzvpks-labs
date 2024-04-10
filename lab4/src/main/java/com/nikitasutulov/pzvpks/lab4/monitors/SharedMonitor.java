package com.nikitasutulov.pzvpks.lab4.monitors;

public class SharedMonitor {
    private int a = 0;
    private int d;

    public synchronized int copya() {
        return a;
    }

    public synchronized int copyd() {
        return d;
    }

    public synchronized void writed(int value) {
        d = value;
    }

    public synchronized void calca(int ai) {
        a = a + ai;
    }
}
