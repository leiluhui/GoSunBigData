package com.hzgc.service.imsi.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hzgc.common.service.api.bean.DetectorQueryDTO;
import com.hzgc.common.service.api.service.PlatformService;
import com.hzgc.common.service.imsi.ImsiInfo;
import com.hzgc.service.imsi.dao.ImsiDao;
import com.hzgc.service.imsi.dao.ImsiInfoMapper;
import com.hzgc.service.imsi.model.ImsiVO;
import com.hzgc.service.imsi.model.SearchImsiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImsiService {

    @Autowired
    private ImsiDao imsiDao;

    @Autowired
    private ImsiInfoMapper imsiInfoMapper;

    @Autowired
    private PlatformService platformService;

    public List <ImsiInfo> queryByTime(Long time) {
        return imsiDao.queryByTime(time);
    }

    public List<ImsiVO> searchIMSI(SearchImsiDTO searchImsiDTO) {
        List<ImsiVO> imsiVOList = new ArrayList<>();
        Page page = PageHelper.offsetPage(searchImsiDTO.getStart(), searchImsiDTO.getLimit(), true);
        List<ImsiInfo> imsiInfoList = imsiInfoMapper.searchIMSI(searchImsiDTO);
        for (ImsiInfo imsiInfo : imsiInfoList){
            ImsiVO imsiVO = new ImsiVO();
            imsiVO.setCellid(imsiInfo.getCellid());
            imsiVO.setControlsn(imsiInfo.getControlsn());
            imsiVO.setImsi(imsiInfo.getImsi());
            imsiVO.setLac(imsiInfo.getLac());
            imsiVO.setTime(imsiInfo.getTime());
            List<String> idList = new ArrayList<>();
            idList.add(imsiInfo.getControlsn());
            DetectorQueryDTO detectorQueryDTO = platformService.getImsiDeviceInfoByBatchId(idList).get(imsiInfo.getControlsn());
            if (detectorQueryDTO != null){
                imsiVO.setCommunityName(detectorQueryDTO.getCommunity());
                imsiVO.setRegionName(detectorQueryDTO.getRegion());
            }
            imsiVO.setDeviceName(platformService.getImsiDeviceName(imsiInfo.getControlsn()));
            imsiVOList.add(imsiVO);
        }
        return imsiVOList;
    }
}
