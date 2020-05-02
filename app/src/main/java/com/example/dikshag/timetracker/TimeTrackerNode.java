package com.example.dikshag.timetracker;

import java.util.ArrayList;
import java.util.List;

public class TimeTrackerNode {
    String name;
    boolean start;
    long localTime;
    long startLocalTime;
    int height;
    List<TimeTrackerNode> children = new ArrayList<>();

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
            localTime = 0;
            start = false;
        }
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
}
