package com.example.dikshag.timetracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private TimeTrackerNode rootTreeNode;
    private RecyclerView recyclerView;
    private TimeTrackerResultsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new TimeTrackerResultsAdapter(TimeTrackerNodesUtil.getTimeTrackerRecords(rootTreeNode));
        recyclerView.setAdapter(mAdapter);
        // Devise a good way of showing time spent.
    }

    public void notifyDataSetChanged() {
        List<TimeTrackerNode> timeTrackerNodes = TimeTrackerNodesUtil
                .getTimeTrackerRecords(rootTreeNode);
        mAdapter.dataSetChanged(timeTrackerNodes);
        mAdapter.notifyDataSetChanged();
    }

}
