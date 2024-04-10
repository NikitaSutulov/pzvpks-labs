package com.nikitasutulov.pzvpks.lab4.threads;

import com.nikitasutulov.pzvpks.lab4.Data;

public class T3 extends Thread {
    private Data data;
    private int a3;
    private int d3;
    private int from;
    private int to;
    private int toT4;
    private int[] Ah;

    public T3(Data data) {
        this.data = data;
        from = 2 * data.H;
        to = 3 * data.H - 1;
        toT4 = 4 * data.H - 1;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T3
        System.out.println("Потік T3 почав виконання");

        // Очікування завершення введення даних потоками T1, T2, T4     W1-1, W2-1, W4-1
        try {
            data.syncMon.waitInput();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Копіювання d3 = d
        // КД1
        d3 = data.shrMon.copyd();

        // Обчислення1
        Ah = Data.addTwoVectors( // d3 * Bн
                Data.multiplyVectorByScalar( // d3 * Bн
                        Data.getPartOfVector(data.B, from, to), // Bн
                        d3
                ),
                Data.multiplyVectorByMatrix( // Z * MMн
                        data.Z,
                        Data.getPartOfMatrixColumns(data.MM, from, to)) // MMн
        );
        Data.insertVectorPartIntoVector(Ah, data.A, from);
        Data.mergeSort(data.A, from, to);

        // Очікування завершення обчислення 1 потоком T4                W4-2
        try {
            data.syncMon.waitCalcAh43();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Обчислення2
        Data.merge(data.A, from, to, toT4);

        // Сигнал потоку T1 про завершення обчислення 2                 S1-1
        data.syncMon.signalCalcA2h();

        // Очікування завершення обчислення 3 потоком T1                W1-2
        try {
            data.syncMon.waitCalcA();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Обчислення 4
        a3 = Data.multiplyTwoVectors(
                Data.getPartOfVector(data.B, from, to),
                Data.getPartOfVector(data.Z, from, to)
        );

        // Обчислення 5
        // КД2
        data.shrMon.calca(a3);

        // Сигнал потокам T1, T2, T4 про завершення обчислення 5        S1-2, S2-1, S4-1
        data.syncMon.signalCalca();

        // Очікування завершення обчислення 5 потоками T1, T2, T4       W1-3, W2-2, W4-3
        try {
            data.syncMon.waitCalca();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Копіювання a3 = a
        // КД3
        a3 = data.shrMon.copya();

        // Обчислення 6
        data.doSixthCalculation(a3, from, to);

        // Сигнал потоку T2 про завершення обчислення 6                 S2-2
        data.syncMon.signalCalcV();

        // Повідомлення про завершення виконання потоку T3
        System.out.println("Потік T3 завершив виконання");
    }
}