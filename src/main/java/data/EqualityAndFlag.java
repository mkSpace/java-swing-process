package data;

public class EqualityAndFlag {

    private final Equality equality;
    private boolean isCalculated;
    private EqualityState state;

    public EqualityAndFlag(Equality equality) {
        this(equality, false);
    }

    public EqualityAndFlag(Equality equality, EqualityState state) {
        this(equality, false, state);
    }

    public EqualityAndFlag(Equality equality, boolean flag) {
        this(equality, flag, EqualityState.NONE);
    }

    public EqualityAndFlag(Equality equality, boolean flag, EqualityState state) {
        this.equality = equality;
        this.isCalculated = flag;
        this.state = state;
    }

    public double calculate() {
        isCalculated = true;
        return equality.calculate();
    }

    public boolean isCalculated() {
        return isCalculated;
    }

    @Override
    public String toString() {
        if (isCalculated) {
            return equality + " = " + equality.calculate();
        } else {
            return equality.toString();
        }
    }

    public String getOnlyEqualityString() {
        return equality.toString();
    }

    public EqualityState getState() {
        if(state != null) return state; else return EqualityState.NONE;
    }

    public void setState(EqualityState state) {
        this.state = state;
    }
}
