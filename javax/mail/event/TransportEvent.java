package javax.mail.event;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Transport;

public class TransportEvent extends MailEvent {
    public static final int MESSAGE_DELIVERED = 1;
    public static final int MESSAGE_NOT_DELIVERED = 2;
    public static final int MESSAGE_PARTIALLY_DELIVERED = 3;
    private static final long serialVersionUID = -4729852364684273073L;
    protected transient Address[] invalid;
    protected transient Message msg;
    protected int type;
    protected transient Address[] validSent;
    protected transient Address[] validUnsent;

    public TransportEvent(Transport transport, int i, Address[] addressArr, Address[] addressArr2, Address[] addressArr3, Message message) {
        int i2 = i;
        Address[] addressArr4 = addressArr;
        Address[] addressArr5 = addressArr2;
        Address[] addressArr6 = addressArr3;
        Message message2 = message;
        super(transport);
        this.type = i2;
        this.validSent = addressArr4;
        this.validUnsent = addressArr5;
        this.invalid = addressArr6;
        this.msg = message2;
    }

    public int getType() {
        return this.type;
    }

    public Address[] getValidSentAddresses() {
        return this.validSent;
    }

    public Address[] getValidUnsentAddresses() {
        return this.validUnsent;
    }

    public Address[] getInvalidAddresses() {
        return this.invalid;
    }

    public Message getMessage() {
        return this.msg;
    }

    public void dispatch(Object obj) {
        Object obj2 = obj;
        if (this.type == 1) {
            ((TransportListener) obj2).messageDelivered(this);
        } else if (this.type == 2) {
            ((TransportListener) obj2).messageNotDelivered(this);
        } else {
            ((TransportListener) obj2).messagePartiallyDelivered(this);
        }
    }
}
