package com.github.xgillard.max2satmip;

public final class BinaryClause {
    private final int a;
    private final int b;

    public BinaryClause(final int x, final int y) {
        this.a = Math.min(x, y);
        this.b = Math.max(x, y);
    }

    public boolean isTautology() {
        return a == -b;
    }
    public boolean isUnit() {
        return a == b;
    }
    public int getA() {return a;}
    public int getB() {return b;}

    @Override
    public int hashCode() {
        return a * 31 + b;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BinaryClause)) {
            return false;
        }
        BinaryClause other = (BinaryClause) obj;
        return this.a == other.a && this.b == other.b;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", a, b);
    }
}
