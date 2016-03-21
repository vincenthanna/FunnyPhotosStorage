package Utils;

import android.util.Base64;

import java.util.ArrayList;

/**
 * Created by vh1981 on 15. 10. 29.
 */
public class CommonUtil<T> {
    public static String selectedCharset = "UTF-8";

    public static String encodeString(String str) {
        try {
            byte ptext[] = str.getBytes(selectedCharset);
            String byteArrayStr = Base64.encodeToString(ptext, 0);
            return byteArrayStr;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decodeString(String encodedStr)
    {
        try {
            byte ttext[] = Base64.decode(encodedStr, 0);
            return new String(ttext, selectedCharset);
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static <T>boolean isExists(ArrayList<T> arr, T target)
    {
        for (T item : arr) {
            if (item.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
