package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.*;
import android.webkit.URLUtil;
import android.widget.*;
import android.net.Uri;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.*;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.*;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Utils.BitmapUtil;
import Utils.ClipboardHelper;
import Utils.DebugLog;

public class MainActivity extends Activity {

    ListView _lvCat;
    MyListAdapter _lvAdapter;
    MainActivity _activity;
    ImageManager _imageManager;

    final static public String APP_DIRECTORY = "/FunnyPhotoStorage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageManager.setContext(this.getBaseContext());

        _lvCat = (ListView) findViewById(R.id.lvmain);
        _lvAdapter = new MyListAdapter(getBaseContext(), R.layout.lvmainitemview);

        _lvCat.setAdapter(_lvAdapter);
        _lvCat.setOnItemClickListener(_lvCatItemClickListener);

        _activity = this;

        _imageManager = ImageManager.instance();
        BitmapUtil.setContext(getBaseContext());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_add_from_clip:
            {
                insertImageFromClip();
            }
            break;
            case R.id.action_settings: {

            }
            break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private AdapterView.OnItemClickListener _lvCatItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,  long l_position) {
            String tag = _lvAdapter.getTagByPosition(position);
            Intent intent = new Intent(_activity, ImageGalleryActivity.class);
            intent.putExtra(ImageGalleryActivity.IE_TAG, tag);
            startActivityForResult(intent, 0);
        }
    };

    void insertImageFromClip()
    {
        Intent intent = new Intent(this, NewImageActivity.class);
        startActivity(intent);
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 실행시킨 Intent를 확인해야 한다.
        Intent intent = getIntent();

        if (intent.getAction() == Intent.ACTION_GET_CONTENT) { ///< 외부에서 입력된
            if (data != null) {
                setResult(Activity.RESULT_OK, data);
            }
            else {
                setResult(Activity.RESULT_CANCELED);
            }
            finish();
        }
    }
}

class MyListAdapter extends BaseAdapter {
    Context maincon;
    LayoutInflater inflater;
    public MainActivity parentActivity;
    int layout;
    ImageManager _imageManager;
    ArrayList<String> _tags;
    public String getTagByPosition(int position) {
        return _tags.get(position);
    }


    public MyListAdapter(Context context, int layout) {
        maincon = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layout = layout;
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
            convertView = new CategoryListViewItem(parent.getContext(), parentActivity);
        }
        CategoryListViewItem listViewItem = (CategoryListViewItem)convertView;
        listViewItem.setTitleText(_tags.get(position) );

        return convertView;
    }

    @Override
    public int getViewTypeCount() { return 1; }
}