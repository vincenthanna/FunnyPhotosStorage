package Utils;

import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.URLUtil;

import com.dropbox.client2.DropboxAPI;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by vh1981 on 15. 1. 8.
 */
public class ClipboardHelper {
    Context _context;

    private ClipboardHelper() {}

    public ClipboardHelper(Context context) {
        _context = context;
    }

    /* clip에 이미지 파일이나 이미지 객체가 있는지 확인한다.
     *      1. 인터넷 링크일 경우 해당 파일을 임시위치에 다운받는다.
     *      2. 이미지 자체의 객체일 경우 임시위치에 파일로 생성한다.
     * 이미지 파일의 이름을 unique하게 생성한다.
     */
    public Bitmap getImageFromClipboard()
    {
        ClipboardManager manager = (ClipboardManager) _context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager.hasPrimaryClip()) {
            ClipDescription desc = manager.getPrimaryClipDescription();

            ClipData clipData = manager.getPrimaryClip();

            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item item = clipData.getItemAt(i);

                // if clipdata is URI type:
                if (item.getText() != null) {
                    String imageFileUrl = item.getText().toString();

                    // URL문자열이 올바른 html주소인지 확인한다.
                    try {
                        URL url = new URL(imageFileUrl);

                        if (URLUtil.isValidUrl(url.toString())) {
                            DebugLog.TRACE("VALID URL!");
                            // 이미지 다운로드를 다른 스레드에서 한다.
                            ClipboardHelperAsyncTask task = new ClipboardHelperAsyncTask();
                            task.execute("LoadImageFromURL", url.toString());

                            if (task.get() == true) {
                                Bitmap b = task.loadedImageBitmap();

                                return b;
                            }
                        }
                        else {
                            DebugLog.TRACE("INVALID URL!");
                        }
                    }
                    catch (MalformedURLException e) {
                        DebugLog.TRACE("Exception=" + e.getMessage());
                        e.printStackTrace();
                    }
                    catch (InterruptedException e) {
                        DebugLog.TRACE("Exception=" + e.getMessage());
                        e.printStackTrace();
                    }
                    catch (ExecutionException e) {
                        DebugLog.TRACE("Exception=" + e.getMessage());
                        e.printStackTrace();
                    }
                }
                else if (item.getHtmlText() != null) {
                    DebugLog.TRACE(i + " data is Text! = " + item.getHtmlText());

                }
                else if (item.getUri() != null) {
                    DebugLog.TRACE(i + " data is URI!" + item.getUri().toString());
                }
                else {
                    DebugLog.TRACE(i + " data is NOT BE PROCESSED!");
                }
            }
        }
        return null;
    }
}

class ClipboardHelperAsyncTask extends AsyncTask<String, Integer, Boolean> /* params, progress, result */ {
    public static String TASK_LOAD_IMG_FROM_URL = "LoadImageFromURL";
    Drawable _loadedImageDrawable = null;
    public Bitmap _loadedImageBitmap = null;
    public Drawable loadedImageDrawable()
    {
        return _loadedImageDrawable;
    }
    public Bitmap loadedImageBitmap() { return _loadedImageBitmap; }


    protected Boolean doInBackground(String... params) {
        if (params[0] == TASK_LOAD_IMG_FROM_URL) {
            String str = params[1]; // Image URL이 넘어온다.
            return loadImageFromURL(str);
        }
        return false;
    }

    // background job functions : load image from URL
    private Boolean loadImageFromURL(String url) {
        try {
            DebugLog.TRACE("URL=" + url);
            InputStream is = (InputStream)new URL(url).openStream();
            _loadedImageBitmap = BitmapFactory.decodeStream(is);
            return true;

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {

    }
}