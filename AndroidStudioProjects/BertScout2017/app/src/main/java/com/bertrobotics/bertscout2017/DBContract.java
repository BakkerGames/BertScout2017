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

        public static final String COLUMN_NAME_START_LEFT = "start_left";
        public static final String COLUMN_NAME_START_CENTER = "start_center";
        public static final String COLUMN_NAME_START_RIGHT = "start_right";
        public static final String COLUMN_NAME_HAS_AUTONOMOUS = "has_autonomous";
        public static final String COLUMN_NAME_PIT_COMMENT = "pit_comment";

        public static final String SQL_QUERY_CREATE_TABLE =
                "CREATE TABLE " + DBContract.TablePitInfo.TABLE_NAME + " (" +
                        DBContract.TablePitInfo._ID + " INTEGER PRIMARY KEY" +
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_EVENT + " TEXT" +
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_TEAM + " INTEGER" +
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_LOW + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_CAN_SCORE_HIGH + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_CAN_BLOCK + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_CAN_CLIMB + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_START_LEFT + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_START_CENTER + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_START_RIGHT + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_HAS_AUTONOMOUS + " INTEGER" + // boolean, 0=false, 1=true
                        ", " + DBContract.TablePitInfo.COLUMN_NAME_PIT_COMMENT + " TEXT" +
                        ");";

        public static final String SQL_QUERY_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

        public static final String SQL_QUERY_UPGRADE_VER_4_START_LEFT =
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + DBContract.TablePitInfo.COLUMN_NAME_START_LEFT + " INTEGER;";
        public static final String SQL_QUERY_UPGRADE_VER_4_START_CENTER =
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + DBContract.TablePitInfo.COLUMN_NAME_START_CENTER + " INTEGER;";
        public static final String SQL_QUERY_UPGRADE_VER_4_START_RIGHT =
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + DBContract.TablePitInfo.COLUMN_NAME_START_RIGHT + " INTEGER;";
        public static final String SQL_QUERY_UPGRADE_VER_4_HAS_AUTONOMOUS =
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + DBContract.TablePitInfo.COLUMN_NAME_HAS_AUTONOMOUS + " INTEGER;";
        public static final String SQL_QUERY_UPGRADE_VER_4_PIT_COMMENT =
                "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + DBContract.TablePitInfo.COLUMN_NAME_PIT_COMMENT + " TEXT;";
    }

}
