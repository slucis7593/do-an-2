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
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<Long, TaskItem> ITEM_MAP = new HashMap<>();

    public static int mNumberSuccessfulItems = 0;

    // TODO: Test values
    static {
        long current = System.currentTimeMillis();

        int[] estimateTimes = new int[] { 2, 4, 1, 2, 3, 1 };
        int[] deadlines = new int[] { 3, 5, 6, 6, 7, 8 };

//        int[] estimateTimes = new int[] { 3, 1, 5, 3, 2 };
//        int[] deadlines = new int[] { 6, 3, 10, 7, 4 };

        for (int i = 0; i < estimateTimes.length; i++) {
            addItem(new TaskItem((long)i, "Công việc " + (i + 1), "Nội dung công việc " + (i + 1), estimateTimes[i] * 60 * 60 * 1000l, current + deadlines[i] * 60 * 60 * 1000l));
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

    private static TaskItem createDummyItem(long position) {
        return new TaskItem(position, "Item " + position, makeDetails(position), 36000000l, 1444150800000l);
    }

    private static String makeDetails(long position) {
        return "Details about Item: " + position;
    }

}
