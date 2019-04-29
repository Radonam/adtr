package javax.mail.search;

import javax.mail.Message;

public final class MessageIDTerm extends StringTerm {
    private static final long serialVersionUID = -2121096296454691963L;

    public MessageIDTerm(String str) {
        super(str);
    }

    public boolean match(Message message) {
        try {
            String[] header = message.getHeader("Message-ID");
            if (header == null) {
                return false;
            }
            for (String match : header) {
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
        if (obj2 instanceof MessageIDTerm) {
            return super.equals(obj2);
        }
        return false;
    }
}
