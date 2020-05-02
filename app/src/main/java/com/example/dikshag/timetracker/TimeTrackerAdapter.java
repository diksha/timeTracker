package com.example.dikshag.timetracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TimeTrackerAdapter extends RecyclerView.Adapter<TimeTrackerAdapter.MyViewHolder> {
    private List<TimeTrackerNode> timeTrackerRecords;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout tableTrackerTableView;

        public MyViewHolder(View v) {
            super(v);
            tableTrackerTableView = v.findViewById(R.id.time_tracker_table);
        }

        public void setView(TimeTrackerNode timeTrackerNode) {
            Context context = tableTrackerTableView.getContext();
            for (int i = 0; i < timeTrackerNode.getHeight(); i++) {
                LinearLayout spaceLayout = new LinearLayout(tableTrackerTableView.getContext());
                spaceLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getResources()
                        .getDimensionPixelSize(R.dimen.width_empty), ViewGroup.LayoutParams
                        .WRAP_CONTENT));
                tableTrackerTableView.addView(spaceLayout);
            }
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tableTrackerTableView.addView(textView);
            textView.setText(timeTrackerNode.getName());


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TimeTrackerAdapter(List<TimeTrackerNode> timeTrackerRecords) {
        this.timeTrackerRecords = timeTrackerRecords;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TimeTrackerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_tracker_record, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TimeTrackerNode timeTrackerNode = timeTrackerRecords.get(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.setView(timeTrackerNode);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return timeTrackerRecords.size();
    }
}