package moe.wolfgirl.kubejs_stages.mixin;

import dev.latvian.mods.kubejs.recipe.ingredientaction.IngredientAction;
import dev.latvian.mods.kubejs.recipe.special.ShapelessKubeJSRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = ShapelessKubeJSRecipe.class, remap = false)
public interface ShapelessRecipeAccessor {
    @Accessor
    List<IngredientAction> getIngredientActions();
}
