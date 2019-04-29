package javax.mail.event;

import javax.mail.Folder;

public class FolderEvent extends MailEvent {
    public static final int CREATED = 1;
    public static final int DELETED = 2;
    public static final int RENAMED = 3;
    private static final long serialVersionUID = 5278131310563694307L;
    protected transient Folder folder;
    protected transient Folder newFolder;
    protected int type;

    public FolderEvent(Object obj, Folder folder, int i) {
        Folder folder2 = folder;
        this(obj, folder2, folder2, i);
    }

    public FolderEvent(Object obj, Folder folder, Folder folder2, int i) {
        Folder folder3 = folder;
        Folder folder4 = folder2;
        int i2 = i;
        super(obj);
        this.folder = folder3;
        this.newFolder = folder4;
        this.type = i2;
    }

    public int getType() {
        return this.type;
    }

    public Folder getFolder() {
        return this.folder;
    }

    public Folder getNewFolder() {
        return this.newFolder;
    }

    public void dispatch(Object obj) {
        Object obj2 = obj;
        if (this.type == 1) {
            ((FolderListener) obj2).folderCreated(this);
        } else if (this.type == 2) {
            ((FolderListener) obj2).folderDeleted(this);
        } else if (this.type == 3) {
            ((FolderListener) obj2).folderRenamed(this);
        }
    }
}
