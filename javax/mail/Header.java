package javax.mail;

public class Header {
    protected String name;
    protected String value;

    public Header(String str, String str2) {
        String str3 = str2;
        this.name = str;
        this.value = str3;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }
}
