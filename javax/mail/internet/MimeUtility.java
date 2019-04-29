package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;
import com.sun.mail.util.BEncoderStream;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.QDecoderStream;
import com.sun.mail.util.QEncoderStream;
import com.sun.mail.util.QPDecoderStream;
import com.sun.mail.util.QPEncoderStream;
import com.sun.mail.util.UUDecoderStream;
import com.sun.mail.util.UUEncoderStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;

public class MimeUtility {
    public static final int ALL = -1;
    static final int ALL_ASCII = 1;
    static final int MOSTLY_ASCII = 2;
    static final int MOSTLY_NONASCII = 3;
    private static boolean decodeStrict;
    private static String defaultJavaCharset;
    private static String defaultMIMECharset;
    private static boolean encodeEolStrict;
    private static boolean foldEncodedWords;
    private static boolean foldText;
    private static Hashtable java2mime;
    private static Hashtable mime2java;

    private MimeUtility() {
    }

    static {
        Exception exception;
        Object put;
        decodeStrict = true;
        encodeEolStrict = false;
        foldEncodedWords = false;
        foldText = true;
        try {
            String property = System.getProperty("mail.mime.decodetext.strict");
            boolean z = property == null || !property.equalsIgnoreCase("false");
            decodeStrict = z;
            property = System.getProperty("mail.mime.encodeeol.strict");
            z = property != null && property.equalsIgnoreCase("true");
            encodeEolStrict = z;
            property = System.getProperty("mail.mime.foldencodedwords");
            z = property != null && property.equalsIgnoreCase("true");
            foldEncodedWords = z;
            property = System.getProperty("mail.mime.foldtext");
            if (property == null || !property.equalsIgnoreCase("false")) {
                z = true;
            } else {
                z = false;
            }
            foldText = z;
        } catch (SecurityException e) {
            SecurityException securityException = e;
        }
        Hashtable hashtable = r6;
        Hashtable hashtable2 = new Hashtable(40);
        java2mime = hashtable;
        hashtable = r6;
        hashtable2 = new Hashtable(10);
        mime2java = hashtable;
        InputStream resourceAsStream;
        try {
            resourceAsStream = MimeUtility.class.getResourceAsStream("/META-INF/javamail.charset.map");
            if (resourceAsStream != null) {
                InputStream inputStream = r6;
                InputStream lineInputStream = new LineInputStream(resourceAsStream);
                resourceAsStream = inputStream;
                loadMappings((LineInputStream) resourceAsStream, java2mime);
                loadMappings((LineInputStream) resourceAsStream, mime2java);
                try {
                    resourceAsStream.close();
                } catch (Exception e2) {
                    exception = e2;
                }
            }
        } catch (Exception e22) {
            Exception exception2 = e22;
        } catch (Throwable th) {
            Throwable th2 = th;
            try {
                resourceAsStream.close();
            } catch (Exception e222) {
                exception = e222;
            }
            Throwable th3 = th2;
        }
        if (java2mime.isEmpty()) {
            put = java2mime.put("8859_1", "ISO-8859-1");
            put = java2mime.put("iso8859_1", "ISO-8859-1");
            put = java2mime.put("iso8859-1", "ISO-8859-1");
            put = java2mime.put("8859_2", "ISO-8859-2");
            put = java2mime.put("iso8859_2", "ISO-8859-2");
            put = java2mime.put("iso8859-2", "ISO-8859-2");
            put = java2mime.put("8859_3", "ISO-8859-3");
            put = java2mime.put("iso8859_3", "ISO-8859-3");
            put = java2mime.put("iso8859-3", "ISO-8859-3");
            put = java2mime.put("8859_4", "ISO-8859-4");
            put = java2mime.put("iso8859_4", "ISO-8859-4");
            put = java2mime.put("iso8859-4", "ISO-8859-4");
            put = java2mime.put("8859_5", "ISO-8859-5");
            put = java2mime.put("iso8859_5", "ISO-8859-5");
            put = java2mime.put("iso8859-5", "ISO-8859-5");
            put = java2mime.put("8859_6", "ISO-8859-6");
            put = java2mime.put("iso8859_6", "ISO-8859-6");
            put = java2mime.put("iso8859-6", "ISO-8859-6");
            put = java2mime.put("8859_7", "ISO-8859-7");
            put = java2mime.put("iso8859_7", "ISO-8859-7");
            put = java2mime.put("iso8859-7", "ISO-8859-7");
            put = java2mime.put("8859_8", "ISO-8859-8");
            put = java2mime.put("iso8859_8", "ISO-8859-8");
            put = java2mime.put("iso8859-8", "ISO-8859-8");
            put = java2mime.put("8859_9", "ISO-8859-9");
            put = java2mime.put("iso8859_9", "ISO-8859-9");
            put = java2mime.put("iso8859-9", "ISO-8859-9");
            put = java2mime.put("sjis", "Shift_JIS");
            put = java2mime.put("jis", "ISO-2022-JP");
            put = java2mime.put("iso2022jp", "ISO-2022-JP");
            put = java2mime.put("euc_jp", "euc-jp");
            put = java2mime.put("koi8_r", "koi8-r");
            put = java2mime.put("euc_cn", "euc-cn");
            put = java2mime.put("euc_tw", "euc-tw");
            put = java2mime.put("euc_kr", "euc-kr");
        }
        if (mime2java.isEmpty()) {
            put = mime2java.put("iso-2022-cn", "ISO2022CN");
            put = mime2java.put("iso-2022-kr", "ISO2022KR");
            put = mime2java.put("utf-8", "UTF8");
            put = mime2java.put("utf8", "UTF8");
            put = mime2java.put("ja_jp.iso2022-7", "ISO2022JP");
            put = mime2java.put("ja_jp.eucjp", "EUCJIS");
            put = mime2java.put("euc-kr", "KSC5601");
            put = mime2java.put("euckr", "KSC5601");
            put = mime2java.put("us-ascii", "ISO-8859-1");
            put = mime2java.put("x-us-ascii", "ISO-8859-1");
        }
    }

    public static String getEncoding(DataSource dataSource) {
        DataSource dataSource2 = dataSource;
        Object obj = null;
        Object obj2 = null;
        Object obj3 = null;
        try {
            String str;
            ContentType contentType = r10;
            ContentType contentType2 = new ContentType(dataSource2.getContentType());
            InputStream inputStream = dataSource2.getInputStream();
            switch (checkAscii(inputStream, -1, !contentType.match("text/*"))) {
                case 1:
                    str = "7bit";
                    break;
                case 2:
                    str = "quoted-printable";
                    break;
                default:
                    str = "base64";
                    break;
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                IOException iOException = e;
            }
            return str;
        } catch (Exception e2) {
            Exception exception = e2;
            return "base64";
        }
    }

    public static String getEncoding(DataHandler dataHandler) {
        IOException iOException;
        DataHandler dataHandler2 = dataHandler;
        Object obj = null;
        Object obj2 = null;
        if (dataHandler2.getName() != null) {
            return getEncoding(dataHandler2.getDataSource());
        }
        try {
            String str;
            ContentType contentType = r9;
            ContentType contentType2 = new ContentType(dataHandler2.getContentType());
            OutputStream outputStream;
            OutputStream asciiOutputStream;
            OutputStream outputStream2;
            if (contentType.match("text/*")) {
                outputStream = r9;
                asciiOutputStream = new AsciiOutputStream(false, false);
                outputStream2 = outputStream;
                try {
                    dataHandler2.writeTo(outputStream2);
                } catch (IOException e) {
                    iOException = e;
                }
                switch (outputStream2.getAscii()) {
                    case 1:
                        str = "7bit";
                        break;
                    case 2:
                        str = "quoted-printable";
                        break;
                    default:
                        str = "base64";
                        break;
                }
            }
            outputStream = r9;
            asciiOutputStream = new AsciiOutputStream(true, encodeEolStrict);
            outputStream2 = outputStream;
            try {
                dataHandler2.writeTo(outputStream2);
            } catch (IOException e2) {
                iOException = e2;
            }
            if (outputStream2.getAscii() == 1) {
                str = "7bit";
            } else {
                str = "base64";
            }
            return str;
        } catch (Exception e3) {
            Exception exception = e3;
            return "base64";
        }
    }

    public static InputStream decode(InputStream inputStream, String str) throws MessagingException {
        InputStream inputStream2 = inputStream;
        String str2 = str;
        InputStream inputStream3;
        InputStream bASE64DecoderStream;
        if (str2.equalsIgnoreCase("base64")) {
            inputStream3 = r7;
            bASE64DecoderStream = new BASE64DecoderStream(inputStream2);
            return inputStream3;
        } else if (str2.equalsIgnoreCase("quoted-printable")) {
            inputStream3 = r7;
            bASE64DecoderStream = new QPDecoderStream(inputStream2);
            return inputStream3;
        } else if (str2.equalsIgnoreCase("uuencode") || str2.equalsIgnoreCase("x-uuencode") || str2.equalsIgnoreCase("x-uue")) {
            inputStream3 = r7;
            bASE64DecoderStream = new UUDecoderStream(inputStream2);
            return inputStream3;
        } else if (str2.equalsIgnoreCase("binary") || str2.equalsIgnoreCase("7bit") || str2.equalsIgnoreCase("8bit")) {
            return inputStream2;
        } else {
            MessagingException messagingException = r7;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder("Unknown encoding: ");
            MessagingException messagingException2 = new MessagingException(stringBuilder.append(str2).toString());
            throw messagingException;
        }
    }

    public static OutputStream encode(OutputStream outputStream, String str) throws MessagingException {
        OutputStream outputStream2 = outputStream;
        String str2 = str;
        if (str2 == null) {
            return outputStream2;
        }
        OutputStream outputStream3;
        OutputStream bASE64EncoderStream;
        if (str2.equalsIgnoreCase("base64")) {
            outputStream3 = r7;
            bASE64EncoderStream = new BASE64EncoderStream(outputStream2);
            return outputStream3;
        } else if (str2.equalsIgnoreCase("quoted-printable")) {
            outputStream3 = r7;
            bASE64EncoderStream = new QPEncoderStream(outputStream2);
            return outputStream3;
        } else if (str2.equalsIgnoreCase("uuencode") || str2.equalsIgnoreCase("x-uuencode") || str2.equalsIgnoreCase("x-uue")) {
            outputStream3 = r7;
            bASE64EncoderStream = new UUEncoderStream(outputStream2);
            return outputStream3;
        } else if (str2.equalsIgnoreCase("binary") || str2.equalsIgnoreCase("7bit") || str2.equalsIgnoreCase("8bit")) {
            return outputStream2;
        } else {
            MessagingException messagingException = r7;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder("Unknown encoding: ");
            MessagingException messagingException2 = new MessagingException(stringBuilder.append(str2).toString());
            throw messagingException;
        }
    }

    public static OutputStream encode(OutputStream outputStream, String str, String str2) throws MessagingException {
        OutputStream outputStream2 = outputStream;
        String str3 = str;
        String str4 = str2;
        if (str3 == null) {
            return outputStream2;
        }
        OutputStream outputStream3;
        OutputStream bASE64EncoderStream;
        if (str3.equalsIgnoreCase("base64")) {
            outputStream3 = r8;
            bASE64EncoderStream = new BASE64EncoderStream(outputStream2);
            return outputStream3;
        } else if (str3.equalsIgnoreCase("quoted-printable")) {
            outputStream3 = r8;
            bASE64EncoderStream = new QPEncoderStream(outputStream2);
            return outputStream3;
        } else if (str3.equalsIgnoreCase("uuencode") || str3.equalsIgnoreCase("x-uuencode") || str3.equalsIgnoreCase("x-uue")) {
            outputStream3 = r8;
            bASE64EncoderStream = new UUEncoderStream(outputStream2, str4);
            return outputStream3;
        } else if (str3.equalsIgnoreCase("binary") || str3.equalsIgnoreCase("7bit") || str3.equalsIgnoreCase("8bit")) {
            return outputStream2;
        } else {
            MessagingException messagingException = r8;
            StringBuilder stringBuilder = r8;
            StringBuilder stringBuilder2 = new StringBuilder("Unknown encoding: ");
            MessagingException messagingException2 = new MessagingException(stringBuilder.append(str3).toString());
            throw messagingException;
        }
    }

    public static String encodeText(String str) throws UnsupportedEncodingException {
        return encodeText(str, null, null);
    }

    public static String encodeText(String str, String str2, String str3) throws UnsupportedEncodingException {
        return encodeWord(str, str2, str3, false);
    }

    public static String decodeText(String str) throws UnsupportedEncodingException {
        String decodeWord;
        String str2 = str;
        String str3 = " \t\n\r";
        if (str2.indexOf("=?") == -1) {
            return str2;
        }
        StringTokenizer stringTokenizer = r16;
        StringTokenizer stringTokenizer2 = new StringTokenizer(str2, str3, true);
        StringTokenizer stringTokenizer3 = stringTokenizer;
        StringBuffer stringBuffer = r16;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        stringBuffer = r16;
        stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer4 = stringBuffer;
        boolean z = false;
        while (stringTokenizer3.hasMoreTokens()) {
            String nextToken = stringTokenizer3.nextToken();
            char charAt = nextToken.charAt(0);
            char c = charAt;
            if (charAt == ' ' || c == 9 || c == 13 || c == 10) {
                stringBuffer = stringBuffer4.append(c);
            } else {
                try {
                    decodeWord = decodeWord(nextToken);
                    if (!z && stringBuffer4.length() > 0) {
                        stringBuffer = stringBuffer3.append(stringBuffer4);
                    }
                    z = true;
                } catch (ParseException e) {
                    ParseException parseException = e;
                    decodeWord = nextToken;
                    if (decodeStrict) {
                        if (stringBuffer4.length() > 0) {
                            stringBuffer = stringBuffer3.append(stringBuffer4);
                        }
                        z = false;
                    } else {
                        String decodeInnerWords = decodeInnerWords(decodeWord);
                        if (decodeInnerWords != decodeWord) {
                            if (!(z && decodeWord.startsWith("=?")) && stringBuffer4.length() > 0) {
                                stringBuffer = stringBuffer3.append(stringBuffer4);
                            }
                            z = decodeWord.endsWith("?=");
                            decodeWord = decodeInnerWords;
                        } else {
                            if (stringBuffer4.length() > 0) {
                                stringBuffer = stringBuffer3.append(stringBuffer4);
                            }
                            z = false;
                        }
                    }
                }
                stringBuffer = stringBuffer3.append(decodeWord);
                stringBuffer4.setLength(0);
            }
        }
        stringBuffer = stringBuffer3.append(stringBuffer4);
        return stringBuffer3.toString();
    }

    public static String encodeWord(String str) throws UnsupportedEncodingException {
        return encodeWord(str, null, null);
    }

    public static String encodeWord(String str, String str2, String str3) throws UnsupportedEncodingException {
        return encodeWord(str, str2, str3, true);
    }

    private static String encodeWord(String str, String str2, String str3, boolean z) throws UnsupportedEncodingException {
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        boolean z2 = z;
        int checkAscii = checkAscii(str4);
        if (checkAscii == 1) {
            return str4;
        }
        String defaultJavaCharset;
        boolean z3;
        if (str5 == null) {
            defaultJavaCharset = getDefaultJavaCharset();
            str5 = getDefaultMIMECharset();
        } else {
            defaultJavaCharset = javaCharset(str5);
        }
        if (str6 == null) {
            if (checkAscii != 3) {
                str6 = "Q";
            } else {
                str6 = "B";
            }
        }
        if (str6.equalsIgnoreCase("B")) {
            z3 = true;
        } else if (str6.equalsIgnoreCase("Q")) {
            z3 = false;
        } else {
            UnsupportedEncodingException unsupportedEncodingException = r16;
            StringBuilder stringBuilder = r16;
            StringBuilder stringBuilder2 = new StringBuilder("Unknown transfer encoding: ");
            UnsupportedEncodingException unsupportedEncodingException2 = new UnsupportedEncodingException(stringBuilder.append(str6).toString());
            throw unsupportedEncodingException;
        }
        StringBuffer stringBuffer = r16;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        String str7 = str4;
        boolean z4 = z3;
        String str8 = defaultJavaCharset;
        int length = 68 - str5.length();
        StringBuilder stringBuilder3 = r16;
        StringBuilder stringBuilder4 = new StringBuilder("=?");
        doEncode(str7, z4, str8, length, stringBuilder3.append(str5).append("?").append(str6).append("?").toString(), true, z2, stringBuffer3);
        return stringBuffer3.toString();
    }

    private static void doEncode(String str, boolean z, String str2, int i, String str3, boolean z2, boolean z3, StringBuffer stringBuffer) throws UnsupportedEncodingException {
        int encodedLength;
        OutputStream outputStream;
        StringBuffer append;
        String str4 = str;
        boolean z4 = z;
        String str5 = str2;
        int i2 = i;
        String str6 = str3;
        boolean z5 = z2;
        boolean z6 = z3;
        StringBuffer stringBuffer2 = stringBuffer;
        byte[] bytes = str4.getBytes(str5);
        if (z4) {
            encodedLength = BEncoderStream.encodedLength(bytes);
        } else {
            encodedLength = QEncoderStream.encodedLength(bytes, z6);
        }
        if (encodedLength > i2) {
            int length = str4.length();
            int i3 = length;
            if (length > 1) {
                doEncode(str4.substring(0, i3 / 2), z4, str5, i2, str6, z5, z6, stringBuffer2);
                doEncode(str4.substring(i3 / 2, i3), z4, str5, i2, str6, false, z6, stringBuffer2);
                return;
            }
        }
        OutputStream outputStream2 = r25;
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream3 = outputStream2;
        if (z4) {
            outputStream2 = r25;
            byteArrayOutputStream = new BEncoderStream(outputStream3);
            outputStream = outputStream2;
        } else {
            outputStream2 = r25;
            byteArrayOutputStream = new QEncoderStream(outputStream3, z6);
            outputStream = outputStream2;
        }
        try {
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            IOException iOException = e;
        }
        byte[] toByteArray = outputStream3.toByteArray();
        if (!z5) {
            if (foldEncodedWords) {
                append = stringBuffer2.append("\r\n ");
            } else {
                append = stringBuffer2.append(" ");
            }
        }
        append = stringBuffer2.append(str6);
        for (byte b : toByteArray) {
            append = stringBuffer2.append((char) b);
        }
        append = stringBuffer2.append("?=");
    }

    public static String decodeWord(String str) throws ParseException, UnsupportedEncodingException {
        String str2 = str;
        ParseException parseException;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        ParseException parseException2;
        if (str2.startsWith("=?")) {
            int i = 2;
            int indexOf = str2.indexOf(63, i);
            int i2 = indexOf;
            if (indexOf == -1) {
                parseException = r17;
                stringBuilder = r17;
                stringBuilder2 = new StringBuilder("encoded word does not include charset: ");
                parseException2 = new ParseException(stringBuilder.append(str2).toString());
                throw parseException;
            }
            String javaCharset = javaCharset(str2.substring(i, i2));
            i = i2 + 1;
            indexOf = str2.indexOf(63, i);
            i2 = indexOf;
            if (indexOf == -1) {
                parseException = r17;
                stringBuilder = r17;
                stringBuilder2 = new StringBuilder("encoded word does not include encoding: ");
                parseException2 = new ParseException(stringBuilder.append(str2).toString());
                throw parseException;
            }
            String substring = str2.substring(i, i2);
            i = i2 + 1;
            indexOf = str2.indexOf("?=", i);
            i2 = indexOf;
            if (indexOf == -1) {
                parseException = r17;
                stringBuilder = r17;
                stringBuilder2 = new StringBuilder("encoded word does not end with \"?=\": ");
                parseException2 = new ParseException(stringBuilder.append(str2).toString());
                throw parseException;
            }
            String substring2 = str2.substring(i, i2);
            UnsupportedEncodingException unsupportedEncodingException;
            UnsupportedEncodingException unsupportedEncodingException2;
            try {
                String str3;
                if (substring2.length() > 0) {
                    InputStream inputStream;
                    String str4;
                    InputStream inputStream2 = r17;
                    InputStream byteArrayInputStream = new ByteArrayInputStream(ASCIIUtility.getBytes(substring2));
                    InputStream inputStream3 = inputStream2;
                    if (substring.equalsIgnoreCase("B")) {
                        inputStream2 = r17;
                        byteArrayInputStream = new BASE64DecoderStream(inputStream3);
                        inputStream = inputStream2;
                    } else if (substring.equalsIgnoreCase("Q")) {
                        inputStream2 = r17;
                        byteArrayInputStream = new QDecoderStream(inputStream3);
                        inputStream = inputStream2;
                    } else {
                        unsupportedEncodingException = r17;
                        stringBuilder = r17;
                        stringBuilder2 = new StringBuilder("unknown encoding: ");
                        unsupportedEncodingException2 = new UnsupportedEncodingException(stringBuilder.append(substring).toString());
                        throw unsupportedEncodingException;
                    }
                    int available = inputStream3.available();
                    byte[] bArr = new byte[available];
                    available = inputStream.read(bArr, 0, available);
                    if (available <= 0) {
                        str4 = "";
                    } else {
                        str4 = r17;
                        String str5 = new String(bArr, 0, available, javaCharset);
                    }
                    str3 = str4;
                } else {
                    str3 = "";
                }
                if (i2 + 2 < str2.length()) {
                    String substring3 = str2.substring(i2 + 2);
                    if (!decodeStrict) {
                        substring3 = decodeInnerWords(substring3);
                    }
                    StringBuilder stringBuilder3 = r17;
                    StringBuilder stringBuilder4 = new StringBuilder(String.valueOf(str3));
                    str3 = stringBuilder3.append(substring3).toString();
                }
                return str3;
            } catch (UnsupportedEncodingException unsupportedEncodingException3) {
                throw unsupportedEncodingException3;
            } catch (IOException e) {
                IOException iOException = e;
                parseException = r17;
                parseException2 = new ParseException(iOException.toString());
                throw parseException;
            } catch (IllegalArgumentException e2) {
                IllegalArgumentException illegalArgumentException = e2;
                unsupportedEncodingException3 = r17;
                unsupportedEncodingException2 = new UnsupportedEncodingException(javaCharset);
                throw unsupportedEncodingException3;
            }
        }
        parseException = r17;
        stringBuilder = r17;
        stringBuilder2 = new StringBuilder("encoded word does not start with \"=?\": ");
        parseException2 = new ParseException(stringBuilder.append(str2).toString());
        throw parseException;
    }

    private static String decodeInnerWords(String str) throws UnsupportedEncodingException {
        String str2 = str;
        int i = 0;
        StringBuffer stringBuffer = r11;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        while (true) {
            int indexOf = str2.indexOf("=?", i);
            int i2 = indexOf;
            if (indexOf < 0) {
                break;
            }
            stringBuffer = stringBuffer3.append(str2.substring(i, i2));
            int indexOf2 = str2.indexOf(63, i2 + 2);
            if (indexOf2 < 0) {
                break;
            }
            indexOf2 = str2.indexOf(63, indexOf2 + 1);
            if (indexOf2 < 0) {
                break;
            }
            indexOf2 = str2.indexOf("?=", indexOf2 + 1);
            if (indexOf2 < 0) {
                break;
            }
            String substring = str2.substring(i2, indexOf2 + 2);
            try {
                substring = decodeWord(substring);
            } catch (ParseException e) {
                ParseException parseException = e;
            }
            stringBuffer = stringBuffer3.append(substring);
            i = indexOf2 + 2;
        }
        if (i == 0) {
            return str2;
        }
        if (i < str2.length()) {
            stringBuffer = stringBuffer3.append(str2.substring(i));
        }
        return stringBuffer3.toString();
    }

    public static String quote(String str, String str2) {
        StringBuffer stringBuffer;
        StringBuffer stringBuffer2;
        String str3 = str;
        String str4 = str2;
        int length = str3.length();
        Object obj = null;
        for (int i = 0; i < length; i++) {
            char charAt = str3.charAt(i);
            if (charAt == '\"' || charAt == '\\' || charAt == 13 || charAt == 10) {
                stringBuffer = r14;
                stringBuffer2 = new StringBuffer(length + 3);
                StringBuffer stringBuffer3 = stringBuffer;
                stringBuffer = stringBuffer3.append('\"');
                stringBuffer = stringBuffer3.append(str3.substring(0, i));
                char c = 0;
                for (int i2 = i; i2 < length; i2++) {
                    char charAt2 = str3.charAt(i2);
                    if ((charAt2 == '\"' || charAt2 == '\\' || charAt2 == 13 || charAt2 == 10) && !(charAt2 == 10 && c == 13)) {
                        stringBuffer = stringBuffer3.append('\\');
                    }
                    stringBuffer = stringBuffer3.append(charAt2);
                    c = charAt2;
                }
                stringBuffer = stringBuffer3.append('\"');
                return stringBuffer3.toString();
            }
            if (charAt < ' ' || charAt >= 127 || str4.indexOf(charAt) >= 0) {
                obj = 1;
            }
        }
        if (obj == null) {
            return str3;
        }
        stringBuffer = r14;
        stringBuffer2 = new StringBuffer(length + 2);
        StringBuffer stringBuffer4 = stringBuffer;
        stringBuffer = stringBuffer4.append('\"').append(str3).append('\"');
        return stringBuffer4.toString();
    }

    public static String fold(int i, String str) {
        int i2 = i;
        String str2 = str;
        if (!foldText) {
            return str2;
        }
        char charAt;
        int length = str2.length() - 1;
        while (length >= 0) {
            charAt = str2.charAt(length);
            if (charAt != ' ' && charAt != 9 && charAt != 13 && charAt != 10) {
                break;
            }
            length--;
        }
        if (length != str2.length() - 1) {
            str2 = str2.substring(0, length + 1);
        }
        if (i2 + str2.length() <= 76) {
            return str2;
        }
        StringBuffer stringBuffer = r12;
        StringBuffer stringBuffer2 = new StringBuffer(str2.length() + 4);
        StringBuffer stringBuffer3 = stringBuffer;
        char c = 0;
        while (i2 + str2.length() > 76) {
            int i3 = -1;
            int i4 = 0;
            while (i4 < str2.length() && (i3 == -1 || i2 + i4 <= 76)) {
                charAt = str2.charAt(i4);
                if (!((charAt != ' ' && charAt != 9) || r5 == ' ' || r5 == 9)) {
                    i3 = i4;
                }
                c = charAt;
                i4++;
            }
            if (i3 == -1) {
                stringBuffer = stringBuffer3.append(str2);
                str2 = "";
                Object obj = null;
                break;
            }
            stringBuffer = stringBuffer3.append(str2.substring(0, i3));
            stringBuffer = stringBuffer3.append("\r\n");
            c = str2.charAt(i3);
            stringBuffer = stringBuffer3.append(c);
            str2 = str2.substring(i3 + 1);
            i2 = 1;
        }
        stringBuffer = stringBuffer3.append(str2);
        return stringBuffer3.toString();
    }

    public static String unfold(String str) {
        String str2 = str;
        if (!foldText) {
            return str2;
        }
        StringBuffer stringBuffer;
        StringBuffer stringBuffer2 = null;
        while (true) {
            int indexOfAny = indexOfAny(str2, "\r\n");
            int i = indexOfAny;
            if (indexOfAny < 0) {
                break;
            }
            int i2 = i;
            int length = str2.length();
            i++;
            if (i < length && str2.charAt(i - 1) == 13 && str2.charAt(i) == 10) {
                i++;
            }
            StringBuffer stringBuffer3;
            if (i2 == 0 || str2.charAt(i2 - 1) != '\\') {
                if (i < length) {
                    char charAt = str2.charAt(i);
                    char c = charAt;
                    if (charAt == ' ' || c == 9) {
                        while (true) {
                            i++;
                            if (i >= length) {
                                break;
                            }
                            charAt = str2.charAt(i);
                            c = charAt;
                            if (charAt != ' ' && c != 9) {
                                break;
                            }
                        }
                        if (stringBuffer2 == null) {
                            stringBuffer = r11;
                            stringBuffer3 = new StringBuffer(str2.length());
                            stringBuffer2 = stringBuffer;
                        }
                        if (i2 != 0) {
                            stringBuffer = stringBuffer2.append(str2.substring(0, i2));
                            stringBuffer = stringBuffer2.append(' ');
                        }
                        str2 = str2.substring(i);
                    }
                }
                if (stringBuffer2 == null) {
                    stringBuffer = r11;
                    stringBuffer3 = new StringBuffer(str2.length());
                    stringBuffer2 = stringBuffer;
                }
                stringBuffer = stringBuffer2.append(str2.substring(0, i));
                str2 = str2.substring(i);
            } else {
                if (stringBuffer2 == null) {
                    stringBuffer = r11;
                    stringBuffer3 = new StringBuffer(str2.length());
                    stringBuffer2 = stringBuffer;
                }
                stringBuffer = stringBuffer2.append(str2.substring(0, i2 - 1));
                stringBuffer = stringBuffer2.append(str2.substring(i2, i));
                str2 = str2.substring(i);
            }
        }
        if (stringBuffer2 == null) {
            return str2;
        }
        stringBuffer = stringBuffer2.append(str2);
        return stringBuffer2.toString();
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

    public static String javaCharset(String str) {
        String str2 = str;
        if (mime2java == null || str2 == null) {
            return str2;
        }
        String str3 = (String) mime2java.get(str2.toLowerCase(Locale.ENGLISH));
        return str3 == null ? str2 : str3;
    }

    public static String mimeCharset(String str) {
        String str2 = str;
        if (java2mime == null || str2 == null) {
            return str2;
        }
        String str3 = (String) java2mime.get(str2.toLowerCase(Locale.ENGLISH));
        return str3 == null ? str2 : str3;
    }

    public static String getDefaultJavaCharset() {
        SecurityException securityException;
        if (defaultJavaCharset == null) {
            String str = null;
            try {
                str = System.getProperty("mail.mime.charset");
            } catch (SecurityException e) {
                securityException = e;
            }
            if (str == null || str.length() <= 0) {
                try {
                    defaultJavaCharset = System.getProperty("file.encoding", "8859_1");
                } catch (SecurityException e2) {
                    securityException = e2;
                    InputStreamReader inputStreamReader = r7;
                    InputStream inputStream = r7;
                    InputStream anonymousClass1NullInputStream = new InputStream() {
                        public int read() {
                            return 0;
                        }
                    };
                    InputStreamReader inputStreamReader2 = new InputStreamReader(inputStream);
                    defaultJavaCharset = inputStreamReader.getEncoding();
                    if (defaultJavaCharset == null) {
                        defaultJavaCharset = "8859_1";
                    }
                }
            } else {
                defaultJavaCharset = javaCharset(str);
                return defaultJavaCharset;
            }
        }
        return defaultJavaCharset;
    }

    static String getDefaultMIMECharset() {
        if (defaultMIMECharset == null) {
            try {
                defaultMIMECharset = System.getProperty("mail.mime.charset");
            } catch (SecurityException e) {
                SecurityException securityException = e;
            }
        }
        if (defaultMIMECharset == null) {
            defaultMIMECharset = mimeCharset(getDefaultJavaCharset());
        }
        return defaultMIMECharset;
    }

    private static void loadMappings(LineInputStream lineInputStream, Hashtable hashtable) {
        LineInputStream lineInputStream2 = lineInputStream;
        Hashtable hashtable2 = hashtable;
        while (true) {
            try {
                String readLine = lineInputStream2.readLine();
                if (readLine != null) {
                    if (!readLine.startsWith("--") || !readLine.endsWith("--")) {
                        if (readLine.trim().length() != 0) {
                            if (!readLine.startsWith("#")) {
                                StringTokenizer stringTokenizer = r10;
                                StringTokenizer stringTokenizer2 = new StringTokenizer(readLine, " \t");
                                StringTokenizer stringTokenizer3 = stringTokenizer;
                                try {
                                    String nextToken = stringTokenizer3.nextToken();
                                    Object put = hashtable2.put(nextToken.toLowerCase(Locale.ENGLISH), stringTokenizer3.nextToken());
                                } catch (NoSuchElementException e) {
                                    NoSuchElementException noSuchElementException = e;
                                }
                            }
                        }
                    } else {
                        return;
                    }
                }
                return;
            } catch (IOException e2) {
                IOException iOException = e2;
                return;
            }
        }
    }

    static int checkAscii(String str) {
        String str2 = str;
        int i = 0;
        int i2 = 0;
        int length = str2.length();
        for (int i3 = 0; i3 < length; i3++) {
            if (nonascii(str2.charAt(i3))) {
                i2++;
            } else {
                i++;
            }
        }
        if (i2 == 0) {
            return 1;
        }
        if (i > i2) {
            return 2;
        }
        return 3;
    }

    static int checkAscii(byte[] bArr) {
        byte[] bArr2 = bArr;
        int i = 0;
        int i2 = 0;
        for (byte b : bArr2) {
            if (nonascii(b & 255)) {
                i2++;
            } else {
                i++;
            }
        }
        if (i2 == 0) {
            return 1;
        }
        if (i > i2) {
            return 2;
        }
        return 3;
    }

    static int checkAscii(InputStream inputStream, int i, boolean z) {
        InputStream inputStream2 = inputStream;
        int i2 = i;
        boolean z2 = z;
        int i3 = 0;
        int i4 = 0;
        int i5 = 4096;
        int i6 = 0;
        Object obj = null;
        Object obj2 = null;
        Object obj3 = (encodeEolStrict && z2) ? 1 : null;
        Object obj4 = obj3;
        byte[] bArr = (byte[]) null;
        if (i2 != 0) {
            i5 = i2 == -1 ? 4096 : Math.min(i2, 4096);
            bArr = new byte[i5];
        }
        while (i2 != 0) {
            try {
                int read = inputStream2.read(bArr, 0, i5);
                int i7 = read;
                if (read == -1) {
                    break;
                }
                int i8 = 0;
                for (int i9 = 0; i9 < i7; i9++) {
                    int i10 = bArr[i9] & 255;
                    if (obj4 != null && ((i8 == 13 && i10 != 10) || (i8 != 13 && i10 == 10))) {
                        obj2 = 1;
                    }
                    if (i10 == 13 || i10 == 10) {
                        i6 = 0;
                    } else {
                        i6++;
                        if (i6 > 998) {
                            int obj5 = 1;
                        }
                    }
                    if (!nonascii(i10)) {
                        i3++;
                    } else if (z2) {
                        return 3;
                    } else {
                        i4++;
                    }
                    i8 = i10;
                }
                if (i2 != -1) {
                    i2 -= i7;
                }
            } catch (IOException e) {
                IOException iOException = e;
            }
        }
        if (i2 == 0 && z2) {
            return 3;
        }
        if (i4 == 0) {
            if (obj2 != null) {
                return 3;
            }
            if (obj5 != null) {
                return 2;
            }
            return 1;
        } else if (i3 > i4) {
            return 2;
        } else {
            return 3;
        }
    }

    static final boolean nonascii(int i) {
        int i2 = i;
        return i2 >= 127 || !(i2 >= 32 || i2 == 13 || i2 == 10 || i2 == 9);
    }
}
