package com.hzgc.service.clustering.service;

import com.hzgc.service.clustering.bean.export.PersonObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Aspect
public class ResidentHandlerTool {

    /**
     * 根据请求参数，进行分页处理
     */
    public List<PersonObject> formatTheResidentSearchResult(List<PersonObject> personObjectList, int start, int size){
        log.info("Start's value is : " + start);
        log.info("size's value is : " + size);
        List<PersonObject> personObjectLists = new ArrayList<>();
        if (personObjectList == null){
            return personObjectLists;
        }else {
            if ((start + size) > personObjectList.size()){
                personObjectLists = personObjectList.subList(start,personObjectList.size());
                log.info("personObjectList's Size is : " + personObjectList.size());
                log.info("personObjectList is : " + personObjectList);
            }else {
                personObjectLists = personObjectList.subList(start,start+size);
                log.info("personObjectList's Size is : " + personObjectList.size());
                log.info("personObjectList is : " + personObjectList);
            }
        }
        return personObjectLists;
    }
}
