package moe.wolfgirl.kubejs_stages.predicate;

import java.util.Collection;
import java.util.Set;

public class ExactMatch extends StagePredicate {
    private final String match;

    public ExactMatch(String match) {
        this.match = match;
    }


    @Override
    public boolean test(Collection<String> strings) {
        return strings.contains(match);
    }

    public String getMatch() {
        return match;
    }
}
