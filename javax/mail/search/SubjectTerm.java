package javax.mail.search;

import javax.mail.Message;

public final class SubjectTerm extends StringTerm {
    private static final long serialVersionUID = 7481568618055573432L;

    public SubjectTerm(String str) {
        super(str);
    }

    public boolean match(Message message) {
        try {
            String subject = message.getSubject();
            if (subject == null) {
                return false;
            }
            return super.match(subject);
        } catch (Exception e) {
            Exception exception = e;
            return false;
        }
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof SubjectTerm) {
            return super.equals(obj2);
        }
        return false;
    }
}
