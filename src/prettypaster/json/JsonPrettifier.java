package prettypaster.json;

import com.google.gson.*;
import prettypaster.Prettifier;

import java.util.Optional;

public class JsonPrettifier implements Prettifier {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser parser = new JsonParser();

    @Override public Optional<String> prettify(String in) {
        return Optional.of(in)
                .map(this::parse)
                .map(gson::toJson)
                .map(s->s.replace("\n", System.lineSeparator())); //Gson always prints only linefeed
    }

    private JsonElement parse(String s) {
        try {
            return parser.parse(s);
        } catch (JsonParseException e) {
            return null;
        }
    }
}
