package com.vuduc.android.worksoptimization.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class TaskContent {

    private static final String TAG = TaskContent.class.getSimpleName();

    public static ArrayList<TaskItem> ITEMS = new ArrayList<TaskItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<Long, TaskItem> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    public static void addItem(TaskItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void deleteItem(long id) {
       TaskItem deleteItem = null;
        for (TaskItem item : ITEMS) {
            if (item.id == id) {
                deleteItem = item;
                break;
            }
        }

        if (deleteItem != null)
            ITEMS.remove(deleteItem);

        ITEM_MAP.remove(id);
    }

    private static TaskItem createDummyItem(long position) {
        return new TaskItem(position, "Item " + position, makeDetails(position), 36000000l, 1444150800000l);
    }

    private static String makeDetails(long position) {
        return "Details about Item: " + position;
    }

    /**
     * A dummy item representing a piece of name.
     */
    public static class TaskItem {
        public Long id;
        public String name;
        public String details;
        public Long estimateTime;
        public Long deadline;

        public TaskItem() {

        }

        public TaskItem(Long id, String name, String details, Long estimateTime, Long deadline) {
            this.id = id;
            this.name = name;
            this.details = details;
            this.estimateTime = estimateTime;
            this.deadline = deadline;
        }

        public TaskItem(Long id, String content, String details) {
            this.id = id;
            this.name = content;
            this.details = details;
            this.estimateTime = 0l;
            this.deadline = 0l;
        }

        public TaskItem(long id) {
            this.id = id;
            this.estimateTime = 0l;
            this.deadline = System.currentTimeMillis();
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
