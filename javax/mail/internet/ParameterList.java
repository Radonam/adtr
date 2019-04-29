package javax.mail.internet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.mail.internet.HeaderTokenizer.Token;

public class ParameterList {
    private static boolean applehack;
    private static boolean decodeParameters;
    private static boolean decodeParametersStrict;
    private static boolean encodeParameters;
    private static final char[] hex = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private String lastName;
    private Map list;
    private Set multisegmentNames;
    private Map slist;

    private static class MultiValue extends ArrayList {
        String value;

        private MultiValue() {
        }

        /* synthetic */ MultiValue(MultiValue multiValue) {
            MultiValue multiValue2 = multiValue;
            this();
        }
    }

    private static class ParamEnum implements Enumeration {
        private Iterator it;

        ParamEnum(Iterator it) {
            this.it = it;
        }

        public boolean hasMoreElements() {
            return this.it.hasNext();
        }

        public Object nextElement() {
            return this.it.next();
        }
    }

    private static class ToStringBuffer {
        private StringBuffer sb;
        private int used;

        public ToStringBuffer(int i) {
            int i2 = i;
            StringBuffer stringBuffer = r5;
            StringBuffer stringBuffer2 = new StringBuffer();
            this.sb = stringBuffer;
            this.used = i2;
        }

        public void addNV(String str, String str2) {
            String str3 = str;
            String access$0 = ParameterList.quote(str2);
            StringBuffer append = this.sb.append("; ");
            this.used += 2;
            if (this.used + ((str3.length() + access$0.length()) + 1) > 76) {
                append = this.sb.append("\r\n\t");
                this.used = 8;
            }
            append = this.sb.append(str3).append('=');
            this.used += str3.length() + 1;
            if (this.used + access$0.length() > 76) {
                String fold = MimeUtility.fold(this.used, access$0);
                append = this.sb.append(fold);
                int lastIndexOf = fold.lastIndexOf(10);
                if (lastIndexOf >= 0) {
                    this.used += (fold.length() - lastIndexOf) - 1;
                    return;
                }
                this.used += fold.length();
                return;
            }
            append = this.sb.append(access$0);
            this.used += access$0.length();
        }

        public String toString() {
            return this.sb.toString();
        }
    }

    private static class Value {
        String charset;
        String encodedValue;
        String value;

        private Value() {
        }

        /* synthetic */ Value(Value value) {
            Value value2 = value;
            this();
        }
    }

    static {
        encodeParameters = false;
        decodeParameters = false;
        decodeParametersStrict = false;
        applehack = false;
        try {
            String property = System.getProperty("mail.mime.encodeparameters");
            boolean z = property != null && property.equalsIgnoreCase("true");
            encodeParameters = z;
            property = System.getProperty("mail.mime.decodeparameters");
            z = property != null && property.equalsIgnoreCase("true");
            decodeParameters = z;
            property = System.getProperty("mail.mime.decodeparameters.strict");
            z = property != null && property.equalsIgnoreCase("true");
            decodeParametersStrict = z;
            property = System.getProperty("mail.mime.applefilenames");
            if (property == null || !property.equalsIgnoreCase("true")) {
                z = false;
            } else {
                z = true;
            }
            applehack = z;
        } catch (SecurityException e) {
            SecurityException securityException = e;
        }
    }

    public ParameterList() {
        LinkedHashMap linkedHashMap = r4;
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        this.list = linkedHashMap;
        this.lastName = null;
        if (decodeParameters) {
            HashSet hashSet = r4;
            HashSet hashSet2 = new HashSet();
            this.multisegmentNames = hashSet;
            HashMap hashMap = r4;
            HashMap hashMap2 = new HashMap();
            this.slist = hashMap;
        }
    }

    /* JADX WARNING: Missing block: B:40:0x018f, code skipped:
            throw r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ParameterList(String str) throws ParseException {
        String str2 = str;
        this();
        HeaderTokenizer headerTokenizer = r13;
        HeaderTokenizer headerTokenizer2 = new HeaderTokenizer(str2, HeaderTokenizer.MIME);
        HeaderTokenizer headerTokenizer3 = headerTokenizer;
        while (true) {
            Token next = headerTokenizer3.next();
            int type = next.getType();
            ParseException parseException;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            ParseException parseException2;
            Object put;
            if (type == -4) {
                break;
            } else if (((char) type) == ';') {
                next = headerTokenizer3.next();
                if (next.getType() == -4) {
                    break;
                } else if (next.getType() != -1) {
                    parseException = r13;
                    stringBuilder = r13;
                    stringBuilder2 = new StringBuilder("Expected parameter name, got \"");
                    parseException2 = new ParseException(stringBuilder.append(next.getValue()).append("\"").toString());
                    throw parseException;
                } else {
                    String toLowerCase = next.getValue().toLowerCase(Locale.ENGLISH);
                    next = headerTokenizer3.next();
                    if (((char) next.getType()) != '=') {
                        parseException = r13;
                        stringBuilder = r13;
                        stringBuilder2 = new StringBuilder("Expected '=', got \"");
                        parseException2 = new ParseException(stringBuilder.append(next.getValue()).append("\"").toString());
                        throw parseException;
                    }
                    next = headerTokenizer3.next();
                    type = next.getType();
                    if (type == -1 || type == -2) {
                        String value = next.getValue();
                        this.lastName = toLowerCase;
                        if (decodeParameters) {
                            putEncodedName(toLowerCase, value);
                        } else {
                            put = this.list.put(toLowerCase, value);
                        }
                    } else {
                        parseException = r13;
                        stringBuilder = r13;
                        stringBuilder2 = new StringBuilder("Expected parameter value, got \"");
                        parseException2 = new ParseException(stringBuilder.append(next.getValue()).append("\"").toString());
                        throw parseException;
                    }
                }
            } else if (applehack && type == -1 && this.lastName != null && (this.lastName.equals("name") || this.lastName.equals("filename"))) {
                StringBuilder stringBuilder3 = r13;
                StringBuilder stringBuilder4 = new StringBuilder(String.valueOf((String) this.list.get(this.lastName)));
                put = this.list.put(this.lastName, stringBuilder3.append(" ").append(next.getValue()).toString());
            } else {
                parseException = r13;
                stringBuilder = r13;
                stringBuilder2 = new StringBuilder("Expected ';', got \"");
                parseException2 = new ParseException(stringBuilder.append(next.getValue()).append("\"").toString());
            }
        }
        if (decodeParameters) {
            combineMultisegmentNames(false);
        }
    }

    private void putEncodedName(String str, String str2) throws ParseException {
        String str3 = str;
        String str4 = str2;
        int indexOf = str3.indexOf(42);
        Object put;
        if (indexOf < 0) {
            put = this.list.put(str3, str4);
        } else if (indexOf == str3.length() - 1) {
            put = this.list.put(str3.substring(0, indexOf), decodeValue(str4));
        } else {
            Value value;
            String substring = str3.substring(0, indexOf);
            boolean add = this.multisegmentNames.add(substring);
            put = this.list.put(substring, "");
            if (str3.endsWith("*")) {
                Value value2 = r10;
                Value value3 = new Value();
                value = value2;
                value.encodedValue = str4;
                value.value = str4;
                str3 = str3.substring(0, str3.length() - 1);
            } else {
                Object value4 = str4;
            }
            put = this.slist.put(str3, value4);
        }
    }

    private void combineMultisegmentNames(boolean z) throws ParseException {
        ParseException parseException;
        ParseException parseException2;
        boolean z2 = z;
        Object obj = null;
        Value value;
        Value decodeValue;
        try {
            for (String str : this.multisegmentNames) {
                Object remove;
                StringBuffer stringBuffer = r25;
                StringBuffer stringBuffer2 = new StringBuffer();
                StringBuffer stringBuffer3 = stringBuffer;
                MultiValue multiValue = r25;
                MultiValue multiValue2 = new MultiValue();
                MultiValue multiValue3 = multiValue;
                String str2 = null;
                int i = 0;
                while (true) {
                    StringBuilder stringBuilder = r25;
                    StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(str));
                    String stringBuilder3 = stringBuilder.append("*").append(i).toString();
                    Object obj2 = this.slist.get(stringBuilder3);
                    if (obj2 == null) {
                        break;
                    }
                    boolean add = multiValue3.add(obj2);
                    String str3 = null;
                    if (obj2 instanceof Value) {
                        Value value2 = (Value) obj2;
                        String str4 = value2.encodedValue;
                        str3 = str4;
                        Value value3;
                        String str5;
                        String str6;
                        if (i == 0) {
                            Value decodeValue2 = decodeValue(str4);
                            value3 = value2;
                            str5 = decodeValue2.charset;
                            str6 = str5;
                            value3.charset = str5;
                            str2 = str6;
                            value3 = value2;
                            str5 = decodeValue2.value;
                            str6 = str5;
                            value3.value = str5;
                            str3 = str6;
                        } else if (str2 == null) {
                            add = this.multisegmentNames.remove(str);
                            break;
                        } else {
                            value3 = value2;
                            str5 = decodeBytes(str4, str2);
                            str6 = str5;
                            value3.value = str5;
                            str3 = str6;
                        }
                    } else {
                        str3 = (String) obj2;
                    }
                    stringBuffer = stringBuffer3.append(str3);
                    remove = this.slist.remove(stringBuilder3);
                    i++;
                }
                if (i == 0) {
                    remove = this.list.remove(str);
                } else {
                    multiValue3.value = stringBuffer3.toString();
                    remove = this.list.put(str, multiValue3);
                }
            }
            int i2 = 1;
            if (z2 || i2 != 0) {
                if (this.slist.size() > 0) {
                    for (Object next : this.slist.values()) {
                        if (next instanceof Value) {
                            value = (Value) next;
                            decodeValue = decodeValue(value.encodedValue);
                            value.charset = decodeValue.charset;
                            value.value = decodeValue.value;
                        }
                    }
                    this.list.putAll(this.slist);
                }
                this.multisegmentNames.clear();
                this.slist.clear();
            }
        } catch (NumberFormatException e) {
            NumberFormatException numberFormatException = e;
            if (decodeParametersStrict) {
                parseException = r25;
                parseException2 = new ParseException(numberFormatException.toString());
                throw parseException;
            }
        } catch (UnsupportedEncodingException e2) {
            UnsupportedEncodingException unsupportedEncodingException = e2;
            if (decodeParametersStrict) {
                parseException = r25;
                parseException2 = new ParseException(unsupportedEncodingException.toString());
                throw parseException;
            }
        } catch (StringIndexOutOfBoundsException e3) {
            StringIndexOutOfBoundsException stringIndexOutOfBoundsException = e3;
            if (decodeParametersStrict) {
                parseException = r25;
                parseException2 = new ParseException(stringIndexOutOfBoundsException.toString());
                throw parseException;
            }
        } catch (Throwable th) {
            Throwable th2 = th;
            if (z2 || obj != null) {
                if (this.slist.size() > 0) {
                    for (Object next2 : this.slist.values()) {
                        if (next2 instanceof Value) {
                            value = (Value) next2;
                            decodeValue = decodeValue(value.encodedValue);
                            value.charset = decodeValue.charset;
                            value.value = decodeValue.value;
                        }
                    }
                    this.list.putAll(this.slist);
                }
                this.multisegmentNames.clear();
                this.slist.clear();
            }
            Throwable th3 = th2;
        }
    }

    public int size() {
        return this.list.size();
    }

    public String get(String str) {
        String str2;
        Object obj = this.list.get(str.trim().toLowerCase(Locale.ENGLISH));
        if (obj instanceof MultiValue) {
            str2 = ((MultiValue) obj).value;
        } else if (obj instanceof Value) {
            str2 = ((Value) obj).value;
        } else {
            str2 = (String) obj;
        }
        return str2;
    }

    public void set(String str, String str2) {
        ParseException parseException;
        Object put;
        String str3 = str;
        String str4 = str2;
        if (str3 != null || str4 == null || !str4.equals("DONE")) {
            str3 = str3.trim().toLowerCase(Locale.ENGLISH);
            if (decodeParameters) {
                try {
                    putEncodedName(str3, str4);
                    return;
                } catch (ParseException e) {
                    parseException = e;
                    put = this.list.put(str3, str4);
                    return;
                }
            }
            put = this.list.put(str3, str4);
        } else if (decodeParameters && this.multisegmentNames.size() > 0) {
            try {
                combineMultisegmentNames(true);
            } catch (ParseException e2) {
                parseException = e2;
            }
        }
    }

    public void set(String str, String str2, String str3) {
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        if (encodeParameters) {
            Value encodeValue = encodeValue(str5, str6);
            if (encodeValue != null) {
                Object put = this.list.put(str4.trim().toLowerCase(Locale.ENGLISH), encodeValue);
                return;
            } else {
                set(str4, str5);
                return;
            }
        }
        set(str4, str5);
    }

    public void remove(String str) {
        Object remove = this.list.remove(str.trim().toLowerCase(Locale.ENGLISH));
    }

    public Enumeration getNames() {
        ParamEnum paramEnum = r4;
        ParamEnum paramEnum2 = new ParamEnum(this.list.keySet().iterator());
        return paramEnum;
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int i) {
        ToStringBuffer toStringBuffer = r14;
        ToStringBuffer toStringBuffer2 = new ToStringBuffer(i);
        ToStringBuffer toStringBuffer3 = toStringBuffer;
        for (String str : this.list.keySet()) {
            Object obj = this.list.get(str);
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            if (obj instanceof MultiValue) {
                MultiValue multiValue = (MultiValue) obj;
                StringBuilder stringBuilder3 = r14;
                stringBuilder = new StringBuilder(String.valueOf(str));
                String stringBuilder4 = stringBuilder3.append("*").toString();
                for (int i2 = 0; i2 < multiValue.size(); i2++) {
                    Object obj2 = multiValue.get(i2);
                    if (obj2 instanceof Value) {
                        toStringBuffer = toStringBuffer3;
                        stringBuilder = r14;
                        stringBuilder2 = new StringBuilder(String.valueOf(stringBuilder4));
                        toStringBuffer.addNV(stringBuilder.append(i2).append("*").toString(), ((Value) obj2).encodedValue);
                    } else {
                        toStringBuffer = toStringBuffer3;
                        stringBuilder = r14;
                        stringBuilder2 = new StringBuilder(String.valueOf(stringBuilder4));
                        toStringBuffer.addNV(stringBuilder.append(i2).toString(), (String) obj2);
                    }
                }
            } else if (obj instanceof Value) {
                toStringBuffer = toStringBuffer3;
                stringBuilder = r14;
                stringBuilder2 = new StringBuilder(String.valueOf(str));
                toStringBuffer.addNV(stringBuilder.append("*").toString(), ((Value) obj).encodedValue);
            } else {
                toStringBuffer3.addNV(str, (String) obj);
            }
        }
        return toStringBuffer3.toString();
    }

    private static String quote(String str) {
        return MimeUtility.quote(str, HeaderTokenizer.MIME);
    }

    private static Value encodeValue(String str, String str2) {
        String str3 = str;
        String str4 = str2;
        if (MimeUtility.checkAscii(str3) == 1) {
            return null;
        }
        try {
            byte[] bytes = str3.getBytes(MimeUtility.javaCharset(str4));
            StringBuffer stringBuffer = r10;
            StringBuffer stringBuffer2 = new StringBuffer((bytes.length + str4.length()) + 2);
            StringBuffer stringBuffer3 = stringBuffer;
            stringBuffer = stringBuffer3.append(str4).append("''");
            for (byte b : bytes) {
                char c = (char) (b & 255);
                if (c <= ' ' || c >= 127 || c == '*' || c == '\'' || c == '%' || HeaderTokenizer.MIME.indexOf(c) >= 0) {
                    stringBuffer = stringBuffer3.append('%').append(hex[c >> 4]).append(hex[c & 15]);
                } else {
                    stringBuffer = stringBuffer3.append(c);
                }
            }
            Value value = r10;
            Value value2 = new Value();
            Value value3 = value;
            value3.charset = str4;
            value3.value = str3;
            value3.encodedValue = stringBuffer3.toString();
            return value3;
        } catch (UnsupportedEncodingException e) {
            UnsupportedEncodingException unsupportedEncodingException = e;
            return null;
        }
    }

    private static Value decodeValue(String str) throws ParseException {
        String str2 = str;
        Value value = r11;
        Value value2 = new Value();
        Value value3 = value;
        value3.encodedValue = str2;
        value3.value = str2;
        ParseException parseException;
        ParseException parseException2;
        try {
            int indexOf = str2.indexOf(39);
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            if (indexOf > 0) {
                String substring = str2.substring(0, indexOf);
                int indexOf2 = str2.indexOf(39, indexOf + 1);
                if (indexOf2 >= 0) {
                    String substring2 = str2.substring(indexOf + 1, indexOf2);
                    str2 = str2.substring(indexOf2 + 1);
                    value3.charset = substring;
                    value3.value = decodeBytes(str2, substring);
                    return value3;
                } else if (!decodeParametersStrict) {
                    return value3;
                } else {
                    parseException = r11;
                    stringBuilder = r11;
                    stringBuilder2 = new StringBuilder("Missing language in encoded value: ");
                    parseException2 = new ParseException(stringBuilder.append(str2).toString());
                    throw parseException;
                }
            } else if (!decodeParametersStrict) {
                return value3;
            } else {
                parseException = r11;
                stringBuilder = r11;
                stringBuilder2 = new StringBuilder("Missing charset in encoded value: ");
                parseException2 = new ParseException(stringBuilder.append(str2).toString());
                throw parseException;
            }
        } catch (NumberFormatException e) {
            NumberFormatException numberFormatException = e;
            if (decodeParametersStrict) {
                parseException = r11;
                parseException2 = new ParseException(numberFormatException.toString());
                throw parseException;
            }
        } catch (UnsupportedEncodingException e2) {
            UnsupportedEncodingException unsupportedEncodingException = e2;
            if (decodeParametersStrict) {
                parseException = r11;
                parseException2 = new ParseException(unsupportedEncodingException.toString());
                throw parseException;
            }
        } catch (StringIndexOutOfBoundsException e3) {
            StringIndexOutOfBoundsException stringIndexOutOfBoundsException = e3;
            if (decodeParametersStrict) {
                parseException = r11;
                parseException2 = new ParseException(stringIndexOutOfBoundsException.toString());
                throw parseException;
            }
        }
    }

    private static String decodeBytes(String str, String str2) throws UnsupportedEncodingException {
        String str3 = str;
        String str4 = str2;
        byte[] bArr = new byte[str3.length()];
        int i = 0;
        int i2 = 0;
        while (i < str3.length()) {
            char charAt = str3.charAt(i);
            if (charAt == '%') {
                charAt = (char) Integer.parseInt(str3.substring(i + 1, i + 3), 16);
                i += 2;
            }
            int i3 = i2;
            i2++;
            bArr[i3] = (byte) charAt;
            i++;
        }
        String str5 = r13;
        String str6 = new String(bArr, 0, i2, MimeUtility.javaCharset(str4));
        return str5;
    }
}
