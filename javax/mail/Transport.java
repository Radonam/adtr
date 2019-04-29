package javax.mail;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.mail.event.MailEvent;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;

public abstract class Transport extends Service {
    private Vector transportListeners = null;

    public abstract void sendMessage(Message message, Address[] addressArr) throws MessagingException;

    public Transport(Session session, URLName uRLName) {
        super(session, uRLName);
    }

    public static void send(Message message) throws MessagingException {
        Message message2 = message;
        message2.saveChanges();
        send0(message2, message2.getAllRecipients());
    }

    public static void send(Message message, Address[] addressArr) throws MessagingException {
        Message message2 = message;
        Address[] addressArr2 = addressArr;
        message2.saveChanges();
        send0(message2, addressArr2);
    }

    private static void send0(Message message, Address[] addressArr) throws MessagingException {
        Throwable th;
        Exception exception;
        boolean nextException;
        Message message2 = message;
        Address[] addressArr2 = addressArr;
        SendFailedException sendFailedException;
        SendFailedException sendFailedException2;
        if (addressArr2 == null || addressArr2.length == 0) {
            sendFailedException = r28;
            sendFailedException2 = new SendFailedException("No recipient addresses");
            throw sendFailedException;
        }
        int i;
        Hashtable hashtable = r28;
        Hashtable hashtable2 = new Hashtable();
        Hashtable hashtable3 = hashtable;
        Vector vector = r28;
        Vector vector2 = new Vector();
        Vector vector3 = vector;
        vector = r28;
        vector2 = new Vector();
        Vector vector4 = vector;
        vector = r28;
        vector2 = new Vector();
        Vector vector5 = vector;
        for (i = 0; i < addressArr2.length; i++) {
            if (hashtable3.containsKey(addressArr2[i].getType())) {
                ((Vector) hashtable3.get(addressArr2[i].getType())).addElement(addressArr2[i]);
            } else {
                vector = r28;
                vector2 = new Vector();
                Vector vector6 = vector;
                vector6.addElement(addressArr2[i]);
                Object put = hashtable3.put(addressArr2[i].getType(), vector6);
            }
        }
        i = hashtable3.size();
        if (i == 0) {
            sendFailedException = r28;
            sendFailedException2 = new SendFailedException("No recipient addresses");
            throw sendFailedException;
        }
        Session session;
        if (message2.session != null) {
            session = message2.session;
        } else {
            session = Session.getDefaultInstance(System.getProperties(), null);
        }
        Session session2 = session;
        Transport transport;
        if (i == 1) {
            transport = session2.getTransport(addressArr2[0]);
            try {
                transport.connect();
                transport.sendMessage(message2, addressArr2);
                transport.close();
            } catch (Throwable th2) {
                Throwable th3 = th2;
                transport.close();
                th2 = th3;
            }
        } else {
            Address[] addressArr3;
            MessagingException messagingException = null;
            Object obj = null;
            Enumeration elements = hashtable3.elements();
            while (elements.hasMoreElements()) {
                Vector vector7 = (Vector) elements.nextElement();
                addressArr3 = new Address[vector7.size()];
                vector7.copyInto(addressArr3);
                Transport transport2 = session2.getTransport(addressArr3[0]);
                transport = transport2;
                if (transport2 == null) {
                    for (Object addElement : addressArr3) {
                        vector3.addElement(addElement);
                    }
                } else {
                    try {
                        transport.connect();
                        transport.sendMessage(message2, addressArr3);
                        transport.close();
                    } catch (SendFailedException e) {
                        exception = e;
                        obj = 1;
                        if (messagingException == null) {
                            messagingException = exception;
                        } else {
                            nextException = messagingException.setNextException(exception);
                        }
                        Address[] invalidAddresses = exception.getInvalidAddresses();
                        if (invalidAddresses != null) {
                            for (Object addElement2 : invalidAddresses) {
                                vector3.addElement(addElement2);
                            }
                        }
                        invalidAddresses = exception.getValidSentAddresses();
                        if (invalidAddresses != null) {
                            for (Object addElement22 : invalidAddresses) {
                                vector4.addElement(addElement22);
                            }
                        }
                        Address[] validUnsentAddresses = exception.getValidUnsentAddresses();
                        if (validUnsentAddresses != null) {
                            int i2 = 0;
                            while (true) {
                                if (i2 >= validUnsentAddresses.length) {
                                    break;
                                }
                                vector5.addElement(validUnsentAddresses[i2]);
                                i2++;
                            }
                        }
                        transport.close();
                    } catch (MessagingException e2) {
                        exception = e2;
                        obj = 1;
                        if (messagingException == null) {
                            messagingException = exception;
                        } else {
                            nextException = messagingException.setNextException(exception);
                        }
                        transport.close();
                    } catch (Throwable th22) {
                        Throwable th4 = th22;
                        transport.close();
                        th22 = th4;
                    }
                }
            }
            if (obj != null || vector3.size() != 0 || vector5.size() != 0) {
                Address[] addressArr4 = (Address[]) null;
                addressArr3 = (Address[]) null;
                Address[] addressArr5 = (Address[]) null;
                if (vector4.size() > 0) {
                    addressArr4 = new Address[vector4.size()];
                    vector4.copyInto(addressArr4);
                }
                if (vector5.size() > 0) {
                    addressArr3 = new Address[vector5.size()];
                    vector5.copyInto(addressArr3);
                }
                if (vector3.size() > 0) {
                    addressArr5 = new Address[vector3.size()];
                    vector3.copyInto(addressArr5);
                }
                sendFailedException = r28;
                sendFailedException2 = new SendFailedException("Sending failed", messagingException, addressArr4, addressArr3, addressArr5);
                throw sendFailedException;
            }
        }
    }

    public synchronized void addTransportListener(TransportListener transportListener) {
        TransportListener transportListener2 = transportListener;
        synchronized (this) {
            if (this.transportListeners == null) {
                Vector vector = r6;
                Vector vector2 = new Vector();
                this.transportListeners = vector;
            }
            this.transportListeners.addElement(transportListener2);
        }
    }

    public synchronized void removeTransportListener(TransportListener transportListener) {
        TransportListener transportListener2 = transportListener;
        synchronized (this) {
            if (this.transportListeners != null) {
                boolean removeElement = this.transportListeners.removeElement(transportListener2);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void notifyTransportListeners(int i, Address[] addressArr, Address[] addressArr2, Address[] addressArr3, Message message) {
        int i2 = i;
        Address[] addressArr4 = addressArr;
        Address[] addressArr5 = addressArr2;
        Address[] addressArr6 = addressArr3;
        Message message2 = message;
        if (this.transportListeners != null) {
            MailEvent mailEvent = r15;
            MailEvent transportEvent = new TransportEvent(this, i2, addressArr4, addressArr5, addressArr6, message2);
            queueEvent(mailEvent, this.transportListeners);
        }
    }
}
