package javax.mail.event;

public abstract class ConnectionAdapter implements ConnectionListener {
    public ConnectionAdapter() {
    }

    public void opened(ConnectionEvent connectionEvent) {
    }

    public void disconnected(ConnectionEvent connectionEvent) {
    }

    public void closed(ConnectionEvent connectionEvent) {
    }
}
