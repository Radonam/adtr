package javax.activation;

import myjava.awt.datatransfer.DataFlavor;

public class ActivationDataFlavor extends DataFlavor {
    private String humanPresentableName;
    private MimeType mimeObject;
    private String mimeType;
    private Class representationClass;

    public ActivationDataFlavor(Class cls, String str, String str2) {
        Class cls2 = cls;
        String str3 = str;
        String str4 = str2;
        super(str3, str4);
        this.mimeType = null;
        this.mimeObject = null;
        this.humanPresentableName = null;
        this.representationClass = null;
        this.mimeType = str3;
        this.humanPresentableName = str4;
        this.representationClass = cls2;
    }

    public ActivationDataFlavor(Class cls, String str) {
        Class cls2 = cls;
        String str2 = str;
        super(cls2, str2);
        this.mimeType = null;
        this.mimeObject = null;
        this.humanPresentableName = null;
        this.representationClass = null;
        this.mimeType = super.getMimeType();
        this.representationClass = cls2;
        this.humanPresentableName = str2;
    }

    public ActivationDataFlavor(String str, String str2) {
        String str3 = str;
        String str4 = str2;
        super(str3, str4);
        this.mimeType = null;
        this.mimeObject = null;
        this.humanPresentableName = null;
        this.representationClass = null;
        this.mimeType = str3;
        try {
            this.representationClass = Class.forName("java.io.InputStream");
        } catch (ClassNotFoundException e) {
            ClassNotFoundException classNotFoundException = e;
        }
        this.humanPresentableName = str4;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Class getRepresentationClass() {
        return this.representationClass;
    }

    public String getHumanPresentableName() {
        return this.humanPresentableName;
    }

    public void setHumanPresentableName(String str) {
        this.humanPresentableName = str;
    }

    public boolean equals(DataFlavor dataFlavor) {
        DataFlavor dataFlavor2 = dataFlavor;
        if (isMimeTypeEqual(dataFlavor2) && dataFlavor2.getRepresentationClass() == this.representationClass) {
            return true;
        }
        return false;
    }

    public boolean isMimeTypeEqual(String str) {
        String str2 = str;
        Object obj = null;
        try {
            MimeType mimeType;
            if (this.mimeObject == null) {
                mimeType = r8;
                MimeType mimeType2 = new MimeType(this.mimeType);
                this.mimeObject = mimeType;
            }
            MimeType mimeType3 = r8;
            mimeType = new MimeType(str2);
            return this.mimeObject.match(mimeType3);
        } catch (MimeTypeParseException e) {
            MimeTypeParseException mimeTypeParseException = e;
            return this.mimeType.equalsIgnoreCase(str2);
        }
    }

    /* Access modifiers changed, original: protected */
    public String normalizeMimeTypeParameter(String str, String str2) {
        String str3 = str;
        return str2;
    }

    /* Access modifiers changed, original: protected */
    public String normalizeMimeType(String str) {
        return str;
    }
}
