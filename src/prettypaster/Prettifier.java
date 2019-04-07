package prettypaster;

import java.util.Optional;

@FunctionalInterface
public interface Prettifier {
    Optional<String> prettify(String in);
}
