package javax.mail.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

public class ByteArrayDataSource implements DataSource {
    private byte[] data;
    private int len = -1;
    private String name = "";
    private String type;

    static class DSByteArrayOutputStream extends ByteArrayOutputStream {
        DSByteArrayOutputStream() {
        }

        public byte[] getBuf() {
            return this.buf;
        }

        public int getCount() {
            return this.count;
        }
    }

    public ByteArrayDataSource(InputStream inputStream, String str) throws IOException {
        InputStream inputStream2 = inputStream;
        String str2 = str;
        DSByteArrayOutputStream dSByteArrayOutputStream = r10;
        DSByteArrayOutputStream dSByteArrayOutputStream2 = new DSByteArrayOutputStream();
        DSByteArrayOutputStream dSByteArrayOutputStream3 = dSByteArrayOutputStream;
        byte[] bArr = new byte[8192];
        while (true) {
            int read = inputStream2.read(bArr);
            int i = read;
            if (read <= 0) {
                break;
            }
            dSByteArrayOutputStream3.write(bArr, 0, i);
        }
        this.data = dSByteArrayOutputStream3.getBuf();
        this.len = dSByteArrayOutputStream3.getCount();
        if (this.data.length - this.len > 262144) {
            this.data = dSByteArrayOutputStream3.toByteArray();
            this.len = this.data.length;
        }
        this.type = str2;
    }

    public ByteArrayDataSource(byte[] bArr, String str) {
        byte[] bArr2 = bArr;
        String str2 = str;
        this.data = bArr2;
        this.type = str2;
    }

    public ByteArrayDataSource(String str, String str2) throws IOException {
        String str3 = str;
        String str4 = str2;
        String str5 = null;
        try {
            ContentType contentType = r8;
            ContentType contentType2 = new ContentType(str4);
            str5 = contentType.getParameter("charset");
        } catch (ParseException e) {
            ParseException parseException = e;
        }
        if (str5 == null) {
            str5 = MimeUtility.getDefaultJavaCharset();
        }
        this.data = str3.getBytes(str5);
        this.type = str4;
    }

    public InputStream getInputStream() throws IOException {
        if (this.data == null) {
            IOException iOException = r6;
            IOException iOException2 = new IOException("no data");
            throw iOException;
        }
        if (this.len < 0) {
            this.len = this.data.length;
        }
        InputStream inputStream = r6;
        InputStream sharedByteArrayInputStream = new SharedByteArrayInputStream(this.data, 0, this.len);
        return inputStream;
    }

    public OutputStream getOutputStream() throws IOException {
        IOException iOException = r4;
        IOException iOException2 = new IOException("cannot do this");
        throw iOException;
    }

    public String getContentType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
