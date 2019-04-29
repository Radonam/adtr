package javax.mail.search;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;

public final class RecipientStringTerm extends AddressStringTerm {
    private static final long serialVersionUID = -8293562089611618849L;
    private RecipientType type;

    public RecipientStringTerm(RecipientType recipientType, String str) {
        RecipientType recipientType2 = recipientType;
        super(str);
        this.type = recipientType2;
    }

    public RecipientType getRecipientType() {
        return this.type;
    }

    public boolean match(Message message) {
        try {
            Address[] recipients = message.getRecipients(this.type);
            if (recipients == null) {
                return false;
            }
            for (Address match : recipients) {
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
        if (obj2 instanceof RecipientStringTerm) {
            return ((RecipientStringTerm) obj2).type.equals(this.type) && super.equals(obj2);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.type.hashCode() + super.hashCode();
    }
}
