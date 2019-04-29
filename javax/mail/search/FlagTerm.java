package javax.mail.search;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Message;

public final class FlagTerm extends SearchTerm {
    private static final long serialVersionUID = -142991500302030647L;
    protected Flags flags;
    protected boolean set;

    public FlagTerm(Flags flags, boolean z) {
        boolean z2 = z;
        this.flags = flags;
        this.set = z2;
    }

    public Flags getFlags() {
        return (Flags) this.flags.clone();
    }

    public boolean getTestSet() {
        return this.set;
    }

    public boolean match(Message message) {
        try {
            Flags flags = message.getFlags();
            if (!this.set) {
                Flag[] systemFlags = this.flags.getSystemFlags();
                for (Flag contains : systemFlags) {
                    if (flags.contains(contains)) {
                        return false;
                    }
                }
                String[] userFlags = this.flags.getUserFlags();
                for (String contains2 : userFlags) {
                    if (flags.contains(contains2)) {
                        return false;
                    }
                }
                return true;
            } else if (flags.contains(this.flags)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Exception exception = e;
            return false;
        }
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (!(obj2 instanceof FlagTerm)) {
            return false;
        }
        FlagTerm flagTerm = (FlagTerm) obj2;
        return flagTerm.set == this.set && flagTerm.flags.equals(this.flags);
    }

    public int hashCode() {
        return this.set ? this.flags.hashCode() : this.flags.hashCode() ^ -1;
    }
}
