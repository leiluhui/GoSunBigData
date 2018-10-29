package com.hzgc.service.dynrepo.dao;

import com.hzgc.common.util.es.ElasticSearchHelper;
import com.hzgc.common.service.faceattribute.bean.Attribute;
import com.hzgc.common.service.faceattribute.bean.AttributeValue;
import com.hzgc.common.service.facedynrepo.FaceTable;
import com.hzgc.service.dynrepo.bean.CaptureOption;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchDao {
    private TransportClient esClient;


    public ElasticSearchDao(@Value("${es.cluster.name}") String clusterName,
                            @Value("${es.hosts}") String esHost,
                            @Value("${es.cluster.port}") String esPort) {
        this.esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }


    public SearchResponse getCaptureHistory(CaptureOption option, String sortParam) {
        BoolQueryBuilder queryBuilder = createBoolQueryBuilder(option);

        SearchRequestBuilder requestBuilder = createSearchRequestBuilder()
                .setQuery(queryBuilder)
                .setFrom(option.getStart())
                .setSize(option.getLimit())
                .addSort(FaceTable.TIMESTAMP,
                        Objects.equals(sortParam, EsSearchParam.DESC) ? SortOrder.DESC : SortOrder.ASC);
        return requestBuilder.get();
    }

    public SearchResponse getCaptureHistory(CaptureOption option, List<String> ipcList, String sortParam) {
        BoolQueryBuilder queryBuilder = createBoolQueryBuilder(option);
        setDeviceIdList(queryBuilder, ipcList);
        SearchRequestBuilder requestBuilder = createSearchRequestBuilder()
                .setQuery(queryBuilder)
                .setFrom(option.getStart())
                .setSize(option.getLimit())
                .addSort(FaceTable.TIMESTAMP,
                        Objects.equals(sortParam, EsSearchParam.DESC) ? SortOrder.DESC : SortOrder.ASC);
        return requestBuilder.get();
    }

    public SearchResponse getCaptureHistory(CaptureOption option, String ipc, String sortParam) {
        BoolQueryBuilder queryBuilder = createBoolQueryBuilder(option);
        setDeviceId(queryBuilder, ipc);
        SearchRequestBuilder requestBuilder = createSearchRequestBuilder()
                .setQuery(queryBuilder)
                .setFrom(option.getStart())
                .setSize(option.getLimit())
                .addSort(FaceTable.TIMESTAMP,
                        Objects.equals(sortParam, EsSearchParam.DESC) ? SortOrder.DESC : SortOrder.ASC);
        return requestBuilder.get();
    }

    private SearchRequestBuilder createSearchRequestBuilder() {
        return esClient.prepareSearch(FaceTable.DYNAMIC_INDEX)
                .setTypes(FaceTable.PERSON_INDEX_TYPE);
    }

    private BoolQueryBuilder createBoolQueryBuilder(CaptureOption option) {
        // 最终封装成的boolQueryBuilder 对象。
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        //筛选人脸属性
        if (option.getAttributes() != null && option.getAttributes().size() > 0) {
            setAttribute(totalBQ, option.getAttributes());
        }
//        totalBQ.must(queryBuilder);
        // 开始时间和结束时间存在的时候的处理
        if (option.getStartTime() != null && option.getEndTime() != null &&
                !option.getStartTime().equals("") && !option.getEndTime().equals("")) {
            setStartEndTime(totalBQ, option.getStartTime(), option.getEndTime());
        }
        return totalBQ;
    }

    private void setStartEndTime(BoolQueryBuilder totalBQ, String startTime, String endTime) {
        totalBQ.must(QueryBuilders.rangeQuery(FaceTable.TIMESTAMP).gte(startTime).lte(endTime));
    }

    private void setDeviceIdList(BoolQueryBuilder totalBQ, List<String> deviceId) {
        // 设备ID 的的boolQueryBuilder
        BoolQueryBuilder devicdIdBQ = QueryBuilders.boolQuery();
        for (String t : deviceId) {
            devicdIdBQ.should(QueryBuilders.matchPhraseQuery(FaceTable.IPCID, t));
        }
        totalBQ.must(devicdIdBQ);
    }

    private void setDeviceId(BoolQueryBuilder totalBQ, String ipc) {
        BoolQueryBuilder deviceIdBQ = QueryBuilders.boolQuery();
        deviceIdBQ.should(QueryBuilders.matchPhraseQuery(FaceTable.IPCID, ipc).analyzer(EsSearchParam.STANDARD));
        totalBQ.must(deviceIdBQ);
    }

    private void setAttribute(BoolQueryBuilder totalBQ, List <Attribute> attributes) {
        // 筛选行人属性
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Attribute attribute : attributes) {
            String identify = attribute.getIdentify();
            List <AttributeValue> attributeValues = attribute.getValues();
            BoolQueryBuilder attributeBuilder = QueryBuilders.boolQuery();
            for (AttributeValue attributeValue : attributeValues) {
                Integer attr = attributeValue.getValue();
                attributeBuilder.should(QueryBuilders.matchQuery(identify, attr));
            }
            totalBQ.must(attributeBuilder);
        }
    }

    public String getLastCaptureTime(String ipcId) {
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        totalBQ.must(QueryBuilders.matchPhraseQuery(FaceTable.IPCID, ipcId));
        SearchResponse searchResponse = createSearchRequestBuilder()
                .setQuery(totalBQ)
                .setSize(1)
                .addSort(FaceTable.TIMESTAMP, SortOrder.DESC)
                .get();
        SearchHits hits =  searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        String lastTime = "";
        for (SearchHit hit : searchHits){
            lastTime = String.valueOf(hit.getSource().get(FaceTable.TIMESTAMP));
        }
        return lastTime;
    }
}
