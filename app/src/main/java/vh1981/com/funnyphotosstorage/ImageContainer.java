package vh1981.com.funnyphotosstorage;

import java.util.*;

/**
 * Created by vh1981 on 15. 2. 2.
 */
public class ImageContainer {
    ArrayList<Image> _imageArray;

    public ImageContainer() {
        _imageArray = new ArrayList<Image>();
    }

    public ArrayList<Image> array()
    {
        return _imageArray;
    }

    public void addImage(Image image)
    {
        _imageArray.add(image);
    }

    public Image get(int position)
    {
        assert position < _imageArray.size();

        return _imageArray.get(position);
    }

    public void removeImage(Image img)
    {
        boolean needReCheck = true;
        while(needReCheck) {
            needReCheck = false;
            // filename을 key로 사용해서 일치하는 것들을 삭제
            for (int i = 0; i < _imageArray.size(); i++) {
                if (img.get_filename().equals(_imageArray.get(i).get_filename())) {
                    _imageArray.remove(i);
                    needReCheck = true;
                    break;
                }
            }
        }
    }

    public void clear() {
        _imageArray.clear();
    }

    public void checkClear()
    {
        for (Image image : _imageArray) {
            image.setCheck(false);
        }
    }

    public Image findImage(Image img) {
        //for (Image image : _imageArray) {
        for (int i = 0; i < _imageArray.size(); i++) {
            Image image = _imageArray.get(i);
            if (image.get_filename().equals(img.get_filename())) {
                return image;
            }
        }
        return null;
    }

    public int size() {
        return _imageArray.size();
    }

    public ImageContainer copy(boolean checkedOnly)
    {
        ImageContainer container = new ImageContainer();
        for (Image img:_imageArray) {
            if (checkedOnly) {
                if (img.checked()) {
                    container.addImage(img);
                }
            }
            else {
                container.addImage(img);
            }
        }

        return container;
    }
}
