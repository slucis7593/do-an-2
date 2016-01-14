package com.vuduc.android.worksoptimization;

import android.util.Log;

import com.vuduc.android.worksoptimization.model.TaskContent;
import com.vuduc.android.worksoptimization.model.TaskItem;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by vuduc on 11/30/15.
 */
public class ScheduleOptimizationByValue {

    private ArrayList<TaskItem> mResult;
    private long mMaxValue;
    private TaskItem[] mItems;
    private int[] mItemsIndex;
    private ArrayList<TaskItem> mTaskItems;
    private long mCurrentTime;
    public int mNumberSuccessfulItems;

    public ScheduleOptimizationByValue(ArrayList<TaskItem> taskItems) {
        mTaskItems = taskItems;
        mResult = new ArrayList<>();
        mMaxValue = 0;
        int size = mTaskItems.size();
        mItems = new TaskItem[size];
        mItemsIndex = new int[size];
        mCurrentTime = System.currentTimeMillis();
    }

    public ArrayList<TaskItem> run() {
        int index = 0;
        int size = mTaskItems.size();

        while (true) {
            if (index > size) {
                // Tính giá trị của cấu hình tìm được
                int totalValue = 0;
                for (TaskItem item : mItems)
                    totalValue += item.value;

                if (totalValue > mMaxValue) {
                    mMaxValue = totalValue;
                    mResult.clear();
                    mResult.addAll(Arrays.asList(mItems).subList(0, size));
                }

                // Loai bo vi tri hien tai
                index--;
                mItems[index] = null;

                // Lùi
                index--;
            }

            if (index < 0) {
                // Kết thúc thuật toán
                break;
            }

            if (search(index)) {
                // Tien
                index++;
            } else {
                // Tính giá trị của cấu hình tìm được
                int totalValue = 0;
                for (int i = 0; i < index; i++)
                    totalValue += mItems[i].value;

                if (totalValue > mMaxValue) {
                    mMaxValue = totalValue;
                    mResult.clear();
                    mResult.addAll(Arrays.asList(mItems).subList(0, index));
                }


                // Loai bo vi tri hien tai
                mItems[index] = null;

                // Lùi
                index--;
            }
        }

        // Add remaining items
        mNumberSuccessfulItems = mResult.size();

        // TODO: ??? Không hiểu sao phải set cái này thì mới được
        TaskContent.mNumberSuccessfulItems = mNumberSuccessfulItems;

        for (TaskItem item : mTaskItems) {
            boolean contain = false;
            for (TaskItem resultItem : mResult) {
                if (resultItem.equals(item)) {
                    contain = true;
                    break;
                }
            }

            if (!contain) {
                mResult.add(item);
            }
        }

        return mResult;
    }

    private boolean search(int index) {
        boolean found = false;
        int size = mTaskItems.size();

        while (mItemsIndex[index] < size) {
            // Lay Task Item
            int value = mItemsIndex[index];
            TaskItem item = mTaskItems.get(value);

            // Tang gia tri
            mItemsIndex[index]++;

            // Đảm bảo item mới không trùng với các item đã tồn tại trong mảng
            boolean contain = false;
            for (int i = 0; i < index; i++) {
                TaskItem existingItem = mItems[i];
                if (existingItem.equals(item)) {
                    contain = true;
                    break;
                }
            }

            if (!contain) {
                // Thỏa mãn vẫn kịp thời gian hay không
                long time = mCurrentTime;
                for (int i = 0; i < index; i++) {
                    TaskItem existingItem = mItems[i];
                    time += existingItem.estimateTime;
                }
                time += item.estimateTime;

                // Nếu thỏa mãn thì ghi nhận item và thông báo đã tìm thấy
                if (time <= item.deadline) {
                    mItems[index] = item;
                    found = true;
                    break;
                }
            }
        }

        return found;
    }
}
