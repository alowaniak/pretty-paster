package prettypaster.xml;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlPrettifierTest {

    private final XmlPrettifier xmlPrettifier = new XmlPrettifier();

    @Test void returnsAnEmpty_whenGivenInvalidXml() {
        expectEmptyFor("");
        expectEmptyFor("<validTag><invalidNotClosedTag></validTag>");
    }

    private void expectEmptyFor(String in) {
        assertEquals(Optional.empty(), xmlPrettifier.prettify(in));
    }

    @Test void formatsXml_with_newLines_and_indenting2Spaces() {
        expect(
                "<xml><foobar><test>hi</test><test/></foobar></xml>", newLined(
                        "<xml>",
                        "  <foobar>",
                        "    <test>hi</test>",
                        "    <test/>",
                        "  </foobar>",
                        "</xml>",
                        ""));
    }

    private String newLined(String... lines) {
        return Arrays.stream(lines).collect(Collectors.joining(System.lineSeparator()));
    }

    private void expect(String in, String expectation) {
        assertEquals(expectation, xmlPrettifier.prettify(in).get());
    }
}
