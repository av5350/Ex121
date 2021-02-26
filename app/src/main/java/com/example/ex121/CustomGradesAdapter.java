package com.example.ex121;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 *  @author Itay Weintraub <av5350@bs.amalnet.k12.il>
 *  @version 1
 *  @since 6.01.2021
 *  The type Custom grades adapter.
 */
public class CustomGradesAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    ArrayList<String> quarter, grade, subject, names;

    /**
     * Instantiates a new Custom grades adapter.
     *
     * @param applicationContext the application context
     * @param quarter            the quarter
     * @param grade              the grade
     * @param subject            the subject
     * @param names               the names
     */
    public CustomGradesAdapter(Context applicationContext, ArrayList<String> quarter, ArrayList<String> grade, ArrayList<String> subject,
                               ArrayList<String> names) {
        this.context = context;
        this.quarter = quarter;
        this.grade = grade;
        this.subject = subject;
        this.names = names;
        inflter = (LayoutInflater.from(applicationContext));
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return subject.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param i Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int i) {
        return subject.get((i));
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param i The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param i The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param view The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param viewGroup The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.grades_lv_layout, null);

        TextView quarterTv = (TextView) view.findViewById(R.id.quarterValue);
        TextView gradeTv = (TextView) view.findViewById(R.id.gradeValue);
        TextView subjectTv = (TextView) view.findViewById(R.id.subjectValue);
        TextView nameTv = (TextView) view.findViewById(R.id.nameValue);

        quarterTv.setText("Quarter: " + quarter.get(i));
        gradeTv.setText("Grade: " + grade.get(i));
        subjectTv.setText(subject.get(i));
        nameTv.setText("Name: " + names.get(i));

        return view;
    }
}
