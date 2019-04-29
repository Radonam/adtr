package javax.mail;

public class FolderClosedException extends MessagingException {
    private static final long serialVersionUID = 1687879213433302315L;
    private transient Folder folder;

    public FolderClosedException(Folder folder) {
        this(folder, null);
    }

    public FolderClosedException(Folder folder, String str) {
        Folder folder2 = folder;
        super(str);
        this.folder = folder2;
    }

    public Folder getFolder() {
        return this.folder;
    }
}
