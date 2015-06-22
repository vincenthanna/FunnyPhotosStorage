package vh1981.com.funnyphotosstorage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.DebugUtils;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

import java.io.ByteArrayOutputStream;

import TouchImageView.*;
import Utils.DebugLog;


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

        View rootView = inflater.inflate(R.layout.single_image_view_fragment, container, false);
        _imageView = (ImageView) rootView.findViewById(R.id.imageView);
        _imageView.setImageBitmap(_bitmapToShow);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _imageView.setBackgroundColor(Color.BLACK);
        _imageView.setAdjustViewBounds(true);
        _imageView.setPadding(0,0,0,0);
        _imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    public void refreshView()
    {
        DebugLog.TRACE("RefreshView:");
        /// orientation change등에 의해 갱신동작이 필요하면 여기에 넣는다.
    }
}

