package moe.wolfgirl.kubejs_stages.predicate;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AndMatch extends StagePredicate {
    private final List<StagePredicate> predicates;

    public AndMatch(List<StagePredicate> predicates) {
        this.predicates = predicates;
    }


    @Override
    public boolean test(Collection<String> strings) {
        return predicates.stream().allMatch(predicate -> predicate.test(strings));
    }

    public List<StagePredicate> getPredicates() {
        return predicates;
    }
}
