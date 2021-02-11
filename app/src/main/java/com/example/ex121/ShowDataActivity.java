package com.example.ex121;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener{
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ArrayList<String> sortingTbl;
    Spinner sortOption, param;
    ListView sortedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        sortOption = (Spinner) findViewById(R.id.sortOption);
        param = (Spinner) findViewById(R.id.param);

        sortedView = (ListView) findViewById(R.id.sortedView);

        hlp = new HelperDB(this);

        sortOption.setOnItemSelectedListener(this);
        param.setOnItemSelectedListener(this);

        sortedView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        sortedView.setOnItemClickListener(this);

        // init the sortOption spinner
        ArrayAdapter<String> optionsAdp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.sortOptions));
        sortOption.setAdapter(optionsAdp);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId())
        {
            case R.id.sortOption:
                int colName = 0;

                if (position == 0)
                {
                    param.setVisibility(View.INVISIBLE);
                    sortedView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    sortedView.setVisibility(View.VISIBLE);

                    if (position == 1)
                    {
                        // Students names
                        param.setVisibility(View.VISIBLE);
                        sortingTbl = findStudentsNames();
                        sortingTbl.add(0, "Name");

                        ArrayAdapter<String> namesAdp = new ArrayAdapter<String>(this,
                                R.layout.support_simple_spinner_dropdown_item, sortingTbl);
                        param.setAdapter(namesAdp);
                    }
                    else
                    {
                        param.setVisibility(View.INVISIBLE);
                        String orderBy = Grades.Grade;

                        // Students Grades (high->low)
                        if (position == 3)
                        {
                            orderBy += " DESC ";
                        }

                        // Get the names of students
                        ArrayList<String> names = findStudentsNames();
                        gradesSorting(null, names.toArray(new String[names.size()]), orderBy);
                    }
                }
                break;
            case R.id.param:
                if (!sortingTbl.get(position).equals("Name")) {
                    sortedView.setVisibility(View.VISIBLE);

                    // Get the name
                    String[] selectionArgs = {sortingTbl.get(position)};
                    String selection = Students.NAME + "=?";

                    // the grade sort (and init the function parms)
                    db = hlp.getWritableDatabase();

                    crsr = db.query(Students.TABLE_STUDENTS, null, selection, selectionArgs, null, null, null);
                    int col1 = crsr.getColumnIndex(Students.KEY_ID);

                    String userId = "";

                    crsr.moveToFirst();
                    userId = crsr.getString(col1);
                    crsr.close();

                    selectionArgs = new String[]{String.valueOf(Integer.parseInt(userId))};
                    selection = Grades.ID +"=?";

                    crsr.close();
                    db.close();

                    // make the sort
                    gradesSorting(selection, selectionArgs, Grades.Quarter);
                }
                else {
                    sortedView.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    void gradesSorting(String selection, String[] selectionArgs, String orderBy)
    {
        ArrayList<String> quarter = new ArrayList<>();
        ArrayList<String> grade = new ArrayList<>();
        ArrayList<String> subject = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        db = hlp.getWritableDatabase();

        // if its first option
        if (selection != null) {
            crsr = db.query(Grades.TABLE_GRADES, null, selection, selectionArgs, null, null, orderBy);
        }
        // its the 2 or 3 option
        else {
            crsr = db.query(Grades.TABLE_GRADES, null, null, null, null, null, orderBy);
        }

        int col1 = crsr.getColumnIndex(Grades.Quarter);
        int col2 = crsr.getColumnIndex(Grades.Subject);
        int col3 = crsr.getColumnIndex(Grades.Grade);
        int col4 = crsr.getColumnIndex(Grades.ID);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            quarter.add(crsr.getString(col1));
            subject.add(crsr.getString(col2));
            grade.add(crsr.getString(col3));

            // if is 1'st option
            if (selectionArgs.length == 1)
            {
                names.add(selectionArgs[0]);
            }
            else {
                // and the specific name of student
                names.add(selectionArgs[Integer.parseInt(crsr.getString(col4)) - 1]);
            }
            crsr.moveToNext();
        }
        crsr.close();
        db.close();

        if (grade.size() != 0) {
            CustomGradesAdapter customAdp = new CustomGradesAdapter(getApplicationContext(),
                    quarter, grade, subject, names);
            sortedView.setAdapter(customAdp);
        }
        else {
            Toast.makeText(this, "No Grades for this student", Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<String> findStudentsNames()
    {
        ArrayList<String> names = new ArrayList<>();

        db = hlp.getWritableDatabase();

        crsr = db.query(Students.TABLE_STUDENTS, null, null, null, null, null, null);
        int colName = crsr.getColumnIndex(Students.NAME);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            String name = crsr.getString(colName);
            names.add(name);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();

        return names;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}