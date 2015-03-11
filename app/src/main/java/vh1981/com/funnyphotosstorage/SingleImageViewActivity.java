package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;

import java.io.File;

public class SingleImageViewActivity extends FragmentActivity implements SwipeImageViewDataSource {
    SwipeImageViewAdapter _adapter;
    ViewPager _viewPager;
    SingleImageViewActivity _activity;
    String _tag;
    int _index;

    ImageContainer _imageContainer;

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

        _adapter = new SwipeImageViewAdapter(getSupportFragmentManager());
        _adapter.set_dataSource(this);

        _viewPager = (ViewPager) findViewById(R.id.imageviewpager);
        _viewPager.setAdapter(_adapter);
        _viewPager.setCurrentItem(idx, false);
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
            Uri uri = Uri.fromFile(new File(_imageContainer.get(_index).get_filepath()));
            Intent intent = new Intent("image/jpg", uri);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/jpeg");
            startActivity(Intent.createChooser(intent, "Send"));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Bitmap getBitmap(int index) {
        _index = index;
        return _imageContainer.get(index).get_bitmap();
    }

    @Override
    public int getCount() {
        return _imageContainer.size();
    }
}
