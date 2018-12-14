package com.hzgc.datasyncer.plugin;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.ScriptPlugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.script.*;

import javax.script.ScriptEngine;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class VectorScoringPlugin extends Plugin implements ScriptPlugin, ActionPlugin {
    private final static Logger LOGGER = LogManager.getLogger(VectorScoringPlugin.class);

    public VectorScoringPlugin() {
        super();
        LOGGER.warn("Create the Basic Plugin and installed it into elasticsearch");
    }

    @Override
    public List<RestHandler> getRestHandlers(Settings settings, RestController restController, ClusterSettings clusterSettings, IndexScopedSettings indexScopedSettings, SettingsFilter settingsFilter, IndexNameExpressionResolver indexNameExpressionResolver, Supplier<DiscoveryNodes> nodesInCluster) {
        return Collections.singletonList(null);
    }

    @Override
    public List<NativeScriptFactory> getNativeScripts() {
        return Collections.singletonList(new MyNativeScriptFactory());
    }

    public static class MyNativeScriptFactory implements NativeScriptFactory {
        private final static Logger LOGGER = LogManager.getLogger(MyNativeScriptFactory.class);

        @Override
        public ExecutableScript newScript(@Nullable Map<String, Object> params) {
            LOGGER.info("MyNativeScriptFactory  run new Script ");
            String featureStr = params == null ? null : XContentMapValues.nodeStringValue(params.get("feature"), null);
            if (featureStr == null) {
                LOGGER.error("Missing the field parameter ");
            }
            return new MyScript(featureStr);
        }

        @Override
        public boolean needsScores() {
            return false;
        }

        @Override
        public String getName() {
            return "my_native_script";
        }
    }

    public static class MyScript extends AbstractDoubleSearchScript {

        private final static Logger LOGGER = LogManager.getLogger(MyScript.class);
        private final String featureStr;

        public MyScript(String featureStr) {
            this.featureStr = featureStr;
        }

        @Override
        public double runAsDouble() {
            LOGGER.info("my run As begining ");
            String strSrcFeature = (String) source().get("feature");
            int nLen1 = featureStr.length();
            int nLen2 = strSrcFeature.length();
            if (nLen1 == nLen2) {
                return 99.9;
            }
            if (nLen1 < nLen2) {
                return 66.6;
            } else {
                return 33.3;
            }
        }
    }
}
