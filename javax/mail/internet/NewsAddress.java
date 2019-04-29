package javax.mail.internet;

import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.mail.Address;

public class NewsAddress extends Address {
    private static final long serialVersionUID = -4203797299824684143L;
    protected String host;
    protected String newsgroup;

    public NewsAddress() {
    }

    public NewsAddress(String str) {
        this(str, null);
    }

    public NewsAddress(String str, String str2) {
        String str3 = str2;
        this.newsgroup = str;
        this.host = str3;
    }

    public String getType() {
        return "news";
    }

    public void setNewsgroup(String str) {
        this.newsgroup = str;
    }

    public String getNewsgroup() {
        return this.newsgroup;
    }

    public void setHost(String str) {
        this.host = str;
    }

    public String getHost() {
        return this.host;
    }

    public String toString() {
        return this.newsgroup;
    }

    public boolean equals(Object obj) {
        Object obj2 = obj;
        if (!(obj2 instanceof NewsAddress)) {
            return false;
        }
        NewsAddress newsAddress = (NewsAddress) obj2;
        if (!this.newsgroup.equals(newsAddress.newsgroup) || ((this.host != null || newsAddress.host != null) && (this.host == null || newsAddress.host == null || !this.host.equalsIgnoreCase(newsAddress.host)))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        if (this.newsgroup != null) {
            i += this.newsgroup.hashCode();
        }
        if (this.host != null) {
            i += this.host.toLowerCase(Locale.ENGLISH).hashCode();
        }
        return i;
    }

    public static String toString(Address[] addressArr) {
        Address[] addressArr2 = addressArr;
        if (addressArr2 == null || addressArr2.length == 0) {
            return null;
        }
        StringBuffer stringBuffer = r7;
        StringBuffer stringBuffer2 = new StringBuffer(((NewsAddress) addressArr2[0]).toString());
        StringBuffer stringBuffer3 = stringBuffer;
        for (int i = 1; i < addressArr2.length; i++) {
            stringBuffer = stringBuffer3.append(",").append(((NewsAddress) addressArr2[i]).toString());
        }
        return stringBuffer3.toString();
    }

    public static NewsAddress[] parse(String str) throws AddressException {
        StringTokenizer stringTokenizer = r9;
        StringTokenizer stringTokenizer2 = new StringTokenizer(str, ",");
        StringTokenizer stringTokenizer3 = stringTokenizer;
        Vector vector = r9;
        Vector vector2 = new Vector();
        Vector vector3 = vector;
        while (stringTokenizer3.hasMoreTokens()) {
            vector = vector3;
            NewsAddress newsAddress = r9;
            NewsAddress newsAddress2 = new NewsAddress(stringTokenizer3.nextToken());
            vector.addElement(newsAddress);
        }
        int size = vector3.size();
        NewsAddress[] newsAddressArr = new NewsAddress[size];
        if (size > 0) {
            vector3.copyInto(newsAddressArr);
        }
        return newsAddressArr;
    }
}
