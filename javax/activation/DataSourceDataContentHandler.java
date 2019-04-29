package javax.activation;

import java.io.IOException;
import java.io.OutputStream;
import myjava.awt.datatransfer.DataFlavor;
import myjava.awt.datatransfer.UnsupportedFlavorException;

/* compiled from: DataHandler */
class DataSourceDataContentHandler implements DataContentHandler {
    private DataContentHandler dch = null;
    private DataSource ds = null;
    private DataFlavor[] transferFlavors = null;

    public DataSourceDataContentHandler(DataContentHandler dataContentHandler, DataSource dataSource) {
        DataContentHandler dataContentHandler2 = dataContentHandler;
        DataSource dataSource2 = dataSource;
        this.ds = dataSource2;
        this.dch = dataContentHandler2;
    }

    public DataFlavor[] getTransferDataFlavors() {
        if (this.transferFlavors == null) {
            if (this.dch != null) {
                this.transferFlavors = this.dch.getTransferDataFlavors();
            } else {
                this.transferFlavors = new DataFlavor[1];
                DataFlavor[] dataFlavorArr = this.transferFlavors;
                DataFlavor dataFlavor = r7;
                DataFlavor activationDataFlavor = new ActivationDataFlavor(this.ds.getContentType(), this.ds.getContentType());
                dataFlavorArr[0] = dataFlavor;
            }
        }
        return this.transferFlavors;
    }

    public Object getTransferData(DataFlavor dataFlavor, DataSource dataSource) throws UnsupportedFlavorException, IOException {
        DataFlavor dataFlavor2 = dataFlavor;
        DataSource dataSource2 = dataSource;
        if (this.dch != null) {
            return this.dch.getTransferData(dataFlavor2, dataSource2);
        }
        if (dataFlavor2.equals(getTransferDataFlavors()[0])) {
            return dataSource2.getInputStream();
        }
        UnsupportedFlavorException unsupportedFlavorException = r6;
        UnsupportedFlavorException unsupportedFlavorException2 = new UnsupportedFlavorException(dataFlavor2);
        throw unsupportedFlavorException;
    }

    public Object getContent(DataSource dataSource) throws IOException {
        DataSource dataSource2 = dataSource;
        if (this.dch != null) {
            return this.dch.getContent(dataSource2);
        }
        return dataSource2.getInputStream();
    }

    public void writeTo(Object obj, String str, OutputStream outputStream) throws IOException {
        Object obj2 = obj;
        String str2 = str;
        OutputStream outputStream2 = outputStream;
        if (this.dch != null) {
            this.dch.writeTo(obj2, str2, outputStream2);
            return;
        }
        UnsupportedDataTypeException unsupportedDataTypeException = r9;
        StringBuilder stringBuilder = r9;
        StringBuilder stringBuilder2 = new StringBuilder("no DCH for content type ");
        UnsupportedDataTypeException unsupportedDataTypeException2 = new UnsupportedDataTypeException(stringBuilder.append(this.ds.getContentType()).toString());
        throw unsupportedDataTypeException;
    }
}
