package javax.mail;

public class MessageContext {
    private Part part;

    public MessageContext(Part part) {
        this.part = part;
    }

    public Part getPart() {
        return this.part;
    }

    public Message getMessage() {
        try {
            return getMessage(this.part);
        } catch (MessagingException e) {
            MessagingException messagingException = e;
            return null;
        }
    }

    private static Message getMessage(Part part) throws MessagingException {
        Part part2 = part;
        while (part2 != null) {
            if (part2 instanceof Message) {
                return (Message) part2;
            }
            Multipart parent = ((BodyPart) part2).getParent();
            if (parent == null) {
                return null;
            }
            part2 = parent.getParent();
        }
        return null;
    }

    public Session getSession() {
        Message message = getMessage();
        return message != null ? message.session : null;
    }
}
