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
            "http://i.jjalbox.com/_data/jjalbox/2015/10/20151002_560e670d1896f.gif", "유머",
            "http://i.jjalbox.com/_data/jjalbox/2015/10/20151002_560e646f33cb8.jpg", "유머",
            "http://i.jjalbox.com/_data/jjalbox/2015/09/20150916_55f945f439359.gif", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_02.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_02.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_03.jpg", "유머",
            "http://storyis.img7.kr/web/gomu/bra/hwaege/br/M/Hwaege_brown_M_05.jpg", "유머",
            "http://storyis.img7.kr/web/gomu/bra/hwaege/br/M/Hwaege_brown_M_07.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_04.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_05.jpg", "유머",
            "http://storyis.img7.kr/web/HYO/bra/lv2_see_black/lv2_see_black_04.jpg", "유머",
            "http://storyis.img7.kr/web/HYO/bra/lv2_see_black/lv2_see_black_05.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_06.jpg", "유머",
            "http://storyis.img7.kr/web/gomu/bra/hwaege/br/M/Hwaege_brown_M_08.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk/eveleen_bk_03.jpg", "유머",
            "http://storyis.img7.kr/web/HYO/bra/lv2_see_black/lv2_see_black_15.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/input/wedding_bk/braset_wedding_bk.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_02.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_02.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_03.jpg", "유머",
            "http://storyis.img7.kr/web/HYO/bra/lilya_ivory/lilya_ivory_03.jpg", "유머",
            "http://storyis.img7.kr/web/HYO/bra/lilya_ivory/lilya_ivory_06.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_04.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_05.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_06.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk/eveleen_bk_02.jpg", "유머",
            "http://storyis.img7.kr/web/HYO/bra/witch_hpk/witch_hpk_02.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk/eveleen_bk_04.jpg", "유머",
            "http://storyis.img7.kr/web/HYO/bra/nudist_sk/nudist_sk_01.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_02.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_02.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_03.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_06.jpg", "유머",
            "http://storyis.img7.kr/web/gomu/bra/hwaege/br/M/Hwaege_brown_M_08.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk/eveleen_bk_03.jpg", "유머",
            "http://storyis.img7.kr/web/HYO/bra/lv2_see_black/lv2_see_black_15.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/input/wedding_bk/braset_wedding_bk.jpg", "유머",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk/eveleen_bk_02.jpg", "유머",
            "http://storyis.img7.kr/web/gomu/bra/hwaege/br/M/Hwaege_brown_M_08.jpg", "진지",
            "http://storyis.img7.kr/web/HYO/bra/lv2_see_black/lv2_see_black_04.jpg", "진지",
            "http://storyis.img7.kr/web/HYO/bra/lv2_see_black/lv2_see_black_05.jpg", "진지",
            "http://storyis.img7.kr/web/HYO/bra/lv2_see_black/lv2_see_black_15.jpg", "진지",
            "http://storyis.img7.kr/web/HYO/bra/nudist_sk/nudist_sk_01.jpg", "진지",
            "http://storyis.img7.kr/web/HYO/bra/witch_hpk/witch_hpk_02.jpg", "진지",
            "http://storyis.img35.makeshop.co.kr/input/wedding_bk/braset_wedding_bk.jpg", "급정색",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk/eveleen_bk_02.jpg", "급정색",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk/eveleen_bk_03.jpg", "급정색",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk/eveleen_bk_04.jpg", "급정색",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_02.jpg", "근성",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_02.jpg", "근성",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_03.jpg", "근성",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_04.jpg", "근성",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_05.jpg", "간지",
            "http://storyis.img35.makeshop.co.kr/web/HYO/braset/eveleen_bk_long/eveleen_bk_long_06.jpg", "간지",
            "http://storyis.img7.kr/web/HYO/bra/lilya_ivory/lilya_ivory_03.jpg", "간지",
            "http://storyis.img7.kr/web/HYO/bra/lilya_ivory/lilya_ivory_06.jpg", "간지",
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

