package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.content.Context.*;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.*;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.*;

import java.io.*;
import java.util.ArrayList;

import Utils.DebugLog;

import static junit.framework.Assert.assertTrue;

/**
 * Created by yeonhuikim on 15. 8. 21..
 */
public class BackupManager {
    private static BackupManager _instance = null;
    private static Context _context;
    private DropboxAPI<AndroidAuthSession> _mDBApi;
    private BackupTask _backupTask = null;

    private BackupManagerUIHandler _uiHandler = new BackupManagerUIHandler(null);
    public BackupManagerUIHandler uiHandler() { return _uiHandler; }
    public void addUICallback(BackupManagerCallback callback)
    {
        _uiHandler.addCallback(callback);
    }

    final static private String APP_KEY = "jb5r87iwgoambe7";
    final static private String APP_SECRET = "dgq8h1ewnxvh8fs";
    final static private String APP_DROPBOX_TOKEN = "dropbox_app_token";

    public static void setContext(Context context)
    {
        BackupManager._context = context;
    }

    private BackupManager()
    {
        // Dropbox 연결/인증 초기화 :
        SharedPreferences preferences = _context.getSharedPreferences(APP_DROPBOX_TOKEN, Context.MODE_PRIVATE);
        String token = preferences.getString(APP_DROPBOX_TOKEN, "");
        if (token != null && token.length() > 0) {
            // Dropbox set token :
            AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
            AndroidAuthSession session = new AndroidAuthSession(appKeys);
            _mDBApi = new DropboxAPI<AndroidAuthSession>(session);
            _mDBApi.getSession().setOAuth2AccessToken(token);
        }
        else {
            // Dropbox authentication :
            AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
            AndroidAuthSession session = new AndroidAuthSession(appKeys);
            _mDBApi = new DropboxAPI<AndroidAuthSession>(session);
            _mDBApi.getSession().startOAuth2Authentication(_context);
        }
    }

    public static BackupManager instance()
    {
        if (_instance == null) {
            _instance = new BackupManager();
        }
        return _instance;
    }

    public void dropboxAuthenticationResult()
    {
        if (_mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                _mDBApi.getSession().finishAuthentication();

                String accessToken = _mDBApi.getSession().getOAuth2AccessToken();

                SharedPreferences preferences = _context.getSharedPreferences(APP_DROPBOX_TOKEN, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(APP_DROPBOX_TOKEN, accessToken);
                editor.commit();

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
        else if (_mDBApi.getSession().isLinked()) {
            DebugLog.TRACE("Dropbox session linked");
        }
        else {
            /// 인증 토큰 삭제
            SharedPreferences preferences = _context.getSharedPreferences(APP_DROPBOX_TOKEN, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APP_DROPBOX_TOKEN, "");
            editor.commit();
        }
    }

    public boolean doBackup() {

        boolean onRunning = true;
        if (_backupTask == null) {
            onRunning = false;
        }
        else if (_backupTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            onRunning = false;
        }

        if (onRunning == false) {
            _backupTask = new BackupTask(_context, _mDBApi, _uiHandler, null);
            _backupTask.execute();
            return true;
        }

        DebugLog.TRACE("Temp");
        return false;
    }

    public boolean backupRunning()
    {
        boolean onRunning = false;
        if (_backupTask == null) {
            return false;
        }
        if (_backupTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            return true;
        }
        if (_backupTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
            return false;
        }

        return false;
    }
}