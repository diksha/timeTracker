package com.example.dikshag.timetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private TimeTrackerNode rootTreeNode;
    private RecyclerView recyclerView;
    private TimeTrackerResultsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rootTreeNode = (TimeTrackerNode) getIntent().getExtras().get("TreeNode");
        recyclerView = findViewById(R.id.time_tracker_result_nodes);
        dateButton = findViewById(R.id.date_button);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new TimeTrackerResultsAdapter(TimeTrackerNodesUtil.getTimeTrackerRecords(rootTreeNode));
        recyclerView.setAdapter(mAdapter);
        // Devise a good way of showing time spent.

        dateButton.setText("Date: " + LocalDate.now().toString());
        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar cldr = Calendar.getInstance();
                        int day = cldr.get(Calendar.DAY_OF_MONTH);
                        int month = cldr.get(Calendar.MONTH);
                        int year = cldr.get(Calendar.YEAR);
                        // date picker dialog
                        DatePickerDialog picker = new DatePickerDialog(StatisticsActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        LocalDate dateSet = LocalDate.of(year, monthOfYear+1, dayOfMonth);
                                        dateButton.setText("Date: " + dateSet.toString());
                                        mAdapter.dateChanged(dateSet);
                                        notifyDataSetChanged();
                                    }
                                }, year, month, day);
                        picker.show();
                    }
                }
        );
    }

    public void notifyDataSetChanged() {
        List<TimeTrackerNode> timeTrackerNodes = TimeTrackerNodesUtil
                .getTimeTrackerRecords(rootTreeNode);
        mAdapter.dataSetChanged(timeTrackerNodes);
        mAdapter.notifyDataSetChanged();
    }

}
