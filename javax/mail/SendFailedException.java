package javax.mail;

public class SendFailedException extends MessagingException {
    private static final long serialVersionUID = -6457531621682372913L;
    protected transient Address[] invalid;
    protected transient Address[] validSent;
    protected transient Address[] validUnsent;

    public SendFailedException() {
    }

    public SendFailedException(String str) {
        super(str);
    }

    public SendFailedException(String str, Exception exception) {
        super(str, exception);
    }

    public SendFailedException(String str, Exception exception, Address[] addressArr, Address[] addressArr2, Address[] addressArr3) {
        Address[] addressArr4 = addressArr;
        Address[] addressArr5 = addressArr2;
        Address[] addressArr6 = addressArr3;
        super(str, exception);
        this.validSent = addressArr4;
        this.validUnsent = addressArr5;
        this.invalid = addressArr6;
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
}
