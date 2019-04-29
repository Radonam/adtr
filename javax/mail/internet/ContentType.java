package javax.mail.internet;

import javax.mail.internet.HeaderTokenizer.Token;

public class ContentType {
    private ParameterList list;
    private String primaryType;
    private String subType;

    public ContentType() {
    }

    public ContentType(String str, String str2, ParameterList parameterList) {
        String str3 = str2;
        ParameterList parameterList2 = parameterList;
        this.primaryType = str;
        this.subType = str3;
        this.list = parameterList2;
    }

    public ContentType(String str) throws ParseException {
        HeaderTokenizer headerTokenizer = r9;
        HeaderTokenizer headerTokenizer2 = new HeaderTokenizer(str, HeaderTokenizer.MIME);
        HeaderTokenizer headerTokenizer3 = headerTokenizer;
        Token next = headerTokenizer3.next();
        ParseException parseException;
        ParseException parseException2;
        if (next.getType() != -1) {
            parseException = r9;
            parseException2 = new ParseException();
            throw parseException;
        }
        this.primaryType = next.getValue();
        if (((char) headerTokenizer3.next().getType()) != '/') {
            parseException = r9;
            parseException2 = new ParseException();
            throw parseException;
        }
        next = headerTokenizer3.next();
        if (next.getType() != -1) {
            parseException = r9;
            parseException2 = new ParseException();
            throw parseException;
        }
        this.subType = next.getValue();
        String remainder = headerTokenizer3.getRemainder();
        if (remainder != null) {
            ParameterList parameterList = r9;
            ParameterList parameterList2 = new ParameterList(remainder);
            this.list = parameterList;
        }
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public String getSubType() {
        return this.subType;
    }

    public String getBaseType() {
        StringBuilder stringBuilder = r4;
        StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(this.primaryType));
        return stringBuilder.append('/').append(this.subType).toString();
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

    public void setPrimaryType(String str) {
        this.primaryType = str;
    }

    public void setSubType(String str) {
        this.subType = str;
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
        if (this.primaryType == null || this.subType == null) {
            return null;
        }
        StringBuffer stringBuffer = r6;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        stringBuffer = stringBuffer3.append(this.primaryType).append('/').append(this.subType);
        if (this.list != null) {
            stringBuffer = stringBuffer3.append(this.list.toString(stringBuffer3.length() + 14));
        }
        return stringBuffer3.toString();
    }

    public boolean match(ContentType contentType) {
        ContentType contentType2 = contentType;
        if (!this.primaryType.equalsIgnoreCase(contentType2.getPrimaryType())) {
            return false;
        }
        String subType = contentType2.getSubType();
        if (this.subType.charAt(0) == '*' || subType.charAt(0) == '*') {
            return true;
        }
        if (this.subType.equalsIgnoreCase(subType)) {
            return true;
        }
        return false;
    }

    public boolean match(String str) {
        try {
            ContentType contentType = r7;
            ContentType contentType2 = new ContentType(str);
            return match(contentType);
        } catch (ParseException e) {
            ParseException parseException = e;
            return false;
        }
    }
}
