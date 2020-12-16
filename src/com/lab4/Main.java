package com.lab4;

import mpi.MPI;

import static java.lang.Math.min;

public class Main {

    public static void main(String[] args) {
        MPI.Init(args);
        testScript(args, 500, 500);
        testScript(args, 5000, 5000);
        MPI.Finalize();
    }

    private static void testScript(String[] args, int rows, int cols) {
        int systemThreads = Runtime.getRuntime().availableProcessors();
        int maxThreads = systemThreads;
        int maxElementsPerThread = 100000;

        int maxNeededThreads = (int)((double)(rows*cols + maxElementsPerThread - 1) / (double)maxElementsPerThread);
        int numberNeededThreads = min(systemThreads !=  0 ? systemThreads : maxThreads, maxNeededThreads);


        new MPIMatrixSum(1, rows, cols, args).run();
        new MPIMatrixSum(2, rows, cols, args).run();
        new MPIMatrixSum(systemThreads, rows, cols, args).run(); // system threads
        new MPIMatrixSum(2*systemThreads, rows, cols, args).run(); // 2*system threads
        new MPIMatrixSum(numberNeededThreads, rows, cols, args).run(); // system threads
    }
}
