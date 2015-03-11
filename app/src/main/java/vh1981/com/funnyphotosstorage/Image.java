package vh1981.com.funnyphotosstorage;

import android.graphics.Bitmap;

/**
 * Created by vh1981 on 15. 2. 2.
 */
public class Image implements Comparable<Image> {
    String _tag;
    String _filePath;
    int _tagId;
    int _fileId;
    Bitmap _bitmap = null;
    BitmapSupplier _bitmapSupplier = null;

    public int get_tagId() { return _tagId; }
    public String get_tag() { return _tag; }
    public int get_fileId() { return _fileId; }
    public String get_filepath() { return _filePath; }

    public Bitmap get_bitmap()
    {
        if (_bitmap == null) {
            assert _bitmapSupplier != null;
            _bitmap = _bitmapSupplier.getBitmap(_filePath);
        }
        assert _bitmap != null;
        return _bitmap;
    }

    public Image() {
        init(-1, "", -1, "", null);
    }

    public Image(int fileId, String filePath, int tagId, String tag, BitmapSupplier bitmapSupplier)
    {
        init(fileId, filePath, tagId, tag, bitmapSupplier);
    }

    public void init(int fileId, String filepath, int tagId, String tag, BitmapSupplier bitmapSupplier)
    {
        _tagId = tagId;
        _tag = tag;
        _fileId = fileId;
        _filePath = filepath;
        _bitmapSupplier = bitmapSupplier;
    }

    @Override
    public int compareTo(Image image) {
        return (get_fileId() - image.get_fileId());
    }

}
