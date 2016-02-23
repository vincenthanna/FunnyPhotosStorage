package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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


import Utils.DebugLog;


public class SettingsActivity extends Activity implements BackupManagerCallback {

    ListView _lv;
    SettingsListAdapter _lvAdapter;

    enum BACKUP_STATUS {
        BACKUP_IDLE,
        BACKUP_RUNNING
    };
    BACKUP_STATUS _backupStatus;
    public static String APP_AUTO_BACKUP_TOKEN = "jkldjsfakldsj3837";
    Context _context;

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

        BackupManager.instance().addUICallback(this);
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
                case 0:
                    DebugLog.TRACE("backup called!");
                    backupToDropbox();
                    break;
                case 1:
                    break;
                case 2:
                {
                    SharedPreferences preferences = _context.getSharedPreferences(SettingsActivity.APP_AUTO_BACKUP_TOKEN, Context.MODE_PRIVATE);
                    boolean enabled = preferences.getBoolean(SettingsActivity.APP_AUTO_BACKUP_TOKEN, false);
                    SharedPreferences.Editor editor = preferences.edit();
                    if (enabled) {
                        editor.putBoolean(APP_AUTO_BACKUP_TOKEN, false);

                    }
                    else {
                        editor.putBoolean(APP_AUTO_BACKUP_TOKEN, true);
                    }
                    editor.commit();

                    if (preferences.getBoolean(SettingsActivity.APP_AUTO_BACKUP_TOKEN, false) == true) {
                        backupToDropbox();
                    }

                    _lvAdapter.notifyDataSetChanged();
                }
                break;

                default:
                    DebugLog.TRACE("default called!");
                    break;
            }
        }
    };

    public void backupToDropbox()
    {
        BackupManager.instance().doBackup();
        _lvAdapter.notifyDataSetChanged();
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

        switch(position) {
            case 0:
            {
                BackupManager backupManager = BackupManager.instance();

                if (backupManager.backupRunning() == true) {
                    listViewItem.showRunning();
                }
                else {
                    listViewItem.showStop();
                }
            }
            break;
            case 1:
            {
                //

            }
            break;
            case 2:
            {
                SharedPreferences preferences = maincon.getSharedPreferences(SettingsActivity.APP_AUTO_BACKUP_TOKEN, Context.MODE_PRIVATE);
                boolean enabled = preferences.getBoolean(SettingsActivity.APP_AUTO_BACKUP_TOKEN, false);

                listViewItem.showAutoBackup(enabled);
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