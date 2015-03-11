package vh1981.com.funnyphotosstorage;

import android.graphics.Bitmap;

/**
 * Created by vh1981 on 15. 2. 2.
 */
public interface BitmapSupplier {
    Bitmap getBitmap(String filepath);
    Bitmap getBitmap(int imageId);
}
