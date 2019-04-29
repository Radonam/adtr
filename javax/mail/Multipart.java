package javax.mail;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

public abstract class Multipart {
    protected String contentType = "multipart/mixed";
    protected Part parent;
    protected Vector parts;

    public abstract void writeTo(OutputStream outputStream) throws IOException, MessagingException;

    protected Multipart() {
        Vector vector = r4;
        Vector vector2 = new Vector();
        this.parts = vector;
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void setMultipartDataSource(MultipartDataSource multipartDataSource) throws MessagingException {
        MultipartDataSource multipartDataSource2 = multipartDataSource;
        synchronized (this) {
            this.contentType = multipartDataSource2.getContentType();
            int count = multipartDataSource2.getCount();
            for (int i = 0; i < count; i++) {
                addBodyPart(multipartDataSource2.getBodyPart(i));
            }
        }
    }

    public String getContentType() {
        return this.contentType;
    }

    public synchronized int getCount() throws MessagingException {
        int i;
        synchronized (this) {
            if (this.parts == null) {
                i = 0;
            } else {
                i = this.parts.size();
            }
        }
        return i;
    }

    public synchronized BodyPart getBodyPart(int i) throws MessagingException {
        BodyPart bodyPart;
        int i2 = i;
        synchronized (this) {
            if (this.parts == null) {
                IndexOutOfBoundsException indexOutOfBoundsException = r6;
                IndexOutOfBoundsException indexOutOfBoundsException2 = new IndexOutOfBoundsException("No such BodyPart");
                throw indexOutOfBoundsException;
            }
            bodyPart = (BodyPart) this.parts.elementAt(i2);
        }
        return bodyPart;
    }

    public synchronized boolean removeBodyPart(BodyPart bodyPart) throws MessagingException {
        boolean z;
        BodyPart bodyPart2 = bodyPart;
        synchronized (this) {
            if (this.parts == null) {
                MessagingException messagingException = r7;
                MessagingException messagingException2 = new MessagingException("No such body part");
                throw messagingException;
            }
            boolean removeElement = this.parts.removeElement(bodyPart2);
            bodyPart2.setParent(null);
            z = removeElement;
        }
        return z;
    }

    public synchronized void removeBodyPart(int i) throws MessagingException {
        int i2 = i;
        synchronized (this) {
            if (this.parts == null) {
                IndexOutOfBoundsException indexOutOfBoundsException = r7;
                IndexOutOfBoundsException indexOutOfBoundsException2 = new IndexOutOfBoundsException("No such BodyPart");
                throw indexOutOfBoundsException;
            }
            BodyPart bodyPart = (BodyPart) this.parts.elementAt(i2);
            this.parts.removeElementAt(i2);
            bodyPart.setParent(null);
        }
    }

    public synchronized void addBodyPart(BodyPart bodyPart) throws MessagingException {
        BodyPart bodyPart2 = bodyPart;
        synchronized (this) {
            if (this.parts == null) {
                Vector vector = r6;
                Vector vector2 = new Vector();
                this.parts = vector;
            }
            this.parts.addElement(bodyPart2);
            bodyPart2.setParent(this);
        }
    }

    public synchronized void addBodyPart(BodyPart bodyPart, int i) throws MessagingException {
        BodyPart bodyPart2 = bodyPart;
        int i2 = i;
        synchronized (this) {
            if (this.parts == null) {
                Vector vector = r7;
                Vector vector2 = new Vector();
                this.parts = vector;
            }
            this.parts.insertElementAt(bodyPart2, i2);
            bodyPart2.setParent(this);
        }
    }

    public synchronized Part getParent() {
        Part part;
        synchronized (this) {
            part = this.parent;
        }
        return part;
    }

    public synchronized void setParent(Part part) {
        Part part2 = part;
        synchronized (this) {
            this.parent = part2;
        }
    }
}
