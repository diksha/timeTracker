package com.example.dikshag.timetracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimeTrackerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TimeTrackerNode rootTimeTrackerNode;
    private List<TimeTrackerNode> timeTrackerNodes;
    private int delay = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.time_tracker_nodes);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        init();
        this.timeTrackerNodes = TimeTrackerNodesUtil.getTimeTrackerRecords(rootTimeTrackerNode);
        mAdapter = new TimeTrackerAdapter(this.timeTrackerNodes);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.statistics) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("TreeNode", timeTrackerNodes.get(0));
            Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeTrackerNodes != null) {
            try {
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


    private void init() {
        if (fileExists(getApplicationContext(), "someFile")) {
            try {
                FileInputStream fis = getApplicationContext().openFileInput("someFile");
                ObjectInputStream is = new ObjectInputStream(fis);
                rootTimeTrackerNode = (TimeTrackerNode) is.readObject();
                fis.close();
            } catch (Exception e) {
                Log.i("dikshag", "File not found with exception " + e);
            }
        }
        if (rootTimeTrackerNode == null) {
            rootTimeTrackerNode = new TimeTrackerNode(null, "");
        }
    }


    public void notifyDataSetChanged() {
        this.timeTrackerNodes = TimeTrackerNodesUtil.getTimeTrackerRecords(rootTimeTrackerNode);
        mAdapter.dataSetChanged(this.timeTrackerNodes);
        mAdapter.notifyDataSetChanged();
    }

    public void notifyCurrentItemChanged(TimeTrackerNode timeTrackerNode) {
        mAdapter.notifyItemChanged(timeTrackerNodes.indexOf(timeTrackerNode));
    }

    public void notifyItemInsertedOrDeleted() {
        List<TimeTrackerNode> newTimeTrackerNodes = TimeTrackerNodesUtil.getTimeTrackerRecords
                (rootTimeTrackerNode);
        mAdapter.dataSetChanged(newTimeTrackerNodes);
        int newPosition = 0;
        if (newTimeTrackerNodes.size() > timeTrackerNodes.size()) {
            // itemInserted
            while (newPosition < timeTrackerNodes.size() && newTimeTrackerNodes.get(newPosition)
                    == timeTrackerNodes.get(newPosition)) {
                newPosition++;
            }
            mAdapter.notifyItemRangeInserted(newPosition, newTimeTrackerNodes.size() -
                    timeTrackerNodes.size());
        } else {
            // itemDeleted
            while (newPosition < newTimeTrackerNodes.size() && newTimeTrackerNodes.get
                    (newPosition) == timeTrackerNodes.get(newPosition)) {
                newPosition++;
            }
            mAdapter.notifyItemRangeRemoved(newPosition, timeTrackerNodes.size() -
                    newTimeTrackerNodes.size());
        }
        this.timeTrackerNodes = newTimeTrackerNodes;
    }
}
