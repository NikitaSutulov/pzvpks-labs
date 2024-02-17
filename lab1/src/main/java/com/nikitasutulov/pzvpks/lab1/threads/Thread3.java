package com.nikitasutulov.pzvpks.lab1.threads;

import com.nikitasutulov.pzvpks.lab1.data.Data;

public class Thread3 extends Thread {
    private final int n;
    private final int inputOption;

    private int s;
    private int[] S;
    private int[][] MO, MT, MS, MP;

    public Thread3(int n, int inputOption) {
        super();
        this.n = n;
        this.inputOption = inputOption;
    }
    @Override
    public void run() {
        System.out.println("Потік T3 почав виконання");

        S = Data.getArray("S", n, inputOption, 3);
        MO = Data.getMatrix("MO", n, inputOption, 3);
        MT = Data.getMatrix("MT", n, inputOption, 3);
        MS = Data.getMatrix("MS", n, inputOption, 3);
        MP = Data.getMatrix("MP", n, inputOption, 3);

        s = Data.F3(S, MO, MT, MS, MP);

        if (n < 1000) {
            Data.printNumber(s, "s");
        } else {
            Data.writeNumberToFile(s, "s");
        }

        System.out.println("Потік T3 завершив виконання");
    }
}
