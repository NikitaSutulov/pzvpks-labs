namespace Lab3;

using System;
using System.Threading;

public class Data
{
    public static long N = 1600;
    public static long P = 4;
    public static long H => N / P;
    public long[] A = new long[N];
    public long[] B = new long[N];
    public long b;
    public long p;
    public long z;
    public long e;
    public long[,] MB = new long[N, N];
    public long[,] MR = new long[N, N];
    public long[,] MZ = new long[N, N];

    public Barrier B1 = new Barrier(4);

    public Semaphore S1 = new Semaphore(0, 3);
    public Semaphore S2 = new Semaphore(0, 3);
    public Semaphore S3 = new Semaphore(0, 3);
    public Semaphore S4 = new Semaphore(0, 3);
    public Semaphore S5 = new Semaphore(1, 1);

    public Mutex M1 = new Mutex();
    public Mutex M2 = new Mutex();

    public EventWaitHandle E1 = new EventWaitHandle(false, EventResetMode.ManualReset);
    public EventWaitHandle E3 = new EventWaitHandle(false, EventResetMode.ManualReset);
    public EventWaitHandle E4 = new EventWaitHandle(false, EventResetMode.ManualReset);

    public static long GetMinElementOfVector(long[] vector)
    {
        long result = vector[0];
        for (long i = 1; i < vector.Length; i++)
        {
            if (vector[i] < result)
            {
                result = vector[i];
            }
        }
        return result;
    }

    public static long[,] MultiplyTwoMatrices(long[,] matrix1, long[,] matrix2)
    {
        long[,] result = new long[matrix1.GetLength(0),matrix2.GetLength(1)];
        for (long i = 0; i < matrix1.GetLength(0); i++)
        {
            for (long j = 0; j < matrix2.GetLength(1); j++)
            {
                for (long k = 0; k < matrix1.GetLength(1); k++)
                {
                    result[i,j] += matrix1[i,k] * matrix2[k,j];
                }
            }
        }
        return result;
    }

    public static long[] MultiplyVectorByScalar(long[] vector, long scalar)
    {
        long[] result = new long[vector.Length];
        for (long i = 0; i < vector.Length; i++)
        {
            result[i] = vector[i] * scalar;
        }
        return result;
    }

    public static long MultiplyTwoVectors(long[] vector1, long[] vector2)
    {
        long length = Math.Min(vector1.Length, vector2.Length);
        long result = 0;

        for (long i = 0; i < length; i++) {
            result += vector1[i] * vector2[i];
        }

        return result;
    }

    public static long[] MultiplyVectorByMatrix(long[] vector, long[,] matrix) {
        long[] result = new long[matrix.GetLength(1)];

        for (long i = 0; i < matrix.GetLength(1); i++) {
            for (long j = 0; j < vector.Length; j++) {
                result[i] += vector[j] * matrix[j, i];
            }
        }

        return result;
    }

    public static long[] GetPartOfVector(long[] vector, long from, long to)
    {
        long[] result = new long[to - from + 1];
        for (long i = 0; i < result.Length; i++)
        {
            result[i] = vector[i + from];
        }
        return result;
    }

    public static long[,] GetPartOfMatrixColumns(long[,] matrix, long from, long to)
    {
        long[,] result = new long[matrix.GetLength(0), to - from + 1];
        for (long i = 0; i < result.GetLength(0); i++)
        {
            for (long j = 0; j < result.GetLength(1); j++)
            {
                result[i, j] = matrix[i, j + from];
            }
        }
        return result;
    }

    // Об'єкт для ідентифікації КД2
    public readonly object CS1 = new();

    // Об'єкт для ідентифікації КД5
    public readonly object CS2 = new();

    public long DoThirdCalculation(long pi, long from, long to)
    {
        return MultiplyTwoVectors( // (pi * (A * MBн)) * (B * (MZ * MRн))
            MultiplyVectorByScalar( // pi * (A * MBн)
                MultiplyVectorByMatrix( // A * MBн
                    A,
                    GetPartOfMatrixColumns( // MBн
                        MB, from, to
                    )
                ),
                pi
            ),
            MultiplyVectorByMatrix( // B * (MZ * MRн)
                B,
                MultiplyTwoMatrices( // MZ * MRн
                    MZ,
                    GetPartOfMatrixColumns( // MRн
                        MR, from, to
                    )
                )
            )
        );
    }
}
