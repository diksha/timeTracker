package com.example.dikshag.timetracker;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TimeTrackerNode rootTimeTrackerNode;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.time_tracker_nodes);
        button = findViewById(R.id.add_button);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        init();
        // specify an adapter (see also next example)
        mAdapter = new TimeTrackerAdapter(getTimeTrackerRecords());
        recyclerView.setAdapter(mAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment createNewFragment = new CreateNewDialogFragment();
                createNewFragment.show(getSupportFragmentManager(), "createNewDialog");
            }
        });

    }

    private List<TimeTrackerNode> getTimeTrackerRecords() {
        List<TimeTrackerNode> timeTrackerNodes = new ArrayList<>();
        dfsUtil(timeTrackerNodes, rootTimeTrackerNode);
        return timeTrackerNodes;
    }

    private void dfsUtil(List<TimeTrackerNode> timeTrackerNodes, TimeTrackerNode rootTimeTrackerNode) {
        timeTrackerNodes.add(rootTimeTrackerNode);
        for (TimeTrackerNode timeTrackerNode : rootTimeTrackerNode.getChildren()) {
            dfsUtil(timeTrackerNodes, timeTrackerNode);
        }
    }


    private void init() {
        rootTimeTrackerNode = new TimeTrackerNode(null, "");
        rootTimeTrackerNode.addChild(new TimeTrackerNode(rootTimeTrackerNode, "Work"));
    }


}
