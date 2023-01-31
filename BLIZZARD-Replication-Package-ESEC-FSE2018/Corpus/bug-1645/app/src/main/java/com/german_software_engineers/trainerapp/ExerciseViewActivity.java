package com.german_software_engineers.trainerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.german_software_engineers.trainerapp.Model.Exercise;
import com.german_software_engineers.trainerapp.Model.Schedule;
import com.german_software_engineers.trainerapp.Model.ScheduleAvailableException;

public class ExerciseViewActivity extends AppCompatActivity implements ExcersizeListFragment.OnListFragmentInteractionListener {

    String ScheduleName;
    ExcersizeListFragment fragment;
    Schedule ActiveSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ScheduleName = intent.getStringExtra("scheduleName");

        try {
            ActiveSchedule = ApplicationHandler.getModel().getSchedule(ScheduleName);
        } catch (ScheduleAvailableException e) {
            e.printStackTrace();
        }

        setTitle(ActiveSchedule.getName());

        fragment = ExcersizeListFragment.newInstance(1,ScheduleName);
        getSupportFragmentManager().beginTransaction().replace(R.id.execView, fragment ).commit();

        TextView ScheduleInfo = (TextView)findViewById(R.id.ScheduleInfo);
        ScheduleInfo.setText("Repeations: "+ActiveSchedule.getRepeations()+"\nPause: "+ActiveSchedule.getPauseTime()+"\nSets: "+ ActiveSchedule.getSets()+"\nSpeed: "+ActiveSchedule.getSpeed());

        TextView WarmUpInfo = (TextView)findViewById(R.id.WarmUpInfo);
        WarmUpInfo.setText("Exercise: "+ActiveSchedule.getWarmUpExcersize()+"\nTime: "+ActiveSchedule.getWarmUpTime()+"\nIntensity: "+ ActiveSchedule.getWarmUpIntensity().toString());
    }

    @Override
    public void onListFragmentInteraction(Exercise item) {

    }
}
