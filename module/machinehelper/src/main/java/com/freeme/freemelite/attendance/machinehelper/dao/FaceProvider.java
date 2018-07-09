package com.freeme.freemelite.attendance.machinehelper.dao;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FaceProvider extends ContentProvider {
    private static final String TAG = "FaceProvider";
    private static final String FACE_DB_NAME = "face_db";
    private static final int FACE_DB_VERSION = 1;
    public static final String FACE_INFO_TABLE = "face_info_table";
    private static final String AUTHORITY = "com.freeme.freemelite.attendance";
    private FaceSqliteOpenHelper mFaceSqliteOpenHelper;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, FACE_INFO_TABLE, 0);
    }

    private SQLiteDatabase mDb;

    class FaceSqliteOpenHelper extends SQLiteOpenHelper {

        public FaceSqliteOpenHelper(Context context) {
            super(context, FACE_DB_NAME, null, FACE_DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists " + FACE_INFO_TABLE + " (" + FaceColumns._ID
                    + " integer primary key, " + FaceColumns.COLUMN_PHOTO + " binary, " + FaceColumns.COLUMN_USER_NAME + " text, "
                    + FaceColumns.COLUMN_FACE_FEATURE + " text, " + FaceColumns.COLUMN_RECENT_FLAG + " integer "
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + FACE_INFO_TABLE);
            onCreate(db);
        }
    }


    @Override
    public boolean onCreate() {
        mFaceSqliteOpenHelper = new FaceSqliteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int match = URI_MATCHER.match(uri);
        Cursor cursor = null;
        switch (match) {
            case 0:
                cursor = getDatabase().query(FACE_INFO_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = URI_MATCHER.match(uri);
        long insert = -1;
        switch (match) {
            case 0:
                insert = getDatabase().insert(FACE_INFO_TABLE, null, values);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, insert);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        int delete = -1;
        switch (match) {
            case 0:
                delete = getDatabase().delete(FACE_INFO_TABLE, selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return delete;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = URI_MATCHER.match(uri);
        int update = -1;
        switch (match) {
            case 0:
                update = getDatabase().update(FACE_INFO_TABLE, values, selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return update;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        getDatabase().close();
    }

    private SQLiteDatabase getDatabase() {
        if (mDb == null) {
            mDb = mFaceSqliteOpenHelper.getWritableDatabase();
        }
        return mDb;
    }
}
