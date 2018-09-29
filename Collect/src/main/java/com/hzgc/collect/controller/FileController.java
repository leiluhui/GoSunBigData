package com.hzgc.collect.controller;

import com.hzgc.collect.service.http.HttpFile;
import com.hzgc.common.service.api.service.InnerService;
import com.hzgc.common.util.json.JacksonUtil;
import com.hzgc.jniface.PictureData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class FileController {

    @Autowired
    private HttpFile httpFile;

    @RequestMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam(name = "url") String path) {
        log.info("Receive file path:" + path);
        byte[] image;
        if (path != null && !"".equals(path)) {
            image = httpFile.getImage(path);
        } else {
            image = new byte[0];
        }
        if (image == null || image.length == 0) {
            return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(null);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    @RequestMapping("/image_zip")
    public ResponseEntity<byte[]> getZipImage(@RequestParam(name = "url") String path) {
        log.info("Receive file path:" + path + ", zip image");
        byte[] image;
        if (path != null && !"".equals(path)) {
            image = httpFile.getZipImage(path);
        } else {
            image = new byte[0];
        }
        if (image == null || image.length == 0) {
            return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(null);
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
}
