package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.FolderClosedIOException;
import com.sun.mail.util.LineOutputStream;
import com.sun.mail.util.MessageRemovedIOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.HeaderTokenizer.Token;

public class MimeBodyPart extends BodyPart implements MimePart {
    static boolean cacheMultipart;
    private static boolean decodeFileName;
    private static boolean encodeFileName;
    private static boolean setContentTypeFileName;
    private static boolean setDefaultTextCharset;
    private Object cachedContent;
    protected byte[] content;
    protected InputStream contentStream;
    protected DataHandler dh;
    protected InternetHeaders headers;

    static {
        setDefaultTextCharset = true;
        setContentTypeFileName = true;
        encodeFileName = false;
        decodeFileName = false;
        cacheMultipart = true;
        try {
            String property = System.getProperty("mail.mime.setdefaulttextcharset");
            boolean z = property == null || !property.equalsIgnoreCase("false");
            setDefaultTextCharset = z;
            property = System.getProperty("mail.mime.setcontenttypefilename");
            z = property == null || !property.equalsIgnoreCase("false");
            setContentTypeFileName = z;
            property = System.getProperty("mail.mime.encodefilename");
            z = (property == null || property.equalsIgnoreCase("false")) ? false : true;
            encodeFileName = z;
            property = System.getProperty("mail.mime.decodefilename");
            z = (property == null || property.equalsIgnoreCase("false")) ? false : true;
            decodeFileName = z;
            property = System.getProperty("mail.mime.cachemultipart");
            if (property == null || !property.equalsIgnoreCase("false")) {
                z = true;
            } else {
                z = false;
            }
            cacheMultipart = z;
        } catch (SecurityException e) {
            SecurityException securityException = e;
        }
    }

    public MimeBodyPart() {
        InternetHeaders internetHeaders = r4;
        InternetHeaders internetHeaders2 = new InternetHeaders();
        this.headers = internetHeaders;
    }

    public MimeBodyPart(InputStream inputStream) throws MessagingException {
        InputStream inputStream2 = inputStream;
        if (!((inputStream2 instanceof ByteArrayInputStream) || (inputStream2 instanceof BufferedInputStream) || (inputStream2 instanceof SharedInputStream))) {
            InputStream inputStream3 = r9;
            InputStream bufferedInputStream = new BufferedInputStream(inputStream2);
            inputStream2 = inputStream3;
        }
        InternetHeaders internetHeaders = r9;
        InternetHeaders internetHeaders2 = new InternetHeaders(inputStream2);
        this.headers = internetHeaders;
        if (inputStream2 instanceof SharedInputStream) {
            SharedInputStream sharedInputStream = (SharedInputStream) inputStream2;
            this.contentStream = sharedInputStream.newStream(sharedInputStream.getPosition(), -1);
            return;
        }
        try {
            this.content = ASCIIUtility.getBytes(inputStream2);
        } catch (IOException e) {
            Exception exception = e;
            MessagingException messagingException = r9;
            MessagingException messagingException2 = new MessagingException("Error reading input stream", exception);
            throw messagingException;
        }
    }

    public MimeBodyPart(InternetHeaders internetHeaders, byte[] bArr) throws MessagingException {
        byte[] bArr2 = bArr;
        this.headers = internetHeaders;
        this.content = bArr2;
    }

    public int getSize() throws MessagingException {
        if (this.content != null) {
            return this.content.length;
        }
        if (this.contentStream != null) {
            try {
                int available = this.contentStream.available();
                if (available > 0) {
                    return available;
                }
            } catch (IOException e) {
                IOException iOException = e;
            }
        }
        return -1;
    }

    public int getLineCount() throws MessagingException {
        return -1;
    }

    public String getContentType() throws MessagingException {
        String header = getHeader("Content-Type", null);
        if (header == null) {
            header = "text/plain";
        }
        return header;
    }

    public boolean isMimeType(String str) throws MessagingException {
        return isMimeType(this, str);
    }

    public String getDisposition() throws MessagingException {
        return getDisposition(this);
    }

    public void setDisposition(String str) throws MessagingException {
        setDisposition(this, str);
    }

    public String getEncoding() throws MessagingException {
        return getEncoding(this);
    }

    public String getContentID() throws MessagingException {
        return getHeader("Content-Id", null);
    }

    public void setContentID(String str) throws MessagingException {
        String str2 = str;
        if (str2 == null) {
            removeHeader("Content-ID");
        } else {
            setHeader("Content-ID", str2);
        }
    }

    public String getContentMD5() throws MessagingException {
        return getHeader("Content-MD5", null);
    }

    public void setContentMD5(String str) throws MessagingException {
        setHeader("Content-MD5", str);
    }

    public String[] getContentLanguage() throws MessagingException {
        return getContentLanguage(this);
    }

    public void setContentLanguage(String[] strArr) throws MessagingException {
        setContentLanguage(this, strArr);
    }

    public String getDescription() throws MessagingException {
        return getDescription(this);
    }

    public void setDescription(String str) throws MessagingException {
        setDescription(str, null);
    }

    public void setDescription(String str, String str2) throws MessagingException {
        setDescription(this, str, str2);
    }

    public String getFileName() throws MessagingException {
        return getFileName(this);
    }

    public void setFileName(String str) throws MessagingException {
        setFileName(this, str);
    }

    public InputStream getInputStream() throws IOException, MessagingException {
        return getDataHandler().getInputStream();
    }

    /* Access modifiers changed, original: protected */
    public InputStream getContentStream() throws MessagingException {
        if (this.contentStream != null) {
            return ((SharedInputStream) this.contentStream).newStream(0, -1);
        }
        if (this.content != null) {
            InputStream inputStream = r6;
            InputStream byteArrayInputStream = new ByteArrayInputStream(this.content);
            return inputStream;
        }
        MessagingException messagingException = r6;
        MessagingException messagingException2 = new MessagingException("No content");
        throw messagingException;
    }

    public InputStream getRawInputStream() throws MessagingException {
        return getContentStream();
    }

    public DataHandler getDataHandler() throws MessagingException {
        if (this.dh == null) {
            DataHandler dataHandler = r7;
            DataSource dataSource = r7;
            MimePartDataSource mimePartDataSource = new MimePartDataSource(this);
            DataHandler dataHandler2 = new DataHandler(dataSource);
            this.dh = dataHandler;
        }
        return this.dh;
    }

    public Object getContent() throws IOException, MessagingException {
        if (this.cachedContent != null) {
            return this.cachedContent;
        }
        try {
            Object content = getDataHandler().getContent();
            if (cacheMultipart && (((content instanceof Multipart) || (content instanceof Message)) && !(this.content == null && this.contentStream == null))) {
                this.cachedContent = content;
            }
            return content;
        } catch (FolderClosedIOException e) {
            FolderClosedIOException folderClosedIOException = e;
            FolderClosedException folderClosedException = r7;
            FolderClosedException folderClosedException2 = new FolderClosedException(folderClosedIOException.getFolder(), folderClosedIOException.getMessage());
            throw folderClosedException;
        } catch (MessageRemovedIOException e2) {
            MessageRemovedIOException messageRemovedIOException = e2;
            MessageRemovedException messageRemovedException = r7;
            MessageRemovedException messageRemovedException2 = new MessageRemovedException(messageRemovedIOException.getMessage());
            throw messageRemovedException;
        }
    }

    public void setDataHandler(DataHandler dataHandler) throws MessagingException {
        this.dh = dataHandler;
        this.cachedContent = null;
        invalidateContentHeaders(this);
    }

    public void setContent(Object obj, String str) throws MessagingException {
        Object obj2 = obj;
        String str2 = str;
        if (obj2 instanceof Multipart) {
            setContent((Multipart) obj2);
            return;
        }
        DataHandler dataHandler = r8;
        DataHandler dataHandler2 = new DataHandler(obj2, str2);
        setDataHandler(dataHandler);
    }

    public void setText(String str) throws MessagingException {
        setText(str, null);
    }

    public void setText(String str, String str2) throws MessagingException {
        setText(this, str, str2, "plain");
    }

    public void setText(String str, String str2, String str3) throws MessagingException {
        setText(this, str, str2, str3);
    }

    public void setContent(Multipart multipart) throws MessagingException {
        Multipart multipart2 = multipart;
        DataHandler dataHandler = r7;
        DataHandler dataHandler2 = new DataHandler(multipart2, multipart2.getContentType());
        setDataHandler(dataHandler);
        multipart2.setParent(this);
    }

    public void attachFile(File file) throws IOException, MessagingException {
        FileDataSource fileDataSource = r7;
        FileDataSource fileDataSource2 = new FileDataSource(file);
        FileDataSource fileDataSource3 = fileDataSource;
        DataHandler dataHandler = r7;
        DataHandler dataHandler2 = new DataHandler((DataSource) fileDataSource3);
        setDataHandler(dataHandler);
        setFileName(fileDataSource3.getName());
    }

    public void attachFile(String str) throws IOException, MessagingException {
        File file = r6;
        File file2 = new File(str);
        attachFile(file);
    }

    public void saveFile(File file) throws IOException, MessagingException {
        IOException iOException;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            OutputStream outputStream2 = r13;
            OutputStream outputStream3 = r13;
            OutputStream fileOutputStream = new FileOutputStream(file);
            OutputStream bufferedOutputStream = new BufferedOutputStream(outputStream3);
            outputStream = outputStream2;
            inputStream = getInputStream();
            byte[] bArr = new byte[8192];
            while (true) {
                int read = inputStream.read(bArr);
                int i = read;
                if (read <= 0) {
                    break;
                }
                outputStream.write(bArr, 0, i);
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    iOException = e;
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e2) {
                    iOException = e2;
                }
            }
        } catch (Throwable th) {
            Throwable th2 = th;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e22) {
                    iOException = e22;
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e222) {
                    iOException = e222;
                }
            }
            Throwable th3 = th2;
        }
    }

    public void saveFile(String str) throws IOException, MessagingException {
        File file = r6;
        File file2 = new File(str);
        saveFile(file);
    }

    public void writeTo(OutputStream outputStream) throws IOException, MessagingException {
        writeTo(this, outputStream, null);
    }

    public String[] getHeader(String str) throws MessagingException {
        return this.headers.getHeader(str);
    }

    public String getHeader(String str, String str2) throws MessagingException {
        return this.headers.getHeader(str, str2);
    }

    public void setHeader(String str, String str2) throws MessagingException {
        this.headers.setHeader(str, str2);
    }

    public void addHeader(String str, String str2) throws MessagingException {
        this.headers.addHeader(str, str2);
    }

    public void removeHeader(String str) throws MessagingException {
        this.headers.removeHeader(str);
    }

    public Enumeration getAllHeaders() throws MessagingException {
        return this.headers.getAllHeaders();
    }

    public Enumeration getMatchingHeaders(String[] strArr) throws MessagingException {
        return this.headers.getMatchingHeaders(strArr);
    }

    public Enumeration getNonMatchingHeaders(String[] strArr) throws MessagingException {
        return this.headers.getNonMatchingHeaders(strArr);
    }

    public void addHeaderLine(String str) throws MessagingException {
        this.headers.addHeaderLine(str);
    }

    public Enumeration getAllHeaderLines() throws MessagingException {
        return this.headers.getAllHeaderLines();
    }

    public Enumeration getMatchingHeaderLines(String[] strArr) throws MessagingException {
        return this.headers.getMatchingHeaderLines(strArr);
    }

    public Enumeration getNonMatchingHeaderLines(String[] strArr) throws MessagingException {
        return this.headers.getNonMatchingHeaderLines(strArr);
    }

    /* Access modifiers changed, original: protected */
    public void updateHeaders() throws MessagingException {
        updateHeaders(this);
        if (this.cachedContent != null) {
            DataHandler dataHandler = r7;
            DataHandler dataHandler2 = new DataHandler(this.cachedContent, getContentType());
            this.dh = dataHandler;
            this.cachedContent = null;
            this.content = null;
            if (this.contentStream != null) {
                try {
                    this.contentStream.close();
                } catch (IOException e) {
                    IOException iOException = e;
                }
            }
            this.contentStream = null;
        }
    }

    static boolean isMimeType(MimePart mimePart, String str) throws MessagingException {
        MimePart mimePart2 = mimePart;
        String str2 = str;
        try {
            ContentType contentType = r6;
            ContentType contentType2 = new ContentType(mimePart2.getContentType());
            return contentType.match(str2);
        } catch (ParseException e) {
            ParseException parseException = e;
            return mimePart2.getContentType().equalsIgnoreCase(str2);
        }
    }

    static void setText(MimePart mimePart, String str, String str2, String str3) throws MessagingException {
        MimePart mimePart2 = mimePart;
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        if (str5 == null) {
            if (MimeUtility.checkAscii(str4) != 1) {
                str5 = MimeUtility.getDefaultMIMECharset();
            } else {
                str5 = "us-ascii";
            }
        }
        MimePart mimePart3 = mimePart2;
        String str7 = str4;
        StringBuilder stringBuilder = r9;
        StringBuilder stringBuilder2 = new StringBuilder("text/");
        mimePart3.setContent(str7, stringBuilder.append(str6).append("; charset=").append(MimeUtility.quote(str5, HeaderTokenizer.MIME)).toString());
    }

    static String getDisposition(MimePart mimePart) throws MessagingException {
        String header = mimePart.getHeader("Content-Disposition", null);
        if (header == null) {
            return null;
        }
        ContentDisposition contentDisposition = r6;
        ContentDisposition contentDisposition2 = new ContentDisposition(header);
        return contentDisposition.getDisposition();
    }

    static void setDisposition(MimePart mimePart, String str) throws MessagingException {
        MimePart mimePart2 = mimePart;
        String str2 = str;
        if (str2 == null) {
            mimePart2.removeHeader("Content-Disposition");
            return;
        }
        String header = mimePart2.getHeader("Content-Disposition", null);
        if (header != null) {
            ContentDisposition contentDisposition = r7;
            ContentDisposition contentDisposition2 = new ContentDisposition(header);
            ContentDisposition contentDisposition3 = contentDisposition;
            contentDisposition3.setDisposition(str2);
            str2 = contentDisposition3.toString();
        }
        mimePart2.setHeader("Content-Disposition", str2);
    }

    static String getDescription(MimePart mimePart) throws MessagingException {
        String header = mimePart.getHeader("Content-Description", null);
        if (header == null) {
            return null;
        }
        try {
            return MimeUtility.decodeText(MimeUtility.unfold(header));
        } catch (UnsupportedEncodingException e) {
            UnsupportedEncodingException unsupportedEncodingException = e;
            return header;
        }
    }

    static void setDescription(MimePart mimePart, String str, String str2) throws MessagingException {
        MimePart mimePart2 = mimePart;
        String str3 = str;
        String str4 = str2;
        if (str3 == null) {
            mimePart2.removeHeader("Content-Description");
            return;
        }
        try {
            mimePart2.setHeader("Content-Description", MimeUtility.fold(21, MimeUtility.encodeText(str3, str4, null)));
        } catch (UnsupportedEncodingException e) {
            Exception exception = e;
            MessagingException messagingException = r10;
            MessagingException messagingException2 = new MessagingException("Encoding error", exception);
            throw messagingException;
        }
    }

    static String getFileName(MimePart mimePart) throws MessagingException {
        MimePart mimePart2 = mimePart;
        String str = null;
        String header = mimePart2.getHeader("Content-Disposition", null);
        if (header != null) {
            ContentDisposition contentDisposition = r8;
            ContentDisposition contentDisposition2 = new ContentDisposition(header);
            str = contentDisposition.getParameter("filename");
        }
        if (str == null) {
            header = mimePart2.getHeader("Content-Type", null);
            if (header != null) {
                try {
                    ContentType contentType = r8;
                    ContentType contentType2 = new ContentType(header);
                    str = contentType.getParameter("name");
                } catch (ParseException e) {
                    ParseException parseException = e;
                }
            }
        }
        if (decodeFileName && str != null) {
            try {
                str = MimeUtility.decodeText(str);
            } catch (UnsupportedEncodingException e2) {
                Exception exception = e2;
                MessagingException messagingException = r8;
                MessagingException messagingException2 = new MessagingException("Can't decode filename", exception);
                throw messagingException;
            }
        }
        return str;
    }

    static void setFileName(MimePart mimePart, String str) throws MessagingException {
        MimePart mimePart2 = mimePart;
        String str2 = str;
        if (encodeFileName && str2 != null) {
            try {
                str2 = MimeUtility.encodeText(str2);
            } catch (UnsupportedEncodingException e) {
                Exception exception = e;
                MessagingException messagingException = r9;
                MessagingException messagingException2 = new MessagingException("Can't encode filename", exception);
                throw messagingException;
            }
        }
        String header = mimePart2.getHeader("Content-Disposition", null);
        ContentDisposition contentDisposition = r6;
        ContentDisposition contentDisposition2 = contentDisposition2;
        contentDisposition2 = new ContentDisposition(header == null ? Part.ATTACHMENT : header);
        ContentDisposition contentDisposition3 = contentDisposition;
        contentDisposition3.setParameter("filename", str2);
        mimePart2.setHeader("Content-Disposition", contentDisposition3.toString());
        if (setContentTypeFileName) {
            header = mimePart2.getHeader("Content-Type", null);
            if (header != null) {
                try {
                    ContentType contentType = r9;
                    ContentType contentType2 = new ContentType(header);
                    ContentType contentType3 = contentType;
                    contentType3.setParameter("name", str2);
                    mimePart2.setHeader("Content-Type", contentType3.toString());
                } catch (ParseException e2) {
                    ParseException parseException = e2;
                }
            }
        }
    }

    static String[] getContentLanguage(MimePart mimePart) throws MessagingException {
        String header = mimePart.getHeader("Content-Language", null);
        if (header == null) {
            return null;
        }
        HeaderTokenizer headerTokenizer = r11;
        HeaderTokenizer headerTokenizer2 = new HeaderTokenizer(header, HeaderTokenizer.MIME);
        HeaderTokenizer headerTokenizer3 = headerTokenizer;
        Vector vector = r11;
        Vector vector2 = new Vector();
        Vector vector3 = vector;
        while (true) {
            Token next = headerTokenizer3.next();
            int type = next.getType();
            if (type == -4) {
                break;
            } else if (type == -1) {
                vector3.addElement(next.getValue());
            }
        }
        if (vector3.size() == 0) {
            return null;
        }
        String[] strArr = new String[vector3.size()];
        vector3.copyInto(strArr);
        return strArr;
    }

    static void setContentLanguage(MimePart mimePart, String[] strArr) throws MessagingException {
        MimePart mimePart2 = mimePart;
        String[] strArr2 = strArr;
        StringBuffer stringBuffer = r8;
        StringBuffer stringBuffer2 = new StringBuffer(strArr2[0]);
        StringBuffer stringBuffer3 = stringBuffer;
        for (int i = 1; i < strArr2.length; i++) {
            stringBuffer = stringBuffer3.append(',').append(strArr2[i]);
        }
        mimePart2.setHeader("Content-Language", stringBuffer3.toString());
    }

    static String getEncoding(MimePart mimePart) throws MessagingException {
        String header = mimePart.getHeader("Content-Transfer-Encoding", null);
        if (header == null) {
            return null;
        }
        header = header.trim();
        if (header.equalsIgnoreCase("7bit") || header.equalsIgnoreCase("8bit") || header.equalsIgnoreCase("quoted-printable") || header.equalsIgnoreCase("binary") || header.equalsIgnoreCase("base64")) {
            return header;
        }
        Token next;
        HeaderTokenizer headerTokenizer = r9;
        HeaderTokenizer headerTokenizer2 = new HeaderTokenizer(header, HeaderTokenizer.MIME);
        HeaderTokenizer headerTokenizer3 = headerTokenizer;
        int type;
        do {
            next = headerTokenizer3.next();
            type = next.getType();
            if (type == -4) {
                return header;
            }
        } while (type != -1);
        return next.getValue();
    }

    static void setEncoding(MimePart mimePart, String str) throws MessagingException {
        mimePart.setHeader("Content-Transfer-Encoding", str);
    }

    static void updateHeaders(MimePart mimePart) throws MessagingException {
        MimePart mimePart2 = mimePart;
        DataHandler dataHandler = mimePart2.getDataHandler();
        if (dataHandler != null) {
            MessagingException messagingException;
            MessagingException messagingException2;
            try {
                String defaultMIMECharset;
                String contentType = dataHandler.getContentType();
                Object obj = null;
                Object obj2 = mimePart2.getHeader("Content-Type") == null ? 1 : null;
                ContentType contentType2 = r14;
                ContentType contentType3 = new ContentType(contentType);
                ContentType contentType4 = contentType2;
                if (contentType4.match("multipart/*")) {
                    Object content;
                    obj = 1;
                    if (mimePart2 instanceof MimeBodyPart) {
                        MimeBodyPart mimeBodyPart = (MimeBodyPart) mimePart2;
                        content = mimeBodyPart.cachedContent != null ? mimeBodyPart.cachedContent : dataHandler.getContent();
                    } else if (mimePart2 instanceof MimeMessage) {
                        MimeMessage mimeMessage = (MimeMessage) mimePart2;
                        content = mimeMessage.cachedContent != null ? mimeMessage.cachedContent : dataHandler.getContent();
                    } else {
                        content = dataHandler.getContent();
                    }
                    if (content instanceof MimeMultipart) {
                        ((MimeMultipart) content).updateHeaders();
                    } else {
                        messagingException = r14;
                        StringBuilder stringBuilder = r14;
                        StringBuilder stringBuilder2 = new StringBuilder("MIME part of type \"");
                        messagingException2 = new MessagingException(stringBuilder.append(contentType).append("\" contains object of type ").append(content.getClass().getName()).append(" instead of MimeMultipart").toString());
                        throw messagingException;
                    }
                } else if (contentType4.match("message/rfc822")) {
                    int obj3 = 1;
                }
                if (obj3 == null) {
                    if (mimePart2.getHeader("Content-Transfer-Encoding") == null) {
                        setEncoding(mimePart2, MimeUtility.getEncoding(dataHandler));
                    }
                    if (obj2 != null && setDefaultTextCharset && contentType4.match("text/*") && contentType4.getParameter("charset") == null) {
                        String encoding = mimePart2.getEncoding();
                        if (encoding == null || !encoding.equalsIgnoreCase("7bit")) {
                            defaultMIMECharset = MimeUtility.getDefaultMIMECharset();
                        } else {
                            defaultMIMECharset = "us-ascii";
                        }
                        contentType4.setParameter("charset", defaultMIMECharset);
                        contentType = contentType4.toString();
                    }
                }
                if (obj2 != null) {
                    defaultMIMECharset = mimePart2.getHeader("Content-Disposition", null);
                    if (defaultMIMECharset != null) {
                        ContentDisposition contentDisposition = r14;
                        ContentDisposition contentDisposition2 = new ContentDisposition(defaultMIMECharset);
                        String parameter = contentDisposition.getParameter("filename");
                        if (parameter != null) {
                            contentType4.setParameter("name", parameter);
                            contentType = contentType4.toString();
                        }
                    }
                    mimePart2.setHeader("Content-Type", contentType);
                }
            } catch (IOException e) {
                Exception exception = e;
                messagingException = r14;
                messagingException2 = new MessagingException("IOException updating headers", exception);
                throw messagingException;
            }
        }
    }

    static void invalidateContentHeaders(MimePart mimePart) throws MessagingException {
        MimePart mimePart2 = mimePart;
        mimePart2.removeHeader("Content-Type");
        mimePart2.removeHeader("Content-Transfer-Encoding");
    }

    static void writeTo(MimePart mimePart, OutputStream outputStream, String[] strArr) throws IOException, MessagingException {
        LineOutputStream lineOutputStream;
        MimePart mimePart2 = mimePart;
        OutputStream outputStream2 = outputStream;
        String[] strArr2 = strArr;
        Object obj = null;
        if (outputStream2 instanceof LineOutputStream) {
            lineOutputStream = (LineOutputStream) outputStream2;
        } else {
            LineOutputStream lineOutputStream2 = r8;
            LineOutputStream lineOutputStream3 = new LineOutputStream(outputStream2);
            lineOutputStream = lineOutputStream2;
        }
        Enumeration nonMatchingHeaderLines = mimePart2.getNonMatchingHeaderLines(strArr2);
        while (nonMatchingHeaderLines.hasMoreElements()) {
            lineOutputStream.writeln((String) nonMatchingHeaderLines.nextElement());
        }
        lineOutputStream.writeln();
        outputStream2 = MimeUtility.encode(outputStream2, mimePart2.getEncoding());
        mimePart2.getDataHandler().writeTo(outputStream2);
        outputStream2.flush();
    }
}
