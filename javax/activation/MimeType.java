package javax.activation;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Locale;

public class MimeType implements Externalizable {
    private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
    private MimeTypeParameterList parameters;
    private String primaryType;
    private String subType;

    public MimeType() {
        this.primaryType = "application";
        this.subType = "*";
        MimeTypeParameterList mimeTypeParameterList = r4;
        MimeTypeParameterList mimeTypeParameterList2 = new MimeTypeParameterList();
        this.parameters = mimeTypeParameterList;
    }

    public MimeType(String str) throws MimeTypeParseException {
        parse(str);
    }

    public MimeType(String str, String str2) throws MimeTypeParseException {
        String str3 = str;
        String str4 = str2;
        MimeTypeParseException mimeTypeParseException;
        MimeTypeParseException mimeTypeParseException2;
        if (isValidToken(str3)) {
            this.primaryType = str3.toLowerCase(Locale.ENGLISH);
            if (isValidToken(str4)) {
                this.subType = str4.toLowerCase(Locale.ENGLISH);
                MimeTypeParameterList mimeTypeParameterList = r6;
                MimeTypeParameterList mimeTypeParameterList2 = new MimeTypeParameterList();
                this.parameters = mimeTypeParameterList;
                return;
            }
            mimeTypeParseException = r6;
            mimeTypeParseException2 = new MimeTypeParseException("Sub type is invalid.");
            throw mimeTypeParseException;
        }
        mimeTypeParseException = r6;
        mimeTypeParseException2 = new MimeTypeParseException("Primary type is invalid.");
        throw mimeTypeParseException;
    }

    private void parse(String str) throws MimeTypeParseException {
        String str2 = str;
        int indexOf = str2.indexOf(47);
        int indexOf2 = str2.indexOf(59);
        MimeTypeParseException mimeTypeParseException;
        MimeTypeParseException mimeTypeParseException2;
        if (indexOf < 0 && indexOf2 < 0) {
            mimeTypeParseException = r9;
            mimeTypeParseException2 = new MimeTypeParseException("Unable to find a sub type.");
            throw mimeTypeParseException;
        } else if (indexOf >= 0 || indexOf2 < 0) {
            MimeTypeParameterList mimeTypeParameterList;
            MimeTypeParameterList mimeTypeParameterList2;
            if (indexOf >= 0 && indexOf2 < 0) {
                this.primaryType = str2.substring(0, indexOf).trim().toLowerCase(Locale.ENGLISH);
                this.subType = str2.substring(indexOf + 1).trim().toLowerCase(Locale.ENGLISH);
                mimeTypeParameterList = r9;
                mimeTypeParameterList2 = new MimeTypeParameterList();
                this.parameters = mimeTypeParameterList;
            } else if (indexOf < indexOf2) {
                this.primaryType = str2.substring(0, indexOf).trim().toLowerCase(Locale.ENGLISH);
                this.subType = str2.substring(indexOf + 1, indexOf2).trim().toLowerCase(Locale.ENGLISH);
                mimeTypeParameterList = r9;
                mimeTypeParameterList2 = new MimeTypeParameterList(str2.substring(indexOf2));
                this.parameters = mimeTypeParameterList;
            } else {
                mimeTypeParseException = r9;
                mimeTypeParseException2 = new MimeTypeParseException("Unable to find a sub type.");
                throw mimeTypeParseException;
            }
            if (!isValidToken(this.primaryType)) {
                mimeTypeParseException = r9;
                mimeTypeParseException2 = new MimeTypeParseException("Primary type is invalid.");
                throw mimeTypeParseException;
            } else if (!isValidToken(this.subType)) {
                mimeTypeParseException = r9;
                mimeTypeParseException2 = new MimeTypeParseException("Sub type is invalid.");
                throw mimeTypeParseException;
            }
        } else {
            mimeTypeParseException = r9;
            mimeTypeParseException2 = new MimeTypeParseException("Unable to find a sub type.");
            throw mimeTypeParseException;
        }
    }

    public String getPrimaryType() {
        return this.primaryType;
    }

    public void setPrimaryType(String str) throws MimeTypeParseException {
        String str2 = str;
        if (isValidToken(this.primaryType)) {
            this.primaryType = str2.toLowerCase(Locale.ENGLISH);
            return;
        }
        MimeTypeParseException mimeTypeParseException = r5;
        MimeTypeParseException mimeTypeParseException2 = new MimeTypeParseException("Primary type is invalid.");
        throw mimeTypeParseException;
    }

    public String getSubType() {
        return this.subType;
    }

    public void setSubType(String str) throws MimeTypeParseException {
        String str2 = str;
        if (isValidToken(this.subType)) {
            this.subType = str2.toLowerCase(Locale.ENGLISH);
            return;
        }
        MimeTypeParseException mimeTypeParseException = r5;
        MimeTypeParseException mimeTypeParseException2 = new MimeTypeParseException("Sub type is invalid.");
        throw mimeTypeParseException;
    }

    public MimeTypeParameterList getParameters() {
        return this.parameters;
    }

    public String getParameter(String str) {
        return this.parameters.get(str);
    }

    public void setParameter(String str, String str2) {
        this.parameters.set(str, str2);
    }

    public void removeParameter(String str) {
        this.parameters.remove(str);
    }

    public String toString() {
        StringBuilder stringBuilder = r4;
        StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(getBaseType()));
        return stringBuilder.append(this.parameters.toString()).toString();
    }

    public String getBaseType() {
        StringBuilder stringBuilder = r4;
        StringBuilder stringBuilder2 = new StringBuilder(String.valueOf(this.primaryType));
        return stringBuilder.append("/").append(this.subType).toString();
    }

    public boolean match(MimeType mimeType) {
        MimeType mimeType2 = mimeType;
        if (this.primaryType.equals(mimeType2.getPrimaryType()) && (this.subType.equals("*") || mimeType2.getSubType().equals("*") || this.subType.equals(mimeType2.getSubType()))) {
            return true;
        }
        return false;
    }

    public boolean match(String str) throws MimeTypeParseException {
        MimeType mimeType = r6;
        MimeType mimeType2 = new MimeType(str);
        return match(mimeType);
    }

    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        ObjectOutput objectOutput2 = objectOutput;
        objectOutput2.writeUTF(toString());
        objectOutput2.flush();
    }

    public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        try {
            parse(objectInput.readUTF());
        } catch (MimeTypeParseException e) {
            MimeTypeParseException mimeTypeParseException = e;
            IOException iOException = r6;
            IOException iOException2 = new IOException(mimeTypeParseException.toString());
            throw iOException;
        }
    }

    private static boolean isTokenChar(char c) {
        char c2 = c;
        return c2 > ' ' && c2 < 127 && TSPECIALS.indexOf(c2) < 0;
    }

    private boolean isValidToken(String str) {
        String str2 = str;
        int length = str2.length();
        if (length <= 0) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!isTokenChar(str2.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
