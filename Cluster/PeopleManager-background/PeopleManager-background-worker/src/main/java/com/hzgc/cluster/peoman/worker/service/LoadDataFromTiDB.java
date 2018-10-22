package com.hzgc.cluster.peoman.worker.service;

import com.google.common.base.Stopwatch;
import com.hzgc.cluster.peoman.worker.dao.PictureMapper;
import com.hzgc.cluster.peoman.worker.model.Picture;
import com.hzgc.jniface.FaceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
class LoadDataFromTiDB {
    @Autowired
    private PictureMapper pictureMapper;
    @Autowired
    private MemeoryCache memeoryCache;

    void load(int offset, int limit) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<Picture> pictureList = pictureMapper.selectByPrimaryKey(offset, limit);
        if (pictureList != null && pictureList.size() > 0) {
            log.info("Load data from databases successfull, offset {}, limit {}, time {}",
                    offset, limit, stopwatch.elapsed(TimeUnit.MILLISECONDS));
            for(Picture picture : pictureList) {
                log.info("--------------"+picture.getPeopleId());
            }
            cacheToMemeory(pictureList);
        } else {
            log.error("Load data failed, offset {}, limit {}, time {}", offset, limit, stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    private void cacheToMemeory(List<Picture> pictures) {
        List<ComparePicture> comparePictureList = new ArrayList<>();
        for (Picture picture : pictures) {
            if (picture.getBitFeature() != null &&
                    picture.getId() != null &&
                    picture.getPeopleId() != null) {
                ComparePicture comparePicture = new ComparePicture();
                comparePicture.setId(picture.getId());
                comparePicture.setBitFeature(FaceUtil.base64Str2BitFeature(picture.getBitFeature()));
                comparePicture.setPeopleId(picture.getPeopleId());
                comparePicture.setFlagId(picture.getFlagId());
                comparePicture.setName(picture.getName());
                comparePictureList.add(comparePicture);
            }
        }
        memeoryCache.putData(comparePictureList);
    }


}
