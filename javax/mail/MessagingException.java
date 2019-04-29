package javax.mail;

public class MessagingException extends Exception {
    private static final long serialVersionUID = -7569192289819959253L;
    private Exception next;

    public MessagingException() {
        Throwable initCause = initCause(null);
    }

    public MessagingException(String str) {
        super(str);
        Throwable initCause = initCause(null);
    }

    public MessagingException(String str, Exception exception) {
        Exception exception2 = exception;
        super(str);
        this.next = exception2;
        Throwable initCause = initCause(null);
    }

    public synchronized Exception getNextException() {
        Exception exception;
        synchronized (this) {
            exception = this.next;
        }
        return exception;
    }

    public synchronized Throwable getCause() {
        Throwable th;
        synchronized (this) {
            th = this.next;
        }
        return th;
    }

    public synchronized boolean setNextException(Exception exception) {
        boolean z;
        Exception exception2 = exception;
        synchronized (this) {
            Exception exception3;
            Exception exception4 = this;
            while (true) {
                exception3 = exception4;
                if ((exception3 instanceof MessagingException) && ((MessagingException) exception3).next != null) {
                    exception4 = ((MessagingException) exception3).next;
                }
            }
            if (exception3 instanceof MessagingException) {
                ((MessagingException) exception3).next = exception2;
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    public synchronized String toString() {
        String str;
        synchronized (this) {
            String exception = super.toString();
            Exception exception2 = this.next;
            if (exception2 == null) {
                str = exception;
            } else {
                StringBuffer stringBuffer = r6;
                StringBuffer stringBuffer2 = stringBuffer2;
                stringBuffer2 = new StringBuffer(exception == null ? "" : exception);
                StringBuffer stringBuffer3 = stringBuffer;
                while (exception2 != null) {
                    stringBuffer = stringBuffer3.append(";\n  nested exception is:\n\t");
                    if (exception2 instanceof MessagingException) {
                        MessagingException messagingException = (MessagingException) exception2;
                        stringBuffer = stringBuffer3.append(messagingException.superToString());
                        exception2 = messagingException.next;
                    } else {
                        stringBuffer = stringBuffer3.append(exception2.toString());
                        exception2 = null;
                    }
                }
                str = stringBuffer3.toString();
            }
        }
        return str;
    }

    private final String superToString() {
        return super.toString();
    }
}
