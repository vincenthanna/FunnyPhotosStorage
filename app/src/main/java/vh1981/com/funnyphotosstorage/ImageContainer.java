package vh1981.com.funnyphotosstorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Created by vh1981 on 15. 2. 2.
 */
public class ImageContainer {
    ArrayList<Image> _imageArray;

    public ImageContainer() {
        _imageArray = new ArrayList<Image>();
    }

    public void addImage(Image image)
    {
        _imageArray.add(image);
        Collections.sort(_imageArray);
    }

    public Image get(int position)
    {
        assert position < _imageArray.size();

        return _imageArray.get(position);
    }

    public int size() {
        return _imageArray.size();
    }
}
