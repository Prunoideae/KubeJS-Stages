package moe.wolfgirl.kubejs_stages.mixin;

import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.ShapedKubeJSRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = ShapedKubeJSRecipe.class, remap = false)
public interface ShapedRecipeAccessor {
    @Accessor
    boolean getMirror();

    @Accessor
    List<IngredientAction> getIngredientActions();
}
