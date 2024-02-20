package com.nikitasutulov.pzvpks.lab1.threads;

import com.nikitasutulov.pzvpks.lab1.data.Data;

public class Thread1 extends Thread {
    private final int n;
    private final int inputOption;

    private int[] A, B, C, D, E;
    private int[][] MA, MD;

    public Thread1(int n, int inputOption) {
        super();
        this.n = n;
        this.inputOption = inputOption;
    }
    @Override
    public void run() {
        // повідомлення про початок виконання потоку T1
        System.out.println("Потік T1 почав виконання");

        // отримання вхідних даних
        A = Data.getArray("A", n, inputOption, 1);
        B = Data.getArray("B", n, inputOption, 1);
        C = Data.getArray("C", n, inputOption, 1);
        D = Data.getArray("D", n, inputOption, 1);

        MA = Data.getMatrix("MA", n, inputOption, 1);
        MD = Data.getMatrix("MD", n, inputOption, 1);

        // обчислення функції F1
        E = Data.F1(A, B, C, D, MA, MD);

        // виведення результату
        if (n <= 1000) {
            Data.printArray(E, "E");
        } else {
            Data.writeArrayToFile(E, "E");
        }

        // повідомлення про завершення виконання потоку T1
        System.out.println("Потік T1 завершив виконання");
    }
}
