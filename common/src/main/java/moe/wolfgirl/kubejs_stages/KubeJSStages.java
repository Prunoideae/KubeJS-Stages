package moe.wolfgirl.kubejs_stages;

import moe.wolfgirl.kubejs_stages.content.recipe.registry.StagedRecipeEventHandler;

public class KubeJSStages {
    public static final String MOD_ID = "kubejs_stages";

    public static void init() {
        StagedRecipeEventHandler.init();
    }
}