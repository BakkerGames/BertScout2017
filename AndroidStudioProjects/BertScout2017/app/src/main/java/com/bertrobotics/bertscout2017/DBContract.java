package com.bertrobotics.bertscout2017;

import android.provider.BaseColumns;

/**
 * Created by chime on 11/13/2016.
 */

public final class DBContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DBContract() {}

    /* Inner class that defines the table contents */
    public static class TablePitInfo implements BaseColumns {

        public static final String TABLE_NAME = "pit_info";

        public static final String COLUMN_NAME_EVENT = "event";
        public static final String COLUMN_NAME_TEAM = "team";
        public static final String COLUMN_NAME_CAN_SCORE_LOW = "can_score_low";
        public static final String COLUMN_NAME_CAN_SCORE_HIGH = "can_score_high";
        public static final String COLUMN_NAME_CAN_BLOCK = "can_block";
        public static final String COLUMN_NAME_CAN_CLIMB = "can_climb";

        public static final String SQL_QUERY_CREATE_TABLE =
                "CREATE TABLE " + DBContract.TablePitInfo.TABLE_NAME + " (" +
                        DBContract.TablePitInfo._ID + " INTEGER PRIMARY KEY" +
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_EVENT + " TEXT" +
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_TEAM + " INTEGER" +
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_LOW + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_HIGH + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_CAN_CLIMB + " INTEGER" + // boolean, 0=false, 1=true
                        ");";

        public static final String SQL_QUERY_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }

}
