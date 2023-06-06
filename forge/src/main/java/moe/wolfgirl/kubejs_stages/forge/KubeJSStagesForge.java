package moe.wolfgirl.kubejs_stages.forge;

import dev.architectury.platform.forge.EventBuses;
import moe.wolfgirl.kubejs_stages.KubeJSStages;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(KubeJSStages.MOD_ID)
public class KubeJSStagesForge {
    public KubeJSStagesForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(KubeJSStages.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        KubeJSStages.init();
    }
}