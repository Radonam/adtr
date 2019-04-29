package javax.activation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import myjava.awt.datatransfer.DataFlavor;
import myjava.awt.datatransfer.UnsupportedFlavorException;

/* compiled from: DataHandler */
class ObjectDataContentHandler implements DataContentHandler {
    private DataContentHandler dch = null;
    private String mimeType;
    private Object obj;
    private DataFlavor[] transferFlavors = null;

    public ObjectDataContentHandler(DataContentHandler dataContentHandler, Object obj, String str) {
        DataContentHandler dataContentHandler2 = dataContentHandler;
        Object obj2 = obj;
        String str2 = str;
        this.obj = obj2;
        this.mimeType = str2;
        this.dch = dataContentHandler2;
    }

    public DataContentHandler getDCH() {
        return this.dch;
    }

    public synchronized DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] dataFlavorArr;
        synchronized (this) {
            if (this.transferFlavors == null) {
                if (this.dch != null) {
                    this.transferFlavors = this.dch.getTransferDataFlavors();
                } else {
                    this.transferFlavors = new DataFlavor[1];
                    DataFlavor[] dataFlavorArr2 = this.transferFlavors;
                    DataFlavor dataFlavor = r9;
                    DataFlavor activationDataFlavor = new ActivationDataFlavor(this.obj.getClass(), this.mimeType, this.mimeType);
                    dataFlavorArr2[0] = dataFlavor;
                }
            }
            dataFlavorArr = this.transferFlavors;
        }
        return dataFlavorArr;
    }

    public Object getTransferData(DataFlavor dataFlavor, DataSource dataSource) throws UnsupportedFlavorException, IOException {
        DataFlavor dataFlavor2 = dataFlavor;
        DataSource dataSource2 = dataSource;
        if (this.dch != null) {
            return this.dch.getTransferData(dataFlavor2, dataSource2);
        }
        if (dataFlavor2.equals(getTransferDataFlavors()[0])) {
            return this.obj;
        }
        UnsupportedFlavorException unsupportedFlavorException = r6;
        UnsupportedFlavorException unsupportedFlavorException2 = new UnsupportedFlavorException(dataFlavor2);
        throw unsupportedFlavorException;
    }

    public Object getContent(DataSource dataSource) {
        DataSource dataSource2 = dataSource;
        return this.obj;
    }

    public void writeTo(Object obj, String str, OutputStream outputStream) throws IOException {
        Object obj2 = obj;
        String str2 = str;
        OutputStream outputStream2 = outputStream;
        if (this.dch != null) {
            this.dch.writeTo(obj2, str2, outputStream2);
        } else if (obj2 instanceof byte[]) {
            outputStream2.write((byte[]) obj2);
        } else if (obj2 instanceof String) {
            OutputStreamWriter outputStreamWriter = r10;
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(outputStream2);
            OutputStreamWriter outputStreamWriter3 = outputStreamWriter;
            outputStreamWriter3.write((String) obj2);
            outputStreamWriter3.flush();
        } else {
            UnsupportedDataTypeException unsupportedDataTypeException = r10;
            StringBuilder stringBuilder = r10;
            StringBuilder stringBuilder2 = new StringBuilder("no object DCH for MIME type ");
            UnsupportedDataTypeException unsupportedDataTypeException2 = new UnsupportedDataTypeException(stringBuilder.append(this.mimeType).toString());
            throw unsupportedDataTypeException;
        }
    }
}
