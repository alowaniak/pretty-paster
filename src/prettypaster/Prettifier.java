package prettypaster;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@link Prettifier} is able to "{@link #prettify(String) prettify}" content.
 */
@FunctionalInterface
public interface Prettifier {

    static Prettifier chainable(Prettifier... chain) {
        if(chain.length == 0) throw new IllegalArgumentException();
        return in -> {
            var curIn = new AtomicReference<String>();
            for (Prettifier p : chain) {
                p.prettify(curIn.get() == null ? in : curIn.get()).ifPresent(curIn::set);
            }
            return Optional.ofNullable(curIn.get());
        };
    }

    /**
     * "Prettifies" given input, meaning it will turn it into a nicer/more readable format or decode it.
     *
     * @param in the content to prettify
     * @return an {@link Optional} containing the prettified {@code content}.
     * Or an {@link Optional#empty() empty} if given {@code content} could not be prettified (by this Prettifier).
     */
    Optional<String> prettify(String in);
}
