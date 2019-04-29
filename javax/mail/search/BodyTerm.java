package javax.mail.search;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;

public final class BodyTerm extends StringTerm {
    private static final long serialVersionUID = -4888862527916911385L;

    public BodyTerm(String str) {
        super(str);
    }

    public boolean match(Message message) {
        return matchPart(message);
    }

    private boolean matchPart(Part part) {
        Part part2 = part;
        try {
            if (part2.isMimeType("text/*")) {
                String str = (String) part2.getContent();
                if (str == null) {
                    return false;
                }
                return super.match(str);
            }
            if (part2.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) part2.getContent();
                int count = multipart.getCount();
                for (int i = 0; i < count; i++) {
                    if (matchPart(multipart.getBodyPart(i))) {
                        return true;
                    }
                }
            } else if (part2.isMimeType("message/rfc822")) {
                return matchPart((Part) part2.getContent());
            }
            return false;
        } catch (Exception e) {
            Exception exception = e;
        }
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (obj2 instanceof BodyTerm) {
            return super.equals(obj2);
        }
        return false;
    }
}
