package com.nikitasutulov.pzvpks.lab2.threads;

import com.nikitasutulov.pzvpks.lab2.Data;

import java.util.concurrent.BrokenBarrierException;

public class T4 extends Thread {
    private Data data;
    private int a4;
    private int d4;
    private int from;
    private int to;

    private int[] Zh; // Zн

    public T4(Data data) {
        this.data = data;
        from = 3 * data.H;
        to = 4 * data.H - 1;
    }

    @Override
    public void run() {
        try {
            // Повідомлення про початок виконання потоку T4
            System.out.println("Потік T4 почав виконання");

            // Введення Z
            for (int i = 0; i < data.N; i++) {
                data.Z[i] = 1;
            }

            // Введення MD
            for (int i = 0; i < data.N; i++) {
                for (int j = 0; j < data.N; j++) {
                    data.MD[i][j] = 1;
                }
            }

            // Сигнал потокам T1, T2, T3 про завершення введення даних
            // А також очікування завершення введення даних потоками T1, T3
            data.B1.await();

            // Обчислення1
            Zh = Data.getPartOfVector(data.Z, from, to);
            a4 = Data.getMaxElementOfVector(Zh);

            // Обчислення2
            // КД1
            // Захищений перезапис спільного ресурсу a із використанням атомарної змінної
            data.a.updateAndGet(a -> Math.max(a, a4));

            // Сигнал потокам T1, T2, T3 за допомогою семафора S4 про завершення обчислення 1 потоком T3
            data.S4.release(3);

            // Очікування завершення обчислення 2 у потоках T1, T2, T3 за допомогою семафорів S1, S2, S3
            data.S1.acquire();
            data.S2.acquire();
            data.S3.acquire();

            // Копіювання a4 = a
            // КД2, synchronized-метод CS1()
            a4 = data.CS1();

            // Копіювання d4 = d
            // КД3, synchronized-метод CS2()
            d4 = data.CS2();

            // Обчислення3 - обчислення H рядків матриці MU із записом результату у відповідне поле об'єкта data
            data.doThirdCalculation(a4, d4, from, to);

            // Сигнал потоку T1 про завершення обчилення 3 за допомогою семафора S5
            data.S5.release();

            // Повідомлення про завершення виконання потоку T4
            System.out.println("Потік T4 завершив виконання");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
