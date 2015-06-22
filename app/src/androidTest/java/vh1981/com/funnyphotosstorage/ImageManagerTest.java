package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * Created by yeonhuikim on 15. 6. 23..
 */
public class ImageManagerTest extends AndroidTestCase {
    Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getContext();

        ImageManager mananger = ImageManager.instance();

    }


}
