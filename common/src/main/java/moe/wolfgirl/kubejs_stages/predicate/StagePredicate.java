package moe.wolfgirl.kubejs_stages.predicate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;

import java.util.*;
import java.util.function.Predicate;

public abstract class StagePredicate implements Predicate<Collection<String>> {
    public static final StagePredicate ALWAYS_TRUE = new StagePredicate() {
        @Override
        public boolean test(Collection<String> strings) {
            return true;
        }
    };

    public static StagePredicate fromJson(JsonObject jsonObject) {
        if (jsonObject.has("and")) {
            List<StagePredicate> predicates = new ArrayList<>();
            for (JsonElement element : jsonObject.getAsJsonArray("and")) {
                predicates.add(fromJson(element.getAsJsonObject()));
            }
            return new AndMatch(predicates);
        } else if (jsonObject.has("or")) {
            List<StagePredicate> predicates = new ArrayList<>();
            for (JsonElement element : jsonObject.getAsJsonArray("or")) {
                predicates.add(fromJson(element.getAsJsonObject()));
            }
            return new OrMatch(predicates);
        } else if (jsonObject.has("not")) {
            return new NotMatch(fromJson(jsonObject.getAsJsonObject("not")));
        } else if (jsonObject.has("stage")) {
            return new ExactMatch(jsonObject.get("stage").getAsString());
        } else {
            return ALWAYS_TRUE;
        }
    }

    @SuppressWarnings("unused")
    public static JsonObject toJson(StagePredicate stagePredicate) {
        JsonObject result = new JsonObject();
        if (stagePredicate instanceof AndMatch andMatch) {
            result.add("and", andMatch.getPredicates().stream().map(StagePredicate::toJson).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        } else if (stagePredicate instanceof OrMatch orMatch) {
            result.add("or", orMatch.getPredicates().stream().map(StagePredicate::toJson).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        } else if (stagePredicate instanceof NotMatch notMatch) {
            result.add("not", toJson(notMatch.getMatch()));
        } else if (stagePredicate instanceof ExactMatch exactMatch) {
            result.addProperty("stage", exactMatch.getMatch());
        }
        return result;
    }

    public static StagePredicate fromNetwork(FriendlyByteBuf buf) {
        return switch (buf.readByte()) {
            case 0 -> new ExactMatch(buf.readUtf());
            case 1 -> new NotMatch(fromNetwork(buf));
            case 2 -> new AndMatch(buf.readList(StagePredicate::fromNetwork));
            case 3 -> new OrMatch(buf.readList(StagePredicate::fromNetwork));
            default -> ALWAYS_TRUE;
        };
    }

    public static void toNetwork(FriendlyByteBuf buf, StagePredicate predicate) {
        if (predicate instanceof ExactMatch exactMatch) {
            buf.writeByte(0);
            buf.writeUtf(exactMatch.getMatch());
        } else if (predicate instanceof NotMatch notMatch) {
            buf.writeByte(1);
            toNetwork(buf, notMatch.getMatch());
        } else if (predicate instanceof AndMatch andMatch) {
            buf.writeByte(2);
            buf.writeCollection(andMatch.getPredicates(), StagePredicate::toNetwork);
        } else if (predicate instanceof OrMatch orMatch) {
            buf.writeByte(3);
            buf.writeCollection(orMatch.getPredicates(), StagePredicate::toNetwork);
        } else {
            buf.writeByte(4);
        }
    }

    public static StagePredicate of(Object o) {
        if (o instanceof CharSequence sequence) {
            return new ExactMatch(sequence.toString());
        } else if (o instanceof List<?> list) {
            return new AndMatch(list.stream().map(StagePredicate::of).toList());
        } else if (o instanceof Map<?, ?> map) {
            if (map.containsKey("not")) {
                return new NotMatch(of(map.get("not")));
            } else if (map.containsKey("and")) {
                return new AndMatch(((List<?>) map.get("and")).stream().map(StagePredicate::of).toList());
            } else if (map.containsKey("or")) {
                return new OrMatch(((List<?>) map.get("or")).stream().map(StagePredicate::of).toList());
            } else if (map.containsKey("stage")) {
                return new ExactMatch(map.get("stage").toString());
            }
        }
        return ALWAYS_TRUE;
    }
}
