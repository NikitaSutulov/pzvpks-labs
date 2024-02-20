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
        // повідомлення про початок виконання потоку T3
        System.out.println("Потік T3 почав виконання");

        // отримання вхідних даних
        S = Data.getArray("S", n, inputOption, 3);
        MO = Data.getMatrix("MO", n, inputOption, 3);
        MT = Data.getMatrix("MT", n, inputOption, 3);
        MS = Data.getMatrix("MS", n, inputOption, 3);
        MP = Data.getMatrix("MP", n, inputOption, 3);

        // обчислення функції F3
        s = Data.F3(S, MO, MT, MS, MP);

        // виведення результату
        if (n <= 1000) {
            Data.printNumber(s, "s");
        } else {
            Data.writeNumberToFile(s, "s");
        }

        // повідомлення про завершення виконання потоку T3
        System.out.println("Потік T3 завершив виконання");
    }
}
