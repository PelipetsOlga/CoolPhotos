package com.coolphotos.coolphotos.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;


public class PictureProvider extends ContentProvider {


    private static final String DB_NAME = "cool_photos";
    private static final int DB_VERSION = 1;

    public static final String PICTURE_TABLE = "picture";

    public static final String PICTURE_ID = "_id";
    public static final String PICTURE_FILE = "file_name";
    public static final String PICTURE_FILE_PREVIEW = "file_preview_name";
    public static final String PICTURE_IS_PLAY = "is_play";
    public static final String PICTURE_DATE = "date";

    public static final int PLAY = 1;
    public static final int PlAY_NOT = 0;

    private static final String CREATE_TABLE_PICTURE = "create table " + PICTURE_TABLE + "("
            + PICTURE_ID + " integer primary key autoincrement, "
            + PICTURE_FILE + " text, "
            + PICTURE_FILE_PREVIEW + " text, "
            + PICTURE_IS_PLAY + " integer, "
            + PICTURE_DATE + " integer"
            + ");";

    private static final String AUTHORITY = "com.coolphotos.coolphotos.provider.CoolPhotos";
    private static final String PICTURE_PATH = "pictures";
    public static final Uri PICTURE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PICTURE_PATH);
    private static final String PICTURE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + PICTURE_PATH;
    private static final String PICTURE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + PICTURE_PATH;

    private static final int URI_PICTURES = 1;
    private static final int URI_PICTURES_ID = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PICTURE_PATH, URI_PICTURES);
        uriMatcher.addURI(AUTHORITY, PICTURE_PATH + "/#", URI_PICTURES_ID);
    }

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case URI_PICTURES:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = PICTURE_DATE + " DESC";
                }
                cursor = db.query(PICTURE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), PICTURE_CONTENT_URI);
                break;
            case URI_PICTURES_ID:
                String pictureId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = PICTURE_ID + " = " + pictureId;
                } else {
                    selection = selection + " AND " + PICTURE_ID + " = " + pictureId;
                }
                cursor = db.query(PICTURE_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(), PICTURE_CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_PICTURES)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        Uri resultUri = null;
        long rowID = 0;
        switch (uriMatcher.match(uri)) {
            case URI_PICTURES:
                rowID = db.insert(PICTURE_TABLE, null, values);
                resultUri = ContentUris.withAppendedId(PICTURE_CONTENT_URI, rowID);
                break;
        }
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int cnt = 0;
        switch (uriMatcher.match(uri)) {
            case URI_PICTURES:
                cnt = db.delete(PICTURE_TABLE, selection, selectionArgs);
                break;
            case URI_PICTURES_ID:
                String pictureId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = PICTURE_ID + " = " + pictureId;
                } else {
                    selection = selection + " AND " + PICTURE_ID + " = " + pictureId;
                }
                cnt = db.delete(PICTURE_TABLE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int cnt = 0;
        switch (uriMatcher.match(uri)) {
            case URI_PICTURES:
                cnt = db.update(PICTURE_TABLE, values, selection, selectionArgs);
                break;
            case URI_PICTURES_ID:
                String pictureId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = PICTURE_ID + " = " + pictureId;
                } else {
                    selection = selection + " AND " + PICTURE_ID + " = " + pictureId;
                }
                cnt = db.update(PICTURE_TABLE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_PICTURES:
                return PICTURE_CONTENT_TYPE;
            case URI_PICTURES_ID:
                return PICTURE_CONTENT_ITEM_TYPE;
        }
        return null;
    }


    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_PICTURE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + PICTURE_TABLE);
            onCreate(db);
        }
    }
}
