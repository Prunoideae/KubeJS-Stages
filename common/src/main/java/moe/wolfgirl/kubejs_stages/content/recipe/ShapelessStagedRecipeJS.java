package moe.wolfgirl.kubejs_stages.content.recipe;

import dev.latvian.mods.kubejs.recipe.minecraft.ShapelessRecipeJS;
import moe.wolfgirl.kubejs_stages.predicate.StagePredicate;

public class ShapelessStagedRecipeJS extends ShapelessRecipeJS {

    @Override
    public ShapelessStagedRecipeJS stage(String s) {
        return stage(StagePredicate.of(s));
    }

    public ShapelessStagedRecipeJS stage(StagePredicate stagePredicate) {
        this.json.add("stage", StagePredicate.toJson(stagePredicate));
        return this;
    }
}
