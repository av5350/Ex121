package com.example.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *  @author Itay Weintraub <av5350@bs.amalnet.k12.il>
 *  @version 1
 *  @since 6.01.2021
 *  The type Add grade. (add new grade for student)
 */
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

    /**
     * Init spinners - the current students names and the quaters (1-4).
     */
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

    /**
     * Gets data. (the subject name and the grade value)
     *
     * @param view the view
     */
    public void getData(View view) {
        String subjectStr = subject.getText().toString();
        String gradeStr = grade.getText().toString();

        // if the user has chosen a name and quarter
        if ((nameIndex != 0) && (quarterIndex != 0)) {
            if ((!subjectStr.equals("")) && (!gradeStr.equals(""))) {
                // if the subject is Alphabetic and grade <= 100 (its already >= 0)
                if ((checkAlphabetic(subjectStr) && (Integer.parseInt(gradeStr) <= 100)))
                {
                    // Add the data to the grades table
                    ContentValues cv = new ContentValues();

                    cv.put(Grades.ID, nameIndex);
                    cv.put(Grades.Subject, subjectStr);
                    cv.put(Grades.Quarter, quarterIndex);
                    cv.put(Grades.Grade, Integer.parseInt(gradeStr));

                    db = hlp.getWritableDatabase();
                    db.insert(Grades.TABLE_GRADES, null, cv);
                    db.close();

                    Toast.makeText(this, "Grade was added!", Toast.LENGTH_SHORT).show();
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

    /**
     * Check if a string is an full alphabetic.
     *
     * @param input the input to check
     * @return if a string is an alphabetic
     */
    public static boolean checkAlphabetic(String input) {
        for (int i = 0; i != input.length(); ++i) {
            if (!Character.isLetter(input.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Create the options menu
     *
     * @param menu the menu
     * @return true if success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * What do when a item was clicked
     *
     * @param parent The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
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

    /**
     * Where to go when the menu item was selected
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return false to allow normal menu processing to
     *         proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // go to add students activity if clicked
        if (id == R.id.addStudent)
        {
            Intent si = new Intent(this, AddStudentActivity.class);
            startActivity(si);
        }
        else if (id == R.id.sort)
        {
            Intent si = new Intent(this, ShowDataActivity.class);
            startActivity(si);
        }
        else if (id == R.id.credits)
        {
            Intent si = new Intent(this, CreaditsActivity.class);
            startActivity(si);
        }

        return true;
    }
}