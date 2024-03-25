package com.nikitasutulov.pzvpks.lab1.threads;

import com.nikitasutulov.pzvpks.lab1.data.Data;

public class T2 extends Thread {
    private final int n;
    private final int inputOption;

    private int[][] MF, MG, MH, ML;

    public T2(int n, int inputOption) {
        super();
        this.n = n;
        this.inputOption = inputOption;
    }
    @Override
    public void run() {
        // повідомлення про початок виконання потоку T2
        System.out.println("Потік T2 почав виконання");

        // отримання вхідних даних
        MG = Data.getMatrix("MG", n, inputOption, 2);
        MH = Data.getMatrix("MH", n, inputOption, 2);
        ML = Data.getMatrix("ML", n, inputOption, 2);

        // обчислення функції F2
        MF = Data.F2(MG, MH, ML);

        // виведення результату
        if (n <= 1000) {
            Data.printMatrix(MF, "MF");
        } else {
            Data.writeMatrixToFile(MF, "MF");
        }

        // повідомлення про завершення виконання потоку T2
        System.out.println("Потік T2 завершив виконання");
    }
}
