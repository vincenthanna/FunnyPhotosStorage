package vh1981.com.funnyphotosstorage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by yeonhuikim on 15. 6. 9..
 */
public class RemoveTagAlertDialog extends AlertDialog {

    public String _tagToDelete;
    public void setTagToDelete(String tag) {_tagToDelete = tag; }
    public String tagToDelete() { return _tagToDelete; }

    protected RemoveTagAlertDialog(Context context) {
        super(context);
    }


}
