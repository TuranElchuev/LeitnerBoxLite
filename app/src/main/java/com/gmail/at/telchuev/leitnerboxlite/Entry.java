package com.gmail.at.telchuev.leitnerboxlite;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class Entry {

    public static final String CATEGORY_DEFAULT = "Default";

    public static final int PRIORITY_HIGHEST = 0,
            BOX_NMB_MIN = 0,
            BOX_NMB_MAX = 5,
            INVALID_ID = -1;

    private String category = CATEGORY_DEFAULT,
            word,
            hint,
            example,
            exampleHint;

    private int id,
            boxNumber,
            priority;

    long lastVisited,
        created;

    private boolean showHint = false;

    public Entry(){
        this.setId(INVALID_ID);
        this.setBoxNumber(BOX_NMB_MIN);
        this.setPriority(PRIORITY_HIGHEST);
        this.setCreated(0);
        this.setLastVisited(0);
    }

    public Entry(Cursor c){
        init(c);
    }

    private void init(Cursor c){
        this.setCategory(c.getString(c.getColumnIndex(DBHelper.COL_CATEGORY)));
        this.setWord(c.getString(c.getColumnIndex(DBHelper.COL_WORD)));
        this.setHint(c.getString(c.getColumnIndex(DBHelper.COL_HINT)));
        this.setExample(c.getString(c.getColumnIndex(DBHelper.COL_EXAMPLE)));
        this.setExampleHint(c.getString(c.getColumnIndex(DBHelper.COL_EXAMPLE_HINT)));

        this.setLastVisited(c.getLong(c.getColumnIndex(DBHelper.COL_LAST_VISITED)));
        this.setCreated(c.getLong(c.getColumnIndex(DBHelper.COL_CREATED)));

        this.setPriority(c.getInt(c.getColumnIndex(DBHelper.COL_PRIORITY)));
        this.setBoxNumber(c.getInt(c.getColumnIndex(DBHelper.COL_BOX_NMB)));
        this.setId(c.getInt(c.getColumnIndex(DBHelper.COL_ID)));
    }

    private void init(Entry e){
        this.setCategory(e.getCategory());
        this.setWord(e.getWord());
        this.setHint(e.getHint());
        this.setExample(e.getExample());
        this.setExampleHint(e.getExampleHint());
        this.setLastVisited(e.getLastVisited());
        this.setCreated(e.getCreated());
        this.setPriority(e.getPriority());
        this.setBoxNumber(e.getBoxNumber());
        this.setId(e.getId());
    }

    public void init(int id){
        setId(id);
        refresh();
    }

    // SETTERS


    public void setShowHint(boolean showHint) {
        this.showHint = showHint;
    }

    private void setId(int id) {
        this.id = id;
    }

    private void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    private void setCreated(long created) {
        this.created = created;
    }

    private void setLastVisited(long lastVisited) {
        this.lastVisited = lastVisited;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public void setExampleHint(String exampleHint) {
        this.exampleHint = exampleHint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setPriority(int priority) {
        this.priority = Math.max(priority, PRIORITY_HIGHEST); // PRIORITY_HIGHEST = lowest number
    }

    public void setWord(String word) {
        this.word = word;
    }


    // GETTERS


    public boolean isShowHint() {
        return showHint;
    }

    public int getId() {
        return id;
    }

    public int getBoxNumber() {
        return boxNumber;
    }

    public int getPriority() {
        return priority;
    }

    public long getLastVisited() {
        return lastVisited;
    }

    public long getCreated() {
        return created;
    }

    public String getCategory() {
        return category;
    }

    public String getExample() {
        return example;
    }

    public String getExampleHint() {
        return exampleHint;
    }

    public String getHint() {
        return hint;
    }

    public String getWord() {
        return word;
    }

    // STATE

    public void know(){
        this.setBoxNumber(Math.min(BOX_NMB_MAX, getBoxNumber() + 1));
        this.toDB();
    }

    public void repeat(){
        this.setBoxNumber(BOX_NMB_MIN);
        this.toDB();
    }

    // DATABASE MANIPULATIONS

    public void toDB(){
        this.setLastVisited(System.currentTimeMillis());
        if(this.isNew()){
            this.setCreated(System.currentTimeMillis());
            Utility.insertData(getContentValues());
        }else {
            Utility.updateData(getContentValues(),
                    DBHelper.COL_ID + " = ?",
                    new String[]{"" + getId()});
        }
    }

    // HELPERS

    private ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COL_BOX_NMB, getBoxNumber());
        cv.put(DBHelper.COL_CATEGORY, getCategory());
        cv.put(DBHelper.COL_WORD, getWord());
        cv.put(DBHelper.COL_HINT, getHint());
        cv.put(DBHelper.COL_EXAMPLE, getExample());
        cv.put(DBHelper.COL_EXAMPLE_HINT, getExampleHint());
        cv.put(DBHelper.COL_PRIORITY, getPriority());
        cv.put(DBHelper.COL_LAST_VISITED, getLastVisited());
        cv.put(DBHelper.COL_CREATED, getCreated());
        return cv;
    }

    public boolean isNew(){
        return getId() == INVALID_ID;
    }

    public void refresh(){
        if(isNew()) return;

        ArrayList<Entry> vocab = Utility.getVocabulary(DBHelper.COL_ID + " = " + getId());
        if(vocab.isEmpty()) return;

        this.init(vocab.get(0));
    }
}
