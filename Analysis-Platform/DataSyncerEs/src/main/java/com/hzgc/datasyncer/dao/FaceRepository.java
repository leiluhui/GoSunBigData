package com.hzgc.datasyncer.dao;

import com.hzgc.datasyncer.bean.EsFaceObject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FaceRepository extends ElasticsearchRepository<EsFaceObject, String> {
    
}
