package javax.mail.event;

import javax.mail.Message;

public class MessageChangedEvent extends MailEvent {
    public static final int ENVELOPE_CHANGED = 2;
    public static final int FLAGS_CHANGED = 1;
    private static final long serialVersionUID = -4974972972105535108L;
    protected transient Message msg;
    protected int type;

    public MessageChangedEvent(Object obj, int i, Message message) {
        int i2 = i;
        Message message2 = message;
        super(obj);
        this.msg = message2;
        this.type = i2;
    }

    public int getMessageChangeType() {
        return this.type;
    }

    public Message getMessage() {
        return this.msg;
    }

    public void dispatch(Object obj) {
        ((MessageChangedListener) obj).messageChanged(this);
    }
}
