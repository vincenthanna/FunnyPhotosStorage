package Utils;

/**
 * Created by yeonhuikim on 14. 12. 28..
 */
public class DebugLog {

    public static void TRACE() {
        java.lang.Exception e = new java.lang.Exception();
        StackTraceElement ste[] = e.getStackTrace();

        System.out.println("[Trace] " + ste[1].getClassName() + "."
                + ste[1].getMethodName() + "(" + ste[1].getFileName() + ":"
                + ste[1].getLineNumber() + ")");
    }

    public static void TRACE(String str) {
        java.lang.Exception e = new java.lang.Exception();
        StackTraceElement ste[] = e.getStackTrace();

        System.out.println("[Trace] " + ste[1].getClassName() + "."
                + ste[1].getMethodName() + "(" + ste[1].getFileName() + ":"
                + ste[1].getLineNumber() + ")" + str);
    }

    public static void STACK_TRACE() {
        boolean first = true;

        java.lang.Exception e = new java.lang.Exception();
        StackTraceElement array[] = e.getStackTrace();

        System.out.println("[StackTrace]");
        for(StackTraceElement ste : array) {
            if(first == true) { first = false; continue; }

            System.out.println("        " + ste.getClassName() + "."
                    + ste.getMethodName() + "(" + ste.getFileName() + ":"
                    + ste.getLineNumber() + ")");
        }
    }
}
