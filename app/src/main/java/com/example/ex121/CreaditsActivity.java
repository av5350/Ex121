package com.example.ex121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The type Credits activity.
 * @author Itey Weintraub <av5350@bs.amalnet.k12.il>
 * @version	1
 * @since 6.01.2021
 * short description:
 * 
 *      This activity shows the credits of the app :)
 */
public class CreaditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creadits);
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
        else if (id == R.id.sort)
        {
            Intent si = new Intent(this, ShowDataActivity.class);
            startActivity(si);
        }

        return true;
    }
}