package moe.wolfgirl.kubejs_stages.content.recipe.registry;

import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import moe.wolfgirl.kubejs_stages.KubeJSStages;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

// A mirrored class of KubeJS shaped/shapeless recipes registry
public class StagedRecipeEventHandler {

    public static Supplier<RecipeSerializer<?>> SHAPED;
    public static Supplier<RecipeSerializer<?>> SHAPELESS;

    public static void init() {
        if (!CommonProperties.get().serverOnly)
            registry();
    }

    private static void registry() {
        SHAPED = KubeJSRegistries.recipeSerializers().register(new ResourceLocation(KubeJSStages.MOD_ID, "shaped"), ShapedStagedRecipe.Serializer::new);
        SHAPELESS = KubeJSRegistries.recipeSerializers().register(new ResourceLocation(KubeJSStages.MOD_ID, "shapeless"), ShapelessStagedRecipe.Serializer::new);
    }
}
