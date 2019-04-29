package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;

class SecuritySupport {
    private SecuritySupport() {
    }

    public static ClassLoader getContextClassLoader() {
        AnonymousClass1 anonymousClass1 = r2;
        AnonymousClass1 anonymousClass12 = new PrivilegedAction() {
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
        return (ClassLoader) AccessController.doPrivileged(anonymousClass1);
    }

    public static InputStream getResourceAsStream(Class cls, String str) throws IOException {
        try {
            AnonymousClass2 anonymousClass2 = r7;
            final Class cls2 = cls;
            final String str2 = str;
            AnonymousClass2 anonymousClass22 = new PrivilegedExceptionAction() {
                public Object run() throws IOException {
                    return cls2.getResourceAsStream(str2);
                }
            };
            return (InputStream) AccessController.doPrivileged(anonymousClass2);
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }

    public static URL[] getResources(ClassLoader classLoader, String str) {
        AnonymousClass3 anonymousClass3 = r6;
        final ClassLoader classLoader2 = classLoader;
        final String str2 = str;
        AnonymousClass3 anonymousClass32 = new PrivilegedAction() {
            public Object run() {
                Object obj = (URL[]) null;
                try {
                    ArrayList arrayList = r7;
                    ArrayList arrayList2 = new ArrayList();
                    ArrayList arrayList3 = arrayList;
                    Enumeration resources = classLoader2.getResources(str2);
                    while (resources != null && resources.hasMoreElements()) {
                        URL url = (URL) resources.nextElement();
                        if (url != null) {
                            boolean add = arrayList3.add(url);
                        }
                    }
                    if (arrayList3.size() > 0) {
                        obj = (URL[]) arrayList3.toArray(new URL[arrayList3.size()]);
                    }
                } catch (IOException e) {
                    IOException iOException = e;
                } catch (SecurityException e2) {
                    SecurityException securityException = e2;
                }
                return obj;
            }
        };
        return (URL[]) AccessController.doPrivileged(anonymousClass3);
    }

    public static URL[] getSystemResources(String str) {
        AnonymousClass4 anonymousClass4 = r4;
        final String str2 = str;
        AnonymousClass4 anonymousClass42 = new PrivilegedAction() {
            public Object run() {
                Object obj = (URL[]) null;
                try {
                    ArrayList arrayList = r7;
                    ArrayList arrayList2 = new ArrayList();
                    ArrayList arrayList3 = arrayList;
                    Enumeration systemResources = ClassLoader.getSystemResources(str2);
                    while (systemResources != null && systemResources.hasMoreElements()) {
                        URL url = (URL) systemResources.nextElement();
                        if (url != null) {
                            boolean add = arrayList3.add(url);
                        }
                    }
                    if (arrayList3.size() > 0) {
                        obj = (URL[]) arrayList3.toArray(new URL[arrayList3.size()]);
                    }
                } catch (IOException e) {
                    IOException iOException = e;
                } catch (SecurityException e2) {
                    SecurityException securityException = e2;
                }
                return obj;
            }
        };
        return (URL[]) AccessController.doPrivileged(anonymousClass4);
    }

    public static InputStream openStream(URL url) throws IOException {
        try {
            AnonymousClass5 anonymousClass5 = r5;
            final URL url2 = url;
            AnonymousClass5 anonymousClass52 = new PrivilegedExceptionAction() {
                public Object run() throws IOException {
                    return url2.openStream();
                }
            };
            return (InputStream) AccessController.doPrivileged(anonymousClass5);
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        }
    }
}
