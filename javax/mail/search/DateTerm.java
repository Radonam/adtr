package javax.mail.search;

import java.util.Date;

public abstract class DateTerm extends ComparisonTerm {
    private static final long serialVersionUID = 4818873430063720043L;
    protected Date date;

    protected DateTerm(int i, Date date) {
        Date date2 = date;
        this.comparison = i;
        this.date = date2;
    }

    public Date getDate() {
        Date date = r5;
        Date date2 = new Date(this.date.getTime());
        return date;
    }

    public int getComparison() {
        return this.comparison;
    }

    /* Access modifiers changed, original: protected */
    public boolean match(Date date) {
        Date date2 = date;
        switch (this.comparison) {
            case 1:
                return date2.before(this.date) || date2.equals(this.date);
            case 2:
                return date2.before(this.date);
            case 3:
                return date2.equals(this.date);
            case 4:
                return !date2.equals(this.date);
            case 5:
                return date2.after(this.date);
            case 6:
                return date2.after(this.date) || date2.equals(this.date);
            default:
                return false;
        }
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof DateTerm) {
            return ((DateTerm) obj2).date.equals(this.date) && super.equals(obj2);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.date.hashCode() + super.hashCode();
    }
}
