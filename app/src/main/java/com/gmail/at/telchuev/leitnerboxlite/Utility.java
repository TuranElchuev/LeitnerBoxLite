package com.gmail.at.telchuev.leitnerboxlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Utility {

    // DATA RETRIEVAL

    public static ArrayList<Entry> getLowestBoxVocabulary(){

        ArrayList<Entry> result = new ArrayList<>();

        SQLiteDatabase db = new DBHelper().getWritableDatabase();
        String query = "SELECT MIN(" + DBHelper.COL_BOX_NMB + ") AS " + DBHelper.COL_BOX_NMB + " FROM "
                + DBHelper.TABLE_NAME_MAIN;
        Cursor c = db.rawQuery(query, null);

        if(!c.moveToFirst()){
            return result;
        }

        String boxNmb = c.getString(c.getColumnIndex(DBHelper.COL_BOX_NMB));
        c.close();
        db.close();

        return getVocabulary(DBHelper.COL_BOX_NMB + " = " + boxNmb);
    }

    public static ArrayList<Entry> getVocabulary(String selection){

        ArrayList<Entry> result = new ArrayList<>();
        SQLiteDatabase db = new DBHelper().getWritableDatabase();
        String query = "SELECT * FROM "
                + DBHelper.TABLE_NAME_MAIN
                + (selection == null ? "" : " WHERE " + selection)
                + " ORDER BY "
                + DBHelper.COL_WORD + DBHelper.ASC;

        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                result.add(new Entry(c));
            }while (c.moveToNext());
        }
        c.close();
        db.close();

        return result;
    }


    // DATA MANIPULATION

    public static void insertData(ContentValues cv){
        SQLiteDatabase db = new DBHelper().getWritableDatabase();
        db.insert(DBHelper.TABLE_NAME_MAIN, DBHelper.COL_NULL, cv);
        db.close();
    }

    public static void updateData(ContentValues cv,
                           String selection,
                           String[] selectionArgs){

        SQLiteDatabase db = new DBHelper().getWritableDatabase();
        db.update(DBHelper.TABLE_NAME_MAIN, cv, selection, selectionArgs);
        db.close();
    }

    public static void deleteData(String selection, String[] selectionArgs){
        SQLiteDatabase db = new DBHelper().getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME_MAIN, selection, selectionArgs);
        db.close();
    }

    public static void deleteAll(){
        SQLiteDatabase db = new DBHelper().getWritableDatabase();
        db.delete(DBHelper.TABLE_NAME_MAIN, null, null);
        db.close();
    }

}
