package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by vh1981 on 15. 1. 5.
 */
public class NewImageListViewItem_Image extends BaseListViewItem {
    ImageView _imageView;

    public NewImageListViewItem_Image(Context context, Activity activity) {
        super(context);
        this._context = context;
        _activity = activity;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.lv_new_image_image, this);
        _listViewType = BaseListViewItemType.TYPE_Category.TYPE_NewImage;

        _imageView = (ImageView) findViewById(R.id.imageview);
        if (_imageView == null) {
            assert false;
        }
    }

    public void setActivity(Activity activity)
    {
        this._activity = activity;
    }

    public void initUI() {

    }

    public void setImage(Drawable drawable) {

        assert drawable != null;

        if (drawable != null) {
            _imageView.setImageDrawable(drawable);
        }
    }

    public void setImage(Bitmap bitmap) {

        assert bitmap != null;

        if (bitmap != null) {
            _imageView.setImageBitmap(bitmap);
        }
    }
}