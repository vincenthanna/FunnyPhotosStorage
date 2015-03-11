package vh1981.com.funnyphotosstorage;

import android.graphics.Bitmap;

/**
 * Created by vh1981 on 15. 3. 9.
 */
public interface SwipeImageViewDataSource {
    Bitmap getBitmap(int index);
    int getCount();
}
