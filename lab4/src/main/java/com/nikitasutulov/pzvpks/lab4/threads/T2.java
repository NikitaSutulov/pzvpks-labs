package com.nikitasutulov.pzvpks.lab4.threads;

import com.nikitasutulov.pzvpks.lab4.Data;

public class T2 extends Thread {
    private Data data;
    private int a2;
    private int d2;
    private int from;
    private int to;
    private int[] Ah;

    public T2(Data data) {
        this.data = data;
        from = data.H;
        to = 2 * data.H - 1;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T2
        System.out.println("Потік T2 почав виконання");

        // Фіксація часу початку виконання програми
        double startTime = (double) System.nanoTime() / 1000000000F;

        // Введення Z
        for (int i = 0; i < data.N; i++) {
            data.Z[i] = 1;
        }

        // Сигнал потокам T1, T3, T4 про завершення введення даних          S2-1, S3-1, S4-1
        data.syncMon.signalInput();
        // А також очікування завершення введення даних потоками T1, T4     W1-1, W4-1
        try {
            data.syncMon.waitInput();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Копіювання d2 = d
        // КД1
        d2 = data.shrMon.copyd();

        // Обчислення1
        Ah = Data.addTwoVectors( // d2 * Bн
                Data.multiplyVectorByScalar( // d2 * Bн
                        Data.getPartOfVector(data.B, from, to), // Bн
                        d2
                ),
                Data.multiplyVectorByMatrix( // Z * MMн
                        data.Z,
                        Data.getPartOfMatrixColumns(data.MM, from, to)) // MMн
        );
        Data.insertVectorPartIntoVector(Ah, data.A, from);
        Data.mergeSort(data.A, from, to);

        // Сигнал потоку T1 про завершення обчислення 1                     S1-2
        data.syncMon.signalCalcAh21();

        // Очікування завершення обчислення 3 потоком T1                    W1-2
        try {
            data.syncMon.waitCalcA();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Обчислення 4
        a2 = Data.multiplyTwoVectors(
                Data.getPartOfVector(data.B, from, to),
                Data.getPartOfVector(data.Z, from, to)
        );

        // Обчислення 5
        // КД2
        data.shrMon.calca(a2);

        // Сигнал потокам T1, T3, T4 про завершення обчислення 5            S1-3, S3-2, S4-2
        data.syncMon.signalCalca();

        // Очікування завершення обчислення 5 потоками T1, T3, T4           W1-3, W3-1, W4-2
        try {
            data.syncMon.waitCalca();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Копіювання a2 = a
        // КД3
        a2 = data.shrMon.copya();

        // Обчислення 6
        data.doSixthCalculation(a2, from, to);

        // Очікування завершення обчислення 6 потоками T1, T3, T4           W1-4, W3-2, W4-3
        try {
            data.syncMon.waitCalcV();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Виведення V
        Data.printVector(data.V);

        // Повідомлення про завершення виконання потоку T2
        System.out.println("Потік T2 завершив виконання");

        // Фіксація часу закінчення виконання програми
        // Обчислення часу роботи програми і його виведення
        double finishTime = (double) System.nanoTime() / 1000000000F;
        System.out.println("Час виконання програми склав " + String.format("%.2f с", finishTime - startTime));
    }
}
