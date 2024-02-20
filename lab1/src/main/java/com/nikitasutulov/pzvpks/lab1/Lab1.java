/**
 * Лабораторна робота ЛР1
 * F1: 1.23: E = A + B + C + D * (MA * MD)
 * F2: 2.26: MF = MG * (MH * ML)
 * F3: 3.28: s = MAX(S * MO) + MIN(MT * MS + MP)
 * Сутулов Нікіта Олегович
 * Група ІМ-12
 * Дата 20.02.2024
 */

package com.nikitasutulov.pzvpks.lab1;

import com.nikitasutulov.pzvpks.lab1.data.Data;
import com.nikitasutulov.pzvpks.lab1.threads.*;

import java.util.Date;

public class Lab1 {

    public static void main(String[] args) {
        // виведення повідомлення про початок виконання програми
        System.out.println("Головний потік: Виконання програми розпочато.");

        // отримання розмірності n
        int n = Data.getNFromConsole();
        int inputOption = 0;

        // якщо n > 1000, то отримання варіанту надавання вхідних даних
        if (n > 1000) {
            inputOption = Data.getInputOptionFromConsole();
        }

        // час початку виконання потоків
        double startTime = (double) System.nanoTime() / 1000000000F;

        // створення потоків із задаванням отриманих налаштувань у конструктор
        Thread1 t1 = new Thread1(n, inputOption);
        Thread2 t2 = new Thread2(n, inputOption);
        Thread3 t3 = new Thread3(n, inputOption);

        // встановлення пріоритетів для потоків
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t3.setPriority(Thread.NORM_PRIORITY);

        // запуск потоків
        t1.start();
        t2.start();
        t3.start();

        // очікування завершення виконання потоків
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // якщо n > 1000, то вивести час виконання програми
        if (n > 1000) {
            double endTime = (double) System.nanoTime() / 1000000000F;
            System.out.println("Головний потік: час виконання програми склав " + String.format("%.2f с", endTime - startTime));
        }

        // виведення повідомлення про завершення виконання програми
        System.out.println("Головний потік: Виконання програми завершено.");

    }
}