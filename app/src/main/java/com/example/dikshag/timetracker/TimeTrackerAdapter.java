package com.example.dikshag.timetracker;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
        private final LinearLayout timeButtonsView;
        public MyViewHolder(View v) {
            super(v);
            tableTrackerTableView = v.findViewById(R.id.time_tracker_table);
            timeButtonsView = v.findViewById(R.id.time_buttons);
        }

        public void setView(final TimeTrackerNode timeTrackerNode) {
            final Context context = tableTrackerTableView.getContext();
            tableTrackerTableView.removeAllViews();
            timeButtonsView.removeAllViews();
            for (int i = 0; i < timeTrackerNode.getHeight() - 1; i++) {
                LinearLayout spaceLayout = new LinearLayout(tableTrackerTableView.getContext());
                spaceLayout.setLayoutParams(new LinearLayout.LayoutParams(context.getResources()
                        .getDimensionPixelSize(R.dimen.width_empty), ViewGroup.LayoutParams
                        .WRAP_CONTENT));
                tableTrackerTableView.addView(spaceLayout);
            }
            if (timeTrackerNode.getHeight() != 0) {
                TextView textView = new TextView(context);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                        .WRAP_CONTENT, ViewGroup.LayoutParams
                        .WRAP_CONTENT));
                textView.setText(timeTrackerNode.getName());
                tableTrackerTableView.addView(textView);
            }

            ImageButton addButton = new ImageButton(context);
            addButton.setBackground(context.getResources().getDrawable(R.drawable
                    .ic_add_black_18dp));
            addButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                    .WRAP_CONTENT, ViewGroup.LayoutParams
                    .MATCH_PARENT));
            tableTrackerTableView.addView(addButton);
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
                TextView textView = new TextView(context);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                        .WRAP_CONTENT, ViewGroup.LayoutParams
                        .WRAP_CONTENT));
                textView.setText(timeTrackerNode.getDuration());
                timeButtonsView.addView(textView);

                Button startButton = new Button(context);
                startButton.setTextSize(12);
                startButton.setText("Start");
                startButton.setBackgroundColor(0x000000);
                startButton.setTextColor(Color.BLUE);
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeTrackerNode.start();
                    }
                });
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        context.getResources().getDimensionPixelSize(R.dimen.button_width), context
                        .getResources().getDimensionPixelSize(R.dimen.button_height));
                startButton.setLayoutParams(params);
                timeButtonsView.addView(startButton);
                Button pauseButton = new Button(context);
                pauseButton.setTextSize(12);
                pauseButton.setText("Pause");
                pauseButton.setBackgroundColor(0x000000);
                pauseButton.setTextColor(Color.BLUE);
                pauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeTrackerNode.pause();
                        ((MainActivity) context).notifyDataSetChanged();

                    }
                });
                params = new ViewGroup.LayoutParams(
                        context.getResources().getDimensionPixelSize(R.dimen.button_width), context
                        .getResources().getDimensionPixelSize(R.dimen.button_height));
                pauseButton.setLayoutParams(params);
                timeButtonsView.addView(pauseButton);
                Button stopButton = new Button(context);
                stopButton.setText("Stop");
                stopButton.setTextSize(12);
                stopButton.setBackgroundColor(0x000000);
                stopButton.setTextColor(Color.BLUE);
                stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeTrackerNode.stop();
                        ((MainActivity) context).notifyDataSetChanged();

                    }
                });
                params = new ViewGroup.LayoutParams(
                        context.getResources().getDimensionPixelSize(R.dimen.button_width), context
                        .getResources().getDimensionPixelSize(R.dimen.button_height));
                stopButton.setLayoutParams(params);
                timeButtonsView.addView(stopButton);

                ImageButton deleteButton = new ImageButton(context);
                deleteButton.setBackground(context.getResources().getDrawable(R.drawable
                        .ic_cancel_black_18dp));
                deleteButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                        .WRAP_CONTENT, ViewGroup.LayoutParams
                        .MATCH_PARENT));
                timeButtonsView.addView(deleteButton);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeTrackerNode.delete();
                        ((MainActivity) context).notifyDataSetChanged();
                    }
                });
            }
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