package com.hzgc.cluster.peoman.worker;

import com.hzgc.cluster.peoman.model.Picture;
import com.hzgc.cluster.peoman.service.PictureService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@MapperScan(value = "com.hzgc.cluster.peoman.dao")
public class WorkerMain {
    public static void main(String[] args) {
        PictureService pictureService = new PictureService();
        List<Picture> pictureList = pictureService.getData(args[0]);

    }
}
