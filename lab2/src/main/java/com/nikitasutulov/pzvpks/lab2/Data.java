package com.nikitasutulov.pzvpks.lab2;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Data {
    public int N = 32;
    public int P = 4;
    public int H = N / P;
    public int[] Z = new int[N];
    public AtomicInteger a = new AtomicInteger(Integer.MIN_VALUE); // A1
    public int d;
    public int[][] MC = new int[N][N];
    public int[][] MD = new int[N][N];
    public int[][] MR = new int[N][N];
    public int[][] MU = new int[N][N];

    public CyclicBarrier B1 = new CyclicBarrier(4);

    public Semaphore S1 = new Semaphore(0);
    public Semaphore S2 = new Semaphore(0);
    public Semaphore S3 = new Semaphore(0);
    public Semaphore S4 = new Semaphore(0);
    public Semaphore S5 = new Semaphore(0);

    public static int getMaxElementOfVector(int[] vector) {
        int result = vector[0];
        for (int i = 1; i < vector.length; i++) {
            if (vector[i] > result) {
                result = vector[i];
            }
        }
        return result;
    }

    public static int[][] multiplyTwoMatrices(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix2[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    public static int[][] multiplyMatrixByScalar(int[][] matrix, int scalar) {
        int[][] result = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i][j] = matrix[i][j] * scalar;
            }
        }
        return result;
    }

    public static int[][] addTwoMatrices(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1[0].length];

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }

    public static int[] getPartOfVector(int[] vector, int from, int to) {
        int[] result = new int[to - from + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = vector[i + from];
        }
        return result;
    }

    public static int[][] getPartOfMatrixRows(int[][] matrix, int from, int to) {
        int[][] result = new int[to - from + 1][matrix[0].length];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[i][j] = matrix[i + from][j];
            }
        }
        return result;
    }

    public static void insertPartOfMatrixRowsIntoMatrix(int[][] donorMatrix, int[][] acceptorMatrix, int from) {
        for (int i = 0; i < donorMatrix.length; i++) {
            for (int j = 0; j < acceptorMatrix[0].length; j++) {
                acceptorMatrix[from + i][j] = donorMatrix[i][j];
            }
        }
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf("%d\t", matrix[i][j]);
            }
            System.out.println();
        }
    }

    public synchronized int CS1() {
        return a.get();
    }

    public synchronized int CS2() {
        return d;
    }

    public void doThirdCalculation(int ai, int di, int from, int to) {
        insertPartOfMatrixRowsIntoMatrix(                                       // MUн = (MDн * MC) * di + ai * MRн
                addTwoMatrices(                                                 // (MDн * MC) * di + ai * MRн
                        multiplyMatrixByScalar(                                 // (MDн * MC) * di
                                multiplyTwoMatrices(                            // (MDн * MC)
                                        getPartOfMatrixRows(MD, from, to),      // MDн
                                        MC
                                ),
                                di
                        ),
                        multiplyMatrixByScalar(                                 // ai * MRн
                                getPartOfMatrixRows(MR, from, to),              // MRн
                                ai
                        )
                ),
                MU, from
        );
    }
}
