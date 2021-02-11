package com.example.ex121;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class AddGrade extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ArrayList<String> namesTbl;
    Spinner studentName, quarter;
    int nameIndex, quarterIndex;
    EditText subject, grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        studentName = (Spinner) findViewById(R.id.studentName);
        quarter = (Spinner) findViewById(R.id.quarter);

        subject = (EditText) findViewById(R.id.subject);
        grade = (EditText) findViewById(R.id.grade);

        nameIndex = 0;
        quarterIndex = 0;

        hlp = new HelperDB(this);

        studentName.setOnItemSelectedListener(this);
        quarter.setOnItemSelectedListener(this);
        initSpinners();
    }

    public void initSpinners()
    {
        // studentName spinner
        db = hlp.getWritableDatabase();
        namesTbl = new ArrayList<>();

        namesTbl.add("Name");

        crsr = db.query(Students.TABLE_STUDENTS, null, null, null, null, null, null);
        int colName = crsr.getColumnIndex(Students.NAME);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            String name = crsr.getString(colName);
            namesTbl.add(name);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        ArrayAdapter<String> namesAdp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, namesTbl);
        studentName.setAdapter(namesAdp);

        // quarter spinner
        ArrayAdapter<String> quarterAdp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.quarters));
        quarter.setAdapter(quarterAdp);
    }

    public void getData(View view) {
        String subjectStr = subject.getText().toString();
        String gradeStr = grade.getText().toString();

        if ((nameIndex != 0) && (quarterIndex != 0)) {
            if ((!subjectStr.equals("")) && (!gradeStr.equals(""))) {
                // if the subject is Alphabetic and grade <= 100 (its already >= 0)
                if ((checkAlphabetic(subjectStr) && (Integer.parseInt(gradeStr) <= 100)))
                {
                    // Add the data to the second table1
                    ContentValues cv = new ContentValues();

                    cv.put(Grades.ID, nameIndex);
                    cv.put(Grades.Subject, subjectStr);
                    cv.put(Grades.Quarter, quarterIndex);
                    cv.put(Grades.Grade, Integer.parseInt(gradeStr));
                    cv.put(Grades.Active, 1);

                    db = hlp.getWritableDatabase();

                    db.insert(Grades.TABLE_GRADES, null, cv);

                    db.close();
                }
                else {
                    Toast.makeText(this, "Subject or grade is invalid!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Subject or grade is invalid!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Name or quarter is invalid!", Toast.LENGTH_SHORT).show();
        }
    }

    // <check if a string is full Alphabetic>
    public static boolean checkAlphabetic(String input) {
        for (int i = 0; i != input.length(); ++i) {
            if (!Character.isLetter(input.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
            case R.id.studentName:
                nameIndex = position;
                break;
            case R.id.quarter:
                quarterIndex = position;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}