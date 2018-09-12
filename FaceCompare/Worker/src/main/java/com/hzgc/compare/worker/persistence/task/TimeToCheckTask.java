package com.hzgc.compare.worker.persistence.task;

import com.hzgc.compare.worker.common.taskhandle.FlushTask;
import com.hzgc.compare.worker.common.taskhandle.TaskToHandleQueue;
import com.hzgc.compare.worker.common.tuple.Triplet;
import com.hzgc.compare.worker.persistence.FileManager;

import java.util.List;
import java.util.TimerTask;

/**
 * 定期查看TaskToHandle中有无FlushTask，如果有，则flush其中的记录，并删除该FlushTask
 */
public class TimeToCheckTask extends TimerTask{
    private FileManager manager;
    public TimeToCheckTask(FileManager manager){
        this.manager = manager;
    }
    public void run() {
        FlushTask task = TaskToHandleQueue.getTaskQueue().getTask(FlushTask.class);
        if(task != null){
            List<Triplet<String, String, byte[]>>data =  task.getRecords();
            manager.flush(data);
        }
    }
}
