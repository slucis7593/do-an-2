package com.vuduc.android.worksoptimization;

import com.vuduc.android.worksoptimization.model.TaskItem;

import java.util.ArrayList;

/**
 * Created by vuduc on 10/16/15.
 */
public class ScheduleOptimization {

    int i, t, cs;
    int min;
    int[] st;
    int[] p;
    int[] d;
    int[] stt;

    public void xep(int x) {
        int shd = 0;
        int time = 0;

        shd++;
        st[shd] = x;

        if (time + p[x] <= d[x]) {
            time = time + p[x];
            return;
        }

        min = time;
        cs = shd;

        for (int i = 0; i < shd - 1; i++) {
            t = time + p[x] - p[stt[i]];
            if (t < min && t <= d[x]) {
                min = t;
                cs = i;
            }
        }

        shd--;

        for (int i = cs; i < shd; i++) {
            stt[i] = stt[i + 1];
            time = min;
        }
    }

    public ArrayList<TaskItem> runAlgorithm(ArrayList<TaskItem> items) {

        return null;
    }

}
