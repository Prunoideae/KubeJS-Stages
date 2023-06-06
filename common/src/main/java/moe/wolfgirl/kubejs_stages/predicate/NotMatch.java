package moe.wolfgirl.kubejs_stages.predicate;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class NotMatch extends StagePredicate {
    private final StagePredicate match;

    public NotMatch(StagePredicate match) {
        this.match = match;
    }

    @Override
    public boolean test(Collection<String> strings) {
        return !match.test(strings);
    }

    public StagePredicate getMatch() {
        return match;
    }
}
