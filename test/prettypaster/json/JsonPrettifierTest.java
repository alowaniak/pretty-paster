package prettypaster.json;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static prettypaster.PrettifierTest.newLined;

class JsonPrettifierTest {

    @Test void returnsAnEmpty_whenGivenInvalidJson() {
        assertEquals(Optional.empty(), new JsonPrettifier().prettify("'this' : 'is no json'"));
    }

    @Test void formatsJson_with_newLines_and_indenting2Spaces() {
        assertEquals(
                Optional.of(newLined(
                        "{",
                        "  \"name\": \"John\",",
                        "  \"age\": 30,",
                        "  \"car\": {",
                        "    \"wheels\": 4",
                        "  }",
                        "}"
                )),
                new JsonPrettifier().prettify(newLined(
                        "{ \"name\":\"John\", \"age\":30, \"car\": {",
                        "     ",
                        "              ",
                        "\"wheels\": 4 }",
                        "  ",
                        "     }"
                ))
        );
    }

}
