/**
 * Лабораторна робота ЛР1
 * F1: 1.23: E = A + B + C + D * (MA * MD)
 * F2: 2.26: MF = MG * (MH * ML)
 * F3: 3.28: s = MAX(S * MO) + MIN(MT * MS + MP)
 * Сутулов Нікіта Олегович
 * Група ІМ-12
 * Дата 18.02.2024
 */

package com.nikitasutulov.pzvpks.lab1;

import com.nikitasutulov.pzvpks.lab1.data.Data;
import com.nikitasutulov.pzvpks.lab1.threads.*;

public class Lab1 {

    public static void main(String[] args) {
        System.out.println("Головний потік: Виконання програми розпочато.");
        int n = Data.getNFromConsole();
        int inputOption = 0;

        if (n >= 1000) {
            inputOption = Data.getInputOptionFromConsole();
        }

        Thread1 t1 = new Thread1(n, inputOption);
        Thread2 t2 = new Thread2(n, inputOption);
        Thread3 t3 = new Thread3(n, inputOption);

        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t3.setPriority(Thread.NORM_PRIORITY);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Головний потік: Виконання програми завершено.");

    }
}