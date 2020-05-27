package leetcode;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                of(new int[]{}, 1, false),
                of(new int[]{}, 0, false),
                of(new int[]{1, 2, 3}, 0, false),
                of(new int[]{1, 2, 3, 1}, 3, true),
                of(new int[]{1, 0, 1, 1}, 1, true),
                of(new int[]{1, 2, 3, 1, 2, 3}, 2, false), // Map.of(1, List.of(0,3)
                of(new int[]{1, 2, 3, 4, 5, 6}, 1, false)
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

    boolean containsNearbyDuplicate(int[] nums, int k) {

        HashMap<Integer, List<Integer>> map = new HashMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {

            if (!map.containsKey(nums[i])) {
                ArrayList<Integer> indexes = new ArrayList<>(1);
                indexes.add(i);
                map.put(nums[i], indexes);
            } else {
                List<Integer> currentIndexes = map.get(nums[i]);
                List<Integer> indexes = new ArrayList<>(currentIndexes.size() + 1);
                indexes.addAll(currentIndexes);
                indexes.add(i);
                map.put(nums[i], indexes);

                for (List<Integer> idxs : map.values()) {
                    if (idxs.size() < 2) {
                        continue;
                    }
                    for (int idx = 1; idx < idxs.size(); idx++) {
                        if (idxs.get(idx) - idxs.get(idx - 1) <= k) {
                            return true;
                        }
                    }

                }
            }
        }

        return false;
    }
}
