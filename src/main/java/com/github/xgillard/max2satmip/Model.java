package com.github.xgillard.max2satmip;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

import java.util.Iterator;
import java.util.Map;

public class Model {
    private final Max2Sat  problem;
    private final GRBEnv   env;
    private final GRBModel model;
    private final GRBVar[] vars;

    public Model(final Max2Sat pb) throws GRBException {
        this.problem = pb;
        this.env     = new GRBEnv("max2sat.log");
        this.model   = new GRBModel(this.env);
        this.vars    = new GRBVar[pb.getNbVars()];

        model.set(GRB.IntAttr.ModelSense, -1); // maximization

        for (int i = 0; i < pb.getNbVars(); i++) {
            this.vars[i] = model.addVar(0, 1, 0,GRB.BINARY, ""+(1+i));
        }

        //GRBLinExpr objective = new GRBLinExpr();
        for (Iterator<Map.Entry<BinaryClause, Integer>> it = pb.getClauses(); it.hasNext(); ) {
            Map.Entry<BinaryClause, Integer> entry = it.next();

            BinaryClause clause = entry.getKey();
            GRBVar sat = model.addVar(0, 1, entry.getValue(), GRB.BINARY, entry.getKey().toString());

            // varibles
            GRBVar va = vars[Math.abs(clause.getA())-1];
            GRBVar vb = vars[Math.abs(clause.getB())-1];
            // signes des literaux
            double sa = Math.signum(clause.getA());
            double sb = Math.signum(clause.getB());
            // literal a
            GRBLinExpr lta = new GRBLinExpr();
            lta.addTerm(sa, va);
            lta.addConstant(sa > 0 ? 0 : 1);
            // literal b
            GRBLinExpr ltb = new GRBLinExpr();
            ltb.addTerm(sb, vb);
            ltb.addConstant(sb > 0 ? 0 : 1);
            //
            GRBLinExpr a_and_b = new GRBLinExpr();
            a_and_b.add(lta);
            a_and_b.add(ltb);

            model.addConstr(sat, GRB.GREATER_EQUAL,    lta,    "");
            model.addConstr(sat, GRB.GREATER_EQUAL,    ltb,    "");
            model.addConstr(sat, GRB.LESS_EQUAL,   a_and_b,  "");

            //objective.addTerm(1, sat);
        }

        //model.setObjective(objective);
    }


    public void solve(int timeLimit, int threads) throws GRBException {
        if (timeLimit > 0) model.set(GRB.DoubleParam.TimeLimit, timeLimit);
        if (threads   > 0) model.set(GRB.IntParam.Threads, threads);
        model.set(GRB.DoubleParam.MIPGap, 0.0000001);
        model.optimize();
    }


    public double gap() throws GRBException {
        return model.get(GRB.DoubleAttr.MIPGap);
    }

    public double runTime() throws GRBException {
        return model.get(GRB.DoubleAttr.Runtime);
    }

    public double objVal() throws GRBException {
        return model.get(GRB.DoubleAttr.ObjVal);
    }

    public int[] solution() throws GRBException {
        int[] result = new int[problem.getNbVars()];

        for (int i = 0; i < problem.getNbVars(); i++) {
            result[i] = (int) Math.rint(vars[i].get(GRB.DoubleAttr.X));
        }

        return result;
    }
}
