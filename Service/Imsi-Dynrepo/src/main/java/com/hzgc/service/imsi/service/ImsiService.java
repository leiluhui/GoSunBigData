package com.hzgc.service.imsi.service;

import com.hzgc.common.service.response.ResponseResult;
import com.hzgc.service.imsi.bean.ImsiBean;
import com.hzgc.service.imsi.bean.ImsiParam;
import com.hzgc.service.imsi.dao.ImsiDao;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImsiService {

    @Autowired
    private ImsiDao imsiDao;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult <List <ImsiBean>> getImsiInfo(ImsiParam imsiParam) {
        SearchHits hits = imsiDao.getImsiInfo(imsiParam);
        ArrayList <String> sns = new ArrayList <>();
        ArrayList <ImsiBean> imsiBeans = new ArrayList <>();
        for (SearchHit hit : hits) {
            ImsiBean imsiBean = new ImsiBean();
            imsiBean.setImsi((String) hit.getSource().get("com/hzgc/service/imsi"));
            imsiBean.setTime((String) hit.getSource().get("time"));
            String sn = (String) hit.getSource().get("sn");
            imsiBean.setSn(sn);
            sns.add(sn);
            imsiBeans.add(imsiBean);
        }
        //平台服务调用
        Map map = restTemplate.postForObject("", sns, Map.class);
        for (ImsiBean imsiBean : imsiBeans) {
//            if (imsiBean.getSn().equals()) {
//
//            }
        }
        return null;
    }
}
