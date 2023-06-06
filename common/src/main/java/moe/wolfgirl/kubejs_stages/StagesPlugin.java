package moe.wolfgirl.kubejs_stages;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeTypesEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import moe.wolfgirl.kubejs_stages.content.recipe.ShapedStagedRecipeJS;
import moe.wolfgirl.kubejs_stages.content.recipe.ShapelessStagedRecipeJS;
import moe.wolfgirl.kubejs_stages.predicate.StagePredicate;
import net.minecraft.resources.ResourceLocation;

public class StagesPlugin extends KubeJSPlugin {

    @Override
    public void registerEvents() {
        StagesEvents.GROUP.register();
    }

    @Override
    public void registerRecipeTypes(RegisterRecipeTypesEvent event) {
        event.register(new ResourceLocation(KubeJSStages.MOD_ID, "shaped"), ShapedStagedRecipeJS::new);
        event.register(new ResourceLocation(KubeJSStages.MOD_ID, "shapeless"), ShapelessStagedRecipeJS::new);
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        // Register StagePredicate as a simple type wrapper, not using string to avoid conflicts with KubeJS stage(string)
        typeWrappers.registerSimple(StagePredicate.class, o -> !(o instanceof String), StagePredicate::of);
    }
}
