package javax.activation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileDataSource implements DataSource {
    private File _file;
    private FileTypeMap typeMap;

    public FileDataSource(File file) {
        File file2 = file;
        this._file = null;
        this.typeMap = null;
        this._file = file2;
    }

    public FileDataSource(String str) {
        File file = r6;
        File file2 = new File(str);
        this(file);
    }

    public InputStream getInputStream() throws IOException {
        InputStream inputStream = r4;
        InputStream fileInputStream = new FileInputStream(this._file);
        return inputStream;
    }

    public OutputStream getOutputStream() throws IOException {
        OutputStream outputStream = r4;
        OutputStream fileOutputStream = new FileOutputStream(this._file);
        return outputStream;
    }

    public String getContentType() {
        if (this.typeMap == null) {
            return FileTypeMap.getDefaultFileTypeMap().getContentType(this._file);
        }
        return this.typeMap.getContentType(this._file);
    }

    public String getName() {
        return this._file.getName();
    }

    public File getFile() {
        return this._file;
    }

    public void setFileTypeMap(FileTypeMap fileTypeMap) {
        this.typeMap = fileTypeMap;
    }
}
