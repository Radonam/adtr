package javax.mail.internet;

import javax.mail.internet.HeaderTokenizer.Token;

public class ContentDisposition {
    private String disposition;
    private ParameterList list;

    public ContentDisposition() {
    }

    public ContentDisposition(String str, ParameterList parameterList) {
        ParameterList parameterList2 = parameterList;
        this.disposition = str;
        this.list = parameterList2;
    }

    public ContentDisposition(String str) throws ParseException {
        HeaderTokenizer headerTokenizer = r9;
        HeaderTokenizer headerTokenizer2 = new HeaderTokenizer(str, HeaderTokenizer.MIME);
        HeaderTokenizer headerTokenizer3 = headerTokenizer;
        Token next = headerTokenizer3.next();
        if (next.getType() != -1) {
            ParseException parseException = r9;
            ParseException parseException2 = new ParseException();
            throw parseException;
        }
        this.disposition = next.getValue();
        String remainder = headerTokenizer3.getRemainder();
        if (remainder != null) {
            ParameterList parameterList = r9;
            ParameterList parameterList2 = new ParameterList(remainder);
            this.list = parameterList;
        }
    }

    public String getDisposition() {
        return this.disposition;
    }

    public String getParameter(String str) {
        String str2 = str;
        if (this.list == null) {
            return null;
        }
        return this.list.get(str2);
    }

    public ParameterList getParameterList() {
        return this.list;
    }

    public void setDisposition(String str) {
        this.disposition = str;
    }

    public void setParameter(String str, String str2) {
        String str3 = str;
        String str4 = str2;
        if (this.list == null) {
            ParameterList parameterList = r6;
            ParameterList parameterList2 = new ParameterList();
            this.list = parameterList;
        }
        this.list.set(str3, str4);
    }

    public void setParameterList(ParameterList parameterList) {
        this.list = parameterList;
    }

    public String toString() {
        if (this.disposition == null) {
            return null;
        }
        if (this.list == null) {
            return this.disposition;
        }
        StringBuffer stringBuffer = r6;
        StringBuffer stringBuffer2 = new StringBuffer(this.disposition);
        StringBuffer stringBuffer3 = stringBuffer;
        stringBuffer = stringBuffer3.append(this.list.toString(stringBuffer3.length() + 21));
        return stringBuffer3.toString();
    }
}
