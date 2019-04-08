package prettypaster;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PrettifierTest {

    @Test void chainableWithSinglePrettifierDoesntFunctionallyChangeFromSinglePrettifier() {
        var expectedOut = Optional.of(UUID.randomUUID().toString());

        var mock = new Prettifier() {
            String actualIn;
            @Override public Optional<String> prettify(String in) {
                actualIn = in;
                return expectedOut;
            }
        };

        var expectedIn = UUID.randomUUID().toString();
        assertEquals(expectedOut, Prettifier.chainable(mock).prettify(expectedIn));
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

        var actualOut = Prettifier.chainable(first, second).prettify(originalInput);
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

        var actualOut = Prettifier.chainable(first, second).prettify(originalInput);
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

        Prettifier.chainable(first, second).prettify("");
        assertTrue(second.calledWithFirstsResult);
    }
}
