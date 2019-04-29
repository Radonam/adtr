package javax.mail.internet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownServiceException;
import javax.activation.DataSource;
import javax.mail.MessageAware;
import javax.mail.MessageContext;
import javax.mail.MessagingException;

public class MimePartDataSource implements DataSource, MessageAware {
    private static boolean ignoreMultipartEncoding;
    private MessageContext context;
    protected MimePart part;

    static {
        ignoreMultipartEncoding = true;
        try {
            boolean z;
            String property = System.getProperty("mail.mime.ignoremultipartencoding");
            if (property == null || !property.equalsIgnoreCase("false")) {
                z = true;
            } else {
                z = false;
            }
            ignoreMultipartEncoding = z;
        } catch (SecurityException e) {
            SecurityException securityException = e;
        }
    }

    public MimePartDataSource(MimePart mimePart) {
        this.part = mimePart;
    }

    public InputStream getInputStream() throws IOException {
        MessagingException messagingException;
        try {
            InputStream contentStream;
            if (this.part instanceof MimeBodyPart) {
                contentStream = ((MimeBodyPart) this.part).getContentStream();
            } else if (this.part instanceof MimeMessage) {
                contentStream = ((MimeMessage) this.part).getContentStream();
            } else {
                messagingException = r6;
                MessagingException messagingException2 = new MessagingException("Unknown part");
                throw messagingException;
            }
            String restrictEncoding = restrictEncoding(this.part.getEncoding(), this.part);
            if (restrictEncoding != null) {
                return MimeUtility.decode(contentStream, restrictEncoding);
            }
            return contentStream;
        } catch (MessagingException messagingException3) {
            MessagingException messagingException4 = messagingException3;
            IOException iOException = r6;
            IOException iOException2 = new IOException(messagingException4.getMessage());
            throw iOException;
        }
    }

    private static String restrictEncoding(String str, MimePart mimePart) throws MessagingException {
        String str2 = str;
        MimePart mimePart2 = mimePart;
        if (!ignoreMultipartEncoding || str2 == null) {
            return str2;
        }
        if (str2.equalsIgnoreCase("7bit") || str2.equalsIgnoreCase("8bit") || str2.equalsIgnoreCase("binary")) {
            return str2;
        }
        String contentType = mimePart2.getContentType();
        if (contentType == null) {
            return str2;
        }
        try {
            ContentType contentType2 = r7;
            ContentType contentType3 = new ContentType(contentType);
            ContentType contentType4 = contentType2;
            if (contentType4.match("multipart/*") || contentType4.match("message/*")) {
                return null;
            }
        } catch (ParseException e) {
            ParseException parseException = e;
        }
        return str2;
    }

    public OutputStream getOutputStream() throws IOException {
        UnknownServiceException unknownServiceException = r3;
        UnknownServiceException unknownServiceException2 = new UnknownServiceException();
        throw unknownServiceException;
    }

    public String getContentType() {
        try {
            return this.part.getContentType();
        } catch (MessagingException e) {
            MessagingException messagingException = e;
            return "application/octet-stream";
        }
    }

    public String getName() {
        try {
            if (this.part instanceof MimeBodyPart) {
                return ((MimeBodyPart) this.part).getFileName();
            }
        } catch (MessagingException e) {
            MessagingException messagingException = e;
        }
        return "";
    }

    public synchronized MessageContext getMessageContext() {
        MessageContext messageContext;
        synchronized (this) {
            if (this.context == null) {
                MessageContext messageContext2 = r6;
                MessageContext messageContext3 = new MessageContext(this.part);
                this.context = messageContext2;
            }
            messageContext = this.context;
        }
        return messageContext;
    }
}
