package adrt;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;

public class ADRTSender {
    private static Context context;
    private static String debuggerPackageName;

    public ADRTSender() {
    }

    public static void onContext(Context context, String str) {
        String str2 = str;
        context = context;
        debuggerPackageName = str2;
    }

    public static void sendConnect(String str) {
        String str2 = str;
        Intent intent = r5;
        Intent intent2 = new Intent();
        Intent intent3 = intent;
        intent = intent3.setPackage(debuggerPackageName);
        intent = intent3.setAction("com.adrt.CONNECT");
        intent = intent3.putExtra("package", str2);
        context.sendBroadcast(intent3);
    }

    public static void sendStop(String str) {
        String str2 = str;
        Intent intent = r5;
        Intent intent2 = new Intent();
        Intent intent3 = intent;
        intent = intent3.setPackage(debuggerPackageName);
        intent = intent3.setAction("com.adrt.STOP");
        intent = intent3.putExtra("package", str2);
        context.sendBroadcast(intent3);
    }

    public static void sendBreakpointHit(String str, ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3, ArrayList<String> arrayList4, ArrayList<String> arrayList5, ArrayList<String> arrayList6) {
        String str2 = str;
        ArrayList<String> arrayList7 = arrayList;
        ArrayList<String> arrayList8 = arrayList2;
        ArrayList<String> arrayList9 = arrayList3;
        ArrayList<String> arrayList10 = arrayList4;
        ArrayList<String> arrayList11 = arrayList5;
        ArrayList<String> arrayList12 = arrayList6;
        Intent intent = r11;
        Intent intent2 = new Intent();
        Intent intent3 = intent;
        intent = intent3.setPackage(debuggerPackageName);
        intent = intent3.setAction("com.adrt.BREAKPOINT_HIT");
        intent = intent3.putExtra("package", str2);
        intent = intent3.putExtra("variables", arrayList7);
        intent = intent3.putExtra("variableValues", arrayList8);
        intent = intent3.putExtra("variableKinds", arrayList9);
        intent = intent3.putExtra("stackMethods", arrayList10);
        intent = intent3.putExtra("stackLocations", arrayList11);
        intent = intent3.putExtra("stackLocationKinds", arrayList12);
        context.sendBroadcast(intent3);
    }

    public static void sendFields(String str, String str2, ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3) {
        String str3 = str;
        String str4 = str2;
        ArrayList<String> arrayList4 = arrayList;
        ArrayList<String> arrayList5 = arrayList2;
        ArrayList<String> arrayList6 = arrayList3;
        Intent intent = r9;
        Intent intent2 = new Intent();
        Intent intent3 = intent;
        intent = intent3.setPackage(debuggerPackageName);
        intent = intent3.setAction("com.adrt.FIELDS");
        intent = intent3.putExtra("package", str3);
        intent = intent3.putExtra("path", str4);
        intent = intent3.putExtra("fields", arrayList4);
        intent = intent3.putExtra("fieldValues", arrayList5);
        intent = intent3.putExtra("fieldKinds", arrayList6);
        context.sendBroadcast(intent3);
    }

    public static void sendLogcatLines(String[] strArr) {
        String[] strArr2 = strArr;
        Intent intent = r5;
        Intent intent2 = new Intent();
        Intent intent3 = intent;
        intent = intent3.setPackage(debuggerPackageName);
        intent = intent3.setAction("com.adrt.LOGCAT_ENTRIES");
        intent = intent3.putExtra("lines", strArr2);
        context.sendBroadcast(intent3);
    }
}
