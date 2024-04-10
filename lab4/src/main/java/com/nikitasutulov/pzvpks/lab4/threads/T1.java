package com.nikitasutulov.pzvpks.lab4.threads;

import com.nikitasutulov.pzvpks.lab4.Data;

public class T1 extends Thread {
    private Data data;
    private int a1;
    private int d1;
    private int from;
    private int to;
    private int toT2;
    private int toT4;
    private int[] Ah;

    public T1(Data data) {
        this.data = data;
        from = 0;
        to = data.H - 1;
        toT2 = 2 * data.H - 1;
        toT4 = 4 * data.H - 1;
    }

    @Override
    public void run() {
        // Повідомлення про початок виконання потоку T1
        System.out.println("Потік T1 почав виконання");

        // Введення MM
        for (int i = 0; i < data.N; i++) {
            for (int j = 0; j < data.N; j++) {
                data.MM[i][j] = 1;
            }
        }

        // Введення B
        for (int i = 0; i < data.N; i++) {
            data.B[i] = 1;
        }
        data.B[0] = 4;
        data.B[1] = 2;
        data.B[2] = 3; // Встановлення для деяких елементів B значень, відмінних від 1, для перевірки правильності сортування

        // Введення MX
        for (int i = 0; i < data.N; i++) {
            for (int j = 0; j < data.N; j++) {
                data.MX[i][j] = 1;
            }
        }

        // Сигнал потокам T2, T3, T4 про завершення введення даних          S2-1, S3-1, S4-1
        data.syncMon.signalInput();
        // А також очікування завершення введення даних потоками T2, T4     W2-1, W4-1
        try {
            data.syncMon.waitInput();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Копіювання d1 = d
        // КД1
        d1 = data.shrMon.copyd();

        // Обчислення1
        Ah = Data.addTwoVectors( // d1 * Bн
                Data.multiplyVectorByScalar( // d1 * Bн
                        Data.getPartOfVector(data.B, from, to), // Bн
                        d1
                ),
                Data.multiplyVectorByMatrix( // Z * MMн
                        data.Z,
                        Data.getPartOfMatrixColumns(data.MM, from, to)) // MMн
        );
        Data.insertVectorPartIntoVector(Ah, data.A, from);
        Data.mergeSort(data.A, from, to);

        // Очікування завершення обчислення 1 потоком T2                    W2-2
        try {
            data.syncMon.waitCalcAh21();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Перевірка правильності сортування Aн для N <= 16
        if (data.N <= 16) {
            System.out.print("T1: вектор A (відсортовано чверті): ");
            Data.printVector(data.A);
        }

        // Обчислення2
        Data.merge(data.A, from, to, toT2);


        // Очікування завершення обчислення 2 потоком T3                    W3-1
        try {
            data.syncMon.waitCalcA2h();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Перевірка правильності сортування A2н для N <= 16
        if (data.N <= 16) {
            System.out.print("T1: вектор A (відсортовано половини): ");
            Data.printVector(data.A);
        }

        // Обчислення3
        Data.merge(data.A, from, toT2, toT4);

        // Перевірка правильності сортування A для N <= 16
        if (data.N <= 16) {
            System.out.print("T1: вектор A (відсортовано повністю): ");
            Data.printVector(data.A);
        }

        // Сигнал потокам T2, T3, T4 про завершення обчислення 3            S2-2, S3-2, S4-2
        data.syncMon.signalCalcA();

        // Обчислення 4
        a1 = Data.multiplyTwoVectors(
                Data.getPartOfVector(data.B, from, to),
                Data.getPartOfVector(data.Z, from, to)
        );

        // Обчислення 5
        // КД2
        data.shrMon.calca(a1);

        // Сигнал потокам T2, T3, T4 про завершення обчислення 5            S2-3, S3-3, S4-3
        data.syncMon.signalCalca();

        // Очікування завершення обчислення 5 потоками T2, T3, T4           W2-3, W3-2, W4-2
        try {
            data.syncMon.waitCalca();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Копіювання a1 = a
        // КД3
        a1 = data.shrMon.copya();

        // Обчислення 6
        data.doSixthCalculation(a1, from, to);

        // Сигнал потоку T2 про завершення обчислення 6                     S2-4
        data.syncMon.signalCalcV();

        // Повідомлення про завершення виконання потоку T1
        System.out.println("Потік T1 завершив виконання");
    }
}
