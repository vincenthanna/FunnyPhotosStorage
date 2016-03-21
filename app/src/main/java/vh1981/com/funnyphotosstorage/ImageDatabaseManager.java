package vh1981.com.funnyphotosstorage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Path;
import android.util.DebugUtils;
import android.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import Utils.DebugLog;

/**
 * Created by yeonhuikim on 14. 12. 27..
 */
public class ImageDatabaseManager {

    private static ImageDatabaseManager _instance;
    private ImgDbHelper _dbHelper;
    private static Context _context;
    private static String[] _defaultTags = {"유머","진지","급정색", "근성", "간지"};
    private static long UN_TAGGED = -1;

    ////////////////////////////////////////////////////////////////////////////////
    /// 공개 기능들 :
    ////////////////////////////////////////////////////////////////////////////////
    public static void setContext(Context context)
    {
        ImageDatabaseManager._context = context;
    }

    private ImageDatabaseManager()
    {
        _dbHelper = new ImgDbHelper(_context);
        if (needToInitDefaultTags()) {
            initDefaultTags();
        }
    }

    private boolean needToInitDefaultTags()
    {
        if (MySharedPreferences.instance().get_intentAction() == Intent.ACTION_SEND) {
            return false;
        }

        if (MySharedPreferences.instance().get_intentAction() == Intent.ACTION_GET_CONTENT) {
            return false;
        }
        return true;
    }


    public static ImageDatabaseManager getInstance() {
        if (_instance == null) {
            _instance = new ImageDatabaseManager();
        }
        return _instance;
    }

    public boolean addTag(String tag) {
        int tid = findTagId(tag);
        if (tid < 0) { // 없으므로 새로 생성
            SQLiteDatabase db = _dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ImgDbHelper.TagsEntry.COLUMN_NAME_TAGNAME, tag);
            long id = db.insert(ImgDbHelper.TagsEntry.TABLE_NAME, null, values);

            File f = _context.getDatabasePath(_dbHelper.DATABASE_NAME);
            String pathStr = f.getAbsolutePath();
            if (id < 0) {
                assert false;
            }
            db.close();
            return true;
        }
        else {
            return false;
        }
    }

    public boolean removeTag(String tag)
    {
        int tid = findTagId(tag);
        if (tid >= 0) {
            // tag에서 삭제
            SQLiteDatabase db = _dbHelper.getWritableDatabase();
            String whereClause = ImgDbHelper.TagsEntry._ID + " = ?";
            String[] whereArgs = new String[]{Long.toString(tid)};
            int cnt = db.delete(ImgDbHelper.TagsEntry.TABLE_NAME, whereClause, whereArgs);
            if (cnt == 0) {
                DebugLog.TRACE("Error!");
                assert false;
            }
            return true;
        }
        return true;
    }

    public boolean tagExists(String tag)
    {
        if (findTagId(tag) >= 0) {
            return true;
        }
        return false;
    }

    public void initDefaultTags() {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        for(int i = 0; i < _defaultTags.length; i++) {
            ContentValues values = new ContentValues();
            values.put(ImgDbHelper.TagsEntry.COLUMN_NAME_TAGNAME, _defaultTags[i]);
            db.insert(ImgDbHelper.TagsEntry.TABLE_NAME, null, values);
        }

        db.close();
    }

    public ArrayList<String> getTags()
    {
        ArrayList<String> tags = new ArrayList<String>();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String query = "select " + ImgDbHelper.TagsEntry.COLUMN_NAME_TAGNAME + " from " + ImgDbHelper.TagsEntry.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        assert c != null;
        if (c.moveToFirst()) {
            do {
                String tag = c.getString(c.getColumnIndex(ImgDbHelper.TagsEntry.COLUMN_NAME_TAGNAME));
                tags.add(tag);
            } while (c.moveToNext());
        }
        c.close();
        return tags;
    }

    ////////////////////////////////////////////////////////////////////////////////
    /// 내부함수 :
    ////////////////////////////////////////////////////////////////////////////////
    private int findTagId(String tag) {
        String query = "select " + ImgDbHelper.TagsEntry._ID + " from " + ImgDbHelper.TagsEntry.TABLE_NAME + " where "
                + ImgDbHelper.TagsEntry.COLUMN_NAME_TAGNAME + " = " + "'" + tag + "'";
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        int tagId = 0;
        boolean found = false;
        assert c != null;
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    tagId = c.getInt(c.getColumnIndex(ImgDbHelper.TagsEntry._ID));
                    found = true;
                } while (c.moveToNext());
            }
            c.close();
        }

        if (found == false) {
            return -1;
        }
        return tagId;
    }

    public long insertDeletedFilename(Image image)
    {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ImgDbHelper.DeletedFilesEntry.COLUMN_NAME_FILENAME, image.get_filename());
        long ret = db.insert(ImgDbHelper.DeletedFilesEntry.TABLE_NAME, null, values);
        if (ret < 0) {
            assert false;
        }
        return ret;
    }

    public boolean dropDeletedFilename(String filename)
    {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String whereClause = ImgDbHelper.DeletedFilesEntry.COLUMN_NAME_FILENAME + " = ?";
        String[] whereArgs = new String[]{filename};
        int cnt = db.delete(ImgDbHelper.DeletedFilesEntry.TABLE_NAME, whereClause, whereArgs);
        if (cnt == 0) {
            DebugLog.TRACE("Error!");
            assert false;
        }
        return true;
    }

    public ArrayList<String> getDeletedFilenames()
    {
        ArrayList<String> deletedFilenames = new ArrayList<String>();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String query = "select " + ImgDbHelper.DeletedFilesEntry.COLUMN_NAME_FILENAME+ " from " + ImgDbHelper.DeletedFilesEntry.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        assert c != null;
        if (c.moveToFirst()) {
            do {
                String filename = c.getString(c.getColumnIndex(ImgDbHelper.DeletedFilesEntry.COLUMN_NAME_FILENAME));
                deletedFilenames.add(filename);
            } while (c.moveToNext());
        }
        c.close();
        return deletedFilenames;
    }

    public long insertModifiedFilename(Image image)
    {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ImgDbHelper.ModifiedFilesEntry.COLUMN_NAME_FILENAME, image.get_filename());
        long ret = db.insert(ImgDbHelper.ModifiedFilesEntry.TABLE_NAME, null, values);
        if (ret < 0) {
            assert false;
        }
        return ret;
    }

    public boolean dropModifiedFilename(String filename)
    {
        try {
            SQLiteDatabase db = _dbHelper.getWritableDatabase();
            String whereClause = ImgDbHelper.ModifiedFilesEntry.COLUMN_NAME_FILENAME + " = ?";
            String[] whereArgs = new String[]{filename};
            int cnt = db.delete(ImgDbHelper.ModifiedFilesEntry.TABLE_NAME, whereClause, whereArgs);
            if (cnt == 0) {
                DebugLog.TRACE("Error!");
                assert false;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<String> getModifiedFilenames()
    {
        ArrayList<String> filenames = new ArrayList<String>();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String query = "select " + ImgDbHelper.ModifiedFilesEntry.COLUMN_NAME_FILENAME+ " from " + ImgDbHelper.ModifiedFilesEntry.TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        assert c != null;
        if (c.moveToFirst()) {
            do {
                String filename = c.getString(c.getColumnIndex(ImgDbHelper.DeletedFilesEntry.COLUMN_NAME_FILENAME));
                filenames.add(filename);
            } while (c.moveToNext());
        }
        c.close();
        return filenames;
    }

    /*
    public long createImage(String filepath)
    {
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ImgDbHelper.ImagesEntry.COLUMN_NAME_FILENAME, filepath);
        long ret = db.insert(ImgDbHelper.ImagesEntry.TABLE_NAME, null, values);
        if (ret < 0) {
            assert false;
        }
        return ret;
    }

    public boolean createImageTag(int fileId, String tag)
    {
        // tag테이블에 해당 tag의 id를 확인해야 한다.
        String query = "select " + ImgDbHelper.TagsEntry._ID + " from " + ImgDbHelper.TagsEntry.TABLE_NAME + " where "
                + ImgDbHelper.TagsEntry.COLUMN_NAME_TAGNAME + " = " + "'" + tag + "'";
        DebugLog.TRACE("query = " + query);
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        int tagId = 0;
        boolean found = false;
        assert c != null;
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    tagId = c.getInt(c.getColumnIndex(ImgDbHelper.TagsEntry._ID));
                    found = true;
                } while (c.moveToNext());
            }
            c.close();
        }

        if (found == false) {
            return false;
        }

        // ImageTags에 추가한다.
        ContentValues values = new ContentValues();
        values.put(ImgDbHelper.ImageTagEntry.COLUMN_NAME_FILEID, fileId);
        values.put(ImgDbHelper.ImageTagEntry.COLUMN_NAME_TAGID, tagId);
        db.insert(ImgDbHelper.ImageTagEntry.TABLE_NAME, null, values);

        return true;
    }

    public String findTag(int tagId) {
        String query = "select " + ImgDbHelper.TagsEntry.COLUMN_NAME_TAGNAME + " from " + ImgDbHelper.TagsEntry.TABLE_NAME + " where "
                + ImgDbHelper.TagsEntry._ID + " = " + tagId;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        String tag = null;
        boolean found = false;
        assert c != null;
        if (c != null) {
            if (c.moveToFirst()) {
                tag = c.getString(c.getColumnIndex(ImgDbHelper.TagsEntry.COLUMN_NAME_TAGNAME));
                found = true;
            }
            c.close();
        }

        if (found == false) {
            assert false;
        }
        return tag;
    }

    public String findFilepath(int fileId) {
        String query = "select " + ImgDbHelper.ImagesEntry.COLUMN_NAME_FILENAME + " from " + ImgDbHelper.ImagesEntry.TABLE_NAME
                + " where " + ImgDbHelper.ImagesEntry._ID + " = " + fileId;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        String filepath = "";
        boolean found = false;
        if (c != null) {
            if (c.moveToFirst()) {
                filepath = c.getString(c.getColumnIndex(ImgDbHelper.ImagesEntry.COLUMN_NAME_FILENAME));
                found = true;
            }
        }

        if (found == false) {
            assert false;
        }
        return filepath;
    }
    */

    /*
    public ImageContainer getImageContainer(int tagId, BitmapSupplier supplier)
    {
        ImageContainer imageContainer = new ImageContainer();
        String tag;
        int fileId;
        String filepath;

        tag = findTag(tagId); // tagId에서 태그를 얻는다.

        String query = "select " + ImgDbHelper.ImageTagEntry.COLUMN_NAME_FILEID + " from " + ImgDbHelper.ImageTagEntry.TABLE_NAME + " where "
                + ImgDbHelper.ImageTagEntry.COLUMN_NAME_TAGID + " = " + tagId;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        assert c != null;
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    fileId = c.getInt(c.getColumnIndex(ImgDbHelper.ImageTagEntry.COLUMN_NAME_FILEID));
                    filepath = this.findFilepath(fileId);

                    Image img = new Image();
                    img.init(fileId, filepath, tagId, tag, supplier);
                    imageContainer.addImage(img);

                } while (c.moveToNext());
            }
            c.close();
        }
        return imageContainer;
    }
    */

    public String dbFilePath()
    {
        File f = _context.getDatabasePath(_dbHelper.DATABASE_NAME);
        String pathStr = f.getAbsolutePath();
        return pathStr;
    }
}
