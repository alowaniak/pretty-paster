package prettypaster;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * A {@link Prettifier} is able to "{@link #prettify(String) prettify}" content.
 */
@FunctionalInterface
public interface Prettifier {

    static Prettifier chainable(Prettifier first, Prettifier... rest) {
        Stream<Prettifier> chain = Stream.concat(Stream.of(first), Stream.of(rest));
        return in -> {
            String out = chain.reduce(in, (newIn, p) -> p.prettify(newIn).orElse(newIn), (a,b) -> { throw new IllegalStateException("Combining in serial stream.");} );
            return in.equals(out) ? Optional.empty() : Optional.of(out);
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
