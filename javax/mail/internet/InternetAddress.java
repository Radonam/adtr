package javax.mail.internet;

import com.sun.activation.registries.MailcapTokenizer;
import com.sun.mail.iap.Response;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Address;
import javax.mail.Session;

public class InternetAddress extends Address implements Cloneable {
    private static final String rfc822phrase = HeaderTokenizer.RFC822.replace(' ', 0).replace(9, 0);
    private static final long serialVersionUID = -7507595530758302903L;
    private static final String specialsNoDot = "()<>,;:\\\"[]@";
    private static final String specialsNoDotNoAt = "()<>,;:\\\"[]";
    protected String address;
    protected String encodedPersonal;
    protected String personal;

    public InternetAddress() {
    }

    public InternetAddress(String str) throws AddressException {
        String str2 = str;
        InternetAddress[] parse = parse(str2, true);
        if (parse.length != 1) {
            AddressException addressException = r7;
            AddressException addressException2 = new AddressException("Illegal address", str2);
            throw addressException;
        }
        this.address = parse[0].address;
        this.personal = parse[0].personal;
        this.encodedPersonal = parse[0].encodedPersonal;
    }

    public InternetAddress(String str, boolean z) throws AddressException {
        boolean z2 = z;
        this(str);
        if (z2) {
            checkAddress(this.address, true, true);
        }
    }

    public InternetAddress(String str, String str2) throws UnsupportedEncodingException {
        this(str, str2, null);
    }

    public InternetAddress(String str, String str2, String str3) throws UnsupportedEncodingException {
        String str4 = str2;
        String str5 = str3;
        this.address = str;
        setPersonal(str4, str5);
    }

    public Object clone() {
        InternetAddress internetAddress = null;
        try {
            internetAddress = (InternetAddress) super.clone();
        } catch (CloneNotSupportedException e) {
            CloneNotSupportedException cloneNotSupportedException = e;
        }
        return internetAddress;
    }

    public String getType() {
        return "rfc822";
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public void setPersonal(String str, String str2) throws UnsupportedEncodingException {
        String str3 = str;
        String str4 = str2;
        this.personal = str3;
        if (str3 != null) {
            this.encodedPersonal = MimeUtility.encodeWord(str3, str4, null);
            return;
        }
        this.encodedPersonal = null;
    }

    public void setPersonal(String str) throws UnsupportedEncodingException {
        String str2 = str;
        this.personal = str2;
        if (str2 != null) {
            this.encodedPersonal = MimeUtility.encodeWord(str2);
            return;
        }
        this.encodedPersonal = null;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPersonal() {
        if (this.personal != null) {
            return this.personal;
        }
        if (this.encodedPersonal == null) {
            return null;
        }
        try {
            this.personal = MimeUtility.decodeText(this.encodedPersonal);
            return this.personal;
        } catch (Exception e) {
            Exception exception = e;
            return this.encodedPersonal;
        }
    }

    public String toString() {
        if (this.encodedPersonal == null && this.personal != null) {
            try {
                this.encodedPersonal = MimeUtility.encodeWord(this.personal);
            } catch (UnsupportedEncodingException e) {
                UnsupportedEncodingException unsupportedEncodingException = e;
            }
        }
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        if (this.encodedPersonal != null) {
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder(String.valueOf(quotePhrase(this.encodedPersonal)));
            return stringBuilder.append(" <").append(this.address).append(">").toString();
        } else if (isGroup() || isSimple()) {
            return this.address;
        } else {
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder("<");
            return stringBuilder.append(this.address).append(">").toString();
        }
    }

    public String toUnicodeString() {
        String personal = getPersonal();
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        if (personal != null) {
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder(String.valueOf(quotePhrase(personal)));
            return stringBuilder.append(" <").append(this.address).append(">").toString();
        } else if (isGroup() || isSimple()) {
            return this.address;
        } else {
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder("<");
            return stringBuilder.append(this.address).append(">").toString();
        }
    }

    private static String quotePhrase(String str) {
        StringBuffer stringBuffer;
        StringBuffer stringBuffer2;
        String str2 = str;
        int length = str2.length();
        Object obj = null;
        for (int i = 0; i < length; i++) {
            char charAt = str2.charAt(i);
            if (charAt == '\"' || charAt == '\\') {
                stringBuffer = r12;
                stringBuffer2 = new StringBuffer(length + 3);
                StringBuffer stringBuffer3 = stringBuffer;
                stringBuffer = stringBuffer3.append('\"');
                for (int i2 = 0; i2 < length; i2++) {
                    char charAt2 = str2.charAt(i2);
                    if (charAt2 == '\"' || charAt2 == '\\') {
                        stringBuffer = stringBuffer3.append('\\');
                    }
                    stringBuffer = stringBuffer3.append(charAt2);
                }
                stringBuffer = stringBuffer3.append('\"');
                return stringBuffer3.toString();
            }
            if ((charAt < ' ' && charAt != 13 && charAt != 10 && charAt != 9) || charAt >= 127 || rfc822phrase.indexOf(charAt) >= 0) {
                obj = 1;
            }
        }
        if (obj == null) {
            return str2;
        }
        stringBuffer = r12;
        stringBuffer2 = new StringBuffer(length + 2);
        StringBuffer stringBuffer4 = stringBuffer;
        stringBuffer = stringBuffer4.append('\"').append(str2).append('\"');
        return stringBuffer4.toString();
    }

    private static String unquote(String str) {
        String str2 = str;
        if (str2.startsWith("\"") && str2.endsWith("\"")) {
            str2 = str2.substring(1, str2.length() - 1);
            if (str2.indexOf(92) >= 0) {
                StringBuffer stringBuffer = r8;
                StringBuffer stringBuffer2 = new StringBuffer(str2.length());
                StringBuffer stringBuffer3 = stringBuffer;
                int i = 0;
                while (i < str2.length()) {
                    char charAt = str2.charAt(i);
                    if (charAt == '\\' && i < str2.length() - 1) {
                        i++;
                        charAt = str2.charAt(i);
                    }
                    stringBuffer = stringBuffer3.append(charAt);
                    i++;
                }
                str2 = stringBuffer3.toString();
            }
        }
        return str2;
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (!(obj2 instanceof InternetAddress)) {
            return false;
        }
        String address = ((InternetAddress) obj2).getAddress();
        if (address == this.address) {
            return true;
        }
        if (this.address == null || !this.address.equalsIgnoreCase(address)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.address == null) {
            return 0;
        }
        return this.address.toLowerCase(Locale.ENGLISH).hashCode();
    }

    public static String toString(Address[] addressArr) {
        return toString(addressArr, 0);
    }

    public static String toString(Address[] addressArr, int i) {
        Address[] addressArr2 = addressArr;
        int i2 = i;
        if (addressArr2 == null || addressArr2.length == 0) {
            return null;
        }
        StringBuffer stringBuffer = r8;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        for (int i3 = 0; i3 < addressArr2.length; i3++) {
            if (i3 != 0) {
                stringBuffer = stringBuffer3.append(", ");
                i2 += 2;
            }
            String address = addressArr2[i3].toString();
            if (i2 + lengthOfFirstSegment(address) > 76) {
                stringBuffer = stringBuffer3.append("\r\n\t");
                i2 = 8;
            }
            stringBuffer = stringBuffer3.append(address);
            i2 = lengthOfLastSegment(address, i2);
        }
        return stringBuffer3.toString();
    }

    private static int lengthOfFirstSegment(String str) {
        String str2 = str;
        int indexOf = str2.indexOf("\r\n");
        int i = indexOf;
        if (indexOf != -1) {
            return i;
        }
        return str2.length();
    }

    private static int lengthOfLastSegment(String str, int i) {
        String str2 = str;
        int i2 = i;
        int lastIndexOf = str2.lastIndexOf("\r\n");
        int i3 = lastIndexOf;
        if (lastIndexOf != -1) {
            return (str2.length() - i3) - 2;
        }
        return str2.length() + i2;
    }

    public static InternetAddress getLocalAddress(Session session) {
        Session session2 = session;
        String str = null;
        String str2 = null;
        String str3 = null;
        if (session2 == null) {
            try {
                str = System.getProperty("user.name");
                str2 = InetAddress.getLocalHost().getHostName();
            } catch (SecurityException e) {
                SecurityException securityException = e;
            } catch (AddressException e2) {
                AddressException addressException = e2;
            } catch (UnknownHostException e3) {
                UnknownHostException unknownHostException = e3;
            }
        } else {
            str3 = session2.getProperty("mail.from");
            if (str3 == null) {
                str = session2.getProperty("mail.user");
                if (str == null || str.length() == 0) {
                    str = session2.getProperty("user.name");
                }
                if (str == null || str.length() == 0) {
                    str = System.getProperty("user.name");
                }
                str2 = session2.getProperty("mail.host");
                if (str2 == null || str2.length() == 0) {
                    InetAddress localHost = InetAddress.getLocalHost();
                    if (localHost != null) {
                        str2 = localHost.getHostName();
                    }
                }
            }
        }
        if (!(str3 != null || str == null || str.length() == 0 || str2 == null || str2.length() == 0)) {
            StringBuilder stringBuilder = r8;
            StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(str));
            str3 = stringBuilder.append("@").append(str2).toString();
        }
        if (str3 != null) {
            InternetAddress internetAddress = r8;
            InternetAddress internetAddress2 = new InternetAddress(str3);
            return internetAddress;
        }
        return null;
    }

    public static InternetAddress[] parse(String str) throws AddressException {
        return parse(str, true);
    }

    public static InternetAddress[] parse(String str, boolean z) throws AddressException {
        return parse(str, z, false);
    }

    public static InternetAddress[] parseHeader(String str, boolean z) throws AddressException {
        return parse(str, z, true);
    }

    private static InternetAddress[] parse(String str, boolean z, boolean z2) throws AddressException {
        InternetAddress internetAddress;
        InternetAddress internetAddress2;
        InternetAddress internetAddress3;
        StringTokenizer stringTokenizer;
        StringTokenizer stringTokenizer2;
        String str2 = str;
        boolean z3 = z;
        boolean z4 = z2;
        int i = -1;
        int i2 = -1;
        int length = str2.length();
        Object obj = null;
        boolean z5 = false;
        Object obj2 = null;
        Vector vector = r27;
        Vector vector2 = new Vector();
        Vector vector3 = vector;
        int i3 = -1;
        int i4 = i3;
        int i5 = i3;
        int i6 = 0;
        while (i6 < length) {
            int obj22;
            AddressException addressException;
            AddressException addressException2;
            switch (str2.charAt(i6)) {
                case 9:
                case 10:
                case 13:
                case Response.SYNTHETIC /*32*/:
                    break;
                case '\"':
                    obj22 = 1;
                    if (i5 == -1) {
                        i5 = i6;
                    }
                    while (true) {
                        i6++;
                        if (i6 < length) {
                            switch (str2.charAt(i6)) {
                                case '\"':
                                    break;
                                case '\\':
                                    i6++;
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (i6 < length) {
                        break;
                    }
                    addressException = r27;
                    addressException2 = new AddressException("Missing '\"'", str2, i6);
                    throw addressException;
                case '(':
                    obj22 = 1;
                    if (i5 >= 0 && i4 == -1) {
                        i4 = i6;
                    }
                    if (i == -1) {
                        i = i6 + 1;
                    }
                    i6++;
                    int i7 = 1;
                    while (i6 < length && i7 > 0) {
                        switch (str2.charAt(i6)) {
                            case '(':
                                i7++;
                                break;
                            case ')':
                                i7--;
                                break;
                            case '\\':
                                i6++;
                                break;
                            default:
                                break;
                        }
                        i6++;
                    }
                    if (i7 <= 0) {
                        i6--;
                        if (i2 != -1) {
                            break;
                        }
                        i2 = i6;
                        break;
                    }
                    addressException = r27;
                    addressException2 = new AddressException("Missing ')'", str2, i6);
                    throw addressException;
                    break;
                case ')':
                    addressException = r27;
                    addressException2 = new AddressException("Missing '('", str2, i6);
                    throw addressException;
                case ',':
                    if (i5 != -1) {
                        if (obj == null) {
                            if (i4 == -1) {
                                i4 = i6;
                            }
                            String trim = str2.substring(i5, i4).trim();
                            if (obj22 != null || z3 || z4) {
                                if (z3 || !z4) {
                                    checkAddress(trim, z5, false);
                                }
                                internetAddress = r27;
                                internetAddress2 = new InternetAddress();
                                internetAddress3 = internetAddress;
                                internetAddress3.setAddress(trim);
                                if (i >= 0) {
                                    internetAddress3.encodedPersonal = unquote(str2.substring(i, i2).trim());
                                    i3 = -1;
                                    i2 = i3;
                                    i = i3;
                                }
                                vector3.addElement(internetAddress3);
                            } else {
                                stringTokenizer = r27;
                                stringTokenizer2 = new StringTokenizer(trim);
                                StringTokenizer stringTokenizer3 = stringTokenizer;
                                while (stringTokenizer3.hasMoreTokens()) {
                                    String nextToken = stringTokenizer3.nextToken();
                                    checkAddress(nextToken, false, false);
                                    internetAddress = r27;
                                    internetAddress2 = new InternetAddress();
                                    internetAddress3 = internetAddress;
                                    internetAddress3.setAddress(nextToken);
                                    vector3.addElement(internetAddress3);
                                }
                            }
                            z5 = false;
                            obj22 = null;
                            i3 = -1;
                            i4 = i3;
                            i5 = i3;
                            break;
                        }
                        z5 = false;
                        break;
                    }
                    z5 = false;
                    obj22 = null;
                    i3 = -1;
                    i4 = i3;
                    i5 = i3;
                    break;
                    break;
                case ':':
                    obj22 = 1;
                    if (obj == null) {
                        int obj3 = 1;
                        if (i5 != -1) {
                            break;
                        }
                        i5 = i6;
                        break;
                    }
                    addressException = r27;
                    addressException2 = new AddressException("Nested group", str2, i6);
                    throw addressException;
                case MailcapTokenizer.SEMICOLON_TOKEN /*59*/:
                    if (i5 == -1) {
                        i5 = i6;
                    }
                    if (obj3 != null) {
                        obj3 = null;
                        if (i5 == -1) {
                            i5 = i6;
                        }
                        internetAddress = r27;
                        internetAddress2 = new InternetAddress();
                        internetAddress3 = internetAddress;
                        internetAddress3.setAddress(str2.substring(i5, i6 + 1).trim());
                        vector3.addElement(internetAddress3);
                        z5 = false;
                        i3 = -1;
                        i4 = i3;
                        i5 = i3;
                        break;
                    }
                    addressException = r27;
                    addressException2 = new AddressException("Illegal semicolon, not in group", str2, i6);
                    throw addressException;
                case '<':
                    obj22 = 1;
                    if (!z5) {
                        if (obj3 == null) {
                            i = i5;
                            if (i >= 0) {
                                i2 = i6;
                            }
                            i5 = i6 + 1;
                        }
                        Object obj4 = null;
                        while (true) {
                            i6++;
                            if (i6 < length) {
                                switch (str2.charAt(i6)) {
                                    case '\"':
                                        obj4 = obj4 != null ? null : 1;
                                        break;
                                    case '>':
                                        if (obj4 == null) {
                                            break;
                                        }
                                        break;
                                    case '\\':
                                        i6++;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        if (i6 < length) {
                            z5 = true;
                            i4 = i6;
                            break;
                        } else if (obj4 != null) {
                            addressException = r27;
                            addressException2 = new AddressException("Missing '\"'", str2, i6);
                            throw addressException;
                        } else {
                            addressException = r27;
                            addressException2 = new AddressException("Missing '>'", str2, i6);
                            throw addressException;
                        }
                    }
                    addressException = r27;
                    addressException2 = new AddressException("Extra route-addr", str2, i6);
                    throw addressException;
                case '>':
                    addressException = r27;
                    addressException2 = new AddressException("Missing '<'", str2, i6);
                    throw addressException;
                case '[':
                    obj22 = 1;
                    while (true) {
                        i6++;
                        if (i6 < length) {
                            switch (str2.charAt(i6)) {
                                case '\\':
                                    i6++;
                                    break;
                                case ']':
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    if (i6 < length) {
                        break;
                    }
                    addressException = r27;
                    addressException2 = new AddressException("Missing ']'", str2, i6);
                    throw addressException;
                default:
                    if (i5 != -1) {
                        break;
                    }
                    i5 = i6;
                    break;
            }
            i6++;
        }
        if (i5 >= 0) {
            if (i4 == -1) {
                i4 = i6;
            }
            String trim2 = str2.substring(i5, i4).trim();
            if (obj22 != null || z3 || z4) {
                if (z3 || !z4) {
                    checkAddress(trim2, z5, false);
                }
                internetAddress = r27;
                internetAddress2 = new InternetAddress();
                internetAddress3 = internetAddress;
                internetAddress3.setAddress(trim2);
                if (i >= 0) {
                    internetAddress3.encodedPersonal = unquote(str2.substring(i, i2).trim());
                }
                vector3.addElement(internetAddress3);
            } else {
                stringTokenizer = r27;
                stringTokenizer2 = new StringTokenizer(trim2);
                StringTokenizer stringTokenizer4 = stringTokenizer;
                while (stringTokenizer4.hasMoreTokens()) {
                    String nextToken2 = stringTokenizer4.nextToken();
                    checkAddress(nextToken2, false, false);
                    internetAddress = r27;
                    internetAddress2 = new InternetAddress();
                    internetAddress3 = internetAddress;
                    internetAddress3.setAddress(nextToken2);
                    vector3.addElement(internetAddress3);
                }
            }
        }
        InternetAddress[] internetAddressArr = new InternetAddress[vector3.size()];
        vector3.copyInto(internetAddressArr);
        return internetAddressArr;
    }

    public void validate() throws AddressException {
        checkAddress(getAddress(), true, true);
    }

    private static void checkAddress(String str, boolean z, boolean z2) throws AddressException {
        String str2 = str;
        boolean z3 = z;
        boolean z4 = z2;
        int i = 0;
        if (str2.indexOf(34) < 0) {
            int indexOfAny;
            int i2;
            AddressException addressException;
            AddressException addressException2;
            String substring;
            String substring2;
            if (z3) {
                int i3 = 0;
                while (true) {
                    i = i3;
                    indexOfAny = indexOfAny(str2, ",:", i);
                    i2 = indexOfAny;
                    if (indexOfAny < 0) {
                        break;
                    } else if (str2.charAt(i) != '@') {
                        addressException = r11;
                        addressException2 = new AddressException("Illegal route-addr", str2);
                        throw addressException;
                    } else if (str2.charAt(i2) == ':') {
                        i = i2 + 1;
                        break;
                    } else {
                        i3 = i2 + 1;
                    }
                }
            }
            indexOfAny = str2.indexOf(64, i);
            i2 = indexOfAny;
            if (indexOfAny >= 0) {
                if (i2 == i) {
                    addressException = r11;
                    addressException2 = new AddressException("Missing local name", str2);
                    throw addressException;
                } else if (i2 == str2.length() - 1) {
                    addressException = r11;
                    addressException2 = new AddressException("Missing domain", str2);
                    throw addressException;
                } else {
                    substring = str2.substring(i, i2);
                    substring2 = str2.substring(i2 + 1);
                }
            } else if (z4) {
                addressException = r11;
                addressException2 = new AddressException("Missing final '@domain'", str2);
                throw addressException;
            } else {
                substring = str2;
                substring2 = null;
            }
            if (indexOfAny(str2, " \t\n\r") >= 0) {
                addressException = r11;
                addressException2 = new AddressException("Illegal whitespace in address", str2);
                throw addressException;
            } else if (indexOfAny(substring, specialsNoDot) >= 0) {
                addressException = r11;
                addressException2 = new AddressException("Illegal character in local name", str2);
                throw addressException;
            } else if (substring2 != null && substring2.indexOf(91) < 0 && indexOfAny(substring2, specialsNoDot) >= 0) {
                addressException = r11;
                addressException2 = new AddressException("Illegal character in domain", str2);
                throw addressException;
            }
        }
    }

    private boolean isSimple() {
        return this.address == null || indexOfAny(this.address, specialsNoDotNoAt) < 0;
    }

    public boolean isGroup() {
        if (this.address == null || !this.address.endsWith(";") || this.address.indexOf(58) <= 0) {
            return false;
        }
        return true;
    }

    public InternetAddress[] getGroup(boolean z) throws AddressException {
        boolean z2 = z;
        Object obj = null;
        String address = getAddress();
        if (!address.endsWith(";")) {
            return null;
        }
        int indexOf = address.indexOf(58);
        if (indexOf < 0) {
            return null;
        }
        return parseHeader(address.substring(indexOf + 1, address.length() - 1), z2);
    }

    private static int indexOfAny(String str, String str2) {
        return indexOfAny(str, str2, 0);
    }

    private static int indexOfAny(String str, String str2, int i) {
        String str3 = str;
        String str4 = str2;
        int i2 = i;
        try {
            int length = str3.length();
            for (int i3 = i2; i3 < length; i3++) {
                if (str4.indexOf(str3.charAt(i3)) >= 0) {
                    return i3;
                }
            }
            return -1;
        } catch (StringIndexOutOfBoundsException e) {
            StringIndexOutOfBoundsException stringIndexOutOfBoundsException = e;
            return -1;
        }
    }
}
