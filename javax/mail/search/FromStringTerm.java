package javax.mail.search;

import javax.mail.Address;
import javax.mail.Message;

public final class FromStringTerm extends AddressStringTerm {
    private static final long serialVersionUID = 5801127523826772788L;

    public FromStringTerm(String str) {
        super(str);
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
        if (obj2 instanceof FromStringTerm) {
            return super.equals(obj2);
        }
        return false;
    }
}
