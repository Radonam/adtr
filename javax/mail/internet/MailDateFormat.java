package javax.mail.internet;

import com.sun.mail.imap.IMAPStore;
import java.io.PrintStream;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class MailDateFormat extends SimpleDateFormat {
    private static Calendar cal = null;
    static boolean debug = false;
    private static final long serialVersionUID = -8148227605210628779L;
    private static TimeZone tz = TimeZone.getTimeZone("GMT");

    public MailDateFormat() {
        super("EEE, d MMM yyyy HH:mm:ss 'XXXXX' (z)", Locale.US);
    }

    public StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        int i;
        Date date2 = date;
        StringBuffer stringBuffer2 = stringBuffer;
        FieldPosition fieldPosition2 = fieldPosition;
        int length = stringBuffer2.length();
        StringBuffer format = super.format(date2, stringBuffer2, fieldPosition2);
        Object obj = null;
        int i2 = length + 25;
        while (stringBuffer2.charAt(i2) != 'X') {
            i2++;
        }
        this.calendar.clear();
        this.calendar.setTime(date2);
        int i3 = this.calendar.get(15) + this.calendar.get(16);
        if (i3 < 0) {
            i = i2;
            i2++;
            stringBuffer2.setCharAt(i, '-');
            i3 = -i3;
        } else {
            i = i2;
            i2++;
            stringBuffer2.setCharAt(i, '+');
        }
        int i4 = (i3 / 60) / IMAPStore.RESPONSE;
        int i5 = i4 / 60;
        int i6 = i4 % 60;
        i = i2;
        i2++;
        stringBuffer2.setCharAt(i, Character.forDigit(i5 / 10, 10));
        i = i2;
        i2++;
        stringBuffer2.setCharAt(i, Character.forDigit(i5 % 10, 10));
        i = i2;
        i2++;
        stringBuffer2.setCharAt(i, Character.forDigit(i6 / 10, 10));
        i = i2;
        i2++;
        stringBuffer2.setCharAt(i, Character.forDigit(i6 % 10, 10));
        return stringBuffer2;
    }

    public Date parse(String str, ParsePosition parsePosition) {
        return parseDate(str.toCharArray(), parsePosition, isLenient());
    }

    static {
        Calendar calendar = r3;
        Calendar gregorianCalendar = new GregorianCalendar(tz);
        cal = calendar;
    }

    private static Date parseDate(char[] cArr, ParsePosition parsePosition, boolean z) {
        PrintStream printStream;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;
        String str;
        String str2;
        char[] cArr2 = cArr;
        ParsePosition parsePosition2 = parsePosition;
        boolean z2 = z;
        int i = -1;
        int i2 = -1;
        int i3 = -1;
        Object obj = null;
        Object obj2 = null;
        int i4 = 0;
        int i5 = 0;
        try {
            MailDateParser mailDateParser = r20;
            MailDateParser mailDateParser2 = new MailDateParser(cArr2);
            MailDateParser mailDateParser3 = mailDateParser;
            mailDateParser3.skipUntilNumber();
            i = mailDateParser3.parseNumber();
            if (!mailDateParser3.skipIfChar('-')) {
                mailDateParser3.skipWhiteSpace();
            }
            i2 = mailDateParser3.parseMonth();
            if (!mailDateParser3.skipIfChar('-')) {
                mailDateParser3.skipWhiteSpace();
            }
            i3 = mailDateParser3.parseNumber();
            if (i3 < 50) {
                i3 += 2000;
            } else if (i3 < 100) {
                i3 += 1900;
            }
            mailDateParser3.skipWhiteSpace();
            int parseNumber = mailDateParser3.parseNumber();
            mailDateParser3.skipChar(':');
            int parseNumber2 = mailDateParser3.parseNumber();
            if (mailDateParser3.skipIfChar(':')) {
                i4 = mailDateParser3.parseNumber();
            }
            try {
                mailDateParser3.skipWhiteSpace();
                i5 = mailDateParser3.parseTimeZone();
            } catch (ParseException e) {
                ParseException parseException = e;
                if (debug) {
                    printStream = System.out;
                    stringBuilder = r20;
                    stringBuilder2 = new StringBuilder("No timezone? : '");
                    str = r20;
                    str2 = new String(cArr2);
                    printStream.println(stringBuilder.append(str).append("'").toString());
                }
            }
            parsePosition2.setIndex(mailDateParser3.getIndex());
            return ourUTC(i3, i2, i, parseNumber, parseNumber2, i4, i5, z2);
        } catch (Exception e2) {
            Exception exception = e2;
            if (debug) {
                printStream = System.out;
                stringBuilder = r20;
                stringBuilder2 = new StringBuilder("Bad date: '");
                str = r20;
                str2 = new String(cArr2);
                printStream.println(stringBuilder.append(str).append("'").toString());
                exception.printStackTrace();
            }
            parsePosition2.setIndex(1);
            return null;
        }
    }

    private static synchronized Date ourUTC(int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z) {
        Date time;
        int i8 = i;
        int i9 = i2;
        int i10 = i3;
        int i11 = i4;
        int i12 = i5;
        int i13 = i6;
        int i14 = i7;
        boolean z2 = z;
        synchronized (MailDateFormat.class) {
            cal.clear();
            cal.setLenient(z2);
            cal.set(1, i8);
            cal.set(2, i9);
            cal.set(5, i10);
            cal.set(11, i11);
            cal.set(12, i12 + i14);
            cal.set(13, i13);
            time = cal.getTime();
        }
        return time;
    }

    public void setCalendar(Calendar calendar) {
        Calendar calendar2 = calendar;
        RuntimeException runtimeException = r5;
        RuntimeException runtimeException2 = new RuntimeException("Method setCalendar() shouldn't be called");
        throw runtimeException;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        NumberFormat numberFormat2 = numberFormat;
        RuntimeException runtimeException = r5;
        RuntimeException runtimeException2 = new RuntimeException("Method setNumberFormat() shouldn't be called");
        throw runtimeException;
    }
}
