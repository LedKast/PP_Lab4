package com.lab4;

import mpi.MPI;
import mpi.Status;

import java.util.Arrays;

public class MPIMatrixSum {
    private static final int TAG = 0;
    private int mainProcNum;  // Номер координирующего процесса
    private int activeProcCount;  // используемых процессов
    private int processNum;             // Номер текущего процесса
    private int processCount;           // Кол-во процессов всего
    private int rows;
    private int cols;
    private String[] args;

    MPIMatrixSum(int activeProcCount, int rows, int cols, String[] args) {
        this.activeProcCount = activeProcCount;
        this.rows = rows;
        this.cols = cols;
        this.args = args;
    }

    public void run() {
        processNum = MPI.COMM_WORLD.Rank();
        processCount = MPI.COMM_WORLD.Size();
        mainProcNum = processCount - 1;

        if (processNum != mainProcNum)
            if (activeProcCount >= processCount ||
                activeProcCount < 1 ||
                processNum >= activeProcCount) {
                return;
            }

        if (processNum == mainProcNum)
            mainProcess();
        else
            calcProcess();
    }

    private void mainProcess() {
        Matrix2D matrix2D = new Matrix2D(rows, cols, true);
        double[] matrix = matrix2D.getMatrix();
        matrix2D.printMatrix();

        long time = System.currentTimeMillis();

        // Раздаем задачи
        for (int i = 0; i < activeProcCount; i++) {
            int offset = (matrix.length/activeProcCount)*i;
            int blockSize = (i == activeProcCount-1) ?
                    (matrix.length - offset) :
                    (matrix.length/activeProcCount);
//            System.out.println("Process: " + i + ", Block size: " + blockSize + ", offset: " + offset);

            // асинхронная (неблокирующая) отправка данных на узлы
            MPI.COMM_WORLD.Isend(matrix, offset, blockSize, MPI.DOUBLE, i, TAG);
        }
        // Ожидаем данные
        double totalSum = 0;
        for (int i = 0; i < activeProcCount; i++) {
            MPI.COMM_WORLD.Probe(i, TAG);
            double[] procSum = new double[]{0.0};
            MPI.COMM_WORLD.Recv(procSum, 0, 1, MPI.DOUBLE, i, TAG);
            totalSum += procSum[0];
        }

        // Вывод результата
        System.out.println("Matrix size: " + matrix2D.rows() + "x" + matrix2D.cols());
        System.out.println("Process count: " + activeProcCount);
        System.out.println("Total time: " + (System.currentTimeMillis() - time) + " ms");
        System.out.println("Total sum: " + totalSum);
        System.out.println("=========================");
    }

    // Считаем матрицу
    private void calcProcess() {
        Status status = MPI.COMM_WORLD.Probe(mainProcNum, TAG);
        double[] matrixPiece = new double[status.count];
        MPI.COMM_WORLD.Recv(matrixPiece, 0, status.count, MPI.DOUBLE, mainProcNum, TAG);
        MPI.COMM_WORLD.Send(new double[]{sum(matrixPiece)}, 0, 1, MPI.DOUBLE, mainProcNum, TAG);
    }

    // Сумма матрицы (массива)
    private double sum(double[] data) {
        return Arrays.stream(data).sum();
    }
}
