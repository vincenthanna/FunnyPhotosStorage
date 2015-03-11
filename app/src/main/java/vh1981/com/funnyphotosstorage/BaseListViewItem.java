package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Created by vh1981 on 15. 1. 5.
 */
public class BaseListViewItem extends RelativeLayout {
    Activity _activity;
    Context _context;
    BaseListViewItemType _listViewType;
    public static enum BaseListViewItemType{TYPE_Category, TYPE_NewImage}

    BaseListViewItemType type() { return _listViewType; }

    public BaseListViewItem(Context context) {
        super(context);
    }
}
