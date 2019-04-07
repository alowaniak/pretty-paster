package prettypaster;

import java.util.Optional;

/**
 * A {@link Prettifier} is able to "{@link #prettify(String) prettify}" content.
 */
@FunctionalInterface
public interface Prettifier {

    /**
     * "Prettifies" given input, meaning it will turn it into a nicer/more readable format.
     *
     * @param in the content to prettify
     * @return an {@link Optional} containing the prettified {@code content}.
     * Or an {@link Optional#empty() empty} if given {@code content} could not be prettified (by this Prettifier).
     */
    Optional<String> prettify(String in);
}
