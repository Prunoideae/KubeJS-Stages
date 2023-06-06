package moe.wolfgirl.kubejs_stages.content.recipe.registry;

import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.ShapelessKubeJSRecipe;
import dev.latvian.mods.kubejs.util.UtilsJS;
import moe.wolfgirl.kubejs_stages.Utils;
import moe.wolfgirl.kubejs_stages.content.recipe.RecipeLogicClient;
import moe.wolfgirl.kubejs_stages.content.recipe.RecipeLogicServer;
import moe.wolfgirl.kubejs_stages.mixin.ShapelessRecipeAccessor;
import moe.wolfgirl.kubejs_stages.predicate.StagePredicate;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import java.util.List;

public class ShapelessStagedRecipe extends ShapelessKubeJSRecipe {
    private final StagePredicate stage;

    public ShapelessStagedRecipe(ShapelessRecipe original, List<IngredientAction> ingredientActions, ModifyRecipeResultCallback modifyResult, StagePredicate stage) {
        super(original, ingredientActions, modifyResult);
        this.stage = stage;
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        if (Boolean.FALSE.equals(Utils.runSided(() -> () -> RecipeLogicClient.canCraft(container, stage), () -> () -> RecipeLogicServer.canCraft(container, stage)))) {
            return ItemStack.EMPTY;
        }
        return super.assemble(container);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return StagedRecipeEventHandler.SHAPELESS.get();
    }

    public static class Serializer implements RecipeSerializer<ShapelessStagedRecipe> {
        private static final RecipeSerializer<ShapelessRecipe> SHAPELESS = UtilsJS.cast(KubeJSRegistries.recipeSerializers().get(new ResourceLocation("crafting_shapeless")));

        @Override
        public ShapelessStagedRecipe fromJson(ResourceLocation resourceLocation, JsonObject json) {
            //Still need to sync stage because somebody might want it to show in JEI
            var shapeless = SHAPELESS.fromJson(resourceLocation, json);
            var ingredientActions = IngredientAction.parseList(json.get("kubejs_actions"));
            ModifyRecipeResultCallback modifyResult = null;
            if (json.has("kubejs_modify_result")) {
                modifyResult = RecipesEventJS.modifyResultCallbackMap.get(UUIDTypeAdapter.fromString(json.get("kubejs_modify_result").getAsString()));
            }
            var stage = StagePredicate.fromJson(json.get("stage").getAsJsonObject());
            return new ShapelessStagedRecipe(shapeless, ingredientActions, modifyResult, stage);
        }

        @Override
        public ShapelessStagedRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            var shapeless = SHAPELESS.fromNetwork(resourceLocation, friendlyByteBuf);
            var ingredientActions = IngredientAction.readList(friendlyByteBuf);
            var stage = StagePredicate.fromNetwork(friendlyByteBuf);
            return new ShapelessStagedRecipe(shapeless, ingredientActions, null, stage);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, ShapelessStagedRecipe recipe) {
            SHAPELESS.toNetwork(friendlyByteBuf, recipe);
            IngredientAction.writeList(friendlyByteBuf, ((ShapelessRecipeAccessor) recipe).getIngredientActions());
            StagePredicate.toNetwork(friendlyByteBuf, recipe.stage);
        }
    }
}
