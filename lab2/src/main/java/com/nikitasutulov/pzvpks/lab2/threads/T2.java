package com.nikitasutulov.pzvpks.lab2.threads;

import com.nikitasutulov.pzvpks.lab2.Data;

import java.util.concurrent.BrokenBarrierException;

public class T2 extends Thread {
    private Data data;
    private int a2;
    private int d2;
    private int from;
    private int to;

    private int[] Zh; // Zн

    public T2(Data data) {
        this.data = data;
        from = data.H;
        to = 2 * data.H - 1;
    }

    @Override
    public void run() {
        try {
            // Повідомлення про початок виконання потоку T2
            System.out.println("Потік T2 почав виконання");

            // Очікування завершення введення даних потоками T1, T3, T4
            data.B1.await();

            // Обчислення1
            Zh = Data.getPartOfVector(data.Z, from, to);
            a2 = Data.getMaxElementOfVector(Zh);

            // Обчислення2
            // КД1
            // Захищений перезапис спільного ресурсу a із використанням атомарної змінної
            data.a.updateAndGet(a -> Math.max(a, a2));

            // Сигнал потокам T1, T3, T4 за допомогою семафора S2 про завершення обчислення 1 потоком T2
            data.S2.release(3);

            // Очікування завершення обчислення 2 у потоках T1, T3, T4 за допомогою семафорів S1, S3, S4
            data.S1.acquire();
            data.S3.acquire();
            data.S4.acquire();

            // Копіювання a2 = a
            // КД2, synchronized-метод CS1()
            a2 = data.CS1();

            // Копіювання d2 = d
            // КД3, synchronized-метод CS2()
            d2 = data.CS2();

            // Обчислення3 - обчислення H рядків матриці MU із записом результату у відповідне поле об'єкта data
            data.doThirdCalculation(a2, d2, from, to);

            // Повідомлення про завершення виконання потоку T2
            System.out.println("Потік T2 завершив виконання");

            // Сигнал потоку T1 про завершення обчислення 3 за допомогою семафора S5
            data.S5.release();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
