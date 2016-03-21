package Defines;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import vh1981.com.funnyphotosstorage.Image;
import vh1981.com.funnyphotosstorage.ImageContainer;
import vh1981.com.funnyphotosstorage.ImageFileManager;

/**
 * Created by vh1981 on 15. 10. 29.
 */
public class SampleData {

    public String imageUrls[] = {
            "http://cfile215.uf.daum.net/image/1460774B50986045045A57", "유머",
            "http://cfile235.uf.daum.net/image/131CEA194C15F77D0E5B23", "유머",
            "http://cfs12.blog.daum.net/image/25/blog/2008/08/02/00/20/489329a46cd61&filename=23.jpg", "유머",
            "http://blog.donga.com/kangsdogs/files/2012/04/13645571391334494032.jpg", "유머",
            "http://cfile10.uf.tistory.com/image/12680D4D50C37C391F0F1C", "유머",
            "http://wstarnews.hankyung.com/wstardata/images/photo/201303/0d2e3aac4b4f1bcf09c19809f3111436.jpg", "유머",
            "http://pds12.egloos.com/pds/200906/07/13/a0100413_4a2af42599138.jpg", "유머",
            "http://ojsfile.ohmynews.com/STD_IMG_FILE/2007/1115/IE000832413_STD.jpg", "유머",
            "http://cfile215.uf.daum.net/image/1460774B50986045045A57", "유머",
            "http://cfile235.uf.daum.net/image/131CEA194C15F77D0E5B23", "유머",
            "http://cfs12.blog.daum.net/image/25/blog/2008/08/02/00/20/489329a46cd61&filename=23.jpg", "유머",
            "http://blog.donga.com/kangsdogs/files/2012/04/13645571391334494032.jpg", "유머",
            "http://cfile10.uf.tistory.com/image/12680D4D50C37C391F0F1C", "유머",
            "http://wstarnews.hankyung.com/wstardata/images/photo/201303/0d2e3aac4b4f1bcf09c19809f3111436.jpg", "유머",
            "http://pds12.egloos.com/pds/200906/07/13/a0100413_4a2af42599138.jpg", "유머",
            "http://ojsfile.ohmynews.com/STD_IMG_FILE/2007/1115/IE000832413_STD.jpg", "유머",
            "http://cfile215.uf.daum.net/image/1460774B50986045045A57", "유머",
            "http://cfile235.uf.daum.net/image/131CEA194C15F77D0E5B23", "유머",
            "http://cfs12.blog.daum.net/image/25/blog/2008/08/02/00/20/489329a46cd61&filename=23.jpg", "유머",
            "http://blog.donga.com/kangsdogs/files/2012/04/13645571391334494032.jpg", "유머",
            "http://cfile10.uf.tistory.com/image/12680D4D50C37C391F0F1C", "유머",
            "http://wstarnews.hankyung.com/wstardata/images/photo/201303/0d2e3aac4b4f1bcf09c19809f3111436.jpg", "유머",
            "http://pds12.egloos.com/pds/200906/07/13/a0100413_4a2af42599138.jpg", "유머",
            "http://ojsfile.ohmynews.com/STD_IMG_FILE/2007/1115/IE000832413_STD.jpg", "유머",

            "http://cfile1.uf.tistory.com/image/255D8B4E51ADD7CD04EF09", "진지",
            "http://cfile23.uf.tistory.com/image/2320D94152B69F832CCD71", "진지",
            "https://i.ytimg.com/vi/xxJWsxLGwn8/maxresdefault.jpg", "진지",
            "http://cfile24.uf.tistory.com/image/27027D48523284CF1D3682", "진지",
            "http://cfile1.uf.tistory.com/image/255D8B4E51ADD7CD04EF09", "진지",
            "http://cfile23.uf.tistory.com/image/2320D94152B69F832CCD71", "진지",
            "https://i.ytimg.com/vi/xxJWsxLGwn8/maxresdefault.jpg", "진지",
            "http://cfile24.uf.tistory.com/image/27027D48523284CF1D3682", "진지",
            "http://cfile1.uf.tistory.com/image/255D8B4E51ADD7CD04EF09", "진지",
            "http://cfile23.uf.tistory.com/image/2320D94152B69F832CCD71", "진지",
            "https://i.ytimg.com/vi/xxJWsxLGwn8/maxresdefault.jpg", "진지",
            "http://cfile24.uf.tistory.com/image/27027D48523284CF1D3682", "진지",

            "http://m1.i.pbase.com/v3/13/533813/1/51485991.KoreanCow.jpg", "급정색",
            "http://bikemap.ideaapp.co.kr/ckfinder/userfiles/images/소.jpg", "급정색",
            "http://www.nbcambodia.com/wp-content/uploads/2013/11/소.jpg", "급정색",
            "http://m1.i.pbase.com/v3/13/533813/1/51485991.KoreanCow.jpg", "급정색",
            "http://www.hanwooboard.or.kr/upload_file/notice/3/notice_3_584.JPG", "급정색",
            "http://www.nbcambodia.com/wp-content/uploads/2013/11/소.jpg", "급정색",
            "http://cfs8.blog.daum.net/upload_control/download.blog?fhandle=MDlqU3JAZnM4LmJsb2cuZGF1bS5uZXQ6L0lNQUdFLzAvNC5qcGcudGh1bWI=&filename=4.jpg", "급정색",
            "http://bikemap.ideaapp.co.kr/ckfinder/userfiles/images/소.jpg", "급정색",
            "http://cfile213.uf.daum.net/image/115DC5024CE48BC6205A9E", "급정색",


            "http://cfile4.uf.tistory.com/image/2103D73452EE6FD413C04E", "근성",
            "http://www.ua.all.biz/img/ua/catalog/2130771.jpeg", "근성",
            "http://cfile26.uf.tistory.com/image/234FB03953E44CEA2642FB", "근성",
            "http://cfile6.uf.tistory.com/image/13378D0E4CA98F5F7F37DC", "근성",
            "http://images.natureworldnews.com/data/images/full/19297/pig.jpg", "근성",
            "http://www.salimstory.net/board/data/blog/file/admin/30674493_3af40cb9_C5A9B1E2BAAFC8AF_lyw_031.JPG", "근성",


            "http://img2.ruliweb.daum.net/img/img_link7/820/819662_1.jpg", "간지",
            "http://ecache.ilbe.com/files/attach/new/20160314/377678/944245468/7700250471/6fc9e8b2f11d6a3a5bae0cc2172ce322.jpg", "간지",
            "http://cfile8.uf.tistory.com/image/2243E6385385ADBF2C4143", "간지",

            "https://pixabay.com/static/uploads/photo/2015/02/27/16/26/rabbit-652329_960_720.jpg", "간지",
            "http://cfs13.blog.daum.net/image/12/blog/2008/06/05/11/53/4847549c33c6a&filename=토끼와밤비.jpg", "간지",
            "http://cfile27.uf.tistory.com/image/231C4A33531C8C951DED55", "간지",

    };


    private static SampleData _instance;
    ImageContainer _imgContainer = new ImageContainer();
    private static Context _context;


    public static void setContext(Context context) {
        SampleData._context = context;
    }

    public static SampleData getInstance() {
        if (_instance == null) {
            _instance = new SampleData();
        }
        return _instance;
    }

    public SampleData() {

        LoadImagesAsyncTask task = new LoadImagesAsyncTask();
        task.execute("LoadImageFromURL", "");
    }

    void loadImage(String url, String tag) {
        try {

            InputStream is = (InputStream)new URL(url).openStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            //Uri uri = Uri.parse(url);
            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(_context.getContentResolver(), uri);
            ImageFileManager.getInstance().createImageFile(bitmap, tag);

            Image img = new Image(url, tag, ImageFileManager.getInstance());
            _imgContainer.addImage(img);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    class LoadImagesAsyncTask extends AsyncTask<String, Integer, Boolean> /* params, progress, result */ {

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
        protected Boolean doInBackground(String... params) {
            for (int i = 0 ; i < imageUrls.length / 2; i++) {
                loadImage(imageUrls[i*2], imageUrls[(i*2)+1]);
            }

            return null;
        }
    }
}

