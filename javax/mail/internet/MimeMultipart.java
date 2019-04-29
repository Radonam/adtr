package javax.mail.internet;

import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.LineInputStream;
import com.sun.mail.util.LineOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessageAware;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.MultipartDataSource;

public class MimeMultipart extends Multipart {
    private static boolean bmparse;
    private static boolean ignoreMissingBoundaryParameter;
    private static boolean ignoreMissingEndBoundary;
    private boolean complete;
    protected DataSource ds;
    protected boolean parsed;
    private String preamble;

    static {
        ignoreMissingEndBoundary = true;
        ignoreMissingBoundaryParameter = true;
        bmparse = true;
        try {
            String property = System.getProperty("mail.mime.multipart.ignoremissingendboundary");
            boolean z = property == null || !property.equalsIgnoreCase("false");
            ignoreMissingEndBoundary = z;
            property = System.getProperty("mail.mime.multipart.ignoremissingboundaryparameter");
            z = property == null || !property.equalsIgnoreCase("false");
            ignoreMissingBoundaryParameter = z;
            property = System.getProperty("mail.mime.multipart.bmparse");
            if (property == null || !property.equalsIgnoreCase("false")) {
                z = true;
            } else {
                z = false;
            }
            bmparse = z;
        } catch (SecurityException e) {
            SecurityException securityException = e;
        }
    }

    public MimeMultipart() {
        this("mixed");
    }

    public MimeMultipart(String str) {
        String str2 = str;
        this.ds = null;
        this.parsed = true;
        this.complete = true;
        this.preamble = null;
        String uniqueBoundaryValue = UniqueValue.getUniqueBoundaryValue();
        ContentType contentType = r9;
        ContentType contentType2 = new ContentType("multipart", str2, null);
        ContentType contentType3 = contentType;
        contentType3.setParameter("boundary", uniqueBoundaryValue);
        this.contentType = contentType3.toString();
    }

    public MimeMultipart(DataSource dataSource) throws MessagingException {
        DataSource dataSource2 = dataSource;
        this.ds = null;
        this.parsed = true;
        this.complete = true;
        this.preamble = null;
        if (dataSource2 instanceof MessageAware) {
            setParent(((MessageAware) dataSource2).getMessageContext().getPart());
        }
        if (dataSource2 instanceof MultipartDataSource) {
            setMultipartDataSource((MultipartDataSource) dataSource2);
            return;
        }
        this.parsed = false;
        this.ds = dataSource2;
        this.contentType = dataSource2.getContentType();
    }

    public synchronized void setSubType(String str) throws MessagingException {
        String str2 = str;
        synchronized (this) {
            ContentType contentType = r7;
            ContentType contentType2 = new ContentType(this.contentType);
            ContentType contentType3 = contentType;
            contentType3.setSubType(str2);
            this.contentType = contentType3.toString();
        }
    }

    public synchronized int getCount() throws MessagingException {
        int count;
        synchronized (this) {
            parse();
            count = super.getCount();
        }
        return count;
    }

    public synchronized BodyPart getBodyPart(int i) throws MessagingException {
        BodyPart bodyPart;
        int i2 = i;
        synchronized (this) {
            parse();
            bodyPart = super.getBodyPart(i2);
        }
        return bodyPart;
    }

    public synchronized BodyPart getBodyPart(String str) throws MessagingException {
        BodyPart bodyPart;
        String str2 = str;
        synchronized (this) {
            parse();
            int count = getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodyPart2 = (MimeBodyPart) getBodyPart(i);
                String contentID = bodyPart2.getContentID();
                if (contentID != null && contentID.equals(str2)) {
                    bodyPart = bodyPart2;
                    break;
                }
            }
            bodyPart = null;
        }
        return bodyPart;
    }

    public boolean removeBodyPart(BodyPart bodyPart) throws MessagingException {
        BodyPart bodyPart2 = bodyPart;
        parse();
        return super.removeBodyPart(bodyPart2);
    }

    public void removeBodyPart(int i) throws MessagingException {
        int i2 = i;
        parse();
        super.removeBodyPart(i2);
    }

    public synchronized void addBodyPart(BodyPart bodyPart) throws MessagingException {
        BodyPart bodyPart2 = bodyPart;
        synchronized (this) {
            parse();
            super.addBodyPart(bodyPart2);
        }
    }

    public synchronized void addBodyPart(BodyPart bodyPart, int i) throws MessagingException {
        BodyPart bodyPart2 = bodyPart;
        int i2 = i;
        synchronized (this) {
            parse();
            super.addBodyPart(bodyPart2, i2);
        }
    }

    public synchronized boolean isComplete() throws MessagingException {
        boolean z;
        synchronized (this) {
            parse();
            z = this.complete;
        }
        return z;
    }

    public synchronized String getPreamble() throws MessagingException {
        String str;
        synchronized (this) {
            parse();
            str = this.preamble;
        }
        return str;
    }

    public synchronized void setPreamble(String str) throws MessagingException {
        String str2 = str;
        synchronized (this) {
            this.preamble = str2;
        }
    }

    /* Access modifiers changed, original: protected */
    public void updateHeaders() throws MessagingException {
        for (int i = 0; i < this.parts.size(); i++) {
            ((MimeBodyPart) this.parts.elementAt(i)).updateHeaders();
        }
    }

    public synchronized void writeTo(OutputStream outputStream) throws IOException, MessagingException {
        OutputStream outputStream2 = outputStream;
        synchronized (this) {
            parse();
            StringBuilder stringBuilder = r10;
            StringBuilder stringBuilder2 = new StringBuilder("--");
            ContentType contentType = r10;
            ContentType contentType2 = new ContentType(this.contentType);
            String stringBuilder3 = stringBuilder.append(contentType.getParameter("boundary")).toString();
            LineOutputStream lineOutputStream = r10;
            LineOutputStream lineOutputStream2 = new LineOutputStream(outputStream2);
            LineOutputStream lineOutputStream3 = lineOutputStream;
            if (this.preamble != null) {
                byte[] bytes = ASCIIUtility.getBytes(this.preamble);
                lineOutputStream3.write(bytes);
                if (!(bytes.length <= 0 || bytes[bytes.length - 1] == (byte) 13 || bytes[bytes.length - 1] == (byte) 10)) {
                    lineOutputStream3.writeln();
                }
            }
            for (int i = 0; i < this.parts.size(); i++) {
                lineOutputStream3.writeln(stringBuilder3);
                ((MimeBodyPart) this.parts.elementAt(i)).writeTo(outputStream2);
                lineOutputStream3.writeln();
            }
            lineOutputStream = lineOutputStream3;
            stringBuilder2 = r10;
            StringBuilder stringBuilder4 = new StringBuilder(String.valueOf(stringBuilder3));
            lineOutputStream.writeln(stringBuilder2.append("--").toString());
        }
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void parse() throws MessagingException {
        IOException iOException;
        synchronized (this) {
            MessagingException messagingException;
            MessagingException messagingException2;
            try {
                if (!this.parsed) {
                    if (bmparse) {
                        parsebm();
                    } else {
                        Object obj = null;
                        SharedInputStream sharedInputStream = null;
                        long j = 0;
                        long j2 = 0;
                        InputStream inputStream = this.ds.getInputStream();
                        if (!((inputStream instanceof ByteArrayInputStream) || (inputStream instanceof BufferedInputStream) || (inputStream instanceof SharedInputStream))) {
                            InputStream inputStream2 = r36;
                            InputStream bufferedInputStream = new BufferedInputStream(inputStream);
                            inputStream = inputStream2;
                        }
                        if (inputStream instanceof SharedInputStream) {
                            sharedInputStream = (SharedInputStream) inputStream;
                        }
                        ContentType contentType = r36;
                        ContentType contentType2 = new ContentType(this.contentType);
                        String str = null;
                        String parameter = contentType.getParameter("boundary");
                        if (parameter != null) {
                            StringBuilder stringBuilder = r36;
                            StringBuilder stringBuilder2 = new StringBuilder("--");
                            str = stringBuilder.append(parameter).toString();
                        } else if (!ignoreMissingBoundaryParameter) {
                            messagingException = r36;
                            messagingException2 = new MessagingException("Missing boundary parameter");
                            throw messagingException;
                        }
                        try {
                            String readLine;
                            String str2;
                            LineInputStream lineInputStream = r36;
                            LineInputStream lineInputStream2 = new LineInputStream(inputStream);
                            LineInputStream lineInputStream3 = lineInputStream;
                            StringBuffer stringBuffer = null;
                            String str3 = null;
                            while (true) {
                                readLine = lineInputStream3.readLine();
                                str2 = readLine;
                                if (readLine == null) {
                                    break;
                                }
                                int length = str2.length() - 1;
                                while (length >= 0) {
                                    char charAt = str2.charAt(length);
                                    if (charAt != ' ' && charAt != 9) {
                                        break;
                                    }
                                    length--;
                                }
                                str2 = str2.substring(0, length + 1);
                                if (str == null) {
                                    if (str2.startsWith("--")) {
                                        str = str2;
                                        break;
                                    }
                                } else if (str2.equals(str)) {
                                    break;
                                }
                                if (str2.length() > 0) {
                                    StringBuffer stringBuffer2;
                                    if (str3 == null) {
                                        try {
                                            str3 = System.getProperty("line.separator", "\n");
                                        } catch (SecurityException e) {
                                            SecurityException securityException = e;
                                            str3 = "\n";
                                        }
                                    }
                                    if (stringBuffer == null) {
                                        stringBuffer2 = r36;
                                        StringBuffer stringBuffer3 = new StringBuffer(str2.length() + 2);
                                        stringBuffer = stringBuffer2;
                                    }
                                    stringBuffer2 = stringBuffer.append(str2).append(str3);
                                }
                            }
                            if (str2 == null) {
                                messagingException = r36;
                                messagingException2 = new MessagingException("Missing start boundary");
                                throw messagingException;
                            }
                            if (stringBuffer != null) {
                                this.preamble = stringBuffer.toString();
                            }
                            byte[] bytes = ASCIIUtility.getBytes(str);
                            int length2 = bytes.length;
                            Object obj2 = null;
                            while (obj2 == null) {
                                InternetHeaders internetHeaders = null;
                                if (sharedInputStream != null) {
                                    j = sharedInputStream.getPosition();
                                    do {
                                        readLine = lineInputStream3.readLine();
                                        str2 = readLine;
                                        if (readLine == null) {
                                            break;
                                        }
                                    } while (str2.length() > 0);
                                    if (str2 == null) {
                                        if (ignoreMissingEndBoundary) {
                                            this.complete = false;
                                            inputStream.close();
                                            this.parsed = true;
                                        } else {
                                            messagingException = r36;
                                            messagingException2 = new MessagingException("missing multipart end boundary");
                                            throw messagingException;
                                        }
                                    }
                                }
                                internetHeaders = createInternetHeaders(inputStream);
                                if (inputStream.markSupported()) {
                                    BodyPart createMimeBodyPart;
                                    ByteArrayOutputStream byteArrayOutputStream = null;
                                    if (sharedInputStream == null) {
                                        ByteArrayOutputStream byteArrayOutputStream2 = r36;
                                        ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
                                        byteArrayOutputStream = byteArrayOutputStream2;
                                    } else {
                                        j2 = sharedInputStream.getPosition();
                                    }
                                    Object obj3 = 1;
                                    int i = -1;
                                    int i2 = -1;
                                    while (true) {
                                        int i3;
                                        if (obj3 != null) {
                                            inputStream.mark((length2 + 4) + IMAPStore.RESPONSE);
                                            int i4 = 0;
                                            while (i4 < length2) {
                                                if (inputStream.read() != (bytes[i4] & 255)) {
                                                    break;
                                                }
                                                i4++;
                                            }
                                            if (i4 == length2) {
                                                int read = inputStream.read();
                                                if (read == 45 && inputStream.read() == 45) {
                                                    this.complete = true;
                                                    obj2 = 1;
                                                    break;
                                                }
                                                while (true) {
                                                    if (read != 32 && read != 9) {
                                                        break;
                                                    }
                                                    read = inputStream.read();
                                                }
                                                if (read == 10) {
                                                    break;
                                                } else if (read == 13) {
                                                    inputStream.mark(1);
                                                    if (inputStream.read() != 10) {
                                                        inputStream.reset();
                                                    }
                                                }
                                            }
                                            inputStream.reset();
                                            if (!(byteArrayOutputStream == null || i == -1)) {
                                                byteArrayOutputStream.write(i);
                                                if (i2 != -1) {
                                                    byteArrayOutputStream.write(i2);
                                                }
                                                i3 = -1;
                                                i2 = i3;
                                                i = i3;
                                            }
                                        }
                                        i3 = inputStream.read();
                                        int i5 = i3;
                                        if (i3 < 0) {
                                            if (ignoreMissingEndBoundary) {
                                                this.complete = false;
                                                int obj22 = 1;
                                            } else {
                                                messagingException = r36;
                                                messagingException2 = new MessagingException("missing multipart end boundary");
                                                throw messagingException;
                                            }
                                        } else if (i5 == 13 || i5 == 10) {
                                            obj3 = 1;
                                            if (sharedInputStream != null) {
                                                j2 = sharedInputStream.getPosition() - 1;
                                            }
                                            i = i5;
                                            if (i5 == 13) {
                                                inputStream.mark(1);
                                                i3 = inputStream.read();
                                                i5 = i3;
                                                if (i3 == 10) {
                                                    i2 = i5;
                                                } else {
                                                    inputStream.reset();
                                                }
                                            }
                                        } else {
                                            obj3 = null;
                                            if (byteArrayOutputStream != null) {
                                                byteArrayOutputStream.write(i5);
                                            }
                                        }
                                    }
                                    if (sharedInputStream != null) {
                                        createMimeBodyPart = createMimeBodyPart(sharedInputStream.newStream(j, j2));
                                    } else {
                                        createMimeBodyPart = createMimeBodyPart(internetHeaders, byteArrayOutputStream.toByteArray());
                                    }
                                    super.addBodyPart(createMimeBodyPart);
                                } else {
                                    messagingException = r36;
                                    messagingException2 = new MessagingException("Stream doesn't support mark");
                                    throw messagingException;
                                }
                            }
                            try {
                                inputStream.close();
                            } catch (IOException e2) {
                                iOException = e2;
                            }
                            this.parsed = true;
                        } catch (IOException e3) {
                            Exception exception = e3;
                            messagingException = r36;
                            messagingException2 = new MessagingException("IO Error", exception);
                            throw messagingException;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            try {
                                inputStream.close();
                            } catch (IOException e22) {
                                iOException = e22;
                            }
                            throw th2;
                        }
                    }
                }
            } catch (Exception e32) {
                Exception exception2 = e32;
                messagingException = r36;
                messagingException2 = new MessagingException("No inputstream from datasource", exception2);
                throw messagingException;
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }

    private synchronized void parsebm() throws MessagingException {
        IOException iOException;
        synchronized (this) {
            MessagingException messagingException;
            MessagingException messagingException2;
            try {
                if (!this.parsed) {
                    Object obj = null;
                    SharedInputStream sharedInputStream = null;
                    long j = 0;
                    long j2 = 0;
                    InputStream inputStream = this.ds.getInputStream();
                    if (!((inputStream instanceof ByteArrayInputStream) || (inputStream instanceof BufferedInputStream) || (inputStream instanceof SharedInputStream))) {
                        InputStream inputStream2 = r42;
                        InputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        inputStream = inputStream2;
                    }
                    if (inputStream instanceof SharedInputStream) {
                        sharedInputStream = (SharedInputStream) inputStream;
                    }
                    ContentType contentType = r42;
                    ContentType contentType2 = new ContentType(this.contentType);
                    String str = null;
                    String parameter = contentType.getParameter("boundary");
                    if (parameter != null) {
                        StringBuilder stringBuilder = r42;
                        StringBuilder stringBuilder2 = new StringBuilder("--");
                        str = stringBuilder.append(parameter).toString();
                    } else if (!ignoreMissingBoundaryParameter) {
                        messagingException = r42;
                        messagingException2 = new MessagingException("Missing boundary parameter");
                        throw messagingException;
                    }
                    try {
                        String readLine;
                        String str2;
                        LineInputStream lineInputStream = r42;
                        LineInputStream lineInputStream2 = new LineInputStream(inputStream);
                        LineInputStream lineInputStream3 = lineInputStream;
                        StringBuffer stringBuffer = null;
                        String str3 = null;
                        while (true) {
                            readLine = lineInputStream3.readLine();
                            str2 = readLine;
                            if (readLine == null) {
                                break;
                            }
                            int length = str2.length() - 1;
                            while (length >= 0) {
                                char charAt = str2.charAt(length);
                                if (charAt != ' ' && charAt != 9) {
                                    break;
                                }
                                length--;
                            }
                            str2 = str2.substring(0, length + 1);
                            if (str == null) {
                                if (str2.startsWith("--")) {
                                    str = str2;
                                    break;
                                }
                            } else if (str2.equals(str)) {
                                break;
                            }
                            if (str2.length() > 0) {
                                StringBuffer stringBuffer2;
                                if (str3 == null) {
                                    try {
                                        str3 = System.getProperty("line.separator", "\n");
                                    } catch (SecurityException e) {
                                        SecurityException securityException = e;
                                        str3 = "\n";
                                    }
                                }
                                if (stringBuffer == null) {
                                    stringBuffer2 = r42;
                                    StringBuffer stringBuffer3 = new StringBuffer(str2.length() + 2);
                                    stringBuffer = stringBuffer2;
                                }
                                stringBuffer2 = stringBuffer.append(str2).append(str3);
                            }
                        }
                        if (str2 == null) {
                            messagingException = r42;
                            messagingException2 = new MessagingException("Missing start boundary");
                            throw messagingException;
                        }
                        int i;
                        if (stringBuffer != null) {
                            this.preamble = stringBuffer.toString();
                        }
                        byte[] bytes = ASCIIUtility.getBytes(str);
                        int length2 = bytes.length;
                        int[] iArr = new int[256];
                        for (int i2 = 0; i2 < length2; i2++) {
                            iArr[bytes[i2]] = i2 + 1;
                        }
                        int[] iArr2 = new int[length2];
                        for (i = length2; i > 0; i--) {
                            int i3 = length2 - 1;
                            while (i3 >= i) {
                                if (bytes[i3] != bytes[i3 - i]) {
                                    break;
                                }
                                iArr2[i3 - 1] = i;
                                i3--;
                            }
                            while (i3 > 0) {
                                i3--;
                                iArr2[i3] = i;
                            }
                        }
                        iArr2[length2 - 1] = 1;
                        Object obj2 = null;
                        while (obj2 == null) {
                            InternetHeaders internetHeaders = null;
                            if (sharedInputStream != null) {
                                j = sharedInputStream.getPosition();
                                do {
                                    readLine = lineInputStream3.readLine();
                                    str2 = readLine;
                                    if (readLine == null) {
                                        break;
                                    }
                                } while (str2.length() > 0);
                                if (str2 == null) {
                                    if (ignoreMissingEndBoundary) {
                                        this.complete = false;
                                        inputStream.close();
                                        this.parsed = true;
                                    } else {
                                        messagingException = r42;
                                        messagingException2 = new MessagingException("missing multipart end boundary");
                                        throw messagingException;
                                    }
                                }
                            }
                            internetHeaders = createInternetHeaders(inputStream);
                            if (inputStream.markSupported()) {
                                int i4;
                                int readFully;
                                BodyPart createMimeBodyPart;
                                ByteArrayOutputStream byteArrayOutputStream = null;
                                if (sharedInputStream == null) {
                                    ByteArrayOutputStream byteArrayOutputStream2 = r42;
                                    ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
                                    byteArrayOutputStream = byteArrayOutputStream2;
                                } else {
                                    j2 = sharedInputStream.getPosition();
                                }
                                byte[] bArr = new byte[length2];
                                byte[] bArr2 = new byte[length2];
                                Object obj3 = null;
                                int i5 = 0;
                                Object obj4 = 1;
                                while (true) {
                                    inputStream.mark((length2 + 4) + IMAPStore.RESPONSE);
                                    i4 = 0;
                                    readFully = readFully(inputStream, bArr, 0, length2);
                                    if (readFully >= length2) {
                                        int read;
                                        int i6 = length2 - 1;
                                        while (i6 >= 0) {
                                            if (bArr[i6] != bytes[i6]) {
                                                break;
                                            }
                                            i6--;
                                        }
                                        if (i6 < 0) {
                                            i4 = 0;
                                            if (obj4 == null) {
                                                byte b = bArr2[i5 - 1];
                                                if (b == (byte) 13 || b == (byte) 10) {
                                                    i4 = 1;
                                                    if (b == (byte) 10 && i5 >= 2 && bArr2[i5 - 2] == (byte) 13) {
                                                        i4 = 2;
                                                    }
                                                }
                                            }
                                            if (obj4 != null || i4 > 0) {
                                                if (sharedInputStream != null) {
                                                    j2 = (sharedInputStream.getPosition() - ((long) length2)) - ((long) i4);
                                                }
                                                read = inputStream.read();
                                                if (read == 45 && inputStream.read() == 45) {
                                                    this.complete = true;
                                                    i = 1;
                                                    break;
                                                }
                                                while (true) {
                                                    if (read != 32 && read != 9) {
                                                        break;
                                                    }
                                                    read = inputStream.read();
                                                }
                                                if (read == 10) {
                                                    break;
                                                } else if (read == 13) {
                                                    inputStream.mark(1);
                                                    if (inputStream.read() != 10) {
                                                        inputStream.reset();
                                                    }
                                                }
                                            }
                                            i6 = 0;
                                        }
                                        read = Math.max((i6 + 1) - iArr[bArr[i6] & 127], iArr2[i6]);
                                        if (read < 2) {
                                            if (sharedInputStream == null && i5 > 1) {
                                                byteArrayOutputStream.write(bArr2, 0, i5 - 1);
                                            }
                                            inputStream.reset();
                                            skipFully(inputStream, 1);
                                            if (i5 >= 1) {
                                                bArr2[0] = bArr2[i5 - 1];
                                                bArr2[1] = bArr[0];
                                                i5 = 2;
                                            } else {
                                                bArr2[0] = bArr[0];
                                                i5 = 1;
                                            }
                                        } else {
                                            if (i5 > 0 && sharedInputStream == null) {
                                                byteArrayOutputStream.write(bArr2, 0, i5);
                                            }
                                            i5 = read;
                                            inputStream.reset();
                                            skipFully(inputStream, (long) i5);
                                            byte[] bArr3 = bArr;
                                            bArr = bArr2;
                                            bArr2 = bArr3;
                                        }
                                        obj4 = null;
                                    } else if (ignoreMissingEndBoundary) {
                                        if (sharedInputStream != null) {
                                            j2 = sharedInputStream.getPosition();
                                        }
                                        this.complete = false;
                                        obj2 = 1;
                                    } else {
                                        messagingException = r42;
                                        messagingException2 = new MessagingException("missing multipart end boundary");
                                        throw messagingException;
                                    }
                                }
                                if (sharedInputStream != null) {
                                    createMimeBodyPart = createMimeBodyPart(sharedInputStream.newStream(j, j2));
                                } else {
                                    if (i5 - i4 > 0) {
                                        byteArrayOutputStream.write(bArr2, 0, i5 - i4);
                                    }
                                    if (!this.complete && readFully > 0) {
                                        byteArrayOutputStream.write(bArr, 0, readFully);
                                    }
                                    createMimeBodyPart = createMimeBodyPart(internetHeaders, byteArrayOutputStream.toByteArray());
                                }
                                super.addBodyPart(createMimeBodyPart);
                            } else {
                                messagingException = r42;
                                messagingException2 = new MessagingException("Stream doesn't support mark");
                                throw messagingException;
                            }
                        }
                        try {
                            inputStream.close();
                        } catch (IOException e2) {
                            iOException = e2;
                        }
                        this.parsed = true;
                    } catch (IOException e3) {
                        Exception exception = e3;
                        messagingException = r42;
                        messagingException2 = new MessagingException("IO Error", exception);
                        throw messagingException;
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        try {
                            inputStream.close();
                        } catch (IOException e22) {
                            iOException = e22;
                        }
                        throw th2;
                    }
                }
            } catch (Exception e32) {
                Exception exception2 = e32;
                messagingException = r42;
                messagingException2 = new MessagingException("No inputstream from datasource", exception2);
                throw messagingException;
            } catch (Throwable th3) {
                throw th3;
            }
        }
    }

    private static int readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        InputStream inputStream2 = inputStream;
        byte[] bArr2 = bArr;
        int i3 = i;
        int i4 = i2;
        if (i4 == 0) {
            return 0;
        }
        int i5;
        int i6 = 0;
        while (i4 > 0) {
            int read = inputStream2.read(bArr2, i3, i4);
            if (read <= 0) {
                break;
            }
            i3 += read;
            i6 += read;
            i4 -= read;
        }
        if (i6 > 0) {
            i5 = i6;
        } else {
            i5 = -1;
        }
        return i5;
    }

    private void skipFully(InputStream inputStream, long j) throws IOException {
        InputStream inputStream2 = inputStream;
        long j2 = j;
        while (j2 > 0) {
            long skip = inputStream2.skip(j2);
            if (skip <= 0) {
                EOFException eOFException = r10;
                EOFException eOFException2 = new EOFException("can't skip");
                throw eOFException;
            }
            j2 -= skip;
        }
    }

    /* Access modifiers changed, original: protected */
    public InternetHeaders createInternetHeaders(InputStream inputStream) throws MessagingException {
        InternetHeaders internetHeaders = r5;
        InternetHeaders internetHeaders2 = new InternetHeaders(inputStream);
        return internetHeaders;
    }

    /* Access modifiers changed, original: protected */
    public MimeBodyPart createMimeBodyPart(InternetHeaders internetHeaders, byte[] bArr) throws MessagingException {
        MimeBodyPart mimeBodyPart = r7;
        MimeBodyPart mimeBodyPart2 = new MimeBodyPart(internetHeaders, bArr);
        return mimeBodyPart;
    }

    /* Access modifiers changed, original: protected */
    public MimeBodyPart createMimeBodyPart(InputStream inputStream) throws MessagingException {
        MimeBodyPart mimeBodyPart = r5;
        MimeBodyPart mimeBodyPart2 = new MimeBodyPart(inputStream);
        return mimeBodyPart;
    }
}
