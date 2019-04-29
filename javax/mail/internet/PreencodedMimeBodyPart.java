package javax.mail.internet;

import com.sun.mail.util.LineOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import javax.mail.MessagingException;

public class PreencodedMimeBodyPart extends MimeBodyPart {
    private String encoding;

    public PreencodedMimeBodyPart(String str) {
        this.encoding = str;
    }

    public String getEncoding() throws MessagingException {
        return this.encoding;
    }

    public void writeTo(OutputStream outputStream) throws IOException, MessagingException {
        LineOutputStream lineOutputStream;
        OutputStream outputStream2 = outputStream;
        Object obj = null;
        if (outputStream2 instanceof LineOutputStream) {
            lineOutputStream = (LineOutputStream) outputStream2;
        } else {
            LineOutputStream lineOutputStream2 = r7;
            LineOutputStream lineOutputStream3 = new LineOutputStream(outputStream2);
            lineOutputStream = lineOutputStream2;
        }
        Enumeration allHeaderLines = getAllHeaderLines();
        while (allHeaderLines.hasMoreElements()) {
            lineOutputStream.writeln((String) allHeaderLines.nextElement());
        }
        lineOutputStream.writeln();
        getDataHandler().writeTo(outputStream2);
        outputStream2.flush();
    }

    /* Access modifiers changed, original: protected */
    public void updateHeaders() throws MessagingException {
        super.updateHeaders();
        MimeBodyPart.setEncoding(this, this.encoding);
    }
}
