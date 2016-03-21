package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import Utils.BitmapUtil;
import Utils.CommonUtil;
import Utils.DebugLog;

/**
 * Created by yeonhuikim on 14. 12. 27..
 */
public class ImageFileManager implements BitmapSupplier {

    private static ImageFileManager _instance;
    private static Context _context;
    ImageContainer _imgContainerAll = new ImageContainer();

    public static void setContext(Context context) {
        ImageFileManager._context = context;
    }

    public static ImageFileManager getInstance() {
        if (_instance == null) {
            _instance = new ImageFileManager();
            _instance.updateImageFileList();
        }
        return _instance;
    }

    public static String directoryPath(String filename) {
        String str = ImageFileManager._context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        if (filename != null) {
            str += "/"  + filename;
        }
        return str;
    }

    public String createImageFile(Bitmap bitmap, String tag)
    {
        // unique한 파일 이름을 생성한다.
        String fileName = UUID.randomUUID().toString();
        while (FileOpsHelper.isFile(new File(directoryPath(fileName)))) {
            fileName = UUID.randomUUID().toString();
        }

        String filePath = fileName += ".jpg";
        filePath = directoryPath(filePath);

        if (!BitmapUtil.SaveBitmapToFileCache(bitmap, filePath, tag)) {
            return null;
        }
        Image img = new Image(fileName, tag, this);
        _imgContainerAll.addImage(img);

        return fileName;
    }

    public boolean removeImageFile(Image image)
    {
        String filePath = directoryPath(image.get_filename());
        File file = new File(filePath);
        if (FileOpsHelper.isFile(file)) {
            FileOpsHelper.deleteFile(file);
            _imgContainerAll.removeImage(image);
            return true;
        }
        return false;
    }

    public boolean changeImageFileTag(Image image, String newTag)
    {
        String filePath = directoryPath(image.get_filename());
        if (BitmapUtil.changeImageFileTag(filePath, newTag)) {

            _imgContainerAll.findImage(image).set_tag(newTag);
            return true;
        }
        return false;
    }

    public ImageContainer getImageContainer(String tag)
    {
        ImageContainer container = new ImageContainer();
        for (int i = 0 ; i < _imgContainerAll.size(); i++) {
            Image img = _imgContainerAll.get(i);
            img.setCheck(false);
            if (img.get_tag().compareTo(tag) == 0) {
                container.addImage(img);
            }
        }
        return container;
    }

    public ArrayList<String> getImageFilesArray()
    {
        String dir = directoryPath(null);
        File f = new File(dir);
        File file[] = f.listFiles();

        ArrayList<String> alFiles = new ArrayList<String>();
        for (int i = 0; i < file.length; i++) {
            alFiles.add(file[i].getAbsolutePath());
        }

        return alFiles;
    }

    public void updateImageFileList()
    {
        String dir = directoryPath(null);
        File f = new File(dir);
        File file[] = f.listFiles();

        _imgContainerAll.clear();
        ArrayList<String> alFiles = new ArrayList<String>();
        for (int i = 0; i < file.length; i++) {
            String fileName = file[i].getName();
            alFiles.add(fileName);

            ExifInterface exif = null;
            try {
                exif = new ExifInterface(directoryPath(fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (exif != null) {
                String tag = exif.getAttribute(ExifInterface.TAG_MODEL);

                Image img = new Image(fileName, CommonUtil.decodeString(tag), this);
                _imgContainerAll.addImage(img);
            }
        }
    }

    public String getTagFromImageFile(String fileName)
    {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(directoryPath(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exif != null) {
            String tag = exif.getAttribute(ExifInterface.TAG_MODEL);
            return tag;
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////
    /// BitmapSupplier 인터페이스 구현부:
    ////////////////////////////////////////////////////////////////////
    public Bitmap getBitmap(String bitmapFileName)
    {
        String fileName = bitmapFileName;
        if (bitmapFileName.contains("/") == false) {
            fileName = directoryPath(bitmapFileName);
        }

        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        assert bitmap != null;

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tag = exif.getAttribute(ExifInterface.TAG_MODEL);

        return bitmap;
    }

    public void unloadBitmaps(String exceptionTag)
    {
        for (int i = 0 ; i < _imgContainerAll.size(); i++) {
            Image img = _imgContainerAll.get(i);
            if (img.get_tag().compareTo(exceptionTag) != 0) {
                img.unloadBitmap();
            }
        }
    }
}
