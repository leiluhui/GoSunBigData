package com.hzgc.compare.worker;

import com.hzgc.common.rpc.client.result.AllReturn;
import com.hzgc.common.rpc.server.annotation.RpcService;
import com.hzgc.compare.CompareParam;
import com.hzgc.compare.SearchResult;
import com.hzgc.compare.Service;
import com.hzgc.compare.worker.compare.task.CompareNotSamePerson;
import com.hzgc.compare.worker.compare.task.CompareOnePerson;
import com.hzgc.compare.worker.compare.task.CompareSamePerson;
import com.hzgc.compare.worker.conf.Config;
import com.hzgc.compare.worker.util.DateUtil;
import com.hzgc.compare.worker.util.FaceObjectUtil;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RpcService(Service.class)
public class ServiceImpl implements Service {
    private static Logger log = Logger.getLogger(ServiceImpl.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ExecutorService pool;

    public ServiceImpl(){
        pool = Executors.newFixedThreadPool(Config.WORKER_EXECUTORS_TO_COMPARE);
    }

    @Override
    public AllReturn<SearchResult> retrievalOnePerson(CompareParam param) {
        log.info("The param is : " + FaceObjectUtil.objectToJson(param));
        String dateStart = param.getDateStart();
        String dateEnd = param.getDateEnd();
        if(param.getResultCount() == 0){
            param.setResultCount(20);
        }
        long time1 = System.currentTimeMillis();
        try {
            if(sdf.parse(param.getDateEnd()).getTime() - sdf.parse(param.getDateStart()).getTime() >
                    1000L * 60 * 60 * 24 * Config.DAYS_WITHOUT_MULTITHREAD){
                log.info("The period of retrieval is large than predict.");
                log.info("Splite the period.");
                List<String> periods = DateUtil.getPeriod(param.getDateStart(), param.getDateEnd(), Config.DAYS_PER_THREAD);
                List<CompareOnePerson> list = new ArrayList<>();
                for(String period : periods){
                    String[] time = period.split(",");
                    CompareOnePerson compareOnePerson2 = new CompareOnePerson(param, time[0], time[1]);
                    pool.submit(compareOnePerson2);
                    list.add(compareOnePerson2);
                }

                while (true){
                    boolean flug = true;
                    for(CompareOnePerson compare : list){
                        flug = compare.isEnd() && flug;
                    }
                    if(flug){
                        break;
                    }
                }
                SearchResult result = new SearchResult();
                for(CompareOnePerson compare : list){
                    result.merge(compare.getSearchResult());
                }
                log.info("The time used of this Compare is : " + (System.currentTimeMillis() - time1));
                return new AllReturn<>(result.take(param.getResultCount()));
            } else {
                CompareOnePerson compareOnePerson2 = new CompareOnePerson(param, dateStart, dateEnd);
                SearchResult result = compareOnePerson2.compare();
                log.info("The time used of this Compare is : " + (System.currentTimeMillis() - time1));
                return new AllReturn<>(result.take(param.getResultCount()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return new AllReturn<>(new SearchResult());
        }

    }

    @Override
    public AllReturn<SearchResult> retrievalSamePerson(CompareParam param) {
        log.info("The param is : " + FaceObjectUtil.objectToJson(param));
        String dateStart = param.getDateStart();
        String dateEnd = param.getDateEnd();
        if(param.getResultCount() == 0){
            param.setResultCount(20);
        }
        try {
            long time1 = System.currentTimeMillis();
            if(sdf.parse(param.getDateEnd()).getTime() - sdf.parse(param.getDateStart()).getTime() >
                    1000L * 60 * 60 * 24 * Config.DAYS_WITHOUT_MULTITHREAD){
                log.info("The period of retrieval is large than predict.");
                log.info("Splite the period.");
                List<String> periods = DateUtil.getPeriod(param.getDateStart(), param.getDateEnd(), Config.DAYS_PER_THREAD);
                List<CompareSamePerson> list = new ArrayList<>();
                for(String period : periods){
                    String[] time = period.split(",");
                    CompareSamePerson compareOnePerson2 = new CompareSamePerson(param, time[0], time[1]);
                    pool.submit(compareOnePerson2);
                    list.add(compareOnePerson2);
                }

                while (true){
                    boolean flug = true;
                    for(CompareSamePerson compare : list){
                        flug = compare.isEnd() && flug;
                    }
                    if(flug){
                        break;
                    }
                }
                SearchResult result = new SearchResult();
                for(CompareSamePerson compare : list){
                    result.merge(compare.getSearchResult());
                }
                log.info("The time used of this Compare is : " + (System.currentTimeMillis() - time1));
                return new AllReturn<>(result.take(param.getResultCount()));
            } else {
                CompareSamePerson compareOnePerson2 = new CompareSamePerson(param, dateStart, dateEnd);
                SearchResult result = compareOnePerson2.compare();
                log.info("The time used of this Compare is : " + (System.currentTimeMillis() - time1));
                return new AllReturn<>(result.take(param.getResultCount()));
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return new AllReturn<>(null);
        }

    }

    @Override
    public AllReturn<Map<String, SearchResult>> retrievalNotSamePerson(CompareParam param) {
        log.info("The param is : " + FaceObjectUtil.objectToJson(param));
        String dateStart = param.getDateStart();
        String dateEnd = param.getDateEnd();
        if(param.getResultCount() == 0){
            param.setResultCount(20);
        }
        try {
            long time1 = System.currentTimeMillis();
            if(sdf.parse(param.getDateEnd()).getTime() - sdf.parse(param.getDateStart()).getTime() >
                    1000L * 60 * 60 * 24 * Config.DAYS_WITHOUT_MULTITHREAD){
                log.info("The period of retrieval is large than predict.");
                log.info("Splite the period.");
                List<String> periods = DateUtil.getPeriod(param.getDateStart(), param.getDateEnd(), Config.DAYS_PER_THREAD);
                List<CompareNotSamePerson> list = new ArrayList<>();
                for(String period : periods){
                    log.info("Period : " + period);
                    String[] time = period.split(",");
                    CompareNotSamePerson compareOnePerson = new CompareNotSamePerson(param, time[0], time[1]);
                    pool.submit(compareOnePerson);
                    list.add(compareOnePerson);
                }

                while (true){
                    boolean flug = true;
                    for(CompareNotSamePerson compare : list){
                        flug = compare.isEnd() && flug;
                    }
                    if(flug){
                        break;
                    }
                }
                Map<String, SearchResult> temp = new Hashtable<>();
                Map<String, SearchResult> result = new Hashtable<>();
                int index = 0;
                for(CompareNotSamePerson compare : list){
                    if(index == 0){
                        temp = compare.getSearchResult();
                    } else{
                        for(String key : temp.keySet()){
                            temp.get(key).merge(compare.getSearchResult().get(key));
                        }
                    }
                    index ++;
                }
                for(Map.Entry<String, SearchResult> entry : temp.entrySet()){
                    result.put(entry.getKey(), entry.getValue().take(param.getResultCount()));
                }
                log.info("The time used of this Compare is : " + (System.currentTimeMillis() - time1));
                return new AllReturn<>(result);
            } else {
                CompareNotSamePerson compareOnePerson2 = new CompareNotSamePerson(param, dateStart, dateEnd);
                Map<String, SearchResult> result = new Hashtable<>();
                Map<String, SearchResult> temp = compareOnePerson2.compare();
                for(Map.Entry<String, SearchResult> entry : temp.entrySet()){
                    result.put(entry.getKey(), entry.getValue().take(param.getResultCount()));
                }
                log.info("The time used of this Compare is : " + (System.currentTimeMillis() - time1));
                return new AllReturn<>(result);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return new AllReturn<>(null);
        }
    }

    public AllReturn<String> test() throws InterruptedException{
//        Thread.sleep(1000L * 10);
        log.info("TEST ");
        return new AllReturn<>("response");
    }
}
