package leetcode;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

class MaxContiguousSubArrayTest
        implements WithAssertions {

    static Stream<Arguments> arguments() {
        return Stream.of(
                of(new int[]{2, 1, 4, 3}, 2, 3, 3),
                of(new int[]{2, 1, 4, 3}, 1, 3, 5),
                of(new int[]{2, 1, 3, 3}, 2, 3, 5)
        );
    }

    int maxContiguousSubArray(
            final int[] values,
            final int lowerLimit,
            final int upperLimit) {
        final var length = values.length;
        int arrayCount = 0;
        for (int i = 0, j = i+1; i < length; i++) {
            if (values[i] >= lowerLimit && values[i] <= upperLimit) {
                arrayCount++;
                while(j < length) {
                    if (values[j] >= lowerLimit && values[j] <= upperLimit) {
                        arrayCount++;
                    }
                    j++;
                }
            }
        }
        return arrayCount;
    }

    @ParameterizedTest(name = "maxContiguousSubArray({0} {1} {2}) should return {3}")
    @MethodSource("arguments")
    void test(
            final int[] input,
            final int lowerLimit,
            final int upperLimit,
            final int expected) {
        assertThat(maxContiguousSubArray(input, lowerLimit, upperLimit)).isEqualTo(expected);
    }
}
