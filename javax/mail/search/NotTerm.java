package javax.mail.search;

import javax.mail.Message;

public final class NotTerm extends SearchTerm {
    private static final long serialVersionUID = 7152293214217310216L;
    protected SearchTerm term;

    public NotTerm(SearchTerm searchTerm) {
        this.term = searchTerm;
    }

    public SearchTerm getTerm() {
        return this.term;
    }

    public boolean match(Message message) {
        return !this.term.match(message);
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof NotTerm) {
            return ((NotTerm) obj2).term.equals(this.term);
        }
        return false;
    }

    public int hashCode() {
        return this.term.hashCode() << 1;
    }
}
