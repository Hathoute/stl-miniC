package fr.n7.stl.util;

public class TruthSequence {
    protected boolean currentValue;

    public TruthSequence(boolean start) {
        currentValue = start;
    }

    public TruthSequence and(boolean bool) {
        currentValue = bool && currentValue;
        return this;
    }

    public boolean finish() {
        return currentValue;
    }
}