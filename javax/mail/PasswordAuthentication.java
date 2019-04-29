package javax.mail;

public final class PasswordAuthentication {
    private String password;
    private String userName;

    public PasswordAuthentication(String str, String str2) {
        String str3 = str2;
        this.userName = str;
        this.password = str3;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassword() {
        return this.password;
    }
}
