package javax.activation;

import com.sun.activation.registries.LogSupport;
import com.sun.activation.registries.MailcapFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MailcapCommandMap extends CommandMap {
    private static final int PROG = 0;
    private static MailcapFile defDB = null;
    private MailcapFile[] DB;

    public MailcapCommandMap() {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        MailcapFile loadFile;
        SecurityException securityException;
        ArrayList arrayList = r8;
        ArrayList arrayList2 = new ArrayList(5);
        ArrayList arrayList3 = arrayList;
        Object obj = null;
        boolean add = arrayList3.add(null);
        LogSupport.log("MailcapCommandMap: load HOME");
        try {
            String property = System.getProperty("user.home");
            if (property != null) {
                stringBuilder = r8;
                stringBuilder2 = new StringBuilder(String.valueOf(property));
                loadFile = loadFile(stringBuilder.append(File.separator).append(".mailcap").toString());
                if (loadFile != null) {
                    add = arrayList3.add(loadFile);
                }
            }
        } catch (SecurityException e) {
            securityException = e;
        }
        LogSupport.log("MailcapCommandMap: load SYS");
        try {
            stringBuilder = r8;
            stringBuilder2 = new StringBuilder(String.valueOf(System.getProperty("java.home")));
            loadFile = loadFile(stringBuilder.append(File.separator).append("lib").append(File.separator).append("mailcap").toString());
            if (loadFile != null) {
                add = arrayList3.add(loadFile);
            }
        } catch (SecurityException e2) {
            securityException = e2;
        }
        LogSupport.log("MailcapCommandMap: load JAR");
        loadAllResources(arrayList3, "mailcap");
        LogSupport.log("MailcapCommandMap: load DEF");
        Class cls = MailcapCommandMap.class;
        Class cls2 = cls;
        synchronized (cls) {
            try {
                MailcapCommandMap mailcapCommandMap = defDB;
                if (mailcapCommandMap == null) {
                    defDB = mailcapCommandMap.loadResource("mailcap.default");
                }
            } finally {
                cls2 = 
/*
Method generation error in method: javax.activation.MailcapCommandMap.<init>():void, dex: classes.dex
jadx.core.utils.exceptions.CodegenException: Error generate insn: ?: MERGE  (r3_6 'cls2' java.lang.Class) = (r3_5 'cls2' java.lang.Class), (r0_0 'this' java.lang.Class A:{THIS}) in method: javax.activation.MailcapCommandMap.<init>():void, dex: classes.dex
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

    private MailcapFile loadResource(String str) {
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
                MailcapFile mailcapFile = r10;
                MailcapFile mailcapFile2 = new MailcapFile(inputStream);
                MailcapFile mailcapFile3 = mailcapFile;
                if (LogSupport.isLoggable()) {
                    stringBuilder = r10;
                    stringBuilder2 = new StringBuilder("MailcapCommandMap: successfully loaded mailcap file: ");
                    LogSupport.log(stringBuilder.append(str2).toString());
                }
                MailcapFile mailcapFile4 = mailcapFile3;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                        iOException = e2;
                    }
                }
                return mailcapFile4;
            }
            if (LogSupport.isLoggable()) {
                stringBuilder = r10;
                stringBuilder2 = new StringBuilder("MailcapCommandMap: not loading mailcap file: ");
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
                stringBuilder2 = new StringBuilder("MailcapCommandMap: can't load ");
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
                stringBuilder2 = new StringBuilder("MailcapCommandMap: can't load ");
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

    private void loadAllResources(List list, String str) {
        StringBuilder stringBuilder;
        boolean add;
        IOException iOException;
        Throwable th;
        Throwable e;
        List list2 = list;
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
                    LogSupport.log("MailcapCommandMap: getResources");
                }
                for (URL url : resources) {
                    InputStream inputStream = null;
                    if (LogSupport.isLoggable()) {
                        stringBuilder2 = r16;
                        stringBuilder = new StringBuilder("MailcapCommandMap: URL ");
                        LogSupport.log(stringBuilder2.append(url).toString());
                    }
                    try {
                        inputStream = SecuritySupport.openStream(url);
                        if (inputStream != null) {
                            List list3 = list2;
                            MailcapFile mailcapFile = r16;
                            MailcapFile mailcapFile2 = new MailcapFile(inputStream);
                            add = list3.add(mailcapFile);
                            obj = 1;
                            if (LogSupport.isLoggable()) {
                                stringBuilder2 = r16;
                                stringBuilder = new StringBuilder("MailcapCommandMap: successfully loaded mailcap file from URL: ");
                                LogSupport.log(stringBuilder2.append(url).toString());
                            }
                        } else if (LogSupport.isLoggable()) {
                            stringBuilder2 = r16;
                            stringBuilder = new StringBuilder("MailcapCommandMap: not loading mailcap file from URL: ");
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
                            stringBuilder = new StringBuilder("MailcapCommandMap: can't load ");
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
                            stringBuilder = new StringBuilder("MailcapCommandMap: can't load ");
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
                stringBuilder = new StringBuilder("MailcapCommandMap: can't load ");
                LogSupport.log(stringBuilder2.append(str2).toString(), th3);
            }
        }
        if (obj == null) {
            if (LogSupport.isLoggable()) {
                LogSupport.log("MailcapCommandMap: !anyLoaded");
            }
            stringBuilder = r16;
            StringBuilder stringBuilder3 = new StringBuilder("/");
            MailcapFile loadResource = loadResource(stringBuilder.append(str2).toString());
            if (loadResource != null) {
                add = list2.add(loadResource);
            }
        }
    }

    private MailcapFile loadFile(String str) {
        MailcapFile mailcapFile = null;
        try {
            MailcapFile mailcapFile2 = r7;
            MailcapFile mailcapFile3 = new MailcapFile(str);
            mailcapFile = mailcapFile2;
        } catch (IOException e) {
            IOException iOException = e;
        }
        return mailcapFile;
    }

    public MailcapCommandMap(String str) throws IOException {
        String str2 = str;
        this();
        if (LogSupport.isLoggable()) {
            StringBuilder stringBuilder = r7;
            StringBuilder stringBuilder2 = new StringBuilder("MailcapCommandMap: load PROG from ");
            LogSupport.log(stringBuilder.append(str2).toString());
        }
        if (this.DB[0] == null) {
            MailcapFile[] mailcapFileArr = this.DB;
            MailcapFile mailcapFile = r7;
            MailcapFile mailcapFile2 = new MailcapFile(str2);
            mailcapFileArr[0] = mailcapFile;
        }
    }

    public MailcapCommandMap(InputStream inputStream) {
        InputStream inputStream2 = inputStream;
        this();
        LogSupport.log("MailcapCommandMap: load PROG");
        if (this.DB[0] == null) {
            try {
                MailcapFile[] mailcapFileArr = this.DB;
                MailcapFile mailcapFile = r8;
                MailcapFile mailcapFile2 = new MailcapFile(inputStream2);
                mailcapFileArr[0] = mailcapFile;
            } catch (IOException e) {
                IOException iOException = e;
            }
        }
    }

    public synchronized CommandInfo[] getPreferredCommands(String str) {
        CommandInfo[] commandInfoArr;
        String str2 = str;
        synchronized (this) {
            int i;
            Map mailcapList;
            ArrayList arrayList = r9;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            if (str2 != null) {
                str2 = str2.toLowerCase(Locale.ENGLISH);
            }
            for (i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    mailcapList = this.DB[i].getMailcapList(str2);
                    if (mailcapList != null) {
                        appendPrefCmdsToList(mailcapList, arrayList3);
                    }
                }
            }
            for (i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    mailcapList = this.DB[i].getMailcapFallbackList(str2);
                    if (mailcapList != null) {
                        appendPrefCmdsToList(mailcapList, arrayList3);
                    }
                }
            }
            commandInfoArr = (CommandInfo[]) arrayList3.toArray(new CommandInfo[arrayList3.size()]);
        }
        return commandInfoArr;
    }

    private void appendPrefCmdsToList(Map map, List list) {
        Map map2 = map;
        List list2 = list;
        for (String str : map2.keySet()) {
            if (!checkForVerb(list2, str)) {
                List list3 = list2;
                CommandInfo commandInfo = r12;
                CommandInfo commandInfo2 = new CommandInfo(str, (String) ((List) map2.get(str)).get(0));
                boolean add = list3.add(commandInfo);
            }
        }
    }

    private boolean checkForVerb(List list, String str) {
        String str2 = str;
        for (CommandInfo commandName : list) {
            if (commandName.getCommandName().equals(str2)) {
                return true;
            }
        }
        return false;
    }

    public synchronized CommandInfo[] getAllCommands(String str) {
        CommandInfo[] commandInfoArr;
        String str2 = str;
        synchronized (this) {
            int i;
            Map mailcapList;
            ArrayList arrayList = r9;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            if (str2 != null) {
                str2 = str2.toLowerCase(Locale.ENGLISH);
            }
            for (i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    mailcapList = this.DB[i].getMailcapList(str2);
                    if (mailcapList != null) {
                        appendCmdsToList(mailcapList, arrayList3);
                    }
                }
            }
            for (i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    mailcapList = this.DB[i].getMailcapFallbackList(str2);
                    if (mailcapList != null) {
                        appendCmdsToList(mailcapList, arrayList3);
                    }
                }
            }
            commandInfoArr = (CommandInfo[]) arrayList3.toArray(new CommandInfo[arrayList3.size()]);
        }
        return commandInfoArr;
    }

    private void appendCmdsToList(Map map, List list) {
        Map map2 = map;
        List list2 = list;
        for (String str : map2.keySet()) {
            for (String commandInfo : (List) map2.get(str)) {
                List list3 = list2;
                CommandInfo commandInfo2 = r13;
                CommandInfo commandInfo3 = new CommandInfo(str, commandInfo);
                boolean add = list3.add(commandInfo2);
            }
        }
    }

    public synchronized CommandInfo getCommand(String str, String str2) {
        CommandInfo commandInfo;
        String str3 = str;
        String str4 = str2;
        synchronized (this) {
            int i;
            Map mailcapList;
            List list;
            String str5;
            CommandInfo commandInfo2;
            CommandInfo commandInfo3;
            if (str3 != null) {
                str3 = str3.toLowerCase(Locale.ENGLISH);
            }
            for (i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    mailcapList = this.DB[i].getMailcapList(str3);
                    if (mailcapList != null) {
                        list = (List) mailcapList.get(str4);
                        if (list != null) {
                            str5 = (String) list.get(0);
                            if (str5 != null) {
                                commandInfo2 = r12;
                                commandInfo3 = new CommandInfo(str4, str5);
                                commandInfo = commandInfo2;
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
            }
            for (i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    mailcapList = this.DB[i].getMailcapFallbackList(str3);
                    if (mailcapList != null) {
                        list = (List) mailcapList.get(str4);
                        if (list != null) {
                            str5 = (String) list.get(0);
                            if (str5 != null) {
                                commandInfo2 = r12;
                                commandInfo3 = new CommandInfo(str4, str5);
                                commandInfo = commandInfo2;
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
            }
            commandInfo = null;
        }
        return commandInfo;
    }

    public synchronized void addMailcap(String str) {
        String str2 = str;
        synchronized (this) {
            LogSupport.log("MailcapCommandMap: add to PROG");
            if (this.DB[0] == null) {
                MailcapFile[] mailcapFileArr = this.DB;
                MailcapFile mailcapFile = r7;
                MailcapFile mailcapFile2 = new MailcapFile();
                mailcapFileArr[0] = mailcapFile;
            }
            this.DB[0].appendToMailcap(str2);
        }
    }

    public synchronized DataContentHandler createDataContentHandler(String str) {
        DataContentHandler dataContentHandler;
        String str2 = str;
        synchronized (this) {
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            int i;
            Map mailcapList;
            List list;
            DataContentHandler dataContentHandler2;
            if (LogSupport.isLoggable()) {
                stringBuilder = r11;
                stringBuilder2 = new StringBuilder("MailcapCommandMap: createDataContentHandler for ");
                LogSupport.log(stringBuilder.append(str2).toString());
            }
            if (str2 != null) {
                str2 = str2.toLowerCase(Locale.ENGLISH);
            }
            for (i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    if (LogSupport.isLoggable()) {
                        stringBuilder = r11;
                        stringBuilder2 = new StringBuilder("  search DB #");
                        LogSupport.log(stringBuilder.append(i).toString());
                    }
                    mailcapList = this.DB[i].getMailcapList(str2);
                    if (mailcapList != null) {
                        list = (List) mailcapList.get("content-handler");
                        if (list != null) {
                            dataContentHandler2 = getDataContentHandler((String) list.get(0));
                            if (dataContentHandler2 != null) {
                                dataContentHandler = dataContentHandler2;
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
            }
            for (i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    if (LogSupport.isLoggable()) {
                        stringBuilder = r11;
                        stringBuilder2 = new StringBuilder("  search fallback DB #");
                        LogSupport.log(stringBuilder.append(i).toString());
                    }
                    mailcapList = this.DB[i].getMailcapFallbackList(str2);
                    if (mailcapList != null) {
                        list = (List) mailcapList.get("content-handler");
                        if (list != null) {
                            dataContentHandler2 = getDataContentHandler((String) list.get(0));
                            if (dataContentHandler2 != null) {
                                dataContentHandler = dataContentHandler2;
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
            }
            dataContentHandler = null;
        }
        return dataContentHandler;
    }

    private DataContentHandler getDataContentHandler(String str) {
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        Throwable th;
        String str2 = str;
        if (LogSupport.isLoggable()) {
            LogSupport.log("    got content-handler");
        }
        if (LogSupport.isLoggable()) {
            stringBuilder = r8;
            stringBuilder2 = new StringBuilder("      class ");
            LogSupport.log(stringBuilder.append(str2).toString());
        }
        Object obj = null;
        try {
            Class loadClass;
            ClassLoader contextClassLoader = SecuritySupport.getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = getClass().getClassLoader();
            }
            Object obj2 = null;
            try {
                loadClass = contextClassLoader.loadClass(str2);
            } catch (Exception e) {
                Exception exception = e;
                loadClass = Class.forName(str2);
            }
            if (loadClass != null) {
                return (DataContentHandler) loadClass.newInstance();
            }
        } catch (IllegalAccessException e2) {
            th = e2;
            if (LogSupport.isLoggable()) {
                stringBuilder = r8;
                stringBuilder2 = new StringBuilder("Can't load DCH ");
                LogSupport.log(stringBuilder.append(str2).toString(), th);
            }
        } catch (ClassNotFoundException e22) {
            th = e22;
            if (LogSupport.isLoggable()) {
                stringBuilder = r8;
                stringBuilder2 = new StringBuilder("Can't load DCH ");
                LogSupport.log(stringBuilder.append(str2).toString(), th);
            }
        } catch (InstantiationException e222) {
            th = e222;
            if (LogSupport.isLoggable()) {
                stringBuilder = r8;
                stringBuilder2 = new StringBuilder("Can't load DCH ");
                LogSupport.log(stringBuilder.append(str2).toString(), th);
            }
        }
        return null;
    }

    public synchronized String[] getMimeTypes() {
        String[] strArr;
        synchronized (this) {
            ArrayList arrayList = r9;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            for (int i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    String[] mimeTypes = this.DB[i].getMimeTypes();
                    if (mimeTypes != null) {
                        for (int i2 = 0; i2 < mimeTypes.length; i2++) {
                            if (!arrayList3.contains(mimeTypes[i2])) {
                                boolean add = arrayList3.add(mimeTypes[i2]);
                            }
                        }
                    }
                }
            }
            strArr = (String[]) arrayList3.toArray(new String[arrayList3.size()]);
        }
        return strArr;
    }

    public synchronized String[] getNativeCommands(String str) {
        String[] strArr;
        String str2 = str;
        synchronized (this) {
            ArrayList arrayList = r10;
            ArrayList arrayList2 = new ArrayList();
            ArrayList arrayList3 = arrayList;
            if (str2 != null) {
                str2 = str2.toLowerCase(Locale.ENGLISH);
            }
            for (int i = 0; i < this.DB.length; i++) {
                if (this.DB[i] != null) {
                    String[] nativeCommands = this.DB[i].getNativeCommands(str2);
                    if (nativeCommands != null) {
                        for (int i2 = 0; i2 < nativeCommands.length; i2++) {
                            if (!arrayList3.contains(nativeCommands[i2])) {
                                boolean add = arrayList3.add(nativeCommands[i2]);
                            }
                        }
                    }
                }
            }
            strArr = (String[]) arrayList3.toArray(new String[arrayList3.size()]);
        }
        return strArr;
    }
}
