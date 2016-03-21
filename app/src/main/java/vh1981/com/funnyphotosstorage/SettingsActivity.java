package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Handler.*;
import java.util.ArrayList;


import Consts.ActivityRequestType;
import PopupMenu.PopupMenuItem;
import Utils.DebugLog;


public class SettingsActivity extends Activity implements BackupManagerCallback {

    ListView _lv;
    SettingsListAdapter _lvAdapter;
    Activity _activity;

    enum BACKUP_STATUS {
        BACKUP_IDLE,
        BACKUP_RUNNING
    };
    BACKUP_STATUS _backupStatus;
    Context _context;
    BottomMenuSheetDialog _bottomSheetDialog = null;

    static int _timerDelay = 1000;
    Handler _timerHandler = new Handler();

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            _lvAdapter.notifyDataSetChanged();
            _timerHandler.postDelayed(this, _timerDelay);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        _lv = (ListView) findViewById(R.id.lv);
        assert _lv != null;
        _lvAdapter = new SettingsListAdapter(getBaseContext());
        _lv.setAdapter(_lvAdapter);

        _lv.setOnItemClickListener(mItemClickListener);
        _context = getBaseContext();
        _timerHandler.postDelayed(timerRunnable, _timerDelay);
        _activity = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,  long l_position) {
            switch(position) {
                case 0: {
                    ArrayList<PopupMenuItem> popupMenus = new ArrayList<PopupMenuItem>();
                    popupMenus.add(new PopupMenuItem(getResources().getString(R.string.settings_backup), "", android.R.drawable.ic_menu_upload));
                    popupMenus.add(new PopupMenuItem(getResources().getString(R.string.settings_restore), "", android.R.drawable.ic_menu_save));

                    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(_activity, SettingsActivity.class);
                            switch(position) {
                                case 0:
                                    // backup
                                    DebugLog.TRACE("backup called!");
                                    backupToDropbox();
                                    break;
                                case 1:
                                    // restore
                                    DebugLog.TRACE("restore called!");
                                    restoreFromDropbox();
                                    break;
                                default:
                                    break;
                            }
                            _bottomSheetDialog.hide();
                        }
                    };
                    _bottomSheetDialog = new BottomMenuSheetDialog(SettingsActivity.this, popupMenus);
                    _bottomSheetDialog.setListViewItemClickListener(listener).show();
                }
                break;

                default:
                    DebugLog.TRACE("default called!");
                    break;
            }
        }
    };

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin animations, open exclusive-access devices
     * (such as the camera), etc.
     * <p/>
     * <p>Keep in mind that onResume is not the best indicator that your activity
     * is visible to the user; a system window such as the keyguard may be in
     * front.  Use {@link #onWindowFocusChanged} to know for certain that your
     * activity is visible to the user (for example, to resume a game).
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     */
    @Override
    protected void onResume() {
        super.onResume();
        BackupManager.instance().dropboxAuthenticationResult();
    }

    public void backupToDropbox()
    {
        BackupManager.instance().putTask(BackupTask.JOB.BACKUP);
    }

    public void restoreFromDropbox()
    {
        BackupManager.instance().putTask(BackupTask.JOB.RESTORE);
    }

    @Override
    public void BackupStarted() {

    }

    @Override
    public void BackupProgress(int percentage) {

    }

    @Override
    public void BackupFinished(boolean succeeded) {
        _lvAdapter.notifyDataSetChanged();
    }
}

class SettingsListAdapter extends BaseAdapter {
    Context maincon;
    LayoutInflater inflater;

    @Override
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
    }

    public SettingsActivity parentActivity;

    public SettingsListAdapter(Context context) {
        maincon = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return 2;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 각 항목의 view 생성
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = new SettingsListViewItem(parent.getContext(), parentActivity);
        }
        SettingsListViewItem listViewItem = (SettingsListViewItem)convertView;

        BackupManager backupManager = BackupManager.instance();
        switch(position) {
            case 0:
            {
                if (backupManager.isBackupRunning() == true) {
                    listViewItem.showRunning(BackupManager.instance().get_backupTask().get_job());
                }
                else {
                    listViewItem.showStop();
                }
            }
            break;

            case 1:
            {
                listViewItem.showAutoBackup(MySharedPreferences.instance().get_autoBackupEnabled());
            }
            break;
            default:
            {
            }
            break;
        }

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