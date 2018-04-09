package com.gmail.at.telchuev.leitnerboxlite;

import android.app.backup.BackupHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Utility {

    // DATA RETRIEVAL

    public static ArrayList<String> getNonEmptyBoxes(){
        ArrayList<String> result = new ArrayList<>();

        SQLiteDatabase db = new DBHelper().getWritableDatabase();

        String query = "SELECT DISTINCT " + DBHelper.COL_BOX_NMB
                + " FROM " + DBHelper.TABLE_NAME_MAIN
                + " ORDER BY " + DBHelper.COL_BOX_NMB + DBHelper.ASC;
        Cursor c = db.rawQuery(query, null);

        if(c.moveToFirst()){
            do{
                result.add(c.getString(c.getColumnIndex(DBHelper.COL_BOX_NMB)));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return result;
    }

    public static ArrayList<Entry> getBoxVocabulary(String boxNmb){
        return getVocabulary(DBHelper.COL_BOX_NMB + " = " + boxNmb);
    }

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

    public static void importFile(){

        //READ WORDS FROM FILE
        BufferedReader reader = null;
        boolean isWord = true;
        ArrayList<Word> newWords = new ArrayList<>();
        try {
            reader = new BufferedReader(new InputStreamReader(MyApp.getAppContext().getAssets().open("Goethe B1.txt")));

            String line;
            Word w = null;
            String s = "";
            while ((line = reader.readLine()) != null) {
                if(line.isEmpty()) {
                    s = s.substring(0, s.length()-1);
                    if(isWord) {
                        w = new Word();
                        newWords.add(w);
                        w.word = s;
                    }else {
                        w.example = s;
                    }
                    s = "";
                    isWord = !isWord;
                }else {
                    s = s + line + "\n";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // GET EXISTING WORDS
        ArrayList<String> existingWords = new ArrayList<>();
        SQLiteDatabase db = new DBHelper().getWritableDatabase();
        String query = "SELECT " + DBHelper.COL_WORD + " FROM " + DBHelper.TABLE_NAME_MAIN;
        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                existingWords.add(c.getString(c.getColumnIndex(DBHelper.COL_WORD)));
            }while (c.moveToNext());
        }
        c.close();
        db.close();

        // ADD TO DB NON-EXISTING WORDS
        for(Word w: newWords){
            if(!existingWords.contains(w.word)){
                Entry e = new Entry();
                e.setWord(w.word);
                e.setExample(w.example);
                e.toDB();
                Log.d(MyApp.TAG, "Added: " + e.getWord());
            }
        }

    }

    public static class Word {
        public String word;
        public String example;
    }

}
