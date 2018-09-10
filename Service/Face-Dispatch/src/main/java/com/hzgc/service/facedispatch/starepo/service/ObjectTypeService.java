package com.hzgc.service.facedispatch.starepo.service;

import com.github.pagehelper.PageHelper;
import com.hzgc.service.facedispatch.starepo.dao.ObjectInfoMapper;
import com.hzgc.service.facedispatch.starepo.dao.ObjectTypeMapper;
import com.hzgc.service.facedispatch.starepo.model.ObjectInfo;
import com.hzgc.service.facedispatch.starepo.model.ObjectType;
import com.hzgc.service.facedispatch.starepo.model.ObjectTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ObjectTypeService {

    @Autowired
    private ObjectTypeMapper objectTypemapper;

    @Autowired
    private ObjectInfoMapper objectInfoMapper;

    /**
     * 添加objectType
     *
     * @param param add objectType对象
     * @return
     */
    public int addObjectType(ObjectType param) {
        return objectTypemapper.addObjectTypeSelective(param);
    }

    public boolean isExists_objectTypeName(String name) {
        List<ObjectType> list = objectTypemapper.selectAllObjectType();
        for (ObjectType objectType : list){
            if (StringUtils.isNotBlank(objectType.getName()))
                if (name.equals(objectType.getName()))
                    return true;
        }
        return false;
    }

    /**
     * 删除objectType
     *
     * @param idList 类型Key列表
     * @return
     */
    public int deleteObjectType(List<String> idList) {
        for (String typeId : idList){
            List<ObjectInfo> infos = objectInfoMapper.selectObjectByTypeId(typeId);
            if (infos != null && infos.size() > 0){
                log.info("Object info exists under this object type, so can't delete this object type : " + typeId);
                return 0;
            }
        }
        return objectTypemapper.deleteObjectTypeByIdBatch(idList);
    }

    /**
     * 修改ObjectType
     *
     * @param param update objectType对象
     * @return boolean
     */
    public int updateObjectType(ObjectType param) {
        return objectTypemapper.updateObjectTypeById(param);
    }

    /**
     * 查询objectType
     *
     * @param start 查询起始位置
     * @param end  查询结束位置
     * @return List<ObjectTypeParam>
     */
    public List<ObjectTypeVO> searchObjectType(int start, int end) {
        PageHelper.offsetPage(start, end);
        List<ObjectType> list = objectTypemapper.selectAllObjectType();
        List<ObjectTypeVO> voList = new ArrayList<>();
        for (ObjectType type : list){
            ObjectTypeVO vo = ObjectTypeVO.objectTypeShift(type);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 统计对象类型数量
     *
     * @return 对象类型数量
     */
    public int countObjectType() {
        return objectTypemapper.selectAllObjectType().size();
    }

    /**
     * 查询objectTypeName
     *
     * @param idList 对象类型key数组
     * @return Map
     */
    public Map<String, String> searchObjectTypeNames(List<String> idList){
        List<ObjectType> list = objectTypemapper.selectObjectTypeByIdBatch(idList);
        Map<String, String> map = new HashMap<>();
        for (ObjectType type : list){
            if (type != null){
                map.put(type.getId(), type.getName());
            }
        }
        return map;
    }
}
