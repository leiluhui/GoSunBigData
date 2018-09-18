package com.hzgc.cluster.peoman.worker;

import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTaskTracker
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
