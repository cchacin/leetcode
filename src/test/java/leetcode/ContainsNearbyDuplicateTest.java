package leetcode;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashSet;
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
class ContainsNearbyDuplicateTest implements WithAssertions {

    static Stream<Arguments> arguments() {
        return Stream.of(
                of(new int[]{}, 1, false),
                of(new int[]{}, 0, false),
                of(new int[]{1, 2, 3}, 0, false),
                of(new int[]{1, 2, 3, 1}, 3, true),
                of(new int[]{1, 0, 1, 1}, 1, true),
                of(new int[]{1, 2, 3, 1, 2, 3}, 2, false),
                of(new int[]{1, 2, 3, 4, 5, 6}, 1, false)
        );
    }

    @ParameterizedTest(name = "containsNearbyDuplicate({0}, {1}) should return {2}")
    @MethodSource("arguments")
    void test(
            final int[] input,
            final int k,
            final boolean expected) {
        assertThat(containsNearbyDuplicate(input, k)).isEqualTo(expected);
    }

    boolean containsNearbyDuplicate(final int[] nums, final int k) {

        final var set = new HashSet<Integer>();
        for (var i = 0; i < nums.length; ++i) {
            if (set.contains(nums[i])) return true;
            set.add(nums[i]);
            if (set.size() > k) {
                set.remove(nums[i - k]);
            }
        }
        return false;
    }
}
