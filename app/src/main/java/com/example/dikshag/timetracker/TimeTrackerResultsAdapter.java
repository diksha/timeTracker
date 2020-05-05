package com.example.dikshag.timetracker;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TimeTrackerResultsAdapter extends RecyclerView.Adapter<TimeTrackerResultsAdapter.MyViewHolder> {
    private List<TimeTrackerNode> timeTrackerRecords;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout tableTrackerTableView;
        private final LinearLayout timeButtonsView;
        private final ImageButton addButton;
        private final View rootView;

        public MyViewHolder(View v) {
            super(v);
            tableTrackerTableView = v.findViewById(R.id.time_tracker_table);
            timeButtonsView = v.findViewById(R.id.time_buttons);
            addButton = v.findViewById(R.id.add_button);
            rootView = v;
        }

        public void setView(final TimeTrackerNode timeTrackerNode) {
            final Context context = tableTrackerTableView.getContext();
            tableTrackerTableView.removeAllViews();
            for (int i = 0; i < timeTrackerNode.getHeight(); i++) {
                LinearLayout spaceLayout = new LinearLayout(tableTrackerTableView.getContext());
                spaceLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getResources()
                        .getDimensionPixelSize(R.dimen.width_empty), ViewGroup.LayoutParams
                        .WRAP_CONTENT));
                tableTrackerTableView.addView(spaceLayout);
            }
            ImageButton expandCollapseButton = rootView.findViewById(R.id.expand_collapse);
            expandCollapseButton.setImageDrawable(context.getResources().getDrawable
                    (timeTrackerNode.isExpanded() ? R.drawable.ic_expand_more_black_18dp : R
                            .drawable.ic_chevron_right_black_18dp));
            expandCollapseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeTrackerNode.toggleExpanded();
                    ((StatisticsActivity) context).notifyDataSetChanged();
                }
            });
            TextView timeTrackerNameTextView = rootView.findViewById(R.id.time_tracker_name);
            if (timeTrackerNode.getHeight() != 0) {
                timeTrackerNameTextView.setText(timeTrackerNode.getName());
            } else {
                timeTrackerNameTextView.setVisibility(View.GONE);
            }
            if (timeTrackerNode.getHeight() != 0) {
                TextView durationTextView = rootView.findViewById(R.id.duration);
                durationTextView.setText(timeTrackerNode.getDuration());
            } else {
                timeButtonsView.setVisibility(View.GONE);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TimeTrackerResultsAdapter(List<TimeTrackerNode> timeTrackerRecords) {
        this.timeTrackerRecords = timeTrackerRecords;
    }

    public void dataSetChanged(List<TimeTrackerNode> timeTrackerRecords) {
        this.timeTrackerRecords = timeTrackerRecords;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TimeTrackerResultsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_tracker_results_record, parent, false);
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