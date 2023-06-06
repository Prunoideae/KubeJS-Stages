package moe.wolfgirl.kubejs_stages.content.recipe;

import dev.latvian.mods.kubejs.recipe.minecraft.ShapedRecipeJS;
import moe.wolfgirl.kubejs_stages.predicate.StagePredicate;

public class ShapedStagedRecipeJS extends ShapedRecipeJS {

    @Override
    public ShapedStagedRecipeJS stage(String s) {
        return stage(StagePredicate.of(s));
    }

    public ShapedStagedRecipeJS stage(StagePredicate stagePredicate) {
        this.json.add("stage", StagePredicate.toJson(stagePredicate));
        return this;
    }
}
