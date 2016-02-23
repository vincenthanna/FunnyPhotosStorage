package vh1981.com.funnyphotosstorage;

import android.app.Activity;
import android.os.*;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import Utils.DebugLog;

/**
 * Created by yeonhuikim on 15. 9. 29..
 */
public class BackupManagerUIHandler extends Handler {

    public static final int MSG_WHAT_BACKUP_STARTED = 0;
    public static final int MSG_WHAT_BACKUP_PROGRESS = 1;
    public static final int MSG_WHAT_BACKUP_FINISHED = 2;

    private ArrayList<BackupManagerCallback> _registeredCallbacks
            = new ArrayList<BackupManagerCallback>();

    public BackupManagerUIHandler(BackupManagerCallback callback){
        if (callback != null) {
            if (!_registeredCallbacks.contains(callback)) {
                _registeredCallbacks.add(callback);
            }
            else {
                DebugLog.TRACE("Duplicated callback!");
            }
        }
    }

    public void addCallback(BackupManagerCallback callback) {
        if (callback != null) {
            if (_registeredCallbacks.contains(callback) == false) {
                _registeredCallbacks.clear();
                _registeredCallbacks.add(callback);
            }
        }
    }

    @Override
    public void handleMessage(Message message){

        /// 등록된 모든 callback에 통보함
        for (int i = 0; i < _registeredCallbacks.size(); i++) {
            BackupManagerCallback callback = (BackupManagerCallback) _registeredCallbacks.get(i);

            if (callback != null) {
                switch (message.what) {
                    case BackupManagerUIHandler.MSG_WHAT_BACKUP_STARTED:
                        callback.BackupStarted();
                        break;
                    case BackupManagerUIHandler.MSG_WHAT_BACKUP_PROGRESS:
                        callback.BackupProgress(message.arg1);
                        break;
                    case BackupManagerUIHandler.MSG_WHAT_BACKUP_FINISHED:
                        callback.BackupFinished(message.arg1 != 0 ? true : false);
                        break;

                }
            }
        }
    }
}
