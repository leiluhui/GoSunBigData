package com.hzgc.service.dynperson.dao;

import com.hzgc.common.util.es.ElasticSearchHelper;
import com.hzgc.common.service.facedynrepo.PersonTable;
import com.hzgc.common.service.personattribute.bean.PersonAttribute;
import com.hzgc.common.service.personattribute.bean.PersonAttributeValue;
import com.hzgc.service.dynperson.bean.CaptureOption;
import com.hzgc.service.dynperson.util.DeviceToIpcs;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class ElasticSearchDao {
    private TransportClient esClient;

    public ElasticSearchDao(@Value("${es.cluster.name}") String clusterName,
                            @Value("${es.hosts}") String esHost,
                            @Value("${es.cluster.port}") String esPort) {
        this.esClient = ElasticSearchHelper.getEsClient(clusterName, esHost, Integer.parseInt(esPort));
    }

    public SearchResponse getCaptureHistory(CaptureOption captureOption, String sortParam) {
        BoolQueryBuilder queryBuilder = createBoolQueryBuilder(captureOption);
        SearchRequestBuilder requestBuilder = createSearchRequestBuilder()
                .setQuery(queryBuilder)
                .setFrom(captureOption.getStart())
                .setSize(captureOption.getLimit())
                .addSort( PersonTable.TIMESTAMP,
                        Objects.equals(sortParam, EsSearchParam.DESC) ? SortOrder.DESC : SortOrder.ASC);
        return requestBuilder.get();
    }

    public SearchResponse getCaptureHistory(CaptureOption captureOption, List<String> deviceIpcs, String sortParam) {
        BoolQueryBuilder queryBuilder = createBoolQueryBuilder(captureOption);
        setDeviceIdList(queryBuilder,deviceIpcs);
        SearchRequestBuilder requestBuilder = createSearchRequestBuilder()
                .setQuery(queryBuilder)
                .setFrom(captureOption.getStart())
                .setSize(captureOption.getLimit())
                .addSort( PersonTable.TIMESTAMP,
                         Objects.equals(sortParam, EsSearchParam.DESC) ? SortOrder.DESC : SortOrder.ASC);
        return requestBuilder.get();
    }

    public SearchResponse getCaptureHistory(CaptureOption option, String ipc, String sortParam) {
        BoolQueryBuilder queryBuilder = createBoolQueryBuilder(option);
        BoolQueryBuilder boolQueryBuilder=setDeviceId(queryBuilder, ipc);
        SearchRequestBuilder requestBuilder = createSearchRequestBuilder()
                .setQuery(boolQueryBuilder)
                .setFrom(option.getStart())
                .setSize(option.getLimit())
                .addSort( PersonTable.TIMESTAMP,
                        Objects.equals(sortParam, EsSearchParam.DESC) ? SortOrder.DESC : SortOrder.ASC);
        return requestBuilder.get();
    }

    private SearchRequestBuilder createSearchRequestBuilder() {
        return esClient.prepareSearch(PersonTable.DYNAMIC_INDEX)
                .setTypes(PersonTable.PERSON_INDEX_TYPE);
    }

    private BoolQueryBuilder createBoolQueryBuilder(CaptureOption option) {
        // 最终封装成的boolQueryBuilder 对象。
        BoolQueryBuilder totalBQ = QueryBuilders.boolQuery();
        // 筛选行人属性
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (option.getAttributes() != null && option.getAttributes().size() > 0) {
            boolQueryBuilder = setAttribute(totalBQ, option.getAttributes());
        }
        totalBQ.must(boolQueryBuilder);
        // 开始时间和结束时间存在的时候的处理
        if (option.getStartTime() != null && option.getEndTime() != null &&
                !option.getStartTime().equals("") && !option.getEndTime().equals("")) {
            setStartEndTime(totalBQ, option.getStartTime(), option.getEndTime());
        }

        BoolQueryBuilder devicebuilder = QueryBuilders.boolQuery();
        if (option.getDevices().size() > 0){

        for (String deviceIpc: DeviceToIpcs.getIpcs(option.getDevices())){
            devicebuilder.should(QueryBuilders.matchPhraseQuery(PersonTable.IPCID,deviceIpc));
        }

        }
        totalBQ.must(devicebuilder);
        return totalBQ;
    }

    private void setStartEndTime(BoolQueryBuilder totalBQ, String startTime, String endTime) {
        totalBQ.must(QueryBuilders.rangeQuery(PersonTable.TIMESTAMP).gte(startTime).lte(endTime));
    }

    private void setDeviceIdList(BoolQueryBuilder totalBQ, List<String> deviceId) {
        // 设备ID 的的boolQueryBuilder
        BoolQueryBuilder devicdIdBQ = QueryBuilders.boolQuery();
        for (String id : deviceId) {
            devicdIdBQ.should(QueryBuilders.matchPhraseQuery(PersonTable.IPCID, id));
        }
        totalBQ.must(devicdIdBQ);
    }

    private BoolQueryBuilder setDeviceId(BoolQueryBuilder queryBuilder, String deviceId) {
        BoolQueryBuilder deviceIdBQ = QueryBuilders.boolQuery();
        queryBuilder.should(QueryBuilders.matchPhraseQuery(PersonTable.IPCID, deviceId));
        deviceIdBQ.must(queryBuilder);
        return deviceIdBQ;
    }

    private BoolQueryBuilder setAttribute(BoolQueryBuilder totalBQ, List<PersonAttribute> attributes) {
        // 筛选行人属性
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (PersonAttribute attribute : attributes) {
            String identify = attribute.getIdentify().toLowerCase();
            List<PersonAttributeValue> attributeValues = attribute.getValues();
            String logic = String.valueOf(attribute.getPersonLogistic());
            BoolQueryBuilder attributeBuilder = QueryBuilders.boolQuery();
            for (PersonAttributeValue attributeValue : attributeValues) {
                String attr = String.valueOf(attributeValue.getCode());
                if (!attr.equals("0")) {
                    attributeBuilder.should(QueryBuilders.matchQuery(identify, attr));
                }
            }
            boolQueryBuilder.must(attributeBuilder);
        }
        return boolQueryBuilder;
    }
}
