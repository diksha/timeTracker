package com.example.dikshag.timetracker;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class TimeTrackerAdapter extends RecyclerView.Adapter<TimeTrackerAdapter.MyViewHolder> {
    private List<TimeTrackerNode> timeTrackerRecords;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements TimeChangeListener {
        private final LinearLayout tableTrackerTableView;
        private final LinearLayout timeButtonsView;
        private final ImageButton addButton;
        private final View rootView;
        private final Context context;
        private TimeTrackerNode timeTrackerNode;

        public MyViewHolder(View v) {
            super(v);
            tableTrackerTableView = v.findViewById(R.id.time_tracker_table);
            timeButtonsView = v.findViewById(R.id.time_buttons);
            addButton = v.findViewById(R.id.add_button);
            rootView = v;
            context = tableTrackerTableView.getContext();

        }

        public void setView(final TimeTrackerNode timeTrackerNode) {
            this.timeTrackerNode = timeTrackerNode;
            timeTrackerNode.addListener(this);
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
                    ((MainActivity) context).notifyDataSetChanged();
                }
            });
            final EditText timeTrackerNameTextView = rootView.findViewById(R.id.time_tracker_name);
            if (timeTrackerNode.getHeight() != 0) {
                timeTrackerNameTextView.setText(timeTrackerNode.getName());
            } else {
                timeTrackerNameTextView.setVisibility(View.GONE);
            }
            timeTrackerNameTextView.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        timeTrackerNode.resetName(timeTrackerNameTextView.getText().toString());
                        ((MainActivity)context).notifyCurrentItemChanged(timeTrackerNode);
                        return true;
                    }
                    return false;
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CreateNewDialogFragment createNewFragment = new CreateNewDialogFragment();
                    createNewFragment.initTimeTrackerNode(timeTrackerNode);
                    createNewFragment.show(((FragmentActivity) context).getSupportFragmentManager
                            (), "createNewDialog");
                }
            });
            if (timeTrackerNode.getHeight() != 0) {
                TextView durationTextView = rootView.findViewById(R.id.duration);
                durationTextView.setText(timeTrackerNode.getDuration());

                ImageButton startPauseButton = rootView.findViewById(R.id.start_pause_button);
                startPauseButton.setImageDrawable(context.getResources().getDrawable
                        (timeTrackerNode.isStarted() ? R.drawable.ic_pause_black_18dp : R
                                .drawable.ic_play_arrow_black_18dp));
                startPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeTrackerNode.toggleStartPause();
                        ((MainActivity) context).notifyDataSetChanged();
                    }
                });
                ImageButton stopButton = rootView.findViewById(R.id.stop_button);

                stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeTrackerNode.stop();
                        ((MainActivity) context).notifyDataSetChanged();

                    }
                });

                ImageButton deleteButton = rootView.findViewById(R.id.clear_button);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeTrackerNode.delete();
                        ((MainActivity) context).notifyDataSetChanged();
                    }
                });
            } else {
                timeButtonsView.setVisibility(View.GONE);
            }
        }


        @Override
        public void onTimeChanged() {
            ((MainActivity)context).notifyCurrentItemChanged(this.timeTrackerNode);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TimeTrackerAdapter(List<TimeTrackerNode> timeTrackerRecords) {
        this.timeTrackerRecords = timeTrackerRecords;
    }

    public void dataSetChanged(List<TimeTrackerNode> timeTrackerRecords) {
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