package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;

import java.util.ArrayList;
import java.util.UUID;

import Utils.BitmapUtil;

import static junit.framework.Assert.*;

/**
 * Created by yeonhuikim on 14. 12. 27..
 * UI에서 직접 이미지 처리동작을 위해 접근하는 클래스이다.
 */

public class ImageManager {
    private static ImageManager _instance = null;
    private static Context _context;
    public static void setContext(Context context)
    {
        ImageManager._context = context;
    }

    private ImageFileManager _imgFileManager;
    private ImageDatabaseManager _imgDatabaseManager;

    private ImageManager()
    {
        assertTrue(ImageManager._context != null);
        _imgFileManager.setContext(ImageManager._context);
        _imgFileManager = ImageFileManager.getInstance();
        _imgDatabaseManager.setContext(ImageManager._context);
        _imgDatabaseManager = ImageDatabaseManager.getInstance();
    }

    public static ImageManager instance()
    {
        if (_instance == null) {
            _instance = new ImageManager();
        }
        return _instance;
    }

    boolean createImage(Bitmap bitmap, String tag)
    {
        // bitmap을 파일로 저장한다.
        String imageFileName = _imgFileManager.createImageFile(bitmap, tag);
        return true;
    }

    boolean removeImage(Image image)
    {
        return _imgFileManager.removeImageFile(image);
    }

    boolean changeImageTag(Image image, String tag)
    {
        return _imgFileManager.changeImageFileTag(image, tag);
    }

    public ImageContainer getImageContainer(String tag) {
        return _imgFileManager.getImageContainer(tag);
    }

    //////////////////////////////////////////////////////////////////////////////
    /// tag 관련 함수들
    //////////////////////////////////////////////////////////////////////////////
    public boolean addTag(String tag)
    {
        return _imgDatabaseManager.addTag(tag);
    }

    public boolean removeTag(String tag)
    {
        return _imgDatabaseManager.removeTag(tag);
    }

    public boolean tagExists(String tag)
    {
        return _imgDatabaseManager.tagExists(tag);
    }

    public ArrayList<String> getTags()
    {
        return _imgDatabaseManager.getTags();
    }

    //////////////////////////////////////////////////////////////////////////////
    /// misc
    //////////////////////////////////////////////////////////////////////////////
    public String dbFilePath() {
        return _imgDatabaseManager.dbFilePath();
    }
    public ArrayList<String> imageFiles()
    {
        return _imgFileManager.getImageFilesArray();
    }

    public void unloadBitmaps(String tagToRemain)
    {
        _imgFileManager.unloadBitmaps(tagToRemain);
    }
}