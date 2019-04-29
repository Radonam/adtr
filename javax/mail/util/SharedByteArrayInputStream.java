package javax.mail.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.mail.internet.SharedInputStream;

public class SharedByteArrayInputStream extends ByteArrayInputStream implements SharedInputStream {
    protected int start = 0;

    public SharedByteArrayInputStream(byte[] bArr) {
        super(bArr);
    }

    public SharedByteArrayInputStream(byte[] bArr, int i, int i2) {
        int i3 = i;
        super(bArr, i3, i2);
        this.start = i3;
    }

    public long getPosition() {
        return (long) (this.pos - this.start);
    }

    public InputStream newStream(long j, long j2) {
        long j3 = j;
        long j4 = j2;
        if (j3 < 0) {
            IllegalArgumentException illegalArgumentException = r13;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("start < 0");
            throw illegalArgumentException;
        }
        if (j4 == -1) {
            j4 = (long) (this.count - this.start);
        }
        InputStream inputStream = r13;
        InputStream sharedByteArrayInputStream = new SharedByteArrayInputStream(this.buf, this.start + ((int) j3), (int) (j4 - j3));
        return inputStream;
    }
}
