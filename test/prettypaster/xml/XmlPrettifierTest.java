package prettypaster.xml;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static prettypaster.PrettifierTest.newLined;

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
                "<xml><foobar><test>hi</test><test/></foobar></xml>",
                newLined(
                        "<xml>",
                        "  <foobar>",
                        "    <test>hi</test>",
                        "    <test/>",
                        "  </foobar>",
                        "</xml>",
                        ""
                )
        );
    }

    @Test void removesWhitespace() {
        expect(
                newLined(
                        "       <xml>",
                        "  ",
                        "      <foobar> ",
                        "   <test>    hi   </test></foobar>   ",
                        "",
                        "</xml>"),
                newLined(
                        "<xml>",
                        "  <foobar>",
                        "    <test>    hi   </test>",
                        "  </foobar>",
                        "</xml>",
                        ""
                )
        );
    }

    private void expect(String in, String expectation) {
        assertEquals(expectation, xmlPrettifier.prettify(in).get());
    }
}
