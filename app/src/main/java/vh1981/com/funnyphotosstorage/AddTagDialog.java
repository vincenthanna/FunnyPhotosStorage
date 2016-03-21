package vh1981.com.funnyphotosstorage;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import Utils.DebugLog;
import Utils.UIUtils;

/**
 * Created by vh1981 on 15. 5. 19.
 */
public class AddTagDialog extends Dialog {
    EditText _editText;
    Button _button;
    String _tag;
    public String tag() { return _tag; }
    Context _context;
    ArrayList<UIReloadDelegate> _uiReloadDelegateSet = new ArrayList<UIReloadDelegate>();
    public boolean registerResultNotifier(UIReloadDelegate delegate) {
        return _uiReloadDelegateSet.add(delegate);
    }

    public AddTagDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_addtag);
        _context = context;
        initUI();
    }

    public AddTagDialog(Context context, int theme) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_addtag);
        _context = context;
        initUI();
    }

    protected AddTagDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_addtag);
        _context = context;
        initUI();
    }

    void initUI()
    {
        _editText = (EditText) this.findViewById(R.id.editText);
        assert _editText != null;

        _editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    _tag = _editText.getText().toString();
                    addTag(_tag);
                    return true;
                }
                return false;
            }
        });

        _button = (Button) findViewById(R.id.button);
        assert _button != null;
        _button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _tag = _editText.getText().toString();
                if (_tag.trim().length() == 0) return; // 비어있으면 닫히지 않는다.
                addTag(_tag);
                dismiss();
            }
        });
    }

    void addTag(String tag) {
        DebugLog.TRACE("Add New Tag = " + _tag);
        if (tag.length() == 0) return;
        if (tag == "") return;
        if (tag.trim().length() == 0) return;

        long id = 0;
        if (ImageManager.instance().addTag(_tag) == true) {
            // succeeded:
            UIUtils.showToast(_context, _context.getResources().getString(R.string.tag_added), Toast.LENGTH_SHORT);
            notifyDelegates();
        }
        else {
            if (ImageManager.instance().tagExists(_tag) == true) {
                UIUtils.showToast(_context, _context.getResources().getString(R.string.tag_exists), Toast.LENGTH_SHORT);
            }
            else {
                // 실패하면 이곳으로 오나 기본적으로 실패할 일이 없음.
                if (BuildConfig.DEBUG) {
                    assert false;
                }
            }
        }

        dismiss();
    }

    public void notifyDelegates() {
        for (UIReloadDelegate delegate : _uiReloadDelegateSet) {
            delegate.reload();
        }
    }
}
