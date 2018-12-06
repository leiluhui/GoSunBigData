//package com.hzgc.service.collect.service;
//
//import com.hzgc.common.collect.facedis.FtpRegisterInfo;
//import com.hzgc.common.collect.facedis.RefreshDataCallBack;
//import com.hzgc.common.util.json.JacksonUtil;
//import com.hzgc.service.collect.dao.FtpInfoMapper;
//import com.hzgc.service.collect.model.FtpInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.apache.curator.framework.recipes.cache.ChildData;
//import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_ADDED;
//import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_UPDATED;
//
//@Slf4j
//public class FtpService1 implements RefreshDataCallBack {
//    @Autowired
//    private FtpInfoMapper ftpInfoMapper;
//    @Override
//    public void run(PathChildrenCacheEvent event) {
//      if (event.getType() == CHILD_ADDED){
//          FtpRegisterInfo registerInfo =
//                  JacksonUtil.toObject(new String(event.getData().getData()), FtpRegisterInfo.class);
//          boolean isExists_ftpname = isExits_ftpname(registerInfo);
//          if (isExists_ftpname){
//              log.info("ftpname already exists");
//          }
//          FtpInfo info = new FtpInfo();
//          info.setFtpIp(registerInfo.getProxyIP());
//          info.setProxyIp(registerInfo.getProxyIP());
//          info.setProxyPort(registerInfo.getProxyPort());
//          info.setFtpAccount(registerInfo.getFtpAccountName());
//          info.setFtpPassword(registerInfo.getFtpPassword());
//          info.setFtpHome(registerInfo.getFtpHomeName());
//          info.setFtpPort(registerInfo.getFtpPort());
//          Integer status = ftpInfoMapper.insertSelective(info);
//          if (status == 1){
//              log.info("insert info to t_ftpinfo successfully");
//          }else{
//              log.info("insert info to t_ftpinfo failed");
//          }
//      }
//      if (event.getType() == CHILD_UPDATED){
//          FtpRegisterInfo registerInfo =
//                  JacksonUtil.toObject(new String(event.getData().getData()), FtpRegisterInfo.class);
//          FtpInfo info = new FtpInfo();
//          info.setFtpIp(registerInfo.getProxyIP());
//          info.setProxyIp(registerInfo.getProxyIP());
//          info.setProxyPort(registerInfo.getProxyPort());
//          info.setFtpAccount(registerInfo.getFtpAccountName());
//          info.setFtpPassword(registerInfo.getFtpPassword());
//          info.setFtpHome(registerInfo.getFtpHomeName());
//          info.setFtpPort(registerInfo.getFtpPort());
//          Integer status = ftpInfoMapper.updateSelective(info);
//          if (status == 1){
//              log.info("update info to t_ftpinfo successfully");
//          }else{
//              log.info("update info to t_ftpinfo failed");
//          }
//      }
//    }
//    public void runAll(List<ChildData> childDataList) {
//     if (childDataList != null && childDataList.size() > 0) {
//         for (ChildData childData : childDataList) {
//             FtpRegisterInfo registerInfo =
//                     JacksonUtil.toObject(new String(childData.getData()), FtpRegisterInfo.class);
//             FtpInfo info = ftpInfoMapper.selectSelective(registerInfo);
//             if (isExists_ftpip(registerInfo) || isExits_ftpname(registerInfo) ){
//                 log.info("Ftpip already exists or Ftpname already exists");
//             }
//             info.setFtpIp(registerInfo.getProxyIP());
//             info.setProxyIp(registerInfo.getProxyIP());
//             info.setProxyPort(registerInfo.getProxyPort());
//             info.setFtpAccount(registerInfo.getFtpAccountName());
//             info.setFtpPassword(registerInfo.getFtpPassword());
//             info.setFtpHome(registerInfo.getFtpHomeName());
//             info.setFtpPort(registerInfo.getFtpPort());
//             Integer status = ftpInfoMapper.insertSelective(info);
//             if (status == 1){
//                 log.info("insert info to t_ftpinfo successfully");
//             }else{
//                 log.info("insert info to t_ftpinfo failed");
//             }
//         }
//     }
//    }
//
//    private boolean isExists_ftpip(FtpRegisterInfo registerInfo){
//        FtpInfo info = ftpInfoMapper.searchsame(registerInfo);
//        if (StringUtils.isNotBlank(info.getFtpIp())){
//            if (registerInfo.getFtpIPAddress().equals(info.getFtpIp())){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean isExits_ftpname(FtpRegisterInfo registerInfo){
//        FtpInfo info = ftpInfoMapper.searchsame(registerInfo);
//        if (StringUtils.isNotBlank(info.getFtpHome())){
//            if (registerInfo.getFtpHomeName().equals(info.getFtpHome())){
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
