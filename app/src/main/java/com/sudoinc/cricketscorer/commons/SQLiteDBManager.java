package com.sudoinc.cricketscorer.commons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLiteDBManager extends SQLiteOpenHelper
{
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    public ContentValues values = new ContentValues();

    public static final String DATABASE_NAME="CricketScorerAdvancedDB.db";
    public static final int DATABASE_VERSION=2;

    //Table Names
    public static final String T001_TEAM="T001_TEAM";
    public static final String T002_PLAYERS="T002_PLAYERS";
    public static final String T003_MATCHES="T003_MATCHES";
    public static final String T004_TOURNAMENT="T004_TOURNAMENT";
    public static final String T005_OVER_BY_BALL_STATS="T005_OVER_BY_BALL_STATS";
    public static final String T006_WICKETS="T006_WICKETS";
    public static final String T301_PLAYER_BOWLER="T301_PLAYER_BOWLER";
    public static final String T302_PLAYER_BATSMEN="T302_PLAYER_BATSMEN";
    public static final String T303_PLAYER_WK="T303_PLAYER_WK";
    public static final String T304_PLAYER_FIELDER="T304_PLAYER_FIELDER";
//    public static final String T995_MATCH_TYPE="T995_MATCH_TYPE";
    public static final String T996_WICKET_TYPE="T996_WICKET_TYPE";
    public static final String T997_PLAYER_TYPE="T997_PLAYER_TYPE";
    public static final String T998_TEAM_TYPE="T998_TEAM_TYPE";
    public static final String T999_TOURNAMENT_TYPE="T999_TOURNAMENT_TYPE";

    //Generic columns used in almost all tables
    public static final String COL_GEN_CREATE_TS="CREATE_TS";
    public static final String COL_GEN_LAST_UPDATED_TS="LAST_UPDATED_TS";

    //T001 Table Columns
    public static final String COL_T001_TEAM_ID="TEAM_ID";
    public static final String COL_T001_TEAM_NAME="TEAM_NAME";
    public static final String COL_T001_TEAM_TYPE_ID="TEAM_TYPE_ID";

    //T002 Table Columns
    public static final String COL_T002_PLAYER_ID="PLAYER_ID";
    public static final String COL_T002_PLAYER_TYPE_ID="PLAYER_TYPE_ID";
    public static final String COL_T002_PLAYER_NAME="PLAYER_NAME";
    public static final String COL_T002_TEAM_ID="TEAM_ID";
    //public static final String COL_T002_PLAYER_COUNTRY="PLAYER_COUNTRY";

    //T003 Table Columns
    public static final String COL_T003_MATCH_ID="MATCH_ID";
    public static final String COL_T003_TOURNAMENT_TYPE_ID="TOURNAMENT_TYPE_ID";
    public static final String COL_T003_TOURNAMENT_ID="TOURNAMENT_ID";
    public static final String COL_T003_MATCH_DATE="MATCH_DATE";
    public static final String COL_T003_OVERS_MAX="OVERS_MAX";
    public static final String COL_T003_INNING_NUMBER="INNING_NUMBER";
    public static final String COL_T003_OVERS_PLAYED="OVERS_PLAYED";
    public static final String COL_T003_BALLS_PLAYED="BALLS_PLAYED";
    public static final String COL_T003_TEAM_A_ID="TEAM_A_ID";
    public static final String COL_T003_TEAM_B_ID="TEAM_B_ID";
    public static final String COL_T003_TOSS_WINNER_TEAM_ID="TOSS_WINNER_TEAM_ID";
    public static final String COL_T003_TOSS_DECISION="TOSS_DECISION";
    public static final String COL_T003_MATCH_RESULT="MATCH_RESULT";

    //T004 Table Columns
    public static final String COL_T004_TOURNAMENT_ID="TOURNAMENT_ID";
    public static final String COL_T004_TOURNAMENT_TYPE_ID="TOURNAMENT_TYPE_ID";
    public static final String COL_T004_TOURNAMENT_NAME="TOURNAMENT_NAME";
    public static final String COL_T004_TEAMS_INVOLED="TEAMS_INVOLED";

    //T004 Table Columns
    public static final String COL_T005_BALL_ID="BALL_ID";
    public static final String COL_T005_MATCH_ID="MATCH_ID";
    public static final String COL_T005_BATT_TEAM_ID="BATT_TEAM_ID";
    public static final String COL_T005_BOWL_TEAM_ID="BOWL_TEAM_ID";
    public static final String COL_T005_BATTING_PLAYER_ID="BATTING_PLAYER_ID";
    public static final String COL_T005_BOWLER_PLAYER_ID="BOWLER_PLAYER_ID";
    public static final String COL_T005_NONSTRIKER_PLAYER_ID="NONSTRIKER_PLAYER_ID";
    public static final String COL_T005_OVER_NUM="OVER_NUM";
    public static final String COL_T005_BALL_NUM="BALL_NUM";
    public static final String COL_T005_RUNS_SCORED="RUNS_SCORED";
    public static final String COL_T005_WICKET_FLAG="WICKET_FLAG";
    public static final String COL_T005_LEGAL_BALL_FLAG="LEGAL_BALL_FLAG";

    //T006 Table Columns
    public static final String COL_T006_WICKET_ID="WICKET_ID";
    public static final String COL_T006_BALL_ID="BALL_ID";
    public static final String COL_T006_WICKET_PLAYER_ID="WICKET_PLAYER_ID";
    public static final String COL_T006_BOWLER_ID="BOWLER_ID";
    public static final String COL_T006_WICKET_TYPE="WICKET_TYPE";
    public static final String COL_T006_LEGAL_BALL_FLAG="LEGAL_BALL_FLAG";
    public static final String COL_T006_FIELDER_PLAYER_ID="FIELDER_PLAYER_ID";
    public static final String COL_T006_STRIKER_PLAYER_ID="STRIKER_PLAYER_ID";
    public static final String COL_T006_NON_STRIKER_PLAYER_ID="NON_STRIKER_PLAYER_ID";
    public static final String COL_T006_TENTH_WICKET_FLAG="TENTH_WICKET_FLAG";

    //T301 Table Columns
    public static final String COL_T301_PLAYER_ID="PLAYER_ID";
    public static final String COL_T301_MATCH_ID="MATCH_ID";
    public static final String COL_T301_PLAYER_TYPE_ID="PLAYER_TYPE_ID";
    public static final String COL_T301_BALLS_BOWLED="BALLS_BOWLED";
    public static final String COL_T301_RUNS_CONCEEDED="RUNS_CONCEEDED";
    public static final String COL_T301_DOT_BALLS="DOT_BALLS";
    public static final String COL_T301_WICKETS="WICKETS";
    public static final String COL_T301_EXTRAS="EXTRAS";
    public static final String COL_T301_MAIDENS="MAIDENS";

    //T302 Table Columns
    public static final String COL_T302_PLAYER_ID="PLAYER_ID";
    public static final String COL_T302_MATCH_ID="MATCH_ID";
    public static final String COL_T302_PLAYER_TYPE_ID="PLAYER_TYPE_ID";
    public static final String COL_T302_BALLS_FACED="BALLS_FACED";
    public static final String COL_T302_RUNS_SCORED="RUNS_SCORED";
    public static final String COL_T302_DOT_BALLS="DOT_BALLS";
    public static final String COL_T302_FOURS="FOURS";
    public static final String COL_T302_SIXES="SIXES";
    public static final String COL_T302_PLAYER_STATUS_ID="PLAYER_STATUS_ID";
    public static final String COL_T302_WICKET_BOWLER_ID="WICKET_BOWLER_ID";
    public static final String COL_T302_WICKET_PLAYER_ID="WICKET_PLAYER_ID";

    //T303 Table Columns
    public static final String COL_T303_PLAYER_ID="PLAYER_ID";
    public static final String COL_T303_MATCH_ID="MATCH_ID";
    public static final String COL_T303_PLAYER_TYPE_ID="PLAYER_TYPE_ID";
    public static final String COL_T303_CAUGHT_BEHIND="CAUGHT_BEHIND";
    public static final String COL_T303_STUMPED="STUMPED";
    public static final String COL_T303_RUN_OUTS="RUN_OUTS";

    //T304 Table Columns
    public static final String COL_T304_PLAYER_ID="PLAYER_ID";
    public static final String COL_T304_MATCH_ID="MATCH_ID";
    public static final String COL_T304_PLAYER_TYPE_ID="PLAYER_TYPE_ID";
    public static final String COL_T304_CATCHES="CATCHES";
    public static final String COL_T304_RUN_OUTS="RUN_OUTS";

    //T995 Table Columns
//    public static final String COL_T995_MATCH_TYPE_ID="MATCH_TYPE_ID";
//    public static final String COL_T995_MATCH_TYPE_NAME="MATCH_TYPE_ID";

    //T996 Table Columns
    public static final String COL_T996_WICKET_TYPE_ID="WICKET_TYPE_ID";
    public static final String COL_T996_WICKET_TYPE_NAME="WICKET_TYPE_NAME";

    //T997 Table Columns
    public static final String COL_T997_PLAYER_TYPE_ID="PLAYER_TYPE_ID";
    public static final String COL_T997_PLAYER_TYPE_NAME="PLAYER_TYPE_NAME";

    //T998 Table Columns
    public static final String COL_T998_TEAM_TYPE_ID="TEAM_TYPE_ID";
    public static final String COL_T998_TEAM_TYPE_NAME="TEAM_TYPE_NAME";

    //T999 Table Columns
    public static final String COL_T999_TOURNAMENT_TYPE_ID="TOURNAMENT_TYPE_ID";
    public static final String COL_T999_TOURNAMENT_FORMAT="TOURNAMENT_FORMAT";
    public static final String COL_T999_OVERS_MAX="OVERS_MAX";

    //Create Table Scripts
    private static final String T001_TABLE_CREATE = "CREATE TABLE " + T001_TEAM + "(" + COL_T001_TEAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_T001_TEAM_NAME + " VARCHAR,"
            + COL_T001_TEAM_TYPE_ID + " INTEGER,"
            + COL_GEN_CREATE_TS + " DATETIME,"
            + COL_GEN_LAST_UPDATED_TS + " DATETIME,"
            + "CONSTRAINT FK_T001_TEAM_TYPE "
            + "FOREIGN KEY (" + COL_T001_TEAM_TYPE_ID + ") "
            + "REFERENCES " + T998_TEAM_TYPE + "(" + COL_T998_TEAM_TYPE_ID + "))";
    private static final String T002_TABLE_CREATE = "CREATE TABLE " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_T002_PLAYER_TYPE_ID + " INTEGER,"
            + COL_T002_PLAYER_NAME + " VARCHAR,"
            + COL_T002_TEAM_ID + " VARCHAR,"
            //+ COL_T002_PLAYER_COUNTRY + " VARCHAR,"
            + COL_GEN_CREATE_TS + " DATETIME,"
            + COL_GEN_LAST_UPDATED_TS + " DATETIME,"
            + "CONSTRAINT FK_T002_PLAYER_TYPE_ID "
            + "FOREIGN KEY (" + COL_T002_PLAYER_TYPE_ID + ")"
            + "REFERENCES " + T997_PLAYER_TYPE + "(" + COL_T997_PLAYER_TYPE_ID + "),"
            + "CONSTRAINT FK_T002_TEAM_ID "
            + "FOREIGN KEY (" + COL_T002_TEAM_ID + ")"
            + "REFERENCES " + T001_TEAM + "(" + COL_T001_TEAM_ID + "))";
    private static final String T003_TABLE_CREATE = "CREATE TABLE " + T003_MATCHES + "(" + COL_T003_MATCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_T003_TOURNAMENT_TYPE_ID + " VARCHAR,"
            + COL_T003_TOURNAMENT_ID + " INTEGER,"
            + COL_T003_MATCH_DATE + " DATE,"
            + COL_T003_OVERS_MAX + " INTEGER,"
            + COL_T003_INNING_NUMBER + " INTEGER,"
            + COL_T003_OVERS_PLAYED + " INTEGER,"
            + COL_T003_BALLS_PLAYED + " INTEGER,"
            + COL_T003_TEAM_A_ID + " INTEGER,"
            + COL_T003_TEAM_B_ID + " INTEGER,"
            + COL_T003_TOSS_WINNER_TEAM_ID + " INTEGER,"
            + COL_T003_TOSS_DECISION + " VARCHAR,"
            + COL_T003_MATCH_RESULT + " VARCHAR,"
            + COL_GEN_CREATE_TS + " DATETIME,"
            + COL_GEN_LAST_UPDATED_TS + " DATETIME,"
            + "CONSTRAINT FK_T003_TOURNAMENT_TYPE_ID "
            + "FOREIGN KEY (" + COL_T003_TOURNAMENT_TYPE_ID + ")"
            + "REFERENCES " + T999_TOURNAMENT_TYPE + "(" + COL_T999_TOURNAMENT_TYPE_ID + "),"
            + "CONSTRAINT FK_T003_TOURNAMENT_ID "
            + "FOREIGN KEY (" + COL_T003_TOURNAMENT_ID + ")"
            + "REFERENCES " + T004_TOURNAMENT + "(" + COL_T004_TOURNAMENT_ID + "),"
            + "CONSTRAINT FK_T003_TOSS_TEAM_ID_A "
            + "FOREIGN KEY (" + COL_T003_TOSS_WINNER_TEAM_ID + ")"
            + "REFERENCES " + T001_TEAM + "(" + COL_T001_TEAM_ID + "),"
            + "CONSTRAINT FK_T003_TEAM_ID_A "
            + "FOREIGN KEY (" + COL_T003_TEAM_A_ID + ")"
            + "REFERENCES " + T001_TEAM + "(" + COL_T001_TEAM_ID + "),"
            + "CONSTRAINT FK_T003_TEAM_ID_B "
            + "FOREIGN KEY (" + COL_T003_TEAM_B_ID + ")"
            + "REFERENCES " + T001_TEAM + "(" + COL_T001_TEAM_ID + "))";
    private static final String T004_TABLE_CREATE = "CREATE TABLE " + T004_TOURNAMENT + "(" + COL_T004_TOURNAMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_T004_TOURNAMENT_TYPE_ID + " INTEGER,"
            + COL_T004_TOURNAMENT_NAME + " VARCHAR,"
            + COL_T004_TEAMS_INVOLED + " VARCHAR,"
            + COL_GEN_CREATE_TS + " DATETIME,"
            + COL_GEN_LAST_UPDATED_TS + " DATETIME,"
            + "CONSTRAINT FK_T004_TOURNAMENT_TYPE_ID "
            + "FOREIGN KEY (" + COL_T004_TOURNAMENT_TYPE_ID + ")"
            + "REFERENCES " + T999_TOURNAMENT_TYPE + "(" + COL_T999_TOURNAMENT_TYPE_ID + "))";
    private static final String T005_TABLE_CREATE = "CREATE TABLE " + T005_OVER_BY_BALL_STATS + "(" + COL_T005_BALL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_T005_MATCH_ID + " INTEGER,"
            + COL_T005_BATT_TEAM_ID + " INTEGER,"
            + COL_T005_BOWL_TEAM_ID + " INTEGER,"
            + COL_T005_BATTING_PLAYER_ID + " INTEGER,"
            + COL_T005_BOWLER_PLAYER_ID + " INTEGER,"
            + COL_T005_NONSTRIKER_PLAYER_ID + " INTEGER,"
            + COL_T005_OVER_NUM + " INTEGER,"
            + COL_T005_BALL_NUM + " INTEGER,"
            + COL_T005_RUNS_SCORED + " INTEGER,"
            + COL_T005_WICKET_FLAG + " VARCHAR,"
            + COL_T005_LEGAL_BALL_FLAG + " VARCHAR,"
            + " CHECK (LENGTH(" + COL_T005_WICKET_FLAG + ")=1) ,"
            + " CHECK (LENGTH(" + COL_T005_LEGAL_BALL_FLAG + ")=1) ,"
            + "CONSTRAINT FK_COL_T005_MATCH_ID "
            + "FOREIGN KEY (" + COL_T005_MATCH_ID + ")"
            + "REFERENCES " + T003_MATCHES + "(" + COL_T003_MATCH_ID + "),"
            + "CONSTRAINT FK_COL_T005_BATT_TEAM_ID "
            + "FOREIGN KEY (" + COL_T005_BATT_TEAM_ID + ")"
            + "REFERENCES " + T001_TEAM + "(" + COL_T001_TEAM_ID + "),"
            + "CONSTRAINT FK_COL_T005_BOWL_TEAM_ID "
            + "FOREIGN KEY (" + COL_T005_BOWL_TEAM_ID + ")"
            + "REFERENCES " + T001_TEAM + "(" + COL_T001_TEAM_ID + "),"
            + "CONSTRAINT FK_COL_T005_BATTING_PLAYER_ID "
            + "FOREIGN KEY (" + COL_T005_BATTING_PLAYER_ID + ")"
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T005_BOWLER_PLAYER_ID "
            + "FOREIGN KEY (" + COL_T005_BOWLER_PLAYER_ID + ")"
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T005_NONSTRIKER_PLAYER_ID "
            + "FOREIGN KEY (" + COL_T005_NONSTRIKER_PLAYER_ID + ")"
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "))";
    private static final String T006_TABLE_CREATE = "CREATE TABLE " + T006_WICKETS + "(" + COL_T006_WICKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_T006_BALL_ID + " INTEGER,"
            + COL_T006_BOWLER_ID + " INTEGER,"
            + COL_T006_LEGAL_BALL_FLAG + " VARCHAR,"
            + COL_T006_WICKET_PLAYER_ID + " INTEGER,"
            + COL_T006_FIELDER_PLAYER_ID + " INTEGER,"
            + COL_T006_STRIKER_PLAYER_ID + " INTEGER,"
            + COL_T006_NON_STRIKER_PLAYER_ID + " INTEGER,"
            + COL_T006_TENTH_WICKET_FLAG + " VARCHAR,"
            + COL_T006_WICKET_TYPE + " VARCHAR,"
            + "CONSTRAINT FK_COL_T006_WICKET_PLAYER "
            + "FOREIGN KEY (" + COL_T006_WICKET_PLAYER_ID + ") "
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T006_BOWLER "
            + "FOREIGN KEY (" + COL_T006_BOWLER_ID + ") "
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T006_STRIKER "
            + "FOREIGN KEY (" + COL_T006_STRIKER_PLAYER_ID + ") "
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T006_NON_STRIKER "
            + "FOREIGN KEY (" + COL_T006_NON_STRIKER_PLAYER_ID + ") "
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T006_FIELDER_PLAYER "
            + "FOREIGN KEY (" + COL_T006_FIELDER_PLAYER_ID + ") "
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_T006_BALL_ID "
            + "FOREIGN KEY (" + COL_T006_BALL_ID + ") "
            + "REFERENCES " + T005_OVER_BY_BALL_STATS + "(" + COL_T005_BALL_ID + "))";
    private static final String T301_TABLE_CREATE = "CREATE TABLE " + T301_PLAYER_BOWLER + "(" + COL_T301_PLAYER_ID + " INTEGER,"
            + COL_T301_MATCH_ID + " INTEGER,"
            + COL_T301_PLAYER_TYPE_ID + " INTEGER,"
            + COL_T301_BALLS_BOWLED + " INTEGER,"
            + COL_T301_RUNS_CONCEEDED + " INTEGER,"
            + COL_T301_DOT_BALLS + " INTEGER,"
            + COL_T301_WICKETS + " INTEGER,"
            + COL_T301_EXTRAS + " INTEGER,"
            + COL_T301_MAIDENS + " INTEGER,"
            + "CONSTRAINT FK_COL_T301_PLAYER_ID "
            + "FOREIGN KEY (" + COL_T301_PLAYER_ID + ")"
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T301_MATCH_ID "
            + "FOREIGN KEY (" + COL_T301_MATCH_ID + ")"
            + "REFERENCES " + T003_MATCHES + "(" + COL_T003_MATCH_ID + "),"
            + "CONSTRAINT FK_COL_T301_PLAYER_TYPE_ID "
            + "FOREIGN KEY (" + COL_T301_PLAYER_TYPE_ID + ")"
            + "REFERENCES " + T997_PLAYER_TYPE + "(" + COL_T997_PLAYER_TYPE_ID + "))";
    private static final String T302_TABLE_CREATE = "CREATE TABLE " + T302_PLAYER_BATSMEN + "(" + COL_T302_PLAYER_ID + " INTEGER,"
            + COL_T302_MATCH_ID + " INTEGER,"
            + COL_T302_PLAYER_TYPE_ID + " INTEGER,"
            + COL_T302_BALLS_FACED + " INTEGER,"
            + COL_T302_RUNS_SCORED + " INTEGER,"
            + COL_T302_DOT_BALLS + " INTEGER,"
            + COL_T302_FOURS + " INTEGER,"
            + COL_T302_SIXES + " INTEGER,"
            + COL_T302_PLAYER_STATUS_ID + " INTEGER,"
            + COL_T302_WICKET_BOWLER_ID + " INTEGER,"
            + COL_T302_WICKET_PLAYER_ID + " INTEGER,"
            + "CONSTRAINT FK_COL_T302_PLAYER_ID "
            + "FOREIGN KEY (" + COL_T302_PLAYER_ID + ")"
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T302_MATCH_ID "
            + "FOREIGN KEY (" + COL_T302_MATCH_ID + ")"
            + "REFERENCES " + T003_MATCHES + "(" + COL_T003_MATCH_ID + "),"
            + "CONSTRAINT FK_COL_T302_PLAYER_TYPE_ID "
            + "FOREIGN KEY (" + COL_T302_PLAYER_TYPE_ID + ")"
            + "REFERENCES " + T997_PLAYER_TYPE + "(" + COL_T997_PLAYER_TYPE_ID + "),"
            + "CONSTRAINT FK_COL_T302_PLAYER_STATUS_ID "
            + "FOREIGN KEY (" + COL_T302_PLAYER_STATUS_ID + ")"
            + "REFERENCES " + T996_WICKET_TYPE + "(" + COL_T996_WICKET_TYPE_ID + "),"
            + "CONSTRAINT FK_COL_T302_WICKET_BOWLER_ID "
            + "FOREIGN KEY (" + COL_T302_WICKET_BOWLER_ID + ")"
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T302_WICKET_PLAYER_ID "
            + "FOREIGN KEY (" + COL_T302_WICKET_PLAYER_ID + ")"
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "))";
    private static final String T303_TABLE_CREATE = "CREATE TABLE " + T303_PLAYER_WK + "(" + COL_T303_PLAYER_ID + " INTEGER,"
            + COL_T303_MATCH_ID + " INTEGER,"
            + COL_T303_PLAYER_TYPE_ID + " INTEGER,"
            + COL_T303_CAUGHT_BEHIND + " INTEGER,"
            + COL_T303_STUMPED + " INTEGER,"
            + COL_T303_RUN_OUTS + " INTEGER,"
            + "CONSTRAINT FK_COL_T303_PLAYER_ID "
            + "FOREIGN KEY (" + COL_T303_PLAYER_ID + ")"
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T303_MATCH_ID "
            + "FOREIGN KEY (" + COL_T303_MATCH_ID + ")"
            + "REFERENCES " + T003_MATCHES + "(" + COL_T003_MATCH_ID + "),"
            + "CONSTRAINT FK_COL_T303_PLAYER_TYPE_ID "
            + "FOREIGN KEY (" + COL_T303_PLAYER_TYPE_ID + ")"
            + "REFERENCES " + T997_PLAYER_TYPE + "(" + COL_T997_PLAYER_TYPE_ID + "))";
    private static final String T304_TABLE_CREATE = "CREATE TABLE " + T304_PLAYER_FIELDER + "(" + COL_T304_PLAYER_ID + " INTEGER,"
            + COL_T304_MATCH_ID + " INTEGER,"
            + COL_T304_PLAYER_TYPE_ID + " INTEGER,"
            + COL_T304_CATCHES  + " INTEGER,"
            + COL_T304_RUN_OUTS + " INTEGER,"
            + "CONSTRAINT FK_COL_T304_PLAYER_ID "
            + "FOREIGN KEY (" + COL_T304_PLAYER_ID + ")"
            + "REFERENCES " + T002_PLAYERS + "(" + COL_T002_PLAYER_ID + "),"
            + "CONSTRAINT FK_COL_T304_MATCH_ID "
            + "FOREIGN KEY (" + COL_T304_MATCH_ID + ")"
            + "REFERENCES " + T003_MATCHES + "(" + COL_T003_MATCH_ID + "),"
            + "CONSTRAINT FK_COL_T304_PLAYER_TYPE_ID "
            + "FOREIGN KEY (" + COL_T304_PLAYER_TYPE_ID + ")"
            + "REFERENCES " + T997_PLAYER_TYPE + "(" + COL_T997_PLAYER_TYPE_ID + "))";
    private static final String T996_TABLE_CREATE = "CREATE TABLE " + T996_WICKET_TYPE + "(" + COL_T996_WICKET_TYPE_ID + " INTEGER,"
            + COL_T996_WICKET_TYPE_NAME + " VARCHAR,"
            + COL_GEN_CREATE_TS + " DATETIME,"
            + COL_GEN_LAST_UPDATED_TS + " DATETIME )";
    private static final String T997_TABLE_CREATE = "CREATE TABLE " + T997_PLAYER_TYPE + "(" + COL_T997_PLAYER_TYPE_ID + " INTEGER,"
            + COL_T997_PLAYER_TYPE_NAME + " VARCHAR,"
            + COL_GEN_CREATE_TS + " DATETIME,"
            + COL_GEN_LAST_UPDATED_TS + " DATETIME )";
    private static final String T998_TABLE_CREATE = "CREATE TABLE " + T998_TEAM_TYPE + "(" + COL_T998_TEAM_TYPE_ID + " INTEGER,"
            + COL_T998_TEAM_TYPE_NAME + " VARCHAR,"
            + COL_GEN_CREATE_TS + " DATETIME,"
            + COL_GEN_LAST_UPDATED_TS + " DATETIME )";
    private static final String T999_TABLE_CREATE = "CREATE TABLE " + T999_TOURNAMENT_TYPE + "(" + COL_T999_TOURNAMENT_TYPE_ID + " INTEGER,"
            + COL_T999_TOURNAMENT_FORMAT + " VARCHAR,"
            + COL_T999_OVERS_MAX + " INTEGER,"
            + COL_GEN_CREATE_TS + " DATETIME,"
            + COL_GEN_LAST_UPDATED_TS + " DATETIME )";

    // Common Reference Values

    public SQLiteDBManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long InsertDataToT001 (String TeamName, int TeamTypeID, SQLiteDatabase db)
    {
        String DateTimeInput = dateFormat.format(new Date());

        values.clear();
        values.put(COL_T001_TEAM_NAME,TeamName);
        values.put(COL_T001_TEAM_TYPE_ID,TeamTypeID);
        values.put(COL_GEN_CREATE_TS,DateTimeInput);
        values.put(COL_GEN_LAST_UPDATED_TS,DateTimeInput);
//        System.out.println("SQLiteDBManager - InsertDataToT001");
        return db.insert(T001_TEAM,null,values);
    }

    public long InsertDataToT002 (String PlayerName, int TeamID, int PlayerTypeID , SQLiteDatabase db)
    {
        String DateTimeInput = dateFormat.format(new Date());

        values.clear();
        values.put(COL_T002_PLAYER_NAME,PlayerName);
        values.put(COL_T002_TEAM_ID,TeamID);
        values.put(COL_T002_PLAYER_TYPE_ID,PlayerTypeID);
        //values.put(COL_T002_PLAYER_COUNTRY,PlayerCountry);
        values.put(COL_GEN_CREATE_TS,DateTimeInput);
        values.put(COL_GEN_LAST_UPDATED_TS,DateTimeInput);
//        System.out.println("SQLiteDBManager - InsertDataToT002");
        return db.insert(T002_PLAYERS,null,values);
    }

    public long InsertDataToT003 (int TournamentTypeId, int TournamentID, int OversMax, int InningNumber, int OversPlayed, int BallsPlayed, int TeamA, int TeamB,int TossWinnerTeamID, String tossDecision, String matchResult, SQLiteDatabase db)
    {
//        SQLiteDatabase db = this.getWritableDatabase();
        String DateTimeInput = dateFormat.format(new Date());
        values.clear();
        values.put(COL_T003_TOURNAMENT_TYPE_ID,TournamentTypeId);
        values.put(COL_T003_TOURNAMENT_ID,TournamentID);
        values.put(COL_T003_MATCH_DATE,DateTimeInput);
        values.put(COL_T003_OVERS_MAX,OversMax);
        values.put(COL_T003_INNING_NUMBER,InningNumber);
        values.put(COL_T003_OVERS_PLAYED,OversPlayed);
        values.put(COL_T003_BALLS_PLAYED,BallsPlayed);
        values.put(COL_T003_TEAM_A_ID,TeamA);
        values.put(COL_T003_TEAM_B_ID,TeamB);
        values.put(COL_T003_TOSS_WINNER_TEAM_ID,TossWinnerTeamID);
        values.put(COL_T003_TOSS_DECISION,tossDecision);
        values.put(COL_T003_MATCH_RESULT,matchResult);
        values.put(COL_GEN_CREATE_TS,DateTimeInput);
        values.put(COL_GEN_LAST_UPDATED_TS,DateTimeInput);
//        System.out.println("SQLiteDBManager - InsertDataToT003");
        return db.insert(T003_MATCHES,null,values);
    }

    public long InsertDataToT004 (int TournamentTypeId, String TournamentName, String TeamsInvolved, SQLiteDatabase db)
    {
        String DateTimeInput = dateFormat.format(new Date());

        values.clear();
        values.put(COL_T004_TOURNAMENT_TYPE_ID,TournamentTypeId);
        values.put(COL_T004_TOURNAMENT_NAME,TournamentName);
        values.put(COL_T004_TEAMS_INVOLED,TeamsInvolved);
        values.put(COL_GEN_CREATE_TS,DateTimeInput);
        values.put(COL_GEN_LAST_UPDATED_TS,DateTimeInput);
//        System.out.println("SQLiteDBManager - InsertDataToT004");
        return db.insert(T004_TOURNAMENT,null,values);
    }

    public long InsertDataToT005 (int MatchID, int BatTeamID, int BowlTeamID, int BattPlayerID, int BowlPlayerID,
                                  int NonStrikerPlayerID, int OverNum, int BallNum, int RunsScored, String WicketFlag,
                                  String LegalBallFlag, SQLiteDatabase db)
    {
        values.clear();
        values.put(COL_T005_MATCH_ID,MatchID);
        values.put(COL_T005_BATT_TEAM_ID,BatTeamID);
        values.put(COL_T005_BOWL_TEAM_ID,BowlTeamID);
        values.put(COL_T005_BATTING_PLAYER_ID,BattPlayerID);
        values.put(COL_T005_BOWLER_PLAYER_ID,BowlPlayerID);
        values.put(COL_T005_NONSTRIKER_PLAYER_ID,NonStrikerPlayerID);
        values.put(COL_T005_OVER_NUM,OverNum);
        values.put(COL_T005_BALL_NUM,BallNum);
        values.put(COL_T005_RUNS_SCORED,RunsScored);
        values.put(COL_T005_WICKET_FLAG,WicketFlag);
        values.put(COL_T005_LEGAL_BALL_FLAG,LegalBallFlag);
//        System.out.println("SQLiteDBManager - InsertDataToT005");
        return db.insert(T005_OVER_BY_BALL_STATS,null,values);
    }

    public long InsertDataToT006 (int BallID, int BowlerID, String LegalBallFlag, int WicketPlayerID, int FielderID, int StrikerPlayerID, int NonStrikerPlayerID,
                                  String LastWicketFlag, String WicketType, SQLiteDatabase db)
    {
        values.clear();
        values.put(COL_T006_BALL_ID,BallID);
        values.put(COL_T006_BOWLER_ID,BowlerID);
        values.put(COL_T006_LEGAL_BALL_FLAG,LegalBallFlag);
        values.put(COL_T006_WICKET_PLAYER_ID,WicketPlayerID);
        values.put(COL_T006_FIELDER_PLAYER_ID,FielderID);
        values.put(COL_T006_STRIKER_PLAYER_ID,StrikerPlayerID);
        values.put(COL_T006_NON_STRIKER_PLAYER_ID,NonStrikerPlayerID);
        values.put(COL_T006_TENTH_WICKET_FLAG,LastWicketFlag);
        values.put(COL_T006_WICKET_TYPE,WicketType);
        return db.insert(T006_WICKETS,null,values);
    }

    public long InsertDataToT301 (int PlayerID, int MatchID, int PlayerTypeID, int BallsBowled, int RunsConceeded, int DotBalls, int Wickets,
                                  int Extras, int Maidens, SQLiteDatabase db)
    {
        values.clear();
        values.put(COL_T301_PLAYER_ID,PlayerID);
        values.put(COL_T301_MATCH_ID,MatchID);
        values.put(COL_T301_PLAYER_TYPE_ID,PlayerTypeID);
        values.put(COL_T301_BALLS_BOWLED,BallsBowled);
        values.put(COL_T301_RUNS_CONCEEDED,RunsConceeded);
        values.put(COL_T301_DOT_BALLS,DotBalls);
        values.put(COL_T301_WICKETS,Wickets);
        values.put(COL_T301_EXTRAS,Extras);
        values.put(COL_T301_MAIDENS,Maidens);
//        System.out.println("SQLiteDBManager - InsertDataToT301");
        return db.insert(T301_PLAYER_BOWLER,null,values);
    }

    public long InsertDataToT302 (int PlayerID, int MatchID, int PlayerTypeID, int BallsFaced, int RunsScored, int DotBalls, int Fours, int Sixes,
                                  int PlayerStatusID, int WicketBowlerID, int WicketPlayerID, SQLiteDatabase db)
    {
        values.clear();
        values.put(COL_T302_PLAYER_ID,PlayerID);
        values.put(COL_T302_MATCH_ID,MatchID);
        values.put(COL_T302_PLAYER_TYPE_ID,PlayerTypeID);
        values.put(COL_T302_BALLS_FACED,BallsFaced);
        values.put(COL_T302_RUNS_SCORED,RunsScored);
        values.put(COL_T302_DOT_BALLS,DotBalls);
        values.put(COL_T302_FOURS,Fours);
        values.put(COL_T302_SIXES,Sixes);
        values.put(COL_T302_PLAYER_STATUS_ID,PlayerStatusID);
        if (WicketBowlerID != 0 )
        {
            values.put(COL_T302_WICKET_BOWLER_ID,WicketBowlerID);
        }
        if (WicketPlayerID != 0 )
        {
            values.put(COL_T302_WICKET_PLAYER_ID, WicketPlayerID);
        }
//        System.out.println("SQLiteDBManager - InsertDataToT302");
        return db.insert(T302_PLAYER_BATSMEN,null,values);
    }

    public long InsertDataToT303 (int PlayerID, int MatchID, int PlayerTypeID, int CaughtBehind, int Stumped, int RunOuts, SQLiteDatabase db)
    {
        values.clear();
        values.put(COL_T303_PLAYER_ID,PlayerID);
        values.put(COL_T303_MATCH_ID,MatchID);
        values.put(COL_T303_PLAYER_TYPE_ID,PlayerTypeID);
        values.put(COL_T303_CAUGHT_BEHIND,CaughtBehind);
        values.put(COL_T303_STUMPED,Stumped);
        values.put(COL_T303_RUN_OUTS,RunOuts);
//        System.out.println("SQLiteDBManager - InsertDataToT303");
        return db.insert(T303_PLAYER_WK,null,values);
    }

    public long InsertDataToT304 (int PlayerID, int MatchID, int PlayerTypeID, int Catches, int RunOuts, SQLiteDatabase db)
    {
        values.clear();
        values.put(COL_T304_PLAYER_ID,PlayerID);
        values.put(COL_T304_MATCH_ID,MatchID);
        values.put(COL_T304_PLAYER_TYPE_ID,PlayerTypeID);
        values.put(COL_T304_CATCHES,Catches);
        values.put(COL_T304_RUN_OUTS,RunOuts);
//        System.out.println("SQLiteDBManager - InsertDataToT304");
        return db.insert(T304_PLAYER_FIELDER,null,values);
    }

    public long InsertDataToT996 (int WicketTypeID, String WicketTypeName, SQLiteDatabase db)
    {
        String DateTimeInput = dateFormat.format(new Date());

        values.clear();
        values.put(COL_T996_WICKET_TYPE_ID,WicketTypeID);
        values.put(COL_T996_WICKET_TYPE_NAME,WicketTypeName);
        values.put(COL_GEN_CREATE_TS,DateTimeInput);
        values.put(COL_GEN_LAST_UPDATED_TS,DateTimeInput);
//        System.out.println("SQLiteDBManager - InsertDataToT996");
        return db.insert(T996_WICKET_TYPE,null,values);
    }

    public long InsertDataToT997 (int PlayerTypeID, String PlayerTypeName, SQLiteDatabase db)
    {
        String DateTimeInput = dateFormat.format(new Date());

        values.clear();
        values.put(COL_T997_PLAYER_TYPE_ID,PlayerTypeID);
        values.put(COL_T997_PLAYER_TYPE_NAME,PlayerTypeName);
        values.put(COL_GEN_CREATE_TS,DateTimeInput);
        values.put(COL_GEN_LAST_UPDATED_TS,DateTimeInput);
//        System.out.println("SQLiteDBManager - InsertDataToT997");
        return db.insert(T997_PLAYER_TYPE,null,values);
    }

    public long InsertDataToT998 (int TeamTypeID, String TeamTypeName, SQLiteDatabase db)
    {
        String DateTimeInput = dateFormat.format(new Date());

        values.clear();
        values.put(COL_T998_TEAM_TYPE_ID,TeamTypeID);
        values.put(COL_T998_TEAM_TYPE_NAME,TeamTypeName);
        values.put(COL_GEN_CREATE_TS,DateTimeInput);
        values.put(COL_GEN_LAST_UPDATED_TS,DateTimeInput);
//        System.out.println("SQLiteDBManager - InsertDataToT998");
        return db.insert(T998_TEAM_TYPE,null,values);
    }

    public long InsertDataToT999 (int TournamentTypeId, String TournamentFormat, int MaxOvers, SQLiteDatabase db)
    {
        String DateTimeInput = dateFormat.format(new Date());

        values.clear();
        values.put(COL_T999_TOURNAMENT_TYPE_ID,TournamentTypeId);
        values.put(COL_T999_TOURNAMENT_FORMAT,TournamentFormat);
        values.put(COL_T999_OVERS_MAX,MaxOvers);
        values.put(COL_GEN_CREATE_TS,DateTimeInput);
        values.put(COL_GEN_LAST_UPDATED_TS,DateTimeInput);
//        System.out.println("SQLiteDBManager - InsertDataToT999");
        return db.insert(T999_TOURNAMENT_TYPE,null,values);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Cursor cursor;
        // Create T999_TOURNAMENT_TYPE Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T999_TOURNAMENT_TYPE + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T999_TABLE_CREATE);
            InsertDataToT999(1,"Test",0,db);
            InsertDataToT999(2,"ODI",50,db);
            InsertDataToT999(3,"6 Over Limited Cricket",6,db);
            InsertDataToT999(4,"8 Over Limited Cricket",8,db);
            InsertDataToT999(5,"10 Over Limited Cricket",10,db);
            InsertDataToT999(6,"15 Over Limited Cricket",15,db);
            InsertDataToT999(7,"20 Over Limited Cricket",20,db);
            InsertDataToT999(8,"25 Over Limited Cricket",25,db);
            InsertDataToT999(9,"Other",0,db);
        }

        // Create T998_TEAM_TYPE Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T998_TEAM_TYPE + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T998_TABLE_CREATE);
            InsertDataToT998(1,"Home",db);
            InsertDataToT998(2,"Away",db);
            InsertDataToT998(3,"Neutral",db);
        }

        // Create T997_PLAYER_TYPE Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T997_PLAYER_TYPE + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T997_TABLE_CREATE);
            InsertDataToT997(1,"Batsmen",db);
            InsertDataToT997(2,"Bowler",db);
            InsertDataToT997(3,"All Rounder",db);
            InsertDataToT997(4,"WicketKeeper",db);
        }

        // Create T996_PLAYER_STATUS Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T996_WICKET_TYPE + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T996_TABLE_CREATE);
            InsertDataToT996(1,"Not Out",db);
            InsertDataToT996(2,"Bowled",db);
            InsertDataToT996(3,"Caught",db);
            InsertDataToT996(4,"Caught Behind",db);
            InsertDataToT996(5,"Caught and Bowl",db);
            InsertDataToT996(6,"Stumped",db);
            InsertDataToT996(7,"LBW",db);
            InsertDataToT996(8,"Run Out",db);
            InsertDataToT996(9,"Hit Wicket",db);
            InsertDataToT996(10,"Obstructing the Field",db);
            InsertDataToT996(11,"Retired Out",db);
            InsertDataToT996(12,"Hit the ball twice",db);
            InsertDataToT996(13,"Timed out",db);
        }


        // Create T001_TEAM Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T001_TEAM + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T001_TABLE_CREATE);
            InsertDataToT001("MyHomeTeam",1,db);
            InsertDataToT001("MyAwayTeam",2,db);
        }

        // Create T002_PLAYERS Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T002_PLAYERS + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T002_TABLE_CREATE);

            // Insert sample players for Home Team - 1
            String get_team_id = "select min(" + COL_T001_TEAM_ID + ") from " + T001_TEAM + " where " + COL_T001_TEAM_TYPE_ID + "=1";
            cursor = db.rawQuery(get_team_id,null);
            cursor.moveToFirst();
//            InsertDataToT002("MyPlayer1",Integer.parseInt(cursor.getString(0)),"NoCountrySet",db);
            InsertDataToT002("MyPlayer1",Integer.parseInt(cursor.getString(0)),1,db);
            InsertDataToT002("MyPlayer2",Integer.parseInt(cursor.getString(0)),1,db);
            InsertDataToT002("MyPlayer3",Integer.parseInt(cursor.getString(0)),1,db);
            InsertDataToT002("MyPlayer4",Integer.parseInt(cursor.getString(0)),1,db);
            InsertDataToT002("MyPlayer5",Integer.parseInt(cursor.getString(0)),3,db);
            InsertDataToT002("MyPlayer6",Integer.parseInt(cursor.getString(0)),4,db);
            InsertDataToT002("MyPlayer7",Integer.parseInt(cursor.getString(0)),3,db);
            InsertDataToT002("MyPlayer8",Integer.parseInt(cursor.getString(0)),2,db);
            InsertDataToT002("MyPlayer9",Integer.parseInt(cursor.getString(0)),2,db);
            InsertDataToT002("MyPlayer10",Integer.parseInt(cursor.getString(0)),2,db);
            InsertDataToT002("MyPlayer11",Integer.parseInt(cursor.getString(0)),2,db);

            // Insert sample players for Away Team - 1
            get_team_id = "select min(" + COL_T001_TEAM_ID + ") from " + T001_TEAM + " where " + COL_T001_TEAM_TYPE_ID + "=2";
            cursor = db.rawQuery(get_team_id,null);
            cursor.moveToFirst();
//            InsertDataToT002("OppPlayer1",Integer.parseInt(cursor.getString(0)),"NoCountrySet",db);
            InsertDataToT002("OppPlayer1",Integer.parseInt(cursor.getString(0)),1,db);
            InsertDataToT002("OppPlayer2",Integer.parseInt(cursor.getString(0)),1,db);
            InsertDataToT002("OppPlayer3",Integer.parseInt(cursor.getString(0)),1,db);
            InsertDataToT002("OppPlayer4",Integer.parseInt(cursor.getString(0)),1,db);
            InsertDataToT002("OppPlayer5",Integer.parseInt(cursor.getString(0)),3,db);
            InsertDataToT002("OppPlayer6",Integer.parseInt(cursor.getString(0)),4,db);
            InsertDataToT002("OppPlayer7",Integer.parseInt(cursor.getString(0)),3,db);
            InsertDataToT002("OppPlayer8",Integer.parseInt(cursor.getString(0)),2,db);
            InsertDataToT002("OppPlayer9",Integer.parseInt(cursor.getString(0)),2,db);
            InsertDataToT002("OppPlayer10",Integer.parseInt(cursor.getString(0)),2,db);
            InsertDataToT002("OppPlayer11",Integer.parseInt(cursor.getString(0)),2,db);
        }

        // Create T003_MATCHES Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T003_MATCHES + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T003_TABLE_CREATE);
        }

        // Create T004_TOURNAMENT Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T004_TOURNAMENT + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T004_TABLE_CREATE);
            InsertDataToT004(9,"Single Match (No Tournament)","NA",db);
        }

        // Create T005_OVER_BY_BALL_STATS Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T005_OVER_BY_BALL_STATS + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T005_TABLE_CREATE);
        }

        // Create T006_WICKETS Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T006_WICKETS + "'", null);
        System.out.println("SQLiteDBManager - " + cursor.getCount());
        if (cursor.getCount() == 0) {
            System.out.println("SQLiteDBManager - executing");
            db.execSQL(T006_TABLE_CREATE);
        }

        // Create T301_PLAYER_BOWLER Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T301_PLAYER_BOWLER + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T301_TABLE_CREATE);
        }

        // Create T302_PLAYER_BATSMEN Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T302_PLAYER_BATSMEN + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T302_TABLE_CREATE);
        }

        // Create T303_PLAYER_WK Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T303_PLAYER_WK + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T303_TABLE_CREATE);
        }

        // Create T304_PLAYER_FIELDER Table
        cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + T304_PLAYER_FIELDER + "'", null);
        if (cursor.getCount() == 0) {
            db.execSQL(T304_TABLE_CREATE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        //Backup Transactional data before Migration
        db.execSQL("CREATE TABLE " + T001_TEAM + "_BKP AS SELECT * FROM " + T001_TEAM + " WHERE 0");
        db.execSQL("CREATE TABLE " + T002_PLAYERS + "_BKP AS SELECT * FROM " + T002_PLAYERS + " WHERE 0");
        db.execSQL("CREATE TABLE " + T003_MATCHES + "_BKP AS SELECT * FROM " + T003_MATCHES + " WHERE 0");
//        db.execSQL("CREATE TABLE " + T004_TOURNAMENT + "_BKP AS SELECT * FROM " + T004_TOURNAMENT + " WHERE 0");
//        db.execSQL("CREATE TABLE " + T005_OVER_BY_BALL_STATS + "_BKP AS SELECT * FROM " + T005_OVER_BY_BALL_STATS + " WHERE 0");
//        db.execSQL("CREATE TABLE " + T301_PLAYER_BOWLER + "_BKP AS SELECT * FROM " + T301_PLAYER_BOWLER + " WHERE 0");
//        db.execSQL("CREATE TABLE " + T302_PLAYER_BATSMEN + "_BKP AS SELECT * FROM " + T302_PLAYER_BATSMEN + " WHERE 0");
//        db.execSQL("CREATE TABLE " + T303_PLAYER_WK + "_BKP AS SELECT * FROM " + T303_PLAYER_WK + " WHERE 0");
//        db.execSQL("CREATE TABLE " + T304_PLAYER_FIELDER + "_BKP AS SELECT * FROM " + T304_PLAYER_FIELDER + " WHERE 0");

        // Drop older table if exist
//        System.out.println("SQLiteDBManager - Dropping DB");
        db.execSQL("DROP TABLE IF EXISTS " + T001_TEAM);
        db.execSQL("DROP TABLE IF EXISTS " + T002_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + T003_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + T004_TOURNAMENT);
        db.execSQL("DROP TABLE IF EXISTS " + T005_OVER_BY_BALL_STATS);
        db.execSQL("DROP TABLE IF EXISTS " + T301_PLAYER_BOWLER);
        db.execSQL("DROP TABLE IF EXISTS " + T302_PLAYER_BATSMEN);
        db.execSQL("DROP TABLE IF EXISTS " + T303_PLAYER_WK);
        db.execSQL("DROP TABLE IF EXISTS " + T304_PLAYER_FIELDER);
        db.execSQL("DROP TABLE IF EXISTS " + T998_TEAM_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + T999_TOURNAMENT_TYPE);

        // Create tables again
//        System.out.println("SQLiteDBManager - Recreating DB");
        onCreate(db);

        //Restore Transactional Data once the New DB's are Created
        db.execSQL("insert into " + T001_TEAM + " select * from " + T001_TEAM + "_BKP");
        db.execSQL("insert into " + T002_PLAYERS + " select * from " + T002_PLAYERS + "_BKP");
        db.execSQL("insert into " + T003_MATCHES + " select * from " + T003_MATCHES + "_BKP");
//        db.execSQL("insert into " + T004_TOURNAMENT + " select * from " + T004_TOURNAMENT + "_BKP");
//        db.execSQL("insert into " + T005_OVER_BY_BALL_STATS + " select * from " + T005_OVER_BY_BALL_STATS + "_BKP");
//        db.execSQL("insert into " + T301_PLAYER_BOWLER + " select * from " + T301_PLAYER_BOWLER + "_BKP");
//        db.execSQL("insert into " + T302_PLAYER_BATSMEN + " select * from " + T302_PLAYER_BATSMEN + "_BKP");
//        db.execSQL("insert into " + T303_PLAYER_WK + " select * from " + T303_PLAYER_WK + "_BKP");
//        db.execSQL("insert into " + T304_PLAYER_FIELDER + " select * from " + T304_PLAYER_FIELDER + "_BKP");
    }
}