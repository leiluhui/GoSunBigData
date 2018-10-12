package com.hzgc.collect.service.parser;

import com.hzgc.collect.config.CollectContext;
import com.hzgc.collect.service.parser.impl.*;

public class FtpPathBootStrap {
    private Parser boxParser;
    private Parser daHuaParser_hfw5238M;
    private Parser daHuaParser_hdbw5238R;
    private Parser daHuaParser_hf8600E;
    private Parser daHuaParser_hf81230E;
    private Parser daHuaParser_itc302_rf2d;
    private Parser hiKvision_ds_2cd2t26fwd_i8s;
    private Parser daHuaParser_hf8229F;

    public FtpPathBootStrap(CollectContext collectContext) {
        boxParser = new BoxParser(collectContext);
        daHuaParser_hfw5238M = new DaHuaParser_HFW5238M_AS_I1(collectContext);
        daHuaParser_hdbw5238R = new DaHuaParser_HDBW5238R_AS(collectContext);
        daHuaParser_hf8600E = new DaHuaParser_HF8600E(collectContext);
        daHuaParser_hf81230E = new DaHuaParser_HF81230E(collectContext);
        daHuaParser_hf8229F = new DaHuaParser_HF8229F(collectContext);
        daHuaParser_itc302_rf2d = new DaHuaParser_ITC302_RF2D(collectContext);
        hiKvision_ds_2cd2t26fwd_i8s = new HikVisionParser_DS_2CD2T26FWD_I8S(collectContext);
    }

    public Parser getParser(String fileName) {
        if (fileName.contains(DeviceModel.DAHUA_HFW5238M)) {
            return daHuaParser_hfw5238M.canParse(fileName) ? daHuaParser_hfw5238M : null;
        } else if (fileName.contains(DeviceModel.DAHUA_HDBW5238R)) {
            return daHuaParser_hdbw5238R.canParse(fileName) ? daHuaParser_hdbw5238R : null;
        } else if (fileName.contains(DeviceModel.DAHUA_HF8600E)){
            return daHuaParser_hf8600E.canParse(fileName) ? daHuaParser_hf8600E : null;
        } else if (fileName.contains(DeviceModel.DAHUA_ITC302_RF2D)){
            return daHuaParser_itc302_rf2d.canParse(fileName) ? daHuaParser_itc302_rf2d : null;
        } else if (fileName.contains(DeviceModel.HIKVISION_DS_2CD2T26FWD_I8S)){
            return hiKvision_ds_2cd2t26fwd_i8s.canParse(fileName) ? hiKvision_ds_2cd2t26fwd_i8s : null;
        } else if (fileName.contains(DeviceModel.DAHUA_HF8229F)){
            return daHuaParser_hf8229F.canParse(fileName) ? daHuaParser_hf8229F : null;
        } else if (fileName.contains(DeviceModel.DAHUA_HF81230E)) {
            return daHuaParser_hf81230E.canParse(fileName) ? daHuaParser_hf81230E : null;
        }
        return boxParser.canParse(fileName) ? boxParser : null;
    }
}
