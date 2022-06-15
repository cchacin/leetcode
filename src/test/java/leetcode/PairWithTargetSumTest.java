package leetcode;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Given an array of sorted numbers and a target sum, find a pair in the
 * array whose sum is equal to the given target.
 * <p>
 * Write a function to return the indices of the two numbers (i.e. the pair)
 * such that they add up to the given target.
 * <p>
 * Example 1:
 * <p>
 * Input: [1, 2, 3, 4, 6], target=6
 * Output: [1, 3]
 * Explanation: The numbers at index 1 and 3 add up to 6: 2+4=6
 * <p>
 * Example 2:
 * <p>
 * Input: [2, 5, 9, 11], target=11
 * Output: [0, 2]
 * Explanation: The numbers at index 0 and 2 add up to 11: 2+9=11
 */
class PairWithTargetSumTest implements WithAssertions {

    static Stream<TestCase> arguments() {
        return Stream.of(
                TestCase.of(new int[]{1, 2, 3, 4, 6}, 6, new int[]{1, 3}),
                TestCase.of(new int[]{2, 5, 9, 11}, 11, new int[]{0, 2}),
                TestCase.of(new int[]{2, 5, 8, 11}, 11, new int[]{})
        );
    }

    record TestCase(int[]array, int sum, int[]expected) {
        static TestCase of(final int[] array, final int sum, final int[] expected) {
            return new TestCase(array, sum, expected);
        }

        int[] targetSum() {
            final int length = array.length;
            for (int i = 0; i < length; i++) {
                for (int j = length - 1; j > 0; j--) {
                    if (array[i] + array[j] == sum()) {
                        return new int[]{i, j};
                    }
                }
            }
            return new int[]{};
        }
    }

    @ParameterizedTest(name = "targetSum({0}, {1}) should return {2}")
    @MethodSource("arguments")
    void test(
            final TestCase testCase) {
        assertThat(testCase.targetSum()).isEqualTo(testCase.expected);
    }
}
