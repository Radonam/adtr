package javax.mail;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

public class Flags implements Cloneable, Serializable {
    private static final int ANSWERED_BIT = 1;
    private static final int DELETED_BIT = 2;
    private static final int DRAFT_BIT = 4;
    private static final int FLAGGED_BIT = 8;
    private static final int RECENT_BIT = 16;
    private static final int SEEN_BIT = 32;
    private static final int USER_BIT = Integer.MIN_VALUE;
    private static final long serialVersionUID = 6243590407214169028L;
    private int system_flags = 0;
    private Hashtable user_flags = null;

    public static final class Flag {
        public static final Flag ANSWERED;
        public static final Flag DELETED;
        public static final Flag DRAFT;
        public static final Flag FLAGGED;
        public static final Flag RECENT;
        public static final Flag SEEN;
        public static final Flag USER;
        private int bit;

        static {
            Flag flag = r3;
            Flag flag2 = new Flag(1);
            ANSWERED = flag;
            flag = r3;
            flag2 = new Flag(2);
            DELETED = flag;
            flag = r3;
            flag2 = new Flag(4);
            DRAFT = flag;
            flag = r3;
            flag2 = new Flag(8);
            FLAGGED = flag;
            flag = r3;
            flag2 = new Flag(16);
            RECENT = flag;
            flag = r3;
            flag2 = new Flag(32);
            SEEN = flag;
            flag = r3;
            flag2 = new Flag(Flags.USER_BIT);
            USER = flag;
        }

        private Flag(int i) {
            this.bit = i;
        }
    }

    public Flags() {
    }

    public Flags(Flags flags) {
        Flags flags2 = flags;
        this.system_flags = flags2.system_flags;
        if (flags2.user_flags != null) {
            this.user_flags = (Hashtable) flags2.user_flags.clone();
        }
    }

    public Flags(Flag flag) {
        Flag flag2 = flag;
        this.system_flags |= flag2.bit;
    }

    public Flags(String str) {
        String str2 = str;
        Hashtable hashtable = r6;
        Hashtable hashtable2 = new Hashtable(1);
        this.user_flags = hashtable;
        Object put = this.user_flags.put(str2.toLowerCase(Locale.ENGLISH), str2);
    }

    public void add(Flag flag) {
        this.system_flags = this.system_flags | flag.bit;
    }

    public void add(String str) {
        String str2 = str;
        if (this.user_flags == null) {
            Hashtable hashtable = r6;
            Hashtable hashtable2 = new Hashtable(1);
            this.user_flags = hashtable;
        }
        Object put = this.user_flags.put(str2.toLowerCase(Locale.ENGLISH), str2);
    }

    public void add(Flags flags) {
        Flags flags2 = flags;
        this.system_flags |= flags2.system_flags;
        if (flags2.user_flags != null) {
            if (this.user_flags == null) {
                Hashtable hashtable = r8;
                Hashtable hashtable2 = new Hashtable(1);
                this.user_flags = hashtable;
            }
            Enumeration keys = flags2.user_flags.keys();
            while (keys.hasMoreElements()) {
                String str = (String) keys.nextElement();
                Object put = this.user_flags.put(str, flags2.user_flags.get(str));
            }
        }
    }

    public void remove(Flag flag) {
        this.system_flags = this.system_flags & (flag.bit ^ -1);
    }

    public void remove(String str) {
        String str2 = str;
        if (this.user_flags != null) {
            Object remove = this.user_flags.remove(str2.toLowerCase(Locale.ENGLISH));
        }
    }

    public void remove(Flags flags) {
        Flags flags2 = flags;
        this.system_flags &= flags2.system_flags ^ -1;
        if (flags2.user_flags != null && this.user_flags != null) {
            Enumeration keys = flags2.user_flags.keys();
            while (keys.hasMoreElements()) {
                Object remove = this.user_flags.remove(keys.nextElement());
            }
        }
    }

    public boolean contains(Flag flag) {
        return (this.system_flags & flag.bit) != 0;
    }

    public boolean contains(String str) {
        String str2 = str;
        if (this.user_flags == null) {
            return false;
        }
        return this.user_flags.containsKey(str2.toLowerCase(Locale.ENGLISH));
    }

    public boolean contains(Flags flags) {
        Flags flags2 = flags;
        if ((flags2.system_flags & this.system_flags) != flags2.system_flags) {
            return false;
        }
        if (flags2.user_flags != null) {
            if (this.user_flags == null) {
                return false;
            }
            Enumeration keys = flags2.user_flags.keys();
            while (keys.hasMoreElements()) {
                if (!this.user_flags.containsKey(keys.nextElement())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (!(obj2 instanceof Flags)) {
            return false;
        }
        Flags flags = (Flags) obj2;
        if (flags.system_flags != this.system_flags) {
            return false;
        }
        if (flags.user_flags == null && this.user_flags == null) {
            return true;
        }
        if (flags.user_flags == null || this.user_flags == null || flags.user_flags.size() != this.user_flags.size()) {
            return false;
        }
        Enumeration keys = flags.user_flags.keys();
        while (keys.hasMoreElements()) {
            if (!this.user_flags.containsKey(keys.nextElement())) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = this.system_flags;
        if (this.user_flags != null) {
            Enumeration keys = this.user_flags.keys();
            while (keys.hasMoreElements()) {
                i += ((String) keys.nextElement()).hashCode();
            }
        }
        return i;
    }

    public Flag[] getSystemFlags() {
        Vector vector = r5;
        Vector vector2 = new Vector();
        Vector vector3 = vector;
        if ((this.system_flags & 1) != 0) {
            vector3.addElement(Flag.ANSWERED);
        }
        if ((this.system_flags & 2) != 0) {
            vector3.addElement(Flag.DELETED);
        }
        if ((this.system_flags & 4) != 0) {
            vector3.addElement(Flag.DRAFT);
        }
        if ((this.system_flags & 8) != 0) {
            vector3.addElement(Flag.FLAGGED);
        }
        if ((this.system_flags & 16) != 0) {
            vector3.addElement(Flag.RECENT);
        }
        if ((this.system_flags & 32) != 0) {
            vector3.addElement(Flag.SEEN);
        }
        if ((this.system_flags & USER_BIT) != 0) {
            vector3.addElement(Flag.USER);
        }
        Flag[] flagArr = new Flag[vector3.size()];
        vector3.copyInto(flagArr);
        return flagArr;
    }

    public String[] getUserFlags() {
        Vector vector = r5;
        Vector vector2 = new Vector();
        Vector vector3 = vector;
        if (this.user_flags != null) {
            Enumeration elements = this.user_flags.elements();
            while (elements.hasMoreElements()) {
                vector3.addElement(elements.nextElement());
            }
        }
        String[] strArr = new String[vector3.size()];
        vector3.copyInto(strArr);
        return strArr;
    }

    public Object clone() {
        Flags flags = null;
        try {
            flags = (Flags) super.clone();
        } catch (CloneNotSupportedException e) {
            CloneNotSupportedException cloneNotSupportedException = e;
        }
        if (!(this.user_flags == null || flags == null)) {
            flags.user_flags = (Hashtable) this.user_flags.clone();
        }
        return flags;
    }
}
