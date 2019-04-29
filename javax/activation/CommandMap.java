package javax.activation;

public abstract class CommandMap {
    private static CommandMap defaultCommandMap = null;

    public abstract DataContentHandler createDataContentHandler(String str);

    public abstract CommandInfo[] getAllCommands(String str);

    public abstract CommandInfo getCommand(String str, String str2);

    public abstract CommandInfo[] getPreferredCommands(String str);

    public CommandMap() {
    }

    public static CommandMap getDefaultCommandMap() {
        if (defaultCommandMap == null) {
            CommandMap commandMap = r2;
            CommandMap mailcapCommandMap = new MailcapCommandMap();
            defaultCommandMap = commandMap;
        }
        return defaultCommandMap;
    }

    public static void setDefaultCommandMap(CommandMap commandMap) {
        CommandMap commandMap2 = commandMap;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkSetFactory();
            } catch (SecurityException e) {
                SecurityException securityException = e;
                if (CommandMap.class.getClassLoader() != commandMap2.getClass().getClassLoader()) {
                    throw securityException;
                }
            }
        }
        defaultCommandMap = commandMap2;
    }

    public CommandInfo[] getPreferredCommands(String str, DataSource dataSource) {
        DataSource dataSource2 = dataSource;
        return getPreferredCommands(str);
    }

    public CommandInfo[] getAllCommands(String str, DataSource dataSource) {
        DataSource dataSource2 = dataSource;
        return getAllCommands(str);
    }

    public CommandInfo getCommand(String str, String str2, DataSource dataSource) {
        DataSource dataSource2 = dataSource;
        return getCommand(str, str2);
    }

    public DataContentHandler createDataContentHandler(String str, DataSource dataSource) {
        DataSource dataSource2 = dataSource;
        return createDataContentHandler(str);
    }

    public String[] getMimeTypes() {
        return null;
    }
}
