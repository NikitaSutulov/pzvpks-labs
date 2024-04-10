package com.nikitasutulov.pzvpks.lab4.monitors;

public class SyncMonitor {
    private int F1 = 0;
    private int F2 = 0;
    private int F3 = 0;
    private int F4 = 0;
    private int F5 = 0;
    private int F6 = 0;
    private int F7 = 0;

    public synchronized void signalInput() {
        F1++;
        if (F1 == 3) {
            notifyAll();
        }
    }

    public synchronized void waitInput() throws InterruptedException {
        if (F1 != 3) {
            wait();
        }
    }

    public synchronized void signalCalcAh21() {
        F2++;
        if (F2 == 1) {
            notifyAll();
        }
    }

    public synchronized void waitCalcAh21() throws InterruptedException {
        if (F2 != 1) {
            wait();
        }
    }

    public synchronized void signalCalcAh43() {
        F3++;
        if (F3 == 1) {
            notifyAll();
        }
    }

    public synchronized void waitCalcAh43() throws InterruptedException {
        if (F3 != 1) {
            wait();
        }
    }

    public synchronized void signalCalcA2h() {
        F4++;
        if (F4 == 1) {
            notifyAll();
        }
    }

    public synchronized void waitCalcA2h() throws InterruptedException {
        if (F4 != 1) {
            wait();
        }
    }

    public synchronized void signalCalcA() {
        F5++;
        if (F5 == 1) {
            notifyAll();
        }
    }

    public synchronized void waitCalcA() throws InterruptedException {
        if (F5 != 1) {
            wait();
        }
    }

    public synchronized void signalCalca() {
        F6++;
        if (F6 == 4) {
            notifyAll();
        }
    }

    public synchronized void waitCalca() throws InterruptedException {
        if (F6 != 4) {
            wait();
        }
    }

    public synchronized void signalCalcV() {
        F7++;
        if (F7 == 3) {
            notifyAll();
        }
    }

    public synchronized void waitCalcV() throws InterruptedException {
        if (F7 != 3) {
            wait();
        }
    }
}
