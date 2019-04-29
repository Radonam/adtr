package javax.mail.internet;

public class HeaderTokenizer {
    private static final Token EOFToken;
    public static final String MIME = "()<>@,;:\\\"\t []/?=";
    public static final String RFC822 = "()<>@,;:\\\"\t .[]";
    private int currentPos;
    private String delimiters;
    private int maxPos;
    private int nextPos;
    private int peekPos;
    private boolean skipComments;
    private String string;

    public static class Token {
        public static final int ATOM = -1;
        public static final int COMMENT = -3;
        public static final int EOF = -4;
        public static final int QUOTEDSTRING = -2;
        private int type;
        private String value;

        public Token(int i, String str) {
            String str2 = str;
            this.type = i;
            this.value = str2;
        }

        public int getType() {
            return this.type;
        }

        public String getValue() {
            return this.value;
        }
    }

    static {
        Token token = r4;
        Token token2 = new Token(-4, null);
        EOFToken = token;
    }

    public HeaderTokenizer(String str, String str2, boolean z) {
        String str3 = str;
        String str4 = str2;
        boolean z2 = z;
        this.string = str3 == null ? "" : str3;
        this.skipComments = z2;
        this.delimiters = str4;
        int i = 0;
        int i2 = i;
        this.peekPos = i;
        i = i2;
        int i3 = i;
        this.nextPos = i;
        this.currentPos = i3;
        this.maxPos = this.string.length();
    }

    public HeaderTokenizer(String str, String str2) {
        this(str, str2, true);
    }

    public HeaderTokenizer(String str) {
        this(str, RFC822);
    }

    public Token next() throws ParseException {
        this.currentPos = this.nextPos;
        Token next = getNext();
        int i = this.currentPos;
        int i2 = i;
        this.peekPos = i;
        this.nextPos = i2;
        return next;
    }

    public Token peek() throws ParseException {
        this.currentPos = this.peekPos;
        Token next = getNext();
        this.peekPos = this.currentPos;
        return next;
    }

    public String getRemainder() {
        return this.string.substring(this.nextPos);
    }

    private Token getNext() throws ParseException {
        if (this.currentPos >= this.maxPos) {
            return EOFToken;
        }
        if (skipWhiteSpace() == -4) {
            return EOFToken;
        }
        Object obj = null;
        char charAt = this.string.charAt(this.currentPos);
        while (true) {
            char c = charAt;
            int i;
            int i2;
            int i3;
            int obj2;
            ParseException parseException;
            ParseException parseException2;
            Token token;
            Token token2;
            if (c == '(') {
                i = this.currentPos + 1;
                i2 = i;
                this.currentPos = i;
                i3 = i2;
                int i4 = 1;
                while (i4 > 0 && this.currentPos < this.maxPos) {
                    c = this.string.charAt(this.currentPos);
                    if (c == '\\') {
                        this.currentPos++;
                        obj2 = 1;
                    } else if (c == 13) {
                        obj2 = 1;
                    } else if (c == '(') {
                        i4++;
                    } else if (c == ')') {
                        i4--;
                    }
                    this.currentPos++;
                }
                if (i4 != 0) {
                    parseException = r12;
                    parseException2 = new ParseException("Unbalanced comments");
                    throw parseException;
                } else if (!this.skipComments) {
                    String filterToken;
                    if (obj2 != null) {
                        filterToken = filterToken(this.string, i3, this.currentPos - 1);
                    } else {
                        filterToken = this.string.substring(i3, this.currentPos - 1);
                    }
                    token = r12;
                    token2 = new Token(-3, filterToken);
                    return token;
                } else if (skipWhiteSpace() == -4) {
                    return EOFToken;
                } else {
                    charAt = this.string.charAt(this.currentPos);
                }
            } else if (c == '\"') {
                i = this.currentPos + 1;
                i2 = i;
                this.currentPos = i;
                i3 = i2;
                while (this.currentPos < this.maxPos) {
                    c = this.string.charAt(this.currentPos);
                    if (c == '\\') {
                        this.currentPos++;
                        obj2 = 1;
                    } else if (c == 13) {
                        obj2 = 1;
                    } else if (c == '\"') {
                        String filterToken2;
                        this.currentPos++;
                        if (obj2 != null) {
                            filterToken2 = filterToken(this.string, i3, this.currentPos - 1);
                        } else {
                            filterToken2 = this.string.substring(i3, this.currentPos - 1);
                        }
                        token = r12;
                        token2 = new Token(-2, filterToken2);
                        return token;
                    }
                    this.currentPos++;
                }
                parseException = r12;
                parseException2 = new ParseException("Unbalanced quoted string");
                throw parseException;
            } else if (c < ' ' || c >= 127 || this.delimiters.indexOf(c) >= 0) {
                this.currentPos++;
                token = r12;
                char c2 = c;
                String str = r12;
                String str2 = new String(new char[]{c});
                token2 = new Token(c2, str);
                return token;
            } else {
                i3 = this.currentPos;
                while (this.currentPos < this.maxPos) {
                    c = this.string.charAt(this.currentPos);
                    if (c < ' ' || c >= 127 || c == '(' || c == ' ' || c == '\"' || this.delimiters.indexOf(c) >= 0) {
                        break;
                    }
                    this.currentPos++;
                }
                token = r12;
                token2 = new Token(-1, this.string.substring(i3, this.currentPos));
                return token;
            }
        }
    }

    private int skipWhiteSpace() {
        while (this.currentPos < this.maxPos) {
            char charAt = this.string.charAt(this.currentPos);
            char c = charAt;
            if (charAt != ' ' && c != 9 && c != 13 && c != 10) {
                return this.currentPos;
            }
            this.currentPos++;
        }
        return -4;
    }

    private static String filterToken(String str, int i, int i2) {
        String str2 = str;
        int i3 = i;
        int i4 = i2;
        StringBuffer stringBuffer = r10;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        Object obj = null;
        Object obj2 = null;
        for (int i5 = i3; i5 < i4; i5++) {
            char charAt = str2.charAt(i5);
            if (charAt != 10 || obj2 == null) {
                obj2 = null;
                if (obj != null) {
                    stringBuffer = stringBuffer3.append(charAt);
                    obj = null;
                } else if (charAt == '\\') {
                    int obj3 = 1;
                } else if (charAt == 13) {
                    int obj22 = 1;
                } else {
                    stringBuffer = stringBuffer3.append(charAt);
                }
            } else {
                obj22 = null;
            }
        }
        return stringBuffer3.toString();
    }
}
