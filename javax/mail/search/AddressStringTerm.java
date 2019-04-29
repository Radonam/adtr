package javax.mail.search;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

public abstract class AddressStringTerm extends StringTerm {
    private static final long serialVersionUID = 3086821234204980368L;

    protected AddressStringTerm(String str) {
        super(str, true);
    }

    /* Access modifiers changed, original: protected */
    public boolean match(Address address) {
        Address address2 = address;
        if (!(address2 instanceof InternetAddress)) {
            return super.match(address2.toString());
        }
        return super.match(((InternetAddress) address2).toUnicodeString());
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof AddressStringTerm) {
            return super.equals(obj2);
        }
        return false;
    }
}
