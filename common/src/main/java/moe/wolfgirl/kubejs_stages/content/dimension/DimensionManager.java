package moe.wolfgirl.kubejs_stages.content.dimension;

import moe.wolfgirl.kubejs_stages.predicate.StagePredicate;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Map;

public class DimensionManager {
    public static Map<ResourceLocation, StagePredicate> DIMENSION_STAGES;

    public static StagePredicate getStage(ResourceLocation dimension) {
        return DIMENSION_STAGES.getOrDefault(dimension, StagePredicate.ALWAYS_TRUE);
    }

    public static boolean canEnter(ResourceKey<Level> level, ServerPlayer player) {
        return getStage(level.location()).test(player.kjs$getStages().getAll());
    }
}
