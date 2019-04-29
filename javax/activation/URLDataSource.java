package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class URLDataSource implements DataSource {
    private URL url = null;
    private URLConnection url_conn = null;

    public URLDataSource(URL url) {
        URL url2 = url;
        this.url = url2;
    }

    public String getContentType() {
        String str = null;
        try {
            if (this.url_conn == null) {
                this.url_conn = this.url.openConnection();
            }
        } catch (IOException e) {
            IOException iOException = e;
        }
        if (this.url_conn != null) {
            str = this.url_conn.getContentType();
        }
        if (str == null) {
            str = "application/octet-stream";
        }
        return str;
    }

    public String getName() {
        return this.url.getFile();
    }

    public InputStream getInputStream() throws IOException {
        return this.url.openStream();
    }

    public OutputStream getOutputStream() throws IOException {
        this.url_conn = this.url.openConnection();
        if (this.url_conn == null) {
            return null;
        }
        this.url_conn.setDoOutput(true);
        return this.url_conn.getOutputStream();
    }

    public URL getURL() {
        return this.url;
    }
}
