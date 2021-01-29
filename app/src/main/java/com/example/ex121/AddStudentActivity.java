package com.example.ex121;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
        // Phone number must be 10 digits or 0!
        else if ((((studentPhoneString.length() != 10) && (!studentPhoneString.isEmpty())) ||
                ((homePhoneString.length() != 10) && (!homePhoneString.isEmpty())) ||
                ((dadPhoneString.length() != 10) && (!dadPhoneString.isEmpty())) ||
                (momPhoneString.length() != 10)) && (!momPhoneString.isEmpty()))
        {
            Toast.makeText(this, "Phone number must to be 10 digits", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ContentValues cv = new ContentValues();

            cv.put(Students.NAME, studentNameString);
            cv.put(Students.PHONE, ((studentPhoneString.equals("")) ? null : Integer.parseInt(studentPhoneString)));
            cv.put(Students.HOME_PHONE, ((homePhoneString.equals("")) ? null : Integer.parseInt(homePhoneString)));
            cv.put(Students.DAD_NAME, dadNameString);
            cv.put(Students.DAD_PHONE, ((dadPhoneString.equals("")) ? null : Integer.parseInt(dadPhoneString)));
            cv.put(Students.MOM_NAME, momNameString);
            cv.put(Students.MOM_PHONE, ((momPhoneString.equals("")) ? null : Integer.parseInt(momPhoneString)));
            cv.put(Students.ADDRESS, homeAddressString);
            cv.put(Students.IS_ACTIVE, 1); // true

            hlp = new HelperDB(this);
            db = hlp.getWritableDatabase();
            db.insert(Students.TABLE_STUDENTS, null, cv);
            db.close();
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
}