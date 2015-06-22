package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Utils.BitmapUtil;
import Utils.ClipboardHelper;
import Utils.DebugLog;
import Utils.UIUtils;

import com.baoyz.swipemenulistview.*;

public class MainActivity extends Activity implements UIReloadDelegate{

    SwipeMenuListView _lvCat;
    MyListAdapter _lvAdapter;
    MainActivity _activity;
    ImageManager _imageManager;
    String _tagToRemove = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageManager.setContext(this.getBaseContext());

        _lvCat = (SwipeMenuListView) findViewById(R.id.lvmain);
        _lvAdapter = new MyListAdapter(getBaseContext(), R.layout.lvmainitemview);

        _lvCat.setAdapter(_lvAdapter);
        _lvCat.setOnItemClickListener(_lvCatItemClickListener);

        _activity = this;

        _imageManager = ImageManager.instance();
        BitmapUtil.setContext(getBaseContext());


        // SwipeMenuListView init:
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                switch (menu.getViewType()) {
                    case 0:
                        /*
                        // create "open" item
                        SwipeMenuItem openItem = new SwipeMenuItem(
                                getApplicationContext());
                        // set item background
                        openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                                0xCE)));
                        // set item width
                        openItem.setWidth(dp2px(90));
                        // set item title
                        openItem.setTitle("Open");
                        // set item title fontsize
                        openItem.setTitleSize(18);
                        // set item title font color
                        openItem.setTitleColor(Color.WHITE);
                        // add to menu
                        menu.addMenuItem(openItem);
                        */

                        // create "delete" item
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getApplicationContext());
                        // set item background
                        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));
                        // set item width
                        deleteItem.setWidth(dp2px(90));
                        // set a icon
                        deleteItem.setIcon(android.R.drawable.ic_delete);
                        // add to menu
                        menu.addMenuItem(deleteItem);
                        break;
                    default:
                        break;
                }
            }
        };

        // set creator
        _lvCat.setMenuCreator(creator);

        _lvCat.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0: {
                        RemoveTagAlertDialog.Builder db = new RemoveTagAlertDialog.Builder(_activity);
                        String str = getResources().getString(R.string.sure_to_delete);
                        _tagToRemove = _lvAdapter.getTagByPosition(position);
                        RemoveTagAlertDialog dlg;

                        db.setMessage(str)
                                .setCancelable(true)
                                .setPositiveButton(android.R.string.ok, _removeConfirmAlertClickListener)
                                .setNegativeButton(android.R.string.cancel, null)
                                .show();
                    }
                    break;
                    default:
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }
    private int dp2px(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dp * scale + 0.5f);
        return pixels;
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
            case R.id.action_add_tag: {
                // Tag 추가 popup창을 띄운다.

                AddTagDialog d = new AddTagDialog(this);
                d.registerResultNotifier(this);
                d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        AddTagDialog dlg = (AddTagDialog)dialogInterface;
                    }
                });

                d.show();
            }
            break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    DialogInterface.OnClickListener _removeConfirmAlertClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            removeTag(_tagToRemove);
        }
    };

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
        ClipboardHelper helper = new ClipboardHelper(this.getBaseContext());
        Bitmap bitmap = helper.getImageFromClipboard();
        if (bitmap != null) {
            Intent intent = new Intent(this, NewImageActivity.class);
            startActivity(intent);
        }
        else {
            UIUtils.showToast(getApplicationContext(),
                    getResources().getString(R.string.no_image_in_clipboard), Toast.LENGTH_SHORT);
        }
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

    @Override
    public void reload() {
        _lvAdapter.notifyDataSetChanged();
    }

    boolean removeTag(String tag) {
        ImageManager.instance().removeTag(tag);
        _lvAdapter.notifyDataSetChanged();
        return true;
    }
}

class MyListAdapter extends BaseAdapter {
    Context maincon;
    LayoutInflater inflater;

    @Override
    public void notifyDataSetInvalidated() {
        _tags = _imageManager.getTags();
        super.notifyDataSetInvalidated();
    }

    @Override
    public void notifyDataSetChanged() {
        _tags = _imageManager.getTags();
        super.notifyDataSetChanged();
    }

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
        listViewItem.setTitleText(_tags.get(position));

        return convertView;
    }

    @Override
    public int getViewTypeCount() { return 1; }

    @Override
    public int getItemViewType(int position) {
        // current menu type
        return 0;
    }
}