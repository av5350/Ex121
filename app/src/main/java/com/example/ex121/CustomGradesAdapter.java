package com.example.ex121;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomGradesAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    ArrayList<String> quarter, grade, subject, names;

    public CustomGradesAdapter(Context applicationContext, ArrayList<String> quarter, ArrayList<String> grade, ArrayList<String> subject,
                               ArrayList<String> names) {
        this.context = context;
        this.quarter = quarter;
        this.grade = grade;
        this.subject = subject;
        this.names = names;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return subject.size();
    }

    @Override
    public Object getItem(int i) {
        return subject.get((i));
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

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
