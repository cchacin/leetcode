package leetcode;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

/**
 * https://leetcode.com/problems/contains-duplicate-ii/
 * <p>
 * Given an array of integers and an integer k, find out whether there are two distinct indices i and j in the array
 * such that nums[i] = nums[j] and the absolute difference between i and j is at most k.
 * <p>
 * Example 1:
 * <p>
 * Input: nums = [1,2,3,1], k = 3
 * Output: true
 * <p>
 * Example 2:
 * <p>
 * Input: nums = [1,0,1,1], k = 1
 * Output: true
 * <p>
 * Example 3:
 * <p>
 * Input: nums = [1,2,3,1,2,3], k = 2
 * Output: false
 */
class ContainsNearbyDuplicate implements WithAssertions {

    static Stream<Arguments> arguments() {
        return Stream.of(
                of(new int[]{1, 2, 3, 1}, 3, true),
                of(new int[]{1, 0, 1, 1}, 1, true),
                of(new int[]{1, 2, 3, 1, 2, 3}, 2, false)
        );
    }

    @ParameterizedTest(name = "containsNearbyDuplicate({0}, {1}) should return {2}")
    @MethodSource("arguments")
    void test(
            int[] input,
            int k,
            boolean expected) {
        assertThat(containsNearbyDuplicate(input, k)).isEqualTo(expected);
    }

    // TODO improve
    boolean containsNearbyDuplicate(int[] nums, int k) {

        for (int i = 0; i < nums.length; i++) {

            for (int j = i + 1; j < nums.length; j++) {

                if (nums[i] == nums[j] && j - i <= k) {
                    return true;
                }
            }
        }

        return false;
    }
}
