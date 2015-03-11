package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.*;

/**
 * Created by vh1981 on 15. 2. 3.
 */
public class ImageGalleryItemLayout extends RelativeLayout {
    Activity _activity;
    Context _context;
    ImageView _imageView;

    public ImageGalleryItemLayout(Context context, Activity activity) {
        super(context);
        _activity = activity;
        _context = context;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.image_gallery_item, this);
        _imageView = (ImageView) findViewById(R.id.iv_galleryitem);
        assert _imageView != null;
    }

    public void setBitmap(Bitmap bitmap, int height) {
        _imageView.setImageBitmap(bitmap);
        _imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        _imageView.getLayoutParams().height = height;
        _imageView.requestLayout();
    }
}
