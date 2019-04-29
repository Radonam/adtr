package javax.mail.internet;

public class AddressException extends ParseException {
    private static final long serialVersionUID = 9134583443539323120L;
    protected int pos = -1;
    protected String ref = null;

    public AddressException() {
    }

    public AddressException(String str) {
        super(str);
    }

    public AddressException(String str, String str2) {
        String str3 = str2;
        super(str);
        this.ref = str3;
    }

    public AddressException(String str, String str2, int i) {
        String str3 = str2;
        int i2 = i;
        super(str);
        this.ref = str3;
        this.pos = i2;
    }

    public String getRef() {
        return this.ref;
    }

    public int getPos() {
        return this.pos;
    }

    public String toString() {
        String parseException = super.toString();
        if (this.ref == null) {
            return parseException;
        }
        StringBuilder stringBuilder = r5;
        StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(parseException));
        parseException = stringBuilder.append(" in string ``").append(this.ref).append("''").toString();
        if (this.pos < 0) {
            return parseException;
        }
        stringBuilder = r5;
        stringBuilder2 = new StringBuilder(String.valueOf(parseException));
        return stringBuilder.append(" at position ").append(this.pos).toString();
    }
}
