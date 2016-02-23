package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Utils.DebugLog;

/**
 * Created by vh1981 on 15. 10. 7.
 */
public class BackupTask extends AsyncTask<Integer/*param*/, Integer/*progress*/, Boolean /*result*/> {

    private DropboxAPI<AndroidAuthSession> _mDBApi;
    boolean _taskSucceeded = false;
    public ArrayList<String> _filesStr = null;

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        do {
            Message msg = new Message();
            msg.what = BackupManagerUIHandler.MSG_WHAT_BACKUP_FINISHED;
            msg.arg1 = _taskSucceeded == true ? 1 : 0; /// 성공/실패 여부
            _uiHandler.sendMessage(msg);
        } while(false);
    }

    private BackupManagerUIHandler _uiHandler;

    public BackupTask(Context context, DropboxAPI<AndroidAuthSession> dbApi, BackupManagerUIHandler handler, ArrayList<String> filesStr)
    {
        _mDBApi = dbApi;
        _uiHandler = handler;
        _filesStr = filesStr;
    }

    public boolean backup()
    {
        boolean ret = true;
        String dbFilePath = ImageManager.instance().dbFilePath();
        String dbFileName = ImgDbHelper.DATABASE_NAME;

        do {
            Message msg = new Message();
            msg.what = BackupManagerUIHandler.MSG_WHAT_BACKUP_STARTED;
            _uiHandler.sendMessage(msg);
        } while(false);

        try {

            // tag db는 저장하지 않는다.
//            File file = new File(dbFilePath);
//            FileInputStream inputStream = new FileInputStream(file);
//            DropboxAPI.Entry response = _mDBApi.putFileOverwrite("/FunnyPhotoStorage/" + dbFileName, inputStream, file.length(), null);
//            DebugLog.TRACE("DB file uploaded : " + response.rev);


//            ArrayList<String> filesStr = ImageManager.instance().imageFiles();
            // 지정된 파일들이 없으면 일단 다함
            if (_filesStr == null ||_filesStr.size() == 0) {
                _filesStr = ImageManager.instance().imageFiles();
            }

            int percentage =(int) ((1.0f / _filesStr.size() + 1.0f) * 100.0f);
            publishProgress(percentage);

            float currentCnt = 0.0f;
            for (String filepath : _filesStr) {
                File imageFile = new File(filepath);
                FileInputStream imageFileInputStream = new FileInputStream(imageFile);
                String imageFileName = imageFile.getName();

                DropboxAPI.Entry existingEntry = null;
                try {
                    existingEntry = _mDBApi.metadata("/FunnyPhotoStorage/" + imageFileName, 1, null, false, null);
                }
                catch (DropboxException e) {
                    e.printStackTrace();
                }

                boolean exists = false;
                if (existingEntry != null && existingEntry.contents != null) {
                    if (existingEntry.contents.size() == imageFile.length()) {
                        exists = true;
                    }
                }

                if (!exists) {
                    ProgressListener listener = new ProgressListener() {
                        @Override
                        public void onProgress(long l, long l1) {
                            /// 여기서 바깥으로 내보내야...
                            /// what to do?
                        }
                    };
                    //putFileOverwrite(String path, InputStream is, long length, ProgressListener listener)
                    DropboxAPI.Entry r = _mDBApi.putFileOverwrite("/FunnyPhotoStorage/" + imageFileName, imageFileInputStream,
                            imageFile.length(), /*progressListener*/ listener);
                    DebugLog.TRACE("image file uploaded : " + r.rev);

                    currentCnt += 1.0f;
                    percentage = (int) ((1.0f + currentCnt / _filesStr.size() + 1.0f) * 100.0f);
                }

                publishProgress(percentage);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ret = false;
        } catch (DropboxException e) {
            e.printStackTrace();
            ret = false;
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }

        _taskSucceeded = ret;

        return ret;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Boolean doInBackground(Integer... params) {
        return backup();
    }

}
