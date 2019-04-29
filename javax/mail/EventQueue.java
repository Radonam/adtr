package javax.mail;

import java.util.Vector;
import javax.mail.event.MailEvent;

class EventQueue implements Runnable {
    private QueueElement head = null;
    private Thread qThread;
    private QueueElement tail = null;

    static class QueueElement {
        MailEvent event = null;
        QueueElement next = null;
        QueueElement prev = null;
        Vector vector = null;

        QueueElement(MailEvent mailEvent, Vector vector) {
            MailEvent mailEvent2 = mailEvent;
            Vector vector2 = vector;
            this.event = mailEvent2;
            this.vector = vector2;
        }
    }

    public EventQueue() {
        Thread thread = r6;
        Thread thread2 = new Thread(this, "JavaMail-EventQueue");
        this.qThread = thread;
        this.qThread.setDaemon(true);
        this.qThread.start();
    }

    public synchronized void enqueue(MailEvent mailEvent, Vector vector) {
        MailEvent mailEvent2 = mailEvent;
        Vector vector2 = vector;
        synchronized (this) {
            QueueElement queueElement = r9;
            QueueElement queueElement2 = new QueueElement(mailEvent2, vector2);
            QueueElement queueElement3 = queueElement;
            if (this.head == null) {
                this.head = queueElement3;
                this.tail = queueElement3;
            } else {
                queueElement3.next = this.head;
                this.head.prev = queueElement3;
                this.head = queueElement3;
            }
            notifyAll();
        }
    }

    private synchronized QueueElement dequeue() throws InterruptedException {
        QueueElement queueElement;
        synchronized (this) {
            while (this.tail == null) {
                wait();
            }
            QueueElement queueElement2 = this.tail;
            this.tail = queueElement2.prev;
            if (this.tail == null) {
                this.head = null;
            } else {
                this.tail.next = null;
            }
            QueueElement queueElement3 = queueElement2;
            QueueElement queueElement4 = null;
            QueueElement queueElement5 = queueElement4;
            QueueElement queueElement6 = queueElement4;
            queueElement2.next = queueElement6;
            queueElement3.prev = queueElement5;
            queueElement = queueElement2;
        }
        return queueElement;
    }

    public void run() {
        while (true) {
            try {
                QueueElement dequeue = dequeue();
                QueueElement queueElement = dequeue;
                if (dequeue != null) {
                    MailEvent mailEvent = queueElement.event;
                    Vector vector = queueElement.vector;
                    for (int i = 0; i < vector.size(); i++) {
                        try {
                            mailEvent.dispatch(vector.elementAt(i));
                        } catch (Throwable th) {
                            if (th instanceof InterruptedException) {
                                return;
                            }
                        }
                    }
                    Object obj = null;
                    Object obj2 = null;
                    Object obj3 = null;
                } else {
                    return;
                }
            } catch (InterruptedException e) {
                InterruptedException interruptedException = e;
                return;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void stop() {
        if (this.qThread != null) {
            this.qThread.interrupt();
            this.qThread = null;
        }
    }
}
