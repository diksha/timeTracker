package com.example.dikshag.timetracker;

import android.util.Log;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeTrackerNode implements Serializable {
    private String name;
    private boolean start;
    private long localTime;
    private long startLocalTime;
    private int height;
    private List<TimeTrackerNode> children = new ArrayList<>();
    private Map<LocalDate, Long> dateToTime = new HashMap<>();

    TimeTrackerNode(TimeTrackerNode parent, String name) {
        this.name = name;
        this.height = parent == null ? 0 : parent.getHeight() + 1;
    }

    public void start() {
        if (start == false) {
            startLocalTime = System.currentTimeMillis();
            start = true;
        }
    }

    public void pause() {
        if (start == true) {
            localTime += System.currentTimeMillis() - startLocalTime;
            start = false;
        }
    }

    public void stop() {
        if (start == true) {
            localTime += System.currentTimeMillis() - startLocalTime;
            //Store this somewhere;
            start = false;
        }
        LocalDate date = LocalDate.now();
        if(dateToTime.containsKey(date)) {
            dateToTime.put(date, dateToTime.get(date) + localTime);
        } else {
            dateToTime.put(date, localTime);
        }
        localTime = 0;
    }

    public int getHeight() {
        return height;
    }

    public void addChild(TimeTrackerNode node) {
        children.add(node);
    }

    public List<TimeTrackerNode> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        long totalTimeInMillis = dfsUtil(this);
        Log.i("TimeTracker", totalTimeInMillis + "time");
        if (totalTimeInMillis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(totalTimeInMillis);
        totalTimeInMillis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(totalTimeInMillis);
        totalTimeInMillis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeInMillis);
        totalTimeInMillis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeInMillis);

        return days + "d " + hours + "h " + minutes + "m " + seconds + "s ";
    }

    private long dfsUtil(TimeTrackerNode rootTimeTrackerNode) {
        long timeReturned = 0;
        for (TimeTrackerNode timeTrackerNode : rootTimeTrackerNode.getChildren()) {
            timeReturned += dfsUtil(timeTrackerNode);
        }
        return rootTimeTrackerNode.localTime + timeReturned;
    }
}
