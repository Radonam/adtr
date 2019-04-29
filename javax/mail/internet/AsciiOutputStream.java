package javax.mail.internet;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;

/* compiled from: MimeUtility */
class AsciiOutputStream extends OutputStream {
    private int ascii = 0;
    private boolean badEOL = false;
    private boolean breakOnNonAscii;
    private boolean checkEOL = false;
    private int lastb = 0;
    private int linelen = 0;
    private boolean longLine = false;
    private int non_ascii = 0;
    private int ret = 0;

    public AsciiOutputStream(boolean z, boolean z2) {
        boolean z3 = z;
        boolean z4 = z2;
        this.breakOnNonAscii = z3;
        boolean z5 = z4 && z3;
        this.checkEOL = z5;
    }

    public void write(int i) throws IOException {
        check(i);
    }

    public void write(byte[] bArr) throws IOException {
        byte[] bArr2 = bArr;
        write(bArr2, 0, bArr2.length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        byte[] bArr2 = bArr;
        int i3 = i;
        int i4 = i2 + i3;
        for (int i5 = i3; i5 < i4; i5++) {
            check(bArr2[i5]);
        }
    }

    private final void check(int i) throws IOException {
        int i2 = i & 255;
        if (this.checkEOL && ((this.lastb == 13 && i2 != 10) || (this.lastb != 13 && i2 == 10))) {
            this.badEOL = true;
        }
        if (i2 == 13 || i2 == 10) {
            this.linelen = 0;
        } else {
            this.linelen++;
            if (this.linelen > 998) {
                this.longLine = true;
            }
        }
        if (MimeUtility.nonascii(i2)) {
            this.non_ascii++;
            if (this.breakOnNonAscii) {
                this.ret = 3;
                EOFException eOFException = r5;
                EOFException eOFException2 = new EOFException();
                throw eOFException;
            }
        }
        this.ascii++;
        this.lastb = i2;
    }

    public int getAscii() {
        if (this.ret != 0) {
            return this.ret;
        }
        if (this.badEOL) {
            return 3;
        }
        if (this.non_ascii == 0) {
            if (this.longLine) {
                return 2;
            }
            return 1;
        } else if (this.ascii > this.non_ascii) {
            return 2;
        } else {
            return 3;
        }
    }
}
