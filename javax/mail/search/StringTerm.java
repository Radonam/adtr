package javax.mail.search;

public abstract class StringTerm extends SearchTerm {
    private static final long serialVersionUID = 1274042129007696269L;
    protected boolean ignoreCase;
    protected String pattern;

    protected StringTerm(String str) {
        this.pattern = str;
        this.ignoreCase = true;
    }

    protected StringTerm(String str, boolean z) {
        boolean z2 = z;
        this.pattern = str;
        this.ignoreCase = z2;
    }

    public String getPattern() {
        return this.pattern;
    }

    public boolean getIgnoreCase() {
        return this.ignoreCase;
    }

    /* Access modifiers changed, original: protected */
    public boolean match(String str) {
        String str2 = str;
        int length = str2.length() - this.pattern.length();
        for (int i = 0; i <= length; i++) {
            if (str2.regionMatches(this.ignoreCase, i, this.pattern, 0, this.pattern.length())) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (!(obj2 instanceof StringTerm)) {
            return false;
        }
        StringTerm stringTerm = (StringTerm) obj2;
        if (this.ignoreCase) {
            if (stringTerm.pattern.equalsIgnoreCase(this.pattern) && stringTerm.ignoreCase == this.ignoreCase) {
                return true;
            }
            return false;
        } else if (stringTerm.pattern.equals(this.pattern) && stringTerm.ignoreCase == this.ignoreCase) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.ignoreCase ? this.pattern.hashCode() : this.pattern.hashCode() ^ -1;
    }
}
