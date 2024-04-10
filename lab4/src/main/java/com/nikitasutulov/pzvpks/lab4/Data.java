package com.nikitasutulov.pzvpks.lab4;

import com.nikitasutulov.pzvpks.lab4.monitors.SharedMonitor;
import com.nikitasutulov.pzvpks.lab4.monitors.SyncMonitor;

import java.util.Arrays;

public class Data {
    public int N = 16;
    public int P = 4;
    public int H = N / P;

    public int[] A = new int[N];
    public int[] B = new int[N];
    public int[] V = new int[N];
    public int[] Z = new int[N];
    public int[][] MM = new int[N][N];
    public int[][] MX = new int[N][N];
    public int[][] MT = new int[N][N];

    public SyncMonitor syncMon = new SyncMonitor();
    public SharedMonitor shrMon = new SharedMonitor();

    public static int[] multiplyVectorByScalar(int[] vector, int scalar) {
        int[] result = new int[vector.length];
        for (int i = 0; i < vector.length; i++) {
            result[i] = vector[i] * scalar;
        }
        return result;
    }

    public static int[] multiplyVectorByMatrix(int[] vector, int[][] matrix) {
        int[] result = new int[matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < vector.length; j++) {
                result[i] += vector[j] * matrix[j][i];
            }
        }
        return result;
    }

    public static int[] addTwoVectors(int[] vector1, int[] vector2) {
        int[] result = new int[vector1.length];
        for (int i = 0; i < vector1.length; i++) {
            result[i] = vector1[i] + vector2[i];
        }
        return result;
    }

    public static int multiplyTwoVectors(int[] vector1, int[] vector2) {
        int result = 0;
        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
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

    public static int[] getPartOfVector(int[] vector, int from, int to) {
        int[] result = new int[to - from + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = vector[i + from];
        }
        return result;
    }

    public static void insertVectorPartIntoVector(int[] partOfVector, int[] vector, int from) {
        for (int i = 0; i < partOfVector.length; i++) {
            vector[i + from] = partOfVector[i];
        }
    }

    public static int[][] getPartOfMatrixColumns(int[][] matrix, int from, int to) {
        int[][] result = new int[matrix.length][to - from + 1];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = matrix[i][j + from];
            }
        }
        return result;
    }

    public static void mergeSort(int[] array, int from, int to) {
        if (from < to) {
            int mid = (from + to) / 2;
            mergeSort(array, from, mid);
            mergeSort(array, mid + 1, to);
            merge(array, from, mid, to);
        }
    }

    public static void merge(int[] array, int from, int center, int to) {
        int n1 = center - from + 1;
        int n2 = to - center;


        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];


        System.arraycopy(array, from, leftArray, 0, n1);
        for (int j = 0; j < n2; ++j)
            rightArray[j] = array[center + 1 + j];


        int i = 0, j = 0;
        int k = from;
        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }


        while (i < n1) {
            array[k] = leftArray[i];
            i++;
            k++;
        }


        while (j < n2) {
            array[k] = rightArray[j];
            j++;
            k++;
        }
    }

    public static void printVector(int[] vector) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < vector.length; i++) {
            result.append(vector[i] + "\t");
        }
        System.out.println(result.toString());
    }

    public void doSixthCalculation(int ai, int from, int to) {
        Data.insertVectorPartIntoVector(
                Data.addTwoVectors( // A * (MX * MTн) + ai * Bн
                        Data.multiplyVectorByMatrix( // A * (MX * MTн)
                                A,
                                Data.multiplyTwoMatrices( // MX * MTн
                                        MX,
                                        Data.getPartOfMatrixColumns(MT, from, to) // MTн
                                )
                        ),
                        Data.multiplyVectorByScalar( // ai * Bн
                                Data.getPartOfVector(B, from, to), // Bн
                                ai
                        )
                ),
                V, from
        );
    }

}
