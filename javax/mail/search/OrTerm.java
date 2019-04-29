package javax.mail.search;

import javax.mail.Message;

public final class OrTerm extends SearchTerm {
    private static final long serialVersionUID = 5380534067523646936L;
    protected SearchTerm[] terms;

    public OrTerm(SearchTerm searchTerm, SearchTerm searchTerm2) {
        SearchTerm searchTerm3 = searchTerm;
        SearchTerm searchTerm4 = searchTerm2;
        r4 = new SearchTerm[2];
        this.terms = r4;
        this.terms[0] = searchTerm3;
        this.terms[1] = searchTerm4;
    }

    public OrTerm(SearchTerm[] searchTermArr) {
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
            if (match.match(message2)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (!(obj2 instanceof OrTerm)) {
            return false;
        }
        OrTerm orTerm = (OrTerm) obj2;
        if (orTerm.terms.length != this.terms.length) {
            return false;
        }
        for (int i = 0; i < this.terms.length; i++) {
            if (!this.terms[i].equals(orTerm.terms[i])) {
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
