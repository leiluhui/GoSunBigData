package com.hzgc.service.clustering.service;

import com.hzgc.common.collect.bean.FaceObject;
import com.hzgc.common.faceclustering.ClusteringAttribute;
import com.hzgc.common.faceclustering.table.ClusteringTable;
import com.hzgc.common.faceclustering.table.PeopleManagerTable;
import com.hzgc.common.facedynrepo.DynamicTable;
import com.hzgc.common.service.bean.PeopleManagerCount;
import com.hzgc.common.util.empty.IsEmpty;
import com.hzgc.common.util.uuid.UuidUtil;
import com.hzgc.service.clustering.bean.export.*;
import com.hzgc.service.clustering.bean.param.GetResidentParam;
import com.hzgc.service.clustering.bean.param.ResidentParam;
import com.hzgc.service.clustering.bean.param.SortParam;
import com.hzgc.service.clustering.dao.ElasticSearchDao;
import com.hzgc.service.clustering.dao.HBaseDao;
import com.hzgc.service.clustering.dao.PhoenixDao;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 告警聚类结果查询接口实现(彭聪)
 */
@Service
@Slf4j
public class ClusteringSearchService {
    @Autowired
    private HBaseDao hBaseDao;

    @Autowired
    private ElasticSearchDao elasticSearchDao;

    @Autowired
    private  PhoenixDao phoenixDao;

    @Autowired
    private static PeopleManagerProducer peopleManagerProducer;

    /**
     * 查询聚类信息
     *
     * @param time      聚类时间
     * @param start     返回数据下标开始符号
     * @param limit     行数
     * @param sortParam 排序参数（默认按出现次数排序）
     * @return 聚类列表
     */
    public ClusteringInfo clusteringSearch(String region, String time, int start, int limit, String sortParam) {
        //查询不忽略的对象
        List<ClusteringAttribute> listNotIgnore = hBaseDao.getClustering(region, time, ClusteringTable.ClUSTERINGINFO_COLUMN_YES);
        //查询忽略的对象
        List<ClusteringAttribute> listIgnore = hBaseDao.getClustering(region, time, ClusteringTable.ClUSTERINGINFO_COLUMN_NO);
        if (!StringUtils.isBlank(sortParam)) {
            SortParam sortParams = ListUtils.getOrderStringBySort(sortParam);
            ListUtils.sort(listNotIgnore, sortParams.getSortNameArr(), sortParams.getIsAscArr());
            ListUtils.sort(listIgnore, sortParams.getSortNameArr(), sortParams.getIsAscArr());
        }
        int totalYes = listNotIgnore.size();
        int totalNo = listIgnore.size();
        int total = 0;
        ClusteringInfo clusteringInfo = new ClusteringInfo();
        //优先返回不忽略的聚类
        if (start > -1 && start <= totalYes) {
            if ((start + limit - 1) <= totalYes) {
                clusteringInfo.setClusteringAttributeList(listNotIgnore.subList(start - 1, start + limit - 1));
                log.info("Select clustering attributes not ignored num : " + limit);
                total = limit;
            } else if ((start + limit - 1) > totalYes && (start + limit - 1) <= (totalYes + totalNo)) {
                clusteringInfo.setClusteringAttributeList(listNotIgnore.subList(start - 1, totalYes));
                clusteringInfo.setClusteringAttributeList_ignore(listIgnore.subList(0, start + limit - totalYes - 1));
                log.info("Select clustering attributes not ignored num : " + (totalYes - start + 1));
                log.info("Select clustering attributes ignored num : " + (start + limit - totalYes - 1));
                total = limit;
            } else {
                clusteringInfo.setClusteringAttributeList(listNotIgnore.subList(start - 1, totalYes));
                clusteringInfo.setClusteringAttributeList_ignore(listIgnore.subList(0, totalNo));
                log.info("Select clustering attributes not ignored num : " + (totalYes - start + 1));
                log.info("Select clustering attributes ignored num : " + (totalNo));
                total = totalNo + totalYes - start;
            }
        } else {
            log.info("Start or limit out of index ");
        }
        clusteringInfo.setTotalClustering(total);
        return clusteringInfo;
    }


    /**
     * 查询单个聚类详细信息(告警ID)
     *
     * @param clusterId 聚类ID
     * @param time      聚类时间
     * @param start     分页查询开始行
     * @param limit     查询条数
     * @return 返回该类下面所以告警信息
     */
    public List<Integer> detailClusteringSearch_v1(String clusterId, String time, int start, int limit) {
        SearchHit[] results = elasticSearchDao.detailClusteringSearch_v1(clusterId, time, start, limit);
        List<Integer> alarmIdList = new ArrayList<>();
        if (results != null && results.length > 0) {
            for (SearchHit result : results) {
                int alarmTime = Integer.parseInt((String) result.getSource().get(DynamicTable.ALARM_ID));
                alarmIdList.add(alarmTime);
            }
        } else {
            log.info("No data get from es");
        }
        return alarmIdList;
    }

    /**
     * delete a clustering
     *
     * @param clusterIdList clusteringId include region information
     * @param time          clustering time
     * @param flag          yes: delete the ignored clustering, no :delete the not ignored clustering
     * @return ture or false indicating whether delete is successful
     */
    public boolean deleteClustering(List<String> clusterIdList, String time, String flag) {
        String clusteringId = clusterIdList.get(0);
        String region = clusteringId.split("-")[0];
        byte[] colName;
        if (flag.toLowerCase().equals("yes")) {
            colName = ClusteringTable.ClUSTERINGINFO_COLUMN_NO;
            log.info("Delete cluster attribute ignored");
        } else if (flag.toLowerCase().equals("no")) {
            colName = ClusteringTable.ClUSTERINGINFO_COLUMN_YES;
            log.info("Delete cluster attribute not ignored");
        } else {
            log.info("Flag is error, it must be yes or no");
            return false;
        }
        List<ClusteringAttribute> clusteringAttributeList = hBaseDao.getClustering(region, time, colName);
        for (String clusterId : clusterIdList) {
            Iterator<ClusteringAttribute> iterator = clusteringAttributeList.iterator();
            ClusteringAttribute clusteringAttribute;
            while (iterator.hasNext()) {
                clusteringAttribute = iterator.next();
                if (clusterId.equals(clusteringAttribute.getClusteringId())) {
                    iterator.remove();
                }
            }
        }
        return hBaseDao.putClustering(region, time, colName, clusteringAttributeList);

    }

    /**
     * ignore a clustering
     *
     * @param clusterIdList cluteringId include region information
     * @param time          clutering time
     * @param flag          yes is ignore, no is not ignore
     */
    public boolean ignoreClustering(List<String> clusterIdList, String time, String flag) {

        String clusteringId = clusterIdList.get(0);
        String region = clusteringId.split("-")[0];
        byte[] colNameSrc;
        byte[] colNameDes;
        if (flag.toLowerCase().equals("yes")) {
            log.info("Ignore cluster : " + clusterIdList);
            colNameSrc = ClusteringTable.ClUSTERINGINFO_COLUMN_YES;
            colNameDes = ClusteringTable.ClUSTERINGINFO_COLUMN_NO;
        } else if (flag.toLowerCase().equals("no")) {
            log.info("Dont ignore cluster : " + clusterIdList);
            colNameSrc = ClusteringTable.ClUSTERINGINFO_COLUMN_NO;
            colNameDes = ClusteringTable.ClUSTERINGINFO_COLUMN_YES;
        } else {
            log.info("Flag is error, it must be yes or no");
            return false;
        }
        List<ClusteringAttribute> listSrc = hBaseDao.getClustering(region, time, colNameSrc);
        List<ClusteringAttribute> listDes = hBaseDao.getClustering(region, time, colNameDes);
        //yes 表示数据需要忽略（HBase表中存入"n"列），no 表示数据不需要忽略（HBase表中存入"y"列）
        for (String clusterId : clusterIdList) {
            Iterator<ClusteringAttribute> iterator = listSrc.iterator();
            ClusteringAttribute clusteringAttribute;
            while (iterator.hasNext()) {
                clusteringAttribute = iterator.next();
                if (clusterId.equals(clusteringAttribute.getClusteringId())) {
                    clusteringAttribute.setFlag(flag);
                    listDes.add(clusteringAttribute);
                    iterator.remove();
                }
            }
        }
        boolean booSrc = hBaseDao.putClustering(region, time, colNameSrc, listSrc);
        boolean booDes = hBaseDao.putClustering(region, time, colNameDes, listDes);
        return booSrc && booDes;

    }

    /**
     * get detail Clustering from HBase
     *
     * @param clusterId clustering id
     * @param time      clustering time
     * @param start     index start
     * @param limit     count of data
     * @param sortParam the parameters of sort
     */
    @Deprecated
    public List<Integer> detailClusteringSearch_Hbase(String clusterId, String time, int start, int limit, String sortParam) {
        List<Integer> alarmInfoList = hBaseDao.detailClusteringSearch_Hbase(clusterId, time);
        if (IsEmpty.strIsRight(sortParam)) {
            SortParam sortParams = ListUtils.getOrderStringBySort(sortParam);
            ListUtils.sort(alarmInfoList, sortParams.getSortNameArr(), sortParams.getIsAscArr());
        }
        if (start > -1) {
            if ((start + limit) > alarmInfoList.size() - 1) {
                return alarmInfoList.subList(start, alarmInfoList.size());
            } else {
                return alarmInfoList.subList(start, start + limit);
            }
        } else {
            log.info("Start must bigger than -1");
            return null;
        }
    }

    /**
     * 统计时间段内，每个月份的聚类数量
     *
     * @param startTime 查询起始时间
     * @param endTime   查询结束时间
     * @return 每个月份的聚类数量
     */
    public List<PeopleManagerCount> getTotleNum(String startTime, String endTime) {
        HBaseDao hBaseDao = new HBaseDao();
        List<PeopleManagerCount> statisticsList = new ArrayList<>();
        //起止时间处理
        startTime = startTime.substring(0, startTime.lastIndexOf("-"));
        endTime = endTime.substring(0, endTime.lastIndexOf("-"));
        //查询每个rowkey对应的聚类（不忽略）数量
        Map<String, Integer> map = hBaseDao.getTotleNum(startTime, endTime + "-" + "zzzzzzzzzzzzzzzz");
        //获取时间段内的所有月份
        List<String> timeList = getMonthesInRange(startTime, endTime);
        //循环计算每个月的所有聚类数量
        for (String time : timeList) {
            PeopleManagerCount statistics = new PeopleManagerCount();
            int num = 0;
            for (String key : map.keySet()) {
                if (key.startsWith(time)) {
                    num += map.get(key);
                }
            }
            statistics.setMonth(time);
            statistics.setAddPeople(num);
            statisticsList.add(statistics);
        }
        return statisticsList;
    }

    /**
     * 返回时间区域内所有月份
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 所有月份
     */
    private static List<String> getMonthesInRange(String startTime, String endTime) {
        List<String> timeList = new ArrayList<>();

        DateFormat df = new SimpleDateFormat("yyyy-MM");
        try {
            Calendar start = Calendar.getInstance();
            start.setTime(df.parse(startTime));
            Calendar end = Calendar.getInstance();
            end.setTime(df.parse(endTime));
            Long startTimeL = start.getTimeInMillis();
            Long endTimeL = end.getTimeInMillis();
            while (startTimeL <= endTimeL) {
                Date everyTime = new Date(startTimeL);
                timeList.add(df.format(everyTime));

                start.add(Calendar.MONTH, 1);
                startTimeL = start.getTimeInMillis();
            }
            return timeList;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeList;
    }

    public Integer saveRegular(Regular regular) {
        return hBaseDao.saveRegular(regular);
    }

    public List<Regular> searchPlan(String regionID) {
        return hBaseDao.searchPlan(regionID);
    }

    public Integer modifyPlan(String regionID, String sim, String moveInCount, String moveOutDays) {
        return hBaseDao.modifyPlan(regionID, sim, moveInCount, moveOutDays);
    }

    public Integer deletePlan(List<String> regionID) {
        return hBaseDao.deletePlan(regionID);
    }

    /**
     * 判断身份证格式是否正确
     */
    public boolean authentication_idCode(ResidentParam person) {
        if (!StringUtils.isBlank(person.getIdcard())) {
            return idCodeAuthentication(person.getIdcard());
        }
        return true;
    }

    /**
     * 认证身份证格式是否正确。
     *
     * @param idCode 身份证ID
     * @return false 身份证不正确 true 身份证正确
     */
    private boolean idCodeAuthentication(String idCode) {
        if (idCode == null || idCode.isEmpty() || idCode.length() != 18) {
            return false;
        }
        String regEX = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        Pattern pattern = Pattern.compile(regEX);
        Matcher matcher = pattern.matcher(idCode);
        return matcher.matches();
    }

    /**
     * 身份证唯一性判断
     *
     * @param person
     * @return true:存在,false:不存在
     */
    public boolean isExists_idCode(ResidentParam person) {
        if (!StringUtils.isBlank(person.getIdcard())) {
            List<String> idcards = phoenixDao.getAllObjectIdcard();
            return idcards.contains(person.getIdcard());
        }
        return false;
    }

    /**
     * add person
     *
     * @param person 添加对象信息
     * @return 返回值为0，表示插入成功，返回值为1，表示插入失败
     */
    public Integer addPerson(ResidentParam person) {
        person.setId(UuidUtil.getUuid());
        log.info("Start add person,person id is : " + person.getId());
        //数据库操作
        Integer integer = phoenixDao.addPerson(person);
        return integer;
    }

    /**
     * 删除对象的信息
     *
     * @param rowkeyList 对象ID
     * @return 返回值为0，表示删除成功，返回值为1，表示删除失败
     */
    public Integer deletePerson(List<String> rowkeyList) {
        return phoenixDao.deletePerson(rowkeyList);
    }

    public Resident getPerson(String objectId) {
        return phoenixDao.getPerson(objectId);
    }

    /**
     * 获取数据库中idCard
     *
     * @param param
     * @return idCard
     */
    public String getObjectIdCard(ResidentParam param) {
        return phoenixDao.getObjectIdCard(param.getId());
    }

    public Integer updatePerson(ResidentParam param) {
        return phoenixDao.updatePerson(param);
    }


    /**
     * 抓拍次数查询
     *
     * @param rowkeylist 常驻人口库中某个人的ID
     * @reture map 返回这个人的抓拍次数的key-value对
     */
    public Map<String, Integer> getCaptureCount(List<String> rowkeylist) {
        Map<String, Integer> map = phoenixDao.getCaptureCount(rowkeylist);
        return map;
    }

    /**
     * 抓拍历史查询
     *
     * @param rowkeylist 常驻人口库ID的list
     * @return 返回一个人的抓拍历史
     */
    public Map<String, List<FaceObject>> getCaptureHistory(List<String> rowkeylist) {
        Map<String, List<FaceObject>> map = phoenixDao.getCaptureHistory(rowkeylist);
        return map;
    }

    /**
     * 可以匹配精确查找，以图搜索人员信息，模糊查找
     *
     * @param param 搜索参数的封装
     * @return 返回搜索所需要的结果封装成的对象，包含搜索id，成功与否标志，记录数，记录信息，照片id
     */
    public List<PersonObject> searchResident(GetResidentParam param) {
        List<PersonObject> personObjectList = new ArrayList<>();
        SqlRowSet sqlRowSet = phoenixDao.searchResident(param);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (sqlRowSet == null) {
            return personObjectList;
        }
        while (sqlRowSet.next()) {
            Timestamp createTime = sqlRowSet.getTimestamp(PeopleManagerTable.CREATETIME);
            String createTime_str = "";
            if (createTime != null) {
                java.util.Date createTime_data = new java.util.Date(createTime.getTime());
                createTime_str = sdf.format(createTime_data);
            }
            PersonObject personObject = new PersonObject();
            personObject.setObjectID(sqlRowSet.getString(PeopleManagerTable.ROWKEY));
            personObject.setRegionId(sqlRowSet.getString(PeopleManagerTable.REGION));
            personObject.setName(sqlRowSet.getString(PeopleManagerTable.NAME));
            personObject.setSex(sqlRowSet.getInt(PeopleManagerTable.SEX));
            personObject.setIdcard(sqlRowSet.getString(PeopleManagerTable.IDCARD));
            personObject.setCreator(sqlRowSet.getString(PeopleManagerTable.CREATOR));
            personObject.setCreatorConractWay(sqlRowSet.getString(PeopleManagerTable.CPHONE));
            personObject.setCreateTime(createTime_str);
            personObject.setReason(sqlRowSet.getString(PeopleManagerTable.REASON));
            personObject.setStatus(sqlRowSet.getInt(PeopleManagerTable.STATUS));
            personObject.setCareLevel(sqlRowSet.getInt(PeopleManagerTable.CARE));
            personObject.setFollowLevel(sqlRowSet.getInt(PeopleManagerTable.IMPORTANT));
            if (param.getPictureData() != null) {
                personObject.setSimilarity(sqlRowSet.getFloat(PeopleManagerTable.SIM));
            }
            personObject.setLocation(sqlRowSet.getString(PeopleManagerTable.LOCATION));
            personObjectList.add(personObject);
        }
        return personObjectList;
    }

    /**
     * 获取常驻人口库照片
     *
     * @param objectID 对象ID
     *                 return byte[]
     */
    public byte[] getResidentPhoto(String objectID) {
        return phoenixDao.getResidentPhoto(objectID);
    }
}

@Data
class PeopleManagerObject implements Serializable {
    private float[] feature;
    private String pkey;
    private String rowkey;
}