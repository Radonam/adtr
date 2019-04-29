package javax.mail.search;

import java.util.Date;
import javax.mail.Message;

public final class SentDateTerm extends DateTerm {
    private static final long serialVersionUID = 5647755030530907263L;

    public SentDateTerm(int i, Date date) {
        super(i, date);
    }

    public boolean match(Message message) {
        try {
            Date sentDate = message.getSentDate();
            if (sentDate == null) {
                return false;
            }
            return super.match(sentDate);
        } catch (Exception e) {
            Exception exception = e;
            return false;
        }
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof SentDateTerm) {
            return super.equals(obj2);
        }
        return false;
    }
}
