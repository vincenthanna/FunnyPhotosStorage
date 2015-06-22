package Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import vh1981.com.funnyphotosstorage.R;

/**
 * Created by vh1981 on 15. 6. 8.
 */
public class UIUtils {
    public static void showToast(Context context, String text, int duration)
    {
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
