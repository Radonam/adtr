package javax.mail.internet;

import javax.mail.Session;

class UniqueValue {
    private static int id = 0;

    UniqueValue() {
    }

    public static String getUniqueBoundaryValue() {
        StringBuffer stringBuffer = r4;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        stringBuffer = stringBuffer3.append("----=_Part_").append(getUniqueId()).append("_").append(stringBuffer3.hashCode()).append('.').append(System.currentTimeMillis());
        return stringBuffer3.toString();
    }

    public static String getUniqueMessageIDValue(Session session) {
        String address;
        Object obj = null;
        InternetAddress localAddress = InternetAddress.getLocalAddress(session);
        if (localAddress != null) {
            address = localAddress.getAddress();
        } else {
            address = "javamailuser@localhost";
        }
        StringBuffer stringBuffer = r7;
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = stringBuffer;
        stringBuffer = stringBuffer3.append(stringBuffer3.hashCode()).append('.').append(getUniqueId()).append('.').append(System.currentTimeMillis()).append('.').append("JavaMail.").append(address);
        return stringBuffer3.toString();
    }

    private static synchronized int getUniqueId() {
        int i;
        synchronized (UniqueValue.class) {
            int i2 = id;
            i = i2;
            id = i2 + 1;
        }
        return i;
    }
}
