package org.coode.oppl.template.commons;

import static org.coode.oppl.utils.ArgCheck.checkNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.coode.oppl.template.ReplacementStrategy;

/** Use this Strategy to combine several ones that use different place-holders.
 * The replacement will happen in the order specified by the user in the
 * constructor
 * 
 * @author Luigi Iannone
 * @param <I>
 *            type
 * @param <P>
 *            replacement type */
public class CombinedReplacementStrategy<I, P extends I> implements
        ReplacementStrategy<I, P> {
    private final List<ReplacementStrategy<I, P>> strategies = new ArrayList<>();

    /** @param strategies
     *            strategies */
    public CombinedReplacementStrategy(List<ReplacementStrategy<I, P>> strategies) {
        this.strategies.addAll(checkNotNull(strategies, "strategies"));
        if (strategies.isEmpty()) {
            throw new IllegalArgumentException(
                    "The list of the combined strategies cannot be empty");
        }
    }

    @Override
    public final P replace(I input) {
        P replaced = null;
        Iterator<ReplacementStrategy<I, P>> iterator = this.strategies.iterator();
        // There is at least one strategy.
        do {
            ReplacementStrategy<I, P> strategy = iterator.next();
            replaced = strategy.replace(replaced == null ? input : replaced);
        } while (iterator.hasNext());
        return replaced;
    }

    /** @return the strategies */
    public final List<ReplacementStrategy<I, P>> getStrategies() {
        return new ArrayList<>(this.strategies);
    }
}
