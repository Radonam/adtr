package adrt;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ADRTLogCatReader implements Runnable {
    private static Context context;

    public ADRTLogCatReader() {
    }

    public static void onContext(Context context, String str) {
        Context context2 = context;
        String str2 = str;
        if (context == null) {
            context = context2.getApplicationContext();
            if ((0 != (context2.getApplicationInfo().flags & 2) ? 1 : null) != null) {
                try {
                    PackageInfo packageInfo = context2.getPackageManager().getPackageInfo(str2, 128);
                    ADRTSender.onContext(context, str2);
                    Thread thread = r9;
                    ADRTLogCatReader aDRTLogCatReader = r9;
                    ADRTLogCatReader aDRTLogCatReader2 = new ADRTLogCatReader();
                    Thread thread2 = new Thread(aDRTLogCatReader, "LogCat");
                    thread.start();
                } catch (NameNotFoundException e) {
                    NameNotFoundException nameNotFoundException = e;
                }
            }
        }
    }

    public void run() {
        try {
            BufferedReader bufferedReader = r9;
            Reader reader = r9;
            Reader inputStreamReader = new InputStreamReader(Runtime.getRuntime().exec("logcat -v threadtime").getInputStream());
            BufferedReader bufferedReader2 = new BufferedReader(reader, 20);
            BufferedReader bufferedReader3 = bufferedReader;
            String str = "";
            while (true) {
                String readLine = bufferedReader3.readLine();
                str = readLine;
                if (readLine != null) {
                    String[] strArr = new String[1];
                    String[] strArr2 = strArr;
                    strArr[0] = str;
                    ADRTSender.sendLogcatLines(strArr2);
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            IOException iOException = e;
        }
    }
}
