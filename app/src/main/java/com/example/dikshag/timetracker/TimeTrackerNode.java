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
    private boolean start = false;
    private long localTime;
    private long startLocalTime;
    private int height;
    private TimeTrackerNode parent;
    private List<TimeTrackerNode> children = new ArrayList<>();
    private Map<LocalDate, Long> dateToTime = new HashMap<>();
    private boolean isExpanded = true;

    // Non serializable objects.
    transient private TimeChangeListener timeChangeListener;


    TimeTrackerNode(TimeTrackerNode parent, String name) {
        this.name = name;
        this.height = parent == null ? 0 : parent.getHeight() + 1;
        this.parent = parent;
    }

    public void delete() {
        parent.removeChild(this);
    }

    public void removeChild(TimeTrackerNode node) {
        this.children.remove(node);
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
            if (timeChangeListener != null)
                timeChangeListener.onTimeChanged();
            start = false;
        }
    }

    public long stop() {
        // To get total time add the time in children as well.
        long totalTimeWithChildren = 0;
        for (TimeTrackerNode timeTrackerNode : this.getChildren()) {
            totalTimeWithChildren += timeTrackerNode.stop();
        }
        if (start == true) {
            localTime += System.currentTimeMillis() - startLocalTime;
            //Store this somewhere;
            start = false;
        }
        totalTimeWithChildren += localTime;
        LocalDate date = LocalDate.now();
        if (dateToTime.containsKey(date)) {
            dateToTime.put(date, dateToTime.get(date) + totalTimeWithChildren);
        } else {
            dateToTime.put(date, totalTimeWithChildren);
        }
        localTime = 0;
        if (timeChangeListener != null)
            timeChangeListener.onTimeChanged();
        return totalTimeWithChildren;
    }

    public boolean isStarted() {
        return start;
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

    public boolean isExpanded() {
        return isExpanded;
    }

    public void toggleExpanded() {
        isExpanded = !isExpanded();
    }

    public void toggleStartPause() {
        if (start) {
            pause();
        } else {
            start();
        }
    }

    public String getDurationForDate(LocalDate date) {
        long totalTimeInMillis = dfsUtilForDate(this, date);
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

    private long dfsUtilForDate(TimeTrackerNode rootTimeTrackerNode, LocalDate date) {
        long timeReturned = 0;
        for (TimeTrackerNode timeTrackerNode : rootTimeTrackerNode.getChildren()) {
            timeReturned += dfsUtil(timeTrackerNode);
        }
        return rootTimeTrackerNode.dateToTime.containsKey(date) ? rootTimeTrackerNode.dateToTime
                .get(date) + timeReturned : timeReturned;
    }

    public void resetName(String text) {
        this.name = text;

    }

    public void addListener(TimeChangeListener timeChangeListener) {
        this.timeChangeListener = timeChangeListener;
    }

    public void setTime() {
        localTime += System.currentTimeMillis() - startLocalTime;
        startLocalTime = System.currentTimeMillis();
        if (timeChangeListener != null)
            timeChangeListener.onTimeChanged();
    }
}
