package moe.wolfgirl.kubejs_stages.content.recipe;

import moe.wolfgirl.kubejs_stages.predicate.StagePredicate;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.CraftingContainer;

public class RecipeLogicClient {
    public static boolean canCraft(CraftingContainer inventory, StagePredicate predicate) {
        assert Minecraft.getInstance().player != null;
        return predicate.test(Minecraft.getInstance().player.kjs$getStages().getAll());
    }
}
