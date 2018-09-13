package com.hzgc.cluster.peoman.tasktracker;


import com.github.ltsopensource.tasktracker.TaskTracker;
import com.github.ltsopensource.tasktracker.TaskTrackerBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class TaskTrackerMain {
    public static void main(String[] args) {
        final TaskTracker taskTracker = new TaskTrackerBuilder()
                .setPropertiesConfigure("application-pro.properties")
                .build();

        taskTracker.start();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                taskTracker.stop();
            }
        }));
    }
}










