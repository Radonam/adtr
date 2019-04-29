package javax.mail.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import javax.mail.internet.SharedInputStream;

public class SharedFileInputStream extends BufferedInputStream implements SharedInputStream {
    private static int defaultBufferSize = 2048;
    protected long bufpos;
    protected int bufsize;
    protected long datalen;
    protected RandomAccessFile in;
    private boolean master;
    private SharedFile sf;
    protected long start;

    static class SharedFile {
        private int cnt;
        private RandomAccessFile in;

        SharedFile(String str) throws IOException {
            RandomAccessFile randomAccessFile = r7;
            RandomAccessFile randomAccessFile2 = new RandomAccessFile(str, "r");
            this.in = randomAccessFile;
        }

        SharedFile(File file) throws IOException {
            RandomAccessFile randomAccessFile = r7;
            RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "r");
            this.in = randomAccessFile;
        }

        public RandomAccessFile open() {
            this.cnt++;
            return this.in;
        }

        public synchronized void close() throws IOException {
            synchronized (this) {
                if (this.cnt > 0) {
                    int i = this.cnt - 1;
                    int i2 = i;
                    this.cnt = i;
                    if (i2 <= 0) {
                        this.in.close();
                    }
                }
            }
        }

        public synchronized void forceClose() throws IOException {
            synchronized (this) {
                if (this.cnt > 0) {
                    this.cnt = 0;
                    this.in.close();
                } else {
                    try {
                        this.in.close();
                    } catch (IOException e) {
                        IOException iOException = e;
                    }
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            super.finalize();
            this.in.close();
        }
    }

    private void ensureOpen() throws IOException {
        if (this.in == null) {
            IOException iOException = r4;
            IOException iOException2 = new IOException("Stream closed");
            throw iOException;
        }
    }

    public SharedFileInputStream(File file) throws IOException {
        this(file, defaultBufferSize);
    }

    public SharedFileInputStream(String str) throws IOException {
        this(str, defaultBufferSize);
    }

    public SharedFileInputStream(File file, int i) throws IOException {
        File file2 = file;
        int i2 = i;
        super(null);
        this.start = 0;
        this.master = true;
        if (i2 <= 0) {
            IllegalArgumentException illegalArgumentException = r7;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("Buffer size <= 0");
            throw illegalArgumentException;
        }
        SharedFile sharedFile = r7;
        SharedFile sharedFile2 = new SharedFile(file2);
        init(sharedFile, i2);
    }

    public SharedFileInputStream(String str, int i) throws IOException {
        String str2 = str;
        int i2 = i;
        super(null);
        this.start = 0;
        this.master = true;
        if (i2 <= 0) {
            IllegalArgumentException illegalArgumentException = r7;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("Buffer size <= 0");
            throw illegalArgumentException;
        }
        SharedFile sharedFile = r7;
        SharedFile sharedFile2 = new SharedFile(str2);
        init(sharedFile, i2);
    }

    private void init(SharedFile sharedFile, int i) throws IOException {
        SharedFile sharedFile2 = sharedFile;
        int i2 = i;
        this.sf = sharedFile2;
        this.in = sharedFile2.open();
        this.start = 0;
        this.datalen = this.in.length();
        this.bufsize = i2;
        this.buf = new byte[i2];
    }

    private SharedFileInputStream(SharedFile sharedFile, long j, long j2, int i) {
        SharedFile sharedFile2 = sharedFile;
        long j3 = j;
        long j4 = j2;
        int i2 = i;
        super(null);
        this.start = 0;
        this.master = true;
        this.master = false;
        this.sf = sharedFile2;
        this.in = sharedFile2.open();
        this.start = j3;
        this.bufpos = j3;
        this.datalen = j4;
        this.bufsize = i2;
        this.buf = new byte[i2];
    }

    private void fill() throws IOException {
        int i;
        if (this.markpos < 0) {
            this.pos = 0;
            this.bufpos += (long) this.count;
        } else if (this.pos >= this.buf.length) {
            if (this.markpos > 0) {
                i = this.pos - this.markpos;
                System.arraycopy(this.buf, this.markpos, this.buf, 0, i);
                this.pos = i;
                this.bufpos += (long) this.markpos;
                this.markpos = 0;
            } else if (this.buf.length >= this.marklimit) {
                this.markpos = -1;
                this.pos = 0;
                this.bufpos += (long) this.count;
            } else {
                i = this.pos * 2;
                if (i > this.marklimit) {
                    i = this.marklimit;
                }
                Object obj = new byte[i];
                System.arraycopy(this.buf, 0, obj, 0, this.pos);
                this.buf = obj;
            }
        }
        this.count = this.pos;
        this.in.seek(this.bufpos + ((long) this.pos));
        i = this.buf.length - this.pos;
        if (((this.bufpos - this.start) + ((long) this.pos)) + ((long) i) > this.datalen) {
            i = (int) (this.datalen - ((this.bufpos - this.start) + ((long) this.pos)));
        }
        int read = this.in.read(this.buf, this.pos, i);
        if (read > 0) {
            this.count = read + this.pos;
        }
    }

    public synchronized int read() throws IOException {
        int i;
        synchronized (this) {
            ensureOpen();
            if (this.pos >= this.count) {
                fill();
                if (this.pos >= this.count) {
                    i = -1;
                }
            }
            byte[] bArr = this.buf;
            int i2 = this.pos;
            int i3 = i2;
            this.pos = i2 + 1;
            i = bArr[i3] & 255;
        }
        return i;
    }

    private int read1(byte[] bArr, int i, int i2) throws IOException {
        Object obj = bArr;
        int i3 = i;
        int i4 = i2;
        int i5 = this.count - this.pos;
        if (i5 <= 0) {
            fill();
            i5 = this.count - this.pos;
            if (i5 <= 0) {
                return -1;
            }
        }
        int i6 = i5 < i4 ? i5 : i4;
        System.arraycopy(this.buf, this.pos, obj, i3, i6);
        this.pos += i6;
        return i6;
    }

    public synchronized int read(byte[] bArr, int i, int i2) throws IOException {
        int i3;
        byte[] bArr2 = bArr;
        int i4 = i;
        int i5 = i2;
        synchronized (this) {
            ensureOpen();
            if ((((i4 | i5) | (i4 + i5)) | (bArr2.length - (i4 + i5))) < 0) {
                IndexOutOfBoundsException indexOutOfBoundsException = r12;
                IndexOutOfBoundsException indexOutOfBoundsException2 = new IndexOutOfBoundsException();
                throw indexOutOfBoundsException;
            }
            if (i5 == 0) {
                i3 = 0;
            } else {
                int read1 = read1(bArr2, i4, i5);
                if (read1 <= 0) {
                    i3 = read1;
                } else {
                    while (read1 < i5) {
                        int read12 = read1(bArr2, i4 + read1, i5 - read1);
                        if (read12 <= 0) {
                            break;
                        }
                        read1 += read12;
                    }
                    i3 = read1;
                }
            }
        }
        return i3;
    }

    public synchronized long skip(long j) throws IOException {
        long j2;
        long j3 = j;
        synchronized (this) {
            ensureOpen();
            if (j3 <= 0) {
                j2 = 0;
            } else {
                long j4 = (long) (this.count - this.pos);
                if (j4 <= 0) {
                    fill();
                    j4 = (long) (this.count - this.pos);
                    if (j4 <= 0) {
                        j2 = 0;
                    }
                }
                long j5 = j4 < j3 ? j4 : j3;
                this.pos = (int) (((long) this.pos) + j5);
                j2 = j5;
            }
        }
        return j2;
    }

    public synchronized int available() throws IOException {
        int in_available;
        synchronized (this) {
            ensureOpen();
            in_available = (this.count - this.pos) + in_available();
        }
        return in_available;
    }

    private int in_available() throws IOException {
        return (int) ((this.start + this.datalen) - (this.bufpos + ((long) this.count)));
    }

    public synchronized void mark(int i) {
        int i2 = i;
        synchronized (this) {
            this.marklimit = i2;
            this.markpos = this.pos;
        }
    }

    public synchronized void reset() throws IOException {
        synchronized (this) {
            ensureOpen();
            if (this.markpos < 0) {
                IOException iOException = r5;
                IOException iOException2 = new IOException("Resetting to invalid mark");
                throw iOException;
            }
            this.pos = this.markpos;
        }
    }

    public boolean markSupported() {
        return true;
    }

    public void close() throws IOException {
        if (this.in != null) {
            try {
                if (this.master) {
                    this.sf.forceClose();
                } else {
                    this.sf.close();
                }
                this.sf = null;
                this.in = null;
                this.buf = null;
            } catch (Throwable th) {
                Throwable th2 = th;
                this.sf = null;
                this.in = null;
                this.buf = null;
                Throwable th3 = th2;
            }
        }
    }

    public long getPosition() {
        if (this.in != null) {
            return (this.bufpos + ((long) this.pos)) - this.start;
        }
        RuntimeException runtimeException = r5;
        RuntimeException runtimeException2 = new RuntimeException("Stream closed");
        throw runtimeException;
    }

    public InputStream newStream(long j, long j2) {
        long j3 = j;
        long j4 = j2;
        if (this.in == null) {
            RuntimeException runtimeException = r14;
            RuntimeException runtimeException2 = new RuntimeException("Stream closed");
            throw runtimeException;
        } else if (j3 < 0) {
            IllegalArgumentException illegalArgumentException = r14;
            IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("start < 0");
            throw illegalArgumentException;
        } else {
            if (j4 == -1) {
                j4 = this.datalen;
            }
            InputStream inputStream = r14;
            InputStream sharedFileInputStream = new SharedFileInputStream(this.sf, this.start + ((long) ((int) j3)), (long) ((int) (j4 - j3)), this.bufsize);
            return inputStream;
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
