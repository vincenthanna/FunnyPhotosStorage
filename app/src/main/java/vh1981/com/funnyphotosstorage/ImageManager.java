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

public class ImageManager implements BitmapSupplier {
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


    /**
     * 1. 이미지를 파일로 저장한다.
     * 2. 이미지 파일 이름을 Images 테이블에 저장한다.
     * 3. 이미지 파일과 태그를 ImageTags 테이블에 저장한다.
     * @param bitmap
     * @param tag
     * @return
     */
    boolean createImage(Bitmap bitmap, String tag)
    {
        // bitmap을 파일로 저장한다.
        String imageFileName = _imgFileManager.createImageFile(bitmap);

        // 이미지 파일 이름을 Images 테이블에 저장한다.
        long imgId = _imgDatabaseManager.createImage(imageFileName);

        // tag에 해당하는 tagId를 찾는다.
        int tagId = _imgDatabaseManager.findTagId(tag);

        if (tagId < 0) {
            assert false;
        }

        // tagId - imagefilename 쌍을 저장한다.
        if (_imgDatabaseManager.createImageTag((int)imgId, tag) != true) {
            assert false;
        }

        return false;
    }

    public ArrayList<String> getTags() {
        return _imgDatabaseManager.getTags();
    }


    public ImageContainer getImageContainer(int tagId) {
        return _imgDatabaseManager.getImageContainer(tagId, this);
    }

    public ImageContainer getImageContainer(String tag) {
        int tagId = _imgDatabaseManager.findTagId(tag);
        if (tagId < 0) {
            assert false;
        }
        return _imgDatabaseManager.getImageContainer(tagId, this);
    }

    /// BitmapSupplier 인터페이스 구현부:

    /**
     * Title : BitmapSupplier 인터페이스 구현 - imageId로 파일이름을 찾고 Bitmap 인스턴스를 리턴.
     * @param imageId : Images 테이블에 들어있는 파일의 id
     * @return : Bitmap 인스턴스
     */
    public Bitmap getBitmap(int imageId) {
        String filepath = _imgDatabaseManager.findFilepath(imageId);
        return _imgFileManager.getBitmap(filepath);
    }

    /**
     * Title : BitmapSupplier 인터페이스 구현 - 이미지 파일을 열고 Bitmap을 리턴.
     * @param filepath : 파일 경로
     * @return : Bitmap 인스턴스
     */
    public Bitmap getBitmap(String filepath) {
        return _imgFileManager.getBitmap(filepath);
    }


    /**
     * Title : tag를 생성하는 함수
     * @param tag : 생성할 tag 이름
     * @return : 생성된 tag의 tagId
     */
    public boolean addTag(String tag, long idOrFailReason)
    {
        return _imgDatabaseManager.addTag(tag, idOrFailReason);
    }

    public boolean removeTag(String tag) {
        long tagId = _imgDatabaseManager.findTagId(tag);
        if (tagId >= 0) {
            return _imgDatabaseManager.removeTag(tagId);
        }
        assert false; // cannot happen!
        return false;
    }

    public boolean removeTag(long tagId)
    {
        return _imgDatabaseManager.removeTag(tagId);
    }
}