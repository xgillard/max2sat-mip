package com.github.xgillard.max2satmip;

import gurobi.GRBException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public final class Main {
    public static void main(String[] args) throws GRBException, IOException {
        if (args.length != 3) {
            System.err.println("Usage max2satmip <INSTANCE> <duration> <threads>");
            System.exit(0);
        }

        String inst     = args[0];
        String duration = args[1];
        String threads  = args[2];
/*
        String inst     = "/Users/user/Documents/REPO/ddo/examples/tests/resources/max2sat/frb15-9-1.wcnf";
        String duration = "0";
        String threads  = "0";
*/
        Max2Sat g = Max2Sat.fromFile(inst);
        Model   m = new Model(g);
        m.solve(Integer.parseInt(duration), Integer.parseInt(threads));
        showResult(inst, m);
    }

    private static void showResult(String fname, Model mip) throws GRBException {
        String inst   = new File(fname).getName();
        String status = "Timeout";
        if (mip.gap() == 0.0) {
            status = "Proved";
        }

        double lb = mip.objVal();
        double ub = lb + (lb * mip.gap());
        double rt = mip.runTime();

        int[] solution = mip.solution();
        StringBuffer sol= new StringBuffer();
        for(int i = 0; i < solution.length; i++) {
            if (solution[i] > 0) {
                sol.append((1+i)).append(" ");
            } else {
                sol.append(-(1+i)).append(" ");
            }
        }

        System.out.println(
                String.format("%-40s | %-10s | %-10d | %-10d | %10.3f | %s",
                        inst, status, (int) Math.rint(lb), (int) Math.rint(ub), rt, sol));
    }
}
