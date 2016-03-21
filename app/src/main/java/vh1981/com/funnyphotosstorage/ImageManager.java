package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.content.SharedPreferences;
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
    boolean _needUpdate = false;
    public void setNeedUpdate() {
        _needUpdate = true;
    }

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
        updateChanged();
        // bitmap을 파일로 저장한다.
        String imageFileName = _imgFileManager.createImageFile(bitmap, tag);
        return true;
    }

    boolean removeImage(Image image)
    {
        updateChanged();
        _imgDatabaseManager.insertDeletedFilename(image);
        return _imgFileManager.removeImageFile(image);
    }

    boolean changeImageTag(Image image, String tag)
    {
        updateChanged();
        _imgDatabaseManager.insertModifiedFilename(image);
        return _imgFileManager.changeImageFileTag(image, tag);
    }

    public ImageContainer getImageContainer(String tag) {
        rebuildIfNeeded();
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

    public ArrayList<String> getDeletedFilenames()
    {
        return _imgDatabaseManager.getDeletedFilenames();
    }

    public void dropDeletedFilename(String filename) {
        _imgDatabaseManager.dropDeletedFilename(filename);
    }

    public ArrayList<String> getModifiedFilenames()
    {
        return _imgDatabaseManager.getModifiedFilenames();
    }

    public void dropModifiedFilename(String filename) {
        _imgDatabaseManager.dropModifiedFilename(filename);
    }

    public String getTagFromImageFile(String fileName)
    {
        return _imgFileManager.getTagFromImageFile(fileName);
    }

    public void rebuildIfNeeded() {
        if (_needUpdate) {
            rebuild();
        }
    }

    public void rebuild() {
        _imgFileManager.updateImageFileList();
        _needUpdate = false;
    }

    //////////////////////////////////////////////////////////////////////////////
    /// backup
    int _changedCount = 0;
    final static int UPDATE_THRESHOLD = 5;
    void updateChanged()
    {
        _changedCount++;
        if (_changedCount > UPDATE_THRESHOLD) {
            if (MySharedPreferences.instance().get_autoBackupEnabled()) {
                BackupManager.instance().putTask(BackupTask.JOB.BACKUP);
            }
            _changedCount = 0;
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    /// misc

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