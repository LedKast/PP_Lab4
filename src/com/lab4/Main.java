package com.lab4;

import mpi.MPI;

public class Main {

    public static void main(String[] args) {
        MPI.Init(args);
        testScript(args, 500, 500);
        testScript(args, 5000, 5000);
        MPI.Finalize();
    }

    private static void testScript(String[] args, int rows, int cols) {
        new MPIMatrixSum(1, rows, cols, args).run();
        new MPIMatrixSum(2, rows, cols, args).run();
        new MPIMatrixSum(4, rows, cols, args).run(); // real cores
        new MPIMatrixSum(8, rows, cols, args).run(); // threads (cores*2)
        new MPIMatrixSum(16, rows, cols, args).run();
        new MPIMatrixSum(32, rows, cols, args).run();
    }
}
