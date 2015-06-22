package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.test.AndroidTestCase;

/**
 * Created by yeonhuikim on 15. 6. 23..
 */
public class ImageManagerTest extends AndroidTestCase {
    Context context;
    public String _testTag = "TESTTAG_29283";
    ImageManager _imageManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getContext();
        ImageManager.setContext(getContext());
        _imageManager = ImageManager.instance();
    }

    public void testAddDeleteTag() {
        _imageManager.removeTag(_testTag);

        if (_imageManager.addTag(_testTag) == false) {
            fail("");
        }

        long id = _imageManager.findTagId(_testTag);
        if (id < 0) {
            fail();
        }

        if (!_imageManager.findTag((int) id).equals(_testTag)) {
            fail();
        }

        _imageManager.removeTag(id);
    }
}
