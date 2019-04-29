package javax.mail.internet;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.FolderClosedIOException;
import com.sun.mail.util.LineOutputStream;
import com.sun.mail.util.MessageRemovedIOException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessageRemovedException;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.util.SharedByteArrayInputStream;

public class MimeMessage extends Message implements MimePart {
    private static final Flags answeredFlag;
    private static MailDateFormat mailDateFormat;
    Object cachedContent;
    protected byte[] content;
    protected InputStream contentStream;
    protected DataHandler dh;
    protected Flags flags;
    protected InternetHeaders headers;
    protected boolean modified;
    protected boolean saved;
    private boolean strict;

    public static class RecipientType extends javax.mail.Message.RecipientType {
        public static final RecipientType NEWSGROUPS;
        private static final long serialVersionUID = -5468290701714395543L;

        static {
            RecipientType recipientType = r3;
            RecipientType recipientType2 = new RecipientType("Newsgroups");
            NEWSGROUPS = recipientType;
        }

        protected RecipientType(String str) {
            super(str);
        }

        /* Access modifiers changed, original: protected */
        public Object readResolve() throws ObjectStreamException {
            if (this.type.equals("Newsgroups")) {
                return NEWSGROUPS;
            }
            return super.readResolve();
        }
    }

    static {
        MailDateFormat mailDateFormat = r3;
        MailDateFormat mailDateFormat2 = new MailDateFormat();
        mailDateFormat = mailDateFormat;
        Flags flags = r3;
        Flags flags2 = new Flags(Flag.ANSWERED);
        answeredFlag = flags;
    }

    public MimeMessage(Session session) {
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.modified = true;
        InternetHeaders internetHeaders = r5;
        InternetHeaders internetHeaders2 = new InternetHeaders();
        this.headers = internetHeaders;
        Flags flags = r5;
        Flags flags2 = new Flags();
        this.flags = flags;
        initStrict();
    }

    public MimeMessage(Session session, InputStream inputStream) throws MessagingException {
        InputStream inputStream2 = inputStream;
        super(session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        Flags flags = r6;
        Flags flags2 = new Flags();
        this.flags = flags;
        initStrict();
        parse(inputStream2);
        this.saved = true;
    }

    public MimeMessage(MimeMessage mimeMessage) throws MessagingException {
        OutputStream outputStream;
        MimeMessage mimeMessage2 = mimeMessage;
        super(mimeMessage2.session);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        this.flags = mimeMessage2.getFlags();
        int size = mimeMessage2.getSize();
        OutputStream outputStream2;
        OutputStream byteArrayOutputStream;
        if (size > 0) {
            outputStream2 = r9;
            byteArrayOutputStream = new ByteArrayOutputStream(size);
            outputStream = outputStream2;
        } else {
            outputStream2 = r9;
            byteArrayOutputStream = new ByteArrayOutputStream();
            outputStream = outputStream2;
        }
        try {
            this.strict = mimeMessage2.strict;
            mimeMessage2.writeTo(outputStream);
            outputStream.close();
            InputStream inputStream = r9;
            InputStream sharedByteArrayInputStream = new SharedByteArrayInputStream(outputStream.toByteArray());
            InputStream inputStream2 = inputStream;
            parse(inputStream2);
            inputStream2.close();
            this.saved = true;
        } catch (IOException e) {
            Exception exception = e;
            MessagingException messagingException = r9;
            MessagingException messagingException2 = new MessagingException("IOException while copying message", exception);
            throw messagingException;
        }
    }

    protected MimeMessage(Folder folder, int i) {
        super(folder, i);
        this.modified = false;
        this.saved = false;
        this.strict = true;
        Flags flags = r6;
        Flags flags2 = new Flags();
        this.flags = flags;
        this.saved = true;
        initStrict();
    }

    protected MimeMessage(Folder folder, InputStream inputStream, int i) throws MessagingException {
        InputStream inputStream2 = inputStream;
        this(folder, i);
        initStrict();
        parse(inputStream2);
    }

    protected MimeMessage(Folder folder, InternetHeaders internetHeaders, byte[] bArr, int i) throws MessagingException {
        InternetHeaders internetHeaders2 = internetHeaders;
        byte[] bArr2 = bArr;
        this(folder, i);
        this.headers = internetHeaders2;
        this.content = bArr2;
        initStrict();
    }

    private void initStrict() {
        if (this.session != null) {
            String property = this.session.getProperty("mail.mime.address.strict");
            boolean z = property == null || !property.equalsIgnoreCase("false");
            this.strict = z;
        }
    }

    /* Access modifiers changed, original: protected */
    public void parse(InputStream inputStream) throws MessagingException {
        InputStream inputStream2 = inputStream;
        if (!((inputStream2 instanceof ByteArrayInputStream) || (inputStream2 instanceof BufferedInputStream) || (inputStream2 instanceof SharedInputStream))) {
            InputStream inputStream3 = r9;
            InputStream bufferedInputStream = new BufferedInputStream(inputStream2);
            inputStream2 = inputStream3;
        }
        this.headers = createInternetHeaders(inputStream2);
        if (inputStream2 instanceof SharedInputStream) {
            SharedInputStream sharedInputStream = (SharedInputStream) inputStream2;
            this.contentStream = sharedInputStream.newStream(sharedInputStream.getPosition(), -1);
        } else {
            try {
                this.content = ASCIIUtility.getBytes(inputStream2);
            } catch (IOException e) {
                Exception exception = e;
                MessagingException messagingException = r9;
                MessagingException messagingException2 = new MessagingException("IOException", exception);
                throw messagingException;
            }
        }
        this.modified = false;
    }

    public Address[] getFrom() throws MessagingException {
        Address[] addressHeader = getAddressHeader("From");
        if (addressHeader == null) {
            addressHeader = getAddressHeader("Sender");
        }
        return addressHeader;
    }

    public void setFrom(Address address) throws MessagingException {
        Address address2 = address;
        if (address2 == null) {
            removeHeader("From");
        } else {
            setHeader("From", address2.toString());
        }
    }

    public void setFrom() throws MessagingException {
        Address localAddress = InternetAddress.getLocalAddress(this.session);
        if (localAddress != null) {
            setFrom(localAddress);
            return;
        }
        MessagingException messagingException = r5;
        MessagingException messagingException2 = new MessagingException("No From address");
        throw messagingException;
    }

    public void addFrom(Address[] addressArr) throws MessagingException {
        addAddressHeader("From", addressArr);
    }

    public Address getSender() throws MessagingException {
        Address[] addressHeader = getAddressHeader("Sender");
        if (addressHeader == null || addressHeader.length == 0) {
            return null;
        }
        return addressHeader[0];
    }

    public void setSender(Address address) throws MessagingException {
        Address address2 = address;
        if (address2 == null) {
            removeHeader("Sender");
        } else {
            setHeader("Sender", address2.toString());
        }
    }

    public Address[] getRecipients(javax.mail.Message.RecipientType recipientType) throws MessagingException {
        javax.mail.Message.RecipientType recipientType2 = recipientType;
        if (recipientType2 != RecipientType.NEWSGROUPS) {
            return getAddressHeader(getHeaderName(recipientType2));
        }
        String header = getHeader("Newsgroups", ",");
        return header == null ? null : NewsAddress.parse(header);
    }

    public Address[] getAllRecipients() throws MessagingException {
        Object allRecipients = super.getAllRecipients();
        Object recipients = getRecipients(RecipientType.NEWSGROUPS);
        if (recipients == null) {
            return allRecipients;
        }
        if (allRecipients == null) {
            return recipients;
        }
        Object obj = new Address[(allRecipients.length + recipients.length)];
        System.arraycopy(allRecipients, 0, obj, 0, allRecipients.length);
        System.arraycopy(recipients, 0, obj, allRecipients.length, recipients.length);
        return obj;
    }

    public void setRecipients(javax.mail.Message.RecipientType recipientType, Address[] addressArr) throws MessagingException {
        javax.mail.Message.RecipientType recipientType2 = recipientType;
        Address[] addressArr2 = addressArr;
        if (recipientType2 != RecipientType.NEWSGROUPS) {
            setAddressHeader(getHeaderName(recipientType2), addressArr2);
        } else if (addressArr2 == null || addressArr2.length == 0) {
            removeHeader("Newsgroups");
        } else {
            setHeader("Newsgroups", NewsAddress.toString(addressArr2));
        }
    }

    public void setRecipients(javax.mail.Message.RecipientType recipientType, String str) throws MessagingException {
        javax.mail.Message.RecipientType recipientType2 = recipientType;
        String str2 = str;
        if (recipientType2 != RecipientType.NEWSGROUPS) {
            setAddressHeader(getHeaderName(recipientType2), InternetAddress.parse(str2));
        } else if (str2 == null || str2.length() == 0) {
            removeHeader("Newsgroups");
        } else {
            setHeader("Newsgroups", str2);
        }
    }

    public void addRecipients(javax.mail.Message.RecipientType recipientType, Address[] addressArr) throws MessagingException {
        javax.mail.Message.RecipientType recipientType2 = recipientType;
        Address[] addressArr2 = addressArr;
        if (recipientType2 == RecipientType.NEWSGROUPS) {
            String newsAddress = NewsAddress.toString(addressArr2);
            if (newsAddress != null) {
                addHeader("Newsgroups", newsAddress);
                return;
            }
            return;
        }
        addAddressHeader(getHeaderName(recipientType2), addressArr2);
    }

    public void addRecipients(javax.mail.Message.RecipientType recipientType, String str) throws MessagingException {
        javax.mail.Message.RecipientType recipientType2 = recipientType;
        String str2 = str;
        if (recipientType2 != RecipientType.NEWSGROUPS) {
            addAddressHeader(getHeaderName(recipientType2), InternetAddress.parse(str2));
        } else if (str2 != null && str2.length() != 0) {
            addHeader("Newsgroups", str2);
        }
    }

    public Address[] getReplyTo() throws MessagingException {
        Address[] addressHeader = getAddressHeader("Reply-To");
        if (addressHeader == null) {
            addressHeader = getFrom();
        }
        return addressHeader;
    }

    public void setReplyTo(Address[] addressArr) throws MessagingException {
        setAddressHeader("Reply-To", addressArr);
    }

    private Address[] getAddressHeader(String str) throws MessagingException {
        String header = getHeader(str, ",");
        return header == null ? null : InternetAddress.parseHeader(header, this.strict);
    }

    private void setAddressHeader(String str, Address[] addressArr) throws MessagingException {
        String str2 = str;
        String internetAddress = InternetAddress.toString(addressArr);
        if (internetAddress == null) {
            removeHeader(str2);
        } else {
            setHeader(str2, internetAddress);
        }
    }

    private void addAddressHeader(String str, Address[] addressArr) throws MessagingException {
        String str2 = str;
        String internetAddress = InternetAddress.toString(addressArr);
        if (internetAddress != null) {
            addHeader(str2, internetAddress);
        }
    }

    public String getSubject() throws MessagingException {
        String header = getHeader("Subject", null);
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

    public void setSubject(String str) throws MessagingException {
        setSubject(str, null);
    }

    public void setSubject(String str, String str2) throws MessagingException {
        String str3 = str;
        String str4 = str2;
        if (str3 == null) {
            removeHeader("Subject");
            return;
        }
        try {
            setHeader("Subject", MimeUtility.fold(9, MimeUtility.encodeText(str3, str4, null)));
        } catch (UnsupportedEncodingException e) {
            Exception exception = e;
            MessagingException messagingException = r10;
            MessagingException messagingException2 = new MessagingException("Encoding error", exception);
            throw messagingException;
        }
    }

    /*  JADX ERROR: ConcurrentModificationException in pass: EliminatePhiNodes
        java.util.ConcurrentModificationException
        	at java.util.ArrayList$Itr.checkForComodification(Unknown Source)
        	at java.util.ArrayList$Itr.next(Unknown Source)
        	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.replaceMerge(EliminatePhiNodes.java:114)
        	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.replaceMergeInstructions(EliminatePhiNodes.java:68)
        	at jadx.core.dex.visitors.ssa.EliminatePhiNodes.visit(EliminatePhiNodes.java:31)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    public java.util.Date getSentDate() throws javax.mail.MessagingException {
        /*
        r7 = this;
        r0 = r7;
        r3 = r0;
        r4 = "Date";
        r5 = 0;
        r3 = r3.getHeader(r4, r5);
        r1 = r3;
        r3 = r1;
        if (r3 == 0) goto L_0x0028;
    L_0x000d:
        r3 = mailDateFormat;	 Catch:{ ParseException -> 0x0023 }
        r6 = r3;	 Catch:{ ParseException -> 0x0023 }
        r3 = r6;	 Catch:{ ParseException -> 0x0023 }
        r4 = r6;	 Catch:{ ParseException -> 0x0023 }
        r2 = r4;	 Catch:{ ParseException -> 0x0023 }
        monitor-enter(r3);	 Catch:{ ParseException -> 0x0023 }
        r3 = mailDateFormat;	 Catch:{ all -> 0x001f }
        r4 = r1;	 Catch:{ all -> 0x001f }
        r3 = r3.parse(r4);	 Catch:{ all -> 0x001f }
        r4 = r2;	 Catch:{ all -> 0x001f }
        monitor-exit(r4);	 Catch:{ all -> 0x001f }
        r0 = r3;	 Catch:{ all -> 0x001f }
    L_0x001e:
        return r0;	 Catch:{ all -> 0x001f }
    L_0x001f:
        r3 = move-exception;	 Catch:{ all -> 0x001f }
        r4 = r2;	 Catch:{ all -> 0x001f }
        monitor-exit(r4);	 Catch:{ all -> 0x001f }
        throw r3;	 Catch:{ ParseException -> 0x0023 }
    L_0x0023:
        r3 = move-exception;
        r2 = r3;
        r3 = 0;
        r0 = r3;
        goto L_0x001e;
    L_0x0028:
        r3 = 0;
        r0 = r3;
        goto L_0x001e;
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.mail.internet.MimeMessage.getSentDate():java.util.Date");
    }

    public void setSentDate(Date date) throws MessagingException {
        Date date2 = date;
        if (date2 == null) {
            removeHeader("Date");
            return;
        }
        MailDateFormat mailDateFormat = mailDateFormat;
        MailDateFormat mailDateFormat2 = mailDateFormat;
        synchronized (mailDateFormat) {
            try {
                setHeader("Date", mailDateFormat.format(date2));
            } catch (Throwable th) {
                MailDateFormat mailDateFormat3 = mailDateFormat2;
            }
        }
    }

    public Date getReceivedDate() throws MessagingException {
        return null;
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
            return "text/plain";
        }
        return header;
    }

    public boolean isMimeType(String str) throws MessagingException {
        return MimeBodyPart.isMimeType(this, str);
    }

    public String getDisposition() throws MessagingException {
        return MimeBodyPart.getDisposition(this);
    }

    public void setDisposition(String str) throws MessagingException {
        MimeBodyPart.setDisposition(this, str);
    }

    public String getEncoding() throws MessagingException {
        return MimeBodyPart.getEncoding(this);
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

    public String getDescription() throws MessagingException {
        return MimeBodyPart.getDescription(this);
    }

    public void setDescription(String str) throws MessagingException {
        setDescription(str, null);
    }

    public void setDescription(String str, String str2) throws MessagingException {
        MimeBodyPart.setDescription(this, str, str2);
    }

    public String[] getContentLanguage() throws MessagingException {
        return MimeBodyPart.getContentLanguage(this);
    }

    public void setContentLanguage(String[] strArr) throws MessagingException {
        MimeBodyPart.setContentLanguage(this, strArr);
    }

    public String getMessageID() throws MessagingException {
        return getHeader("Message-ID", null);
    }

    public String getFileName() throws MessagingException {
        return MimeBodyPart.getFileName(this);
    }

    public void setFileName(String str) throws MessagingException {
        MimeBodyPart.setFileName(this, str);
    }

    private String getHeaderName(javax.mail.Message.RecipientType recipientType) throws MessagingException {
        String str;
        javax.mail.Message.RecipientType recipientType2 = recipientType;
        if (recipientType2 == javax.mail.Message.RecipientType.TO) {
            str = "To";
        } else if (recipientType2 == javax.mail.Message.RecipientType.CC) {
            str = "Cc";
        } else if (recipientType2 == javax.mail.Message.RecipientType.BCC) {
            str = "Bcc";
        } else if (recipientType2 == RecipientType.NEWSGROUPS) {
            str = "Newsgroups";
        } else {
            MessagingException messagingException = r6;
            MessagingException messagingException2 = new MessagingException("Invalid Recipient Type");
            throw messagingException;
        }
        return str;
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
            InputStream sharedByteArrayInputStream = new SharedByteArrayInputStream(this.content);
            return inputStream;
        }
        MessagingException messagingException = r6;
        MessagingException messagingException2 = new MessagingException("No content");
        throw messagingException;
    }

    public InputStream getRawInputStream() throws MessagingException {
        return getContentStream();
    }

    public synchronized DataHandler getDataHandler() throws MessagingException {
        DataHandler dataHandler;
        synchronized (this) {
            if (this.dh == null) {
                DataHandler dataHandler2 = r8;
                DataSource dataSource = r8;
                MimePartDataSource mimePartDataSource = new MimePartDataSource(this);
                DataHandler dataHandler3 = new DataHandler(dataSource);
                this.dh = dataHandler2;
            }
            dataHandler = this.dh;
        }
        return dataHandler;
    }

    public Object getContent() throws IOException, MessagingException {
        if (this.cachedContent != null) {
            return this.cachedContent;
        }
        try {
            Object content = getDataHandler().getContent();
            if (MimeBodyPart.cacheMultipart && (((content instanceof Multipart) || (content instanceof Message)) && !(this.content == null && this.contentStream == null))) {
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

    public synchronized void setDataHandler(DataHandler dataHandler) throws MessagingException {
        DataHandler dataHandler2 = dataHandler;
        synchronized (this) {
            this.dh = dataHandler2;
            this.cachedContent = null;
            MimeBodyPart.invalidateContentHeaders(this);
        }
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
        MimeBodyPart.setText(this, str, str2, "plain");
    }

    public void setText(String str, String str2, String str3) throws MessagingException {
        MimeBodyPart.setText(this, str, str2, str3);
    }

    public void setContent(Multipart multipart) throws MessagingException {
        Multipart multipart2 = multipart;
        DataHandler dataHandler = r7;
        DataHandler dataHandler2 = new DataHandler(multipart2, multipart2.getContentType());
        setDataHandler(dataHandler);
        multipart2.setParent(this);
    }

    public Message reply(boolean z) throws MessagingException {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        boolean z2 = z;
        Message createMimeMessage = createMimeMessage(this.session);
        String header = getHeader("Subject", null);
        if (header != null) {
            if (!header.regionMatches(true, 0, "Re: ", 0, 4)) {
                stringBuilder = r16;
                stringBuilder2 = new StringBuilder("Re: ");
                header = stringBuilder.append(header).toString();
            }
            createMimeMessage.setHeader("Subject", header);
        }
        Address[] replyTo = getReplyTo();
        createMimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, replyTo);
        if (z2) {
            Address[] eliminateDuplicates;
            Vector vector = r16;
            Vector vector2 = new Vector();
            Vector vector3 = vector;
            InternetAddress localAddress = InternetAddress.getLocalAddress(this.session);
            if (localAddress != null) {
                vector3.addElement(localAddress);
            }
            String str = null;
            if (this.session != null) {
                str = this.session.getProperty("mail.alternates");
            }
            if (str != null) {
                eliminateDuplicates = eliminateDuplicates(vector3, InternetAddress.parse(str, false));
            }
            String str2 = null;
            if (this.session != null) {
                str2 = this.session.getProperty("mail.replyallcc");
            }
            Object obj = (str2 == null || !str2.equalsIgnoreCase("true")) ? null : 1;
            Object obj2 = obj;
            eliminateDuplicates = eliminateDuplicates(vector3, replyTo);
            replyTo = eliminateDuplicates(vector3, getRecipients(javax.mail.Message.RecipientType.TO));
            if (replyTo != null && replyTo.length > 0) {
                if (obj2 != null) {
                    createMimeMessage.addRecipients(javax.mail.Message.RecipientType.CC, replyTo);
                } else {
                    createMimeMessage.addRecipients(javax.mail.Message.RecipientType.TO, replyTo);
                }
            }
            replyTo = eliminateDuplicates(vector3, getRecipients(javax.mail.Message.RecipientType.CC));
            if (replyTo != null && replyTo.length > 0) {
                createMimeMessage.addRecipients(javax.mail.Message.RecipientType.CC, replyTo);
            }
            replyTo = getRecipients(RecipientType.NEWSGROUPS);
            if (replyTo != null && replyTo.length > 0) {
                createMimeMessage.setRecipients(RecipientType.NEWSGROUPS, replyTo);
            }
        }
        String header2 = getHeader("Message-Id", null);
        if (header2 != null) {
            createMimeMessage.setHeader("In-Reply-To", header2);
        }
        String header3 = getHeader("References", " ");
        if (header3 == null) {
            header3 = getHeader("In-Reply-To", " ");
        }
        if (header2 != null) {
            if (header3 != null) {
                stringBuilder = r16;
                stringBuilder2 = new StringBuilder(String.valueOf(MimeUtility.unfold(header3)));
                header3 = stringBuilder.append(" ").append(header2).toString();
            } else {
                header3 = header2;
            }
        }
        if (header3 != null) {
            createMimeMessage.setHeader("References", MimeUtility.fold(12, header3));
        }
        try {
            setFlags(answeredFlag, true);
        } catch (MessagingException e) {
            MessagingException messagingException = e;
        }
        return createMimeMessage;
    }

    private Address[] eliminateDuplicates(Vector vector, Address[] addressArr) {
        Vector vector2 = vector;
        Address[] addressArr2 = addressArr;
        if (addressArr2 == null) {
            return null;
        }
        int i;
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < addressArr2.length; i4++) {
            Object i22 = null;
            for (i = 0; i < vector2.size(); i++) {
                if (((InternetAddress) vector2.elementAt(i)).equals(addressArr2[i4])) {
                    i22 = 1;
                    i3++;
                    addressArr2[i4] = null;
                    break;
                }
            }
            if (i22 == null) {
                vector2.addElement(addressArr2[i4]);
            }
        }
        if (i3 != 0) {
            Address[] addressArr3;
            if (addressArr2 instanceof InternetAddress[]) {
                addressArr3 = new InternetAddress[(addressArr2.length - i3)];
            } else {
                addressArr3 = new Address[(addressArr2.length - i3)];
            }
            i = 0;
            for (i22 = 0; i22 < addressArr2.length; i22++) {
                if (addressArr2[i22] != null) {
                    int i5 = i;
                    i++;
                    addressArr3[i5] = addressArr2[i22];
                }
            }
            addressArr2 = addressArr3;
        }
        return addressArr2;
    }

    public void writeTo(OutputStream outputStream) throws IOException, MessagingException {
        writeTo(outputStream, null);
    }

    public void writeTo(OutputStream outputStream, String[] strArr) throws IOException, MessagingException {
        OutputStream outputStream2 = outputStream;
        String[] strArr2 = strArr;
        if (!this.saved) {
            saveChanges();
        }
        if (this.modified) {
            MimeBodyPart.writeTo(this, outputStream2, strArr2);
            return;
        }
        Enumeration nonMatchingHeaderLines = getNonMatchingHeaderLines(strArr2);
        LineOutputStream lineOutputStream = r12;
        LineOutputStream lineOutputStream2 = new LineOutputStream(outputStream2);
        LineOutputStream lineOutputStream3 = lineOutputStream;
        while (nonMatchingHeaderLines.hasMoreElements()) {
            lineOutputStream3.writeln((String) nonMatchingHeaderLines.nextElement());
        }
        lineOutputStream3.writeln();
        if (this.content == null) {
            InputStream contentStream = getContentStream();
            byte[] bArr = new byte[8192];
            while (true) {
                int read = contentStream.read(bArr);
                int i = read;
                if (read <= 0) {
                    break;
                }
                outputStream2.write(bArr, 0, i);
            }
            contentStream.close();
            bArr = (byte[]) null;
        } else {
            outputStream2.write(this.content);
        }
        outputStream2.flush();
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

    public synchronized Flags getFlags() throws MessagingException {
        Flags flags;
        synchronized (this) {
            flags = (Flags) this.flags.clone();
        }
        return flags;
    }

    public synchronized boolean isSet(Flag flag) throws MessagingException {
        boolean contains;
        Flag flag2 = flag;
        synchronized (this) {
            contains = this.flags.contains(flag2);
        }
        return contains;
    }

    public synchronized void setFlags(Flags flags, boolean z) throws MessagingException {
        Flags flags2 = flags;
        boolean z2 = z;
        synchronized (this) {
            if (z2) {
                this.flags.add(flags2);
            } else {
                this.flags.remove(flags2);
            }
        }
    }

    public void saveChanges() throws MessagingException {
        this.modified = true;
        this.saved = true;
        updateHeaders();
    }

    /* Access modifiers changed, original: protected */
    public void updateMessageID() throws MessagingException {
        StringBuilder stringBuilder = r6;
        StringBuilder stringBuilder2 = new StringBuilder("<");
        setHeader("Message-ID", stringBuilder.append(UniqueValue.getUniqueMessageIDValue(this.session)).append(">").toString());
    }

    /* Access modifiers changed, original: protected */
    public void updateHeaders() throws MessagingException {
        MimeBodyPart.updateHeaders(this);
        setHeader("MIME-Version", "1.0");
        updateMessageID();
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

    /* Access modifiers changed, original: protected */
    public InternetHeaders createInternetHeaders(InputStream inputStream) throws MessagingException {
        InternetHeaders internetHeaders = r5;
        InternetHeaders internetHeaders2 = new InternetHeaders(inputStream);
        return internetHeaders;
    }

    /* Access modifiers changed, original: protected */
    public MimeMessage createMimeMessage(Session session) throws MessagingException {
        MimeMessage mimeMessage = r5;
        MimeMessage mimeMessage2 = new MimeMessage(session);
        return mimeMessage;
    }
}
