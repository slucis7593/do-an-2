package com.vuduc.android.worksoptimization;

import com.vuduc.android.worksoptimization.model.TaskItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by vuduc on 10/16/15.
 */
public class ScheduleOptimization {

    private ArrayList<TaskItem> mTaskItems;
    public int mNumberSuccessfulItems;
    public double mTime;

    public ScheduleOptimization(ArrayList<TaskItem> taskItems) {
        mTaskItems = taskItems;
    }

    public ArrayList<TaskItem> run() {
        ArrayList<TaskItem> transitionTask = new ArrayList<>();

        // Sort increase by deadline
        Collections.sort(mTaskItems, new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem lhs, TaskItem rhs) {
                return (int)(lhs.deadline - rhs.deadline);
            }
        });

        boolean isContinue = true;
        while (isContinue) {
            mTime = 0;
            for (int i = 0; i < mTaskItems.size(); i++) {
                TaskItem item = mTaskItems.get(i);

                if (mTime + item.estimateTime > item.getRemainingTime()) {
                    if (transitionTask.contains(item)) {
                        mNumberSuccessfulItems = i;
                        isContinue = false;
                        break;
                    } else {
                        // Find maximum estimate item
                        long maxEstimateTime = mTaskItems.get(0).estimateTime;
                        int maxItemIndex = 0;

                        for (int j = 1; j <= i; j++) {
                            if (mTaskItems.get(j).estimateTime > maxEstimateTime) {
                                maxEstimateTime = mTaskItems.get(j).estimateTime;
                                maxItemIndex = j;
                            }
                        }

                        // Change this item to end position and break;
                        TaskItem moveToEndItem = mTaskItems.remove(maxItemIndex);
                        mTaskItems.add(moveToEndItem);

                        transitionTask.add(item);
                        break;
                    }
                }

                mTime += item.estimateTime;

                if (i == mTaskItems.size() - 1) {
                    mNumberSuccessfulItems = mTaskItems.size();
                    isContinue = false;
                }
            }
        }

        return mTaskItems;
    }
}
