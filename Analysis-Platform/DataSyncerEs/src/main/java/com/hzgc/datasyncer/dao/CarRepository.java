package com.hzgc.datasyncer.dao;

import com.hzgc.datasyncer.bean.EsCarObject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CarRepository extends ElasticsearchRepository<EsCarObject, String> {

}
