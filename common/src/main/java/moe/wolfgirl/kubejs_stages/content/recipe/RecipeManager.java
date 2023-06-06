package moe.wolfgirl.kubejs_stages.content.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RecipeManager {
    private static final Set<String> EMPTY_STAGE = Set.of();
    public static final Map<String, Set<String>> CONTAINER_MAP = new HashMap<>();
    public static final Map<String, Set<String>> PREFIX_MAP = new HashMap<>();
    private static final Map<String, Set<String>> CACHE = new HashMap<>();

    public static Set<String> getContainerStages(String container) {
        return CACHE.computeIfAbsent(container, c -> {
            var byClass = CONTAINER_MAP.getOrDefault(container, EMPTY_STAGE);
            for (Map.Entry<String, Set<String>> entry : PREFIX_MAP.entrySet()) {
                String key = entry.getKey();
                Set<String> stages = entry.getValue();
                if (container.startsWith(key)) byClass.addAll(stages);
            }
            return byClass;
        });
    }
}
