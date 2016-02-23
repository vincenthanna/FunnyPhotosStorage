package vh1981.com.funnyphotosstorage;

import android.graphics.Bitmap;

/**
 * Created by vh1981 on 15. 2. 2.
 */
public class Image implements Comparable<Image> {

    String _tag;
    public String get_tag() { return _tag; }
    public void set_tag(String _tag) { this._tag = _tag; }
    String _filename;
    Bitmap _thumbnailBitmap = null;
    BitmapSupplier _bitmapSupplier = null;
    boolean _checked = false;
    public void setCheck(boolean checked) {
        _checked = checked;
    }
    public boolean checked() {
        return _checked;
    }
    public void checkToggle()
    {
        _checked = !_checked;
    }

    public String get_filepath() { return _filename; }
    public String get_filename() { return _filename; }

    public Bitmap get_bitmap()
    {
        /*
        if (_bitmap == null) {
            assert _bitmapSupplier != null;
            _bitmap = _bitmapSupplier.getBitmap(ImageFileManager.directoryPath(_filePath));
        }
        /// 파일이 삭제되어 이미지를 읽을 수 없는 경우를 고려해야 한다.
        assert _bitmap != null;
        return _bitmap;
        */
        assert _bitmapSupplier != null;
        return _bitmapSupplier.getBitmap(ImageFileManager.directoryPath(_filename));
    }

    public Bitmap get_thumbnailBitmap()
    {
        if (_thumbnailBitmap == null) {
            assert _bitmapSupplier != null;
            Bitmap bitmap = _bitmapSupplier.getBitmap(ImageFileManager.directoryPath(_filename));

            int width = 300;
            int height = width * bitmap.getHeight() / bitmap.getWidth();
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            _thumbnailBitmap = scaledBitmap;
        }
        /// 파일이 삭제되어 이미지를 읽을 수 없는 경우를 고려해야 한다.
        assert _thumbnailBitmap != null;
        return _thumbnailBitmap;
    }

    public Image() {
        init("", "", null);
    }

    public Image(String filePath, String tag, BitmapSupplier bitmapSupplier)
    {
        init(filePath, tag, bitmapSupplier);
    }

    public void init(String filepath, String tag, BitmapSupplier bitmapSupplier)
    {
        //_tagId = tagId;
        _tag = tag;
        //_fileId = fileId;
        _filename = filepath;
        _bitmapSupplier = bitmapSupplier;
    }


    @Override
    public int compareTo(Image image) {
        return _filename.compareTo(image.get_filename());
    }

    public void unloadBitmap()
    {
        //_bitmap = null;
        _thumbnailBitmap = null;
    }
}
