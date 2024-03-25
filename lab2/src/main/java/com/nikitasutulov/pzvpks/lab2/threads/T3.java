package com.nikitasutulov.pzvpks.lab2.threads;

import com.nikitasutulov.pzvpks.lab2.Data;

import java.util.concurrent.BrokenBarrierException;

public class T3 extends Thread {
    private Data data;
    private int a3;
    private int d3;
    private int from;
    private int to;

    private int[] Zh; // Zн

    public T3(Data data) {
        this.data = data;
        from = 2 * data.H;
        to = 3 * data.H - 1;
    }

    @Override
    public void run() {
        try {
            // Повідомлення про початок виконання потоку T3
            System.out.println("Потік T3 почав виконання");

            // Введення MC
            for (int i = 0; i < data.N; i++) {
                for (int j = 0; j < data.N; j++) {
                    data.MC[i][j] = 1;
                }
            }

            // Введення MR
            for (int i = 0; i < data.N; i++) {
                for (int j = 0; j < data.N; j++) {
                    data.MR[i][j] = 1;
                }
            }

            // Сигнал потокам T1, T2, T4 про завершення введення даних
            // А також очікування завершення введення даних потоками T1, T4
            data.B1.await();

            // Обчислення1
            Zh = Data.getPartOfVector(data.Z, from, to);
            a3 = Data.getMaxElementOfVector(Zh);

            // Обчислення2
            // КД1
            // Захищений перезапис спільного ресурсу a із використанням атомарної змінної
            data.a.updateAndGet(a -> Math.max(a, a3));

            // Сигнал потокам T1, T2, T4 за допомогою семафора S3 про завершення обчислення 1 потоком T3
            data.S3.release(3);

            // Очікування завершення обчислення 2 у потоках T1, T2, T4 за допомогою семафорів S1, S2, S4
            data.S1.acquire();
            data.S2.acquire();
            data.S4.acquire();

            // Копіювання a3 = a
            // КД2, synchronized-метод CS1()
            a3 = data.CS1();

            // Копіювання d3 = d
            // КД3, synchronized-метод CS2()
            d3 = data.CS2();

            // Обчислення3 - обчислення H рядків матриці MU із записом результату у відповідне поле об'єкта data
            data.doThirdCalculation(a3, d3, from, to);

            // Повідомлення про завершення виконання потоку T3
            System.out.println("Потік T3 завершив виконання");

            // Сигнал потоку T1 про завершення обчислення 3 за допомогою семафора S5
            data.S5.release();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
