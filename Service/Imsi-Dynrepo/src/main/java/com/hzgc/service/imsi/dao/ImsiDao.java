package com.hzgc.service.imsi.dao;

import com.hzgc.service.imsi.bean.ImsiParam;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ImsiDao {

    @Value(value = "${es.cluster.name}")
    private String clusterName;
    @Value("${es.hosts}")
    private String esHost;
    @Value("${es.cluster.port}")
    private String esPort;
    private String index = "com/hzgc/service/imsi";
    private String type = "recognize";
    private KafkaConsumer <String, String> consumer;
    private TransportClient esClient;

    public ImsiDao() {
//        esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }

    public SearchHits getImsiInfo(ImsiParam imsiParam) {
        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch(index).setTypes(type);
        if (null != imsiParam.getImsi()) {
            searchRequestBuilder.setQuery(QueryBuilders.matchQuery("com/hzgc/service/imsi", imsiParam.getImsi()));
        }
        if (null != imsiParam.getSns() && imsiParam.getSns().size() > 0) {
            BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
            for (String sn : imsiParam.getSns()) {
                queryBuilder.should(QueryBuilders.matchQuery("sn", sn));
            }
            searchRequestBuilder.setQuery(queryBuilder);
        }
        if (null != imsiParam.getStart()) {
            searchRequestBuilder.setFrom(imsiParam.getStart());
        }
        if (null != imsiParam.getLimit()) {
            searchRequestBuilder.setSize(imsiParam.getLimit());
        }
        SearchResponse searchResponse = searchRequestBuilder.addSort("time", SortOrder.DESC).get();
        SearchHits hits = searchResponse.getHits();
        return hits;
    }
}
