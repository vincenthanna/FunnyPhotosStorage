package vh1981.com.funnyphotosstorage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;

import org.bouncycastle.crypto.SignerWithRecovery;
import org.bouncycastle.crypto.StreamBlockCipher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Utils.CommonUtil;
import Utils.DebugLog;

/**
 * Created by vh1981 on 15. 10. 7.
 */
public class BackupTask implements Runnable{

    private DropboxAPI<AndroidAuthSession> _mDBApi;
    boolean _taskSucceeded = false;
    public ArrayList<String> _filesStr = null;
    int _progress = 0;
    boolean _isRunning = false;
    public enum JOB {BACKUP, RESTORE};
    JOB _job = JOB.BACKUP;
    public JOB get_job() { return _job; }

    int progress() {
        return _progress;
    }

    boolean isRunning() {
        return _isRunning;
    }

    String dbFilePath(String filename)
    {
        return ("/FunnyPhotoStorage/" + filename);
    }

    public BackupTask(JOB job, Context context, DropboxAPI<AndroidAuthSession> dbApi, ArrayList<String> filesStr)
    {
        _mDBApi = dbApi;
        _filesStr = filesStr;
        _job = job;
    }

    public boolean backup()
    {
        boolean ret = true;
        String dbFilePath = ImageManager.instance().dbFilePath();
        String dbFileName = ImgDbHelper.DATABASE_NAME;

        _progress = 0;

        try {
            DebugLog.TRACE("backup task started ...");
            _isRunning = true;

            // 지정된 파일들이 없으면 일단 다함
            if (_filesStr == null ||_filesStr.size() == 0) {
                _filesStr = ImageManager.instance().imageFiles();
            }

            _progress = 0;

            int currentCnt = 0;
            for (String filepath : _filesStr) {
                File imageFile = new File(filepath);
                FileInputStream imageFileInputStream = new FileInputStream(imageFile);
                String imageFileName = imageFile.getName();

                DebugLog.TRACE("checking file... : " + imageFileName);

                DropboxAPI.Entry existingEntry = null;
                boolean noExceptionRaised = true;
                try {
                    existingEntry = _mDBApi.metadata("/FunnyPhotoStorage/" + imageFileName, 1, null, false, null);
                }
                catch (DropboxException e) {
                    //e.printStackTrace();
                    noExceptionRaised = false;
                }

                ArrayList<String> modifiedFiles = ImageManager.instance().getModifiedFilenames();

                boolean skip = false;
                if (noExceptionRaised) {
                    if (existingEntry != null && existingEntry.bytes != 0) {
                        skip = true;
                    }

                    for (String fname : modifiedFiles) {
                        if (fname.equals(imageFileName)) {
                            skip = false;
                            ImageManager.instance().dropModifiedFilename(fname);
                        }
                    }
                }

                if (!skip) {
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

                    currentCnt++;
                    int progress = (int) ((1.0f + (float)currentCnt / _filesStr.size() + 1.0f) * 100.0f);

                    if (progress != _progress) {
                        DebugLog.TRACE(progress + "% completed");
                    }

                    _progress = progress;
                }
            }

            DebugLog.TRACE("completed.");

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

        _isRunning = false;

        return ret;
    }

    public boolean clearDeleted()
    {
        boolean ret = true;

        _progress = 0;

        try {
            DebugLog.TRACE("clear task started ...");

            _isRunning = true;
            _filesStr = ImageManager.instance().getDeletedFilenames();
            _progress = 0;
            int currentCnt = 0;
            for (String imageFileName : _filesStr) {
                DropboxAPI.Entry existingEntry = null;
                try {
                    existingEntry = _mDBApi.metadata("/FunnyPhotoStorage/" + imageFileName, 1, null, false, null);
                }
                catch (DropboxException e) {
                    e.printStackTrace();
                }

                boolean exists = false;
                if (existingEntry != null) {
                    exists = true;
                }

                if (exists) {
                    boolean succeeded = true;
                    try {
                        _mDBApi.delete("/FunnyPhotoStorage/" + imageFileName);
                    }
                    catch (DropboxException e) {
                        e.printStackTrace();
                        succeeded = false;
                    }

                    if (succeeded) {
                        ImageManager.instance().dropDeletedFilename(imageFileName); /// 내부 db에서도 삭제
                        DebugLog.TRACE("image file deleted = " + imageFileName);
                    }
                    else {
                        DebugLog.TRACE("update delete failed! " + "(" + imageFileName + ")");
                    }

                    currentCnt++;
                    int progress = (int) ((1.0f + (float)currentCnt / _filesStr.size() + 1.0f) * 100.0f);

                    if (progress != _progress) {
                        DebugLog.TRACE(progress + "% completed");
                    }

                    _progress = progress;
                }
            }

            DebugLog.TRACE("completed.");
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }

        _taskSucceeded = ret;

        _isRunning = false;

        return ret;
    }



    private boolean downloadDropboxFile(String filename) {
        try {
            String localFilePath = ImageFileManager.directoryPath(filename);
            File localFile = new File(localFilePath);

            if (!localFile.exists()) {
                localFile.createNewFile();
                copy(dbFilePath(filename), localFile);
            }
            else {
                assert false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void copy(final String dbPath, final File localFile) {

        BufferedInputStream br = null;
        BufferedOutputStream bw = null;
        try {
            DropboxAPI.DropboxInputStream fd = _mDBApi.getFileStream(dbPath,null);

            br = new BufferedInputStream(fd);
            bw = new BufferedOutputStream(new FileOutputStream(localFile));

            byte[] buffer = new byte[4096];
            int read;
            while (true) {
                read = br.read(buffer);
                if (read <= 0) {
                    break;
                }
                bw.write(buffer, 0, read);
            }


        } catch (DropboxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    void restore()
    {
        boolean ret = true;
        String dbFilePath = ImageManager.instance().dbFilePath();
        String dbFileName = ImgDbHelper.DATABASE_NAME;
        ArrayList<String> tags = new ArrayList<String>();

        _progress = 0;

        try {
            DebugLog.TRACE("backup task started ...");
            _isRunning = true;

            _filesStr = ImageManager.instance().getDeletedFilenames();

            int currentCnt = 0;

            DropboxAPI.Entry dirent = _mDBApi.metadata("/FunnyPhotoStorage/", 1000, null, true, null);
            for (DropboxAPI.Entry ent : dirent.contents) {
                String filename = ent.fileName();

                // 없는 파일만 복사해옴
                if (CommonUtil.<String>isExists(_filesStr, filename) == false) {
                    downloadDropboxFile(filename);
                    // 파일로부터 태그를 추출해서 모아둔다.
                    String tag = ImageManager.instance().getTagFromImageFile(filename);
                    if (tag != null) {
                        if (CommonUtil.<String>isExists(tags, tag) == false) {
                            tags.add(tag);
                            DebugLog.TRACE("tag added=" + tag);
                        }
                    }
                    DebugLog.TRACE("restored : " + filename);
                }
            }

            // 추출된 tag들을 tag db에 추가한다.
            for (String tag : tags) {
                if (ImageManager.instance().addTag(tag) == true) {
                    DebugLog.TRACE("tag added = " + tag);
                }
            }

            ImageManager.instance().setNeedUpdate();

            _isRunning = false;

            DebugLog.TRACE("completed.");

        } catch (DropboxException e) {
            e.printStackTrace();
            ret = false;
        } catch (Exception e) {
            e.printStackTrace();
            ret = false;
        }

        _taskSucceeded = ret;

        _isRunning = false;
    }

    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        if (_job == JOB.BACKUP) {
            backup();
            clearDeleted();
        }
        else if (_job == JOB.RESTORE)
        {
            restore();
        }
    }

    //    /**
//     * Override this method to perform a computation on a background thread. The
//     * specified parameters are the parameters passed to {@link #execute}
//     * by the caller of this task.
//     * <p/>
//     * This method can call {@link #publishProgress} to publish updates
//     * on the UI thread.
//     *
//     * @param params The parameters of the task.
//     * @return A result, defined by the subclass of this task.
//     * @see #onPreExecute()
//     * @see #onPostExecute
//     * @see #publishProgress
//     */
//    @Override
//    protected Boolean doInBackground(Integer... params) {
//        return backup();
//    }

}
