package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;

import java.io.File;

import Utils.DebugLog;

public class SingleImageViewActivity extends FragmentActivity implements SwipeImageViewDataSource {
    SwipeImageViewAdapter _adapter;
    ViewPager _viewPager;
    SingleImageViewActivity _activity;
    String _tag;
    int _index;
    ImageContainer _imageContainer;
    Button _shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_view);
        Intent intent = getIntent();

        _activity = this;

        /// 넘어온 tag에 해당하는 이미지들을 불러온다.
        _tag = intent.getStringExtra(ImageGalleryActivity.IE_TAG);
        int idx = intent.getIntExtra(ImageGalleryActivity.IE_INDEX, 0);
        _imageContainer = ImageManager.instance().getImageContainer(_tag);

        DebugLog.TRACE("tag=" + _tag + " index=" + idx);

        _adapter = new SwipeImageViewAdapter(getSupportFragmentManager());
        _adapter.set_dataSource(this);

        _viewPager = (ViewPager) findViewById(R.id.imageviewpager);
        _viewPager.setAdapter(_adapter);
        _viewPager.setCurrentItem(idx, false);
        /// ViewPager page변경에 따른 index 변경을 반영 :
        _viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                DebugLog.TRACE("index = " + position);
                _index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /// orientation 변경을 처리하고 싶을 때 다음의 코드 사용
        OrientationEventListener orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int i) {
                DebugLog.TRACE("Hello");
            }
        };

        orientationEventListener.enable();

        _shareButton = (Button) findViewById(R.id.button_share);
        _shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShareActivity();
            }
        });

        String title = getResources().getString(R.string.title_activity_single_image_view);
        title += " - " + _tag;
        setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_image_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_share) {
            startShareActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    /// share intent로 activity를 시작 :
    void startShareActivity() {
        DebugLog.TRACE("_tag=" + _tag + " index=" + _index);
        Uri uri = Uri.fromFile(new File(_imageContainer.get(_index).get_filepath()));
        Intent intent = new Intent("image/jpg", uri);
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/jpeg");
        startActivity(Intent.createChooser(intent, "Send"));
    }

    int _pageIndex = 0;
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        System.out.println("TAG, onSavedInstanceState");
        _pageIndex = _viewPager.getCurrentItem();
    }
    protected void onRestoreInstanceState(Bundle savedState)
    {
        SwipeImageViewFragment fragment = (SwipeImageViewFragment) _adapter.getCurrentFragment();
        if (fragment != null) {
            fragment.setImageBitmap(getBitmap(_pageIndex));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }

        SwipeImageViewFragment fragment = (SwipeImageViewFragment) _adapter.getCurrentFragment();
        if (fragment != null) {
            fragment.refreshView();
        }
    }

    ////////////////////////////////////////////////////////////////////////////

    /// SwipeImageViewDataSource delegate :
    @Override
    public Bitmap getBitmap(int index) {
        return _imageContainer.get(index).get_bitmap();
    }

    @Override
    public int getCount() {
        return _imageContainer.size();
    }
}