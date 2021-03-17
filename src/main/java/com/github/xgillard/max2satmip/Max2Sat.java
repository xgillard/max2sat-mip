package com.github.xgillard.max2satmip;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Max2Sat {
    private final int nbVars;
    private final HashMap<BinaryClause, Integer> weights;

    public Max2Sat(final int n) {
        this.nbVars = n;
        this.weights= new HashMap<>();
    }

    public int getNbVars() { return nbVars;}
    public int getNbClauses() {return weights.size(); }
    public int getWeight(int x, int y) {
        return weights.getOrDefault(new BinaryClause(x, y), 0);
    }
    public void setWeight(int x, int y, int w) {
        weights.put(new BinaryClause(x, y), w);
    }

    public Iterator<Map.Entry<BinaryClause, Integer>> getClauses() {
        return this.weights.entrySet().iterator();
    }

    public static final Max2Sat fromFile(final String fname) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(fname))) {
            Max2Sat g = new Max2Sat(0);
            Pattern comment = Pattern.compile("^c\\s.*$");
            Pattern pbDecl  = Pattern.compile("^p\\s+wcnf\\s+(?<vars>\\d+)\\s+(?<clauses>\\d+)$");
            Pattern binDecl = Pattern.compile("^^(?<w>-?\\d+)\\s+(?<x>-?\\d+)\\s+(?<y>-?\\d+)\\s+0");
            Pattern unitDecl= Pattern.compile("^(?<w>-?\\d+)\\s+(?<x>-?\\d+)-?\\s+0");

            String line = null;
            while ((line = in.readLine())!=null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                Matcher capture = comment.matcher(line);
                if (capture.matches()) {
                    continue;
                }

                capture = pbDecl.matcher(line);
                if (capture.matches()) {
                    int n = Integer.parseInt(capture.group("vars"));
                    g = new Max2Sat(n);
                    continue;
                }

                capture = binDecl.matcher(line);
                if (capture.matches()) {
                    int w = Integer.parseInt(capture.group("w"));
                    int x = Integer.parseInt(capture.group("x"));
                    int y = Integer.parseInt(capture.group("y"));

                    g.setWeight(x, y, w);
                    continue;
                }
                capture = unitDecl.matcher(line);
                if (capture.matches()) {
                    int w = Integer.parseInt(capture.group("w"));
                    int x = Integer.parseInt(capture.group("x"));

                    g.setWeight(x, x, w);
                    continue;
                }

                throw new RuntimeException("Ill formatted");
            }

            return g;
        }
    }
}
