package javax.mail;

import java.util.Vector;

public class FetchProfile {
    private Vector headers = null;
    private Vector specials = null;

    public static class Item {
        public static final Item CONTENT_INFO;
        public static final Item ENVELOPE;
        public static final Item FLAGS;
        private String name;

        static {
            Item item = r3;
            Item item2 = new Item("ENVELOPE");
            ENVELOPE = item;
            item = r3;
            item2 = new Item("CONTENT_INFO");
            CONTENT_INFO = item;
            item = r3;
            item2 = new Item("FLAGS");
            FLAGS = item;
        }

        protected Item(String str) {
            this.name = str;
        }
    }

    public FetchProfile() {
    }

    public void add(Item item) {
        Item item2 = item;
        if (this.specials == null) {
            Vector vector = r5;
            Vector vector2 = new Vector();
            this.specials = vector;
        }
        this.specials.addElement(item2);
    }

    public void add(String str) {
        String str2 = str;
        if (this.headers == null) {
            Vector vector = r5;
            Vector vector2 = new Vector();
            this.headers = vector;
        }
        this.headers.addElement(str2);
    }

    public boolean contains(Item item) {
        return this.specials != null && this.specials.contains(item);
    }

    public boolean contains(String str) {
        return this.headers != null && this.headers.contains(str);
    }

    public Item[] getItems() {
        if (this.specials == null) {
            return new Item[0];
        }
        Item[] itemArr = new Item[this.specials.size()];
        this.specials.copyInto(itemArr);
        return itemArr;
    }

    public String[] getHeaderNames() {
        if (this.headers == null) {
            return new String[0];
        }
        String[] strArr = new String[this.headers.size()];
        this.headers.copyInto(strArr);
        return strArr;
    }
}
