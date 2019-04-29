package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MimeTypeFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

public class MimetypesFileTypeMap extends FileTypeMap {
    private static final int PROG = 0;
    private static MimeTypeFile defDB = null;
    private static String defaultType = "application/octet-stream";
    private MimeTypeFile[] DB;

    public MimetypesFileTypeMap() {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        MimeTypeFile loadFile;
        SecurityException securityException;
        Vector vector = r8;
        Vector vector2 = new Vector(5);
        Vector vector3 = vector;
        Object obj = null;
        vector3.addElement(null);
        LogSupport.log("MimetypesFileTypeMap: load HOME");
        try {
            String property = System.getProperty("user.home");
            if (property != null) {
                stringBuilder = r8;
                stringBuilder2 = new StringBuilder(String.valueOf(property));
                loadFile = loadFile(stringBuilder.append(File.separator).append(".mime.types").toString());
                if (loadFile != null) {
                    vector3.addElement(loadFile);
                }
            }
        } catch (SecurityException e) {
            securityException = e;
        }
        LogSupport.log("MimetypesFileTypeMap: load SYS");
        try {
            stringBuilder = r8;
            stringBuilder2 = new StringBuilder(String.valueOf(System.getProperty("java.home")));
            loadFile = loadFile(stringBuilder.append(File.separator).append("lib").append(File.separator).append("mime.types").toString());
            if (loadFile != null) {
                vector3.addElement(loadFile);
            }
        } catch (SecurityException e2) {
            securityException = e2;
        }
        LogSupport.log("MimetypesFileTypeMap: load JAR");
        loadAllResources(vector3, "mime.types");
        LogSupport.log("MimetypesFileTypeMap: load DEF");
        Class cls = MimetypesFileTypeMap.class;
        Class cls2 = cls;
        synchronized (cls) {
            try {
                MimetypesFileTypeMap mimetypesFileTypeMap = defDB;
                if (mimetypesFileTypeMap == null) {
                    defDB = mimetypesFileTypeMap.loadResource("/mimetypes.default");
                }
            } finally {
                cls2 = 
/*
Method generation error in method: javax.activation.MimetypesFileTypeMap.<init>():void, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r3_6 'cls2' java.lang.Class) = (r3_5 'cls2' java.lang.Class), (r0_0 'this' java.lang.Class A:{THIS}) in method: javax.activation.MimetypesFileTypeMap.<init>():void, dex: classes.dex
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

    private MimeTypeFile loadResource(String str) {
        IOException iOException;
        Throwable th;
        Throwable e;
        String str2 = str;
        InputStream inputStream = null;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        try {
            inputStream = SecuritySupport.getResourceAsStream(getClass(), str2);
            if (inputStream != null) {
                MimeTypeFile mimeTypeFile = r10;
                MimeTypeFile mimeTypeFile2 = new MimeTypeFile(inputStream);
                MimeTypeFile mimeTypeFile3 = mimeTypeFile;
                if (LogSupport.isLoggable()) {
                    stringBuilder = r10;
                    stringBuilder2 = new StringBuilder("MimetypesFileTypeMap: successfully loaded mime types file: ");
                    LogSupport.log(stringBuilder.append(str2).toString());
                }
                MimeTypeFile mimeTypeFile4 = mimeTypeFile3;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                        iOException = e2;
                    }
                }
                return mimeTypeFile4;
            }
            if (LogSupport.isLoggable()) {
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder("MimetypesFileTypeMap: not loading mime types file: ");
                LogSupport.log(stringBuilder.append(str2).toString());
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e22) {
                    iOException = e22;
                }
            }
            return null;
        } catch (IOException e3) {
            th = e3;
            if (LogSupport.isLoggable()) {
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder("MimetypesFileTypeMap: can't load ");
                LogSupport.log(stringBuilder.append(str2).toString(), th);
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e222) {
                    iOException = e222;
                }
            }
        } catch (SecurityException e32) {
            th = e32;
            if (LogSupport.isLoggable()) {
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder("MimetypesFileTypeMap: can't load ");
                LogSupport.log(stringBuilder.append(str2).toString(), th);
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2222) {
                    iOException = e2222;
                }
            }
        } catch (Throwable e322) {
            Throwable th2 = e322;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e22222) {
                    iOException = e22222;
                }
            }
            e322 = th2;
        }
    }

    private void loadAllResources(Vector vector, String str) {
        StringBuilder stringBuilder;
        IOException iOException;
        Throwable th;
        Throwable e;
        Vector vector2 = vector;
        String str2 = str;
        Object obj = null;
        Object obj2 = null;
        StringBuilder stringBuilder2;
        try {
            URL[] resources;
            ClassLoader contextClassLoader = SecuritySupport.getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = getClass().getClassLoader();
            }
            if (contextClassLoader != null) {
                resources = SecuritySupport.getResources(contextClassLoader, str2);
            } else {
                resources = SecuritySupport.getSystemResources(str2);
            }
            if (resources != null) {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("MimetypesFileTypeMap: getResources");
                }
                for (URL url : resources) {
                    InputStream inputStream = null;
                    if (LogSupport.isLoggable()) {
                        stringBuilder2 = r16;
                        stringBuilder = new StringBuilder("MimetypesFileTypeMap: URL ");
                        LogSupport.log(stringBuilder2.append(url).toString());
                    }
                    try {
                        inputStream = SecuritySupport.openStream(url);
                        if (inputStream != null) {
                            Vector vector3 = vector2;
                            MimeTypeFile mimeTypeFile = r16;
                            MimeTypeFile mimeTypeFile2 = new MimeTypeFile(inputStream);
                            vector3.addElement(mimeTypeFile);
                            obj = 1;
                            if (LogSupport.isLoggable()) {
                                stringBuilder2 = r16;
                                stringBuilder = new StringBuilder("MimetypesFileTypeMap: successfully loaded mime types from URL: ");
                                LogSupport.log(stringBuilder2.append(url).toString());
                            }
                        } else if (LogSupport.isLoggable()) {
                            stringBuilder2 = r16;
                            stringBuilder = new StringBuilder("MimetypesFileTypeMap: not loading mime types from URL: ");
                            LogSupport.log(stringBuilder2.append(url).toString());
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e2) {
                                iOException = e2;
                            }
                        }
                    } catch (IOException e3) {
                        th = e3;
                        if (LogSupport.isLoggable()) {
                            stringBuilder2 = r16;
                            stringBuilder = new StringBuilder("MimetypesFileTypeMap: can't load ");
                            LogSupport.log(stringBuilder2.append(url).toString(), th);
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e22) {
                                iOException = e22;
                            }
                        }
                    } catch (SecurityException e32) {
                        th = e32;
                        if (LogSupport.isLoggable()) {
                            stringBuilder2 = r16;
                            stringBuilder = new StringBuilder("MimetypesFileTypeMap: can't load ");
                            LogSupport.log(stringBuilder2.append(url).toString(), th);
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e222) {
                                iOException = e222;
                            }
                        }
                    } catch (Throwable e322) {
                        Throwable th2 = e322;
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e2222) {
                                iOException = e2222;
                            }
                        }
                        e322 = th2;
                    }
                }
            }
        } catch (Exception e3222) {
            Throwable th3 = e3222;
            if (LogSupport.isLoggable()) {
                stringBuilder2 = r16;
                stringBuilder = new StringBuilder("MimetypesFileTypeMap: can't load ");
                LogSupport.log(stringBuilder2.append(str2).toString(), th3);
            }
        }
        if (obj == null) {
            LogSupport.log("MimetypesFileTypeMap: !anyLoaded");
            stringBuilder = r16;
            StringBuilder stringBuilder3 = new StringBuilder("/");
            MimeTypeFile loadResource = loadResource(stringBuilder.append(str2).toString());
            if (loadResource != null) {
                vector2.addElement(loadResource);
            }
        }
    }

    private MimeTypeFile loadFile(String str) {
        MimeTypeFile mimeTypeFile = null;
        try {
            MimeTypeFile mimeTypeFile2 = r7;
            MimeTypeFile mimeTypeFile3 = new MimeTypeFile(str);
            mimeTypeFile = mimeTypeFile2;
        } catch (IOException e) {
            IOException iOException = e;
        }
        return mimeTypeFile;
    }

    public MimetypesFileTypeMap(String str) throws IOException {
        String str2 = str;
        this();
        MimeTypeFile[] mimeTypeFileArr = this.DB;
        MimeTypeFile mimeTypeFile = r7;
        MimeTypeFile mimeTypeFile2 = new MimeTypeFile(str2);
        mimeTypeFileArr[0] = mimeTypeFile;
    }

    public MimetypesFileTypeMap(InputStream inputStream) {
        InputStream inputStream2 = inputStream;
        this();
        try {
            MimeTypeFile[] mimeTypeFileArr = this.DB;
            MimeTypeFile mimeTypeFile = r8;
            MimeTypeFile mimeTypeFile2 = new MimeTypeFile(inputStream2);
            mimeTypeFileArr[0] = mimeTypeFile;
        } catch (IOException e) {
            IOException iOException = e;
        }
    }

    public synchronized void addMimeTypes(String str) {
        String str2 = str;
        synchronized (this) {
            if (this.DB[0] == null) {
                MimeTypeFile[] mimeTypeFileArr = this.DB;
                MimeTypeFile mimeTypeFile = r7;
                MimeTypeFile mimeTypeFile2 = new MimeTypeFile();
                mimeTypeFileArr[0] = mimeTypeFile;
            }
            this.DB[0].appendToRegistry(str2);
        }
    }

    public String getContentType(File file) {
        return getContentType(file.getName());
    }

    public synchronized String getContentType(String str) {
        String str2;
        String str3 = str;
        synchronized (this) {
            int lastIndexOf = str3.lastIndexOf(".");
            if (lastIndexOf < 0) {
                str2 = defaultType;
            } else {
                String substring = str3.substring(lastIndexOf + 1);
                if (substring.length() == 0) {
                    str2 = defaultType;
                } else {
                    for (int i = 0; i < this.DB.length; i++) {
                        if (this.DB[i] != null) {
                            String mIMETypeString = this.DB[i].getMIMETypeString(substring);
                            if (mIMETypeString != null) {
                                str2 = mIMETypeString;
                                break;
                            }
                        }
                    }
                    str2 = defaultType;
                }
            }
        }
        return str2;
    }
}
