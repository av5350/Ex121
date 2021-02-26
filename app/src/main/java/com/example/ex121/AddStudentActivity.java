package com.example.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 *  @author Itay Weintraub <av5350@bs.amalnet.k12.il>
 *  @version 1
 *  @since 6.01.2021
 *  The type Add student activity.
 */
public class AddStudentActivity extends AppCompatActivity {
    EditText studentName, studentPhone, dadName, dadPhone,
            momName, momPhone, homePhone, homeAddress;
    SQLiteDatabase db;
    HelperDB hlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        studentName = (EditText) findViewById(R.id.studentName);
        studentPhone = (EditText) findViewById(R.id.studentPhone);
        dadName = (EditText) findViewById(R.id.dadName);
        dadPhone = (EditText) findViewById(R.id.dadPhone);
        momName = (EditText) findViewById(R.id.momName);
        momPhone = (EditText) findViewById(R.id.momPhone);
        homePhone = (EditText) findViewById(R.id.homePhone);
        homeAddress = (EditText) findViewById(R.id.homeAddress);
    }

    /**
     * Gets data (include name, phone, address etc)
     *
     * @param view the view
     */
    public void getData(View view) {
        String studentNameString = studentName.getText().toString();
        String studentPhoneString = studentPhone.getText().toString();
        String dadNameString = dadName.getText().toString();
        String dadPhoneString = dadPhone.getText().toString();
        String momNameString = momName.getText().toString();
        String momPhoneString = momPhone.getText().toString();
        String homePhoneString = homePhone.getText().toString();
        String homeAddressString = homeAddress.getText().toString();

        if (!checkAlphabetic(studentNameString) || studentNameString.equals(""))
        {
            Toast.makeText(this, "Name must contains only letters!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // insert the data to the table
            ContentValues cv = new ContentValues();

            cv.put(Students.NAME, studentNameString);
            cv.put(Students.PHONE, ((studentPhoneString.equals("")) ? null : Integer.parseInt(studentPhoneString)));
            cv.put(Students.HOME_PHONE, ((homePhoneString.equals("")) ? null : Integer.parseInt(homePhoneString)));
            cv.put(Students.DAD_NAME, dadNameString);
            cv.put(Students.DAD_PHONE, ((dadPhoneString.equals("")) ? null : Integer.parseInt(dadPhoneString)));
            cv.put(Students.MOM_NAME, momNameString);
            cv.put(Students.MOM_PHONE, ((momPhoneString.equals("")) ? null : Integer.parseInt(momPhoneString)));
            cv.put(Students.ADDRESS, homeAddressString);

            hlp = new HelperDB(this);
            db = hlp.getWritableDatabase();
            db.insert(Students.TABLE_STUDENTS, null, cv);
            db.close();

            Toast.makeText(this, "Student was added!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Check if a string is full Alphabetic
     *
     * @param input the input
     * @return  if a string is full Alphabetic
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

        // go to add Grade activity if clicked
        if (id == R.id.addGrade)
        {
            Intent si = new Intent(this, AddGrade.class);
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