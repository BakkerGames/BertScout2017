package com.bertrobotics.bertscout2017;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bert_scout.db";
    public static final String STAND_SCOUTING_TABLE_NAME = "stand_scouting";

    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE stand_scouting " +
                        "(event text, match_no text, team text, " +
                        "auto_high integer, auto_low integer, auto_cross integer, tele_high integer, " +
                        "tele_low integer, tele_cross integer, endgame integer, " +
                        "PRIMARY KEY (event, match_no, team))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS stand_scouting");
        onCreate(db);
    }

    public JSONArray getData(){
        JSONArray resultSet = new JSONArray();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results =  db.rawQuery("select * from stand_scouting", null);
        results.moveToFirst();

        while (results.isAfterLast() == false) {
            int totalColumn = results.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for(int i = 0; i < totalColumn; i++) {
                if(results.getColumnName(i) != null) {
                    try {
                        if(results.getString(i) != null) {
                            rowObject.put(results.getColumnName(i) ,  results.getString(i) );
                        } else {
                            rowObject.put( results.getColumnName(i) ,  "" );
                        }
                    } catch( Exception e ) {

                    }
                }
            }

            resultSet.put(rowObject);
            results.moveToNext();
        }

        results.close();
        return resultSet;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, STAND_SCOUTING_TABLE_NAME);
        return numRows;
    }

    public boolean insertStandScouting (String event, String match_no, String team, Integer auto_high,
                                        Integer auto_low, Integer auto_cross, Integer tele_high,
                                        Integer tele_low, Integer tele_cross, Integer endgame)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("event", event);
        contentValues.put("match_no", match_no);
        contentValues.put("team", team);
        contentValues.put("auto_high", auto_high);
        contentValues.put("auto_low", auto_low);
        contentValues.put("auto_cross", auto_cross);
        contentValues.put("tele_high", tele_high);
        contentValues.put("tele_low", tele_low);
        contentValues.put("tele_cross", tele_cross);
        contentValues.put("endgame", endgame);
        db.insert("stand_scouting", null, contentValues);
        return true;
    }

    public boolean updateStandScouting (String event, String match_no, String team, Integer auto_high,
                                        Integer auto_low, Integer auto_cross, Integer tele_high,
                                        Integer tele_low, Integer tele_cross, Integer endgame)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("event", event);
        contentValues.put("match_no", match_no);
        contentValues.put("team", team);
        contentValues.put("auto_high", auto_high);
        contentValues.put("auto_low", auto_low);
        contentValues.put("auto_cross", auto_cross);
        contentValues.put("tele_high", tele_high);
        contentValues.put("tele_low", tele_low);
        contentValues.put("tele_cross", tele_cross);
        contentValues.put("endgame", endgame);
        db.update("stand_scouting", contentValues, "event = ? and match_no = ? and team = ?", new String[] {event, match_no, team});
        return true;
    }

    public Integer deleteStandScouting (String event, String match_no, String team)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("stand_scouting",
                "event = ? and match_no = ? and team = ?", new String[] {event, match_no, team});
    }
}