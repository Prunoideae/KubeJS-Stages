package moe.wolfgirl.kubejs_stages.content.dimension.forge;

import moe.wolfgirl.kubejs_stages.content.dimension.DimensionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DimensionEventsForge {
    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player)
            if (!DimensionManager.canEnter(event.getDimension(), player)) {
                event.setCanceled(true);
            }
    }
}
