package javax.mail.event;

import javax.mail.Folder;
import javax.mail.Message;

public class MessageCountEvent extends MailEvent {
    public static final int ADDED = 1;
    public static final int REMOVED = 2;
    private static final long serialVersionUID = -7447022340837897369L;
    protected transient Message[] msgs;
    protected boolean removed;
    protected int type;

    public MessageCountEvent(Folder folder, int i, boolean z, Message[] messageArr) {
        int i2 = i;
        boolean z2 = z;
        Message[] messageArr2 = messageArr;
        super(folder);
        this.type = i2;
        this.removed = z2;
        this.msgs = messageArr2;
    }

    public int getType() {
        return this.type;
    }

    public boolean isRemoved() {
        return this.removed;
    }

    public Message[] getMessages() {
        return this.msgs;
    }

    public void dispatch(Object obj) {
        Object obj2 = obj;
        if (this.type == 1) {
            ((MessageCountListener) obj2).messagesAdded(this);
        } else {
            ((MessageCountListener) obj2).messagesRemoved(this);
        }
    }
}
