package com.example.ex121;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.ex121.Grades.Grade;
import static com.example.ex121.Grades.ID;
import static com.example.ex121.Grades.Quarter;
import static com.example.ex121.Grades.Subject;
import static com.example.ex121.Grades.TABLE_GRADES;
import static com.example.ex121.Students.ADDRESS;
import static com.example.ex121.Students.DAD_NAME;
import static com.example.ex121.Students.DAD_PHONE;
import static com.example.ex121.Students.KEY_ID;
import static com.example.ex121.Students.MOM_NAME;
import static com.example.ex121.Students.MOM_PHONE;
import static com.example.ex121.Students.NAME;
import static com.example.ex121.Students.PHONE;
import static com.example.ex121.Students.TABLE_STUDENTS;
import static com.example.ex121.Students.HOME_PHONE;

/**
 *  @author Itay Weintraub <av5350@bs.amalnet.k12.il>
 *  @version 1
 *  @since 6.01.2021
 *  The type Helper db.
 */
public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Students.db";
    private static final int DATABASE_VERSION = 5;
    String strCreate, strDelete;

    /**
     * Instantiates a new Helper db.
     *
     * @param context the context
     */
    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the students table
        strCreate="CREATE TABLE "+TABLE_STUDENTS;
        strCreate+=" ("+KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+NAME+" TEXT,";
        strCreate+=" "+PHONE+" INTEGER,";
        strCreate+=" "+HOME_PHONE+" INTEGER,";
        strCreate+=" "+DAD_NAME+" TEXT,";
        strCreate+=" "+DAD_PHONE+" INTEGER,";
        strCreate+=" "+MOM_NAME+" TEXT,";
        strCreate+=" "+MOM_PHONE+" INTEGER,";
        strCreate+=" "+ADDRESS+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);

        // create the grades table
        strCreate="CREATE TABLE "+TABLE_GRADES;
        strCreate+=" ("+KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+ID+" INTEGER,";
        strCreate+=" "+Subject+" TEXT,";
        strCreate+=" "+Quarter+" INTEGER,";
        strCreate+=" "+Grade+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete the students table
        strDelete="DROP TABLE IF EXISTS "+TABLE_STUDENTS;
        db.execSQL(strDelete);

        // delete the grades table
        strDelete="DROP TABLE IF EXISTS "+TABLE_GRADES;
        db.execSQL(strDelete);

        onCreate(db);
    }
}