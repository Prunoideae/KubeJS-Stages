package moe.wolfgirl.kubejs_stages.fabric;

import moe.wolfgirl.kubejs_stages.KubeJSStages;
import net.fabricmc.api.ModInitializer;

public class KubeJSStagesFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        KubeJSStages.init();
    }
}