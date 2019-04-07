package prettypaster.base64;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Base64PrettifierTest {

    private final Base64Prettifier base64Prettifier = new Base64Prettifier();

    @Test void returnsAnEmpty_when_inputShorter_than_50_Characters() {
        var shorterThan50 = "A".repeat(48);
        expectEmptyFor(shorterThan50);
    }

    @Test void returnsAnEmpty_when_inputContains_non_base64_character() {
        var containingNonBase64 = "A".repeat(49) + " ";
        expectEmptyFor(containingNonBase64);
    }

    @Test void returns_theInput_decoded() {
        var quickBrownFoxInBase64 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
        assertEquals(
                Optional.of("The quick brown fox jumps over the lazy dog."),
                base64Prettifier.prettify(quickBrownFoxInBase64)
        );
    }

    @Test void returnsAnEmpty_when_input_decoded_is_not_valid_or_mappable_utf_8() {
        var jibberishBase64 = "0123456789ABCDEF".repeat(10);
        expectEmptyFor(jibberishBase64);
    }

    private void expectEmptyFor(String shorterThan50) {
        assertEquals(Optional.empty(), base64Prettifier.prettify(shorterThan50));
    }
}
