package com.hzgc.service.facedispatch.starepo.dao;

import com.hzgc.service.facedispatch.starepo.model.FilterField;
import com.hzgc.service.facedispatch.starepo.model.ObjectInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ObjectInfoMapper {
    /**
     * 通过对象ID删除对象（单个对象）
     *
     * @param id 对象ID
     * @return 是否成功
     */
    int deleteObjectById(String id);

    /**
     * 通过对象ID列表批量删除对象
     *
     * @param idList ID列表
     * @return 是否成功
     */
    int deleteObjectByIdBatch(List <String> idList);

    /**
     * 添加对象（单个对象）
     *
     * @param objectInfo 人员对象
     * @return 是否成功
     */
    int addObject(ObjectInfo objectInfo);

    /**
     * 添加对象（多个对象）
     *
     * @param objectInfos 人员对象列表
     * @return 是否成功
     */
    int addObjectByBatch(List <ObjectInfo> objectInfos);

    /**
     * 添加对象（带判空条件）
     *
     * @param objectInfo 人员对象
     * @return 是否成功
     */
    int addObjectSelective(ObjectInfo objectInfo);

    /**
     * 修改对象（单个对象）
     *
     * @param objectInfo 人员对象
     * @return 是否成功
     */
    int updateObjectSelective(ObjectInfo objectInfo);

    /**
     * 根据对象ID查询对象
     *
     * @param id 对象ID
     * @return 对象信息
     */
    ObjectInfo selectObjectById(String id);

    /**
     * 根据对象类型ID查询对象列表
     *
     * @param typeId 对象类型ID
     * @return 对象信息列表
     */
    List<ObjectInfo> selectObjectByTypeId(String typeId);

    /**
     * 通过ID列表批量查询对象
     *
     * @param idList ID列表
     * @return 对象信息列表
     */
    List<ObjectInfo> selectObjectByIdBatch(List <String> idList);

    /**
     * 查询对象库,一般用作基本的分页查询
     *
     * @return 对象信息列表
     */
    List<ObjectInfo> selectAllObject();

    /**
     * 查询对象库全部身份证信息
     *
     * @return 对象信息列表
     */
    List<String> selectAllObjectIdCard();

    /**
     * 根据参数字段过滤，获取相应对象信息
     *
     * @param param 过滤字段
     * @return 对象信息列表
     */
    List<ObjectInfo> searchObject(FilterField param);
}