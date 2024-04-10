package com.nikitasutulov.pzvpks.lab4.threads;

import com.nikitasutulov.pzvpks.lab4.Data;

public class T4 extends Thread {
    private Data data;
    private int a4;
    private int d4;
    private int from;
    private int to;
    private int[] Ah;

    public T4(Data data) {
        this.data = data;
        from = 3 * data.H;
        to = 4 * data.H - 1;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T4
        System.out.println("Потік T4 почав виконання");

        // Введення MT
        for (int i = 0; i < data.N; i++) {
            for (int j = 0; j < data.N; j++) {
                data.MT[i][j] = 1;
            }
        }

        // Введення d
        data.shrMon.writed(1);

        // Сигнал потокам T1, T2, T3 про завершення введення даних      S1-1, S2-1, S3-1
        data.syncMon.signalInput();
        // Очікування завершення введення даних потоками T1, T2, T3     W1-1, W2-1
        try {
            data.syncMon.waitInput();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Копіювання d4 = d
        // КД1
        d4 = data.shrMon.copyd();

        // Обчислення1
        Ah = Data.addTwoVectors( // d4 * Bн
                Data.multiplyVectorByScalar( // d4 * Bн
                        Data.getPartOfVector(data.B, from, to), // Bн
                        d4
                ),
                Data.multiplyVectorByMatrix( // Z * MMн
                        data.Z,
                        Data.getPartOfMatrixColumns(data.MM, from, to)) // MMн
        );
        Data.insertVectorPartIntoVector(Ah, data.A, from);
        Data.mergeSort(data.A, from, to);

        // Сигнал потоку T3 про завершення обчислення 1                 S3-2
        data.syncMon.signalCalcAh43();

        // Очікування завершення обчислення 3 потоком T1                W1-2
        try {
            data.syncMon.waitCalcA();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Обчислення 4
        a4 = Data.multiplyTwoVectors(
                Data.getPartOfVector(data.B, from, to),
                Data.getPartOfVector(data.Z, from, to)
        );

        // Обчислення 5
        // КД2
        data.shrMon.calca(a4);

        // Сигнал потокам T1, T2, T3 про завершення обчислення 5        S1-2, S2-2, S3-3
        data.syncMon.signalCalca();

        // Очікування завершення обчислення 5 потоками T1, T2, T3       W1-3, W2-2, W3-1
        try {
            data.syncMon.waitCalca();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Копіювання a4 = a
        a4 = data.shrMon.copya();

        // Обчислення 6
        data.doSixthCalculation(a4, from, to);

        // Сигнал потоку T2 про завершення обчислення 6                 S2-3
        data.syncMon.signalCalcV();

        // Повідомлення про завершення виконання потоку T4
        System.out.println("Потік T4 завершив виконання");
    }
}
