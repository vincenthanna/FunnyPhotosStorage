package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.content.SharedPreferences;

import static junit.framework.Assert.assertTrue;

/**
 * Created by vh1981 on 16. 3. 21.
 */
public class MySharedPreferences {

    public static String APP_AUTO_BACKUP_TOKEN = "jkldjsfakldsj3837";
    public static String APP_INTENT_ACTION_TOKEN = "dsfa3d334234dc";

    private static MySharedPreferences _instance = null;
    private static Context _context;

    public static void setContext(Context context) {
        MySharedPreferences._context = context;
    }

    private MySharedPreferences() {

    }

    public static MySharedPreferences instance() {
        if (_instance == null) {
            _instance = new MySharedPreferences();
        }
        return _instance;
    }


    // APP_AUTO_BACKUP_TOKEN :
    public boolean get_autoBackupEnabled() {
        SharedPreferences preferences = _context.getSharedPreferences(APP_AUTO_BACKUP_TOKEN, Context.MODE_PRIVATE);
        boolean enabled = preferences.getBoolean(APP_AUTO_BACKUP_TOKEN, false);
        return enabled;
    }

    public void set_autoBackupEnabled(boolean value) {
        SharedPreferences preferences = _context.getSharedPreferences(APP_AUTO_BACKUP_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_AUTO_BACKUP_TOKEN, value);
        editor.commit();
    }

    // APP_RUNNING_INTENT_TOKEN :
    public String get_intentAction() {
        SharedPreferences preferences = _context.getSharedPreferences(APP_INTENT_ACTION_TOKEN, Context.MODE_PRIVATE);
        String intentType = preferences.getString(APP_INTENT_ACTION_TOKEN, "");
        return intentType;
    }

    public void set_intentAction(String intentStr) {
        SharedPreferences preferences = _context.getSharedPreferences(APP_INTENT_ACTION_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_INTENT_ACTION_TOKEN, intentStr);
        editor.commit();
    }

}
