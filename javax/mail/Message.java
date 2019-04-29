package javax.mail;

import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Date;
import javax.mail.Flags.Flag;
import javax.mail.search.SearchTerm;

public abstract class Message implements Part {
    protected boolean expunged = false;
    protected Folder folder = null;
    protected int msgnum = 0;
    protected Session session = null;

    public static class RecipientType implements Serializable {
        public static final RecipientType BCC;
        public static final RecipientType CC;
        public static final RecipientType TO;
        private static final long serialVersionUID = -7479791750606340008L;
        protected String type;

        static {
            RecipientType recipientType = r3;
            RecipientType recipientType2 = new RecipientType("To");
            TO = recipientType;
            recipientType = r3;
            recipientType2 = new RecipientType("Cc");
            CC = recipientType;
            recipientType = r3;
            recipientType2 = new RecipientType("Bcc");
            BCC = recipientType;
        }

        protected RecipientType(String str) {
            this.type = str;
        }

        /* Access modifiers changed, original: protected */
        public Object readResolve() throws ObjectStreamException {
            if (this.type.equals("To")) {
                return TO;
            }
            if (this.type.equals("Cc")) {
                return CC;
            }
            if (this.type.equals("Bcc")) {
                return BCC;
            }
            InvalidObjectException invalidObjectException = r6;
            StringBuilder stringBuilder = r6;
            StringBuilder stringBuilder2 = new StringBuilder("Attempt to resolve unknown RecipientType: ");
            InvalidObjectException invalidObjectException2 = new InvalidObjectException(stringBuilder.append(this.type).toString());
            throw invalidObjectException;
        }

        public String toString() {
            return this.type;
        }
    }

    public abstract void addFrom(Address[] addressArr) throws MessagingException;

    public abstract void addRecipients(RecipientType recipientType, Address[] addressArr) throws MessagingException;

    public abstract Flags getFlags() throws MessagingException;

    public abstract Address[] getFrom() throws MessagingException;

    public abstract Date getReceivedDate() throws MessagingException;

    public abstract Address[] getRecipients(RecipientType recipientType) throws MessagingException;

    public abstract Date getSentDate() throws MessagingException;

    public abstract String getSubject() throws MessagingException;

    public abstract Message reply(boolean z) throws MessagingException;

    public abstract void saveChanges() throws MessagingException;

    public abstract void setFlags(Flags flags, boolean z) throws MessagingException;

    public abstract void setFrom() throws MessagingException;

    public abstract void setFrom(Address address) throws MessagingException;

    public abstract void setRecipients(RecipientType recipientType, Address[] addressArr) throws MessagingException;

    public abstract void setSentDate(Date date) throws MessagingException;

    public abstract void setSubject(String str) throws MessagingException;

    protected Message() {
    }

    protected Message(Folder folder, int i) {
        Folder folder2 = folder;
        int i2 = i;
        this.folder = folder2;
        this.msgnum = i2;
        this.session = folder2.store.session;
    }

    protected Message(Session session) {
        Session session2 = session;
        this.session = session2;
    }

    public Address[] getAllRecipients() throws MessagingException {
        Object recipients = getRecipients(RecipientType.TO);
        Object recipients2 = getRecipients(RecipientType.CC);
        Object recipients3 = getRecipients(RecipientType.BCC);
        if (recipients2 == null && recipients3 == null) {
            return recipients;
        }
        Object obj = new Address[(((recipients != null ? recipients.length : 0) + (recipients2 != null ? recipients2.length : 0)) + (recipients3 != null ? recipients3.length : 0))];
        int i = 0;
        if (recipients != null) {
            System.arraycopy(recipients, 0, obj, i, recipients.length);
            i += recipients.length;
        }
        if (recipients2 != null) {
            System.arraycopy(recipients2, 0, obj, i, recipients2.length);
            i += recipients2.length;
        }
        if (recipients3 != null) {
            System.arraycopy(recipients3, 0, obj, i, recipients3.length);
            i += recipients3.length;
        }
        return obj;
    }

    public void setRecipient(RecipientType recipientType, Address address) throws MessagingException {
        setRecipients(recipientType, new Address[]{address});
    }

    public void addRecipient(RecipientType recipientType, Address address) throws MessagingException {
        addRecipients(recipientType, new Address[]{address});
    }

    public Address[] getReplyTo() throws MessagingException {
        return getFrom();
    }

    public void setReplyTo(Address[] addressArr) throws MessagingException {
        Address[] addressArr2 = addressArr;
        MethodNotSupportedException methodNotSupportedException = r5;
        MethodNotSupportedException methodNotSupportedException2 = new MethodNotSupportedException("setReplyTo not supported");
        throw methodNotSupportedException;
    }

    public boolean isSet(Flag flag) throws MessagingException {
        return getFlags().contains(flag);
    }

    public void setFlag(Flag flag, boolean z) throws MessagingException {
        boolean z2 = z;
        Flags flags = r7;
        Flags flags2 = new Flags(flag);
        setFlags(flags, z2);
    }

    public int getMessageNumber() {
        return this.msgnum;
    }

    /* Access modifiers changed, original: protected */
    public void setMessageNumber(int i) {
        this.msgnum = i;
    }

    public Folder getFolder() {
        return this.folder;
    }

    public boolean isExpunged() {
        return this.expunged;
    }

    /* Access modifiers changed, original: protected */
    public void setExpunged(boolean z) {
        this.expunged = z;
    }

    public boolean match(SearchTerm searchTerm) throws MessagingException {
        return searchTerm.match(this);
    }
}
