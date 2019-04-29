package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* compiled from: DataHandler */
class DataHandlerDataSource implements DataSource {
    DataHandler dataHandler = null;

    public DataHandlerDataSource(DataHandler dataHandler) {
        DataHandler dataHandler2 = dataHandler;
        this.dataHandler = dataHandler2;
    }

    public InputStream getInputStream() throws IOException {
        return this.dataHandler.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return this.dataHandler.getOutputStream();
    }

    public String getContentType() {
        return this.dataHandler.getContentType();
    }

    public String getName() {
        return this.dataHandler.getName();
    }
}
