package javax.activation;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

public class MimeTypeParameterList {
    private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
    private Hashtable parameters;

    public MimeTypeParameterList() {
        Hashtable hashtable = r4;
        Hashtable hashtable2 = new Hashtable();
        this.parameters = hashtable;
    }

    public MimeTypeParameterList(String str) throws MimeTypeParseException {
        String str2 = str;
        Hashtable hashtable = r5;
        Hashtable hashtable2 = new Hashtable();
        this.parameters = hashtable;
        parse(str2);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:10:0x002b, code skipped:
            if (r3 >= r2) goto L_?;
     */
    /* JADX WARNING: Missing block: B:11:0x002d, code skipped:
            r8 = r13;
            r9 = new javax.activation.MimeTypeParseException("More characters encountered in input than expected.");
     */
    /* JADX WARNING: Missing block: B:12:0x0037, code skipped:
            throw r8;
     */
    /* JADX WARNING: Missing block: B:84:?, code skipped:
            return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parse(String str) throws MimeTypeParseException {
        String str2 = str;
        if (str2 != null) {
            int length = str2.length();
            if (length > 0) {
                MimeTypeParseException mimeTypeParseException;
                MimeTypeParseException mimeTypeParseException2;
                int skipWhiteSpace = skipWhiteSpace(str2, 0);
                while (true) {
                    int i = skipWhiteSpace;
                    if (i >= length) {
                        break;
                    }
                    char charAt = str2.charAt(i);
                    char c = charAt;
                    if (charAt != ';') {
                        break;
                    }
                    i = skipWhiteSpace(str2, i + 1);
                    if (i < length) {
                        int i2 = i;
                        while (i < length && isTokenChar(str2.charAt(i))) {
                            i++;
                        }
                        String toLowerCase = str2.substring(i2, i).toLowerCase(Locale.ENGLISH);
                        i = skipWhiteSpace(str2, i);
                        if (i >= length || str2.charAt(i) != '=') {
                            mimeTypeParseException = r13;
                            mimeTypeParseException2 = new MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
                        } else {
                            i = skipWhiteSpace(str2, i + 1);
                            StringBuilder stringBuilder;
                            StringBuilder stringBuilder2;
                            if (i >= length) {
                                mimeTypeParseException = r13;
                                stringBuilder = r13;
                                stringBuilder2 = new StringBuilder("Couldn't find a value for parameter named ");
                                mimeTypeParseException2 = new MimeTypeParseException(stringBuilder.append(toLowerCase).toString());
                                throw mimeTypeParseException;
                            }
                            String unquote;
                            c = str2.charAt(i);
                            if (c == '\"') {
                                i++;
                                if (i >= length) {
                                    mimeTypeParseException = r13;
                                    mimeTypeParseException2 = new MimeTypeParseException("Encountered unterminated quoted parameter value.");
                                    throw mimeTypeParseException;
                                }
                                i2 = i;
                                while (i < length) {
                                    c = str2.charAt(i);
                                    if (c == '\"') {
                                        break;
                                    }
                                    if (c == '\\') {
                                        i++;
                                    }
                                    i++;
                                }
                                if (c != '\"') {
                                    mimeTypeParseException = r13;
                                    mimeTypeParseException2 = new MimeTypeParseException("Encountered unterminated quoted parameter value.");
                                    throw mimeTypeParseException;
                                }
                                unquote = unquote(str2.substring(i2, i));
                                i++;
                            } else if (isTokenChar(c)) {
                                i2 = i;
                                while (i < length && isTokenChar(str2.charAt(i))) {
                                    i++;
                                }
                                unquote = str2.substring(i2, i);
                            } else {
                                mimeTypeParseException = r13;
                                stringBuilder = r13;
                                stringBuilder2 = new StringBuilder("Unexpected character encountered at index ");
                                mimeTypeParseException2 = new MimeTypeParseException(stringBuilder.append(i).toString());
                                throw mimeTypeParseException;
                            }
                            Object put = this.parameters.put(toLowerCase, unquote);
                            skipWhiteSpace = skipWhiteSpace(str2, i);
                        }
                    } else {
                        return;
                    }
                }
                mimeTypeParseException = r13;
                mimeTypeParseException2 = new MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
                throw mimeTypeParseException;
            }
        }
    }

    public int size() {
        return this.parameters.size();
    }

    public boolean isEmpty() {
        return this.parameters.isEmpty();
    }

    public String get(String str) {
        return (String) this.parameters.get(str.trim().toLowerCase(Locale.ENGLISH));
    }

    public void set(String str, String str2) {
        String str3 = str2;
        Object put = this.parameters.put(str.trim().toLowerCase(Locale.ENGLISH), str3);
    }

    public void remove(String str) {
        Object remove = this.parameters.remove(str.trim().toLowerCase(Locale.ENGLISH));
    }

    public Enumeration getNames() {
        return this.parameters.keys();
    }

    public String toString() {
        StringBuffer stringBuffer = r7;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        stringBuffer3.ensureCapacity(this.parameters.size() * 16);
        Enumeration keys = this.parameters.keys();
        while (keys.hasMoreElements()) {
            String str = (String) keys.nextElement();
            stringBuffer = stringBuffer3.append("; ");
            stringBuffer = stringBuffer3.append(str);
            stringBuffer = stringBuffer3.append('=');
            stringBuffer = stringBuffer3.append(quote((String) this.parameters.get(str)));
        }
        return stringBuffer3.toString();
    }

    private static boolean isTokenChar(char c) {
        char c2 = c;
        return c2 > ' ' && c2 < 127 && TSPECIALS.indexOf(c2) < 0;
    }

    private static int skipWhiteSpace(String str, int i) {
        String str2 = str;
        int i2 = i;
        int length = str2.length();
        while (i2 < length && Character.isWhitespace(str2.charAt(i2))) {
            i2++;
        }
        return i2;
    }

    private static String quote(String str) {
        String str2 = str;
        Object obj = null;
        int length = str2.length();
        for (int i = 0; i < length && obj == null; i++) {
            obj = isTokenChar(str2.charAt(i)) ? null : 1;
        }
        if (obj == null) {
            return str2;
        }
        StringBuffer stringBuffer = r11;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        stringBuffer3.ensureCapacity((int) (((double) length) * 1.5d));
        stringBuffer = stringBuffer3.append('\"');
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str2.charAt(i2);
            if (charAt == '\\' || charAt == '\"') {
                stringBuffer = stringBuffer3.append('\\');
            }
            stringBuffer = stringBuffer3.append(charAt);
        }
        stringBuffer = stringBuffer3.append('\"');
        return stringBuffer3.toString();
    }

    private static String unquote(String str) {
        String str2 = str;
        int length = str2.length();
        StringBuffer stringBuffer = r8;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        stringBuffer3.ensureCapacity(length);
        Object obj = null;
        for (int i = 0; i < length; i++) {
            char charAt = str2.charAt(i);
            if (obj == null && charAt != '\\') {
                stringBuffer = stringBuffer3.append(charAt);
            } else if (obj != null) {
                stringBuffer = stringBuffer3.append(charAt);
                obj = null;
            } else {
                int obj2 = 1;
            }
        }
        return stringBuffer3.toString();
    }
}
