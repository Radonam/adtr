package javax.activation;

import java.beans.Beans;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class CommandInfo {
    private String className;
    private String verb;

    public CommandInfo(String str, String str2) {
        String str3 = str2;
        this.verb = str;
        this.className = str3;
    }

    public String getCommandName() {
        return this.verb;
    }

    public String getCommandClass() {
        return this.className;
    }

    public Object getCommandObject(DataHandler dataHandler, ClassLoader classLoader) throws IOException, ClassNotFoundException {
        DataHandler dataHandler2 = dataHandler;
        Object obj = null;
        obj = Beans.instantiate(classLoader, this.className);
        if (obj != null) {
            if (obj instanceof CommandObject) {
                ((CommandObject) obj).setCommandContext(this.verb, dataHandler2);
            } else if ((obj instanceof Externalizable) && dataHandler2 != null) {
                InputStream inputStream = dataHandler2.getInputStream();
                if (inputStream != null) {
                    Externalizable externalizable = (Externalizable) obj;
                    ObjectInputStream objectInputStream = r9;
                    ObjectInputStream objectInputStream2 = new ObjectInputStream(inputStream);
                    externalizable.readExternal(objectInputStream);
                }
            }
        }
        return obj;
    }
}
