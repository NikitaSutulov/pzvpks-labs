package com.nikitasutulov.pzvpks.lab1.data;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Data {
    private static final Scanner scanner = new Scanner(System.in);

    public static int[] F1(int[] A, int[] B, int[] C, int[] D, int[][] MA, int[][] MD) {
        return addTwoVectors(
                A,
                addTwoVectors(
                        B,
                        addTwoVectors(
                                C,
                                multiplyVectorAndMatrix(
                                        D,
                                        multiplyTwoMatrices(MA, MD)
                                )
                        )
                )
        );
    }

    public static int[][] F2(int[][] MG, int[][] MH, int[][] ML) {
        return multiplyTwoMatrices(MG, multiplyTwoMatrices(MH, ML));
    }

    public static int F3(int[] S, int[][] MO, int[][] MT, int[][] MS, int[][] MP) {
        return getMaxElementOfVector(multiplyVectorAndMatrix(S, MO)) +
                getMinElementOfMatrix(
                        addTwoMatrices(
                                multiplyTwoMatrices(MT, MS),
                                MP
                        )
                );
    }

    public static int getNFromConsole() {
        while (true) {
            System.out.print("Введіть n: ");
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
            System.out.println("n має бути цілим числом");
        }
    }

    public static int getInputOptionFromConsole() {
        while (true) {
            System.out.println("Введіть варіант введення даних." +
                    "\n1 для зчитування з файлу, " +
                    "\n2 для встановлення всіх елементів заданому значенню, " +
                    "\n3 для використання генератора випадкових значень ");
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
        }
    }

    public static int[] addTwoVectors(int[] vector1, int[] vector2) {
        int[] sum = new int[vector1.length];
        for (int i = 0; i < sum.length; i++){
            sum[i] = vector1[i] + vector2[i];
        }
        return sum;
    }

    public static int[][] multiplyTwoMatrices(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1[0].length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    public static int[] multiplyVectorAndMatrix(int[] vector, int[][] matrix) {
        int[] result = new int[matrix[0].length];

        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < vector.length; j++) {
                result[i] += vector[j] * matrix[j][i];
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

    public static int getMaxElementOfVector(int[] vector) {
        int result = vector[0];
        for (int i = 1; i < vector.length; i++) {
            if (vector[i] > result) {
                result = vector[i];
            }
        }
        return result;
    }

    public static int getMinElementOfMatrix(int[][] matrix) {
        int result = Integer.MAX_VALUE;
        for (int[] row : matrix) {
            for (int i = 0; i < matrix[0].length; i++) {
                if (row[i] < result) {
                    result = row[i];
                }
            }
        }
        return result;
    }

    public static int[] getArray(String arrayName, int n, int inputOption, int defaultValue) {
        if (n < 1000) {
            return getArrayFromConsole(arrayName, n);
        }
        if (inputOption == 1) { // зчитати масив з файла
            return getArrayFromFile(arrayName, n);
        }
        if (inputOption == 2) { // заповнити масив одним значенням (1, 2 або 3)
            return getArrayFilledWithDefaultValue(n, defaultValue);
        }
        // згенерувати масив з випадковими значеннями
        return getArrayWithRandomValues(n);
    }

    public static int[][] getMatrix(String matrixName, int n, int inputOption, int defaultValue) {
        if (n < 1000) {
            return getMatrixFromConsole(matrixName, n);
        }
        if (inputOption == 1) { // зчитати матрицю з файла
            return getMatrixFromFile(matrixName, n);
        }
        if (inputOption == 2) { // заповнити матрицю одним значенням (1, 2 або 3)
            return getMatrixFilledWithDefaultValue(n, defaultValue);
        }
        // згенерувати матрицю з випадковими значеннями
        return getMatrixWithRandomValues(n);
    }

    public static int[] getArrayFromFile(String arrayName, int n) {
        try (BufferedReader reader = new BufferedReader(new FileReader(arrayName + ".txt"))) {
            int[] result = new int[n];
            String[] arrayLine = reader.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                result[i] = Integer.parseInt(arrayLine[i]);
            }
            return result;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[][] getMatrixFromFile(String matrixName, int n) {
        try (BufferedReader reader = new BufferedReader(new FileReader(matrixName + ".txt"))) {
            int[][] result = new int[n][n];
            for (int i = 0; i < n; i++) {
                String[] arrayLine = reader.readLine().split(" ");
                for (int j = 0; j < n; j++) {
                    result[i][j] = Integer.parseInt(arrayLine[j]);
                }
            }

            return result;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int[] getArrayFilledWithDefaultValue(int n, int defaultValue) {
        int[] result = new int[n];
        Arrays.fill(result, defaultValue);
        return result;
    }

    private static int[][] getMatrixFilledWithDefaultValue(int n, int defaultValue) {
        int[][] result = new int[n][n];
        for (int[] row: result) {
            Arrays.fill(row, defaultValue);
        }
        return result;
    }

    private static int[] getArrayWithRandomValues(int n) {
        int[] result = new int[n];
        Random random = new Random();
        for (int i = 0; i < result.length; i++) {
            result[i] = random.nextInt(100); // заповнити значеннями віл 0 до 99 включно
        }
        return result;
    }

    private static int[][] getMatrixWithRandomValues(int n) {
        int[][] result = new int[n][n];
        Random random = new Random();
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                result[i][j] = random.nextInt(100); // заповнити значеннями віл 0 до 99 включно
            }
        }
        return result;
    }

    private static int[] getArrayFromConsole(String arrayName, int n) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int[] result = new int[n];
            for (int i = 0; i < n; i++) {
                System.out.println("Enter " + arrayName + "[" + i + "] element:");
                result[i] = Integer.parseInt(reader.readLine());
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static int[][] getMatrixFromConsole(String matrixName, int n) {
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            result[i] = getArrayFromConsole(matrixName + "[" + i + "]", n);
        }
        return result;
    }

    public static void printArray(int[] array, String arrayName) {
        System.out.println(arrayName + ": " + Arrays.toString(array));
    }

    public static void printMatrix(int[][] matrix, String matrixName) {
        System.out.println(matrixName + ": ");
        for (int i = 0; i < matrix.length; i++) {
            printArray(matrix[i], matrixName + "[" + i + "]");
        }
    }

    public static void printNumber(int number, String numberName) {
        System.out.println(numberName + ": " + number);
    }

    public static void writeArrayToFile(int[] array, String arrayName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arrayName + ".txt"))) {
            writer.write(Arrays.toString(array).replaceAll("[,\\[\\]]", ""));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeMatrixToFile(int[][] matrix, String matrixName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(matrixName + ".txt"))) {
            for (int[] row: matrix) {
                writer.write(Arrays.toString(row).replaceAll("[,\\[\\]]", "") + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNumberToFile(int number, String numberName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(numberName + ".txt"))) {
            writer.write(String.valueOf(number));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
