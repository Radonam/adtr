package javax.mail;

import java.net.InetAddress;

public abstract class Authenticator {
    private int requestingPort;
    private String requestingPrompt;
    private String requestingProtocol;
    private InetAddress requestingSite;
    private String requestingUserName;

    public Authenticator() {
    }

    private void reset() {
        this.requestingSite = null;
        this.requestingPort = -1;
        this.requestingProtocol = null;
        this.requestingPrompt = null;
        this.requestingUserName = null;
    }

    /* Access modifiers changed, original: final */
    public final PasswordAuthentication requestPasswordAuthentication(InetAddress inetAddress, int i, String str, String str2, String str3) {
        InetAddress inetAddress2 = inetAddress;
        int i2 = i;
        String str4 = str;
        String str5 = str2;
        String str6 = str3;
        reset();
        this.requestingSite = inetAddress2;
        this.requestingPort = i2;
        this.requestingProtocol = str4;
        this.requestingPrompt = str5;
        this.requestingUserName = str6;
        return getPasswordAuthentication();
    }

    /* Access modifiers changed, original: protected|final */
    public final InetAddress getRequestingSite() {
        return this.requestingSite;
    }

    /* Access modifiers changed, original: protected|final */
    public final int getRequestingPort() {
        return this.requestingPort;
    }

    /* Access modifiers changed, original: protected|final */
    public final String getRequestingProtocol() {
        return this.requestingProtocol;
    }

    /* Access modifiers changed, original: protected|final */
    public final String getRequestingPrompt() {
        return this.requestingPrompt;
    }

    /* Access modifiers changed, original: protected|final */
    public final String getDefaultUserName() {
        return this.requestingUserName;
    }

    /* Access modifiers changed, original: protected */
    public PasswordAuthentication getPasswordAuthentication() {
        return null;
    }
}
