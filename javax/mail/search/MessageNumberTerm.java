package javax.mail.search;

import javax.mail.Message;

public final class MessageNumberTerm extends IntegerComparisonTerm {
    private static final long serialVersionUID = -5379625829658623812L;

    public MessageNumberTerm(int i) {
        super(3, i);
    }

    public boolean match(Message message) {
        try {
            return super.match(message.getMessageNumber());
        } catch (Exception e) {
            Exception exception = e;
            return false;
        }
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof MessageNumberTerm) {
            return super.equals(obj2);
        }
        return false;
    }
}
