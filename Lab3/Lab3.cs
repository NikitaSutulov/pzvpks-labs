/**
 * ПЗВПКС
 * ЛР3. Семафори, м'ютекси, події,
 * критичні секції, бар’єри в мові C#
 * Варіант 11
 * e = ((p * (A * MB)) * (B * (MZ * MR)) + min(B)
 * Сутулов Нікіта Олегович
 * Група ІМ-12
 * Дата 06.04.2024
*/

using System.Diagnostics;

namespace Lab3;

class Lab3
{
    static void Main(string[] args)
    {
        Stopwatch stopwatch = new Stopwatch();
        Data data = new Data();
        Console.WriteLine("Натисніть будь-яку кнопку після встановлення кількості ядер");
        Console.ReadKey();
        stopwatch.Start();
        Console.WriteLine("Програма розпочала роботу");
        Thread T1 = new Thread(() => t1(data));
        Thread T2 = new Thread(() => t2(data));
        Thread T3 = new Thread(() => t3(data));
        Thread T4 = new Thread(() => t4(data));

        T1.Start();
        T2.Start();
        T3.Start();
        T4.Start();

        T1.Join();
        T2.Join();
        T3.Join();
        T4.Join();
        
        Console.WriteLine("Програма завершила роботу");
        Console.WriteLine("Час роботи програми склав " + (double) stopwatch.ElapsedMilliseconds / 1000F + "с");
    }

    private static void t1(Data data)
    {
        Console.WriteLine("Потік T1 розпочав свою роботу");

        long from = 0;
        long to = Data.H - 1;
        long b1;
        long p1;
        long z1;
        
        // Введення MZ
        for (long i = 0; i < Data.N; i++)
        {
            for (long j = 0; j < Data.N; j++)
            {
                data.MZ[i, j] = 1;
            }
            
        }

        // Сигнал потокам T2, T3, T4 про введення MZ
        // Чекати на введення даних в потоках T2, T3
        data.B1.SignalAndWait();

        // Обчислення 1
        b1 = Data.GetMinElementOfVector(Data.GetPartOfVector(data.B, from, to));

        // Обчислення 2
        // КД1 через мютекс M1
        data.M1.WaitOne();
        data.b = Math.Min(data.b, b1);
        data.M1.ReleaseMutex();

        // Сигнал потокам T2, T3, T4 про завершення обчислення b через семафор S1
        data.S1.Release(3);

        // Чекати на завершення обчислення b в потоках T2, T3, T4 через семафори S2, S3, S4
        data.S2.WaitOne();
        data.S3.WaitOne();
        data.S4.WaitOne();

        // Копія p1 = p
        // КД2 через критичну секцію (lock на об'єкті CS1)
        lock (data.CS1)
        {
            p1 = data.p;
        }

        // Обчислення 3
        z1 = data.DoThirdCalculation(p1, from, to);

        // Обчислення 4
        // КД3 через мютекс M2
        data.M2.WaitOne();
        data.z = data.z + z1;
        data.M2.ReleaseMutex();

        // Сигнал потоку T2 про завершення обчислення z через подію E1
        data.E1.Set();

        Console.WriteLine("Потік T1 закінчив свою роботу");
    }
    
    private static void t2(Data data)
    {
        Console.WriteLine("Потік T2 розпочав свою роботу");

        long from = Data.H;
        long to = 2 * Data.H - 1;
        long b2;
        long p2;
        long z2;
        
        // Введення A
        for (long i = 0; i < Data.N; i++)
        {
            data.A[i] = 1;
        }
        
        // Введення MR
        for (long i = 0; i < Data.N; i++)
        {
            for (long j = 0; j < Data.N; j++)
            {
                data.MR[i, j] = 1;
            }
            
        }

        // Сигнал потокам T1, T3, T4 про введення A та MR
        // Чекати на введення даних в потоках T1, T3
        data.B1.SignalAndWait();

        // Обчислення 1
        b2 = Data.GetMinElementOfVector(Data.GetPartOfVector(data.B, from, to));

        // Обчислення 2
        // КД1 через мютекс M1
        data.M1.WaitOne();
        data.b = Math.Min(data.b, b2);
        data.M1.ReleaseMutex();

        // Сигнал потокам T1, T3, T4 про завершення обчислення b через семафор S2
        data.S2.Release(3);

        // Чекати на завершення обчислення b в потоках T2, T3, T4 через семафори S1, S3, S4
        data.S1.WaitOne();
        data.S3.WaitOne();
        data.S4.WaitOne();

        // Копія p2 = p
        // КД2 через критичну секцію (lock на об'єкті CS1)
        lock (data.CS1)
        {
            p2 = data.p;
        }

        // Обчислення 3
        z2 = data.DoThirdCalculation(p2, from, to);

        // Обчислення 4
        // КД3 через мютекс M2
        data.M2.WaitOne();
        data.z = data.z + z2;
        data.M2.ReleaseMutex();

        // Чекати на завершення обчислення z в задачах T1, T3, T4 через події E1, E3, E4
        data.E1.WaitOne();
        data.E3.WaitOne();
        data.E4.WaitOne();

        // Копія b2 = b
        // КД4 через семафор S5
        data.S5.WaitOne();
        b2 = data.b;
        data.S5.Release();

        // Копія z2 = z
        // КД5 через критичну секцію (lock на об'єкті CS2)
        lock (data.CS2)
        {
            z2 = data.z;
        }

        // Обчислення 5
        data.e = z2 + b2;

        // Виведення e
        Console.WriteLine(data.e);

        Console.WriteLine("Потік T2 закінчив свою роботу");
    }

    private static void t3(Data data)
    {
        Console.WriteLine("Потік T3 розпочав свою роботу");

        long from = 2 * Data.H;
        long to = 3 * Data.H - 1;
        long b3;
        long p3;
        long z3;
        
        // Введення MB
        for (long i = 0; i < Data.N; i++)
        {
            for (long j = 0; j < Data.N; j++)
            {
                data.MB[i, j] = 1;
            }
            
        }

        // Введення B
        for (long i = 0; i < Data.N; i++)
        {
            data.B[i] = 1;
        }
        data.B[Data.H / 2] = -1; // встановлення значення -1 для одного елемента B з метою перевірки правильності обчислення мінімального елемента

        // Введення p
        data.p = 1;

        // Сигнал потокам T1, T2, T4 про введення MB, B та p
        // Чекати на введення даних в потоках T1, T2
        data.B1.SignalAndWait();

        // Обчислення 1
        b3 = Data.GetMinElementOfVector(Data.GetPartOfVector(data.B, from, to));

        // Обчислення 2
        // КД1 через мютекс M1
        data.M1.WaitOne();
        data.b = Math.Min(data.b, b3);
        data.M1.ReleaseMutex();

        // Сигнал потокам T1, T2, T4 про завершення обчислення b через семафор S3
        data.S3.Release(3);

        // Чекати на завершення обчислення b в потоках T1, T2, T4 через семафори S1, S2, S4
        data.S1.WaitOne();
        data.S2.WaitOne();
        data.S4.WaitOne();

        // Копія p3 = p
        // КД2 через критичну секцію (lock на об'єкті CS1)
        lock (data.CS1)
        {
            p3 = data.p;
        }

        // Обчислення 3
        z3 = data.DoThirdCalculation(p3, from, to);

        // Обчислення 4
        // КД3 через мютекс M2
        data.M2.WaitOne();
        data.z = data.z + z3;
        data.M2.ReleaseMutex();

        // Сигнал потоку T2 про завершення обчислення z через подію E3
        data.E3.Set();

        Console.WriteLine("Потік T3 закінчив свою роботу");
    }

    private static void t4(Data data)
    {
        Console.WriteLine("Потік T4 розпочав свою роботу");

        long from = 3 * Data.H;
        long to = 4 * Data.H - 1;
        long b4;
        long p4;
        long z4;
        
        // Чекати на введення даних в потоках T1, T2, T3
        data.B1.SignalAndWait();

        // Обчислення 1
        b4 = Data.GetMinElementOfVector(Data.GetPartOfVector(data.B, from, to));

        // Обчислення 2
        // КД1 через мютекс M1
        data.M1.WaitOne();
        data.b = Math.Min(data.b, b4);
        data.M1.ReleaseMutex();

        // Сигнал потокам T1, T2, T3 про завершення обчислення b через семафор S4
        data.S4.Release(3);

        // Чекати на завершення обчислення b в потоках T1, T2, T3 через семафори S1, S2, S3
        data.S1.WaitOne();
        data.S2.WaitOne();
        data.S3.WaitOne();

        // Копія p4 = p
        // КД2 через критичну секцію (lock на об'єкті CS1)
        lock (data.CS1)
        {
            p4 = data.p;
        }

        // Обчислення 3
        z4 = data.DoThirdCalculation(p4, from, to);

        // Обчислення 4
        // КД3 через мютекс M2
        data.M2.WaitOne();
        data.z = data.z + z4;
        data.M2.ReleaseMutex();

        // Сигнал потоку T2 про завершення обчислення z через подію E4
        data.E4.Set();

        Console.WriteLine("Потік T4 закінчив свою роботу");
    }

}
