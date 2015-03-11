package Utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.*;


/**
 * Created by vh1981 on 15. 1. 28.
 */
public class BitmapUtil {
    static Context _context;
    public static void setContext(Context context)
    {
        BitmapUtil._context = context;
    }

    /**
     * Title : Bitmap을 jpeg파일로 생성해서 strFilePath에 저장한다.
     *
     * @param bitmap : 파일로 생성할 bitmap
     * @param strFilePath : 생성할 파일의 path(경로 + 이름)
     * @return success : 성공하면 true, 실패하면 false.
     */
    public static boolean SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {
        FileOutputStream out = null;
        try {
            //out = _context.openFileOutput(strFilePath, Context.MODE_PRIVATE);
            out = new FileOutputStream(strFilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            File f = new File(strFilePath);
            if (f.exists() == true) {
                DebugLog.TRACE("File " + strFilePath + " exists!");
            }
            else {
                DebugLog.TRACE("Error : File " + strFilePath + " NOT! exists!");
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
