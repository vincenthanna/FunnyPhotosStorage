package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.content.Context.*;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
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
public class BackupManager extends HandlerThread {
    private static BackupManager _instance = null;
    private static Context _context;
    private DropboxAPI<AndroidAuthSession> _mDBApi;
    BackupTask _backupTask = null;
    public BackupTask get_backupTask() { return _backupTask; }
    Handler _handler = null;
    boolean _running = false;
    public boolean isRunning() { return _running; }


    /**
     * Starts the new Thread of execution. The <code>run()</code> method of
     * the receiver will be called by the receiver Thread itself (and not the
     * Thread calling <code>start()</code>).
     *
     * @throws IllegalThreadStateException - if this thread has already started.
     * @see Thread#run
     */
    @Override
    public synchronized void start() {
        super.start();
        _running = true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    /// Dropbox 로그인/인증 관련 코드

    final static private String APP_KEY = "jb5r87iwgoambe7";
    final static private String APP_SECRET = "dgq8h1ewnxvh8fs";
    final static private String APP_DROPBOX_TOKEN = "dropbox_app_token";

    public static void setContext(Context context)
    {
        BackupManager._context = context;
    }

    public BackupManager(String name) {
        super(name);
        dropboxAuthentication();
    }

    public static BackupManager instance()
    {
        if (_instance == null) {
            _instance = new BackupManager("BackupManager");
        }
        return _instance;
    }

    private void dropboxAuthentication()
    {
        // Dropbox 연결/인증 초기화 :
        SharedPreferences preferences = _context.getSharedPreferences(APP_DROPBOX_TOKEN, Context.MODE_PRIVATE);
        String token = preferences.getString(APP_DROPBOX_TOKEN, "");
        if (token.length() > 0) {
            // Dropbox set token :
            AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
            AndroidAuthSession session = new AndroidAuthSession(appKeys);
            _mDBApi = new DropboxAPI<AndroidAuthSession>(session);
            _mDBApi.getSession().setOAuth2AccessToken(token);
        } else {
            // Dropbox authentication :
            AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
            AndroidAuthSession session = new AndroidAuthSession(appKeys);
            _mDBApi = new DropboxAPI<AndroidAuthSession>(session);
            _mDBApi.getSession().startOAuth2Authentication(_context);
        }
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
            // 한 번 인증이 성공하면 offline에서도 성공함
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

    public boolean isDropboxReady()
    {
        if (_mDBApi.getSession().isLinked()) {
            return true;
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Call back method that can be explicitly overridden if needed to execute some
     * setup before Looper loops.
     */
    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        _handler = new Handler(getLooper()) {
            /**
             * Subclasses must implement this to receive messages.
             *
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                _backupTask = (BackupTask) msg.obj;
                _backupTask.run();
                super.handleMessage(msg);
            }

        };
    }

    public void putTask(BackupTask.JOB job)
    {
        if (isDropboxReady()) {
            if (isNetworkOnline()) {
                BackupTask task = new BackupTask(job, _context, _mDBApi, null);
                if (_handler != null) {
                    DebugLog.TRACE("backup task reserved");
                    Message msg = Message.obtain(_handler);
                    msg.obj = task;
                    _handler.sendMessage(msg);
                }
            }
        }
        else {
            dropboxAuthentication();
        }
    }

    public boolean isBackupRunning() {
        if (_backupTask != null) {
            return _backupTask.isRunning();
        }
        return false;
    }

    public boolean isNetworkOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

}