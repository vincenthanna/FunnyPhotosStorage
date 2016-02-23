package vh1981.com.funnyphotosstorage;

/**
 * Created by vh1981 on 15. 9. 21.
 */
public interface BackupManagerCallback {
    void BackupStarted();
    void BackupProgress(int percentage);
    void BackupFinished(boolean succeeded);
}
