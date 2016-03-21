package vh1981.com.funnyphotosstorage;

/**
 * Created by vh1981 on 14. 12. 18.
 */

import android.content.Context;
import android.database.sqlite.*;
import android.os.Bundle;
import android.provider.BaseColumns;

public class ImgDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "imagewithtags.db";

    public ImgDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final class ImagesEntry implements BaseColumns {
        public static final String TABLE_NAME = "images";
        public static final String COLUMN_NAME_FILENAME = "filename"; //이미지 파일 이름
    }

    public static final class ImageTagEntry implements  BaseColumns {
        public static final String TABLE_NAME = "imagetag";
        public static final String COLUMN_NAME_FILEID = "fileid"; //이미지 파일 이름
        public static final String COLUMN_NAME_TAGID = "tagid"; //이미지 파일 이름
    }

    public static final class TagsEntry implements BaseColumns {
        public static final String TABLE_NAME = "tags";
        public static final String COLUMN_NAME_TAGNAME = "tagname"; //태그 이름
    }

    public static final class DeletedFilesEntry implements BaseColumns {
        public static final String TABLE_NAME = "deletedFiles";
        public static final String COLUMN_NAME_FILENAME= "filename"; //파일명
    }

    public static final class ModifiedFilesEntry implements BaseColumns {
        public static final String TABLE_NAME = "modifiedFiles";
        public static final String COLUMN_NAME_FILENAME= "filename"; //파일명
    }

    // sql create string :
    private static final String SQL_CREATE_IMAGES = "CREATE TABLE "
            + ImagesEntry.TABLE_NAME + " ("
            + ImagesEntry._ID + " INTEGER PRIMARY KEY,"
            + ImagesEntry.COLUMN_NAME_FILENAME + " TEXT " + " NOT NULL UNIQUE " + " )";

    private static final String SQL_CREATE_IMAGETAG = "CREATE TABLE "
            + ImageTagEntry.TABLE_NAME + " ("
            + ImageTagEntry._ID + " INTEGER PRIMARY KEY,"
            + ImageTagEntry.COLUMN_NAME_FILEID + " INTEGER,"
            + ImageTagEntry.COLUMN_NAME_TAGID + " INTEGER" + " )";

    private static final String SQL_CREATE_TAGS = "CREATE TABLE "
            + TagsEntry.TABLE_NAME + " ("
            + TagsEntry._ID + " INTEGER PRIMARY KEY,"
            + TagsEntry.COLUMN_NAME_TAGNAME + " TEXT " + " NOT NULL UNIQUE " + " )";

    private static final String SQL_CREATE_DELETEDFILES = "CREATE TABLE "
            + DeletedFilesEntry.TABLE_NAME + " ("
            + DeletedFilesEntry._ID + " INTEGER PRIMARY KEY,"
            + DeletedFilesEntry.COLUMN_NAME_FILENAME + " TEXT " + " NOT NULL UNIQUE " + " )";

    private static final String SQL_CREATE_MODIFIEDFILES = "CREATE TABLE "
            + ModifiedFilesEntry.TABLE_NAME + " ("
            + ModifiedFilesEntry._ID + " INTEGER PRIMARY KEY,"
            + ModifiedFilesEntry.COLUMN_NAME_FILENAME + " TEXT " + " NOT NULL UNIQUE " + " )";

    // sql delete string:
    private static final String SQL_DELETE_IMAGES = "DROP TABLE IF EXISTS "
            + ImagesEntry.TABLE_NAME;
    private static final String SQL_DELETE_IMAGETAG = "DROP TABLE IF EXISTS "
            + ImageTagEntry.TABLE_NAME;
    private static final String SQL_DELETE_TAGS = "DROP TABLE IF EXISTS "
            + TagsEntry.TABLE_NAME;
    private static final String SQL_DELETE_DELETEDFILES = "DROP TABLE IF EXISTS "
            + DeletedFilesEntry.TABLE_NAME;
    private static final String SQL_DELETE_MODIFIEDFILES = "DROP TABLE IF EXISTS "
            + ModifiedFilesEntry.TABLE_NAME;

    /// override functions:
    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
        arg0.execSQL(SQL_CREATE_IMAGES);
        arg0.execSQL(SQL_CREATE_IMAGETAG);
        arg0.execSQL(SQL_CREATE_TAGS);
        arg0.execSQL(SQL_CREATE_DELETEDFILES);
        arg0.execSQL(SQL_CREATE_MODIFIEDFILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        arg0.execSQL(SQL_DELETE_IMAGES);
        arg0.execSQL(SQL_DELETE_IMAGETAG);
        arg0.execSQL(SQL_DELETE_TAGS);
        arg0.execSQL(SQL_DELETE_DELETEDFILES);
        arg0.execSQL(SQL_DELETE_MODIFIEDFILES);
        onCreate(arg0);
    }
}
