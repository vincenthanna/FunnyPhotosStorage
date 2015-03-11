package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.File;

import Utils.DebugLog;


public class ImageGalleryActivity extends Activity{

    public static String IE_TAG = "TAG";
    public static String IE_INDEX = "INDEX";
    ImageContainer _imageContainer;
    ImagesListGridAdapter _gridAdapter;
    GridView _gridView = null;
    ImageGalleryActivity _activity;
    public GridView get_gridView() { return _gridView; }
    String _tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        Intent intent = getIntent();

        _activity = this;

        /// 넘어온 tag에 해당하는 이미지들을 불러온다.
        _tag = intent.getStringExtra(IE_TAG);
        DebugLog.TRACE("tag=" + _tag);
        _imageContainer = ImageManager.instance().getImageContainer(_tag);

        _gridView = (GridView) findViewById(R.id.gridImageGallery);

        _gridAdapter = new ImagesListGridAdapter(getBaseContext(), this);
        _gridAdapter.set_imageContainer(_imageContainer);

        _gridView.setAdapter(_gridAdapter);

        _gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DebugLog.TRACE("position=" + i);

                // 암시적 인텐트로 실행된 것이면 Uri만들어서 넘김
                /*
                // Uri 생성하고 종료한다.
                Uri uri = Uri.fromFile(new File(_imageContainer.get(i).get_filepath()));
                Intent result = new Intent("image/jpg", uri);
                setResult(Activity.RESULT_OK, result);
                finish();
                */

                // 내부 intent이면 full화면으로 이동
                Intent intent = new Intent(_activity, SingleImageViewActivity.class);
                intent.putExtra(ImageGalleryActivity.IE_TAG, _tag);
                intent.putExtra(ImageGalleryActivity.IE_INDEX, i);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_gallery, menu);
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

        return super.onOptionsItemSelected(item);
    }
}

class ImagesListGridAdapter extends ArrayAdapter
{
    Context context;
    ImageGalleryActivity _activity;
    ImageContainer _imageContainer;
    void set_activity(ImageGalleryActivity activity) { _activity = activity; }
    void set_imageContainer(ImageContainer container) { _imageContainer = container;}

    public ImagesListGridAdapter(Context context, ImageGalleryActivity activity)
    {
        super(context, 0);
        this.context=context;
        this._activity = activity;
    }

    public int getCount()
    {
        return _imageContainer.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageGalleryItemLayout imageGalleryItemLayout = null;
        if (convertView == null) {
            convertView = new ImageGalleryItemLayout(context, _activity);
        }
        imageGalleryItemLayout = (ImageGalleryItemLayout) convertView;
        imageGalleryItemLayout.setBitmap(_imageContainer.get(position).get_bitmap(), _activity.get_gridView().getColumnWidth());
        return convertView;
    }
}

