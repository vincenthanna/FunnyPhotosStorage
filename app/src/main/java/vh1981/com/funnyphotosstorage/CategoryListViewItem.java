package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by vh1981 on 14. 12. 5.
 */
public class CategoryListViewItem extends BaseListViewItem {

    TextView _tvTitle;
    public CategoryListViewItem(Context context, Activity activity) {
        super(context);
        this._context = context;
        _activity = activity;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        li.inflate(R.layout.lvmainitemview, this);
        _listViewType = BaseListViewItemType.TYPE_Category;
        _tvTitle = (TextView) findViewById(R.id.tvtitle);
        assert _tvTitle != null;
    }

    public void setTitleText(String title) {
        assert _tvTitle != null;
        _tvTitle.setText(title);
    }

    public void setActivity(Activity activity)
    {
        this._activity = activity;
    }

    public void initUI() {

    }
}
