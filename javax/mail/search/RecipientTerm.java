package javax.mail.search;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;

public final class RecipientTerm extends AddressTerm {
    private static final long serialVersionUID = 6548700653122680468L;
    protected RecipientType type;

    public RecipientTerm(RecipientType recipientType, Address address) {
        RecipientType recipientType2 = recipientType;
        super(address);
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
        if (obj2 instanceof RecipientTerm) {
            return ((RecipientTerm) obj2).type.equals(this.type) && super.equals(obj2);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.type.hashCode() + super.hashCode();
    }
}
