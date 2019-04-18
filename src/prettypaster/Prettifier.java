package prettypaster;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * A {@link Prettifier} is able to "{@link #prettify(String) prettify}" content.
 */
@FunctionalInterface
public interface Prettifier {

    /**
     * @param prettifiers the prettifiers that are chain in given order
     * @return a {@link Prettifier} which "chains" the given {@code prettifiers},
     * meaning if an earlier prettifier prettifies input then its output will be passed as input to a later prettifier.
     * Note that if a prettifier can't prettify given input (which could be output of an earlier prettifier),
     * then the next prettifier will receive that input.
     */
    static Prettifier chain(Prettifier... prettifiers) {
        return in -> {
            AtomicReference<String> out = new AtomicReference<>();
            Stream.of(prettifiers).forEach(p -> p.prettify(out.get() == null ? in : out.get()).ifPresent(out::set));
            return Optional.ofNullable(out.get());
        };
    }

    /**
     * @param prettifiers the prettifiers of which (at most) one will prettify the content
     * @return a {@link Prettifier} whose result will be the result of a succesfull prettification
     * of one of the given {@code prettifiers}.
     */
    static Prettifier anyOf(Prettifier... prettifiers) {
        return in -> Stream.of(prettifiers).parallel()
                .map(it -> it.prettify(in)).filter(Optional::isPresent)
                .findAny().orElse(Optional.empty());
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
