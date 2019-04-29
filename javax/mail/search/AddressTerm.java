package javax.mail.search;

import javax.mail.Address;

public abstract class AddressTerm extends SearchTerm {
    private static final long serialVersionUID = 2005405551929769980L;
    protected Address address;

    protected AddressTerm(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return this.address;
    }

    /* Access modifiers changed, original: protected */
    public boolean match(Address address) {
        return address.equals(this.address);
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof AddressTerm) {
            return ((AddressTerm) obj2).address.equals(this.address);
        }
        return false;
    }

    public int hashCode() {
        return this.address.hashCode();
    }
}
