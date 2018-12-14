package com.hzgc.datasyncer.dao;

import com.hzgc.datasyncer.bean.EsPersonObject;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PersonRepository extends ElasticsearchRepository<EsPersonObject, String> {
}
