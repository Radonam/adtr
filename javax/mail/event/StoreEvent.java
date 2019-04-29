package javax.mail.event;

import javax.mail.Store;

public class StoreEvent extends MailEvent {
    public static final int ALERT = 1;
    public static final int NOTICE = 2;
    private static final long serialVersionUID = 1938704919992515330L;
    protected String message;
    protected int type;

    public StoreEvent(Store store, int i, String str) {
        int i2 = i;
        String str2 = str;
        super(store);
        this.type = i2;
        this.message = str2;
    }

    public int getMessageType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    public void dispatch(Object obj) {
        ((StoreListener) obj).notification(this);
    }
}
