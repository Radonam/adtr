package javax.mail.search;

import javax.mail.Address;
import javax.mail.Message;

public final class FromTerm extends AddressTerm {
    private static final long serialVersionUID = 5214730291502658665L;

    public FromTerm(Address address) {
        super(address);
    }

    public boolean match(Message message) {
        try {
            Address[] from = message.getFrom();
            if (from == null) {
                return false;
            }
            for (Address match : from) {
                if (super.match(match)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            Exception exception = e;
            return false;
        }
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof FromTerm) {
            return super.equals(obj2);
        }
        return false;
    }
}
