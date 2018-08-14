package com.hzgc.service.clustering.service;

import com.hzgc.common.faceclustering.table.PeopleManagerTable;
import com.hzgc.common.faceclustering.table.PersonRegionTable;
import com.hzgc.jni.FaceAttribute;
import com.hzgc.jni.FaceFunction;
import com.hzgc.jni.PictureData;
import com.hzgc.service.clustering.bean.param.GetResidentParam;
import com.hzgc.service.clustering.bean.param.ResidentParam;
import com.hzgc.service.clustering.bean.param.ResidentSortParam;
import com.hzgc.service.clustering.dao.SqlAndArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class ParseByOption {

    public String getAllObjectIdcard() {
        return "select " + PeopleManagerTable.IDCARD + " from " + PeopleManagerTable.TABLE_NAME;
    }

    public String addPerson() {
        return "upsert into " + PeopleManagerTable.TABLE_NAME + "("
                + PeopleManagerTable.ROWKEY + ", "
                + PeopleManagerTable.NAME + ", "
                + PeopleManagerTable.IDCARD + ", "
                + PeopleManagerTable.SEX + ", "
                + PeopleManagerTable.PHOTO + ", "
                + PeopleManagerTable.FEATURE + ", "
                + PeopleManagerTable.REASON + ", "
                + PeopleManagerTable.CREATOR + ", "
                + PeopleManagerTable.CPHONE + ", "
                + PeopleManagerTable.CREATETIME + ", "
                + PeopleManagerTable.UPDATETIME + ", "
                + PeopleManagerTable.IMPORTANT + ", "
                + PeopleManagerTable.STATUS + ", "
                + PeopleManagerTable.CARE
                + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public String deletePerson() {
        return "delete from " + PeopleManagerTable.TABLE_NAME + " where " + PeopleManagerTable.ROWKEY + " = ?";
    }

    public String getPerson() {
        return "select " + PeopleManagerTable.ROWKEY + ", "
                + PeopleManagerTable.NAME + ", "
                + PeopleManagerTable.IDCARD + ", "
                + PeopleManagerTable.SEX + ", "
                + PeopleManagerTable.REASON + ", "
                + PeopleManagerTable.CREATOR + ", "
                + PeopleManagerTable.CPHONE + ", "
                + PeopleManagerTable.CREATETIME + ", "
                + PeopleManagerTable.UPDATETIME + ", "
                + PeopleManagerTable.IMPORTANT + ", "
                + PeopleManagerTable.STATUS + ", "
                + PeopleManagerTable.CARE + " from "
                + PeopleManagerTable.TABLE_NAME + " where "
                + PeopleManagerTable.ROWKEY + " = ?";
    }

    public String getObjectIdCard() {
        return "select " + PeopleManagerTable.IDCARD
                + " from " + PeopleManagerTable.TABLE_NAME
                + " where " + PeopleManagerTable.ROWKEY + " = ?";
    }

    /**
     * 根据传过来的person的封装的数据Map，进行生成一个sql,用来进行插入和更新
     *
     * @param param 需要更新的数据
     * @return 拼装成的sql 以及需要设置的值
     */
    public ConcurrentHashMap<String, CopyOnWriteArrayList<Object>> getUpdateSqlFromPerson(ResidentParam param) {
        CopyOnWriteArrayList<Object> setValues = new CopyOnWriteArrayList<>();
        StringBuffer sql = new StringBuffer("");
        sql.append("upsert into ");
        sql.append(PeopleManagerTable.TABLE_NAME);
        sql.append("(");
        sql.append(PeopleManagerTable.ROWKEY);
        setValues.add(param.getId());

        String name = param.getName();
        if (name != null) {
            sql.append(", ");
            sql.append(PeopleManagerTable.NAME);
            setValues.add(name);
        }
        String idcard = param.getIdcard();
        if (idcard != null) {
            sql.append(", ");
            sql.append(PeopleManagerTable.IDCARD);
            setValues.add(idcard);
        }
        Integer sex = param.getSex();
        if (sex != null) {
            sql.append(", ");
            sql.append(PeopleManagerTable.SEX);
            setValues.add(sex);
        }
        String reason = param.getReason();
        if (reason != null) {
            sql.append(", ");
            sql.append(PeopleManagerTable.REASON);
            setValues.add(reason);
        }
        String creator = param.getCreator();
        if (creator != null) {
            sql.append(", ");
            sql.append(PeopleManagerTable.CREATOR);
            setValues.add(creator);
        }
        String cphone = param.getCreatorContactWay();
        if (cphone != null) {
            sql.append(", ");
            sql.append(PeopleManagerTable.CPHONE);
            setValues.add(cphone);
        }
        Integer important = param.getFollowLevel();
        if (important != null) {
            sql.append(", ");
            sql.append(PeopleManagerTable.IMPORTANT);
            setValues.add(important);
        }
        Integer care = param.getCareLevel();
        if (care != null) {
            sql.append(", ");
            sql.append(PeopleManagerTable.CARE);
            setValues.add(care);
        }
        sql.append(") values(?");
        StringBuilder tmp = new StringBuilder("");
        for (int i = 0; i <= setValues.size() - 2; i++) {
            tmp.append(",?");
        }
        sql.append(tmp);
        sql.append(")");
        ConcurrentHashMap<String, CopyOnWriteArrayList<Object>> sqlAndSetValues = new ConcurrentHashMap<>();
        sqlAndSetValues.put(new String(sql), setValues);
        return sqlAndSetValues;
    }

    public SqlAndArgs getSqlFromGetResidentParam(GetResidentParam param) {
        StringBuilder sql = new StringBuilder();
        List<Object> setValues = new ArrayList<>();
        PictureData pictureData = param.getPictureData();
        List<ResidentSortParam> params = param.getSortParamList();
        sql.append("select ");
        if (pictureData != null) {
            sql.append(sameFieldNeedReturn());
            sql.append(", sim as sim")
                    .append(" from (select ")
                    .append(sameFieldNeedReturn())
                    .append(", FACECOMP(")
                    .append(PeopleManagerTable.FEATURE)
                    .append(", ?");
            StringBuilder featureString = new StringBuilder();
            FaceAttribute faceAttribute = pictureData.getFeature();
            if (faceAttribute != null) {
                featureString.append(FaceFunction.floatArray2string(faceAttribute.getFeature()));
            }
            setValues.add(new String(featureString));
            sql.append(") as sim from ")
                    .append(PeopleManagerTable.TABLE_NAME);
            List<Object> whereParamList = new ArrayList<>();
            String whereSql = sameWhereSql(param, whereParamList);
            if (whereParamList.size() > 0) {
                sql.append(" where ").append(whereSql);
            }
            log.info("Where SQL :" + whereSql);
            log.info("Where SQL param list :" + Arrays.toString(whereParamList.toArray()));
            sql.append(")").append(" where sim >= ? ");
            setValues.add(param.getSimilarity());
            sql.append("order by ")
                    .append(sameSortSql(params, true));
        } else {
            sql.append(sameFieldNeedReturn())
                    .append(" from ")
                    .append(PeopleManagerTable.TABLE_NAME);
            List<Object> whereParamList = new ArrayList<>();
            String whereSql = sameWhereSql(param, whereParamList);
            log.info("Where SQL :" + whereSql);
            log.info("Where SQL param list :" + Arrays.toString(whereParamList.toArray()));
            if (whereParamList.size() > 0) {
                sql.append(" where ").append(whereSql);
            }
            Integer followLevel = param.getFollowLevel();
            boolean bb = false;
            if (followLevel != 0 && followLevel != 1) {
                sql.append(" order by ").append(PeopleManagerTable.IMPORTANT).append(" desc");
                bb = true;
            }
            if (params != null && params.size() > 1) {
                if (bb) {
                    sql.append(sameSortSql(params, false));
                } else {
                    sql.append(" order by ").append(sameSortSql(params, false));
                }
            }
        }
        // 进行分组
        SqlAndArgs sqlAndArgs = new SqlAndArgs();
        sqlAndArgs.setArgs(setValues);
        sqlAndArgs.setSql(sql.toString());
        return sqlAndArgs;
    }

    /**
     * 返回排序sql 自句
     *
     * @param params        排序参数
     * @param serarchByPics 是否有图片
     * @return 返回排序参数语句
     */
    private StringBuilder sameSortSql(List<ResidentSortParam> params, boolean serarchByPics) {
        StringBuilder sameSortSql = new StringBuilder();
        if (params != null) {
            int count = 0;
            if (serarchByPics) {
                if (params.contains(ResidentSortParam.RELATEDASC)) {
                    sameSortSql.append("sim asc");
                    if (params.size() > 1) {
                        sameSortSql.append(", ");
                    }
                }
                if (params.contains(ResidentSortParam.RELATEDDESC)) {
                    sameSortSql.append("sim desc");
                    if (params.size() > 1) {
                        sameSortSql.append(", ");
                    }
                }

            }
            if (params.contains(ResidentSortParam.IMPORTANTASC)) {
                sameSortSql.append(PeopleManagerTable.IMPORTANT);
                sameSortSql.append(" asc");
                count++;
            }
            if (params.contains(ResidentSortParam.IMPORTANTDESC)) {
                sameSortSql.append(PeopleManagerTable.IMPORTANT);
                sameSortSql.append(" desc");
                count++;
            }
            if (params.contains(ResidentSortParam.TIMEASC)) {
                if (count > 0) {
                    sameSortSql.append(", ");
                    sameSortSql.append(PeopleManagerTable.CREATETIME);
                    sameSortSql.append(" asc");
                } else {
                    sameSortSql.append(PeopleManagerTable.CREATETIME);
                    sameSortSql.append(" asc");
                }
            }
            if (params.contains(ResidentSortParam.TIMEDESC)) {
                if (count > 0) {
                    sameSortSql.append(", ");
                    sameSortSql.append(PeopleManagerTable.CREATETIME);
                    sameSortSql.append(" desc");
                } else {
                    sameSortSql.append(PeopleManagerTable.CREATETIME);
                    sameSortSql.append(" desc");
                }
            }
        }
        return sameSortSql;
    }

    /**
     * @return 不同情况下需要返回的相同的字段
     */
    private StringBuffer sameFieldNeedReturn() {
        StringBuffer sameFieldReturn = new StringBuffer("");
        sameFieldReturn.append(PeopleManagerTable.ROWKEY);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.REGION);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.NAME);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.SEX);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.IDCARD);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.CREATOR);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.CARE);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.CPHONE);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.CREATETIME);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.UPDATETIME);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.REASON);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.IMPORTANT);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.STATUS);
        sameFieldReturn.append(", ");
        sameFieldReturn.append(PeopleManagerTable.LOCATION);
        return sameFieldReturn;
    }

    /**
     * 封装共同的子where 查询
     *
     * @param param          传过来的搜索参数
     * @param whereParamList 需要对sql设置的参数
     * @return 子where查询
     */
    private String sameWhereSql(GetResidentParam param, List<Object> whereParamList) {
        StringBuilder whereQuery = new StringBuilder();
        boolean isChanged = false;

        // 关于姓名的搜索\
        String objectName = param.getName();
        if (!StringUtils.isBlank(objectName)) {
            whereQuery.append(PeopleManagerTable.NAME)
                    .append(" like '%").append(objectName).append("%'");
            whereParamList.add(objectName);
            isChanged = true;
        }

        // 关于身份证号的查询
        String idcard = param.getIdcard();
        if (!StringUtils.isBlank(idcard)) {
            if (isChanged) {
                whereQuery.append(" and ");
            }
            whereQuery.append(PeopleManagerTable.IDCARD)
                    .append(" like '%").append(idcard).append("%'");
            whereParamList.add(idcard);
            isChanged = true;
        }

        // 关于性别的查询
        Integer sex = param.getSex();
        if (sex != null) {
            if (sex == 0 || sex == 1 || sex == 2) {
                if (isChanged) {
                    whereQuery.append(" and ");
                }
                whereQuery.append(PeopleManagerTable.SEX).append(" = ").append(sex);
                whereParamList.add(sex);
                isChanged = true;
            }
        }

        // 关于人员类型列表的查询
        List<String> regionList = param.getRegionList();
        if (regionList != null && regionList.size() > 0) {
            if (regionList.size() == 1) {
                if (isChanged) {
                    whereQuery.append(" and ").append(PeopleManagerTable.REGION)
                            .append(" = '").append(regionList.get(0)).append("'");
                    whereParamList.add(regionList.get(0));
                } else {
                    whereQuery.append(PeopleManagerTable.REGION)
                            .append(" = '").append(regionList.get(0)).append("'");
                    whereParamList.add(regionList.get(0));
                    isChanged = true;
                }
            } else {
                int count = 0;
                if (isChanged) {
                    whereQuery.append(" and ");
                }
                whereQuery.append(PeopleManagerTable.REGION).append(" in (");
                for (int i = 0; i < regionList.size(); i++) {
                    if (count < regionList.size() - 1) {
                        whereQuery.append("'").append(regionList.get(i)).append("', ");
                        count++;
                    } else {
                        whereQuery.append("'").append(regionList.get(i)).append("')");
                    }
                    whereParamList.add(regionList.get(i));
                }
                isChanged = true;
            }
        }

        // 关于创建人姓名的查询
        String creator = param.getCreator();
        if (!StringUtils.isBlank(creator)) {
            if (isChanged) {
                whereQuery.append(" and ");
            }
            whereQuery.append(PeopleManagerTable.CREATOR).append(" like '%").append(creator).append("%'");
            whereParamList.add(creator);
            isChanged = true;
        }

        // 关于布控人手机号的查询
        String creatorConractWay = param.getCreatorContactWay();
        if (!StringUtils.isBlank(creatorConractWay)) {
            if (isChanged) {
                whereQuery.append(" and ");
            }
            whereQuery.append(PeopleManagerTable.CPHONE).append(" like '%").append(creatorConractWay).append("%'");
            whereParamList.add(creatorConractWay);
            isChanged = true;
        }

        // 关于关爱人口的查询
        Integer care = param.getCareLevel();
        if (care != null) {
            if (care == 0 || care == 1) {
                if (isChanged) {
                    whereQuery.append(" and ");
                }
                whereQuery.append(PeopleManagerTable.CARE).append(" = ").append(care);
                whereParamList.add(care);
                isChanged = true;
            }
        }

        //查询人员状态值
        Integer status = param.getStatus();
        if (status != null) {
            if (status == 0 || status == 1) {
                if (isChanged) {
                    whereQuery.append(" and ");
                }
                whereQuery.append(PeopleManagerTable.STATUS).append(" = ").append(status);
                whereParamList.add(status);
                isChanged = true;
            }
        }

        // 关于是否是重点人员的查询
        Integer followLevel = param.getFollowLevel();
        if (followLevel != null) {
            if (followLevel == 0 || followLevel == 1) {
                if (isChanged) {
                    whereQuery.append(" and ");
                }
                whereQuery.append(PeopleManagerTable.IMPORTANT).append(" = ").append(followLevel);
                whereParamList.add(followLevel);
            }
        }
        return whereQuery.toString();
    }

    public String getPhotoByObjectID() {
        return  "select " + PeopleManagerTable.PHOTO
                + " from " + PeopleManagerTable.TABLE_NAME
                + " where " + PeopleManagerTable.ROWKEY + " = ?";
    }
}






















