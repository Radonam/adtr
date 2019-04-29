package javax.mail.search;

import javax.mail.Message;

public final class AndTerm extends SearchTerm {
    private static final long serialVersionUID = -3583274505380989582L;
    protected SearchTerm[] terms;

    public AndTerm(SearchTerm searchTerm, SearchTerm searchTerm2) {
        SearchTerm searchTerm3 = searchTerm;
        SearchTerm searchTerm4 = searchTerm2;
        r4 = new SearchTerm[2];
        this.terms = r4;
        this.terms[0] = searchTerm3;
        this.terms[1] = searchTerm4;
    }

    public AndTerm(SearchTerm[] searchTermArr) {
        SearchTerm[] searchTermArr2 = searchTermArr;
        this.terms = new SearchTerm[searchTermArr2.length];
        for (int i = 0; i < searchTermArr2.length; i++) {
            this.terms[i] = searchTermArr2[i];
        }
    }

    public SearchTerm[] getTerms() {
        return (SearchTerm[]) this.terms.clone();
    }

    public boolean match(Message message) {
        Message message2 = message;
        for (SearchTerm match : this.terms) {
            if (!match.match(message2)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (!(obj2 instanceof AndTerm)) {
            return false;
        }
        AndTerm andTerm = (AndTerm) obj2;
        if (andTerm.terms.length != this.terms.length) {
            return false;
        }
        for (int i = 0; i < this.terms.length; i++) {
            if (!this.terms[i].equals(andTerm.terms[i])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        for (Object hashCode : this.terms) {
            i += hashCode.hashCode();
        }
        return i;
    }
}
