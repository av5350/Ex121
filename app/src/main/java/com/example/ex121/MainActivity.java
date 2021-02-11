package com.example.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    HelperDB hlp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
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
     * go to credits activity if it was clicked at the menu
     *
     * @param item the item in menu that was clicked
     * @return true - if it success
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // TODO: FILL IT - AND THE JAVADOC
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
        else if (id == R.id.sort)
        {
            Intent si = new Intent(this, ShowDataActivity.class);
            startActivity(si);
        }

        return true;
    }
}