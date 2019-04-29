package javax.mail.search;

public abstract class IntegerComparisonTerm extends ComparisonTerm {
    private static final long serialVersionUID = -6963571240154302484L;
    protected int number;

    protected IntegerComparisonTerm(int i, int i2) {
        int i3 = i2;
        this.comparison = i;
        this.number = i3;
    }

    public int getNumber() {
        return this.number;
    }

    public int getComparison() {
        return this.comparison;
    }

    /* Access modifiers changed, original: protected */
    public boolean match(int i) {
        int i2 = i;
        switch (this.comparison) {
            case 1:
                return i2 <= this.number;
            case 2:
                return i2 < this.number;
            case 3:
                return i2 == this.number;
            case 4:
                return i2 != this.number;
            case 5:
                return i2 > this.number;
            case 6:
                return i2 >= this.number;
            default:
                return false;
        }
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof IntegerComparisonTerm) {
            return ((IntegerComparisonTerm) obj2).number == this.number && super.equals(obj2);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.number + super.hashCode();
    }
}
