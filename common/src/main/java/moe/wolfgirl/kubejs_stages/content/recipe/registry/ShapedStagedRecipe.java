package moe.wolfgirl.kubejs_stages.content.recipe.registry;

import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.recipe.ModifyRecipeResultCallback;
import dev.latvian.mods.kubejs.recipe.RecipesEventJS;
import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.ShapedKubeJSRecipe;
import dev.latvian.mods.kubejs.util.UtilsJS;
import moe.wolfgirl.kubejs_stages.Utils;
import moe.wolfgirl.kubejs_stages.content.recipe.RecipeLogicClient;
import moe.wolfgirl.kubejs_stages.content.recipe.RecipeLogicServer;
import moe.wolfgirl.kubejs_stages.mixin.ShapedRecipeAccessor;
import moe.wolfgirl.kubejs_stages.predicate.StagePredicate;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;

public class ShapedStagedRecipe extends ShapedKubeJSRecipe {

    private final StagePredicate stage;

    public ShapedStagedRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result, boolean mirror, List<IngredientAction> ingredientActions, ModifyRecipeResultCallback modifyResult, StagePredicate stage) {
        super(id, group, width, height, ingredients, result, mirror, ingredientActions, modifyResult);
        this.stage = stage;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return StagedRecipeEventHandler.SHAPED.get();
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        if (Boolean.FALSE.equals(Utils.runSided(() -> () -> RecipeLogicClient.canCraft(container, stage), () -> () -> RecipeLogicServer.canCraft(container, stage)))) {
            return ItemStack.EMPTY;
        }
        return super.assemble(container);
    }

    public static class Serializer implements RecipeSerializer<ShapedStagedRecipe> {
        private static final RecipeSerializer<ShapedRecipe> SHAPED = UtilsJS.cast(KubeJSRegistries.recipeSerializers().get(new ResourceLocation("crafting_shaped")));

        @Override
        public ShapedStagedRecipe fromJson(ResourceLocation id, JsonObject json) {
            var shaped = SHAPED.fromJson(id, json);
            var mirror = GsonHelper.getAsBoolean(json, "mirror", true);
            var shrink = GsonHelper.getAsBoolean(json, "shrink", true);

            var key = ShapedRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            var pattern = ShapedRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern"));

            if (shrink) {
                pattern = ShapedRecipe.shrink(pattern);
            }
            int w = pattern[0].length(), h = pattern.length;
            var ingredients = ShapedRecipe.dissolvePattern(pattern, key, w, h);
            var ingredientActions = IngredientAction.parseList(json.get("kubejs_actions"));

            ModifyRecipeResultCallback modifyResult = null;
            if (json.has("kubejs_modify_result")) {
                modifyResult = RecipesEventJS.modifyResultCallbackMap.get(UUIDTypeAdapter.fromString(json.get("kubejs_modify_result").getAsString()));
            }

            var stage = StagePredicate.fromJson(json.get("stage").getAsJsonObject());

            return new ShapedStagedRecipe(id, shaped.getGroup(), w, h, ingredients, shaped.getResultItem(), mirror, ingredientActions, modifyResult, stage);
        }

        @Override
        public ShapedStagedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            var shaped = SHAPED.fromNetwork(id, buf);
            var mirror = buf.readBoolean();
            var ingredientActions = IngredientAction.readList(buf);
            var stage = StagePredicate.fromNetwork(buf);

            var group = shaped.getGroup();
            var width = shaped.getWidth();
            var height = shaped.getHeight();
            var ingredients = shaped.getIngredients();
            var result = shaped.getResultItem();

            return new ShapedStagedRecipe(id, group, width, height, ingredients, result, mirror, ingredientActions, null, stage);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShapedStagedRecipe r) {
            SHAPED.toNetwork(buf, r);
            buf.writeBoolean(((ShapedRecipeAccessor) r).getMirror());
            IngredientAction.writeList(buf, ((ShapedRecipeAccessor) r).getIngredientActions());
            StagePredicate.toNetwork(buf, r.stage);
        }
    }
}
