package javax.mail;

public class Provider {
    private String className;
    private String protocol;
    private Type type;
    private String vendor;
    private String version;

    public static class Type {
        public static final Type STORE;
        public static final Type TRANSPORT;
        private String type;

        static {
            Type type = r3;
            Type type2 = new Type("STORE");
            STORE = type;
            type = r3;
            type2 = new Type("TRANSPORT");
            TRANSPORT = type;
        }

        private Type(String str) {
            this.type = str;
        }

        public String toString() {
            return this.type;
        }
    }

    public Provider(Type type, String str, String str2, String str3, String str4) {
        String str5 = str;
        String str6 = str2;
        String str7 = str3;
        String str8 = str4;
        this.type = type;
        this.protocol = str5;
        this.className = str6;
        this.vendor = str7;
        this.version = str8;
    }

    public Type getType() {
        return this.type;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public String getClassName() {
        return this.className;
    }

    public String getVendor() {
        return this.vendor;
    }

    public String getVersion() {
        return this.version;
    }

    public String toString() {
        StringBuilder stringBuilder = r5;
        StringBuilder stringBuilder2 = new StringBuilder("javax.mail.Provider[");
        String stringBuilder3 = stringBuilder.append(this.type).append(",").append(this.protocol).append(",").append(this.className).toString();
        if (this.vendor != null) {
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder(String.valueOf(stringBuilder3));
            stringBuilder3 = stringBuilder.append(",").append(this.vendor).toString();
        }
        if (this.version != null) {
            stringBuilder = r5;
            stringBuilder2 = new StringBuilder(String.valueOf(stringBuilder3));
            stringBuilder3 = stringBuilder.append(",").append(this.version).toString();
        }
        stringBuilder = r5;
        stringBuilder2 = new StringBuilder(String.valueOf(stringBuilder3));
        return stringBuilder.append("]").toString();
    }
}
