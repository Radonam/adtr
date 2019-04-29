package javax.mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.BitSet;
import java.util.Locale;

public class URLName {
    static final int caseDiff = 32;
    private static boolean doEncode;
    static BitSet dontNeedEncoding;
    private String file;
    protected String fullURL;
    private int hashCode;
    private String host;
    private InetAddress hostAddress;
    private boolean hostAddressKnown;
    private String password;
    private int port;
    private String protocol;
    private String ref;
    private String username;

    static {
        int i;
        doEncode = true;
        try {
            boolean z;
            if (Boolean.getBoolean("mail.URLName.dontencode")) {
                z = false;
            } else {
                z = true;
            }
            doEncode = z;
        } catch (Exception e) {
            Exception exception = e;
        }
        BitSet bitSet = r4;
        BitSet bitSet2 = new BitSet(256);
        dontNeedEncoding = bitSet;
        for (i = 97; i <= 122; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 65; i <= 90; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 48; i <= 57; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set(32);
        dontNeedEncoding.set(45);
        dontNeedEncoding.set(95);
        dontNeedEncoding.set(46);
        dontNeedEncoding.set(42);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0070  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0053  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0072  */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x005f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public URLName(String str, String str2, int i, String str3, String str4, String str5) {
        String str6 = str;
        String str7 = str2;
        int i2 = i;
        String str8 = str3;
        String str9 = str4;
        String str10 = str5;
        this.hostAddressKnown = false;
        this.port = -1;
        this.hashCode = 0;
        this.protocol = str6;
        this.host = str7;
        this.port = i2;
        if (str8 != null) {
            int indexOf = str8.indexOf(35);
            int i3 = indexOf;
            if (indexOf != -1) {
                this.file = str8.substring(0, i3);
                this.ref = str8.substring(i3 + 1);
                this.username = doEncode ? encode(str9) : str9;
                this.password = doEncode ? encode(str10) : str10;
            }
        }
        this.file = str8;
        this.ref = null;
        if (doEncode) {
        }
        this.username = doEncode ? encode(str9) : str9;
        if (doEncode) {
        }
        this.password = doEncode ? encode(str10) : str10;
    }

    public URLName(URL url) {
        this(url.toString());
    }

    public URLName(String str) {
        String str2 = str;
        this.hostAddressKnown = false;
        this.port = -1;
        this.hashCode = 0;
        parseString(str2);
    }

    public String toString() {
        if (this.fullURL == null) {
            StringBuffer stringBuffer = r4;
            StringBuffer stringBuffer2 = new StringBuffer();
            StringBuffer stringBuffer3 = stringBuffer;
            if (this.protocol != null) {
                stringBuffer = stringBuffer3.append(this.protocol);
                stringBuffer = stringBuffer3.append(":");
            }
            if (!(this.username == null && this.host == null)) {
                stringBuffer = stringBuffer3.append("//");
                if (this.username != null) {
                    stringBuffer = stringBuffer3.append(this.username);
                    if (this.password != null) {
                        stringBuffer = stringBuffer3.append(":");
                        stringBuffer = stringBuffer3.append(this.password);
                    }
                    stringBuffer = stringBuffer3.append("@");
                }
                if (this.host != null) {
                    stringBuffer = stringBuffer3.append(this.host);
                }
                if (this.port != -1) {
                    stringBuffer = stringBuffer3.append(":");
                    stringBuffer = stringBuffer3.append(Integer.toString(this.port));
                }
                if (this.file != null) {
                    stringBuffer = stringBuffer3.append("/");
                }
            }
            if (this.file != null) {
                stringBuffer = stringBuffer3.append(this.file);
            }
            if (this.ref != null) {
                stringBuffer = stringBuffer3.append("#");
                stringBuffer = stringBuffer3.append(this.ref);
            }
            this.fullURL = stringBuffer3.toString();
        }
        return this.fullURL;
    }

    /* Access modifiers changed, original: protected */
    public void parseString(String str) {
        String str2 = str;
        String str3 = null;
        String str4 = str3;
        this.password = str3;
        str3 = str4;
        String str5 = str3;
        this.username = str3;
        str3 = str5;
        String str6 = str3;
        this.host = str3;
        str3 = str6;
        String str7 = str3;
        this.ref = str3;
        str3 = str7;
        String str8 = str3;
        this.file = str3;
        this.protocol = str8;
        this.port = -1;
        int length = str2.length();
        int indexOf = str2.indexOf(58);
        if (indexOf != -1) {
            this.protocol = str2.substring(0, indexOf);
        }
        if (str2.regionMatches(indexOf + 1, "//", 0, 2)) {
            String substring;
            int indexOf2;
            Object obj = null;
            int indexOf3 = str2.indexOf(47, indexOf + 3);
            if (indexOf3 != -1) {
                substring = str2.substring(indexOf + 3, indexOf3);
                if (indexOf3 + 1 < length) {
                    this.file = str2.substring(indexOf3 + 1);
                } else {
                    this.file = "";
                }
            } else {
                substring = str2.substring(indexOf + 3);
            }
            int indexOf4 = substring.indexOf(64);
            if (indexOf4 != -1) {
                String substring2 = substring.substring(0, indexOf4);
                substring = substring.substring(indexOf4 + 1);
                int indexOf5 = substring2.indexOf(58);
                if (indexOf5 != -1) {
                    this.username = substring2.substring(0, indexOf5);
                    this.password = substring2.substring(indexOf5 + 1);
                } else {
                    this.username = substring2;
                }
            }
            if (substring.length() <= 0 || substring.charAt(0) != '[') {
                indexOf2 = substring.indexOf(58);
            } else {
                indexOf2 = substring.indexOf(58, substring.indexOf(93));
            }
            if (indexOf2 != -1) {
                String substring3 = substring.substring(indexOf2 + 1);
                if (substring3.length() > 0) {
                    try {
                        this.port = Integer.parseInt(substring3);
                    } catch (NumberFormatException e) {
                        NumberFormatException numberFormatException = e;
                        this.port = -1;
                    }
                }
                this.host = substring.substring(0, indexOf2);
            } else {
                this.host = substring;
            }
        } else if (indexOf + 1 < length) {
            this.file = str2.substring(indexOf + 1);
        }
        if (this.file != null) {
            int indexOf6 = this.file.indexOf(35);
            int i = indexOf6;
            if (indexOf6 != -1) {
                this.ref = this.file.substring(i + 1);
                this.file = this.file.substring(0, i);
            }
        }
    }

    public int getPort() {
        return this.port;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getFile() {
        return this.file;
    }

    public String getRef() {
        return this.ref;
    }

    public String getHost() {
        return this.host;
    }

    public String getUsername() {
        return doEncode ? decode(this.username) : this.username;
    }

    public String getPassword() {
        return doEncode ? decode(this.password) : this.password;
    }

    public URL getURL() throws MalformedURLException {
        URL url = r7;
        URL url2 = new URL(getProtocol(), getHost(), getPort(), getFile());
        return url;
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (!(obj2 instanceof URLName)) {
            return false;
        }
        URLName uRLName = (URLName) obj2;
        if (uRLName.protocol == null || !uRLName.protocol.equals(this.protocol)) {
            return false;
        }
        InetAddress hostAddress = getHostAddress();
        InetAddress hostAddress2 = uRLName.getHostAddress();
        if (hostAddress == null || hostAddress2 == null) {
            if (this.host == null || uRLName.host == null) {
                if (this.host != uRLName.host) {
                    return false;
                }
            } else if (!this.host.equalsIgnoreCase(uRLName.host)) {
                return false;
            }
        } else if (!hostAddress.equals(hostAddress2)) {
            return false;
        }
        if (this.username != uRLName.username && (this.username == null || !this.username.equals(uRLName.username))) {
            return false;
        }
        if (!(this.file == null ? "" : this.file).equals(uRLName.file == null ? "" : uRLName.file)) {
            return false;
        }
        if (this.port != uRLName.port) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        if (this.protocol != null) {
            this.hashCode += this.protocol.hashCode();
        }
        InetAddress hostAddress = getHostAddress();
        if (hostAddress != null) {
            this.hashCode += hostAddress.hashCode();
        } else if (this.host != null) {
            this.hashCode += this.host.toLowerCase(Locale.ENGLISH).hashCode();
        }
        if (this.username != null) {
            this.hashCode += this.username.hashCode();
        }
        if (this.file != null) {
            this.hashCode += this.file.hashCode();
        }
        this.hashCode += this.port;
        return this.hashCode;
    }

    private synchronized InetAddress getHostAddress() {
        InetAddress inetAddress;
        synchronized (this) {
            if (this.hostAddressKnown) {
                inetAddress = this.hostAddress;
            } else if (this.host == null) {
                inetAddress = null;
            } else {
                try {
                    this.hostAddress = InetAddress.getByName(this.host);
                } catch (UnknownHostException e) {
                    UnknownHostException unknownHostException = e;
                    this.hostAddress = null;
                }
                this.hostAddressKnown = true;
                inetAddress = this.hostAddress;
            }
        }
        return inetAddress;
    }

    static String encode(String str) {
        String str2 = str;
        if (str2 == null) {
            return null;
        }
        for (int i = 0; i < str2.length(); i++) {
            char charAt = str2.charAt(i);
            if (charAt == ' ' || !dontNeedEncoding.get(charAt)) {
                return _encode(str2);
            }
        }
        return str2;
    }

    private static String _encode(String str) {
        String str2 = str;
        int i = 10;
        StringBuffer stringBuffer = r13;
        StringBuffer stringBuffer2 = new StringBuffer(str2.length());
        StringBuffer stringBuffer3 = stringBuffer;
        OutputStream outputStream = r13;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream(i);
        OutputStream outputStream2 = outputStream;
        OutputStreamWriter outputStreamWriter = r13;
        OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2);
        OutputStreamWriter outputStreamWriter3 = outputStreamWriter;
        for (int i2 = 0; i2 < str2.length(); i2++) {
            char charAt = str2.charAt(i2);
            if (dontNeedEncoding.get(charAt)) {
                if (charAt == ' ') {
                    charAt = '+';
                }
                stringBuffer = stringBuffer3.append((char) charAt);
            } else {
                try {
                    outputStreamWriter3.write(charAt);
                    outputStreamWriter3.flush();
                    byte[] toByteArray = outputStream2.toByteArray();
                    for (int i3 = 0; i3 < toByteArray.length; i3++) {
                        stringBuffer = stringBuffer3.append('%');
                        char forDigit = Character.forDigit((toByteArray[i3] >> 4) & 15, 16);
                        if (Character.isLetter(forDigit)) {
                            forDigit = (char) (forDigit - 32);
                        }
                        stringBuffer = stringBuffer3.append(forDigit);
                        forDigit = Character.forDigit(toByteArray[i3] & 15, 16);
                        if (Character.isLetter(forDigit)) {
                            forDigit = (char) (forDigit - 32);
                        }
                        stringBuffer = stringBuffer3.append(forDigit);
                    }
                    outputStream2.reset();
                } catch (IOException e) {
                    IOException iOException = e;
                    outputStream2.reset();
                }
            }
        }
        return stringBuffer3.toString();
    }

    static String decode(String str) {
        String str2 = str;
        if (str2 == null) {
            return null;
        }
        if (indexOfAny(str2, "+%") == -1) {
            return str2;
        }
        StringBuffer stringBuffer = r10;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        int i = 0;
        while (i < str2.length()) {
            char charAt = str2.charAt(i);
            switch (charAt) {
                case '%':
                    try {
                        stringBuffer = stringBuffer3.append((char) Integer.parseInt(str2.substring(i + 1, i + 3), 16));
                        i += 2;
                        break;
                    } catch (NumberFormatException e) {
                        NumberFormatException numberFormatException = e;
                        IllegalArgumentException illegalArgumentException = r10;
                        IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException();
                        throw illegalArgumentException;
                    }
                case '+':
                    stringBuffer = stringBuffer3.append(' ');
                    break;
                default:
                    stringBuffer = stringBuffer3.append(charAt);
                    break;
            }
            i++;
        }
        String stringBuffer4 = stringBuffer3.toString();
        try {
            String str3 = r10;
            String str4 = new String(stringBuffer4.getBytes("8859_1"));
            stringBuffer4 = str3;
        } catch (UnsupportedEncodingException e2) {
            UnsupportedEncodingException unsupportedEncodingException = e2;
        }
        return stringBuffer4;
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
