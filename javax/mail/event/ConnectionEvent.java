package javax.mail.event;

public class ConnectionEvent extends MailEvent {
    public static final int CLOSED = 3;
    public static final int DISCONNECTED = 2;
    public static final int OPENED = 1;
    private static final long serialVersionUID = -1855480171284792957L;
    protected int type;

    public ConnectionEvent(Object obj, int i) {
        int i2 = i;
        super(obj);
        this.type = i2;
    }

    public int getType() {
        return this.type;
    }

    public void dispatch(Object obj) {
        Object obj2 = obj;
        if (this.type == 1) {
            ((ConnectionListener) obj2).opened(this);
        } else if (this.type == 2) {
            ((ConnectionListener) obj2).disconnected(this);
        } else if (this.type == 3) {
            ((ConnectionListener) obj2).closed(this);
        }
    }
}
