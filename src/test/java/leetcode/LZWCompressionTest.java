package leetcode;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * https://youtu.be/1KzUikIae6k
 */
class LZWCompressionTest
        implements WithAssertions {

    static Stream<Arguments> arguments() {
        return Stream.of(of("""
                abababa abababa abababa abababa abababa abababa
                """, List.of(97, 98, 256, 258, 32, 258, 257, 257, 260, 262, 259, 261, 266, 265, 263, 267, 257, 10)), of("""
                abababa abababa abababa abababa abababa abababa abababa
                """, List.of(97, 98, 256, 258, 32, 258, 257, 257, 260, 262, 259, 261, 266, 265, 263, 267, 270, 269, 97, 10)), of("""
                abababa abababa abababa abababa abababa abababa abababa abababa
                """, List.of(97, 98, 256, 258, 32, 258, 257, 257, 260, 262, 259, 261, 266, 265, 263, 267, 270, 269, 97, 264, 267, 10)));
    }

    @ParameterizedTest(name = "encode({0}) should return {1}")
    @MethodSource("arguments")
    void test(
            final String string,
            final List<Integer> encoded) {
        assertThat(new LWZCompressor().encode(string)).isEqualTo(encoded);
        assertThat(new LWZCompressor().decode(encoded)).isEqualTo(string);
    }

    static class LWZCompressor {
        public static final int INITIAL_DICTIONARY_SIZE = 256;
        public final Map<String, Integer> ENCODING_DICTIONARY = new HashMap<>(INITIAL_DICTIONARY_SIZE * 2);
        public final Map<Integer, String> DECODING_DICTIONARY = new HashMap<>(INITIAL_DICTIONARY_SIZE * 2);

        public LWZCompressor() {
            IntStream.range(0, INITIAL_DICTIONARY_SIZE - 1).forEach(code -> {
                ENCODING_DICTIONARY.put(String.valueOf((char) code), code);
                DECODING_DICTIONARY.put(code, String.valueOf((char) code));
            });
        }

        public List<Integer> encode(final String string) {
            var dictionarySize = INITIAL_DICTIONARY_SIZE;
            final var dictionary = new HashMap<>(ENCODING_DICTIONARY);
            var foundChars = "";
            final var result = new ArrayList<Integer>(string.length());
            for (final char character : string.toCharArray()) {
                final var charsToAdd = foundChars + character;
                if (dictionary.containsKey(charsToAdd)) {
                    foundChars = charsToAdd;
                } else {
                    result.add(dictionary.get(foundChars));
                    dictionary.put(charsToAdd, dictionarySize++);
                    foundChars = String.valueOf(character);
                }
            }
            if (!foundChars.isEmpty()) {
                result.add(dictionary.get(foundChars));
            }
            return result;
        }

        public String decode(final List<Integer> input) {
            var dictionarySize = INITIAL_DICTIONARY_SIZE;
            final var encodedText = new ArrayList<>(input);
            final var dictionary = new HashMap<>(DECODING_DICTIONARY);
            var characters = String.valueOf((char) encodedText.remove(0).intValue());
            final var result = new StringBuilder(encodedText.size()).append(characters);
            for (final int code : encodedText) {
                final var entry = dictionary.containsKey(code)
                        ? dictionary.get(code)
                        : characters + characters.charAt(0);
                result.append(entry);
                dictionary.put(dictionarySize++, characters + entry.charAt(0));
                characters = entry;
            }
            return result.toString();
        }
    }
}
