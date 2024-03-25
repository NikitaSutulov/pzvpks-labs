package com.nikitasutulov.pzvpks.lab2.threads;

import com.nikitasutulov.pzvpks.lab2.Data;

import java.util.concurrent.BrokenBarrierException;

public class T1 extends Thread {
    private Data data;
    private int a1;
    private int d1;
    private int from;
    private int to;

    private int[] Zh; // Zн

    public T1(Data data) {
        this.data = data;
        from = 0;
        to = data.H - 1;
    }

    @Override
    public void run() {
        try {
            // Повідомлення про початок виконання потоку T1
            System.out.println("Потік T1 почав виконання");

            // Фіксація часу початку виконання програми
            double startTime = (double) System.nanoTime() / 1000000000F;

            // Введення d
            data.d = 1;

            // Сигнал потокам T2, T3, T4 про завершення введення даних
            // А також очікування завершення введення даних потоками T3, T4
            data.B1.await();

            // Обчислення1
            Zh = Data.getPartOfVector(data.Z, from, to);
            a1 = Data.getMaxElementOfVector(Zh);

            // Обчислення2
            // КД1
            // Захищений перезапис спільного ресурсу a із використанням атомарної змінної
            data.a.updateAndGet(a -> Math.max(a, a1));

            // Сигнал потокам T2, T3, T4 за допомогою семафора S1 про завершення обчислення 1 потоком T1
            data.S1.release(3);

            // Очікування завершення обчислення 2 у потоках T2, T3, T4 за допомогою семафорів S2, S3, S4
            data.S2.acquire();
            data.S3.acquire();
            data.S4.acquire();

            // Копіювання a1 = a
            // КД2, synchronized-метод CS1()
            a1 = data.CS1();

            // Копіювання d1 = d
            // КД3, synchronized-метод CS2()
            d1 = data.CS2();

            // Обчислення3 - обчислення H рядків матриці MU із записом результату у відповідне поле об'єкта data
            data.doThirdCalculation(a1, d1, from, to);

            // Очікування на завершення обчислення 3 у потоках T2, T3, T4 за допомогою семафора S5
            data.S5.acquire(3);

            // Виведення результату MU
            Data.printMatrix(data.MU);

            // Фіксація часу закінчення виконання програми
            // Обчислення часу роботи програми і його виведення
            double finishTime = (double) System.nanoTime() / 1000000000F;
            System.out.println("T1: час виконання програми склав " + String.format("%.2f с", finishTime - startTime));

            // Повідомлення про завершення виконання потоку T1
            System.out.println("Потік T1 завершив виконання");

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}
