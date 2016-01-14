package com.vuduc.android.worksoptimization.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class TaskContent {

    private static final String TAG = TaskContent.class.getSimpleName();

    public static ArrayList<TaskItem> ITEMS = new ArrayList<TaskItem>();

    public static Map<Long, TaskItem> ITEM_MAP = new HashMap<>();

    public static int mNumberSuccessfulItems = 0;

    // TODO: Test values
    static {
        long current = System.currentTimeMillis();

        long[] values = new long[] { 1, 9, 1, 3, 15, 4 };
        int[] estimateTimes = new int[] { 2, 4, 1, 2, 3, 1 };
        int[] deadlines = new int[] { 3, 5, 6, 6, 7, 8 };

//        int[] estimateTimes = new int[] { 3, 1, 5, 3, 2 };
//        int[] deadlines = new int[] { 6, 3, 10, 7, 4 };

        for (int i = 0; i < estimateTimes.length; i++) {
            addItem(new TaskItem((long)i, "Công việc " + (i + 1), "Nội dung công việc " + (i + 1), estimateTimes[i] * 60 * 60 * 1000l, current + deadlines[i] * 60 * 60 * 1000l, values[i]));
        }
    }

    public static void setItems(ArrayList<TaskItem> items) {
        ITEMS.clear();
        ITEMS.addAll(items);
        ITEM_MAP.clear();
        for (TaskItem item : items) {
            ITEM_MAP.put(item.id, item);
        }
    }

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

    public static void sortItems(ArrayList<TaskItem> items) {
        ITEMS.clear();
        ITEMS.addAll(items);
    }
}
