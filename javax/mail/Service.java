package javax.mail;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.mail.event.MailEvent;

public abstract class Service {
    private boolean connected = false;
    private Vector connectionListeners = null;
    protected boolean debug = false;
    private EventQueue q;
    private Object qLock;
    protected Session session;
    protected URLName url = null;

    static class TerminatorEvent extends MailEvent {
        private static final long serialVersionUID = 5542172141759168416L;

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

    protected Service(Session session, URLName uRLName) {
        Session session2 = session;
        URLName uRLName2 = uRLName;
        Object obj = r6;
        Object obj2 = new Object();
        this.qLock = obj;
        this.session = session2;
        this.url = uRLName2;
        this.debug = session2.getDebug();
    }

    public void connect() throws MessagingException {
        connect(null, null, null);
    }

    public void connect(String str, String str2, String str3) throws MessagingException {
        connect(str, -1, str2, str3);
    }

    public void connect(String str, String str2) throws MessagingException {
        connect(null, str, str2);
    }

    public synchronized void connect(String str, int i, String str2, String str3) throws MessagingException {
        AuthenticationFailedException e;
        String str4 = str;
        int i2 = i;
        String str5 = str2;
        String str6 = str3;
        synchronized (this) {
            if (isConnected()) {
                IllegalStateException illegalStateException = r23;
                IllegalStateException illegalStateException2 = new IllegalStateException("already connected");
                throw illegalStateException;
            }
            Session session;
            URLName uRLName;
            URLName uRLName2;
            PasswordAuthentication passwordAuthentication;
            boolean z = false;
            Object obj = null;
            String str7 = null;
            String str8 = null;
            if (this.url != null) {
                str7 = this.url.getProtocol();
                if (str4 == null) {
                    str4 = this.url.getHost();
                }
                if (i2 == -1) {
                    i2 = this.url.getPort();
                }
                if (str5 == null) {
                    str5 = this.url.getUsername();
                    if (str6 == null) {
                        str6 = this.url.getPassword();
                    }
                } else if (str6 == null && str5.equals(this.url.getUsername())) {
                    str6 = this.url.getPassword();
                }
                str8 = this.url.getFile();
            }
            if (str7 != null) {
                StringBuilder stringBuilder;
                StringBuilder stringBuilder2;
                if (str4 == null) {
                    session = this.session;
                    stringBuilder = r23;
                    stringBuilder2 = new StringBuilder("mail.");
                    str4 = session.getProperty(stringBuilder.append(str7).append(".host").toString());
                }
                if (str5 == null) {
                    session = this.session;
                    stringBuilder = r23;
                    stringBuilder2 = new StringBuilder("mail.");
                    str5 = session.getProperty(stringBuilder.append(str7).append(".user").toString());
                }
            }
            if (str4 == null) {
                str4 = this.session.getProperty("mail.host");
            }
            if (str5 == null) {
                str5 = this.session.getProperty("mail.user");
            }
            if (str5 == null) {
                try {
                    str5 = System.getProperty("user.name");
                } catch (SecurityException e2) {
                    SecurityException securityException = e2;
                    if (this.debug) {
                        securityException.printStackTrace(this.session.getDebugOut());
                    }
                }
            }
            if (str6 == null) {
                if (this.url != null) {
                    uRLName = r23;
                    uRLName2 = new URLName(str7, str4, i2, str8, str5, null);
                    setURLName(uRLName);
                    passwordAuthentication = this.session.getPasswordAuthentication(getURLName());
                    if (passwordAuthentication == null) {
                        int obj2 = 1;
                    } else if (str5 == null) {
                        str5 = passwordAuthentication.getUserName();
                        str6 = passwordAuthentication.getPassword();
                    } else if (str5.equals(passwordAuthentication.getUserName())) {
                        str6 = passwordAuthentication.getPassword();
                    }
                }
            }
            AuthenticationFailedException authenticationFailedException = null;
            try {
                z = protocolConnect(str4, i2, str5, str6);
            } catch (AuthenticationFailedException e3) {
                authenticationFailedException = e3;
            }
            if (!z) {
                InetAddress byName;
                try {
                    byName = InetAddress.getByName(str4);
                } catch (UnknownHostException e4) {
                    UnknownHostException unknownHostException = e4;
                    byName = null;
                }
                passwordAuthentication = this.session.requestPasswordAuthentication(byName, i2, str7, null, str5);
                if (passwordAuthentication != null) {
                    str5 = passwordAuthentication.getUserName();
                    str6 = passwordAuthentication.getPassword();
                    z = protocolConnect(str4, i2, str5, str6);
                }
            }
            if (z) {
                uRLName = r23;
                uRLName2 = new URLName(str7, str4, i2, str8, str5, str6);
                setURLName(uRLName);
                if (obj2 != null) {
                    session = this.session;
                    uRLName = getURLName();
                    PasswordAuthentication passwordAuthentication2 = r23;
                    PasswordAuthentication passwordAuthentication3 = new PasswordAuthentication(str5, str6);
                    session.setPasswordAuthentication(uRLName, passwordAuthentication2);
                }
                setConnected(true);
                notifyConnectionListeners(1);
            } else if (authenticationFailedException != null) {
                throw authenticationFailedException;
            } else {
                e3 = r23;
                AuthenticationFailedException authenticationFailedException2 = new AuthenticationFailedException();
                throw e3;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean protocolConnect(String str, int i, String str2, String str3) throws MessagingException {
        String str4 = str;
        int i2 = i;
        String str5 = str2;
        String str6 = str3;
        return false;
    }

    public synchronized boolean isConnected() {
        boolean z;
        synchronized (this) {
            z = this.connected;
        }
        return z;
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void setConnected(boolean z) {
        boolean z2 = z;
        synchronized (this) {
            this.connected = z2;
        }
    }

    public synchronized void close() throws MessagingException {
        synchronized (this) {
            setConnected(false);
            notifyConnectionListeners(3);
        }
    }

    public synchronized URLName getURLName() {
        URLName uRLName;
        synchronized (this) {
            if (this.url == null || (this.url.getPassword() == null && this.url.getFile() == null)) {
                uRLName = this.url;
            } else {
                URLName uRLName2 = r10;
                URLName uRLName3 = new URLName(this.url.getProtocol(), this.url.getHost(), this.url.getPort(), null, this.url.getUsername(), null);
                uRLName = uRLName2;
            }
        }
        return uRLName;
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void setURLName(URLName uRLName) {
        URLName uRLName2 = uRLName;
        synchronized (this) {
            this.url = uRLName2;
        }
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

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void notifyConnectionListeners(int i) {
        int i2 = i;
        synchronized (this) {
            if (this.connectionListeners != null) {
                MailEvent mailEvent = r8;
                MailEvent connectionEvent = new ConnectionEvent(this, i2);
                queueEvent(mailEvent, this.connectionListeners);
            }
            if (i2 == 3) {
                terminateQueue();
            }
        }
    }

    public String toString() {
        URLName uRLName = getURLName();
        if (uRLName != null) {
            return uRLName.toString();
        }
        return super.toString();
    }

    /* Access modifiers changed, original: protected */
    public void queueEvent(MailEvent mailEvent, Vector vector) {
        MailEvent mailEvent2 = mailEvent;
        Vector vector2 = vector;
        Object obj = this.qLock;
        Object obj2 = obj;
        synchronized (obj) {
            try {
                Service service = this.q;
                if (service == null) {
                    EventQueue eventQueue = r7;
                    EventQueue eventQueue2 = new EventQueue();
                    service.q = eventQueue;
                }
            } finally {
                obj2 = 
/*
Method generation error in method: javax.mail.Service.queueEvent(javax.mail.event.MailEvent, java.util.Vector):void, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r3_5 'obj2' java.lang.Object) = (r3_4 'obj2' java.lang.Object), (r0_0 'this' java.lang.Object A:{THIS}) in method: javax.mail.Service.queueEvent(javax.mail.event.MailEvent, java.util.Vector):void, dex: classes.dex
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
}
