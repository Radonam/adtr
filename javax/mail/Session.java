package javax.mail;

import com.sun.mail.util.LineInputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Provider.Type;

public final class Session {
    private static Session defaultSession = null;
    private final Properties addressMap;
    private final Hashtable authTable;
    private final Authenticator authenticator;
    private boolean debug = false;
    private PrintStream out;
    private final Properties props;
    private final Vector providers;
    private final Hashtable providersByClassName;
    private final Hashtable providersByProtocol;

    private Session(Properties properties, Authenticator authenticator) {
        Class cls;
        Properties properties2 = properties;
        Authenticator authenticator2 = authenticator;
        Hashtable hashtable = r7;
        Hashtable hashtable2 = new Hashtable();
        this.authTable = hashtable;
        Vector vector = r7;
        Vector vector2 = new Vector();
        this.providers = vector;
        hashtable = r7;
        hashtable2 = new Hashtable();
        this.providersByProtocol = hashtable;
        hashtable = r7;
        hashtable2 = new Hashtable();
        this.providersByClassName = hashtable;
        Properties properties3 = r7;
        Properties properties4 = new Properties();
        this.addressMap = properties3;
        this.props = properties2;
        this.authenticator = authenticator2;
        if (Boolean.valueOf(properties2.getProperty("mail.debug")).booleanValue()) {
            this.debug = true;
        }
        if (this.debug) {
            pr("DEBUG: JavaMail version 1.4.1");
        }
        if (authenticator2 != null) {
            cls = authenticator2.getClass();
        } else {
            cls = getClass();
        }
        loadProviders(cls);
        loadAddressMap(cls);
    }

    public static Session getInstance(Properties properties, Authenticator authenticator) {
        Session session = r6;
        Session session2 = new Session(properties, authenticator);
        return session;
    }

    public static Session getInstance(Properties properties) {
        Session session = r5;
        Session session2 = new Session(properties, null);
        return session;
    }

    public static synchronized Session getDefaultInstance(Properties properties, Authenticator authenticator) {
        Session session;
        Properties properties2 = properties;
        Authenticator authenticator2 = authenticator;
        synchronized (Session.class) {
            if (defaultSession == null) {
                Session session2 = r7;
                Session session3 = new Session(properties2, authenticator2);
                defaultSession = session2;
            } else if (defaultSession.authenticator != authenticator2) {
                if (defaultSession.authenticator == null || authenticator2 == null || defaultSession.authenticator.getClass().getClassLoader() != authenticator2.getClass().getClassLoader()) {
                    SecurityException securityException = r7;
                    SecurityException securityException2 = new SecurityException("Access to default session denied");
                    throw securityException;
                }
            }
            session = defaultSession;
        }
        return session;
    }

    public static Session getDefaultInstance(Properties properties) {
        return getDefaultInstance(properties, null);
    }

    public synchronized void setDebug(boolean z) {
        boolean z2 = z;
        synchronized (this) {
            this.debug = z2;
            if (z2) {
                pr("DEBUG: setDebug: JavaMail version 1.4.1");
            }
        }
    }

    public synchronized boolean getDebug() {
        boolean z;
        synchronized (this) {
            z = this.debug;
        }
        return z;
    }

    public synchronized void setDebugOut(PrintStream printStream) {
        PrintStream printStream2 = printStream;
        synchronized (this) {
            this.out = printStream2;
        }
    }

    public synchronized PrintStream getDebugOut() {
        PrintStream printStream;
        synchronized (this) {
            if (this.out == null) {
                printStream = System.out;
            } else {
                printStream = this.out;
            }
        }
        return printStream;
    }

    public synchronized Provider[] getProviders() {
        Provider[] providerArr;
        synchronized (this) {
            Provider[] providerArr2 = new Provider[this.providers.size()];
            this.providers.copyInto(providerArr2);
            providerArr = providerArr2;
        }
        return providerArr;
    }

    public synchronized Provider getProvider(String str) throws NoSuchProviderException {
        Provider provider;
        String str2 = str;
        synchronized (this) {
            NoSuchProviderException noSuchProviderException;
            NoSuchProviderException noSuchProviderException2;
            if (str2 != null) {
                if (str2.length() > 0) {
                    Provider provider2 = null;
                    Properties properties = this.props;
                    StringBuilder stringBuilder = r10;
                    StringBuilder stringBuilder2 = new StringBuilder("mail.");
                    String property = properties.getProperty(stringBuilder.append(str2).append(".class").toString());
                    if (property != null) {
                        if (this.debug) {
                            stringBuilder = r10;
                            stringBuilder2 = new StringBuilder("DEBUG: mail.");
                            pr(stringBuilder.append(str2).append(".class property exists and points to ").append(property).toString());
                        }
                        provider2 = (Provider) this.providersByClassName.get(property);
                    }
                    if (provider2 != null) {
                        provider = provider2;
                    } else {
                        provider2 = (Provider) this.providersByProtocol.get(str2);
                        if (provider2 == null) {
                            noSuchProviderException = r10;
                            stringBuilder2 = r10;
                            StringBuilder stringBuilder3 = new StringBuilder("No provider for ");
                            noSuchProviderException2 = new NoSuchProviderException(stringBuilder2.append(str2).toString());
                            throw noSuchProviderException;
                        }
                        if (this.debug) {
                            stringBuilder = r10;
                            stringBuilder2 = new StringBuilder("DEBUG: getProvider() returning ");
                            pr(stringBuilder.append(provider2.toString()).toString());
                        }
                        provider = provider2;
                    }
                }
            }
            noSuchProviderException = r10;
            noSuchProviderException2 = new NoSuchProviderException("Invalid protocol: null");
            throw noSuchProviderException;
        }
        return provider;
    }

    public synchronized void setProvider(Provider provider) throws NoSuchProviderException {
        Provider provider2 = provider;
        synchronized (this) {
            if (provider2 == null) {
                NoSuchProviderException noSuchProviderException = r7;
                NoSuchProviderException noSuchProviderException2 = new NoSuchProviderException("Can't set null provider");
                throw noSuchProviderException;
            }
            Object put = this.providersByProtocol.put(provider2.getProtocol(), provider2);
            Properties properties = this.props;
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder("mail.");
            put = properties.put(stringBuilder.append(provider2.getProtocol()).append(".class").toString(), provider2.getClassName());
        }
    }

    public Store getStore() throws NoSuchProviderException {
        return getStore(getProperty("mail.store.protocol"));
    }

    public Store getStore(String str) throws NoSuchProviderException {
        URLName uRLName = r11;
        URLName uRLName2 = new URLName(str, null, -1, null, null, null);
        return getStore(uRLName);
    }

    public Store getStore(URLName uRLName) throws NoSuchProviderException {
        URLName uRLName2 = uRLName;
        return getStore(getProvider(uRLName2.getProtocol()), uRLName2);
    }

    public Store getStore(Provider provider) throws NoSuchProviderException {
        return getStore(provider, null);
    }

    private Store getStore(Provider provider, URLName uRLName) throws NoSuchProviderException {
        Provider provider2 = provider;
        URLName uRLName2 = uRLName;
        NoSuchProviderException noSuchProviderException;
        NoSuchProviderException noSuchProviderException2;
        if (provider2 == null || provider2.getType() != Type.STORE) {
            noSuchProviderException = r7;
            noSuchProviderException2 = new NoSuchProviderException("invalid provider");
            throw noSuchProviderException;
        }
        try {
            return (Store) getService(provider2, uRLName2);
        } catch (ClassCastException e) {
            ClassCastException classCastException = e;
            noSuchProviderException = r7;
            noSuchProviderException2 = new NoSuchProviderException("incorrect class");
            throw noSuchProviderException;
        }
    }

    public Folder getFolder(URLName uRLName) throws MessagingException {
        URLName uRLName2 = uRLName;
        Store store = getStore(uRLName2);
        store.connect();
        return store.getFolder(uRLName2);
    }

    public Transport getTransport() throws NoSuchProviderException {
        return getTransport(getProperty("mail.transport.protocol"));
    }

    public Transport getTransport(String str) throws NoSuchProviderException {
        URLName uRLName = r11;
        URLName uRLName2 = new URLName(str, null, -1, null, null, null);
        return getTransport(uRLName);
    }

    public Transport getTransport(URLName uRLName) throws NoSuchProviderException {
        URLName uRLName2 = uRLName;
        return getTransport(getProvider(uRLName2.getProtocol()), uRLName2);
    }

    public Transport getTransport(Provider provider) throws NoSuchProviderException {
        return getTransport(provider, null);
    }

    public Transport getTransport(Address address) throws NoSuchProviderException {
        Address address2 = address;
        String str = (String) this.addressMap.get(address2.getType());
        if (str != null) {
            return getTransport(str);
        }
        NoSuchProviderException noSuchProviderException = r8;
        StringBuilder stringBuilder = r8;
        StringBuilder stringBuilder2 = new StringBuilder("No provider for Address type: ");
        NoSuchProviderException noSuchProviderException2 = new NoSuchProviderException(stringBuilder.append(address2.getType()).toString());
        throw noSuchProviderException;
    }

    private Transport getTransport(Provider provider, URLName uRLName) throws NoSuchProviderException {
        Provider provider2 = provider;
        URLName uRLName2 = uRLName;
        NoSuchProviderException noSuchProviderException;
        NoSuchProviderException noSuchProviderException2;
        if (provider2 == null || provider2.getType() != Type.TRANSPORT) {
            noSuchProviderException = r7;
            noSuchProviderException2 = new NoSuchProviderException("invalid provider");
            throw noSuchProviderException;
        }
        try {
            return (Transport) getService(provider2, uRLName2);
        } catch (ClassCastException e) {
            ClassCastException classCastException = e;
            noSuchProviderException = r7;
            noSuchProviderException2 = new NoSuchProviderException("incorrect class");
            throw noSuchProviderException;
        }
    }

    private Object getService(Provider provider, URLName uRLName) throws NoSuchProviderException {
        Exception exception;
        Provider provider2 = provider;
        URLName uRLName2 = uRLName;
        NoSuchProviderException noSuchProviderException;
        NoSuchProviderException noSuchProviderException2;
        if (provider2 == null) {
            noSuchProviderException = r17;
            noSuchProviderException2 = new NoSuchProviderException("null");
            throw noSuchProviderException;
        }
        ClassLoader classLoader;
        if (uRLName2 == null) {
            URLName uRLName3 = r17;
            URLName uRLName4 = new URLName(provider2.getProtocol(), null, -1, null, null, null);
            uRLName2 = uRLName3;
        }
        Object obj = null;
        if (this.authenticator != null) {
            classLoader = this.authenticator.getClass().getClassLoader();
        } else {
            classLoader = getClass().getClassLoader();
        }
        Class cls = null;
        try {
            ClassLoader contextClassLoader = getContextClassLoader();
            if (contextClassLoader != null) {
                try {
                    cls = contextClassLoader.loadClass(provider2.getClassName());
                } catch (ClassNotFoundException e) {
                    ClassNotFoundException classNotFoundException = e;
                }
            }
            if (cls == null) {
                cls = classLoader.loadClass(provider2.getClassName());
            }
        } catch (Exception e2) {
            exception = e2;
            try {
                cls = Class.forName(provider2.getClassName());
            } catch (Exception e22) {
                Exception exception2 = e22;
                if (this.debug) {
                    exception2.printStackTrace(getDebugOut());
                }
                noSuchProviderException = r17;
                noSuchProviderException2 = new NoSuchProviderException(provider2.getProtocol());
                throw noSuchProviderException;
            }
        }
        try {
            r17 = new Class[2];
            Class[] clsArr = r17;
            r17[0] = Session.class;
            r17 = clsArr;
            clsArr = r17;
            r17[1] = URLName.class;
            Constructor constructor = cls.getConstructor(clsArr);
            r17 = new Object[2];
            Object[] objArr = r17;
            r17[0] = this;
            r17 = objArr;
            objArr = r17;
            r17[1] = uRLName2;
            return constructor.newInstance(objArr);
        } catch (Exception e222) {
            exception = e222;
            if (this.debug) {
                exception.printStackTrace(getDebugOut());
            }
            noSuchProviderException = r17;
            noSuchProviderException2 = new NoSuchProviderException(provider2.getProtocol());
            throw noSuchProviderException;
        }
    }

    public void setPasswordAuthentication(URLName uRLName, PasswordAuthentication passwordAuthentication) {
        URLName uRLName2 = uRLName;
        PasswordAuthentication passwordAuthentication2 = passwordAuthentication;
        Object remove;
        if (passwordAuthentication2 == null) {
            remove = this.authTable.remove(uRLName2);
        } else {
            remove = this.authTable.put(uRLName2, passwordAuthentication2);
        }
    }

    public PasswordAuthentication getPasswordAuthentication(URLName uRLName) {
        return (PasswordAuthentication) this.authTable.get(uRLName);
    }

    public PasswordAuthentication requestPasswordAuthentication(InetAddress inetAddress, int i, String str, String str2, String str3) {
        InetAddress inetAddress2 = inetAddress;
        int i2 = i;
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        if (this.authenticator != null) {
            return this.authenticator.requestPasswordAuthentication(inetAddress2, i2, str4, str5, str6);
        }
        return null;
    }

    public Properties getProperties() {
        return this.props;
    }

    public String getProperty(String str) {
        return this.props.getProperty(str);
    }

    private void loadProviders(Class cls) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        Class cls2 = cls;
        AnonymousClass1 anonymousClass1 = r12;
        AnonymousClass1 anonymousClass12 = new StreamLoader(this) {
            final /* synthetic */ Session this$0;

            {
                this.this$0 = r5;
            }

            public void load(InputStream inputStream) throws IOException {
                this.this$0.loadProvidersFromStream(inputStream);
            }
        };
        AnonymousClass1 anonymousClass13 = anonymousClass1;
        try {
            StringBuilder stringBuilder3 = r12;
            stringBuilder = new StringBuilder(String.valueOf(System.getProperty("java.home")));
            loadFile(stringBuilder3.append(File.separator).append("lib").append(File.separator).append("javamail.providers").toString(), anonymousClass13);
        } catch (SecurityException e) {
            SecurityException securityException = e;
            if (this.debug) {
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder("DEBUG: can't get java.home: ");
                pr(stringBuilder.append(securityException).toString());
            }
        }
        loadAllResources("META-INF/javamail.providers", cls2, anonymousClass13);
        loadResource("/META-INF/javamail.default.providers", cls2, anonymousClass13);
        if (this.providers.size() == 0) {
            if (this.debug) {
                pr("DEBUG: failed to load any providers, using defaults");
            }
            Provider provider = r12;
            Provider provider2 = new Provider(Type.STORE, "imap", "com.sun.mail.imap.IMAPStore", "Sun Microsystems, Inc.", Version.version);
            addProvider(provider);
            provider = r12;
            provider2 = new Provider(Type.STORE, "imaps", "com.sun.mail.imap.IMAPSSLStore", "Sun Microsystems, Inc.", Version.version);
            addProvider(provider);
            provider = r12;
            provider2 = new Provider(Type.STORE, "pop3", "com.sun.mail.pop3.POP3Store", "Sun Microsystems, Inc.", Version.version);
            addProvider(provider);
            provider = r12;
            provider2 = new Provider(Type.STORE, "pop3s", "com.sun.mail.pop3.POP3SSLStore", "Sun Microsystems, Inc.", Version.version);
            addProvider(provider);
            provider = r12;
            provider2 = new Provider(Type.TRANSPORT, "smtp", "com.sun.mail.smtp.SMTPTransport", "Sun Microsystems, Inc.", Version.version);
            addProvider(provider);
            provider = r12;
            provider2 = new Provider(Type.TRANSPORT, "smtps", "com.sun.mail.smtp.SMTPSSLTransport", "Sun Microsystems, Inc.", Version.version);
            addProvider(provider);
        }
        if (this.debug) {
            pr("DEBUG: Tables of loaded providers");
            stringBuilder = r12;
            stringBuilder2 = new StringBuilder("DEBUG: Providers Listed By Class Name: ");
            pr(stringBuilder.append(this.providersByClassName.toString()).toString());
            stringBuilder = r12;
            stringBuilder2 = new StringBuilder("DEBUG: Providers Listed By Protocol: ");
            pr(stringBuilder.append(this.providersByProtocol.toString()).toString());
        }
    }

    private void loadProvidersFromStream(InputStream inputStream) throws IOException {
        InputStream inputStream2 = inputStream;
        if (inputStream2 != null) {
            LineInputStream lineInputStream = r20;
            LineInputStream lineInputStream2 = new LineInputStream(inputStream2);
            LineInputStream lineInputStream3 = lineInputStream;
            while (true) {
                String readLine = lineInputStream3.readLine();
                String str = readLine;
                if (readLine != null) {
                    if (!str.startsWith("#")) {
                        Type type = null;
                        String str2 = null;
                        String str3 = null;
                        String str4 = null;
                        String str5 = null;
                        StringTokenizer stringTokenizer = r20;
                        StringTokenizer stringTokenizer2 = new StringTokenizer(str, ";");
                        StringTokenizer stringTokenizer3 = stringTokenizer;
                        while (stringTokenizer3.hasMoreTokens()) {
                            String trim = stringTokenizer3.nextToken().trim();
                            int indexOf = trim.indexOf("=");
                            if (trim.startsWith("protocol=")) {
                                str2 = trim.substring(indexOf + 1);
                            } else if (trim.startsWith("type=")) {
                                String substring = trim.substring(indexOf + 1);
                                if (substring.equalsIgnoreCase("store")) {
                                    type = Type.STORE;
                                } else if (substring.equalsIgnoreCase("transport")) {
                                    type = Type.TRANSPORT;
                                }
                            } else if (trim.startsWith("class=")) {
                                str3 = trim.substring(indexOf + 1);
                            } else if (trim.startsWith("vendor=")) {
                                str4 = trim.substring(indexOf + 1);
                            } else if (trim.startsWith("version=")) {
                                str5 = trim.substring(indexOf + 1);
                            }
                        }
                        if (type != null && str2 != null && str3 != null && str2.length() > 0 && str3.length() > 0) {
                            Provider provider = r20;
                            Provider provider2 = new Provider(type, str2, str3, str4, str5);
                            addProvider(provider);
                        } else if (this.debug) {
                            StringBuilder stringBuilder = r20;
                            StringBuilder stringBuilder2 = new StringBuilder("DEBUG: Bad provider entry: ");
                            pr(stringBuilder.append(str).toString());
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    public synchronized void addProvider(Provider provider) {
        Provider provider2 = provider;
        synchronized (this) {
            this.providers.addElement(provider2);
            Object put = this.providersByClassName.put(provider2.getClassName(), provider2);
            if (!this.providersByProtocol.containsKey(provider2.getProtocol())) {
                put = this.providersByProtocol.put(provider2.getProtocol(), provider2);
            }
        }
    }

    private void loadAddressMap(Class cls) {
        StringBuilder stringBuilder;
        Class cls2 = cls;
        AnonymousClass2 anonymousClass2 = r8;
        AnonymousClass2 anonymousClass22 = new StreamLoader(this) {
            final /* synthetic */ Session this$0;

            {
                this.this$0 = r5;
            }

            public void load(InputStream inputStream) throws IOException {
                this.this$0.addressMap.load(inputStream);
            }
        };
        AnonymousClass2 anonymousClass23 = anonymousClass2;
        loadResource("/META-INF/javamail.default.address.map", cls2, anonymousClass23);
        loadAllResources("META-INF/javamail.address.map", cls2, anonymousClass23);
        try {
            StringBuilder stringBuilder2 = r8;
            stringBuilder = new StringBuilder(String.valueOf(System.getProperty("java.home")));
            loadFile(stringBuilder2.append(File.separator).append("lib").append(File.separator).append("javamail.address.map").toString(), anonymousClass23);
        } catch (SecurityException e) {
            SecurityException securityException = e;
            if (this.debug) {
                stringBuilder = r8;
                StringBuilder stringBuilder3 = new StringBuilder("DEBUG: can't get java.home: ");
                pr(stringBuilder.append(securityException).toString());
            }
        }
        if (this.addressMap.isEmpty()) {
            if (this.debug) {
                pr("DEBUG: failed to load address map, using defaults");
            }
            Object put = this.addressMap.put("rfc822", "smtp");
        }
    }

    public synchronized void setProtocolForAddress(String str, String str2) {
        String str3 = str;
        String str4 = str2;
        synchronized (this) {
            Object remove;
            if (str4 == null) {
                remove = this.addressMap.remove(str3);
            } else {
                remove = this.addressMap.put(str3, str4);
            }
        }
    }

    private void loadFile(String str, StreamLoader streamLoader) {
        IOException iOException;
        String str2 = str;
        StreamLoader streamLoader2 = streamLoader;
        InputStream inputStream = null;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        try {
            InputStream inputStream2 = r12;
            InputStream inputStream3 = r12;
            InputStream fileInputStream = new FileInputStream(str2);
            InputStream bufferedInputStream = new BufferedInputStream(inputStream3);
            inputStream = inputStream2;
            streamLoader2.load(inputStream);
            if (this.debug) {
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder("DEBUG: successfully loaded file: ");
                pr(stringBuilder.append(str2).toString());
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    iOException = e;
                }
            }
        } catch (IOException e2) {
            IOException iOException2 = e2;
            if (this.debug) {
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder("DEBUG: not loading file: ");
                pr(stringBuilder.append(str2).toString());
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder("DEBUG: ");
                pr(stringBuilder.append(iOException2).toString());
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e22) {
                    iOException = e22;
                }
            }
        } catch (SecurityException e3) {
            SecurityException securityException = e3;
            if (this.debug) {
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder("DEBUG: not loading file: ");
                pr(stringBuilder.append(str2).toString());
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder("DEBUG: ");
                pr(stringBuilder.append(securityException).toString());
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e222) {
                    iOException = e222;
                }
            }
        } catch (Throwable th) {
            Throwable th2 = th;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2222) {
                    iOException = e2222;
                }
            }
            Throwable th3 = th2;
        }
    }

    private void loadResource(String str, Class cls, StreamLoader streamLoader) {
        IOException iOException;
        String str2 = str;
        StreamLoader streamLoader2 = streamLoader;
        InputStream inputStream = null;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        try {
            inputStream = getResourceAsStream(cls, str2);
            if (inputStream != null) {
                streamLoader2.load(inputStream);
                if (this.debug) {
                    stringBuilder = r12;
                    stringBuilder2 = new StringBuilder("DEBUG: successfully loaded resource: ");
                    pr(stringBuilder.append(str2).toString());
                }
            } else if (this.debug) {
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder("DEBUG: not loading resource: ");
                pr(stringBuilder.append(str2).toString());
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    iOException = e;
                }
            }
        } catch (IOException e2) {
            IOException iOException2 = e2;
            if (this.debug) {
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder("DEBUG: ");
                pr(stringBuilder.append(iOException2).toString());
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e22) {
                    iOException = e22;
                }
            }
        } catch (SecurityException e3) {
            SecurityException securityException = e3;
            if (this.debug) {
                stringBuilder = r12;
                stringBuilder2 = new StringBuilder("DEBUG: ");
                pr(stringBuilder.append(securityException).toString());
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e222) {
                    iOException = e222;
                }
            }
        } catch (Throwable th) {
            Throwable th2 = th;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2222) {
                    iOException = e2222;
                }
            }
            Throwable th3 = th2;
        }
    }

    private void loadAllResources(String str, Class cls, StreamLoader streamLoader) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        IOException iOException;
        String str2 = str;
        Class cls2 = cls;
        StreamLoader streamLoader2 = streamLoader;
        Object obj = null;
        Object obj2 = null;
        try {
            URL[] resources;
            ClassLoader contextClassLoader = getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = cls2.getClassLoader();
            }
            if (contextClassLoader != null) {
                resources = getResources(contextClassLoader, str2);
            } else {
                resources = getSystemResources(str2);
            }
            if (resources != null) {
                for (URL url : resources) {
                    InputStream inputStream = null;
                    if (this.debug) {
                        stringBuilder = r17;
                        stringBuilder2 = new StringBuilder("DEBUG: URL ");
                        pr(stringBuilder.append(url).toString());
                    }
                    try {
                        inputStream = openStream(url);
                        if (inputStream != null) {
                            streamLoader2.load(inputStream);
                            obj = 1;
                            if (this.debug) {
                                stringBuilder = r17;
                                stringBuilder2 = new StringBuilder("DEBUG: successfully loaded resource: ");
                                pr(stringBuilder.append(url).toString());
                            }
                        } else if (this.debug) {
                            stringBuilder = r17;
                            stringBuilder2 = new StringBuilder("DEBUG: not loading resource: ");
                            pr(stringBuilder.append(url).toString());
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                iOException = e;
                            }
                        }
                    } catch (IOException e2) {
                        IOException iOException2 = e2;
                        if (this.debug) {
                            stringBuilder = r17;
                            stringBuilder2 = new StringBuilder("DEBUG: ");
                            pr(stringBuilder.append(iOException2).toString());
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e22) {
                                iOException = e22;
                            }
                        }
                    } catch (SecurityException e3) {
                        SecurityException securityException = e3;
                        if (this.debug) {
                            stringBuilder = r17;
                            stringBuilder2 = new StringBuilder("DEBUG: ");
                            pr(stringBuilder.append(securityException).toString());
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e222) {
                                iOException = e222;
                            }
                        }
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e2222) {
                                iOException = e2222;
                            }
                        }
                        Throwable th3 = th2;
                    }
                }
            }
        } catch (Exception e4) {
            Exception exception = e4;
            if (this.debug) {
                stringBuilder = r17;
                stringBuilder2 = new StringBuilder("DEBUG: ");
                pr(stringBuilder.append(exception).toString());
            }
        }
        if (obj == null) {
            if (this.debug) {
                pr("DEBUG: !anyLoaded");
            }
            stringBuilder = r17;
            stringBuilder2 = new StringBuilder("/");
            loadResource(stringBuilder.append(str2).toString(), cls2, streamLoader2);
        }
    }

    private void pr(String str) {
        getDebugOut().println(str);
    }

    private static ClassLoader getContextClassLoader() {
        AnonymousClass3 anonymousClass3 = r2;
        AnonymousClass3 anonymousClass32 = new PrivilegedAction() {
            public Object run() {
                ClassLoader classLoader = null;
                try {
                    classLoader = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e) {
                    SecurityException securityException = e;
                }
                return classLoader;
            }
        };
        return (ClassLoader) AccessController.doPrivileged(anonymousClass3);
    }

    private static InputStream getResourceAsStream(Class cls, String str) throws IOException {
        try {
            AnonymousClass4 anonymousClass4 = r7;
            final Class cls2 = cls;
            final String str2 = str;
            AnonymousClass4 anonymousClass42 = new PrivilegedExceptionAction() {
                public Object run() throws IOException {
                    return cls2.getResourceAsStream(str2);
                }
            };
            return (InputStream) AccessController.doPrivileged(anonymousClass4);
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    private static URL[] getResources(ClassLoader classLoader, String str) {
        AnonymousClass5 anonymousClass5 = r6;
        final ClassLoader classLoader2 = classLoader;
        final String str2 = str;
        AnonymousClass5 anonymousClass52 = new PrivilegedAction() {
            public Object run() {
                Object obj = (URL[]) null;
                try {
                    Vector vector = r7;
                    Vector vector2 = new Vector();
                    Vector vector3 = vector;
                    Enumeration resources = classLoader2.getResources(str2);
                    while (resources != null && resources.hasMoreElements()) {
                        URL url = (URL) resources.nextElement();
                        if (url != null) {
                            vector3.addElement(url);
                        }
                    }
                    if (vector3.size() > 0) {
                        obj = new URL[vector3.size()];
                        vector3.copyInto(obj);
                    }
                } catch (IOException e) {
                    IOException iOException = e;
                } catch (SecurityException e2) {
                    SecurityException securityException = e2;
                }
                return obj;
            }
        };
        return (URL[]) AccessController.doPrivileged(anonymousClass5);
    }

    private static URL[] getSystemResources(String str) {
        AnonymousClass6 anonymousClass6 = r4;
        final String str2 = str;
        AnonymousClass6 anonymousClass62 = new PrivilegedAction() {
            public Object run() {
                Object obj = (URL[]) null;
                try {
                    Vector vector = r7;
                    Vector vector2 = new Vector();
                    Vector vector3 = vector;
                    Enumeration systemResources = ClassLoader.getSystemResources(str2);
                    while (systemResources != null && systemResources.hasMoreElements()) {
                        URL url = (URL) systemResources.nextElement();
                        if (url != null) {
                            vector3.addElement(url);
                        }
                    }
                    if (vector3.size() > 0) {
                        obj = new URL[vector3.size()];
                        vector3.copyInto(obj);
                    }
                } catch (IOException e) {
                    IOException iOException = e;
                } catch (SecurityException e2) {
                    SecurityException securityException = e2;
                }
                return obj;
            }
        };
        return (URL[]) AccessController.doPrivileged(anonymousClass6);
    }

    private static InputStream openStream(URL url) throws IOException {
        try {
            AnonymousClass7 anonymousClass7 = r5;
            final URL url2 = url;
            AnonymousClass7 anonymousClass72 = new PrivilegedExceptionAction() {
                public Object run() throws IOException {
                    return url2.openStream();
                }
            };
            return (InputStream) AccessController.doPrivileged(anonymousClass7);
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }
}
