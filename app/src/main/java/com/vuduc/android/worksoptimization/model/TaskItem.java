package com.vuduc.android.worksoptimization.model;

/**
 * Created by vuduc on 10/16/15.
 */
public class TaskItem {
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

    public double getRemainingTime() {
        // TODO: return deadline - System.currentTimeMillis();
        return deadline - System.currentTimeMillis();
    }
}
