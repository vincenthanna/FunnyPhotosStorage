package PopupMenu;

/**
 * Created by vh1981 on 16. 2. 11.
 */
public class PopupMenuItem
{
    String _title;
    public void set_title(String _title) { this._title = _title; }
    public String get_title() { return _title; }

    String _desc;
    public void set_desc(String _desc) { this._desc = _desc; }
    public String get_desc() { return _desc; }

    Object _data;
    public Object get_data() { return _data; }
    public void set_data(Object _data) { this._data = _data; }

    int _drawableId = android.R.drawable.ic_menu_view;
    public void set_drawableId(int _drawableId) { this._drawableId = _drawableId; }
    public int get_drawableId() { return _drawableId; }

    public PopupMenuItem(String title)
    {
        _title = title;
    }

    public PopupMenuItem(String title, String desc)
    {
        _title = title;
        _desc = desc;
    }

    public PopupMenuItem(String title, String desc, int drawableId)
    {
        _title = title;
        _desc = desc;
        _drawableId = drawableId;
    }
}