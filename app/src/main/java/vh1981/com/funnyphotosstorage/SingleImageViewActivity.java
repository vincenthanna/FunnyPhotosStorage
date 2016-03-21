package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;

import java.io.File;

import Consts.ActivityResultType;
import Utils.DebugLog;

public class SingleImageViewActivity extends FragmentActivity implements SwipeImageViewDataSource {
    SwipeImageViewAdapter _adapter;
    ViewPager _viewPager;
    SingleImageViewActivity _activity;
    String _tag;
    int _index;
    ImageContainer _imageContainer;
    Button _shareButton;
    boolean _fromGetContent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_view);

        ImageManager.setContext(getBaseContext());
        MySharedPreferences.setContext(this.getBaseContext());
        MySharedPreferences.instance().set_intentAction(getIntent().getAction());

        Intent intent = getIntent();

        _activity = this;

        /// 넘어온 tag에 해당하는 이미지들을 불러온다.
        _tag = intent.getStringExtra(ImageGalleryActivity.IE_TAG); // tag
        _index = intent.getIntExtra(ImageGalleryActivity.IE_INDEX, 0); // tag db index
        _imageContainer = ImageManager.instance().getImageContainer(_tag);

        if (intent.getIntExtra(ImageGalleryActivity.IE_ACTION_GET_CONTENT, 0) != 0) {
            _fromGetContent = true;
        }

        DebugLog.TRACE("tag=" + _tag + " index=" + _index);

        _adapter = new SwipeImageViewAdapter(getSupportFragmentManager());
        _adapter.set_dataSource(this);

        _viewPager = (ViewPager) findViewById(R.id.imageviewpager);
        _viewPager.setAdapter(_adapter);
        _viewPager.setCurrentItem(_index, false);
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
        _shareButton.setText(getResources().getString(R.string.action_share));

        if (_fromGetContent) {
            _shareButton.setVisibility(View.INVISIBLE);
        }

        // title 설정 :
        String title = getResources().getString(R.string.title_activity_single_image_view);
        title += " - " + _tag;
        setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (_fromGetContent == true) {
            getMenuInflater().inflate(R.menu.menu_single_image_view_get_content, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_single_image_view, menu);
        }
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
        else if (id == R.id.action_single_image_get_content) {
            if (setImageIntent()) {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }

        SwipeImageViewFragment fragment = (SwipeImageViewFragment) _adapter.getCurrentFragment();
        if (fragment != null) {
            fragment.refreshView();
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        this.setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();
    }

    boolean setImageIntent()
    {
        String filePath = ImageFileManager.directoryPath(_imageContainer.get(_index).get_filename());
        File file = new File(filePath);
        if(file.exists()) {
            // 권한 문제로 FileProvider로 Uri를 얻어야 한다. File.fromFile()은 사용하지 말 것.
            Uri uri = FileProvider.getUriForFile(this.getBaseContext()
                    ,"vh1981.com.funnyphotosstorage.fileprovider"
                    , file);

            Intent result = new Intent("image/jpeg", uri);
            result.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            setResult(Activity.RESULT_OK, result);
            return true;
        }
        return false;
    }

    void startShareActivity() {

        DebugLog.TRACE("_tag=" + _tag + " index=" + _index);

        /*
         * 앱끼리 간단한 데이터를 보내고 받으려면 Intent에 Uri를 넣는 것이 가장 단단하지만, 해당 방법은
         * 'World Reachable'한 External Storage에만 해당되고 Internal Storage에는
         * access restriction으로인해 불가능함.
         * FileProvider를 통해 Uri를 얻고 Intent에 권한 Flag를 설정하는 것으로 위 문제를 해결할 수 있다.
         */

        Uri uri = FileProvider.getUriForFile(this.getBaseContext()
                ,"vh1981.com.funnyphotosstorage.fileprovider"
                ,new File(ImageFileManager.directoryPath(_imageContainer.get(_index).get_filename()))
        );
        Intent intent = new Intent("image/jpg", uri);
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        startActivity(Intent.createChooser(intent, "Send"));
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("TAG, onSavedInstanceState");
        _index = _viewPager.getCurrentItem();
    }

    protected void onRestoreInstanceState(Bundle savedState) {
        SwipeImageViewFragment fragment = (SwipeImageViewFragment) _adapter.getCurrentFragment();
        if (fragment != null) {
            fragment.setImageBitmap(getBitmap(_index));
        }
    }

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