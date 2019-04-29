package javax.mail.internet;

import com.sun.mail.util.LineInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.mail.Header;
import javax.mail.MessagingException;

public class InternetHeaders {
    protected List headers;

    protected static final class InternetHeader extends Header {
        String line;

        public InternetHeader(String str) {
            String str2 = str;
            super("", "");
            int indexOf = str2.indexOf(58);
            if (indexOf < 0) {
                this.name = str2.trim();
            } else {
                this.name = str2.substring(0, indexOf).trim();
            }
            this.line = str2;
        }

        public InternetHeader(String str, String str2) {
            String str3 = str;
            String str4 = str2;
            super(str3, "");
            if (str4 != null) {
                StringBuilder stringBuilder = r7;
                StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(str3));
                this.line = stringBuilder.append(": ").append(str4).toString();
                return;
            }
            this.line = null;
        }

        public String getValue() {
            int indexOf = this.line.indexOf(58);
            if (indexOf < 0) {
                return this.line;
            }
            int i = indexOf + 1;
            while (i < this.line.length()) {
                char charAt = this.line.charAt(i);
                if (charAt != ' ' && charAt != 9 && charAt != 13 && charAt != 10) {
                    break;
                }
                i++;
            }
            return this.line.substring(i);
        }
    }

    static class matchEnum implements Enumeration {
        private Iterator e;
        private boolean match;
        private String[] names;
        private InternetHeader next_header = null;
        private boolean want_line;

        matchEnum(List list, String[] strArr, boolean z, boolean z2) {
            String[] strArr2 = strArr;
            boolean z3 = z;
            boolean z4 = z2;
            this.e = list.iterator();
            this.names = strArr2;
            this.match = z3;
            this.want_line = z4;
        }

        public boolean hasMoreElements() {
            if (this.next_header == null) {
                this.next_header = nextMatch();
            }
            return this.next_header != null;
        }

        public Object nextElement() {
            if (this.next_header == null) {
                this.next_header = nextMatch();
            }
            if (this.next_header == null) {
                NoSuchElementException noSuchElementException = r6;
                NoSuchElementException noSuchElementException2 = new NoSuchElementException("No more headers");
                throw noSuchElementException;
            }
            InternetHeader internetHeader = this.next_header;
            this.next_header = null;
            if (this.want_line) {
                return internetHeader.line;
            }
            Header header = r6;
            Header header2 = new Header(internetHeader.getName(), internetHeader.getValue());
            return header;
        }

        private InternetHeader nextMatch() {
            while (this.e.hasNext()) {
                InternetHeader internetHeader = (InternetHeader) this.e.next();
                if (internetHeader.line != null) {
                    if (this.names == null) {
                        return this.match ? null : internetHeader;
                    }
                    int i = 0;
                    while (i < this.names.length) {
                        if (!this.names[i].equalsIgnoreCase(internetHeader.getName())) {
                            i++;
                        } else if (this.match) {
                            return internetHeader;
                        }
                    }
                    if (!this.match) {
                        return internetHeader;
                    }
                }
            }
            return null;
        }
    }

    public InternetHeaders() {
        ArrayList arrayList = r6;
        ArrayList arrayList2 = new ArrayList(40);
        this.headers = arrayList;
        List list = this.headers;
        InternetHeader internetHeader = r6;
        InternetHeader internetHeader2 = new InternetHeader("Return-Path", null);
        boolean add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Received", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Resent-Date", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Resent-From", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Resent-Sender", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Resent-To", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Resent-Cc", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Resent-Bcc", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Resent-Message-Id", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Date", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("From", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Sender", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Reply-To", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("To", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Cc", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Bcc", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Message-Id", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("In-Reply-To", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("References", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Subject", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Comments", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Keywords", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Errors-To", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("MIME-Version", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Content-Type", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Content-Transfer-Encoding", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Content-MD5", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader(":", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Content-Length", null);
        add = list.add(internetHeader);
        list = this.headers;
        internetHeader = r6;
        internetHeader2 = new InternetHeader("Status", null);
        add = list.add(internetHeader);
    }

    public InternetHeaders(InputStream inputStream) throws MessagingException {
        InputStream inputStream2 = inputStream;
        ArrayList arrayList = r6;
        ArrayList arrayList2 = new ArrayList(40);
        this.headers = arrayList;
        load(inputStream2);
    }

    public void load(InputStream inputStream) throws MessagingException {
        LineInputStream lineInputStream = r11;
        LineInputStream lineInputStream2 = new LineInputStream(inputStream);
        LineInputStream lineInputStream3 = lineInputStream;
        String str = null;
        StringBuffer stringBuffer = r11;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        String readLine;
        do {
            try {
                readLine = lineInputStream3.readLine();
                if (readLine == null || !(readLine.startsWith(" ") || readLine.startsWith("\t"))) {
                    if (str != null) {
                        addHeaderLine(str);
                    } else if (stringBuffer3.length() > 0) {
                        addHeaderLine(stringBuffer3.toString());
                        stringBuffer3.setLength(0);
                    }
                    str = readLine;
                } else {
                    if (str != null) {
                        stringBuffer = stringBuffer3.append(str);
                        str = null;
                    }
                    stringBuffer = stringBuffer3.append("\r\n");
                    stringBuffer = stringBuffer3.append(readLine);
                }
                if (readLine == null) {
                    return;
                }
            } catch (IOException e) {
                Exception exception = e;
                MessagingException messagingException = r11;
                MessagingException messagingException2 = new MessagingException("Error in input stream", exception);
                throw messagingException;
            }
        } while (readLine.length() > 0);
    }

    public String[] getHeader(String str) {
        String str2 = str;
        ArrayList arrayList = r7;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = arrayList;
        for (InternetHeader internetHeader : this.headers) {
            if (str2.equalsIgnoreCase(internetHeader.getName()) && internetHeader.line != null) {
                boolean add = arrayList3.add(internetHeader.getValue());
            }
        }
        if (arrayList3.size() == 0) {
            return null;
        }
        return (String[]) arrayList3.toArray(new String[arrayList3.size()]);
    }

    public String getHeader(String str, String str2) {
        String str3 = str2;
        String[] header = getHeader(str);
        if (header == null) {
            return null;
        }
        if (header.length == 1 || str3 == null) {
            return header[0];
        }
        StringBuffer stringBuffer = r10;
        StringBuffer stringBuffer2 = new StringBuffer(header[0]);
        StringBuffer stringBuffer3 = stringBuffer;
        for (int i = 1; i < header.length; i++) {
            stringBuffer = stringBuffer3.append(str3);
            stringBuffer = stringBuffer3.append(header[i]);
        }
        return stringBuffer3.toString();
    }

    public void setHeader(String str, String str2) {
        String str3 = str;
        String str4 = str2;
        Object obj = null;
        int i = 0;
        while (i < this.headers.size()) {
            InternetHeader internetHeader = (InternetHeader) this.headers.get(i);
            if (str3.equalsIgnoreCase(internetHeader.getName())) {
                if (obj == null) {
                    InternetHeader internetHeader2;
                    StringBuilder stringBuilder;
                    StringBuilder stringBuilder2;
                    if (internetHeader.line != null) {
                        int indexOf = internetHeader.line.indexOf(58);
                        int i2 = indexOf;
                        if (indexOf >= 0) {
                            internetHeader2 = internetHeader;
                            stringBuilder = r14;
                            stringBuilder2 = new StringBuilder(String.valueOf(internetHeader.line.substring(0, i2 + 1)));
                            internetHeader2.line = stringBuilder.append(" ").append(str4).toString();
                            obj = 1;
                        }
                    }
                    internetHeader2 = internetHeader;
                    stringBuilder = r14;
                    stringBuilder2 = new StringBuilder(String.valueOf(str3));
                    internetHeader2.line = stringBuilder.append(": ").append(str4).toString();
                    obj = 1;
                } else {
                    Object remove = this.headers.remove(i);
                    i--;
                }
            }
            i++;
        }
        if (obj == null) {
            addHeader(str3, str4);
        }
    }

    public void addHeader(String str, String str2) {
        List list;
        int i;
        InternetHeader internetHeader;
        InternetHeader internetHeader2;
        String str3 = str;
        String str4 = str2;
        int size = this.headers.size();
        Object obj = (str3.equalsIgnoreCase("Received") || str3.equalsIgnoreCase("Return-Path")) ? 1 : null;
        Object obj2 = obj;
        if (obj2 != null) {
            size = 0;
        }
        for (int size2 = this.headers.size() - 1; size2 >= 0; size2--) {
            InternetHeader internetHeader3 = (InternetHeader) this.headers.get(size2);
            if (str3.equalsIgnoreCase(internetHeader3.getName())) {
                if (obj2 != null) {
                    size = size2;
                } else {
                    list = this.headers;
                    i = size2 + 1;
                    internetHeader = r13;
                    internetHeader2 = new InternetHeader(str3, str4);
                    list.add(i, internetHeader);
                    return;
                }
            }
            if (internetHeader3.getName().equals(":")) {
                size = size2;
            }
        }
        list = this.headers;
        i = size;
        internetHeader = r13;
        internetHeader2 = new InternetHeader(str3, str4);
        list.add(i, internetHeader);
    }

    public void removeHeader(String str) {
        String str2 = str;
        for (int i = 0; i < this.headers.size(); i++) {
            InternetHeader internetHeader = (InternetHeader) this.headers.get(i);
            if (str2.equalsIgnoreCase(internetHeader.getName())) {
                internetHeader.line = null;
            }
        }
    }

    public Enumeration getAllHeaders() {
        matchEnum matchenum = r7;
        matchEnum matchenum2 = new matchEnum(this.headers, null, false, false);
        return matchenum;
    }

    public Enumeration getMatchingHeaders(String[] strArr) {
        matchEnum matchenum = r8;
        matchEnum matchenum2 = new matchEnum(this.headers, strArr, true, false);
        return matchenum;
    }

    public Enumeration getNonMatchingHeaders(String[] strArr) {
        matchEnum matchenum = r8;
        matchEnum matchenum2 = new matchEnum(this.headers, strArr, false, false);
        return matchenum;
    }

    public void addHeaderLine(String str) {
        String str2 = str;
        try {
            char charAt = str2.charAt(0);
            InternetHeader internetHeader;
            if (charAt == ' ' || charAt == 9) {
                internetHeader = (InternetHeader) this.headers.get(this.headers.size() - 1);
                InternetHeader internetHeader2 = internetHeader;
                StringBuilder stringBuilder = r9;
                StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(internetHeader.line));
                internetHeader2.line = stringBuilder.append("\r\n").append(str2).toString();
                return;
            }
            List list = this.headers;
            InternetHeader internetHeader3 = internetHeader;
            InternetHeader internetHeader4 = new InternetHeader(str2);
            boolean add = list.add(internetHeader3);
        } catch (StringIndexOutOfBoundsException e) {
            StringIndexOutOfBoundsException stringIndexOutOfBoundsException = e;
        } catch (NoSuchElementException e2) {
            NoSuchElementException noSuchElementException = e2;
        }
    }

    public Enumeration getAllHeaderLines() {
        return getNonMatchingHeaderLines(null);
    }

    public Enumeration getMatchingHeaderLines(String[] strArr) {
        matchEnum matchenum = r8;
        matchEnum matchenum2 = new matchEnum(this.headers, strArr, true, true);
        return matchenum;
    }

    public Enumeration getNonMatchingHeaderLines(String[] strArr) {
        matchEnum matchenum = r8;
        matchEnum matchenum2 = new matchEnum(this.headers, strArr, false, true);
        return matchenum;
    }
}
