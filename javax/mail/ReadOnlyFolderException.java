package javax.mail;

public class ReadOnlyFolderException extends MessagingException {
    private static final long serialVersionUID = 5711829372799039325L;
    private transient Folder folder;

    public ReadOnlyFolderException(Folder folder) {
        this(folder, null);
    }

    public ReadOnlyFolderException(Folder folder, String str) {
        Folder folder2 = folder;
        super(str);
        this.folder = folder2;
    }

    public Folder getFolder() {
        return this.folder;
    }
}
