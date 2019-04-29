package javax.activation;

import java.io.File;

public abstract class FileTypeMap {
    private static FileTypeMap defaultMap = null;

    public abstract String getContentType(File file);

    public abstract String getContentType(String str);

    public FileTypeMap() {
    }

    public static void setDefaultFileTypeMap(FileTypeMap fileTypeMap) {
        FileTypeMap fileTypeMap2 = fileTypeMap;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkSetFactory();
            } catch (SecurityException e) {
                SecurityException securityException = e;
                if (FileTypeMap.class.getClassLoader() != fileTypeMap2.getClass().getClassLoader()) {
                    throw securityException;
                }
            }
        }
        defaultMap = fileTypeMap2;
    }

    public static FileTypeMap getDefaultFileTypeMap() {
        if (defaultMap == null) {
            FileTypeMap fileTypeMap = r2;
            FileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
            defaultMap = fileTypeMap;
        }
        return defaultMap;
    }
}
