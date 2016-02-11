package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import PopupMenu.*;

import java.util.ArrayList;

import Consts.ActivityRequestType;
import Utils.DebugLog;


public class ImageGalleryActivity extends Activity {

    public static String IE_TAG = "TAG";
    public static String IE_INDEX = "INDEX";
    public static String IE_ACTION_GET_CONTENT = "GET_CONTENT";
    public static String IE_ACTION_MULTIPLE_SELECT = "MULTIPLE_SELECT";
    ImageContainer _imageContainer;
    ImagesListGridAdapter _gridAdapter;
    public enum Mode {
        SINGLE_IMAGE_VIEW,
        MULTIPLE_SELECT_MODE,
    }
    Mode _mode = Mode.SINGLE_IMAGE_VIEW;
    public boolean multiSelectMode() {
        return _mode == Mode.MULTIPLE_SELECT_MODE;
    }

    GridView _gridView = null;
    ImageGalleryActivity _activity;

    public GridView get_gridView() {
        return _gridView;
    }

    String _tag;
    boolean _activityGetContent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        Intent intent = getIntent();

        _activity = this;

        /// 넘어온 tag에 해당하는 이미지들을 불러온다.
        _tag = intent.getStringExtra(IE_TAG);
        if (intent.getIntExtra(IE_ACTION_GET_CONTENT, 0) > 0) {
            _activityGetContent = true;
        }
        DebugLog.TRACE("tag=" + _tag);
        _imageContainer = ImageManager.instance().getImageContainer(_tag);

        if (intent.getStringExtra(IE_ACTION_MULTIPLE_SELECT) != null) {
            _mode = Mode.MULTIPLE_SELECT_MODE;
        }

        _gridView = (GridView) findViewById(R.id.gridImageGallery);

        _gridAdapter = new ImagesListGridAdapter(getBaseContext(), this);
        _gridAdapter.set_imageContainer(_imageContainer);

        _gridView.setAdapter(_gridAdapter);

        _gridView.setOnItemClickListener(_gridViewItemClickListener);

        setTitleByMode();

        registerForContextMenu(_gridView);

        if (_mode == Mode.MULTIPLE_SELECT_MODE) {
            startMultipleSelectMode();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitleByMode();
        switch(_mode) {
            case SINGLE_IMAGE_VIEW:
                getActionBar().setDisplayShowHomeEnabled(true);
                getMenuInflater().inflate(R.menu.menu_image_gallery, menu);
                _imageContainer.checkClear();
                return true;
            case MULTIPLE_SELECT_MODE:
                getActionBar().setDisplayShowHomeEnabled(false);
                getMenuInflater().inflate(R.menu.menu_image_gallery_multiselect, menu);
                _imageContainer.checkClear();
                return true;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_down_menu) {
            ////////////////////////////////////////////////////////////////////////

            View view = getLayoutInflater().inflate (R.layout.image_gallery_bottom_sheet, null);
            final ListView lv = (ListView) view.findViewById( R.id.lv);

            ArrayList<PopupMenuItem> popupMenus = new ArrayList<PopupMenuItem>();
            popupMenus.add(new PopupMenuItem(getResources().getString(R.string.action_move_image), "", android.R.drawable.ic_menu_directions));
            popupMenus.add(new PopupMenuItem(getResources().getString(R.string.action_delete), "", android.R.drawable.ic_menu_delete));
            lv.setAdapter(new PopupMenuAdapter(getBaseContext(), R.layout.image_gallery_popup_menu_item, this, popupMenus));

            final Dialog mBottomSheetDialog = new Dialog (ImageGalleryActivity.this,
                    R.style.MaterialDialogSheet);
            mBottomSheetDialog.setContentView (view);
            mBottomSheetDialog.setCancelable (true);
            mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
            mBottomSheetDialog.show ();

            ////////////////////////////////////////////////////////////////////////
        }
        else if (id == R.id.action_delete_image) {
            _mode = Mode.MULTIPLE_SELECT_MODE;
            invalidateOptionsMenu();
            startMultipleSelectMode();
        }
        else if (id == R.id.action_cancel)
        {
            _mode = Mode.SINGLE_IMAGE_VIEW;
            _imageContainer.checkClear();
            invalidateOptionsMenu();
            stopMultipleSelectMode();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.imagegallertview_contextmenu, menu);
    }

    /// item을 오래 눌렀을 때 :
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()) {
            case R.id.action_copy_image: {
                Image img = _imageContainer.get(index);
                copyImageToClipboard(img);
            }
            break;
            case R.id.action_delete_image: {
                Image img = _imageContainer.get(index);
                deleteImage(img);
            }
            break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestType.GetContent) {
            if (resultCode == Activity.RESULT_OK) { // pick up을 눌렀을 경우에만 back해야 한다.
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        }

        /// 특정 조건에서만
//        _gridAdapter.notifyDataSetChanged();
    }

    /**
     * Called by the system when the device configuration changes while your
     * activity is running.  Note that this will <em>only</em> be called if
     * you have selected configurations you would like to handle with the
     * {@link android.R.attr#configChanges} attribute in your manifest.  If
     * any configuration change occurs that is not selected to be reported
     * by that attribute, then instead of reporting it the system will stop
     * and restart the activity (to have it launched with the new
     * configuration).
     * <p/>
     * <p>At the time that this function has been called, your Resources
     * object will have been updated to return resource values matching the
     * new configuration.
     *
     * @param newConfig The new device configuration.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /// ItemView click listener :
    AdapterView.OnItemClickListener _gridViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (_mode != Mode.MULTIPLE_SELECT_MODE) {

                DebugLog.TRACE("position=" + position);

                Intent intentFrom = getIntent();
                // 내부 intent이면 full화면으로 이동
                Intent intent = new Intent(_activity, SingleImageViewActivity.class);
                intent.putExtra(ImageGalleryActivity.IE_TAG, _tag);
                intent.putExtra(ImageGalleryActivity.IE_INDEX, position);
                if (_activityGetContent) {
                    intent.putExtra(ImageGalleryActivity.IE_ACTION_GET_CONTENT, 1);
                    startActivityForResult(intent, ActivityRequestType.GetContent);
                } else {
                    intent.putExtra(ImageGalleryActivity.IE_ACTION_GET_CONTENT, 0);
                    startActivityForResult(intent, ActivityRequestType.Normal);
                }
            }
            else {
                _imageContainer.get(position).checkToggle();
                int visiblePosition = _gridView.getFirstVisiblePosition();
                View target = _gridView.getChildAt(position - visiblePosition);
                ImageGalleryItemLayout layout = (ImageGalleryItemLayout) _gridView.getAdapter().getView(position, target, _gridView);
                layout.transition(false);
            }
        }
    };

    boolean copyImageToClipboard(Image img) {
        ClipboardManager mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ContentValues values = new ContentValues(2);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        values.put(MediaStore.Images.Media.DATA, ImageFileManager.directoryPath(img.get_filename()));
        ContentResolver theContent = getContentResolver();
        Uri imageUri = theContent.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        ClipData theClip = ClipData.newUri(getContentResolver(), "URI", imageUri);
        mClipboard.setPrimaryClip(theClip);
        return true;
    }

    boolean deleteImage(Image img) {
        boolean succeeded = ImageManager.instance().removeImage(img);
        _imageContainer.removeImage(img);
        _gridAdapter.notifyDataSetChanged();
        return succeeded;
    }

    void startMultipleSelectMode() {

    }

    void stopMultipleSelectMode()
    {
        _gridAdapter.notifyDataSetChanged();
        _gridView.setAdapter(_gridAdapter);
    }

    void setTitleByMode()
    {
        String title;
        switch(_mode) {
            case MULTIPLE_SELECT_MODE:
                title = "";
                break;
            case SINGLE_IMAGE_VIEW:
                title = getResources().getString(R.string.title_activity_image_gallery);
                title += " - " + _tag;
                break;
            default:
                title = getResources().getString(R.string.title_activity_image_gallery);
                title += " - " + _tag;
                break;
        }
        setTitle(title);
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
        imageGalleryItemLayout.setImage(_imageContainer.get(position), _activity.multiSelectMode());

        return convertView;
    }
}