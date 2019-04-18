package prettypaster;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrettifierTest {

    public static String newLined(String... lines) {
        return Arrays.stream(lines).collect(Collectors.joining(System.lineSeparator()));
    }

    @Nested
    class Chained {
        @Test void chainWithSinglePrettifierDoesntFunctionallyChangeFromSinglePrettifier() {
            var expectedOut = Optional.of(UUID.randomUUID().toString());

            var mock = new Prettifier() {
                String actualIn;

                @Override public Optional<String> prettify(String in) {
                    actualIn = in;
                    return expectedOut;
                }
            };

            var expectedIn = UUID.randomUUID().toString();
            assertEquals(expectedOut, Prettifier.chain(mock).prettify(expectedIn));
            assertEquals(expectedIn, mock.actualIn);
        }

        @Test void ifFirstPrettifierReturnsAnEmptyThenSecondPrettifierGetsOriginalInputAndItsResultIsChainResult() {
            String originalInput = UUID.randomUUID().toString();
            var expectedOut = Optional.of(UUID.randomUUID().toString());

            Prettifier first = s -> Optional.empty();
            var second = new Prettifier() {
                boolean calledWithOriginalInput;

                @Override public Optional<String> prettify(String in) {
                    if (in.equals(originalInput)) calledWithOriginalInput = true;
                    return expectedOut;
                }
            };

            var actualOut = Prettifier.chain(first, second).prettify(originalInput);
            assertEquals(expectedOut, actualOut);
            assertTrue(second.calledWithOriginalInput);
        }

        @Test void ifSecondPrettifierReturnsAnEmptyThenFirstPrettifiersResultIsChainResult() {
            var originalInput = UUID.randomUUID().toString();
            var expectedOut = Optional.of(UUID.randomUUID().toString());

            var first = new Prettifier() {
                boolean calledWithOriginalInput;

                @Override public Optional<String> prettify(String in) {
                    if (in.equals(originalInput)) calledWithOriginalInput = true;
                    return expectedOut;
                }
            };
            Prettifier second = s -> Optional.empty();

            var actualOut = Prettifier.chain(first, second).prettify(originalInput);
            assertEquals(expectedOut, actualOut);
            assertTrue(first.calledWithOriginalInput);
        }

        @Test void resultOfFirstPretifierIsChainedToSecondPrettifier() {
            var firstsResult = UUID.randomUUID().toString();
            Prettifier first = s -> Optional.of(firstsResult);
            var second = new Prettifier() {
                boolean calledWithFirstsResult;

                @Override public Optional<String> prettify(String in) {
                    if (in.equals(firstsResult)) calledWithFirstsResult = true;
                    return Optional.empty();
                }
            };

            Prettifier.chain(first, second).prettify("");
            assertTrue(second.calledWithFirstsResult);
        }

        @Test void usingChainMultipleTimesShouldWork() {
            Prettifier first = s -> Optional.of("  " + s + "  ");
            Prettifier second = s -> Optional.of(s.stripLeading());

            var chain = Prettifier.chain(first, second);
            assertEquals(Optional.of("foo  "), chain.prettify("foo"));
            assertEquals(Optional.of("foo  "), chain.prettify("foo"));
        }
    }

    @Nested
    class AnyOf {
        @Test void anyOfOneWillFunctionallyDOesntChange() {
            var expected = Optional.of(UUID.randomUUID().toString());
            Prettifier first = s -> expected;

            var any = Prettifier.anyOf(first);
            assertEquals(expected, any.prettify(UUID.randomUUID().toString()));
        }

        @Test void anyOfWhereNonePrettifyWillGiveEmpty() {
            var any = Prettifier.anyOf(s->Optional.empty());

            assertEquals(Optional.empty(), any.prettify(UUID.randomUUID().toString()));
        }

        @Test void anyOfTwoWhereOnlyOneReturnsWillGiveThatOneAndCanBeUsedAgain() {
            var expected = Optional.of(UUID.randomUUID().toString());

            Prettifier succesfulOne = s -> expected;
            Prettifier failingOne = s -> Optional.empty();

            var any = Prettifier.anyOf(failingOne, succesfulOne);
            assertEquals(expected, any.prettify(UUID.randomUUID().toString()));
        }

        @Test void anyOfMultipleReturningWillGiveAnyResult() {
            var result1 = Optional.of(UUID.randomUUID().toString());
            var result2 = Optional.of(UUID.randomUUID().toString());
            var result3 = Optional.of(UUID.randomUUID().toString());
            var result4 = Optional.of(UUID.randomUUID().toString());

            var anyExpected = List.of(result1, result2, result3, result4);
            var any = Prettifier.anyOf(s->result1, s->result2, s->result3, s->result4, s->Optional.empty());
            assertTrue(anyExpected.contains(any.prettify(UUID.randomUUID().toString())));
        }
    }
}
