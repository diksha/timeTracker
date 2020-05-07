package com.example.dikshag.timetracker;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class TimeTrackerDialogFragment extends AppCompatDialogFragment {
    private TimeTrackerNode timeTrackerNode;
    private ImageButton cancelDialog;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 1000;
    private TextView name;
    private TextView duration;
    private ImageButton startPause;
    private ImageButton stop;
    private long timePassed = 0;
    private long startTime;
    private boolean isStarted = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.timer_dialog, container, false);
        cancelDialog = view.findViewById(R.id.cancel);
        name = view.findViewById(R.id.task_name);
        duration = view.findViewById(R.id.duration);
        startPause = view.findViewById(R.id.start_pause_button);
        stop = view.findViewById(R.id.stop_button);
        name.setText(timeTrackerNode.getName());
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAndDismiss();
            }
        });
        startPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStarted = !isStarted;
                startPause.setImageDrawable(getContext().getResources().getDrawable
                        (isStarted ? R.drawable.ic_pause_black_18dp : R
                                .drawable.ic_play_arrow_black_18dp));
                if(!isStarted) {
                    timePassed += System.currentTimeMillis() - startTime;
                    handler.removeCallbacks(runnable);
                } else {
                    startHandler();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAndDismiss();
            }
        });
        return view;
    }

    private void stopAndDismiss() {
        if(isStarted) {
            timePassed += System.currentTimeMillis() - startTime;
            handler.removeCallbacks(runnable);
            isStarted = false;
        }
        timeTrackerNode.setTimeElapsed(timePassed);
        ((MainActivity)getContext()).notifyCurrentItemChanged(timeTrackerNode);
        TimeTrackerNode parent = timeTrackerNode.getParent();
        while(parent!=null) {
            ((MainActivity)getContext()).notifyCurrentItemChanged(parent);
            parent = parent.getParent();
        }
        dismiss();
    }

    public void init(final TimeTrackerNode timeTrackerNode) {
        this.timeTrackerNode = timeTrackerNode;
        startHandler();

    }

    private void startHandler() {
        startTime = System.currentTimeMillis();
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                duration.setText(getDurationFrom(timePassed + System.currentTimeMillis() - startTime));
            }
        }, delay);
    }

    private String getDurationFrom(long totalTimeInMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(totalTimeInMillis);
        String hoursString = hours<10? "0" + hours : String.valueOf(hours);
        totalTimeInMillis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeInMillis);
        String minuteString = minutes<10? "0" + minutes : String.valueOf(minutes);

        totalTimeInMillis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeInMillis);
        String secondString = seconds<10? "0" + seconds : String.valueOf(seconds);

        return hoursString + ":" + minuteString + ":" + secondString;
    }
}
