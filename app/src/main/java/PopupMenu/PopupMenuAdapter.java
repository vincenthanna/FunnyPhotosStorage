package PopupMenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vh1981.com.funnyphotosstorage.R;

/**
 * Created by vh1981 on 16. 2. 11.
 */
public class PopupMenuAdapter extends ArrayAdapter<PopupMenuItem>
{
    Context _context;
    ArrayList<PopupMenuItem> _menuItems;
    public PopupMenuAdapter(Context context, int resource, ArrayList<PopupMenuItem> items) {
        super(context, resource);
        _context = context;
        _menuItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.image_gallery_popup_menu_item, null);
        }
        PopupMenuItem item = _menuItems.get(position);
        TextView title = (TextView) v.findViewById(R.id.title);
        if (item != null) {
            if (title != null) {
                title.setText(item.get_title());
                Drawable img = getContext().getResources().getDrawable(item.get_drawableId());
                title.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            }
        }

        return v;
    }

    @Override
    public int getCount() {
        return _menuItems.size();
    }
}
