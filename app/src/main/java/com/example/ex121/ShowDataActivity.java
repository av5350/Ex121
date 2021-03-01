package com.example.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *  @author Itay Weintraub <av5350@bs.amalnet.k12.il>
 *  @version 1
 *  @since 6.01.2021
 *  The type Show data (and edit data) activity.
 */
public class ShowDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ArrayList<String> sortingTbl, idArr, quarter, grade, subject, names;
    Spinner sortOption, param;
    ListView sortedView;
    AlertDialog.Builder adb;
    CustomGradesAdapter customAdp;

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
        sortedView.setOnItemLongClickListener(this);
        sortedView.setOnItemClickListener(this);

        // init the sortOption spinner
        ArrayAdapter<String> optionsAdp = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.sortOptions));
        sortOption.setAdapter(optionsAdp);
    }

    /**
     * when an item was selected in the spiner, Responds it
     *
     * @param parent The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // check what spinmner was clicked
        switch (parent.getId())
        {
            case R.id.sortOption:
                // if its the name of the spinner (0's place)
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

                        // make the sort - for 2/3 option
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

                    crsr = db.query(Students.TABLE_STUDENTS, new String[]{Students.KEY_ID}, selection, selectionArgs, null, null, null);
                    int col1 = crsr.getColumnIndex(Students.KEY_ID);

                    crsr.moveToFirst();
                    String userId = crsr.getString(col1);
                    crsr.close();
                    db.close();

                    selectionArgs = new String[]{String.valueOf(Integer.parseInt(userId))};
                    selection = Grades.ID +"=?";

                    // make the sort - for 1'st option
                    gradesSorting(selection, selectionArgs, Grades.Quarter);
                }
                else {
                    sortedView.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    /**
     * Grades sorting.
     *
     * @param selection     the selection
     * @param selectionArgs the selection args
     * @param orderBy       the order by
     */
    void gradesSorting(String selection, String[] selectionArgs, String orderBy)
    {
        String Studname = "";

        quarter = new ArrayList<>();
        grade = new ArrayList<>();
        subject = new ArrayList<>();
        names = new ArrayList<>();
        idArr = new ArrayList<String>();

        // if is 1'st option - get the student name just once...
        if (selectionArgs.length == 1)
        {
            db = hlp.getWritableDatabase();

            crsr = db.query(Students.TABLE_STUDENTS, new String[]{Students.NAME}, Students.KEY_ID+"=?", selectionArgs, null, null, null);
            int colName = crsr.getColumnIndex(Students.NAME);

            crsr.moveToFirst();
            Studname = crsr.getString(colName);
            crsr.close();
            db.close();
        }

        db = hlp.getWritableDatabase();

        // if its first option
        if (selection != null) {
            crsr = db.query(Grades.TABLE_GRADES, new String[]{Grades.Quarter, Grades.Subject, Grades.Grade, Grades.ID, Grades.KEY_ID}, selection, selectionArgs, null, null, orderBy);
        }
        // its the 2 or 3 option
        else {
            crsr = db.query(Grades.TABLE_GRADES, new String[]{Grades.Quarter, Grades.Subject, Grades.Grade, Grades.ID, Grades.KEY_ID}, null, null, null, null, orderBy);
        }

        int col1 = crsr.getColumnIndex(Grades.Quarter);
        int col2 = crsr.getColumnIndex(Grades.Subject);
        int col3 = crsr.getColumnIndex(Grades.Grade);
        int col4 = crsr.getColumnIndex(Grades.ID);
        int col5 = crsr.getColumnIndex(Grades.KEY_ID);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            quarter.add(crsr.getString(col1));
            subject.add(crsr.getString(col2));
            grade.add(crsr.getString(col3));
            idArr.add(crsr.getString(col5));

            // if is 1'st option
            if (selectionArgs.length == 1)
            {
                names.add(Studname);
            }
            // else - its 2/3 option
            else {
                // and the specific name of student
                names.add(selectionArgs[Integer.parseInt(crsr.getString(col4)) - 1]);
            }
            crsr.moveToNext();
        }
        crsr.close();
        db.close();

        if (grade.size() != 0) {
            sortedView.setVisibility(View.VISIBLE);
            customAdp = new CustomGradesAdapter(getApplicationContext(),
                    quarter, grade, subject, names);
            sortedView.setAdapter(customAdp);
        }
        else {
            Toast.makeText(this, "No Grades for this student", Toast.LENGTH_SHORT).show();
            sortedView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Find students names into array list.
     *
     * @return the array list with the names
     */
    public ArrayList<String> findStudentsNames()
    {
        ArrayList<String> names = new ArrayList<>();

        db = hlp.getWritableDatabase();

        crsr = db.query(Students.TABLE_STUDENTS, new String[]{Students.NAME}, null, null, null, null, null);
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

    /**
     * The database editor - change the subject name
     *
     * @param parent The AbsListView where the click happened
     * @param view The view within the AbsListView that was clicked
     * @param position The position of the view in the list
     * @param id The row id of the item that was clicked
     *
     * @return true if the callback consumed the long click, false otherwise
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
        adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Change The Subject Name");
        final EditText input = new EditText(this);
        input.setHint("Type new name here");
        adb.setView(input);

        adb.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputString = input.getText().toString();

                if (!inputString.equals("")) {
                    // update the table with the new subject name
                    ContentValues cv = new ContentValues();
                    db = hlp.getWritableDatabase();
                    cv.put(Grades.Subject, inputString);
                    db.update(Grades.TABLE_GRADES, cv, Grades.KEY_ID+"=?", new String[]{idArr.get(position)});
                    db.close();

                    // notify the adp with the changes
                    subject.set(position, inputString);
                    customAdp.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(ShowDataActivity.this, "Subject must contains a letter...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();

        return true;
    }

    /**
     * The deleter of the database element - delete a grade
     *
     * @param parent The AdapterView where the click happened.
     * @param view The view within the AdapterView that was clicked (this
     *            will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Create the deleter dialog
        adb = new AlertDialog.Builder(this);

        adb.setTitle("More option");
        adb.setMessage("Are You Sure You Want To Delete This Grade?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete the selected column
                db = hlp.getWritableDatabase();
                db.delete(Grades.TABLE_GRADES, Grades.KEY_ID+"=?", new String[]{idArr.get(position)});
                db.close();

                // remove the grade from the lists and change the adp
                quarter.remove(position);
                grade.remove(position);
                subject.remove(position);
                names.remove(position);

                customAdp.notifyDataSetChanged();
            }
        });
        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();
    }

    /**
     * Create the options menu
     *
     * @param menu the menu
     * @return ture if success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
        else if (id == R.id.addGrade)
        {
            Intent si = new Intent(this, AddGrade.class);
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