package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.UUID;

import Utils.BitmapUtil;

/**
 * Created by yeonhuikim on 14. 12. 27..
 */
public class ImageFileManager {

    private static ImageFileManager _instance;
    private static Context _context;

    public static void setContext(Context context) {
        ImageFileManager._context = context;
    }

    private ImageFileManager() {

    }

    public static ImageFileManager getInstance() {
        if (_instance == null) {
            _instance = new ImageFileManager();
        }
        return _instance;
    }

    /**
     * Title : 파일을 생성하고 파일 경로를 리턴한다.
     *
     * @param bitmap : 생성할 이미지 비트맵
     * @return filename : 생성된 파일 이름
     */
    public String createImageFile(Bitmap bitmap)
    {
        // unique한 파일 이름을 생성한다.
        String filename = UUID.randomUUID().toString();
        filename += ".jpg";
        filename = (ImageFileManager._context.getExternalFilesDir(null) + "/" + filename);

        if (!BitmapUtil.SaveBitmapToFileCache(bitmap, filename)) {
            return null;
        }

        return filename;
    }

    public Bitmap getBitmap(String filename)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(filename);
        assert bitmap != null;
        return bitmap;
    }
}
