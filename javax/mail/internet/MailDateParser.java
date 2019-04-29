package javax.mail.internet;

import com.sun.mail.iap.Response;
import java.text.ParseException;

/* compiled from: MailDateFormat */
class MailDateParser {
    int index = 0;
    char[] orig = null;

    public MailDateParser(char[] cArr) {
        char[] cArr2 = cArr;
        this.orig = cArr2;
    }

    public void skipUntilNumber() throws ParseException {
        while (true) {
            try {
                switch (this.orig[this.index]) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        return;
                    default:
                        this.index++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException = e;
                ParseException parseException = r6;
                ParseException parseException2 = new ParseException("No Number Found", this.index);
                throw parseException;
            }
        }
    }

    public void skipWhiteSpace() {
        int length = this.orig.length;
        while (this.index < length) {
            switch (this.orig[this.index]) {
                case 9:
                case 10:
                case 13:
                case Response.SYNTHETIC /*32*/:
                    this.index++;
                default:
                    return;
            }
        }
    }

    public int peekChar() throws ParseException {
        if (this.index < this.orig.length) {
            return this.orig[this.index];
        }
        ParseException parseException = r5;
        ParseException parseException2 = new ParseException("No more characters", this.index);
        throw parseException;
    }

    public void skipChar(char c) throws ParseException {
        char c2 = c;
        ParseException parseException;
        ParseException parseException2;
        if (this.index >= this.orig.length) {
            parseException = r6;
            parseException2 = new ParseException("No more characters", this.index);
            throw parseException;
        } else if (this.orig[this.index] == c2) {
            this.index++;
        } else {
            parseException = r6;
            parseException2 = new ParseException("Wrong char", this.index);
            throw parseException;
        }
    }

    public boolean skipIfChar(char c) throws ParseException {
        char c2 = c;
        if (this.index >= this.orig.length) {
            ParseException parseException = r6;
            ParseException parseException2 = new ParseException("No more characters", this.index);
            throw parseException;
        } else if (this.orig[this.index] != c2) {
            return false;
        } else {
            this.index++;
            return true;
        }
    }

    public int parseNumber() throws ParseException {
        ParseException parseException;
        ParseException parseException2;
        int length = this.orig.length;
        Object obj = null;
        int i = 0;
        while (this.index < length) {
            int i2;
            switch (this.orig[this.index]) {
                case '0':
                    i2 = i * 10;
                    break;
                case '1':
                    i2 = (i * 10) + 1;
                    break;
                case '2':
                    i2 = (i * 10) + 2;
                    break;
                case '3':
                    i2 = (i * 10) + 3;
                    break;
                case '4':
                    i2 = (i * 10) + 4;
                    break;
                case '5':
                    i2 = (i * 10) + 5;
                    break;
                case '6':
                    i2 = (i * 10) + 6;
                    break;
                case '7':
                    i2 = (i * 10) + 7;
                    break;
                case '8':
                    i2 = (i * 10) + 8;
                    break;
                case '9':
                    i2 = (i * 10) + 9;
                    break;
                default:
                    if (obj != null) {
                        return i;
                    }
                    parseException = r8;
                    parseException2 = new ParseException("No Number found", this.index);
                    throw parseException;
            }
            i = i2;
            int obj2 = 1;
            this.index++;
        }
        if (obj2 != null) {
            return i;
        }
        parseException = r8;
        parseException2 = new ParseException("No Number found", this.index);
        throw parseException;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int parseMonth() throws ParseException {
        try {
            char[] cArr = this.orig;
            int i = this.index;
            int i2 = i;
            this.index = i + 1;
            char c;
            switch (cArr[i2]) {
                case 'A':
                case 'a':
                    cArr = this.orig;
                    i = this.index;
                    i2 = i;
                    this.index = i + 1;
                    c = cArr[i2];
                    if (c == 'P' || c == 'p') {
                        cArr = this.orig;
                        i = this.index;
                        i2 = i;
                        this.index = i + 1;
                        c = cArr[i2];
                        if (c == 'R' || c == 'r') {
                            return 3;
                        }
                    } else if (c == 'U' || c == 'u') {
                        cArr = this.orig;
                        i = this.index;
                        i2 = i;
                        this.index = i + 1;
                        c = cArr[i2];
                        if (c == 'G' || c == 'g') {
                            return 7;
                        }
                    }
                    break;
                case 'D':
                case 'd':
                    cArr = this.orig;
                    i = this.index;
                    i2 = i;
                    this.index = i + 1;
                    c = cArr[i2];
                    if (c == 'E' || c == 'e') {
                        cArr = this.orig;
                        i = this.index;
                        i2 = i;
                        this.index = i + 1;
                        c = cArr[i2];
                        if (c == 'C' || c == 'c') {
                            return 11;
                        }
                    }
                case 'F':
                case 'f':
                    cArr = this.orig;
                    i = this.index;
                    i2 = i;
                    this.index = i + 1;
                    c = cArr[i2];
                    if (c == 'E' || c == 'e') {
                        cArr = this.orig;
                        i = this.index;
                        i2 = i;
                        this.index = i + 1;
                        c = cArr[i2];
                        if (c == 'B' || c == 'b') {
                            return 1;
                        }
                    }
                case 'J':
                case 'j':
                    cArr = this.orig;
                    i = this.index;
                    i2 = i;
                    this.index = i + 1;
                    switch (cArr[i2]) {
                        case 'A':
                        case 'a':
                            cArr = this.orig;
                            i = this.index;
                            i2 = i;
                            this.index = i + 1;
                            c = cArr[i2];
                            if (c == 'N' || c == 'n') {
                                return 0;
                            }
                        case 'U':
                        case 'u':
                            cArr = this.orig;
                            i = this.index;
                            i2 = i;
                            this.index = i + 1;
                            c = cArr[i2];
                            if (c == 'N' || c == 'n') {
                                return 5;
                            }
                            if (c == 'L' || c == 'l') {
                                return 6;
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case 'M':
                case 'm':
                    cArr = this.orig;
                    i = this.index;
                    i2 = i;
                    this.index = i + 1;
                    c = cArr[i2];
                    if (c == 'A' || c == 'a') {
                        cArr = this.orig;
                        i = this.index;
                        i2 = i;
                        this.index = i + 1;
                        c = cArr[i2];
                        if (c == 'R' || c == 'r') {
                            return 2;
                        }
                        if (c == 'Y' || c == 'y') {
                            return 4;
                        }
                    }
                    break;
                case 'N':
                case 'n':
                    cArr = this.orig;
                    i = this.index;
                    i2 = i;
                    this.index = i + 1;
                    c = cArr[i2];
                    if (c == 'O' || c == 'o') {
                        cArr = this.orig;
                        i = this.index;
                        i2 = i;
                        this.index = i + 1;
                        c = cArr[i2];
                        if (c == 'V' || c == 'v') {
                            return 10;
                        }
                    }
                case 'O':
                case 'o':
                    cArr = this.orig;
                    i = this.index;
                    i2 = i;
                    this.index = i + 1;
                    c = cArr[i2];
                    if (c == 'C' || c == 'c') {
                        cArr = this.orig;
                        i = this.index;
                        i2 = i;
                        this.index = i + 1;
                        c = cArr[i2];
                        if (c == 'T' || c == 't') {
                            return 9;
                        }
                    }
                case 'S':
                case 's':
                    cArr = this.orig;
                    i = this.index;
                    i2 = i;
                    this.index = i + 1;
                    c = cArr[i2];
                    if (c == 'E' || c == 'e') {
                        cArr = this.orig;
                        i = this.index;
                        i2 = i;
                        this.index = i + 1;
                        c = cArr[i2];
                        if (c == 'P' || c == 'p') {
                            return 8;
                        }
                    }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException = e;
        }
        ParseException parseException = r8;
        ParseException parseException2 = new ParseException("Bad Month", this.index);
        throw parseException;
    }

    public int parseTimeZone() throws ParseException {
        if (this.index >= this.orig.length) {
            ParseException parseException = r6;
            ParseException parseException2 = new ParseException("No more characters", this.index);
            throw parseException;
        }
        char c = this.orig[this.index];
        if (c == '+' || c == '-') {
            return parseNumericTimeZone();
        }
        return parseAlphaTimeZone();
    }

    public int parseNumericTimeZone() throws ParseException {
        Object obj = null;
        char[] cArr = this.orig;
        int i = this.index;
        int i2 = i;
        this.index = i + 1;
        char c = cArr[i2];
        if (c == '+') {
            obj = 1;
        } else if (c != '-') {
            ParseException parseException = r10;
            ParseException parseException2 = new ParseException("Bad Numeric TimeZone", this.index);
            throw parseException;
        }
        int parseNumber = parseNumber();
        int i3 = ((parseNumber / 100) * 60) + (parseNumber % 100);
        if (obj != null) {
            return -i3;
        }
        return i3;
    }

    public int parseAlphaTimeZone() throws ParseException {
        Object obj = null;
        Object obj2 = null;
        ParseException parseException;
        ParseException parseException2;
        try {
            int i;
            char c;
            char[] cArr = this.orig;
            int i2 = this.index;
            int i3 = i2;
            this.index = i2 + 1;
            int obj22;
            switch (cArr[i3]) {
                case 'C':
                case 'c':
                    i = 360;
                    obj22 = 1;
                    break;
                case 'E':
                case 'e':
                    i = 300;
                    obj22 = 1;
                    break;
                case 'G':
                case 'g':
                    cArr = this.orig;
                    i2 = this.index;
                    i3 = i2;
                    this.index = i2 + 1;
                    c = cArr[i3];
                    if (c == 'M' || c == 'm') {
                        cArr = this.orig;
                        i2 = this.index;
                        i3 = i2;
                        this.index = i2 + 1;
                        c = cArr[i3];
                        if (c == 'T' || c == 't') {
                            i = 0;
                            break;
                        }
                    }
                    parseException = r10;
                    parseException2 = new ParseException("Bad Alpha TimeZone", this.index);
                    throw parseException;
                case 'M':
                case 'm':
                    i = 420;
                    obj22 = 1;
                    break;
                case 'P':
                case 'p':
                    i = 480;
                    obj22 = 1;
                    break;
                case 'U':
                case 'u':
                    cArr = this.orig;
                    i2 = this.index;
                    i3 = i2;
                    this.index = i2 + 1;
                    c = cArr[i3];
                    if (c == 'T' || c == 't') {
                        i = 0;
                        break;
                    }
                    parseException = r10;
                    parseException2 = new ParseException("Bad Alpha TimeZone", this.index);
                    throw parseException;
                    break;
                default:
                    parseException = r10;
                    parseException2 = new ParseException("Bad Alpha TimeZone", this.index);
                    throw parseException;
            }
            if (obj22 != null) {
                cArr = this.orig;
                i2 = this.index;
                i3 = i2;
                this.index = i2 + 1;
                c = cArr[i3];
                if (c == 'S' || c == 's') {
                    cArr = this.orig;
                    i2 = this.index;
                    i3 = i2;
                    this.index = i2 + 1;
                    c = cArr[i3];
                    if (!(c == 'T' || c == 't')) {
                        parseException = r10;
                        parseException2 = new ParseException("Bad Alpha TimeZone", this.index);
                        throw parseException;
                    }
                } else if (c == 'D' || c == 'd') {
                    cArr = this.orig;
                    i2 = this.index;
                    i3 = i2;
                    this.index = i2 + 1;
                    c = cArr[i3];
                    if (c == 'T' || c != 't') {
                        i -= 60;
                    } else {
                        parseException = r10;
                        parseException2 = new ParseException("Bad Alpha TimeZone", this.index);
                        throw parseException;
                    }
                }
            }
            return i;
        } catch (ArrayIndexOutOfBoundsException e) {
            ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException = e;
            parseException = r10;
            parseException2 = new ParseException("Bad Alpha TimeZone", this.index);
            throw parseException;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getIndex() {
        return this.index;
    }
}
