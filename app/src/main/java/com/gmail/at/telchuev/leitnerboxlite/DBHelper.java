package com.gmail.at.telchuev.leitnerboxlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private final String TEXT = " TEXT";
    private final String INTEGER = " INTEGER";
    private final String COMMA = ", ";

    public static final String DESC = " DESC";
    public static final String ASC = " ASC";

    public static final String TABLE_NAME_MAIN = "table_main";

    public static final String COL_NULL = "col_null";
    public static final String COL_ID = "id";

    public static final String COL_WORD = "col_word";
    public static final String COL_HINT = "col_hint";
    public static final String COL_EXAMPLE = "col_example";
    public static final String COL_EXAMPLE_HINT = "col_example_hint";
    public static final String COL_BOX_NMB = "col_box_nmb";
    public static final String COL_LAST_VISITED = "col_last_visited";
    public static final String COL_CREATED = "col_created";
    public static final String COL_PRIORITY = "col_priority";
    public static final String COL_CATEGORY = "col_category";

    private final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME_MAIN + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
                    COL_CATEGORY + TEXT + COMMA +
                    COL_WORD + TEXT + COMMA +
                    COL_HINT + TEXT + COMMA +
                    COL_EXAMPLE + TEXT + COMMA +
                    COL_EXAMPLE_HINT + TEXT + COMMA +
                    COL_PRIORITY + INTEGER + COMMA +
                    COL_BOX_NMB + INTEGER + COMMA +
                    COL_LAST_VISITED + INTEGER + COMMA +
                    COL_CREATED + INTEGER +
                    " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME_MAIN;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "com.gmail.at.telchuev.leitnerboxlite.database";

    public DBHelper() {
        super(MyApp.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}