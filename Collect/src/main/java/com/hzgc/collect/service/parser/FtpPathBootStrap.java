package com.hzgc.collect.service.parser;

import com.hzgc.collect.config.CollectContext;

public class FtpPathBootStrap {
    private Parser boxParser;
    private Parser daHuaParser_hfw5238M;
    private Parser daHuaParser_hdbw5238R;
    private Parser daHuaParser_hf8600E;
    private Parser daHuaParser_hf81230E;

    public FtpPathBootStrap(CollectContext collectContext) {
        boxParser = new BoxParser(collectContext);
        daHuaParser_hfw5238M = new DaHuaParser_HFW5238M(collectContext);
        daHuaParser_hdbw5238R = new DaHuaParser_HDBW5238R(collectContext);
        daHuaParser_hf8600E = new DaHuaParser_HF8600E(collectContext);
        daHuaParser_hf81230E = new DaHuaParser_HF81230E(collectContext);
    }

    public Parser getParser(String fileName) {
        if (fileName.contains(DeviceModel.DAHUA_HFW5238M)) {
            return daHuaParser_hfw5238M.canParse(fileName) ? daHuaParser_hfw5238M : null;
        } else if (fileName.contains(DeviceModel.DAHUA_HDBW5238R)) {
            return daHuaParser_hdbw5238R.canParse(fileName) ? daHuaParser_hdbw5238R : null;
        } else if (fileName.contains(DeviceModel.DAHUA_HF8600E)){
            return daHuaParser_hf8600E.canParse(fileName) ? daHuaParser_hf8600E : null;
        } else if (fileName.contains(DeviceModel.DAHUA_HF81230E)) {
            return daHuaParser_hf81230E.canParse(fileName) ? daHuaParser_hf81230E : null;
        }
        return boxParser.canParse(fileName) ? boxParser : null;
    }
}
