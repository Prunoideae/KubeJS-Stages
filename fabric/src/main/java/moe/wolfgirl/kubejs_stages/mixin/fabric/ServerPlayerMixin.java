package moe.wolfgirl.kubejs_stages.mixin.fabric;

import moe.wolfgirl.kubejs_stages.content.dimension.DimensionManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {
    @Inject(method = "changeDimension", at = @At("HEAD"))
    private void onChangedDimension(ServerLevel serverLevel, CallbackInfoReturnable<Entity> cir) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        if (!DimensionManager.canEnter(serverLevel.dimension(), player)) {
            cir.cancel();
        }
    }
}
