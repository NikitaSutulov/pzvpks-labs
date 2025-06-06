/**
 * ПЗВПКС
 * ЛР2. Семафори, критичні секції,
 * атомік-змінні, бар’єри в мові Java
 * Варіант 24
 * МU = (MD*MC) * d + max(Z) * MR
 * Сутулов Нікіта Олегович
 * Група ІМ-12
 * Дата 25.03.2024
 */

package com.nikitasutulov.pzvpks.lab2;

import com.nikitasutulov.pzvpks.lab2.threads.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Lab2 {
    public static void main(String[] args) {
        // Очікування налаштування користувачем кількості виділених ядер для програми
        // Не впливає на вимірюваний час виконання програми, оскільки вимірювання починається після запуску потоків
        System.out.print("Натисніть Enter після налаштування кількості виділених ядер для програми");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Створення об'єкта класу Data
        Data data = new Data();

        // Створення потоків
        T1 t1 = new T1(data);
        T2 t2 = new T2(data);
        T3 t3 = new T3(data);
        T4 t4 = new T4(data);

        // Запуск потоків на виконання
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}