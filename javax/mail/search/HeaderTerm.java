package javax.mail.search;

import java.util.Locale;
import javax.mail.Message;

public final class HeaderTerm extends StringTerm {
    private static final long serialVersionUID = 8342514650333389122L;
    protected String headerName;

    public HeaderTerm(String str, String str2) {
        String str3 = str;
        super(str2);
        this.headerName = str3;
    }

    public String getHeaderName() {
        return this.headerName;
    }

    public boolean match(Message message) {
        try {
            String[] header = message.getHeader(this.headerName);
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
        if (!(obj2 instanceof HeaderTerm)) {
            return false;
        }
        HeaderTerm headerTerm = (HeaderTerm) obj2;
        return headerTerm.headerName.equalsIgnoreCase(this.headerName) && super.equals(headerTerm);
    }

    public int hashCode() {
        return this.headerName.toLowerCase(Locale.ENGLISH).hashCode() + super.hashCode();
    }
}
