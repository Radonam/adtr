package javax.mail;

public class FolderNotFoundException extends MessagingException {
    private static final long serialVersionUID = 472612108891249403L;
    private transient Folder folder;

    public FolderNotFoundException() {
    }

    public FolderNotFoundException(Folder folder) {
        this.folder = folder;
    }

    public FolderNotFoundException(Folder folder, String str) {
        Folder folder2 = folder;
        super(str);
        this.folder = folder2;
    }

    public FolderNotFoundException(String str, Folder folder) {
        Folder folder2 = folder;
        super(str);
        this.folder = folder2;
    }

    public Folder getFolder() {
        return this.folder;
    }
}
