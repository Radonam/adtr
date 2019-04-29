package javax.mail;

import java.util.Vector;
import javax.mail.Flags.Flag;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.FolderEvent;
import javax.mail.event.FolderListener;
import javax.mail.event.MailEvent;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.search.SearchTerm;

public abstract class Folder {
    public static final int HOLDS_FOLDERS = 2;
    public static final int HOLDS_MESSAGES = 1;
    public static final int READ_ONLY = 1;
    public static final int READ_WRITE = 2;
    private volatile Vector connectionListeners = null;
    private volatile Vector folderListeners = null;
    private volatile Vector messageChangedListeners = null;
    private volatile Vector messageCountListeners = null;
    protected int mode = -1;
    private EventQueue q;
    private Object qLock;
    protected Store store;

    static class TerminatorEvent extends MailEvent {
        private static final long serialVersionUID = 3765761925441296565L;

        TerminatorEvent() {
            Object obj = r4;
            Object obj2 = new Object();
            super(obj);
        }

        public void dispatch(Object obj) {
            Object obj2 = obj;
            Thread.currentThread().interrupt();
        }
    }

    public abstract void appendMessages(Message[] messageArr) throws MessagingException;

    public abstract void close(boolean z) throws MessagingException;

    public abstract boolean create(int i) throws MessagingException;

    public abstract boolean delete(boolean z) throws MessagingException;

    public abstract boolean exists() throws MessagingException;

    public abstract Message[] expunge() throws MessagingException;

    public abstract Folder getFolder(String str) throws MessagingException;

    public abstract String getFullName();

    public abstract Message getMessage(int i) throws MessagingException;

    public abstract int getMessageCount() throws MessagingException;

    public abstract String getName();

    public abstract Folder getParent() throws MessagingException;

    public abstract Flags getPermanentFlags();

    public abstract char getSeparator() throws MessagingException;

    public abstract int getType() throws MessagingException;

    public abstract boolean hasNewMessages() throws MessagingException;

    public abstract boolean isOpen();

    public abstract Folder[] list(String str) throws MessagingException;

    public abstract void open(int i) throws MessagingException;

    public abstract boolean renameTo(Folder folder) throws MessagingException;

    protected Folder(Store store) {
        Store store2 = store;
        Object obj = r5;
        Object obj2 = new Object();
        this.qLock = obj;
        this.store = store2;
    }

    public URLName getURLName() throws MessagingException {
        URLName uRLName = getStore().getURLName();
        String fullName = getFullName();
        StringBuffer stringBuffer = r13;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        char separator = getSeparator();
        if (fullName != null) {
            stringBuffer = stringBuffer3.append(fullName);
        }
        URLName uRLName2 = r13;
        URLName uRLName3 = new URLName(uRLName.getProtocol(), uRLName.getHost(), uRLName.getPort(), stringBuffer3.toString(), uRLName.getUsername(), null);
        return uRLName2;
    }

    public Store getStore() {
        return this.store;
    }

    public Folder[] listSubscribed(String str) throws MessagingException {
        return list(str);
    }

    public Folder[] list() throws MessagingException {
        return list("%");
    }

    public Folder[] listSubscribed() throws MessagingException {
        return listSubscribed("%");
    }

    public boolean isSubscribed() {
        return true;
    }

    public void setSubscribed(boolean z) throws MessagingException {
        boolean z2 = z;
        MethodNotSupportedException methodNotSupportedException = r4;
        MethodNotSupportedException methodNotSupportedException2 = new MethodNotSupportedException();
        throw methodNotSupportedException;
    }

    public int getMode() {
        if (isOpen()) {
            return this.mode;
        }
        IllegalStateException illegalStateException = r4;
        IllegalStateException illegalStateException2 = new IllegalStateException("Folder not open");
        throw illegalStateException;
    }

    public synchronized int getNewMessageCount() throws MessagingException {
        int i;
        synchronized (this) {
            if (isOpen()) {
                int i2 = 0;
                int messageCount = getMessageCount();
                for (int i3 = 1; i3 <= messageCount; i3++) {
                    try {
                        if (getMessage(i3).isSet(Flag.RECENT)) {
                            i2++;
                        }
                    } catch (MessageRemovedException e) {
                        MessageRemovedException messageRemovedException = e;
                    }
                }
                i = i2;
            } else {
                i = -1;
            }
        }
        return i;
    }

    public synchronized int getUnreadMessageCount() throws MessagingException {
        int i;
        synchronized (this) {
            if (isOpen()) {
                int i2 = 0;
                int messageCount = getMessageCount();
                for (int i3 = 1; i3 <= messageCount; i3++) {
                    try {
                        if (!getMessage(i3).isSet(Flag.SEEN)) {
                            i2++;
                        }
                    } catch (MessageRemovedException e) {
                        MessageRemovedException messageRemovedException = e;
                    }
                }
                i = i2;
            } else {
                i = -1;
            }
        }
        return i;
    }

    public synchronized int getDeletedMessageCount() throws MessagingException {
        int i;
        synchronized (this) {
            if (isOpen()) {
                int i2 = 0;
                int messageCount = getMessageCount();
                for (int i3 = 1; i3 <= messageCount; i3++) {
                    try {
                        if (getMessage(i3).isSet(Flag.DELETED)) {
                            i2++;
                        }
                    } catch (MessageRemovedException e) {
                        MessageRemovedException messageRemovedException = e;
                    }
                }
                i = i2;
            } else {
                i = -1;
            }
        }
        return i;
    }

    public synchronized Message[] getMessages(int i, int i2) throws MessagingException {
        Message[] messageArr;
        int i3 = i;
        int i4 = i2;
        synchronized (this) {
            Message[] messageArr2 = new Message[((i4 - i3) + 1)];
            for (int i5 = i3; i5 <= i4; i5++) {
                messageArr2[i5 - i3] = getMessage(i5);
            }
            messageArr = messageArr2;
        }
        return messageArr;
    }

    public synchronized Message[] getMessages(int[] iArr) throws MessagingException {
        Message[] messageArr;
        int[] iArr2 = iArr;
        synchronized (this) {
            int length = iArr2.length;
            Message[] messageArr2 = new Message[length];
            for (int i = 0; i < length; i++) {
                messageArr2[i] = getMessage(iArr2[i]);
            }
            messageArr = messageArr2;
        }
        return messageArr;
    }

    public synchronized Message[] getMessages() throws MessagingException {
        Message[] messageArr;
        synchronized (this) {
            if (isOpen()) {
                int messageCount = getMessageCount();
                Message[] messageArr2 = new Message[messageCount];
                for (int i = 1; i <= messageCount; i++) {
                    messageArr2[i - 1] = getMessage(i);
                }
                messageArr = messageArr2;
            } else {
                IllegalStateException illegalStateException = r9;
                IllegalStateException illegalStateException2 = new IllegalStateException("Folder not open");
                throw illegalStateException;
            }
        }
        return messageArr;
    }

    public void fetch(Message[] messageArr, FetchProfile fetchProfile) throws MessagingException {
    }

    public synchronized void setFlags(Message[] messageArr, Flags flags, boolean z) throws MessagingException {
        Message[] messageArr2 = messageArr;
        Flags flags2 = flags;
        boolean z2 = z;
        synchronized (this) {
            for (Message flags3 : messageArr2) {
                try {
                    flags3.setFlags(flags2, z2);
                } catch (MessageRemovedException e) {
                    MessageRemovedException messageRemovedException = e;
                }
            }
        }
    }

    public synchronized void setFlags(int i, int i2, Flags flags, boolean z) throws MessagingException {
        int i3 = i;
        int i4 = i2;
        Flags flags2 = flags;
        boolean z2 = z;
        synchronized (this) {
            for (int i5 = i3; i5 <= i4; i5++) {
                try {
                    getMessage(i5).setFlags(flags2, z2);
                } catch (MessageRemovedException e) {
                    MessageRemovedException messageRemovedException = e;
                }
            }
        }
    }

    public synchronized void setFlags(int[] iArr, Flags flags, boolean z) throws MessagingException {
        int[] iArr2 = iArr;
        Flags flags2 = flags;
        boolean z2 = z;
        synchronized (this) {
            for (int message : iArr2) {
                try {
                    getMessage(message).setFlags(flags2, z2);
                } catch (MessageRemovedException e) {
                    MessageRemovedException messageRemovedException = e;
                }
            }
        }
    }

    public void copyMessages(Message[] messageArr, Folder folder) throws MessagingException {
        Message[] messageArr2 = messageArr;
        Folder folder2 = folder;
        if (folder2.exists()) {
            folder2.appendMessages(messageArr2);
            return;
        }
        FolderNotFoundException folderNotFoundException = r8;
        StringBuilder stringBuilder = r8;
        StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(folder2.getFullName()));
        FolderNotFoundException folderNotFoundException2 = new FolderNotFoundException(stringBuilder.append(" does not exist").toString(), folder2);
        throw folderNotFoundException;
    }

    public Message[] search(SearchTerm searchTerm) throws MessagingException {
        return search(searchTerm, getMessages());
    }

    public Message[] search(SearchTerm searchTerm, Message[] messageArr) throws MessagingException {
        SearchTerm searchTerm2 = searchTerm;
        Message[] messageArr2 = messageArr;
        Vector vector = r9;
        Vector vector2 = new Vector();
        Vector vector3 = vector;
        for (int i = 0; i < messageArr2.length; i++) {
            try {
                if (messageArr2[i].match(searchTerm2)) {
                    vector3.addElement(messageArr2[i]);
                }
            } catch (MessageRemovedException e) {
                MessageRemovedException messageRemovedException = e;
            }
        }
        Message[] messageArr3 = new Message[vector3.size()];
        vector3.copyInto(messageArr3);
        return messageArr3;
    }

    public synchronized void addConnectionListener(ConnectionListener connectionListener) {
        ConnectionListener connectionListener2 = connectionListener;
        synchronized (this) {
            if (this.connectionListeners == null) {
                Vector vector = r6;
                Vector vector2 = new Vector();
                this.connectionListeners = vector;
            }
            this.connectionListeners.addElement(connectionListener2);
        }
    }

    public synchronized void removeConnectionListener(ConnectionListener connectionListener) {
        ConnectionListener connectionListener2 = connectionListener;
        synchronized (this) {
            if (this.connectionListeners != null) {
                boolean removeElement = this.connectionListeners.removeElement(connectionListener2);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void notifyConnectionListeners(int i) {
        int i2 = i;
        if (this.connectionListeners != null) {
            MailEvent mailEvent = r7;
            MailEvent connectionEvent = new ConnectionEvent(this, i2);
            queueEvent(mailEvent, this.connectionListeners);
        }
        if (i2 == 3) {
            terminateQueue();
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
    public void notifyFolderListeners(int i) {
        int i2 = i;
        if (this.folderListeners != null) {
            MailEvent mailEvent = r8;
            MailEvent folderEvent = new FolderEvent(this, this, i2);
            queueEvent(mailEvent, this.folderListeners);
        }
        this.store.notifyFolderListeners(i2, this);
    }

    /* Access modifiers changed, original: protected */
    public void notifyFolderRenamedListeners(Folder folder) {
        Folder folder2 = folder;
        if (this.folderListeners != null) {
            MailEvent mailEvent = r9;
            MailEvent folderEvent = new FolderEvent(this, this, folder2, 3);
            queueEvent(mailEvent, this.folderListeners);
        }
        this.store.notifyFolderRenamedListeners(this, folder2);
    }

    public synchronized void addMessageCountListener(MessageCountListener messageCountListener) {
        MessageCountListener messageCountListener2 = messageCountListener;
        synchronized (this) {
            if (this.messageCountListeners == null) {
                Vector vector = r6;
                Vector vector2 = new Vector();
                this.messageCountListeners = vector;
            }
            this.messageCountListeners.addElement(messageCountListener2);
        }
    }

    public synchronized void removeMessageCountListener(MessageCountListener messageCountListener) {
        MessageCountListener messageCountListener2 = messageCountListener;
        synchronized (this) {
            if (this.messageCountListeners != null) {
                boolean removeElement = this.messageCountListeners.removeElement(messageCountListener2);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void notifyMessageAddedListeners(Message[] messageArr) {
        Message[] messageArr2 = messageArr;
        if (this.messageCountListeners != null) {
            MailEvent mailEvent = r9;
            MailEvent messageCountEvent = new MessageCountEvent(this, 1, false, messageArr2);
            queueEvent(mailEvent, this.messageCountListeners);
        }
    }

    /* Access modifiers changed, original: protected */
    public void notifyMessageRemovedListeners(boolean z, Message[] messageArr) {
        boolean z2 = z;
        Message[] messageArr2 = messageArr;
        if (this.messageCountListeners != null) {
            MailEvent mailEvent = r10;
            MailEvent messageCountEvent = new MessageCountEvent(this, 2, z2, messageArr2);
            queueEvent(mailEvent, this.messageCountListeners);
        }
    }

    public synchronized void addMessageChangedListener(MessageChangedListener messageChangedListener) {
        MessageChangedListener messageChangedListener2 = messageChangedListener;
        synchronized (this) {
            if (this.messageChangedListeners == null) {
                Vector vector = r6;
                Vector vector2 = new Vector();
                this.messageChangedListeners = vector;
            }
            this.messageChangedListeners.addElement(messageChangedListener2);
        }
    }

    public synchronized void removeMessageChangedListener(MessageChangedListener messageChangedListener) {
        MessageChangedListener messageChangedListener2 = messageChangedListener;
        synchronized (this) {
            if (this.messageChangedListeners != null) {
                boolean removeElement = this.messageChangedListeners.removeElement(messageChangedListener2);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void notifyMessageChangedListeners(int i, Message message) {
        int i2 = i;
        Message message2 = message;
        if (this.messageChangedListeners != null) {
            MailEvent mailEvent = r9;
            MailEvent messageChangedEvent = new MessageChangedEvent(this, i2, message2);
            queueEvent(mailEvent, this.messageChangedListeners);
        }
    }

    private void queueEvent(MailEvent mailEvent, Vector vector) {
        MailEvent mailEvent2 = mailEvent;
        Vector vector2 = vector;
        Object obj = this.qLock;
        Object obj2 = obj;
        synchronized (obj) {
            try {
                Folder folder = this.q;
                if (folder == null) {
                    EventQueue eventQueue = r7;
                    EventQueue eventQueue2 = new EventQueue();
                    folder.q = eventQueue;
                }
            } finally {
                obj2 = 
/*
Method generation error in method: javax.mail.Folder.queueEvent(javax.mail.event.MailEvent, java.util.Vector):void, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r3_5 'obj2' java.lang.Object) = (r3_4 'obj2' java.lang.Object), (r0_0 'this' java.lang.Object A:{THIS}) in method: javax.mail.Folder.queueEvent(javax.mail.event.MailEvent, java.util.Vector):void, dex: classes.dex
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:228)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:205)
	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:102)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:52)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:300)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:95)
	at jadx.core.codegen.RegionGen.makeSynchronizedRegion(RegionGen.java:230)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:67)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:89)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:183)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:321)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:259)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:221)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:111)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:77)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:10)
	at jadx.core.ProcessClass.process(ProcessClass.java:38)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
Caused by: jadx.core.utils.exceptions.CodegenException: MERGE can be used only in fallback mode
	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:539)
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:511)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:222)
	... 28 more

*/

    private void terminateQueue() {
        Object obj = this.qLock;
        Object obj2 = obj;
        synchronized (obj) {
            try {
                if (this.q != null) {
                }
            } finally {
                Object obj3 = obj2;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        super.finalize();
        terminateQueue();
    }

    public String toString() {
        String fullName = getFullName();
        if (fullName != null) {
            return fullName;
        }
        return super.toString();
    }
}
