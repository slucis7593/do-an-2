package com.vuduc.android.worksoptimization.model;

/**
 * Created by vuduc on 10/16/15.
 */
public class TaskItem {
    public Long id;
    public String name;
    public String details;
    public Long estimateTime; // Hour
    public Long deadline; // Date value
    public Long value;

    public TaskItem() {

    }

    public TaskItem(Long id, String name, String details, Long estimateTime, Long deadline, Long value) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.estimateTime = estimateTime;
        this.deadline = deadline;
        this.value = value;
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
        this.value = 0l;
    }

    @Override
    public String toString() {
        return name;
    }

    public double getRemainingTime() {
        return deadline - System.currentTimeMillis();
    }
}
