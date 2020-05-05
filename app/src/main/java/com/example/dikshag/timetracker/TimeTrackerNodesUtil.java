package com.example.dikshag.timetracker;

import java.util.ArrayList;
import java.util.List;

public class TimeTrackerNodesUtil {

    public static List<TimeTrackerNode> getTimeTrackerRecords(TimeTrackerNode rootTimeTrackerNode) {
        List<TimeTrackerNode> timeTrackerNodes = new ArrayList<>();
        dfsUtil(timeTrackerNodes, rootTimeTrackerNode);
        return timeTrackerNodes;
    }

    private static void dfsUtil(List<TimeTrackerNode> timeTrackerNodes, TimeTrackerNode
            rootTimeTrackerNode) {
        timeTrackerNodes.add(rootTimeTrackerNode);
        if(rootTimeTrackerNode.isExpanded()) {
            for (TimeTrackerNode timeTrackerNode : rootTimeTrackerNode.getChildren()) {
                dfsUtil(timeTrackerNodes, timeTrackerNode);
            }
        }
    }
}
