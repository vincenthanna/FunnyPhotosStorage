package vh1981.com.funnyphotosstorage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;

public class SwipeImageViewFragment extends Fragment {
    Bitmap _bitmapToShow;
    ImageView _imageView;

    public void setImageBitmap(Bitmap bitmap)
    {
        _bitmapToShow = bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _imageView = new ImageView(getActivity());
        _imageView.setImageBitmap(_bitmapToShow);
        return _imageView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup.LayoutParams lp = _imageView.getLayoutParams();
        lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        _imageView.setBackgroundColor(Color.BLACK);
        _imageView.setLayoutParams(lp);
        _imageView.setAdjustViewBounds(true);
        _imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }
}

