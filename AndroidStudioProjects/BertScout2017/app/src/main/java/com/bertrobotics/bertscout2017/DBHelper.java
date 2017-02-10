package com.bertrobotics.bertscout2017;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bert_scout.db";
    private static final int DATABASE_VERSION = 10;

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(DBContract.TableStandInfo.SQL_QUERY_CREATE_TABLE);
        db.execSQL(DBContract.TablePitInfo.SQL_QUERY_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        if (oldVersion < 10) {
            db.execSQL(DBContract.TableStandInfo.SQL_QUERY_DELETE_TABLE);
            db.execSQL(DBContract.TablePitInfo.SQL_QUERY_DELETE_TABLE);
            onCreate(db);
            return;
        }
//        if (oldVersion < 4)
//        {
//            db.execSQL(DBContract.TablePitInfo.SQL_QUERY_UPGRADE_VER_4_START_LEFT);
//            db.execSQL(DBContract.TablePitInfo.SQL_QUERY_UPGRADE_VER_4_START_CENTER);
//            db.execSQL(DBContract.TablePitInfo.SQL_QUERY_UPGRADE_VER_4_START_RIGHT);
//            db.execSQL(DBContract.TablePitInfo.SQL_QUERY_UPGRADE_VER_4_HAS_AUTONOMOUS);
//            db.execSQL(DBContract.TablePitInfo.SQL_QUERY_UPGRADE_VER_4_PIT_COMMENT);
//        }
    }

    public JSONArray getDataAllStand(String pEvent) {

        JSONArray resultSet = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results;

        if (pEvent == "") {
            results = db.rawQuery(
                    "SELECT * FROM " + DBContract.TableStandInfo.TABLE_NAME_STAND, null);
        } else {
            results = db.rawQuery(
                    "SELECT * FROM " + DBContract.TableStandInfo.TABLE_NAME_STAND +
                            " WHERE " + DBContract.TableStandInfo.COLNAME_STAND_EVENT + " = '" + pEvent + "'" +
                            "", null);
        }
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int totalColumn = results.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (results.getColumnName(i) != null) {
                    try {
                        switch (results.getColumnName(i)) {
                            case DBContract.TableStandInfo._ID:
                            case DBContract.TableStandInfo.COLNAME_STAND_MATCH:
                            case DBContract.TableStandInfo.COLNAME_STAND_TEAM:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_SCORE_HIGH:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_SCORE_LOW:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_GEARS_RECEIVED:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_GEARS_PLACED:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_CLIMB_TIME:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_HUMAN_SPEED:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_PILOT_SPEED:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_PENALTIES:
                                rowObject.put(results.getColumnName(i), results.getInt(i));
                                break;
                            case DBContract.TableStandInfo.COLNAME_STAND_EVENT:
                            case DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE:
                            case DBContract.TableStandInfo.COLNAME_STAND_SCOUT_NAME:
                            case DBContract.TableStandInfo.COLNAME_STAND_COMMENT:
                                rowObject.put(results.getColumnName(i), results.getString(i));
                                break;
                            case DBContract.TableStandInfo.COLNAME_STAND_AUTO_BASE_LINE:
                            case DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_HIGH:
                            case DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_LOW:
                            case DBContract.TableStandInfo.COLNAME_STAND_AUTO_PLACE_GEAR:
                            case DBContract.TableStandInfo.COLNAME_STAND_AUTO_OPEN_HOPPER:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_CLIMBED:
                            case DBContract.TableStandInfo.COLNAME_STAND_TELE_TOUCHPAD:
                                if (results.getInt(i) == 0) {
                                    rowObject.put(results.getColumnName(i), false);
                                } else {
                                    rowObject.put(results.getColumnName(i), true);
                                }
                                break;
                        }
                    } catch (JSONException e) {
                        return null;
                    }
                }
            }
            resultSet.put(rowObject);
            results.moveToNext();
        }

        results.close();
        return resultSet;
    }

    public boolean updateStandInfo(JSONObject standInfo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {

            SetStringValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_EVENT);
            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_MATCH);
            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TEAM);
            SetStringValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_ALLIANCE);
            SetStringValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_SCOUT_NAME);

            SetBooleanValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_AUTO_BASE_LINE);
            SetBooleanValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_HIGH);
            SetBooleanValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_AUTO_SCORE_LOW);
            SetBooleanValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_AUTO_PLACE_GEAR);
            SetBooleanValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_AUTO_OPEN_HOPPER);

            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_SCORE_HIGH);
            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_SCORE_LOW);
            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_GEARS_RECEIVED);
            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_GEARS_PLACED);
            SetBooleanValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_CLIMBED);
            SetBooleanValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_TOUCHPAD);
            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_CLIMB_TIME);
            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_HUMAN_SPEED);
            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_PILOT_SPEED);
            SetIntegerValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_TELE_PENALTIES);

            SetStringValue(standInfo, contentValues, DBContract.TableStandInfo.COLNAME_STAND_COMMENT);

            if (standInfo.has(DBContract.TableStandInfo._ID)) {

                db.update(
                        DBContract.TableStandInfo.TABLE_NAME_STAND,
                        contentValues,
                        "_id = ?",
                        new String[]{String.valueOf(standInfo.getInt(DBContract.TableStandInfo._ID))}
                );
                return true;

            } else {

                long newID = db.insert(
                        DBContract.TableStandInfo.TABLE_NAME_STAND,
                        null,
                        contentValues
                );
                if (newID > 0) {
                    standInfo.put(DBContract.TableStandInfo._ID, newID);
                    return true;
                }

            }

        } catch (JSONException e) {
            return false;
        }

        return true;

    }

//    public JSONArray getTeamData(String pEvent, Integer pTeam){
//        JSONArray resultSet = new JSONArray();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor results =  db.rawQuery("SELECT * FROM stand_scouting WHERE event = '" + pEvent + "' AND team = " + pTeam, null);
//        results.moveToFirst();
//
//        while (!results.isAfterLast()) {
//            int totalColumn = results.getColumnCount();
//            JSONObject rowObject = new JSONObject();
//
//            for(int i = 0; i < totalColumn; i++) {
//                if(results.getColumnName(i) != null) {
//                    try {
//                        if(results.getString(i) != null) {
//                            rowObject.put(results.getColumnName(i) ,  results.getString(i) );
//                        } else {
//                            rowObject.put(results.getColumnName(i) ,  "" );
//                        }
//                    } catch( Exception e ) {
//
//                    }
//                }
//            }
//
//            resultSet.put(rowObject);
//            results.moveToNext();
//        }
//
//        results.close();
//        return resultSet;
//    }
//
//
//    public JSONArray getMatchData(String pEvent, Integer match_no, Integer team){
//        JSONArray resultSet = new JSONArray();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor results =  db.rawQuery("SELECT * FROM stand_scouting WHERE event = '" + pEvent +
//                                "' AND match_no = " + match_no + " AND team = " + team, null);
//        results.moveToFirst();
//
//        int totalColumn = results.getColumnCount();
//        JSONObject rowObject = new JSONObject();
//
//        for(int i = 0; i < totalColumn; i++) {
//            if(results.getColumnName(i) != null) {
//                try {
//                    if(results.getString(i) != null) {
//                        rowObject.put(results.getColumnName(i) ,  results.getString(i) );
//                    } else {
//                        rowObject.put(results.getColumnName(i) ,  "" );
//                    }
//                } catch( Exception e ) {
//
//                }
//            }
//        }
//
//        rowObject.length();
//
//        if (rowObject.length() != 0) {
//            resultSet.put(rowObject);
//        }
//
//        results.close();
//        return resultSet;
//    }

    public Integer deleteStandScouting() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        try {
            db.execSQL(DBContract.TableStandInfo.SQL_QUERY_DELETE_TABLE);
            try {
                db.execSQL(DBContract.TableStandInfo.SQL_QUERY_CREATE_TABLE);
                result = 0;
            } catch (Exception e) {
                result = 2; // error during create
            }
        } catch (Exception e) {
            result = 1; // error during delete
        }
        return result;
    }

//    public int numberOfRows(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        int numRows = (int) DatabaseUtils.queryNumEntries(db, DBContract.TableStandInfo.TABLE_NAME_STAND);
//        return numRows;
//    }

    //
    // Pit Scouting
    //

    public JSONArray getDataAllPit(String pEvent) {

        JSONArray resultSet = new JSONArray();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor results;

        if (pEvent == "") {
            results = db.rawQuery(
                    "SELECT * FROM " + DBContract.TablePitInfo.TABLE_NAME_PIT, null);
        } else {
            results = db.rawQuery(
                    "SELECT * FROM " + DBContract.TablePitInfo.TABLE_NAME_PIT +
                            " WHERE " + DBContract.TablePitInfo.COLNAME_PIT_EVENT + " = '" + pEvent + "'" +
                            "", null);
        }
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int totalColumn = results.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (results.getColumnName(i) != null) {
                    try {
                        switch (results.getColumnName(i)) {
                            case DBContract.TablePitInfo._ID:
                            case DBContract.TablePitInfo.COLNAME_PIT_TEAM:
                            case DBContract.TablePitInfo.COLNAME_PIT_TEAM_YEARS:
                            case DBContract.TablePitInfo.COLNAME_PIT_TEAM_MEMBERS:
                            case DBContract.TablePitInfo.COLNAME_PIT_HEIGHT:
                            case DBContract.TablePitInfo.COLNAME_PIT_WEIGHT:
                            case DBContract.TablePitInfo.COLNAME_PIT_NUM_CIMS:
                            case DBContract.TablePitInfo.COLNAME_PIT_MAX_SPEED:
                            case DBContract.TablePitInfo.COLNAME_PIT_MAX_FUEL:
                            case DBContract.TablePitInfo.COLNAME_PIT_SHOOT_SPEED:
                            case DBContract.TablePitInfo.COLNAME_PIT_AUTO_NUM_MODES:
                            case DBContract.TablePitInfo.COLNAME_PIT_TEAM_RATING:
                                rowObject.put(results.getColumnName(i), results.getInt(i));
                                break;
                            case DBContract.TablePitInfo.COLNAME_PIT_EVENT:
                            case DBContract.TablePitInfo.COLNAME_PIT_SCOUT_NAME:
                            case DBContract.TablePitInfo.COLNAME_PIT_WHEEL_TYPE:
                            case DBContract.TablePitInfo.COLNAME_PIT_WHEEL_LAYOUT:
                            case DBContract.TablePitInfo.COLNAME_PIT_SHOOT_LOCATION:
                            case DBContract.TablePitInfo.COLNAME_PIT_COMMENT:
                                rowObject.put(results.getColumnName(i), results.getString(i));
                                break;
                            case DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_HIGH:
                            case DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_LOW:
                            case DBContract.TablePitInfo.COLNAME_PIT_FLOOR_PICKUP:
                            case DBContract.TablePitInfo.COLNAME_PIT_TOP_LOADER:
                            case DBContract.TablePitInfo.COLNAME_PIT_AUTO_AIM:
                            case DBContract.TablePitInfo.COLNAME_PIT_CAN_CARRY_GEAR:
                            case DBContract.TablePitInfo.COLNAME_PIT_CAN_CLIMB:
                            case DBContract.TablePitInfo.COLNAME_PIT_OWN_ROPE:
                            case DBContract.TablePitInfo.COLNAME_PIT_START_LEFT:
                            case DBContract.TablePitInfo.COLNAME_PIT_START_CENTER:
                            case DBContract.TablePitInfo.COLNAME_PIT_START_RIGHT:
                            case DBContract.TablePitInfo.COLNAME_PIT_AUTO_BASE_LINE:
                            case DBContract.TablePitInfo.COLNAME_PIT_AUTO_PLACE_GEAR:
                            case DBContract.TablePitInfo.COLNAME_PIT_AUTO_HIGH_GOAL:
                            case DBContract.TablePitInfo.COLNAME_PIT_AUTO_LOW_GOAL:
                            case DBContract.TablePitInfo.COLNAME_PIT_AUTO_HOPPER:
                                if (results.getInt(i) == 0) {
                                    rowObject.put(results.getColumnName(i), false);
                                } else {
                                    rowObject.put(results.getColumnName(i), true);
                                }
                                break;
                        }
                    } catch (JSONException e) {
                        return null;
                    }
                }
            }
            resultSet.put(rowObject);
            results.moveToNext();
        }

        results.close();
        return resultSet;
    }

    public boolean updatePitInfo(JSONObject pitInfo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        try {

            SetStringValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_EVENT);
            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_TEAM);
            SetStringValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_SCOUT_NAME);

            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_TEAM_YEARS);
            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_TEAM_MEMBERS);

            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_HEIGHT);
            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_WEIGHT);
            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_NUM_CIMS);
            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_MAX_SPEED);

            SetStringValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_WHEEL_TYPE);
            SetStringValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_WHEEL_LAYOUT);

            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_MAX_FUEL);
            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_SHOOT_SPEED);
            SetStringValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_SHOOT_LOCATION);

            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_HIGH);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_CAN_SHOOT_LOW);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_FLOOR_PICKUP);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_TOP_LOADER);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_AUTO_AIM);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_CAN_CARRY_GEAR);

            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_CAN_CLIMB);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_OWN_ROPE);

            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_START_LEFT);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_START_CENTER);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_START_RIGHT);

            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_AUTO_NUM_MODES);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_AUTO_BASE_LINE);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_AUTO_PLACE_GEAR);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_AUTO_HIGH_GOAL);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_AUTO_LOW_GOAL);
            SetBooleanValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_AUTO_HOPPER);

            SetIntegerValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_TEAM_RATING);

            SetStringValue(pitInfo, contentValues, DBContract.TablePitInfo.COLNAME_PIT_COMMENT);

            if (pitInfo.has(DBContract.TablePitInfo._ID)) {

                db.update(
                        DBContract.TablePitInfo.TABLE_NAME_PIT,
                        contentValues,
                        "_id = ?",
                        new String[]{String.valueOf(pitInfo.getInt(DBContract.TablePitInfo._ID))}
                );
                return true;

            } else {

                long newID = db.insert(
                        DBContract.TablePitInfo.TABLE_NAME_PIT,
                        null,
                        contentValues
                );
                if (newID > 0) {
                    pitInfo.put(DBContract.TablePitInfo._ID, newID);
                    return true;
                }

            }

        } catch (JSONException e) {
            return false;
        }

        return true;

    }

    public Integer deletePitInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        try {
            db.execSQL(DBContract.TablePitInfo.SQL_QUERY_DELETE_TABLE);
            try {
                db.execSQL(DBContract.TablePitInfo.SQL_QUERY_CREATE_TABLE);
                result = 0;
            } catch (Exception e) {
                result = 2; // error during create
            }
        } catch (Exception e) {
            result = 1; // error during delete
        }
        return result;
    }

    public void SetBooleanValue(JSONObject obj, ContentValues contentValues, String fieldName) {
        try {
            if (obj.getBoolean(fieldName)) {
                contentValues.put(fieldName, 1);
            } else {
                contentValues.put(fieldName, 0);
            }
        } catch (JSONException e) {
        }
    }

    public void SetIntegerValue(JSONObject obj, ContentValues contentValues, String fieldName) {
        try {
            contentValues.put(fieldName, obj.getInt(fieldName));
        } catch (JSONException e) {
        }
    }

    public void SetStringValue(JSONObject obj, ContentValues contentValues, String fieldName) {
        try {
            contentValues.put(fieldName, obj.getString(fieldName));
        } catch (JSONException e) {
        }
    }
}