package javax.mail;

import java.util.Vector;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.MailEvent;
import javax.mail.event.StoreEvent;
import javax.mail.event.StoreListener;

public abstract class Store extends Service {
    private volatile Vector folderListeners = null;
    private volatile Vector storeListeners = null;

    public abstract Folder getDefaultFolder() throws MessagingException;

    public abstract Folder getFolder(String str) throws MessagingException;

    public abstract Folder getFolder(URLName uRLName) throws MessagingException;

    protected Store(Session session, URLName uRLName) {
        super(session, uRLName);
    }

    public Folder[] getPersonalNamespaces() throws MessagingException {
        Folder[] folderArr = new Folder[1];
        Folder[] folderArr2 = folderArr;
        folderArr[0] = getDefaultFolder();
        return folderArr2;
    }

    public Folder[] getUserNamespaces(String str) throws MessagingException {
        String str2 = str;
        return new Folder[0];
    }

    public Folder[] getSharedNamespaces() throws MessagingException {
        return new Folder[0];
    }

    public synchronized void addStoreListener(StoreListener storeListener) {
        StoreListener storeListener2 = storeListener;
        synchronized (this) {
            if (this.storeListeners == null) {
                Vector vector = r6;
                Vector vector2 = new Vector();
                this.storeListeners = vector;
            }
            this.storeListeners.addElement(storeListener2);
        }
    }

    public synchronized void removeStoreListener(StoreListener storeListener) {
        StoreListener storeListener2 = storeListener;
        synchronized (this) {
            if (this.storeListeners != null) {
                boolean removeElement = this.storeListeners.removeElement(storeListener2);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void notifyStoreListeners(int i, String str) {
        int i2 = i;
        String str2 = str;
        if (this.storeListeners != null) {
            MailEvent mailEvent = r9;
            MailEvent storeEvent = new StoreEvent(this, i2, str2);
            queueEvent(mailEvent, this.storeListeners);
        }
    }

    public synchronized void addFolderListener(FolderListener folderListener) {
        FolderListener folderListener2 = folderListener;
        synchronized (this) {
            if (this.folderListeners == null) {
                Vector vector = r6;
                Vector vector2 = new Vector();
                this.folderListeners = vector;
            }
            this.folderListeners.addElement(folderListener2);
        }
    }

    public synchronized void removeFolderListener(FolderListener folderListener) {
        FolderListener folderListener2 = folderListener;
        synchronized (this) {
            if (this.folderListeners != null) {
                boolean removeElement = this.folderListeners.removeElement(folderListener2);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void notifyFolderListeners(int i, Folder folder) {
        int i2 = i;
        Folder folder2 = folder;
        if (this.folderListeners != null) {
            MailEvent mailEvent = r9;
            MailEvent folderEvent = new FolderEvent(this, folder2, i2);
            queueEvent(mailEvent, this.folderListeners);
        }
    }

    /* Access modifiers changed, original: protected */
    public void notifyFolderRenamedListeners(Folder folder, Folder folder2) {
        Folder folder3 = folder;
        Folder folder4 = folder2;
        if (this.folderListeners != null) {
            MailEvent mailEvent = r10;
            MailEvent folderEvent = new FolderEvent(this, folder3, folder4, 3);
            queueEvent(mailEvent, this.folderListeners);
        }
    }
}
