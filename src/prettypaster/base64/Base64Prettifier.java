package prettypaster.base64;

import prettypaster.Prettifier;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Base64;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * "Prettifies" base64 by decoding it.
 */
public class Base64Prettifier implements Prettifier {

    private final Base64.Decoder b64Decoder = Base64.getDecoder();
    private final CharsetDecoder utf8Decoder = UTF_8.newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT);

    @Override public Optional<String> prettify(String in) {
        return Optional.of(in)
                .filter(this::hasAtLeast50Characters)
                .map(this::b64DecodedToUtf8Bytes)
                .map(this::utf8BytesToString);
    }

    private boolean hasAtLeast50Characters(String s) {
        return s.length() >= 50;
    }

    private byte[] b64DecodedToUtf8Bytes(String in) {
        try {
            return b64Decoder.decode(in.getBytes(UTF_8));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String utf8BytesToString(byte[] bytes) {
        try {
            return utf8Decoder.decode(ByteBuffer.wrap(bytes)).toString();
        } catch (CharacterCodingException e) {
            return null;
        }
    }
}
