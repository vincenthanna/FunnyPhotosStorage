package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DebugUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Utils.ClipboardHelper;
import Utils.DebugLog;


public class NewImageActivity extends Activity {

    public static String INTENT_DATA_NEW_IMAGE = "image";
    ListView _lvNewImage;
    NewImageListViewAdapter _lvAdapter;
    NewImageActivity _activity;
    Bitmap _imageBitmap;

    public Bitmap getNewImageBitmap() { return _imageBitmap; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image);

        ImageManager.setContext(getBaseContext());
        MySharedPreferences.setContext(this.getBaseContext());
        MySharedPreferences.instance().set_intentAction(getIntent().getAction());

        ClipboardHelper helper = new ClipboardHelper(this.getBaseContext());
        _imageBitmap = helper.getImageFromClipboard();

        _lvNewImage = (ListView) findViewById(R.id.lv);
        _lvAdapter = new NewImageListViewAdapter(getBaseContext(), this);

        _lvNewImage.setAdapter(_lvAdapter);
        _lvNewImage.setOnItemClickListener(_lvCatItemClickListener);

        _activity = this;

        Intent intent = getIntent();
        if (intent.getAction() == Intent.ACTION_SEND) { ///< 외부 암시 intent 수신
            Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (imageUri != null) {
                try {
                    // Update UI to reflect image being shared
                    _imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        ImageView iv = (ImageView)findViewById(R.id.iv);
        iv.setImageBitmap(_imageBitmap);
    }

    boolean needShowActionMenu() {
        if (MySharedPreferences.instance().get_intentAction() != Intent.ACTION_SEND) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (needShowActionMenu()) {
            getMenuInflater().inflate(R.menu.menu_new_image, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemClickListener _lvCatItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,  long l_position) {
            String tag = ImageManager.instance().getTags().get(position);
            ImageManager.instance().createImage(_imageBitmap, tag);

            Intent intent = getIntent();
            if (intent.getAction() == Intent.ACTION_SEND) { ///< 외부 암시 intent 수신
                setResult(Activity.RESULT_OK);
                finish();
            }
            else {
                finish();
            }
            return;
        }
    };
}


class NewImageListViewAdapter extends BaseAdapter {
    Context maincon;
    LayoutInflater inflater;
    public NewImageActivity _parentActivity;
    int _count = 8;
    NewImageListViewItem_Image _lvItemViewImage;
    ArrayList<String> _tags;
    ImageManager _imageManager;

    public NewImageListViewAdapter(Context context, NewImageActivity parentActivity) {
        maincon = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this._parentActivity = parentActivity;
        _imageManager = ImageManager.instance();
        _tags = _imageManager.getTags();
    }

    public int getCount() {
        return _tags.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int pos) {
        return pos;
    }

    // 각 항목의 view 생성
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new CategoryListViewItem(parent.getContext(), _parentActivity);
            CategoryListViewItem listViewItem = (CategoryListViewItem)convertView;
            listViewItem.setTitleText(_tags.get(position) );
        }
        CategoryListViewItem listViewItem = (CategoryListViewItem)convertView;
        listViewItem.setTitleText(_tags.get(position) );
        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}