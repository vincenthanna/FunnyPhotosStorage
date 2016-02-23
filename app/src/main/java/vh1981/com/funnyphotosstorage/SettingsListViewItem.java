package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by vh1981 on 15. 6. 24.
 */
public class SettingsListViewItem extends BaseListViewItem {

    TextView _tvTitle;
    TextView _tvDescription;

    /// 택 1
    ProgressBar _progressBar;
    Switch _switch;

    public SettingsListViewItem(Context context, Activity activity) {
        super(context);
        this._context = context;
        _activity = activity;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.lvitem_settings, this);
        initUI();
    }

    public void setTitleText(String title) {
        assert _tvTitle != null;
        _tvTitle.setText(title);
    }

    public void setDescription(String str)
    {
        _tvDescription.setText(str);
    }

    public void setActivity(Activity activity)
    {
        this._activity = activity;
    }

    public void initUI() {
        _tvTitle = (TextView) findViewById(R.id.tvtitle);
        _tvDescription = (TextView) findViewById(R.id.tvdesc);
        _progressBar = (ProgressBar) findViewById(R.id.progressBar);
        _switch = (Switch) findViewById(R.id.switch1);
        // 기본은 모두 가린다.
        _progressBar.setVisibility(View.INVISIBLE);
        _switch.setVisibility(View.INVISIBLE);
    }

    public void showStop()
    {
        _tvTitle.setText(_context.getResources().getString(R.string.settings_backup));
        _tvDescription.setText(_context.getResources().getString(R.string.settings_back_desc));
        _progressBar.setVisibility(View.INVISIBLE);
        _tvDescription.setEnabled(true);
        _tvTitle.setEnabled(true);
        _switch.setVisibility(View.INVISIBLE);
    }

    public void showRunning()
    {
        _tvTitle.setText(_context.getResources().getString(R.string.settings_backup_running));
        _tvDescription.setText(_context.getResources().getString(R.string.settings_back_desc));
        _progressBar.setVisibility(View.VISIBLE);
        _tvDescription.setEnabled(false);
        _tvTitle.setEnabled(false);
        _switch.setVisibility(View.INVISIBLE);
    }

    public void showRestore()
    {
        _tvTitle.setText(_context.getResources().getString(R.string.settings_backup_running));
        _tvDescription.setText(_context.getResources().getString(R.string.settings_back_desc));
        _progressBar.setVisibility(View.VISIBLE);
        _tvDescription.setEnabled(false);
        _tvTitle.setEnabled(false);
        _switch.setVisibility(View.INVISIBLE);
    }

    public void showAutoBackup(boolean on)
    {
        _tvTitle.setText(_context.getResources().getString(R.string.settings_enable_auto_backup));
        _tvDescription.setText(_context.getResources().getString(R.string.settings_auto_backup_desc));
        _progressBar.setVisibility(View.INVISIBLE);
        _switch.setVisibility(View.VISIBLE);
        _switch.setChecked(on);
    }
}