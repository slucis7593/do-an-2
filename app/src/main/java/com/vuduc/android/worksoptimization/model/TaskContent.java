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

}
