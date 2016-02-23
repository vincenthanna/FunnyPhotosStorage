package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import PopupMenu.PopupMenuAdapter;
import PopupMenu.PopupMenuItem;
import Utils.DebugLog;

public class MultiSelImageGalleryActivity extends Activity {
    public enum Mode {
        MOVE,
        DELETE,
    }

    public static String IE_TAG = "TAG";
    public static String IE_INDEX = "INDEX";
    public static String IE_MODE = "MODE";
    public static String IE_MODE_MOVE = "MOVE";
    public static String IE_MODE_DELETE = "DELETE";

    Mode _mode;
    ImageContainer _imageContainer;
    MultiSelImagesListGridAdapter _gridAdapter;
    String _tag;
    GridView _gridView = null;
    MultiSelImageGalleryActivity _activity;
    Dialog _bottomSheetDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_sel_image_gallery);
        Intent intent = getIntent();

        _activity = this;

        /// 넘어온 tag에 해당하는 이미지들을 불러온다.
        _tag = intent.getStringExtra(IE_TAG);

        DebugLog.TRACE("tag=" + _tag);
        _imageContainer = ImageManager.instance().getImageContainer(_tag);

        if (intent.getStringExtra(IE_MODE).equals(IE_MODE_MOVE)) {
            _mode = Mode.MOVE;
        } else if (intent.getStringExtra(IE_MODE).equals(IE_MODE_DELETE)) {
            _mode = Mode.DELETE;
        } else {
            // FIXME: need to crash... undefined.
        }

        _gridAdapter = new MultiSelImagesListGridAdapter(getBaseContext(), this);
        _gridAdapter.set_imageContainer(_imageContainer);

        _gridView = (GridView) findViewById(R.id.gridImageGallery);
        _gridView.setAdapter(_gridAdapter);
        _gridView.setOnItemClickListener(_gridViewItemClickListener);

        _bottomSheetDialog = new Dialog(MultiSelImageGalleryActivity.this, R.style.MaterialDialogSheet);

        setTitleByMode();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        setTitleByMode();
        getActionBar().setDisplayShowHomeEnabled(false);
        getMenuInflater().inflate(R.menu.menu_image_gallery_multiselect, menu);
        MenuItem item = menu.findItem(R.id.action_ok);
        if (_mode == Mode.MOVE) {
            item.setTitle(getResources().getString(R.string.action_move_image));
        } else if (_mode == Mode.DELETE) {
            item.setTitle(getResources().getString(R.string.action_delete));
        }
        _imageContainer.checkClear();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_ok) {

            if (_mode == Mode.MOVE) {

                final ArrayList<PopupMenuItem> popupMenus = new ArrayList<PopupMenuItem>();
                for (String tag : ImageManager.instance().getTags()) {
                    if (false == tag.equals(this._tag)) {
                        PopupMenuItem popupMenuItem = new PopupMenuItem(tag, "", android.R.drawable.ic_menu_directions);
                        popupMenuItem.set_data(tag);
                        popupMenus.add(popupMenuItem);
                    }
                }

                AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PopupMenuItem item = popupMenus.get(position);
                        String tag = (String) item.get_data();
                        ImageContainer container = _imageContainer.copy(true);
                        //for (Image img:container.array()) {
                        for (int i = 0; i < container.size(); i++) {
                            Image img = container.get(i);
                            if (ImageManager.instance().changeImageTag(img, tag) == false) {
                                DebugLog.TRACE("can't change tag : file=" + img.get_filename() + " " + "tag=" + tag);
                            }
                        }

                        finish();
                    }
                };

                BottomMenuSheetDialog dialog = new BottomMenuSheetDialog(MultiSelImageGalleryActivity.this, popupMenus);
                dialog.setListViewItemClickListener(listener);
                dialog.show();

            } else if (_mode == Mode.DELETE) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                deleteSelectedImages();
                                finish();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MultiSelImageGalleryActivity.this);
                builder.setMessage(getResources().getString(R.string.sure_to_delete_images))
                        .setPositiveButton(getResources().getString(R.string.action_delete), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.action_cancel), dialogClickListener).show();


            } else {
                // FIXME: 16. 2. 11 crash 시켜야 함
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void deleteSelectedImages()
    {
        for (int pos = 0; pos < _imageContainer.size(); pos++) {
            Image img = _imageContainer.get(pos);
            if (img.checked()) {
                ImageManager.instance().removeImage(img);
            }
        }
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

            _imageContainer.get(position).checkToggle();
            int visiblePosition = _gridView.getFirstVisiblePosition();
            View target = _gridView.getChildAt(position - visiblePosition);
            ImageGalleryItemLayout layout = (ImageGalleryItemLayout) _gridView.getAdapter().getView(position, target, _gridView);
            layout.transition(false);
        }
    };

    void setTitleByMode() {
        String title = "";
        setTitle(title);
    }
}

class MultiSelImagesListGridAdapter extends ArrayAdapter {
    Context context;
    MultiSelImageGalleryActivity _activity;
    ImageContainer _imageContainer;

    void set_activity(MultiSelImageGalleryActivity activity) {
        _activity = activity;
    }

    void set_imageContainer(ImageContainer container) {
        _imageContainer = container;
    }

    public MultiSelImagesListGridAdapter(Context context, MultiSelImageGalleryActivity activity) {
        super(context, 0);
        this.context = context;
        this._activity = activity;
    }

    public int getCount() {
        return _imageContainer.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageGalleryItemLayout imageGalleryItemLayout = null;
        if (convertView == null) {
            convertView = new ImageGalleryItemLayout(context, _activity);
        }
        imageGalleryItemLayout = (ImageGalleryItemLayout) convertView;
        imageGalleryItemLayout.setImage(_imageContainer.get(position), true);

        return convertView;
    }
}