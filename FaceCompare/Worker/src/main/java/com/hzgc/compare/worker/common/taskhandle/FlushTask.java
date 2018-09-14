package com.hzgc.compare.worker.common.taskhandle;

import com.hzgc.compare.worker.common.tuple.Triplet;

import java.util.List;

public class FlushTask extends TaskToHandle {
        private List<Triplet<String, String, byte[]>> records;
        public FlushTask(List<Triplet<String, String, byte[]>> records){
            super();
            this.records = records;
        }

    public List<Triplet<String, String, byte[]>> getRecords() {
        return records;
    }
}