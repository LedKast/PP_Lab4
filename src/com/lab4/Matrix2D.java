package com.lab4;

import java.util.Random;

public class Matrix2D {
    private final double[] matrix;
    private final int rows, cols;

    Matrix2D(int rows, int cols, boolean generate) {
        this.rows = rows;
        this.cols = cols;
        if (rows <= 0 || cols <= 0)
            throw new NumberFormatException("Matrix size must be > 0");

        matrix = new double[rows * cols];
        Random random = new Random(666);
        if (generate)
            for (int i = 0; i < rows * cols; i++)
                matrix[i] = random.nextInt(10);
    }

    public void printMatrix() {
        if (rows < 15 && cols < 15) {
            System.out.println("Matrix: ");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++)
                    System.out.print(getElement(i, j) + " ");
                System.out.println();
            }
        }
    }

    public void setElement(double value, int i, int j) {
        matrix[j * rows + i] = value;
    }

    public double getElement(int i, int j) {
        return matrix[j * rows + i];
    }

    public double[] getMatrix() {
        return matrix;
    }

    public int calcIndex1D(int i, int j) {
        return j * rows + i;
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }
}
