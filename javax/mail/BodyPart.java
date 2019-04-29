package javax.mail;

public abstract class BodyPart implements Part {
    protected Multipart parent;

    public BodyPart() {
    }

    public Multipart getParent() {
        return this.parent;
    }

    /* Access modifiers changed, original: 0000 */
    public void setParent(Multipart multipart) {
        this.parent = multipart;
    }
}
