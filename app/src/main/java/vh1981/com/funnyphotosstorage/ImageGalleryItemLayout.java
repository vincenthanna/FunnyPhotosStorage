package vh1981.com.funnyphotosstorage;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.*;

import Utils.DebugLog;

/**
 * Created by vh1981 on 15. 2. 3.
 */
public class ImageGalleryItemLayout extends FrameLayout {
    Activity _activity;
    Context _context;
    ImageView _imageView;
    ImageView _imageViewCheck;
    Image _image;
    boolean _multiSelectMode = false;
    boolean _resized = false;

    public ImageGalleryItemLayout(Context context, Activity activity) {
        super(context);
        _activity = activity;
        _context = context;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.image_gallery_item, this);
        _imageView = (ImageView) findViewById(R.id.iv_galleryitem);
        _imageViewCheck = (ImageView) findViewById(R.id.iv_check);
        assert _imageView != null;
        _imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        _imageViewCheck.setVisibility(View.GONE);
    }

    public void setImage(Image image, boolean multiSelectMode)
    {
        boolean needTransitionInstant = false;
        if (_image != null && !_image.equals(image)) {
            needTransitionInstant = true;
        }
        _image = image;
        _multiSelectMode = multiSelectMode;
        Bitmap bitmap = image.get_thumbnailBitmap();
        _imageView.setImageBitmap(bitmap);
        transition(needTransitionInstant);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        transition(false);
    }

    public void transition(boolean instant) {
        showCheck();
        float scale = 0.2f;
        int duration = 120;
        if (instant) {
            duration = 0;
        }
        if (_image.checked()) {
            setBackgroundColor(Color.argb(100, 200,200,200));
            if (!_resized) {
                _imageView.animate().scaleXBy(-scale).scaleYBy(-scale).setDuration(duration).start();
                _resized = true;
            }
        }
        else {
            setBackgroundColor(Color.TRANSPARENT);
            if (_resized) {
                _imageView.animate().scaleXBy(scale).scaleYBy(scale).setDuration(duration).start();
                _resized = false;
            }
        }
    }

    void showCheck()
    {
        boolean showCheck = false;
        if (_multiSelectMode) {
            if (_image.checked()) {
                showCheck = true;
            } else {
                showCheck = false;
            }
        }
        else {
            showCheck = false;
        }
        _imageViewCheck.setVisibility(showCheck ? View.VISIBLE : View.GONE);
    }
}
