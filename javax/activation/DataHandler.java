package javax.activation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import myjava.awt.datatransfer.DataFlavor;
import myjava.awt.datatransfer.Transferable;
import myjava.awt.datatransfer.UnsupportedFlavorException;

public class DataHandler implements Transferable {
    private static final DataFlavor[] emptyFlavors = new DataFlavor[0];
    private static DataContentHandlerFactory factory = null;
    private CommandMap currentCommandMap = null;
    private DataContentHandler dataContentHandler = null;
    private DataSource dataSource = null;
    private DataContentHandler factoryDCH = null;
    private DataSource objDataSource = null;
    private Object object = null;
    private String objectMimeType = null;
    private DataContentHandlerFactory oldFactory = null;
    private String shortType = null;
    private DataFlavor[] transferFlavors = emptyFlavors;

    public DataHandler(DataSource dataSource) {
        DataSource dataSource2 = dataSource;
        this.dataSource = dataSource2;
        this.oldFactory = factory;
    }

    public DataHandler(Object obj, String str) {
        Object obj2 = obj;
        String str2 = str;
        this.object = obj2;
        this.objectMimeType = str2;
        this.oldFactory = factory;
    }

    public DataHandler(URL url) {
        URL url2 = url;
        URLDataSource uRLDataSource = r6;
        URLDataSource uRLDataSource2 = new URLDataSource(url2);
        this.dataSource = uRLDataSource;
        this.oldFactory = factory;
    }

    private synchronized CommandMap getCommandMap() {
        CommandMap commandMap;
        synchronized (this) {
            if (this.currentCommandMap != null) {
                commandMap = this.currentCommandMap;
            } else {
                commandMap = CommandMap.getDefaultCommandMap();
            }
        }
        return commandMap;
    }

    public DataSource getDataSource() {
        if (this.dataSource != null) {
            return this.dataSource;
        }
        if (this.objDataSource == null) {
            DataHandlerDataSource dataHandlerDataSource = r5;
            DataHandlerDataSource dataHandlerDataSource2 = new DataHandlerDataSource(this);
            this.objDataSource = dataHandlerDataSource;
        }
        return this.objDataSource;
    }

    public String getName() {
        if (this.dataSource != null) {
            return this.dataSource.getName();
        }
        return null;
    }

    public String getContentType() {
        if (this.dataSource != null) {
            return this.dataSource.getContentType();
        }
        return this.objectMimeType;
    }

    public InputStream getInputStream() throws IOException {
        InputStream inputStream;
        Object obj = null;
        if (this.dataSource != null) {
            inputStream = this.dataSource.getInputStream();
        } else {
            DataContentHandler dataContentHandler = getDataContentHandler();
            UnsupportedDataTypeException unsupportedDataTypeException;
            StringBuilder stringBuilder;
            StringBuilder stringBuilder2;
            UnsupportedDataTypeException unsupportedDataTypeException2;
            if (dataContentHandler == null) {
                unsupportedDataTypeException = r13;
                stringBuilder = r13;
                stringBuilder2 = new StringBuilder("no DCH for MIME type ");
                unsupportedDataTypeException2 = new UnsupportedDataTypeException(stringBuilder.append(getBaseType()).toString());
                throw unsupportedDataTypeException;
            } else if ((dataContentHandler instanceof ObjectDataContentHandler) && ((ObjectDataContentHandler) dataContentHandler).getDCH() == null) {
                unsupportedDataTypeException = r13;
                stringBuilder = r13;
                stringBuilder2 = new StringBuilder("no object DCH for MIME type ");
                unsupportedDataTypeException2 = new UnsupportedDataTypeException(stringBuilder.append(getBaseType()).toString());
                throw unsupportedDataTypeException;
            } else {
                DataContentHandler dataContentHandler2 = dataContentHandler;
                PipedOutputStream pipedOutputStream = r13;
                PipedOutputStream pipedOutputStream2 = new PipedOutputStream();
                PipedOutputStream pipedOutputStream3 = pipedOutputStream;
                InputStream inputStream2 = r13;
                InputStream pipedInputStream = new PipedInputStream(pipedOutputStream3);
                InputStream inputStream3 = inputStream2;
                Thread thread = r13;
                AnonymousClass1 anonymousClass1 = r13;
                final PipedOutputStream pipedOutputStream4 = pipedOutputStream3;
                final DataContentHandler dataContentHandler3 = dataContentHandler2;
                AnonymousClass1 anonymousClass12 = new Runnable(this) {
                    final /* synthetic */ DataHandler this$0;

                    public void run() {
                        IOException iOException;
                        try {
                            dataContentHandler3.writeTo(this.this$0.object, this.this$0.objectMimeType, pipedOutputStream4);
                            try {
                                pipedOutputStream4.close();
                            } catch (IOException e) {
                                iOException = e;
                            }
                        } catch (IOException e2) {
                            IOException iOException2 = e2;
                            try {
                                pipedOutputStream4.close();
                            } catch (IOException e22) {
                                iOException = e22;
                            }
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            try {
                                pipedOutputStream4.close();
                            } catch (IOException e222) {
                                iOException = e222;
                            }
                            Throwable th3 = th2;
                        }
                    }
                };
                Thread thread2 = new Thread(anonymousClass1, "DataHandler.getInputStream");
                thread.start();
                inputStream = inputStream3;
            }
        }
        return inputStream;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        OutputStream outputStream2 = outputStream;
        if (this.dataSource != null) {
            Object obj = null;
            byte[] bArr = new byte[8192];
            InputStream inputStream = this.dataSource.getInputStream();
            while (true) {
                try {
                    int read = inputStream.read(bArr);
                    int i = read;
                    if (read <= 0) {
                        inputStream.close();
                        obj = null;
                        return;
                    }
                    outputStream2.write(bArr, 0, i);
                } catch (Throwable th) {
                    Throwable th2 = th;
                    inputStream.close();
                    obj = null;
                    Throwable th3 = th2;
                }
            }
        } else {
            getDataContentHandler().writeTo(this.object, this.objectMimeType, outputStream2);
        }
    }

    public OutputStream getOutputStream() throws IOException {
        if (this.dataSource != null) {
            return this.dataSource.getOutputStream();
        }
        return null;
    }

    public synchronized DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] dataFlavorArr;
        synchronized (this) {
            if (factory != this.oldFactory) {
                this.transferFlavors = emptyFlavors;
            }
            if (this.transferFlavors == emptyFlavors) {
                this.transferFlavors = getDataContentHandler().getTransferDataFlavors();
            }
            dataFlavorArr = this.transferFlavors;
        }
        return dataFlavorArr;
    }

    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        DataFlavor dataFlavor2 = dataFlavor;
        DataFlavor[] transferDataFlavors = getTransferDataFlavors();
        for (DataFlavor equals : transferDataFlavors) {
            if (equals.equals(dataFlavor2)) {
                return true;
            }
        }
        return false;
    }

    public Object getTransferData(DataFlavor dataFlavor) throws UnsupportedFlavorException, IOException {
        return getDataContentHandler().getTransferData(dataFlavor, this.dataSource);
    }

    public synchronized void setCommandMap(CommandMap commandMap) {
        CommandMap commandMap2 = commandMap;
        synchronized (this) {
            if (commandMap2 != this.currentCommandMap || commandMap2 == null) {
                this.transferFlavors = emptyFlavors;
                this.dataContentHandler = null;
                this.currentCommandMap = commandMap2;
            }
        }
    }

    public CommandInfo[] getPreferredCommands() {
        if (this.dataSource != null) {
            return getCommandMap().getPreferredCommands(getBaseType(), this.dataSource);
        }
        return getCommandMap().getPreferredCommands(getBaseType());
    }

    public CommandInfo[] getAllCommands() {
        if (this.dataSource != null) {
            return getCommandMap().getAllCommands(getBaseType(), this.dataSource);
        }
        return getCommandMap().getAllCommands(getBaseType());
    }

    public CommandInfo getCommand(String str) {
        String str2 = str;
        if (this.dataSource != null) {
            return getCommandMap().getCommand(getBaseType(), str2, this.dataSource);
        }
        return getCommandMap().getCommand(getBaseType(), str2);
    }

    public Object getContent() throws IOException {
        if (this.object != null) {
            return this.object;
        }
        return getDataContentHandler().getContent(getDataSource());
    }

    public Object getBean(CommandInfo commandInfo) {
        CommandInfo commandInfo2 = commandInfo;
        Object obj = null;
        Object obj2 = null;
        try {
            ClassLoader contextClassLoader = SecuritySupport.getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = getClass().getClassLoader();
            }
            obj = commandInfo2.getCommandObject(this, contextClassLoader);
        } catch (IOException e) {
            IOException iOException = e;
        } catch (ClassNotFoundException e2) {
            ClassNotFoundException classNotFoundException = e2;
        }
        return obj;
    }

    private synchronized DataContentHandler getDataContentHandler() {
        DataContentHandler dataContentHandler;
        synchronized (this) {
            if (factory != this.oldFactory) {
                this.oldFactory = factory;
                this.factoryDCH = null;
                this.dataContentHandler = null;
                this.transferFlavors = emptyFlavors;
            }
            if (this.dataContentHandler != null) {
                dataContentHandler = this.dataContentHandler;
            } else {
                String baseType = getBaseType();
                if (this.factoryDCH == null && factory != null) {
                    this.factoryDCH = factory.createDataContentHandler(baseType);
                }
                if (this.factoryDCH != null) {
                    this.dataContentHandler = this.factoryDCH;
                }
                if (this.dataContentHandler == null) {
                    if (this.dataSource != null) {
                        this.dataContentHandler = getCommandMap().createDataContentHandler(baseType, this.dataSource);
                    } else {
                        this.dataContentHandler = getCommandMap().createDataContentHandler(baseType);
                    }
                }
                if (this.dataSource != null) {
                    DataSourceDataContentHandler dataSourceDataContentHandler = r9;
                    DataSourceDataContentHandler dataSourceDataContentHandler2 = new DataSourceDataContentHandler(this.dataContentHandler, this.dataSource);
                    this.dataContentHandler = dataSourceDataContentHandler;
                } else {
                    ObjectDataContentHandler objectDataContentHandler = r9;
                    ObjectDataContentHandler objectDataContentHandler2 = new ObjectDataContentHandler(this.dataContentHandler, this.object, this.objectMimeType);
                    this.dataContentHandler = objectDataContentHandler;
                }
                dataContentHandler = this.dataContentHandler;
            }
        }
        return dataContentHandler;
    }

    private synchronized String getBaseType() {
        String str;
        synchronized (this) {
            if (this.shortType == null) {
                String contentType = getContentType();
                try {
                    MimeType mimeType = r7;
                    MimeType mimeType2 = new MimeType(contentType);
                    MimeType mimeType3 = mimeType;
                    this.shortType = mimeType3.getBaseType();
                } catch (MimeTypeParseException e) {
                    MimeTypeParseException mimeTypeParseException = e;
                    this.shortType = contentType;
                }
            }
            str = this.shortType;
        }
        return str;
    }

    public static synchronized void setDataContentHandlerFactory(DataContentHandlerFactory dataContentHandlerFactory) {
        DataContentHandlerFactory dataContentHandlerFactory2 = dataContentHandlerFactory;
        synchronized (DataHandler.class) {
            if (factory != null) {
                Error error = r7;
                Error error2 = new Error("DataContentHandlerFactory already defined");
                throw error;
            }
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                try {
                    securityManager.checkSetFactory();
                } catch (SecurityException e) {
                    SecurityException securityException = e;
                    if (DataHandler.class.getClassLoader() != dataContentHandlerFactory2.getClass().getClassLoader()) {
                        throw securityException;
                    }
                }
            }
            factory = dataContentHandlerFactory2;
        }
    }
}
