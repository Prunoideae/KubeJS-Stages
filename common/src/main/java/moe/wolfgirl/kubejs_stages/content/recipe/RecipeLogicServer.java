package moe.wolfgirl.kubejs_stages.content.recipe;

import dev.latvian.mods.kubejs.bindings.UtilsWrapper;
import moe.wolfgirl.kubejs_stages.predicate.StagePredicate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;

public class RecipeLogicServer {
    public static boolean canCraft(CraftingContainer inventory, StagePredicate predicate) {
        MinecraftServer server = UtilsWrapper.getServer();
        if (server == null) return false;
        PlayerList manager = server.getPlayerList();
        AbstractContainerMenu container = inventory.menu;
        if (container == null) return false;
        ServerPlayer foundPlayer = null;
        for (ServerPlayer player : manager.getPlayers()) {
            if (player.containerMenu == container && container.stillValid(player)) {
                foundPlayer = player;
                break;
            }
        }
        if (foundPlayer != null)
            return predicate.test(foundPlayer.kjs$getStages().getAll());
        return predicate.test(RecipeManager.getContainerStages(container.getClass().getName()));
    }
}
