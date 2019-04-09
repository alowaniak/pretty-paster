package prettypaster;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * A {@link Prettifier} is able to "{@link #prettify(String) prettify}" content.
 */
@FunctionalInterface
public interface Prettifier {

    static Prettifier chainable(Prettifier... prettifiers) {
        return in -> {
            AtomicReference<String> out = new AtomicReference<>();
            Stream.of(prettifiers).forEach(p -> p.prettify(out.get() == null ? in : out.get()).ifPresent(out::set));
            return Optional.ofNullable(out.get());
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
