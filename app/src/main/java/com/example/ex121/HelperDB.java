package com.example.ex121;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.ex121.Grades.Active;
import static com.example.ex121.Grades.Grade;
import static com.example.ex121.Grades.ID;
import static com.example.ex121.Grades.Quarter;
import static com.example.ex121.Grades.Subject;
import static com.example.ex121.Grades.TABLE_GRADES;
import static com.example.ex121.Students.ADDRESS;
import static com.example.ex121.Students.DAD_NAME;
import static com.example.ex121.Students.DAD_PHONE;
import static com.example.ex121.Students.IS_ACTIVE;
import static com.example.ex121.Students.KEY_ID;
import static com.example.ex121.Students.MOM_NAME;
import static com.example.ex121.Students.MOM_PHONE;
import static com.example.ex121.Students.NAME;
import static com.example.ex121.Students.PHONE;
import static com.example.ex121.Students.TABLE_STUDENTS;
import static com.example.ex121.Students.HOME_PHONE;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Students.db";
    private static final int DATABASE_VERSION = 2;
    String strCreate, strDelete;

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
        strCreate+=" "+ADDRESS+" TEXT,";
        strCreate+=" "+IS_ACTIVE+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);

        // create the grades table
        strCreate="CREATE TABLE "+TABLE_GRADES;
        strCreate+=" "+ID+" INTEGER,";
        strCreate+=" "+Subject+" TEXT,";
        strCreate+=" "+Quarter+" INTEGER,";
        strCreate+=" "+Grade+" INTEGER,";
        strCreate+=" "+Active+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete the students table
        strDelete="DROP TABLE IF EXISTS "+TABLE_STUDENTS;
        db.execSQL(strDelete);

        onCreate(db);
    }
}
