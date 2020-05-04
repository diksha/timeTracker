package com.example.dikshag.timetracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimeTrackerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TimeTrackerNode rootTimeTrackerNode;
    private List<TimeTrackerNode> timeTrackerNodes;
//    private Button statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        statistics = findViewById(R.id.statistics);
        recyclerView = findViewById(R.id.time_tracker_nodes);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        init();
        // specify an adapter (see also next example)
        mAdapter = new TimeTrackerAdapter(getTimeTrackerRecords());
        recyclerView.setAdapter(mAdapter);
//        statistics.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("TreeNode", timeTrackerNodes.get(0));
//                Intent intent = new Intent(StatisticsActivity.class);
//                intent.putExtras(bundle);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeTrackerNodes != null) {
            try {
                for (TimeTrackerNode timeTrackerNode : timeTrackerNodes) {
                    timeTrackerNode.stop();
                }
                FileOutputStream file = getApplicationContext().openFileOutput("someFile",
                        Context.MODE_PRIVATE);
                ObjectOutputStream out = new ObjectOutputStream
                        (file);
                out.writeObject(timeTrackerNodes.get(0));
                out.flush();
                out.close();

            } catch (Exception e) {
                Log.e("dikshag", "Exception found");
            }
        }
    }

    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    private List<TimeTrackerNode> getTimeTrackerRecords() {
        List<TimeTrackerNode> timeTrackerNodes = new ArrayList<>();
        dfsUtil(timeTrackerNodes, rootTimeTrackerNode);
        this.timeTrackerNodes = timeTrackerNodes;
        return timeTrackerNodes;
    }

    private void dfsUtil(List<TimeTrackerNode> timeTrackerNodes, TimeTrackerNode
            rootTimeTrackerNode) {
        timeTrackerNodes.add(rootTimeTrackerNode);
        for (TimeTrackerNode timeTrackerNode : rootTimeTrackerNode.getChildren()) {
            dfsUtil(timeTrackerNodes, timeTrackerNode);
        }
    }


    private void init() {
        if (fileExists(getApplicationContext(), "someFile")) {
            try {
                FileInputStream fis = getApplicationContext().openFileInput("someFile");
                ObjectInputStream is = new ObjectInputStream(fis);
                rootTimeTrackerNode = (TimeTrackerNode) is.readObject();
            } catch (Exception e) {
                Log.i("dikshag", "File not found with exception " + e);
            }
        }
        if (rootTimeTrackerNode == null) {
            rootTimeTrackerNode = new TimeTrackerNode(null, "");
        }
    }


    public void notifyDataSetChanged() {
        mAdapter.dataSetChanged(getTimeTrackerRecords());
        mAdapter.notifyDataSetChanged();
    }
}
