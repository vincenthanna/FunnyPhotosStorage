package vh1981.com.funnyphotosstorage;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import PopupMenu.PopupMenuAdapter;
import PopupMenu.PopupMenuItem;
import Utils.DebugLog;

/**
 * Created by vh1981 on 16. 2. 15.
 */
public class BottomMenuSheetDialog extends Dialog {
    Context _context;
    ArrayList<PopupMenuItem> _menuItems;
    ListView _lv = null;
    PopupMenuAdapter _adapter = null;
    /**
     * Create a Dialog window that uses the default dialog frame style.
     *
     * @param context The Context the Dialog is to run it.  In particular, it
     *                uses the window manager and theme in this context to
     *                present its UI.
     */
    public BottomMenuSheetDialog(Context context, ArrayList<PopupMenuItem> items) {
        super(context, R.style.MaterialDialogSheet);
        this.setContentView(R.layout.image_gallery_bottom_sheet);
        _context = context;
        _menuItems = items;
        initUI();
    }

    void initUI()
    {
        _lv = (ListView) findViewById(R.id.lv);
        _adapter = new PopupMenuAdapter(_context, R.layout.image_gallery_popup_menu_item, _menuItems);
        _lv.setAdapter(_adapter);
        setCancelable(true);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    public BottomMenuSheetDialog setListViewItemClickListener(AdapterView.OnItemClickListener listener)
    {
        if (_lv != null) {
            _lv.setOnItemClickListener(listener);
        }
        return this;
    }
}
